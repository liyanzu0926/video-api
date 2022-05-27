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
    String VIDEO_PLAYS = "VIDEO_PLAYS:"; // 视频播放数前缀
    String USER_LIKES = "USER_LIKES:"; // 用户点赞视频前缀
    String USER_DISLIKES = "USER_DISLIKES:"; // 用户不喜欢视频前缀
    String USER_FAVORITE = "USER_FAVORITE:"; // 用户收藏视频前缀
}
