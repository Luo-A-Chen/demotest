package org.example.testdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.testdemo.mapper")
public class TestdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestdemoApplication.class, args);
    }

}
