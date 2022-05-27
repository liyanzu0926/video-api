package top.liguapi.api.service;

import top.liguapi.api.entity.pojo.Comment;

import java.util.Map;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/27 20:55
 */
public interface CommentService {
    void insert(Comment comment);

    Map<String, Object> getComments(Integer video_id, Integer page, Integer size);
}
