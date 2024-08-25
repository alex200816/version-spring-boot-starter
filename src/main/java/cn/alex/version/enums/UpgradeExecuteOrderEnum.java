package cn.alex.version.enums;

import lombok.Getter;

/**
 * 升级执行顺序
 *
 * @author Alex
 * @date 2024/8/24 02:52
 */
@Getter
public enum UpgradeExecuteOrderEnum {

    /**
     * 只更新JAR
     */
    NONE(0),
    /**
     * 执行SQL脚本
     */
    SQL(1),
    /**
     * 执行JAVA方法
     */
    JAVA(2),
    /**
     * 先执行SQL脚本后执行JAVA方法
     */
    SQL_JAVA(3),
    /**
     * 先执行JAVA方法后执行SQL脚本
     */
    JAVA_SQL(4),
    ;

    Integer order;

    UpgradeExecuteOrderEnum(Integer order) {
        this.order = order;
    }
}
