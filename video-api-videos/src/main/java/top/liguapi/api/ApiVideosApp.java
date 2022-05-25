package top.liguapi.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/17 15:46
 */
@SpringBootApplication
@EnableFeignClients
@MapperScan("top.liguapi.api.mapper")
public class ApiVideosApp {
    public static void main(String[] args) {
        SpringApplication.run(ApiVideosApp.class, args);
    }
}
