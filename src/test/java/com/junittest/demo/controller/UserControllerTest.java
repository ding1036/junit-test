package com.junittest.demo.controller;

import com.junittest.demo.DemoApplication;
import com.junittest.demo.domin.User;
import com.junittest.demo.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = DemoApplication.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@WebMvcTest(UserController.class)
@DirtiesContext
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WebApplicationContext context;

    //在测试Controller时需要进行隔离测试，这个时候需要Mock Service层的服务。
    @MockBean
    UserService userService;

    private MockMvc mockMvc;

   @Before
    public void setupMockMvc() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    /***
     * 使用mockmvc方法
     * 测试根据用户id获取用户信息接口
     *
     * @throws Exception
     */
    @Test
    public void testGetUser() throws Exception {
        User user = new User();
        user.setName("lisi");
        user.setId(2);
        user.setAge(21);
        given(this.userService.getUserById(anyInt()))
                .willReturn(user);
        String responce = mockMvc
                .perform(get("/user/getUser").contentType(MediaType.APPLICATION_FORM_URLENCODED) // 请求数据的格式
                        .param("id", "2"))// 相当于直接写在url上的参数
                .andExpect(status().isOk()) // 比较结果
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("lisi"))).andExpect(jsonPath("$.age", is(21)))
                // .andDo(System.out.println()) //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        System.out.println(responce);
    }

    /***
     * GET请求测试
     *
     * @throws Exception
     */
    @Test
    public void testGetUser_rest() throws Exception {
       given(this.userService.getUserInfoById(anyInt()))
                .willReturn(new User());
        Map<String,Integer> multiValueMap = new HashMap<>();
        multiValueMap.put("id",2);//传值，但要在url上配置相应的参数
        User result = restTemplate.getForObject("/user/getUserInfo?id={id}",User.class,multiValueMap);
        Assert.assertEquals(result.getAge(),21);
    }

    /***
     * POST请求测试
     *
     * @throws Exception
     */
    @Test
    public void testSaveUser() throws Exception {
       given(this.userService.saveUser(any()))
                .willReturn(1);
        Map<String,Object> multiValueMap = new HashMap<>();
        multiValueMap.put("id",2);
        multiValueMap.put("name","zhangsan");
        multiValueMap.put("age","22");
        int result = restTemplate.postForObject("/user/saveUser",multiValueMap,Integer.class);
        Assert.assertEquals(result,1);
    }


/***
     * PUT请求测试
     *
     * @throws Exception
     *
***/
    @Test
    public void testUpdateUser() throws Exception {
        given(this.userService.updateUser(any()))
                .willReturn(1);
        HttpHeaders headers = new HttpHeaders();
        headers.set("token","xxxxxx");
        headers.setContentType(MediaType.APPLICATION_JSON);
        User user = new User();
        user.setAge(21);
        user.setName("lisi");
        user.setId(6);
        HttpEntity formEntity = new HttpEntity(user,headers);
        ResponseEntity<Integer> result = restTemplate.exchange("/user/updateUser",HttpMethod.PUT,formEntity,Integer.class);
        Assert.assertEquals(result.getBody().intValue(),1);
    }



    @Test
    public void testDeleteUser() throws Exception {
        given(this.userService.deleteUser(anyLong()))
                .willReturn(1);
        HttpHeaders headers = new HttpHeaders();
        headers.set("token","xxxxx");
        MultiValueMap multiValueMap = new LinkedMultiValueMap();
        multiValueMap.add("id",2);
        HttpEntity formEntity = new HttpEntity(multiValueMap,headers);
        Integer urlVariables = 2;
        ResponseEntity<Integer> result = restTemplate.exchange("/user/deleteUser", HttpMethod.DELETE,formEntity,Integer.class,urlVariables);
        Assert.assertEquals(result.getBody().intValue(),1);
    }

}
