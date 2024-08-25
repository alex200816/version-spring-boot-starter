package cn.alex.version.exception;

/**
 * 读取XML异常
 *
 * @author Alex
 * @date 2024/8/24 02:20
 */
public class ReadXmlException extends RuntimeException {
    public ReadXmlException() {}

    public ReadXmlException(String message, Exception e) {
        super(message, e);
    }

    public ReadXmlException(String message) {
        super(message);
    }
}
