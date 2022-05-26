package top.liguapi.api.entity.dto;

import lombok.Data;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/26 23:25
 */
@Data
public class UploaderDTO {

    private Integer id;

    private String name;

    private String avatar;

    private Boolean followed;
}
