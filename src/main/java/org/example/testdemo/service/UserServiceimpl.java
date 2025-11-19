package org.example.testdemo.service;

import org.example.testdemo.dto.LoginRequest;
import org.example.testdemo.dto.RegisterRequest;
import org.example.testdemo.dto.UserResponse;
import org.example.testdemo.dto.SafeUser;
import org.example.testdemo.entity.User;
import org.example.testdemo.exception.BusinessException;
import org.example.testdemo.exception.ErrorCode;
import org.example.testdemo.mapper.UserMapper;
import org.example.testdemo.response.BaseResponse;
import org.example.testdemo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceimpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    @Override
    public User getUser() {
        String token= jwtUtil.getTokenFromRequest();
        String account= jwtUtil.extractUserAccount(token);
        return userMapper.getUserByAccount(account);
    }

    @Override
    public int insertUser(User user) {
        return userMapper.insertUser(user);
    }

    @Override
    public BaseResponse<SafeUser> updateUser(User user) {
        // 从JWT token中获取当前登录用户的账户信息
        String token = jwtUtil.getTokenFromRequest();
        String currentAccount = jwtUtil.extractUserAccount(token);
        
        // 根据账户查询当前用户
        User currentUser = userMapper.getUserByAccount(currentAccount);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        
        // 创建一个新的用户对象，只更新前端传递的字段
        User updateUser = new User();
        updateUser.setId(currentUser.getId());
        updateUser.setAccount(currentAccount); // 确保账户不被修改
        
        // 只更新前端传递的字段，保持其他字段不变
        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }
        if (user.getAvatar() != null) {
            updateUser.setAvatar(user.getAvatar());
        }
        if (user.getEmail() != null) {
            updateUser.setEmail(user.getEmail());
        }
        if (user.getProfile() != null) {
            updateUser.setProfile(user.getProfile());
        }
        
        userMapper.updateUser(updateUser);
        
        // 查询更新后的用户信息
        User updatedUser = userMapper.getUserByAccount(currentAccount);
        SafeUser safeUser = new SafeUser();
        safeUser.setName(updatedUser.getName());
        safeUser.setAvatar(updatedUser.getAvatar());
        safeUser.setAccount(updatedUser.getAccount());
        safeUser.setEmail(updatedUser.getEmail());
        safeUser.setProfile(updatedUser.getProfile());

        return BaseResponse.success(safeUser);
    }

    @Override
    public int deleteUser(int id) {
        return userMapper.deleteUser(id);
    }

    @Override
    public BaseResponse<UserResponse> login(LoginRequest request) {
        String account=request.getAccount();
        String password=request.getPassword();
        if(account == null || password == null){
            throw new BusinessException(ErrorCode.ACCOUNT_OR_PASSWORD_ERROR);
        }
        User user=userMapper.login(account,password);
        if(user==null){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        String token = jwtUtil.generateToken(user.getAccount());

        UserResponse result=new UserResponse();
        result.setId(user.getId());
        result.setName(user.getName());
        result.setAvatar(user.getAvatar());
        result.setAccount(user.getAccount());
        result.setEmail(user.getEmail());
        result.setProfile(user.getProfile());
        result.setToken(token);
        return BaseResponse.success(result);

    }

    @Override
    public int register(RegisterRequest request) {
        String account=request.getAccount();
        String password=request.getPassword();
        String checkPassword=request.getCheckPassword();
        if(account == null || password == null || checkPassword == null){
            throw new BusinessException(ErrorCode.ACCOUNT_OR_PASSWORD_ERROR);
        }
        if(account.length()<6||account.length()>20){
            throw new BusinessException(ErrorCode.ACCOUNT_OR_PASSWORD_ERROR);
        }
        if(password.length()<6||password.length()>20){
            throw new BusinessException(ErrorCode.ACCOUNT_OR_PASSWORD_ERROR);
        }
        if(!password.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        User existUser=userMapper.getUserByAccount(account);
        String accountPattern = "^[0-9]+$";
        if (!account.matches(accountPattern)) {
            throw new BusinessException(ErrorCode.ACCOUNT_INVALID);
        }
        if(existUser!=null){
            throw new BusinessException(ErrorCode.ACCOUNT_EXISTED);
        }
        User user=new User();
        user.setAccount(account);
        user.setPassword(password);
        int result=userMapper.insertUser(user);

        return 0;
    }
}
