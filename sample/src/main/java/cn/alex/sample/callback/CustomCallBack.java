package cn.alex.sample.callback;

import cn.alex.version.callback.VersionUpdatingCallback;
import cn.alex.version.callback.builder.VersionUpdatingCallbackBuilder;
import cn.alex.version.callback.builder.VersionUpdatingCallbackExceptionBuilder;
import cn.alex.version.callback.builder.VersionUpdatingCallbackStartBuilder;
import org.springframework.stereotype.Service;

/**
 * 版本升级回调
 *
 * @author Alex
 * @date 2024/8/31 04:54
 */
@Service
public class CustomCallBack implements VersionUpdatingCallback {
    @Override
    public void onStartCall(VersionUpdatingCallbackStartBuilder startBuilder) {
        System.out.println("开始执行版本升级");
    }

    @Override
    public void onUpdatingStartCall(VersionUpdatingCallbackBuilder updateBuilder) {
        System.out.println("开始执行一个版本的升级");
    }

    @Override
    public void onUpdatingEndCall(VersionUpdatingCallbackBuilder updateBuilder) {
        System.out.println("结束执行一个版本升级");
    }

    @Override
    public void onEndCall() {
        System.out.println("结束执行版本升级回调");
    }

    @Override
    public void onExceptionCall(VersionUpdatingCallbackExceptionBuilder exceptionBuilder) {
        System.out.println("异常回调");
    }
}
