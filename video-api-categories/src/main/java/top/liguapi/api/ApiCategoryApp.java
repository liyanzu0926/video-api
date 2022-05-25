package top.liguapi.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/17 15:46
 */
@SpringBootApplication
@MapperScan("top.liguapi.api.mapper")
public class ApiCategoryApp {
    public static void main(String[] args) {
        SpringApplication.run(ApiCategoryApp.class, args);
    }
}
