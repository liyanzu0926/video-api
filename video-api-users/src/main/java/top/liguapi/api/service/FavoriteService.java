package top.liguapi.api.service;

import top.liguapi.api.entity.pojo.Favorite;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/27 20:23
 */
public interface FavoriteService {
    void insert(Favorite favorite);

    void delete(Integer uid, Integer video_id);

}
