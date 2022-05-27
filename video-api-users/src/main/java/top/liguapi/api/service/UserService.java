package top.liguapi.api.service;

import top.liguapi.api.entity.dto.VideoDTO;
import top.liguapi.api.entity.pojo.User;

import java.util.List;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/17 19:57
 */
public interface UserService {
    User queryByPhone(String phone);

    void insert(User user);

    void updateUserInfo(User user);

    String queryNameById(Integer id);

    User userInfo(Integer id);

    List<VideoDTO> queryPlayedList(Integer id, Integer page, Integer size);
}
