package top.liguapi.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.liguapi.api.annotation.Token;
import top.liguapi.api.config.AliyunComponent;
import top.liguapi.api.constant.RedisPrefix;
import top.liguapi.api.entity.dto.CaptchaDTO;
import top.liguapi.api.entity.dto.UserDTO;
import top.liguapi.api.entity.dto.VideoDTO;
import top.liguapi.api.entity.pojo.*;
import top.liguapi.api.exception.UserException;
import top.liguapi.api.exception.UserExceptionEnum;
import top.liguapi.api.service.*;
import top.liguapi.api.utils.AvatarUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/17 19:52
 */
@RestController
@RequestMapping("api")
@Slf4j
public class UserController {

    private RedisTemplate redisTemplate;
    private UserService userService;
    private AliyunComponent aliyunComponent;
    private VideoService videoService;
    private CategoryService categoryService;
    private PlayedService playedService;
    private FavoriteService favoriteService;
    private CommentService commentService;


    @Autowired
    public UserController(RedisTemplate redisTemplate, UserService userService, AliyunComponent aliyunComponent, VideoService videoService, CategoryService categoryService, PlayedService playedService, FavoriteService favoriteService, CommentService commentService) {
        this.redisTemplate = redisTemplate;
        this.userService = userService;
        this.aliyunComponent = aliyunComponent;
        this.videoService = videoService;
        this.categoryService = categoryService;
        this.playedService = playedService;
        this.favoriteService = favoriteService;
        this.commentService = commentService;
    }

    /**
     * @Description: 登录注册
     * @author: lww
     * @date: 2022/5/26 22:14
     */
    @PostMapping("tokens")
    public Map<String, String> login(@RequestBody CaptchaDTO captchaDTO, HttpSession session) {
        String phone = captchaDTO.getPhone();
        String captcha = captchaDTO.getCaptcha();

        // 1.验证码是否有效
        if (!redisTemplate.hasKey(RedisPrefix.CAPTCHA + phone)) {
            throw new UserException(UserExceptionEnum.VERIFICATION_CODE_EXPIRED);
        }
        String code = (String) redisTemplate.opsForValue().get(RedisPrefix.CAPTCHA + phone);
        if (!code.equals(captcha)) {
            throw new UserException(UserExceptionEnum.VERIFICATION_CODE_ERROR);
        }

        // 2.登录成功，判断时候为新用户
        User user = userService.queryByPhone(phone);
        if (user == null) {
            user = new User();
            user.setName(phone);
            user.setAvatar(AvatarUtils.getPhoto());
            user.setIntro("");
            user.setPhone(phone);
            user.setPhoneLinked(true);
            user.setOpenid("");
            user.setWechatLinked(false);
            user.setFollowersCount(0);
            user.setFollowingCount(0);
            Date date = new Date();
            user.setCreatedAt(date);
            user.setUpdatedAt(date);
            userService.insert(user);
        }

        // 3.将token放入redis
        String token = session.getId();
        redisTemplate.opsForValue().set(RedisPrefix.TOKEN_KEY + token, user, 7, TimeUnit.DAYS);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        log.info("登陆成功");
        return map;
    }

    /**
     * @Description: 注销登录
     * @author: lww
     * @date: 2022/5/26 22:16
     */
    @DeleteMapping("tokens")
    @Token
    public void logout(HttpServletRequest request) {
        String token = (String) request.getAttribute("token");
        redisTemplate.delete(RedisPrefix.TOKEN_KEY + token);
        log.info("注销成功");
    }

