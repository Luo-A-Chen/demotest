package org.example.testdemo.strategy;

import org.example.testdemo.strategy.storage.FileStorage;
import org.example.testdemo.dto.response.FileProcessResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component("imageFileProcessStrategy")
public class ImageFileProcessStrategy implements FileProcessStrategy {

    private final FileStorage fileStorage;
    
    // 支持的图片格式
    private static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"
    );
    
    // 最大文件大小 (10MB)
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    public ImageFileProcessStrategy(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @Override
    public FileProcessResponse process(String originalFilename, byte[] bytes) throws Exception {
        // 1. 基础校验
        validateInput(originalFilename, bytes);
        
        // 2. 文件格式校验
        String ext = getExt(originalFilename);
        validateImageFormat(ext);
        
        // 3. 文件大小校验
        validateFileSize(bytes.length);
        
        // 4. 生成唯一文件名并上传
        String objectName = UUID.randomUUID() + ext;
        try (InputStream in = new ByteArrayInputStream(bytes)) {
            String url = fileStorage.store(objectName, in);
            return new FileProcessResponse(url, null, null);
        }
    }

    private void validateInput(String originalFilename, byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("图片内容为空");
        }
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }
    }

    private void validateImageFormat(String ext) {
        if (!SUPPORTED_EXTENSIONS.contains(ext.toLowerCase())) {
            throw new IllegalArgumentException("不支持的图片格式: " + ext + 
                    "，支持的格式: " + String.join(", ", SUPPORTED_EXTENSIONS));
        }
    }

    private void validateFileSize(long fileSize) {
        if (fileSize > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("图片文件过大，最大支持 " + 
                    (MAX_FILE_SIZE / 1024 / 1024) + "MB");
        }
    }

    private String getExt(String filename) {
        if (filename == null) {
            return ".jpg";
        }
        int idx = filename.lastIndexOf('.');
        return idx != -1 ? filename.substring(idx) : ".jpg";
    }
}
