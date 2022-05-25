package top.liguapi.api.service;

import com.github.pagehelper.PageHelper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.liguapi.api.constant.RedisPrefix;
import top.liguapi.api.entity.dto.VideoDTO;
import top.liguapi.api.entity.pojo.Video;
import top.liguapi.api.mapper.VideoMapper;
import top.liguapi.api.utils.JSONUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/18 19:47
 */
@Service
public class VideoServiceImpl implements VideoService {

    private VideoMapper videoMapper;
    private RabbitTemplate rabbitTemplate;
    private UserService userService;
    private CategoryService categoryService;
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public VideoServiceImpl(VideoMapper videoMapper, RabbitTemplate rabbitTemplate, UserService userService, CategoryService categoryService, StringRedisTemplate stringRedisTemplate) {
        this.videoMapper = videoMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.userService = userService;
        this.categoryService = categoryService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Video insert(Video video) {
        Date date = new Date();
        video.setCreatedAt(date);
        video.setUpdatedAt(date);
        videoMapper.insert(video);

        // 用rabbitmq发布索引信息
        rabbitTemplate.convertAndSend("video", "", JSONUtils.writeValueAsString(videoToDTO(video)));
        return video;
    }

    @Override
    public List<VideoDTO> query(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Video> videos = videoMapper.selectByExample(null);
        ArrayList<VideoDTO> videoDTOS = new ArrayList<>();
        for (Video video : videos) {
            videoDTOS.add(videoToDTO(video));
        }
        return videoDTOS;
    }

    /**
     * @param video
     * @return 将Video转换成VideoDTO
     */
    public VideoDTO videoToDTO(Video video) {
        VideoDTO videoDTO = new VideoDTO();
        BeanUtils.copyProperties(video, videoDTO);
        // 调用user服务查询名字
        videoDTO.setUploader(userService.queryNameById(video.getUid()));
        // 调用分类服务查询分类名称
        videoDTO.setCategory(categoryService.queryNameById(video.getCategoryId()));
        videoDTO.setLikes(0);
        // 到redis中查询点赞数
        String likes = stringRedisTemplate.opsForValue().get(RedisPrefix.VIDEO_LIKES + video.getId());
        if (!StringUtils.isEmpty(likes)) {
            videoDTO.setLikes(Integer.valueOf(likes));
        }
        return videoDTO;
    }
}
