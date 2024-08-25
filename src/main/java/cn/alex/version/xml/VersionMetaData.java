package cn.alex.version.xml;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.VersionComparator;

/**
 * 版本信息元数据
 *
 * @author Alex
 * @date 2024/8/24 01:44
 */
public class VersionMetaData {
    public static final List<VersionXml> VERSION_LIST = new ReadVersionXml().read();

    /**
     * 获取Xml最新版本
     */
    public static VersionXml getNewestVersion() {
        return VERSION_LIST.stream()
            .sorted(Comparator.comparing(VersionXml::getVersion).reversed())
            .collect(Collectors.toList())
            .get(0);
    }

    /**
     * 获取本机版本需要升级的版本
     */
    public static List<VersionXml> getUpgradeVersion(String version) {
        if (CollUtil.isEmpty(VERSION_LIST)) {
            return ListUtil.empty();
        }

        List<VersionXml> upgradeVersionList = VERSION_LIST.stream().filter(item ->
            VersionComparator.INSTANCE.compare(item.getVersion(), version) > 0
        ).collect(Collectors.toList());

        if (CollUtil.isEmpty(upgradeVersionList)) {
            return ListUtil.empty();
        }

        return upgradeVersionList.stream()
            .sorted(Comparator.comparing(VersionXml::getVersion))
            .collect(Collectors.toList());
    }
}
