package com.junittest.demo.mapper;


import com.junittest.demo.domin.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    @Rollback
    public void testInsertUser() throws Exception {
        User user = new User();
        user.setName("lisi");
        int n = userMapper.insertUser(user);
        Assert.assertEquals(n,1);
    }

}
