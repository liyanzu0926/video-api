package top.liguapi.api.entity.dto;

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

    private Date created_at;

    private Date updated_at;

    private Integer plays_count;

    private Integer likes_count;

    private Boolean liked;

    private Boolean disliked;

    private Boolean favorite;
}
