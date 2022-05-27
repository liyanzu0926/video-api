package top.liguapi.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/26 23:23
 */
@Data
public class VideoDetailsDTO {

    private UploaderDTO uploader;

    private Integer id;

    private String title;

    private String category;

    private String link;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("updated_at")
    private Date updatedAt;

    private Integer plays_count;

    private Integer likes_count;

    private boolean liked;

    private boolean disliked;

    private boolean favorite;
}
