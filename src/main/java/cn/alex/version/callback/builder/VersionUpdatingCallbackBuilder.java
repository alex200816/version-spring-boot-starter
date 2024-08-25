package cn.alex.version.callback.builder;

import java.util.List;

import cn.alex.version.xml.VersionXml;
import lombok.Builder;
import lombok.Data;

/**
 * 执行版本升级 回调参数
 *
 * @author Alex
 * @date 2024/8/24 21:45
 */
@Data
@Builder
public class VersionUpdatingCallbackBuilder {

    /**
     * 版本列表
     */
    private List<VersionXml> versionList;

    /**
     * 当前升级版本
     */
    private String upgradeVersion;

    /**
     * 本地版本号
     */
    private String localVersion;

    /**
     * JAR包版本号
     */
    private String jarVersion;
}
