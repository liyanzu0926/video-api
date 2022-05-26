package top.liguapi.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.liguapi.api.annotation.Token;
import top.liguapi.api.config.AliyunComponent;
import top.liguapi.api.constant.RedisPrefix;
import top.liguapi.api.entity.dto.CaptchaDTO;
import top.liguapi.api.entity.dto.UserDTO;
import top.liguapi.api.entity.dto.VideoDTO;
import top.liguapi.api.entity.pojo.User;
import top.liguapi.api.entity.pojo.Video;
import top.liguapi.api.exception.UserException;
import top.liguapi.api.exception.UserExceptionEnum;
import top.liguapi.api.service.CategoryService;
import top.liguapi.api.service.UserService;
import top.liguapi.api.service.VideoService;
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


    @Autowired
    public UserController(RedisTemplate redisTemplate, UserService userService, AliyunComponent aliyunComponent, VideoService videoService, CategoryService categoryService) {
        this.redisTemplate = redisTemplate;
        this.userService = userService;
        this.aliyunComponent = aliyunComponent;
        this.videoService = videoService;
        this.categoryService = categoryService;
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
     * @Description: 获取用户信息
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
    public String queryNameById(@PathVariable Integer id){
        return userService.queryNameById(id);
    }

    /**
     * @Description: 查询用户发布的视频
     * @author: lww
     * @date: 2022/5/26 22:27
     */
    @GetMapping("/user/videos")
    @Token
    public List<VideoDTO> queryUserVideo(HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return videoService.queryVideoByUid(user.getId());
    }


}
