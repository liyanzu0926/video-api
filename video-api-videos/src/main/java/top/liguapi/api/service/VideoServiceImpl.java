package top.liguapi.api.service;

import com.github.pagehelper.PageHelper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.liguapi.api.constant.RedisPrefix;
import top.liguapi.api.entity.dto.UploaderDTO;
import top.liguapi.api.entity.dto.VideoDTO;
import top.liguapi.api.entity.dto.VideoDetailsDTO;
import top.liguapi.api.entity.pojo.User;
import top.liguapi.api.entity.pojo.Video;
import top.liguapi.api.entity.pojo.VideoExample;
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
    private RedisTemplate redisTemplate;

    @Autowired
    public VideoServiceImpl(VideoMapper videoMapper, RabbitTemplate rabbitTemplate, UserService userService, CategoryService categoryService, RedisTemplate redisTemplate) {
        this.videoMapper = videoMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.userService = userService;
        this.categoryService = categoryService;
        this.redisTemplate = redisTemplate;
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

    @Override
    public List<VideoDTO> queryVideoByUid(Integer uid) {
        VideoExample example = new VideoExample();
        example.createCriteria().andUidEqualTo(uid);
        List<Video> videos = videoMapper.selectByExample(example);
        List<VideoDTO> videoDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(videos)) {
            for (Video video : videos) {
                VideoDTO videoDTO = videoToDTO(video);
                videoDTOS.add(videoDTO);
            }
        }
        return videoDTOS;
    }

    @Override
    public List<VideoDTO> getVideoListByCategoryId(Integer page, Integer size, Integer categoryId) {
        PageHelper.startPage(page, size);
        VideoExample example = new VideoExample();
        example.createCriteria().andCategoryIdEqualTo(categoryId);
        List<Video> videos = videoMapper.selectByExample(example);
        List<VideoDTO> videoDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(videos)) {
            for (Video video : videos) {
                VideoDTO videoDTO = videoToDTO(video);
                videoDTOS.add(videoDTO);
            }
        }
        return videoDTOS;
    }

    @Override
    public VideoDetailsDTO getVideoDetailsByid(Integer video_id, String token) {
        // 1.获取video基本信息
        VideoDetailsDTO videoDetailsDTO = new VideoDetailsDTO();
        Video video = videoMapper.selectByPrimaryKey(video_id);
        BeanUtils.copyProperties(video, videoDetailsDTO);

        // 2.调用类别服务获取类别名
        videoDetailsDTO.setCategory(categoryService.queryNameById(video.getCategoryId()));

        // 3.调用user服务获取up主信息
        User up = userService.userInfo(video.getUid());
        UploaderDTO uploaderDTO = new UploaderDTO();
        BeanUtils.copyProperties(up, uploaderDTO);

        // 4.是否关注
        uploaderDTO.setFollowed(false);

        // 5.设置uploader信息
        videoDetailsDTO.setUploader(uploaderDTO);

        // 6.播放数
        videoDetailsDTO.setPlays_count(0);
        Integer plays = (Integer) redisTemplate.opsForHash().get(RedisPrefix.VIDEO_PLAYS, video.getId().toString());
        if (plays != null) {
            videoDetailsDTO.setPlays_count(plays);
        }

        // 7.喜欢数
        videoDetailsDTO.setLikes_count(0);
        Integer likes = ((Integer) redisTemplate.opsForHash().get(RedisPrefix.VIDEO_LIKES, video.getId().toString()));
        if (likes != null) {
            videoDetailsDTO.setLikes_count(likes);
        }

        // 8.若用户为登录状态，则查询是否点赞/收藏视频
        if (!StringUtils.isEmpty(token)) {

            // 9.获取用户信息
            User user = (User) redisTemplate.opsForValue().get(RedisPrefix.TOKEN_KEY + token);
            Integer id = user.getId();

            // 10.是否点赞
            if (redisTemplate.opsForSet().isMember(RedisPrefix.USER_LIKES + id, video_id)) {
                videoDetailsDTO.setLiked(true);
            }

            // 11.是否不喜欢
            if (redisTemplate.opsForSet().isMember(RedisPrefix.USER_DISLIKES + id, video_id)) {
                videoDetailsDTO.setDisliked(true);
            }

            // 12.是否收藏
            if (redisTemplate.opsForSet().isMember(RedisPrefix.USER_FAVORITE + id, video_id)) {
                videoDetailsDTO.setFavorite(true);
            }
        }

        return videoDetailsDTO;
    }

    @Override
    public VideoDTO getVideoById(Integer id) {
        Video video = videoMapper.selectByPrimaryKey(id);
        return videoToDTO(video);
    }

    /**
     * @Description: 将Video转换成VideoDTO
     * @author: lww
     * @date: 2022/5/26 22:24
     */
    public VideoDTO videoToDTO(Video video) {
        VideoDTO videoDTO = new VideoDTO();
        BeanUtils.copyProperties(video, videoDTO);

        // 1.调用user服务查询名字
        videoDTO.setUploader(userService.queryNameById(video.getUid()));

        // 2.调用分类服务查询分类名称
        videoDTO.setCategory(categoryService.queryNameById(video.getCategoryId()));

        // 3.到redis中查询点赞数
        videoDTO.setLikes(0);
        Integer likes = (Integer) redisTemplate.opsForHash().get(RedisPrefix.VIDEO_LIKES, video.getId().toString());
        if (likes != null) {
            videoDTO.setLikes(likes);
        }
        return videoDTO;
    }
}
