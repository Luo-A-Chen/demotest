package org.example.testdemo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 头像上传请求DTO
 */
@Data
public class AvatarUploadRequest {
    /**
     * 上传的头像文件
     */
    private MultipartFile avatarFile;
}