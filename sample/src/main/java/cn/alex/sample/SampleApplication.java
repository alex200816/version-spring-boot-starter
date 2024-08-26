package cn.alex.sample;

import cn.alex.version.annotation.EnableVersionUpdating;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Alex
 * @date 2024/8/26 20:05
 */
@MapperScan("cn.alex.sample.mapper")
@EnableVersionUpdating
@SpringBootApplication
public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

}
