package cn.alex.sample.version;

import cn.alex.sample.entity.SysUser;
import cn.alex.sample.mapper.SysUserMapper;
import cn.alex.version.exception.ExecuteCodeException;
import cn.alex.version.execute.JavaExecuteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Alex
 * @date 2024/8/26 19:35
 */
@Service("insertUser")
@RequiredArgsConstructor
public class InsertUser implements JavaExecuteService {
    private final SysUserMapper userMapper;

    @Override
    public void execute() throws ExecuteCodeException {
        userMapper.insert(new SysUser("zhangsan", "123456"));
    }
}
