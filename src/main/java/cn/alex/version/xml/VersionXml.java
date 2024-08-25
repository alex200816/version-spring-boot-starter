package cn.alex.version.xml;

import java.util.Date;

import lombok.Data;

/**
 * 版本信息
 *
 * @author Alex
 * @date 2024/7/27 下午9:15
 */
@Data
public class VersionXml {

    /**
     * 版本号
     */
    private String version;

    /**
     * 上一个版本号（自动获取，无需在xml填写）
     */
    private String oldVersion;

    /**
     * 发布时间
     */
    private Date releaseTime;

    /**
     * 升级说明
     * 每个版本的升级说明
     */
    private String description;

    /**
     * 类路径(可调用方法执行一些升级操作)
     * 例: com.hcr.data.submission.ApplicationStandAlone
     */
    private String classPath;

    /**
     * 执行顺序
     * 0.只更新JAR代码
     * 1.执行SQL脚本
     * 2.执行JAVA方法
     * 3.先执行SQL脚本后执行JAVA方法
     * 4.先执行JAVA方法后执行SQL脚本
     */
    private Integer order;
}
