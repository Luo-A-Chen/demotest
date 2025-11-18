package org.example.testdemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.testdemo.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> getAllUsers();
    int insertUser(User user);
    int updateUser(User user);
    int deleteUser(int id);

    User getUserByAccount(String account);
    User login(String account, String password);
}
