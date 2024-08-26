package cn.alex.version.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.alex.version.ApplicationVersionManagement;
import cn.alex.version.config.VersionUpdatingConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author Alex
 * @date 2024/8/26 21:27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({VersionUpdatingConfiguration.class, ApplicationVersionManagement.class})
@Documented
public @interface EnableVersionUpdating {
}
