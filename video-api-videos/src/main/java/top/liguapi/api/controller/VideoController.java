package top.liguapi.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.liguapi.api.entity.pojo.Video;
import top.liguapi.api.service.VideoService;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/18 19:46
 */
@RestController
@RequestMapping("api")
@Slf4j
public class VideoController {

    private VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("videos/insert")
    public Video insert(@RequestBody Video video){
        return videoService.insert(video);
    }
}
