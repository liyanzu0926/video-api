package top.liguapi.api.controller;

import com.sun.xml.internal.bind.v2.TODO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.liguapi.api.constant.RedisPrefix;
import top.liguapi.api.entity.dto.CaptchaDTO;
import top.liguapi.api.exception.SMSException;
import top.liguapi.api.exception.SMSExceptionEnum;

import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/17 17:18
 */
@RestController
@Slf4j
@RequestMapping("api")
public class SMSController {
    private RedisTemplate redisTemplate;

    @Autowired
    public SMSController(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("captchas")
    public void sendCode(@RequestBody CaptchaDTO captchaDTO){
        String phone = captchaDTO.getPhone();

        // 1.查看是否在发送验证码间隙时间内
        if (redisTemplate.hasKey(RedisPrefix.CAPTCHA_GAP_TIME + phone)) throw new SMSException(SMSExceptionEnum.FREQUENT_REQUESTS);

        // 2.生成4位验证码
        String code = RandomStringUtils.randomNumeric(4);
        log.info("验证码为:{}", code);

        // 3.发送验证码
        // TODO

        // 4.将验证码放入redis
        redisTemplate.opsForValue().set(RedisPrefix.CAPTCHA + phone, code, 10, TimeUnit.MINUTES); // 有效时间5分钟

        // 5.设置发送验证码间隙时间
        redisTemplate.opsForValue().set(RedisPrefix.CAPTCHA_GAP_TIME + phone, true, 60, TimeUnit.SECONDS);
    }
}
