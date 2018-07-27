package com.junittest.demo.mapper;

import com.junittest.demo.domin.User;

public interface UserMapper {
    User getUserById(int id);

    int insertUser(User user);
}
