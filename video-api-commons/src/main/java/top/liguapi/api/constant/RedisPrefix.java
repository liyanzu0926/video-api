package top.liguapi.api.constant;

/**
 * @Author lww
 * @Version 1.0
 * @Date 2022/5/14 18:20
 */
public interface RedisPrefix {
    String TOKEN_KEY = "TOKEN:";  // token前缀
    String CAPTCHA = "CAPTCHA:";  // 验证码前缀
    String CAPTCHA_GAP_TIME = "GAP_TIME:";  // 验证码间隙时间
    String VIDEO_LIKES = "VIDEO_LIKES:"; // 视频点赞数前缀
}
