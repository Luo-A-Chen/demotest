package org.example.testdemo.dto.response;
import lombok.Data;

@Data
public class UserResponse {
        private String token;

        private int id;
        private String name;
        private String avatar;
        private String account;
        private String email;
        private String profile;
}
