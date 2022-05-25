package top.liguapi.api.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/25 19:52
 */
@Service
@FeignClient(value = "VIDEO-API-USERS/api")
public interface UserService {

    @GetMapping("getName/{id}")
    String queryNameById(@PathVariable Integer id);
}
