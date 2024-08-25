package cn.alex.version.exception;

/**
 * 版本异常
 *
 * @author Alex
 * @date 2024/8/23 23:37
 */
public class VersionException extends RuntimeException {
    public VersionException() {}

    public VersionException(String message, Exception e) {
        super(message, e);
    }

    public VersionException(String message) {
        super(message);
    }
}
