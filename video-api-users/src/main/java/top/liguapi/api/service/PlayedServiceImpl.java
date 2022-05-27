package top.liguapi.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.liguapi.api.entity.pojo.Played;
import top.liguapi.api.entity.pojo.PlayedExample;
import top.liguapi.api.mapper.PlayedMapper;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/27 16:39
 */
@Service
public class PlayedServiceImpl implements PlayedService {

    private PlayedMapper playedMapper;

    @Autowired
    public PlayedServiceImpl(PlayedMapper playedMapper) {
        this.playedMapper = playedMapper;
    }

    @Override
    public Played query(Integer id, Integer video_id) {
        PlayedExample example = new PlayedExample();
        example.createCriteria().andUidEqualTo(id).andVideoIdEqualTo(video_id);
        List<Played> playeds = playedMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(playeds)) {
            return playeds.get(0);
        }
        return null;
    }

    @Override
    public void insertBrowsingHistory(Played newPlayed) {
        playedMapper.insertSelective(newPlayed);
    }

    @Override
    public void updateBrowsingTime(Integer id) {
        Played played = new Played();
        played.setId(id);
        played.setUpdatedAt(new Date());
        playedMapper.updateByPrimaryKeySelective(played);
    }
}
