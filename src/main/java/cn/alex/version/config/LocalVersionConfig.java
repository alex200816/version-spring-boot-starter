package cn.alex.version.config;

import javax.annotation.PostConstruct;

import cn.hutool.core.io.FileUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * 本机版本配置
 *
 * @author Alex
 * @date 2024/8/24 00:48
 */
@Component
@DependsOn("VersionConfigPathHandle")
@RequiredArgsConstructor
public class LocalVersionConfig {
    private final ApplicationVersionProperties properties;

    /**
     * 本地版本号存储文件目录
     */
    private static String versionConfigPath;

    /**
     * 本地版本号
     */
    @Getter
    private static String localVersion;

    @PostConstruct
    public void init() {
        versionConfigPath = properties.getVersionConfigPath();
        if (FileUtil.exist(versionConfigPath)) {
            localVersion = FileUtil.readUtf8String(versionConfigPath).trim();
        } else {
            setLocalVersion(properties.getVersion());
        }
    }

    public static void setLocalVersion(String localVersion) {
        LocalVersionConfig.localVersion = localVersion;
        FileUtil.writeUtf8String(localVersion, versionConfigPath);
    }
}
