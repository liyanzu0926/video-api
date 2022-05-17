package top.liguapi.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/17 21:37
 */
@Data
public class UserDTO {

    private String avatar;

    private String name;

    private String intro;

    private String phone;

    @JsonProperty("phone_linked")
    private Boolean phoneLinked;

    @JsonProperty("wechat_linked")
    private Boolean wechatLinked;
}
