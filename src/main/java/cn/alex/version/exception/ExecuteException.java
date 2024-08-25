package cn.alex.version.exception;

/**
 * 执行异常
 *
 * @author Alex
 * @date 2024/8/23 23:38
 */
public class ExecuteException extends RuntimeException {
    public ExecuteException() {}

    public ExecuteException(String message, Exception e) {
        super(message, e);
    }

    public ExecuteException(String message) {
        super(message);
    }
}
