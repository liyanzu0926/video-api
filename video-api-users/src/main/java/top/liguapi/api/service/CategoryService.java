package top.liguapi.api.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/18 20:52
 */
@Service
@FeignClient(value = "VIDEO-API-CATEGORIES/api")
public interface CategoryService {

    @GetMapping("categories/name/{id}")
    String queryNameById(@PathVariable("id") Integer id);
}
