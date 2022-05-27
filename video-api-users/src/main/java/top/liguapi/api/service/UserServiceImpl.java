package top.liguapi.api.service;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.liguapi.api.entity.dto.VideoDTO;
import top.liguapi.api.entity.pojo.Played;
import top.liguapi.api.entity.pojo.PlayedExample;
import top.liguapi.api.entity.pojo.User;
import top.liguapi.api.entity.pojo.UserExample;
import top.liguapi.api.mapper.PlayedMapper;
import top.liguapi.api.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/17 19:58
 */
@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;
    private PlayedMapper playedMapper;
    private VideoService videoService;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, PlayedMapper playedMapper, VideoService videoService) {
        this.userMapper = userMapper;
        this.playedMapper = playedMapper;
        this.videoService = videoService;
    }

    @Override
    public User queryByPhone(String phone) {
        UserExample example = new UserExample();
        example.createCriteria().andPhoneEqualTo(phone);
        List<User> users = userMapper.selectByExample(example);
        if (users == null || users.size() == 0) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public void insert(User user) {
        userMapper.insertSelective(user);
    }

    @Override
    public void updateUserInfo(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public String queryNameById(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user.getName();
    }

    @Override
    public User userInfo(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<VideoDTO> queryPlayedList(Integer id, Integer page, Integer size) {
        ArrayList<VideoDTO> videoDTOS = new ArrayList<>();
        PageHelper.startPage(page, size);
        PlayedExample example = new PlayedExample();
        example.createCriteria().andUidEqualTo(id);
        List<Played> playeds = playedMapper.selectByExample(example);
        for (Played played : playeds) {
            videoDTOS.add(videoService.getVideoById(played.getVideoId()));
        }
        return videoDTOS;
    }
}
