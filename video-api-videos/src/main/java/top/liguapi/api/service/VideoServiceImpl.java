package top.liguapi.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.liguapi.api.entity.pojo.Video;
import top.liguapi.api.mapper.VideoMapper;

import java.util.Date;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/18 19:47
 */
@Service
public class VideoServiceImpl implements VideoService {

    private VideoMapper videoMapper;

    @Autowired
    public VideoServiceImpl(VideoMapper videoMapper) {
        this.videoMapper = videoMapper;
    }

    @Override
    public Video insert(Video video) {
        Date date = new Date();
        video.setCreatedAt(date);
        video.setUpdatedAt(date);
        videoMapper.insert(video);
        // 用rabbitmq发布索引信息
        // TODO
        return video;
    }
}
