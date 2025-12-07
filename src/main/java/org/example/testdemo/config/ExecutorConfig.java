package org.example.testdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//线程池配置
@Configuration
public class ExecutorConfig {
    @Bean
    public Executor videoExecutor(){
        return new ThreadPoolExecutor(
                5,10,
                60, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
