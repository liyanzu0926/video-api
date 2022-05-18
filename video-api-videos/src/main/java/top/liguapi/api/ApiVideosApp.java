package top.liguapi.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/17 15:46
 */
@SpringBootApplication
@MapperScan("top.liguapi.api.mapper")
public class ApiVideosApp {
    public static void main(String[] args) {
        SpringApplication.run(ApiVideosApp.class, args);
    }
}
