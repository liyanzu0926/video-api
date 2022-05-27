package top.liguapi.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.liguapi.api.entity.pojo.Favorite;
import top.liguapi.api.entity.pojo.FavoriteExample;
import top.liguapi.api.mapper.FavoriteMapper;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/27 20:23
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

    private FavoriteMapper favoriteMapper;

    @Autowired
    public FavoriteServiceImpl(FavoriteMapper favoriteMapper) {
        this.favoriteMapper = favoriteMapper;
    }

    @Override
    public void insert(Favorite favorite) {
        favoriteMapper.insertSelective(favorite);
    }

    @Override
    public void delete(Integer uid, Integer video_id) {
        FavoriteExample example = new FavoriteExample();
        example.createCriteria().andUidEqualTo(uid).andVideoIdEqualTo(video_id);
        favoriteMapper.deleteByExample(example);
    }
}
