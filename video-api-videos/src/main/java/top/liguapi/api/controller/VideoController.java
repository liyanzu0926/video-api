package top.liguapi.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.liguapi.api.entity.dto.VideoDTO;
import top.liguapi.api.entity.dto.VideoDetailsDTO;
import top.liguapi.api.entity.pojo.Comment;
import top.liguapi.api.entity.pojo.Video;
import top.liguapi.api.service.UserService;
import top.liguapi.api.service.VideoService;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private UserService userService;

    @Autowired
    public VideoController(VideoService videoService, UserService userService) {
        this.videoService = videoService;
        this.userService = userService;
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
     * @Description: 根据视频id获取视频信息
     * @author: lww
     * @date: 2022/5/27 23:18
     */
    @GetMapping("video/getVideoById/{id}")
    public VideoDTO getVideoById(@PathVariable Integer id) {
        return videoService.getVideoById(id);
    }

    /**
     * @Description: 获取视频的详细信息
     * @author: lww
     * @date: 2022/5/26 23:35
     */
    @GetMapping("/videos/{video_id}")
    public VideoDetailsDTO getVideoDetailsByid(@PathVariable Integer video_id, String token) {
        return videoService.getVideoDetailsByid(video_id, token);
    }

    /**
     * @Description: 发表评论
     * @author: lww
     * @date: 2022/5/27 21:14
     */
    @PostMapping("/videos/{video_id}/comments")
    public void comments(@PathVariable Integer video_id, String token, @RequestBody Comment comment) {
        comment.setVideoId(video_id);
        Date date = new Date();
        comment.setCreatedAt(date);
        comment.setUpdatedAt(date);

        // 调用用户服务插入评论
        userService.comments(token, comment);
    }

    /**
     * @Description: 获取评论列表
     * @author: lww
     * @date: 2022/5/27 22:47
     */
    @GetMapping("/videos/{video_id}/comments")
    public Map<String, Object> getComments(@PathVariable Integer video_id,
                                           @RequestParam(value = "page", defaultValue = "1") Integer page,
                                           @RequestParam(value = "per_page", defaultValue = "1") Integer size) {
        return userService.getComments(video_id, page, size);
    }

}
