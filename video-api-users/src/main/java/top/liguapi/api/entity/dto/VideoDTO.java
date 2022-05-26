package top.liguapi.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/25 19:32
 */
@Data
public class VideoDTO {
    private Integer id;

    private String title;

    private String cover;

    private String category;

    private Integer likes;

    private String uploader;

    @JsonProperty("created_at")
    private Date createdAt;
}
