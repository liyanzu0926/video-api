package top.liguapi.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.liguapi.api.entity.pojo.User;
import top.liguapi.api.entity.pojo.UserExample;
import top.liguapi.api.mapper.UserMapper;

import java.util.List;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/17 19:58
 */
@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
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
}
