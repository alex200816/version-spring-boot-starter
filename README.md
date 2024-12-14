# 单机应用自动升级工具

## 介绍
以往部署单机版应用时，用户常常面临手动更新SQL和JAR包等繁琐操作，这不仅要求用户具备一定的技术背景，还容易在升级过程中出错，导致应用无法正常运行。为了解决这一难题，推出了这款专为单机应用设计的升级工具。

## 特点
**单机版设计，灵活便捷**：
本工具专为单机应用量身打造，无需网络连接即可轻松使用。无论您身处何地，只要集成本工具，就能随时随地进行软件升级，无需担心网络环境的限制。

**无网升级，安全可靠**：
与传统的在线升级方式不同，本工具采用离线升级模式，无需依赖网络连接。您只需将旧版的JAR包替换为新版，即可完成升级操作，既安全又可靠，避免了网络不稳定或数据泄露的风险。

**简便易用，一键升级**：
我们深知用户对于升级过程的担忧和困扰，因此本工具无需用户操作任何步骤。只需将旧JAR替换的操作，即可完成整个升级过程，无需繁琐的设置或特殊技能，真正实现无感升级。

**智能检测，自动升级**：
本工具内置智能检测功能，能够自动检测当前安装的软件版本，并与加载的最新版本进行比对。一旦发现新版本，将自动执行升级操作，无需您手动干预，让您及时享受最新功能。

**数据升级，无缝衔接**：
为了确保软件升级后数据的完整性和一致性，本工具在每个版本升级时都会自动执行相应的自定义SQL脚本用于升级数据库。在升级过程中，工具会自动执行这些SQL脚本，确保数据库结构与软件版本相匹配，实现无缝衔接。

**完全开源**：
本工具采用完全开源的设计理念，让您能够自由定制和扩展其功能。无论您是开发者还是技术爱好者，都能轻松上手，根据自己的需求进行个性化定制，打造属于自己的专属升级工具。

## 集成

### 示例
[查看示例工程](https://gitee.com/alex200816/version-spring-boot-starter/tree/master/sample)

### 文件目录
~~~ bash
src
  |-- main
      |-- java
      |-- resources
          |-- version   # 主目录
              |-- sql   # 当前目录存放每个版本需要执行的SQL脚本
                  |-- 1.0.0.sql     # 文件名称需与version.xml定义的版本号相同
              |-- version.xml   # 当前文件存放各个版本信息，及升级需要执行的操作
~~~

### 使用说明

**1、maven引入依赖**
~~~ xml
<dependency>
    <groupId>cn.002alex</groupId>
    <artifactId>version-spring-boot-starter</artifactId>
    <version>1.0.3</version>
</dependency>
~~~

**2、启动类添加@EnableVersionUpdating注解**
~~~ java
@EnableVersionUpdating
@SpringBootApplication
public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

}
~~~

**3、application.yml配置示例**
~~~ yml
application-version:
    # 是否启用自动升级
    enable: true
    # 当前JAR包版本
    version: 1.0.0
    # 版本信息保存地址
    # 默认: {Jar路径}/config/version.config
    # 如果使用Jar同目录下的其他路径可以使用{user.dir}/server/config/version.config
    versionConfigPath: 
~~~

**4、version.xml配置示例**
> 当前文件存放位置请看 **`文件目录`**
~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<versions>
    <version>
        <!-- 版本号 -->
        <version>1.0.0</version>
        <!-- 发布时间 -->
        <releaseTime>2024-7-27 12:30:53</releaseTime>
        <!-- 升级说明 -->
        <description>
            1.新增某某某功能
            2.修复某某BUG
        </description>
        <!-- 执行顺序
             0.只更新jar，不更新SQL也不执行Java代码
             1.执行SQL
             2.执行JAVA方法
             3.先执行SQL脚本后执行JAVA方法
             4.先执行JAVA方法后执行SQL脚本
        -->
        <order>0</order>
        <!-- 自定义BeanName(可调用方法执行一些升级操作) 
             例: invokeTarget 或 updateUser
             注意：需实现cn.alex.version.execute.JavaExecuteService接口
        -->
        <invokeTarget></invokeTarget>
    </version>
    <version>
        <version>1.0.1</version>
        <releaseTime>2024-7-27 12:30:53</releaseTime>
        <description>
            1.新增某某某功能
            2.修复某某BUG
        </description>
    </version>
</versions>
~~~

**5、各版本SQL脚本配置示例**
> 需注意每条语句需加结束符 **`;`**，否则将出现无法预估的异常
~~~ mysql
update sys_user set pass_word = '123456' where user_name = '张三';
~~~

**6.每个版本执行回调**

ApplicationVersionCallback.java
~~~ java
import cn.alex.version.callback.VersionUpdatingCallback;
import cn.alex.version.callback.builder.VersionUpdatingCallbackBuilder;
import cn.alex.version.callback.builder.VersionUpdatingCallbackExceptionBuilder;
import cn.alex.version.callback.builder.VersionUpdatingCallbackStartBuilder;
import org.springframework.stereotype.Service;

/**
 * 版本升级回调
 *
 * @author Alex
 * @date 2024/8/31 04:54
 */
@Service
public class CustomCallBack implements VersionUpdatingCallback {
    @Override
    public void onStartCall(VersionUpdatingCallbackStartBuilder startBuilder) {
        System.out.println("开始执行版本升级");
    }

    @Override
    public void onUpdatingStartCall(VersionUpdatingCallbackBuilder updateBuilder) {
        System.out.println("开始执行一个版本的升级");
    }

    @Override
    public void onUpdatingEndCall(VersionUpdatingCallbackBuilder updateBuilder) {
        System.out.println("结束执行一个版本升级");
    }

    @Override
    public void onEndCall() {
        System.out.println("结束执行版本升级回调");
    }

    @Override
    public void onExceptionCall(VersionUpdatingCallbackExceptionBuilder exceptionBuilder) {
        System.out.println("异常回调");
    }
}
~~~


