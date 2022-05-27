package top.liguapi.api.service;

import top.liguapi.api.entity.pojo.Played;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/27 16:39
 */
public interface PlayedService {
    Played query(Integer id, Integer video_id);

    void insertBrowsingHistory(Played newPlayed);

    void updateBrowsingTime(Integer id);
}
