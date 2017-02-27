package com.left.service.user;

import com.left.dao.UserMapper;
import com.left.entity.User;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by left on 15/10/29.
 */
public class UserServiceImpl implements UserService {

    @Resource(name = "userMapper")
    private UserMapper userMapper;

    @Override
    public User getUserById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }


    @Override
    public int updateUser(User user) {
        user.setUpdateTime(new Date());
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int insertUser(User user) {
        return userMapper.insertSelective(user);
    }
}
