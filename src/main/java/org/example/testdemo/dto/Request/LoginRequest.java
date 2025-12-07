package org.example.testdemo.dto.Request;

import lombok.Data;

@Data
public class LoginRequest {
    private String account;
    private String password;
}
