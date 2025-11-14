package org.example.testdemo.service;

import org.example.testdemo.dto.LoginRequest;
import org.example.testdemo.dto.RegisterRequest;
import org.example.testdemo.dto.UserResponse;
import org.example.testdemo.entity.User;
import org.example.testdemo.response.BaseResponse;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(int id);
    int insertUser(User user);
    int updateUser(User user);
    int deleteUser(int id);

    BaseResponse<UserResponse> login(LoginRequest request);
    int register(RegisterRequest request);
}
