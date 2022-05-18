package top.liguapi.api.entity.dto;

import lombok.Data;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/18 18:29
 */
@Data
public class VideoDTO {
    private Integer id;

    private String title;

    private String cover;

    private String category;

    private Integer likes;
}
