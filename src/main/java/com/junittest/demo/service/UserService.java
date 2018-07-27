package com.junittest.demo.service;

import com.junittest.demo.domin.User;
import com.junittest.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper mapper;

    public User getUserById(Integer id) {
        User user = mapper.getUserById(id);
        return user;
    }

    public Integer updateUser(User user) {
        return 1;
    }

    public Integer saveUser(User user) {
        return 1;
    }

    public Integer deleteUser(Long id) {
        return 1;
    }

    public User getUserInfoById(Integer id) {
        User user = new User();
        user.setAge(21);
        user.setName("lisi");
        user.setId(2);
        return user;
    }
}