    /**
     * @Description: 获取当前用户信息
     * @author: lww
     * @date: 2022/5/26 22:16
     */
    @GetMapping("user")
    @Token
    public UserDTO userInfo(HttpServletRequest request) {
        // 从上下文中获取
        User user = (User) request.getAttribute("user");
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    /**
     * @Description: 通过id获取用户信息
     * @author: lww
     * @date: 2022/5/27 14:54
     */
    @GetMapping("user/{id}")
    public User userInfo(@PathVariable Integer id) {
        return userService.userInfo(id);
    }

    /**
     * @Description: 修改用户信息
     * @author: lww
     * @date: 2022/5/26 22:16
     */
    @PatchMapping("user")
    @Token
    public UserDTO updateUserInfo(@RequestBody Map<String, String> map, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        String token = (String) request.getAttribute("token");
        String name = map.get("name");
        String intro = map.get("intro");
        String phone = map.get("phone");
        String captcha = map.get("captcha");
        if (name != null) {
            user.setName(name);
        }
        if (intro != null) {
            user.setIntro(intro);
        }
        if (phone != null) {
            if (!redisTemplate.hasKey(RedisPrefix.CAPTCHA + phone)) {
                throw new UserException(UserExceptionEnum.VERIFICATION_CODE_EXPIRED);
            }
            String code = (String) redisTemplate.opsForValue().get(RedisPrefix.CAPTCHA + phone);
            if (captcha == null || !captcha.equals(code)) {
                throw new UserException(UserExceptionEnum.VERIFICATION_CODE_ERROR);
            }
            user.setPhone(phone);
            user.setPhoneLinked(true);
        }
        user.setUpdatedAt(new Date());
        userService.updateUserInfo(user);
        redisTemplate.opsForValue().set(RedisPrefix.TOKEN_KEY + token, user, 7, TimeUnit.DAYS);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    /**
     * @Description: 上传视频
     * @author: lww
     * @date: 2022/5/26 22:17
     */
    @PostMapping("user/videos")
    @Token
//    @GlobalTransactional(name = "abc", rollbackFor = Exception.class)
    public VideoDTO upload(MultipartFile file, Video video, Integer category_id, HttpServletRequest request) {

        // 1.获取文件类型
        String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        log.info("文件类型：{}", type);

        // 2.自定义文件名
        String uuid = UUID.randomUUID().toString();
        String filename = uuid + type;
        log.info("文件名：{}", filename);

        // 3.上传视频
        String link = null;
        try {
            link = aliyunComponent.fileUpload("videos", filename, file.getInputStream());
            log.info("视频上传后返回url:{}", link);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 4.阿里云oss获取某一帧作为视频封面
        String cover = link + "?x-oss-process=video/snapshot,t_3000,f_jpg,w_0,h_0,m_fast,ar_auto";

        // 5.设定video信息
        User user = (User) request.getAttribute("user");
        video.setUid(user.getId());
        video.setLink(link);
        video.setCover(cover);
        video.setCategoryId(category_id);

        // 6.用openfeign调用videoService插入video信息
        Video videoResult = videoService.insert(video);

        // 7.用openfeign调用categoryService查询类别名
        String category = categoryService.queryNameById(category_id);

        // 8.设定返回值userDTO信息
        VideoDTO videoDTO = new VideoDTO();
        BeanUtils.copyProperties(videoResult, videoDTO);
        videoDTO.setCategory(category);
        videoDTO.setLikes(0);

        return videoDTO;
    }

    /**
     * @Description: 通过id获取name
     * @author: lww
     * @date: 2022/5/26 22:17
     */
    @GetMapping("getName/{id}")
    public String queryNameById(@PathVariable Integer id) {
        return userService.queryNameById(id);
    }

    /**
     * @Description: 查询用户发布的视频
     * @author: lww
     * @date: 2022/5/26 22:27
     */
    @GetMapping("/user/videos")
    @Token
    public List<VideoDTO> queryUserVideo(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        return videoService.queryVideoByUid(user.getId());
    }

    /**
     * @Description: 视频播放
     * @author: lww
     * @date: 2022/5/27 17:47
     */
    @PutMapping("/user/played/{video_id}")
    public void playVideo(@PathVariable Integer video_id, String token) {

        // 1.视频播放次数+1
        redisTemplate.opsForHash().increment(RedisPrefix.VIDEO_PLAYS, video_id.toString(), 1);

        // 2.若用户为登录状态
        if (!StringUtils.isEmpty(token)) {

            // 3.从redis获取用户对象
            User user = (User) redisTemplate.opsForValue().get(RedisPrefix.TOKEN_KEY + token);

            // 4.查询是否已有浏览记录
            Played played = playedService.query(user.getId(), video_id);

            // 5.没有记录就插入
            if (ObjectUtils.isEmpty(played)) {
                Played newPlayed = new Played();
                newPlayed.setUid(user.getId());
                newPlayed.setVideoId(video_id);
                Date date = new Date();
                newPlayed.setCreatedAt(date);
                newPlayed.setUpdatedAt(date);
                playedService.insertBrowsingHistory(newPlayed);
            } else {
                // 6.有记录就更新浏览时间
                playedService.updateBrowsingTime(played.getId());
            }
        }
    }

    /**
     * @Description: 点赞
     * @author: lww
     * @date: 2022/5/27 18:05
     */
    @PutMapping("user/liked/{video_id}")
    @Token
    public void liked(@PathVariable Integer video_id, HttpServletRequest request) {

        // 1.将该视频id添加到用户点赞列表中
        User user = (User) request.getAttribute("user");
        Integer id = user.getId();
        redisTemplate.opsForSet().add(RedisPrefix.USER_LIKES + id, video_id);

        // 2.判断之前有没有点不喜欢，如果有，则将该视频从不喜欢列表中移除
        if (redisTemplate.opsForSet().isMember(RedisPrefix.USER_DISLIKES + id, video_id)) {
            redisTemplate.opsForSet().remove(RedisPrefix.USER_DISLIKES + id, video_id);
        }

        // 3.点赞次数+1
        redisTemplate.opsForHash().increment(RedisPrefix.VIDEO_LIKES, video_id.toString(), 1);

    }

    /**
     * @Description: 取消点赞
     * @author: lww
     * @date: 2022/5/27 18:08
     */
    @DeleteMapping("user/liked/{video_id}")
    @Token
    public void unliked(@PathVariable Integer video_id, HttpServletRequest request) {

        // 1.将该视频id从用户点赞set中移除
        User user = (User) request.getAttribute("user");
        redisTemplate.opsForSet().remove(RedisPrefix.USER_LIKES + user.getId(), video_id);

        // 2.点赞次数-1
        redisTemplate.opsForHash().increment(RedisPrefix.VIDEO_LIKES, video_id.toString(), -1);

    }

    /**
     * @Description: 不喜欢
     * @author: lww
     * @date: 2022/5/27 18:54
     */
    @PutMapping("user/disliked/{video_id}")
    @Token
    public void disliked(@PathVariable Integer video_id, HttpServletRequest request) {

        User user = (User) request.getAttribute("user");
        Integer id = user.getId();

        // 1.将该视频id添加到用户不喜欢列表中
        redisTemplate.opsForSet().add(RedisPrefix.USER_DISLIKES + id, video_id);

        // 2.如果之前有点赞过则将此视频移除点赞列表
        if (redisTemplate.opsForSet().isMember(RedisPrefix.USER_LIKES + id, video_id)) {

            // 3.将此视频移除点赞列表
            redisTemplate.opsForSet().remove(RedisPrefix.USER_LIKES + id, video_id);

            // 4.视频点赞数-1
            redisTemplate.opsForHash().increment(RedisPrefix.VIDEO_LIKES, video_id.toString(), -1);
        }

    }

    /**
     * @Description: 取消不喜欢
     * @author: lww
     * @date: 2022/5/27 20:05
     */
    @DeleteMapping("user/disliked/{video_id}")
    @Token
    public void undisliked(@PathVariable Integer video_id, HttpServletRequest request) {

        // 将该视频id从不喜欢列表中移除
        User user = (User) request.getAttribute("user");
        redisTemplate.opsForSet().remove(RedisPrefix.USER_DISLIKES + user.getId(), video_id);

    }

    /**
     * @Description: 收藏视频
     * @author: lww
     * @date: 2022/5/27 20:29
     */
    @PutMapping("user/favorites/{video_id}")
    @Token
    public void favorites(@PathVariable Integer video_id, HttpServletRequest request) {

        User user = (User) request.getAttribute("user");
        Integer uid = user.getId();

        // 1.将视频插入数据库收藏表
        Favorite favorite = new Favorite();
        favorite.setUid(uid);
        favorite.setVideoId(video_id);
        Date date = new Date();
        favorite.setCreatedAt(date);
        favorite.setUpdatedAt(date);
        favoriteService.insert(favorite);

        // 2.将视频插入redis收藏列表
        redisTemplate.opsForSet().add(RedisPrefix.USER_FAVORITE + uid, video_id);

    }

    /**
     * @Description: 取消收藏
     * @author: lww
     * @date: 2022/5/27 20:29
     */
    @DeleteMapping("user/favorites/{video_id}")
    @Token
    public void unfavorites(@PathVariable Integer video_id, HttpServletRequest request) {

        User user = (User) request.getAttribute("user");
        Integer uid = user.getId();

        // 1.将该视频从数据库收藏表中删除
        favoriteService.delete(uid, video_id);

        // 2.将该视频从redis收藏表中删除
        redisTemplate.opsForSet().remove(RedisPrefix.USER_FAVORITE + uid, video_id);
    }

    /**
     * @Description: 发表评论
     * @author: lww
     * @date: 2022/5/27 22:13
     */
    @PostMapping("user/comments")
    public void comments(String token, @RequestBody Comment comment) {

        // 1.判断token是否为空
        if (StringUtils.isEmpty(token)) {
            throw new UserException(UserExceptionEnum.TOKEN_IS_EMPTY);
        }

        // 2.判断令牌是否合法
        User user = (User) redisTemplate.opsForValue().get(RedisPrefix.TOKEN_KEY + token);
        if (ObjectUtils.isEmpty(user)) {
            throw new UserException(UserExceptionEnum.TOKEN_IS_ILLEGAL);
        }

        // 3.设定评论信息
        comment.setUid(user.getId());

        // 4.插入评论
        commentService.insert(comment);
    }

    /**
     * @Description: 获取评论列表
     * @author: lww
     * @date: 2022/5/27 22:45
     */
    @GetMapping("user/getComments")
    public Map<String, Object> getComments(Integer video_id, Integer page, Integer size) {
        return commentService.getComments(video_id, page, size);
    }

    /**
     * @Description: 获取播放历史
     * @author: lww
     * @date: 2022/5/27 23:22
     */
    @GetMapping("user/played")
    @Token
    public List<VideoDTO> playedLisat(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "per_page", defaultValue = "1") Integer size,
                                      HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        return userService.queryPlayedList(user.getId(), page, size);
    }
}
