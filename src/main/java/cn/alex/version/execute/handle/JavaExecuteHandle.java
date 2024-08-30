package cn.alex.version.execute.handle;

import cn.alex.version.exception.ExecuteCodeException;
import cn.alex.version.xml.VersionXml;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * 执行Java代码
 *
 * @author Alex
 * @date 2024/7/28 下午11:40
 */
@Service
@RequiredArgsConstructor
public class JavaExecuteHandle {
    private final ApplicationContext applicationContext;

    public void executeJava(VersionXml versionXmlDTO) throws ExecuteCodeException {
        try {
            Object obj = applicationContext.getBean(versionXmlDTO.getInvokeTarget());
            obj.getClass().getDeclaredMethod("execute").invoke(obj);
        } catch (Exception e) {
            throw new ExecuteCodeException("执行Java代码异常", e);
        }
    }

}
