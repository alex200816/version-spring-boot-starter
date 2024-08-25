package cn.alex.version.execute;

import cn.alex.version.exception.ExecuteCodeException;
import cn.alex.version.xml.VersionXml;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.stereotype.Service;

/**
 * 执行Java代码
 *
 * @author Alex
 * @date 2024/7/28 下午11:40
 */
@Service
public class JavaExecuteService {

    public void executeJava(VersionXml versionXmlDTO) throws ExecuteCodeException {
        try {
            Object obj = SpringUtil.getBean(Class.forName(versionXmlDTO.getClassPath()));
            obj.getClass().getDeclaredMethod("run").invoke(obj);
        } catch (Exception e) {
            throw new ExecuteCodeException("执行Java代码异常", e);
        }
    }

}
