package cn.alex.sample.version;

import cn.alex.sample.entity.SysUser;
import cn.alex.sample.mapper.SysUserMapper;
import cn.alex.version.exception.ExecuteCodeException;
import cn.alex.version.execute.JavaExecuteService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Alex
 * @date 2024/8/26 19:36
 */
@Service("updateUser")
@RequiredArgsConstructor
public class UpdateUser implements JavaExecuteService {
    private final SysUserMapper userMapper;

    @Override
    public void execute() throws ExecuteCodeException {
        userMapper.update(new LambdaUpdateWrapper<SysUser>()
            .eq(SysUser::getUserName, "zhangsan")
            .set(SysUser::getPassWord, "zhangsan")
        );

        // /config/version.config内容中的版本号改为1.0.0, 注释放开可查看异常事务回滚
        // int i = 1 / 0;
    }
}
