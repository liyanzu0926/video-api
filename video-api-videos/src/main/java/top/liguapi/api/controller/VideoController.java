package top.liguapi.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.liguapi.api.entity.dto.VideoDTO;
import top.liguapi.api.entity.dto.VideoDetailsDTO;
import top.liguapi.api.entity.pojo.Video;
import top.liguapi.api.service.VideoService;

import java.util.List;

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

    /**
     * @Description: 插入视频
     * @author: lww
     * @date: 2022/5/26 22:18
     */
    @PostMapping("videos/insert")
    public Video insert(@RequestBody Video video) {
        return videoService.insert(video);
    }

    /**
     * @Description: 推荐视频
     * @author: lww
     * @date: 2022/5/26 22:18
     */
    @GetMapping("recommends")
    public List<VideoDTO> recommends(Integer page, @RequestParam("per_page") Integer size) {
        return videoService.query(page, size);
    }

    /**
     * @Description: 通过用户id，查询视频信息
     * @author: lww
     * @date: 2022/5/26 22:39
     */
    @GetMapping("video/query/{uid}")
    public List<VideoDTO> queryVideoByUid(@PathVariable Integer uid) {
        return videoService.queryVideoByUid(uid);
    }

    /**
     * @Description: 根据分类id获取视频列表
     * @author: lww
     * @date: 2022/5/26 23:06
     */
    @GetMapping("videos")
    public List<VideoDTO> getVideoListByCategoryId(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                   @RequestParam(value = "per_page", defaultValue = "1") Integer size,
                                                   @RequestParam("category") Integer categoryId) {
        return videoService.getVideoListByCategoryId(page, size, categoryId);
    }

    /**
     * @Description: 获取视频的详细信息
     * @author: lww
     * @date: 2022/5/26 23:35
     */
    @GetMapping("/videos/{video_id}")
    public VideoDetailsDTO getVideoDetailsByid(@PathVariable Integer video_id){
        return null;
    }

}
