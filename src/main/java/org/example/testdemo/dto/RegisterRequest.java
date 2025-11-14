package org.example.testdemo.dto;
import lombok.Data;

@Data
public class RegisterRequest {
    private String account;
    private String password;
    private String checkPassword;
}
