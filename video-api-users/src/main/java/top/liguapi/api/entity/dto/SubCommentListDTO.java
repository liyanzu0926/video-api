package top.liguapi.api.entity.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/27 21:11
 */
@Data
public class SubCommentListDTO {

    private ReviewerDTO reviewer;

    private Integer id;

    private Integer parent_id;

    private String content;

    private Date created_at;

}
