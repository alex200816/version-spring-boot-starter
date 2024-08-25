package cn.alex.version.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 配置映射
 *
 * @author Alex
 * @date 2024/8/24 00:57
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "application-version")
public class ApplicationVersionProperties {

    /**
     * 是否启用
     */
    private boolean enable = false;

    /**
     * 当前Jar包版本
     */
    private String version;

    /**
     * 版本信息保存地址
     * 默认: {Jar路径}/config/version.config
     * 如果使用Jar同目录下的其他路径可以使用({user.dir}/server/config/version.config)
     */
    private String versionConfigPath;

}
