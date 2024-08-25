package cn.alex.version.execute;

import java.sql.Connection;

import cn.alex.version.constant.VersionSqlConstant;
import cn.alex.version.exception.ExecuteSqlException;
import cn.alex.version.xml.VersionXml;
import cn.hutool.core.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;

/**
 * 执行SQL脚本
 *
 * @author Alex
 * @date 2024/7/28 下午9:36
 */
@Service
@RequiredArgsConstructor
public class SqlExecuteService {

    public void executeSqlScript(Connection connection, VersionXml versionXmlDTO) throws ExecuteSqlException {
        try {
            Resource resource = new ClassPathResource(
                VersionSqlConstant.VERSION_SQL_PATH + versionXmlDTO.getVersion() + VersionSqlConstant.SQL_FILE_SUFFIX
            );
            EncodedResource encodedResource = new EncodedResource(resource, CharsetUtil.UTF_8);
            ScriptUtils.executeSqlScript(connection, encodedResource);
        } catch (Exception e) {
            throw new ExecuteSqlException("执行SQL脚本异常", e);
        }
    }

}
