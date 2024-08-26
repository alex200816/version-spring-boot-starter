# version-spring-boot-starter

## 介绍
1. 无网应用自动升级
2. 每个版本升级时支持运行SQL脚本和Java代码，Java代码中支持操作数据库
3. 执行SQL脚本或Java代码支持异常事务回滚

## 文件目录
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

## 使用说明
### 1、maven引入依赖
~~~ xml
<dependency>
    <groupId>cn.002alex</groupId>
    <artifactId>version-spring-boot-starter</artifactId>
    <version>1.0.2</version>
</dependency>
~~~

### 2、启动类添加@EnableVersionUpdating注解
~~~ java
@EnableVersionUpdating
@SpringBootApplication
public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

}
~~~

### 3、application.yml配置示例
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

### 4、version.xml配置示例
###### 当前文件存放位置请看 **`文件目录`**
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
        <!-- 类路径(可调用方法执行一些升级操作) 
             例: cn.xxx.version.1.0.0.UpdateUser
             注意：类必须添加@Component注解，需继承cn.alex.version.execute.AbstractJavaExecution，否则事务可能会失效
        -->
        <classPath></classPath>
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

### 5、各版本SQL脚本配置示例
###### 需注意每条语句需加结束符 **`;`**，否则将出现无法预估的异常
~~~ mysql
update sys_user set pass_word = '123456' where user_name = '张三';
~~~

## 执行回调
### ApplicationVersionCallback.java
~~~ java
import cn.alex.version.callback.VersionUpdatingCallback;
import cn.alex.version.callback.builder.VersionUpdatingCallbackBuilder;
import cn.alex.version.callback.builder.VersionUpdatingCallbackExceptionBuilder;
import cn.alex.version.callback.builder.VersionUpdatingCallbackStartBuilder;

/**
 * 版本升级回调
 *
 * @author Alex
 * @date 2024/8/25 01:42
 */
public class ApplicationVersionCallback implements VersionUpdatingCallback {

    /**
     * 开始升级回调
     * @param startBuilder 回调参数
     */
    @Override
    public void onStartCall(VersionUpdatingCallbackStartBuilder startBuilder) {
        System.out.println("开始升级");
    }

    /**
     * 每个版本开始执行升级回调
     * @param updateBuilder 回调参数
     */
    @Override
    public void onUpdatingCall(VersionUpdatingCallbackBuilder updateBuilder) {
        System.out.println("正在升级" + updateBuilder.getUpgradeVersion());
    }

    /**
     * 升级完成回调
     */
    @Override
    public void onEndCall() {
        System.out.println("完成升级");
    }

    /**
     * 升级异常回调
     * @param exceptionBuilder 回调参数
     */
    @Override
    public void onExceptionCall(VersionUpdatingCallbackExceptionBuilder exceptionBuilder) {
        System.out.println("升级异常");
    }
}
~~~

