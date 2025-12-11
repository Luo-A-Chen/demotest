package org.example.testdemo.controller;

import org.example.testdemo.dto.Request.LoginRequest;
import org.example.testdemo.dto.Request.RegisterRequest;
import org.example.testdemo.dto.response.UserResponse;
import org.example.testdemo.dto.SafeUser;
import org.example.testdemo.entity.User;
import org.example.testdemo.dto.response.BaseResponse;
import org.example.testdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @GetMapping("/getUser")
    public User getUser() {
        return userService.getUser();
    }
    @PostMapping("/insertUser")
    public int insertUser(User user) {
        return userService.insertUser(user);
    }
    @PostMapping("/updateUser")
    public BaseResponse<SafeUser> updateUser(@RequestBody User user) {
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
    @PostMapping("/uploadAvatar")
    public BaseResponse<String> uploadAvatar(@RequestParam("avatar") MultipartFile avatarFile) {
        return userService.uploadAvatar(avatarFile);
    }
}
