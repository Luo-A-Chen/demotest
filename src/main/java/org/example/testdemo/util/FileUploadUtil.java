package org.example.testdemo.util;

import org.example.testdemo.config.FileUploadConfig;
import org.example.testdemo.exception.BusinessException;
import org.example.testdemo.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件上传工具类
 */
@Component
public class FileUploadUtil {
    
    private final FileUploadConfig fileUploadConfig;

    public FileUploadUtil(FileUploadConfig fileUploadConfig) {
        this.fileUploadConfig = fileUploadConfig;
    }
    
    // 允许的头像文件类型
    private static final String[] ALLOWED_IMAGE_TYPES = {"image/jpeg", "image/png", "image/gif", "image/jpg"};
    
    // 最大文件大小（5MB）
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    
    /**
     * 上传头像文件
     * @param file 头像文件
     * @return 文件访问URL
     */
    public String uploadAvatar(MultipartFile file) {
        // 验证文件
        validateFile(file);
        
        try {
            // 创建上传目录
            File uploadDir = new File(fileUploadConfig.getAvatarUploadDir());
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
            
            // 保存文件
            Path filePath = Paths.get(fileUploadConfig.getAvatarUploadDir(), uniqueFileName);
            Files.copy(file.getInputStream(), filePath);
            
            // 返回文件访问URL（使用配置类动态生成）
            return fileUploadConfig.getAvatarFullUrl(uniqueFileName);
            
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }
    
    /**
     * 验证文件
     * @param file 上传的文件
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.FILE_EMPTY);
        }
        
        // 验证文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException(ErrorCode.FILE_TOO_LARGE);
        }
        
        // 验证文件类型
        String contentType = file.getContentType();
        boolean isValidType = false;
        for (String allowedType : ALLOWED_IMAGE_TYPES) {
            if (allowedType.equals(contentType)) {
                isValidType = true;
                break;
            }
        }
        
        if (!isValidType) {
            throw new BusinessException(ErrorCode.FILE_TYPE_NOT_SUPPORTED);
        }
    }
    
    /**
     * 获取文件扩展名
     * @param filename 文件名
     * @return 文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return ".jpg";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}