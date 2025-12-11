package org.example.testdemo.service.Service;

import org.example.testdemo.dto.Request.LoginRequest;
import org.example.testdemo.dto.Request.RegisterRequest;
import org.example.testdemo.dto.response.UserResponse;
import org.example.testdemo.dto.SafeUser;
import org.example.testdemo.entity.User;
import org.example.testdemo.dto.response.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {
    List<User> getAllUsers();
    User getUser();
    int insertUser(User user);
    BaseResponse<SafeUser> updateUser(User user);
    int deleteUser(int id);

    BaseResponse<UserResponse> login(LoginRequest request);
    int register(RegisterRequest request);
    
    BaseResponse<String> uploadAvatar(MultipartFile avatarFile);
}
