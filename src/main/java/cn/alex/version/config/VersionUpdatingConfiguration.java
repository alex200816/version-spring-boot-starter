package cn.alex.version.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alex
 * @date 2024/8/26 21:28
 */
@Configuration
@ComponentScan(basePackages = {
    "cn.alex.version.config",
    "cn.alex.version.callback",
    "cn.alex.version.execute",
    "cn.alex.version.xml"
})
public class VersionUpdatingConfiguration {
}
