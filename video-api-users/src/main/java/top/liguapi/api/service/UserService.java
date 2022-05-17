package top.liguapi.api.service;

import top.liguapi.api.entity.pojo.User;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/17 19:57
 */
public interface UserService {
    User queryByPhone(String phone);

    void insert(User user);

    void updateUserInfo(User user);
}
