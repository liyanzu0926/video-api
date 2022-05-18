package top.liguapi.api.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.liguapi.api.entity.pojo.Video;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/18 18:57
 */
@Service
@FeignClient(value = "VIDEO-API-VIDEOS/api")
public interface VideoService {

    @GetMapping("videos/insert")
    Video insert(@RequestBody Video video);
}
