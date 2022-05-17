package top.liguapi.api.entity.dto;

import lombok.Data;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/17 20:05
 */
@Data
public class CaptchaDTO {
    private String phone;
    private String captcha;
}
