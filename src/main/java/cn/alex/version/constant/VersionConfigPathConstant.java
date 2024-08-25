package cn.alex.version.constant;

/**
 * @author Alex
 * @date 2024/8/24 01:17
 */
public class VersionConfigPathConstant {
    public static final String USER_DIR_REPLACE = "{user.dir}";
    public static final String USER_DIR = System.getProperty("user.dir");
    public static final String VERSION_FILE_NAME = "version.config";
    public static final String VERSION_PATH = USER_DIR + "/config/" + VERSION_FILE_NAME;
}
