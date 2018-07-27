package com.junittest.demo.controller;

import com.junittest.demo.domin.User;
import com.junittest.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user/")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value="getUser",method=RequestMethod.GET)
    public User getUserById(HttpServletRequest request, Integer id){
        User user = this.userService.getUserById(id);
        return user;
    }

    @RequestMapping(value="getUserInfo",method=RequestMethod.GET)
    public User getUserInfoById(HttpServletRequest request, Integer id){
        User user = this.userService.getUserInfoById(id);
        return user;
    }

    @RequestMapping(value="updateUser" ,method=RequestMethod.PUT)
    public Integer updateUser(HttpServletRequest request,@RequestBody User user){
        return this.userService.updateUser(user);
    }

    @RequestMapping(value="saveUser" ,method=RequestMethod.POST)
    public Integer saveUser(HttpServletRequest request,@RequestBody User user){
        System.out.println(request.getHeader("SESSIONNO"));
        return this.userService.saveUser(user);
    }

    @RequestMapping(value="deleteUser",method=RequestMethod.DELETE )
    public Integer deleteUser(Long id){
        System.out.println(id);
        return this.userService.deleteUser(id);


    }

}
