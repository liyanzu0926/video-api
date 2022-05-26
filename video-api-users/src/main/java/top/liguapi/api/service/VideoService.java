package top.liguapi.api.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import top.liguapi.api.entity.dto.VideoDTO;
import top.liguapi.api.entity.pojo.Video;

import java.util.List;

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

    /**
     * @Description: 通过用户id，查询视频信息
     * @author: lww
     * @date: 2022/5/26 22:39
     */
    @GetMapping("video/query/{uid}")
    List<VideoDTO> queryVideoByUid(@PathVariable Integer uid);
}
