package org.example.testdemo.service;

import org.example.testdemo.dto.LoginRequest;
import org.example.testdemo.dto.RegisterRequest;
import org.example.testdemo.dto.UserResponse;
import org.example.testdemo.dto.SafeUser;
import org.example.testdemo.entity.User;
import org.example.testdemo.response.BaseResponse;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUser();
    int insertUser(User user);
    BaseResponse<SafeUser> updateUser(User user);
    int deleteUser(int id);

    BaseResponse<UserResponse> login(LoginRequest request);
    int register(RegisterRequest request);
}
