package top.liguapi.api.service;

import top.liguapi.api.entity.dto.VideoDTO;
import top.liguapi.api.entity.pojo.Video;

import java.util.List;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/18 19:46
 */
public interface VideoService {
    Video insert(Video video);

    List<VideoDTO> query(Integer page, Integer size);
}
