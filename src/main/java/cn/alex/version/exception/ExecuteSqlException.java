package cn.alex.version.exception;

/**
 * 执行SQL异常
 *
 * @author Alex
 * @date 2024/8/23 23:42
 */
public class ExecuteSqlException extends ExecuteException {
    public ExecuteSqlException() {}

    public ExecuteSqlException(String message, Exception e) {
        super(message, e);
    }

    public ExecuteSqlException(String message) {
        super(message);
    }
}
