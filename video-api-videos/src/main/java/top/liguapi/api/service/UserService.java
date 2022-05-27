package top.liguapi.api.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import top.liguapi.api.entity.pojo.Comment;
import top.liguapi.api.entity.pojo.User;

import java.util.Map;

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

    @GetMapping("user/{id}")
    User userInfo(@PathVariable Integer id);

    @PostMapping("user/comments")
    void comments(@RequestParam String token, @RequestBody Comment comment);

    @GetMapping("user/getComments")
    Map<String, Object> getComments(@RequestParam Integer video_id, @RequestParam Integer page, @RequestParam Integer size);
}
