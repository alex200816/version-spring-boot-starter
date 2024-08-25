package cn.alex.version.exception;

/**
 * 执行代码异常
 *
 * @author Alex
 * @date 2024/8/23 23:42
 */
public class ExecuteCodeException extends ExecuteException {
    public ExecuteCodeException() {}

    public ExecuteCodeException(String message, Exception e) {
        super(message, e);
    }

    public ExecuteCodeException(String message) {
        super(message);
    }
}
