package cn.alex.version.utils;

import java.util.Arrays;
import java.util.function.Predicate;

import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * @author Alex
 * @date 2024/8/29 12:07
 */
public class EnumUtil {


    /**
     * 通过 某字段对应值 获取 枚举，获取不到时为 {@code null}
     *
     * @param enumClass 枚举类
     * @param predicate 条件
     * @param <E>       枚举类型
     * @return 对应枚举 ，获取不到时为 {@code null}
     * @since 5.8.0
     */
    public static <E extends Enum<E>> E getBy(Class<E> enumClass, Predicate<? super E> predicate) {
        if(null == enumClass || null == predicate){
            return null;
        }
        return Arrays.stream(enumClass.getEnumConstants())
            .filter(predicate).findFirst().orElse(null);
    }

    /**
     * 通过 某字段对应值 获取 枚举，获取不到时为 {@code null}
     *
     * @param condition 条件字段，为{@code null}返回{@code null}
     * @param value     条件字段值
     * @param <E>       枚举类型
     * @param <C>       字段类型
     * @return 对应枚举 ，获取不到时为 {@code null}
     */
    public static <E extends Enum<E>, C> E getBy(Func1<E, C> condition, C value) {
        if (null == condition) {
            return null;
        }
        final Class<E> implClass = LambdaUtil.getRealClass(condition);
        return Arrays.stream(implClass.getEnumConstants())
            .filter(constant -> ObjUtil.equals(condition.callWithRuntimeException(constant), value))
            .findAny()
            .orElse(null);
    }

    /**
     * 通过 某字段对应值 获取 枚举，获取不到时为 {@code defaultEnum}
     *
     * @param <E>         枚举类型
     * @param <C>         字段类型
     * @param condition   条件字段
     * @param value       条件字段值
     * @param defaultEnum 条件找不到则返回结果使用这个
     * @return 对应枚举 ，获取不到时为 {@code null}
     * @since 5.8.8
     */
    public static <E extends Enum<E>, C> E getBy(Func1<E, C> condition, C value, E defaultEnum) {
        return ObjectUtil.defaultIfNull(getBy(condition, value), defaultEnum);
    }


}
