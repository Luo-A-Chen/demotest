package org.example.testdemo.controller;

import org.example.testdemo.config.JwtConfig;
import org.example.testdemo.dto.LoginRequest;
import org.example.testdemo.dto.RegisterRequest;
import org.example.testdemo.dto.UserResponse;
import org.example.testdemo.entity.User;
import org.example.testdemo.response.BaseResponse;
import org.example.testdemo.service.UserService;
import org.example.testdemo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @PostMapping("/getUserById")
    public User getUserById(int id) {
        return userService.getUserById(id);
    }
    @PostMapping("/insertUser")
    public int insertUser(User user) {
        return userService.insertUser(user);
    }
    @PostMapping("/updateUser")
    public int updateUser(User user) {
        return userService.updateUser(user);
    }
    @PostMapping("/deleteUser")
    public int deleteUser(int id) {
        return userService.deleteUser(id);
    }
    @PostMapping("/login")
    public BaseResponse<UserResponse> login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
    @PostMapping("/register")
    public Integer register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }
}
