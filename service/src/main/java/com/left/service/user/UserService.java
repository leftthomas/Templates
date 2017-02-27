package com.left.service.user;


import com.left.entity.User;

/**
 * Created by left on 15/10/29.
 */
public interface UserService {

    /**
     * @param id
     * @return
     */
    User getUserById(Long id);

    /**
     * @param user
     * @return
     */
    int updateUser(User user);

    /**
     * @param user
     * @return
     */
    int insertUser(User user);
}
