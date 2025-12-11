package org.example.testdemo.strategy.storage;

import io.minio.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * MinIO 文件存储实现
 */
@Component("minioStorage")
public class MinioStorage implements FileStorage {
    private final MinioClient minioClient;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Autowired
    public MinioStorage(MinioClient minioClient) {
        this.minioClient = minioClient;
    }
    @PostConstruct
    public void init() {
        // 确保 bucket 存在（不阻塞构造）
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("MinIO 初始化失败: " + e.getMessage(), e);
        }
    }
    @Override
    public String store(String objectName, InputStream inputStream) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, -1, 10 * 1024 * 1024)
                            .build()
            );
            return endpoint + "/" + bucketName + "/" + objectName;
        } catch (Exception e) {
            throw new RuntimeException("MinIO 上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(String url) {
        try {
            String objectName = url.substring(url.lastIndexOf("/") + 1);
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return true;
        } catch (Exception e) {
            throw new RuntimeException("MinIO 删除失败: " + e.getMessage(), e);
        }
    }
}
