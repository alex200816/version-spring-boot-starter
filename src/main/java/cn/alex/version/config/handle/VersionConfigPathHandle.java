package cn.alex.version.config.handle;

import javax.annotation.PostConstruct;

import cn.alex.version.config.ApplicationVersionProperties;
import cn.alex.version.constant.VersionConfigPathConstant;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 版本信息文件地址处理
 *
 * @author Alex
 * @date 2024/8/24 01:03
 */
@Component("VersionConfigPathHandle")
@RequiredArgsConstructor
public class VersionConfigPathHandle {
    private final ApplicationVersionProperties properties;

    @PostConstruct
    public void init() {
        String versionConfigPath = properties.getVersionConfigPath();
        if (StrUtil.isNotBlank(versionConfigPath)) {
            if (StrUtil.contains(versionConfigPath, VersionConfigPathConstant.USER_DIR_REPLACE)) {
                properties.setVersionConfigPath(
                    StrUtil.replace(versionConfigPath,
                        VersionConfigPathConstant.USER_DIR_REPLACE,
                        VersionConfigPathConstant.USER_DIR
                    )
                );
            }
        } else {
            properties.setVersionConfigPath(VersionConfigPathConstant.VERSION_PATH);
        }
    }

}
