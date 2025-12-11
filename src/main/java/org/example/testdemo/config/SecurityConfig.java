package org.example.testdemo.config;

import org.example.testdemo.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 安全配置类
 * 配置Spring Security的安全策略，包括CORS、CSRF、HTTP Basic认证、会话管理和请求授权。
 * 目前使用JWT认证。
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig{
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors ->{})
                .csrf(AbstractHttpConfigurer::disable)// 禁用CSRF防护
                .httpBasic(AbstractHttpConfigurer::disable)// 禁用HTTP Basic认证
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )// 配置会话管理为无状态，不使用HTTP会话
                .authorizeHttpRequests(authz -> authz
                        // 登录、注册接口开放
                        // todo
                        // 这里的user接口里面还包含了除登录注册以外的其他方法，后续可以优化
                        .requestMatchers("/user/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/static/**", "/templates/**").permitAll()
                        // 需要认证的接口
                        .anyRequest().authenticated()
                )
                // 添加JWT过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(); // 空用户管理器
    }
}
