package cn.alex.version.execute;

import cn.alex.version.exception.ExecuteCodeException;

/**
 * Java代码执行
 *
 * @author Alex
 * @date 2024/7/29 上午12:34
 */
public abstract class AbstractJavaExecution {

    /**
     * 执行代码
     *
     * @throws ExecuteCodeException 执行代码异常
     */
    public abstract void run() throws ExecuteCodeException;
}
