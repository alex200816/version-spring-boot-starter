package cn.alex.version.callback;

import cn.alex.version.callback.builder.VersionUpdatingCallbackBuilder;
import cn.alex.version.callback.builder.VersionUpdatingCallbackExceptionBuilder;
import cn.alex.version.callback.builder.VersionUpdatingCallbackStartBuilder;

/**
 * 版本升级回调
 *
 * @author Alex
 * @date 2024/8/24 21:33
 */
public interface VersionUpdatingCallback {

    /**
     * 开始执行版本升级
     *
     * @param startBuilder 回调参数
     */
    void onStartCall(VersionUpdatingCallbackStartBuilder startBuilder);

    /**
     * 开始执行一个版本的升级
     *
     * @param updateBuilder 回调参数
     */
    void onUpdatingStartCall(VersionUpdatingCallbackBuilder updateBuilder);

    /**
     * 结束执行一个版本升级
     *
     * @param updateBuilder 回调参数
     */
    void onUpdatingEndCall(VersionUpdatingCallbackBuilder updateBuilder);

    /**
     * 结束执行版本升级回调
     */
    void onEndCall();

    /**
     * 异常回调
     * @param exceptionBuilder 回调参数
     */
    void onExceptionCall(VersionUpdatingCallbackExceptionBuilder exceptionBuilder);
}
