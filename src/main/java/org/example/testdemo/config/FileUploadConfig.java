package org.example.testdemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

/**
 * 文件上传配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadConfig {
    
    /**
     * 服务器地址
     */
    @Value("${file.upload.server-url}")
    private String serverUrl;
    
    /**
     * 头像上传目录
     */
    @Value("${file.upload.avatar-upload-dir}")
    private String avatarUploadDir;
    
    /**
     * 头像访问路径
     */
    @Value("${file.upload.avatar-access-path}")
    private String avatarAccessPath;
    
    /**
     * 获取完整的头像访问URL
     */
    public String getAvatarFullUrl(String filename) {
        return serverUrl + avatarAccessPath + filename;
    }
}