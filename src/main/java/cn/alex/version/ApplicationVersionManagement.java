package cn.alex.version;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import cn.alex.version.callback.VersionUpdatingCallback;
import cn.alex.version.callback.builder.VersionUpdatingCallbackBuilder;
import cn.alex.version.callback.builder.VersionUpdatingCallbackExceptionBuilder;
import cn.alex.version.callback.builder.VersionUpdatingCallbackStartBuilder;
import cn.alex.version.config.ApplicationVersionProperties;
import cn.alex.version.config.LocalVersionConfig;
import cn.alex.version.enums.UpgradeExecuteOrderEnum;
import cn.alex.version.exception.ExecuteException;
import cn.alex.version.exception.VersionException;
import cn.alex.version.execute.JavaExecuteService;
import cn.alex.version.execute.SqlExecuteService;
import cn.alex.version.xml.VersionMetaData;
import cn.alex.version.xml.VersionXml;
import cn.hutool.core.comparator.VersionComparator;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 版本管理器
 *
 * @author Alex
 * @date 2023/6/15 17:28
 */
@Component
@RequiredArgsConstructor
public class ApplicationVersionManagement implements ApplicationRunner, VersionUpdatingCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationVersionManagement.class);

    private final List<VersionUpdatingCallback> callbackList;
    private final ApplicationVersionProperties properties;
    private final DataSource dataSource;
    private final SqlExecuteService sqlExecute;
    private final JavaExecuteService javaExecute;

    /**
     * JAR的版本号
     */
    private String jarVersion;

    /**
     * 本地版本号（不会改变）
     */
    private String finalLocalVersion;

    /**
     * 本地版本号（随着升级改变）
     */
    private String localVersion;

    /**
     * 正在升级的版本
     */
    private String updatingVersion;

    @Override
    public void run(ApplicationArguments args) {
        if (!properties.isEnable()) {
            return;
        }

        jarVersion = properties.getVersion();
        finalLocalVersion = LocalVersionConfig.getLocalVersion();
        localVersion = finalLocalVersion;

        VersionXml newestVersion = VersionMetaData.getNewestVersion();
        if (newestVersion == null || !jarVersion.equals(newestVersion.getVersion())) {
            throw new VersionException("version.xml 最新版本与 JAR 版本不同");
        }

        try {
            onStartCall(VersionUpdatingCallbackStartBuilder.builder().build());
            if (VersionComparator.INSTANCE.compare(finalLocalVersion, jarVersion) == 0) {
                LOGGER.info("当前版本[{}]，为最新版本", jarVersion);
            } else if (VersionComparator.INSTANCE.compare(finalLocalVersion, jarVersion) < 0) {
                LOGGER.info("检测到有新版本，即将开始更新，当前版本[{}]，最新版本[{}]", finalLocalVersion, jarVersion);
                versionUpdating();
                LOGGER.info("应用 [{}] 至 [{}] 版本更新成功", finalLocalVersion, jarVersion);
            }
        } catch (Exception e) {
            onExceptionCall(VersionUpdatingCallbackExceptionBuilder.builder().exception(e).build());
            LOGGER.error("版本升级失败, 已升级版本[v{}]，最新版本[v{}]", localVersion, jarVersion, e);
        } finally {
            onEndCall();
        }
    }

    /**
     * 根据版本号自动执行更新
     */
    public void versionUpdating() throws Exception {
        if (VersionComparator.INSTANCE.compare(finalLocalVersion, jarVersion) > 0) {
            throw new VersionException(
                StrUtil.format(
                    "本机应用版本高于应用的最新版本，请检查本机应用版本是否有误，[当前版本：{} > 最新版本：{}]",
                    finalLocalVersion,
                    jarVersion
                )
            );
        }

        List<VersionXml> upgradeVersion = VersionMetaData.getUpgradeVersion(finalLocalVersion);
        for (VersionXml versionXml : upgradeVersion) {
            String newVersion = versionXml.getVersion();

            if (VersionComparator.INSTANCE.compare(versionXml.getOldVersion(), localVersion) == 0) {
                updatingVersion = newVersion;

                onUpdatingStartCall(VersionUpdatingCallbackBuilder.builder().build());
                execute(versionXml);
                onUpdatingStartCall(VersionUpdatingCallbackBuilder.builder().build());

                // 更新本地配置文件版本
                LocalVersionConfig.setLocalVersion(newVersion);
                localVersion = newVersion;
            }

            // 如果upgradeVersion的最新版本号等于jar包的版本号，则说明已更新到最新版本
            if (VersionComparator.INSTANCE.compare(newVersion, jarVersion) == 0) {
                break;
            }
        }
    }

    private void execute(VersionXml versionXml) throws Exception {
        Integer order = versionXml.getOrder();
        UpgradeExecuteOrderEnum orderEnum = EnumUtil.getBy(
            UpgradeExecuteOrderEnum.class,
            item -> order.equals(item.getOrder())
        );

        // 为空或为0则说明只更新JAR
        if (ObjectUtil.isEmpty(orderEnum) || orderEnum == UpgradeExecuteOrderEnum.NONE) {
            return;
        }

        @Cleanup
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        try {
            switch (orderEnum) {
                case SQL:
                    sqlExecute.executeSqlScript(connection, versionXml);
                    break;
                case JAVA:
                    javaExecute.executeJava(versionXml);
                    break;
                case SQL_JAVA:
                    sqlExecute.executeSqlScript(connection, versionXml);
                    javaExecute.executeJava(versionXml);
                    break;
                case JAVA_SQL:
                    javaExecute.executeJava(versionXml);
                    sqlExecute.executeSqlScript(connection, versionXml);
                    break;
                default:
                    break;
            }
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw new ExecuteException("执行Java代码或SQL脚本异常", e);
        }
    }

    @Override
    public void onStartCall(VersionUpdatingCallbackStartBuilder startBuilder) {
        startBuilder.setVersionList(VersionMetaData.VERSION_LIST);
        startBuilder.setJarVersion(jarVersion);
        startBuilder.setLocalVersion(finalLocalVersion);

        for (VersionUpdatingCallback callback : callbackList) {
            Class<? extends VersionUpdatingCallback> aClass = callback.getClass();
            if (aClass != this.getClass()) {
                callback.onStartCall(startBuilder);
            }
        }
    }

    @Override
    public void onUpdatingStartCall(VersionUpdatingCallbackBuilder updateBuilder) {
        updateBuilder.setVersionList(VersionMetaData.VERSION_LIST);
        updateBuilder.setUpgradeVersion(updatingVersion);
        updateBuilder.setJarVersion(jarVersion);
        updateBuilder.setLocalVersion(finalLocalVersion);

        for (VersionUpdatingCallback callback : callbackList) {
            Class<? extends VersionUpdatingCallback> aClass = callback.getClass();
            if (aClass != this.getClass()) {
                callback.onUpdatingStartCall(updateBuilder);
            }
        }
    }

    @Override
    public void onUpdatingEndCall(VersionUpdatingCallbackBuilder updateBuilder) {
        updateBuilder.setVersionList(VersionMetaData.VERSION_LIST);
        updateBuilder.setUpgradeVersion(updatingVersion);
        updateBuilder.setJarVersion(jarVersion);
        updateBuilder.setLocalVersion(finalLocalVersion);

        for (VersionUpdatingCallback callback : callbackList) {
            Class<? extends VersionUpdatingCallback> aClass = callback.getClass();
            if (aClass != this.getClass()) {
                callback.onUpdatingEndCall(updateBuilder);
            }
        }
    }

    @Override
    public void onEndCall() {
        for (VersionUpdatingCallback callback : callbackList) {
            Class<? extends VersionUpdatingCallback> aClass = callback.getClass();
            if (aClass != this.getClass()) {
                callback.onEndCall();
            }
        }
    }

    @Override
    public void onExceptionCall(VersionUpdatingCallbackExceptionBuilder exceptionBuilder) {
        exceptionBuilder.setVersionList(VersionMetaData.VERSION_LIST);
        exceptionBuilder.setUpgradeVersion(updatingVersion);
        exceptionBuilder.setJarVersion(jarVersion);
        exceptionBuilder.setLocalVersion(finalLocalVersion);

        for (VersionUpdatingCallback callback : callbackList) {
            Class<? extends VersionUpdatingCallback> aClass = callback.getClass();
            if (aClass != this.getClass()) {
                callback.onExceptionCall(exceptionBuilder);
            }
        }
    }
}
