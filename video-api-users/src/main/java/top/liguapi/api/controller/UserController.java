package top.liguapi.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.liguapi.api.annotation.Token;
import top.liguapi.api.constant.RedisPrefix;
import top.liguapi.api.entity.dto.CaptchaDTO;
import top.liguapi.api.entity.dto.UserDTO;
import top.liguapi.api.entity.pojo.User;
import top.liguapi.api.exception.UserException;
import top.liguapi.api.exception.UserExceptionEnum;
import top.liguapi.api.service.UserService;
import top.liguapi.api.utils.AvatarUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

    @Autowired
    public UserController(RedisTemplate redisTemplate, UserService userService) {
        this.redisTemplate = redisTemplate;
        this.userService = userService;
    }

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

    @DeleteMapping("tokens")
    @Token
    public void logout(HttpServletRequest request) {
        String token = (String) request.getAttribute("token");
        redisTemplate.delete(RedisPrefix.TOKEN_KEY + token);
        log.info("注销成功");
    }

    @GetMapping("user")
    @Token
    public UserDTO userInfo(HttpServletRequest request) {
        // 从上下文中获取
        User user = (User) request.getAttribute("user");
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

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
            if (!redisTemplate.hasKey(RedisPrefix.TOKEN_KEY + phone)) {
                throw new UserException(UserExceptionEnum.VERIFICATION_CODE_EXPIRED);
            }
            String code = (String) redisTemplate.opsForValue().get(RedisPrefix.CAPTCHA + phone);
            if (captcha == null || !captcha.equals(code)) {
                throw new UserException(UserExceptionEnum.VERIFICATION_CODE_ERROR);
            }
            user.setPhone(phone);
            user.setPhoneLinked(true);
        }
        userService.updateUserInfo(user);
        redisTemplate.opsForValue().set(RedisPrefix.TOKEN_KEY + token, user, 7, TimeUnit.DAYS);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }


}
