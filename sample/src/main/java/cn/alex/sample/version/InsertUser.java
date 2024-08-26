package cn.alex.sample.version;

import cn.alex.sample.entity.SysUser;
import cn.alex.sample.mapper.SysUserMapper;
import cn.alex.version.exception.ExecuteCodeException;
import cn.alex.version.execute.AbstractJavaExecution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Alex
 * @date 2024/8/26 19:35
 */
@Component
@RequiredArgsConstructor
    public class InsertUser extends AbstractJavaExecution {
        private final SysUserMapper userMapper;

    @Override
    public void run() throws ExecuteCodeException {
        userMapper.insert(new SysUser("zhangsan", "123456"));
    }
}
