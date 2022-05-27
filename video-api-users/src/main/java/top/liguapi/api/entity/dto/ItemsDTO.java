package top.liguapi.api.entity.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/27 21:07
 */
@Data
public class ItemsDTO {

    private ReviewerDTO reviewer;

    private Integer id;

    private String content;

    private Date created_at;

    private SubCommentListDTO sub_comments;


}
