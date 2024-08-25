package cn.alex.version.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import cn.alex.version.constant.VersionXmlConstant;
import cn.alex.version.exception.ReadXmlException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.XmlUtil;
import lombok.Cleanup;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 读取XML
 *
 * @author Alex
 * @date 2024/8/24 01:43
 */
public class ReadVersionXml {

    /**
     * 读取版本XML
     *
     * @return 版本集合
     */
    public List<VersionXml> read() {
        List<VersionXml> versionXmlList = null;
        try {
            @Cleanup
            InputStream fis = this.getClass().getClassLoader().getResourceAsStream(VersionXmlConstant.VERSION_XML_PATH);
            Document document = XmlUtil.readXML(IoUtil.readUtf8(fis));
            NodeList nodeList = XmlUtil.getNodeListByXPath("/versions/version", document);

            List<VersionXml> versionList = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node item = nodeList.item(i);
                versionList.add(XmlUtil.xmlToBean(item, VersionXml.class));
            }

            versionXmlList = versionSorted(versionList);
            setOldVersion(versionXmlList);
        } catch (Exception e) {
            throw new ReadXmlException("读取XML异常", e);
        }
        return versionXmlList;
    }

    /**
     * 将版本集合排序
     *
     * @param versionList 版本集合
     * @return 排序后的版本集合
     */
    private List<VersionXml> versionSorted(List<VersionXml> versionList) {
        return versionList.stream()
            .sorted(Comparator.comparing(VersionXml::getVersion))
            .collect(Collectors.toList());
    }

    /**
     * 设置上一个版本号
     *
     * @param versionList 版本集合
     */
    private void setOldVersion(List<VersionXml> versionList) {
        for (int i = 0; i < versionList.size(); i++) {
            if (i == 0) {
                versionList.get(i).setOldVersion(versionList.get(i).getVersion());
            } else {
                versionList.get(i).setOldVersion(versionList.get(i - 1).getVersion());
            }
        }
    }
}
