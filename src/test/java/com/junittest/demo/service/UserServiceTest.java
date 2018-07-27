package com.junittest.demo.service;

import com.junittest.demo.domin.User;
import com.junittest.demo.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;

//测试Service和测试Controller类似，同样采用隔离法
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @MockBean
    private UserMapper userMapper;
    @Autowired
    private UserService userServiceImpl;
    @Test
    public void testModelServiceServiceImpl(){
        User mockuser = new User();
        mockuser.setName("lisi");
        given(userMapper.getUserById(anyInt()))
                .willReturn(mockuser);
        User user =  userServiceImpl.getUserById(1);
        assertThat(user.getName(),equalTo("lisi"));
    }
}
