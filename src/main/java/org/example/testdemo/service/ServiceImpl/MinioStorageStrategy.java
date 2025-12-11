package org.example.testdemo.service.ServiceImpl;

import io.minio.*;
import org.springframework.beans.factory.annotation.Value;
import org.example.testdemo.service.Service.FileStorageStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

@Service("minioStorageServiceImpl")
public class MinioStorageStrategy implements FileStorageStrategy {

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    private final MinioClient minioClient;

    @Autowired
    public MinioStorageStrategy(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public String save(File file) {
        try {
            //1.校验 bucket桶
            createBucketIfNotExists();
            //2.自动生成文件名（唯一）
            String originalName = file.getName();
            String ext = "";
            if (originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID() + ext;
            //3.上传文件到 MinIO
            try (FileInputStream fis = new FileInputStream(file)) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .stream(fis, file.length(), -1)
                                .contentType("application/octet-stream")
                                .build()
                );
            }
            //4.返回文件访问路径
            return endpoint + "/" + bucketName + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("MinIO 文件上传失败" + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(String fileUrl) {
        try{
            String objectName = fileUrl.substring(fileUrl.lastIndexOf("/"));

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            return true;
        }catch (Exception e){
            throw new RuntimeException("MinIO 文件删除失败" + e.getMessage(), e);
        }
    }

    @Override
    public String saveMultipartFile(MultipartFile multipartFile) {
        try {
            // 验证文件
            validateMultipartFile(multipartFile);
            
            // 创建存储桶（如果不存在）
            createBucketIfNotExists();
            
            // 生成唯一文件名
            String originalFilename = multipartFile.getOriginalFilename();
            String ext = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID() + ext;
            
            // 上传文件到MinIO
            try (InputStream inputStream = multipartFile.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .stream(inputStream, multipartFile.getSize(), -1)
                                .contentType(multipartFile.getContentType())
                                .build()
                );
            }
            
            // 返回文件访问路径
            return endpoint + "/" + bucketName + "/" + fileName;
            
        } catch (Exception e) {
            throw new RuntimeException("MinIO MultipartFile上传失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 验证MultipartFile
     */
    private void validateMultipartFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        
        // 文件大小限制（10MB）
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("文件大小不能超过10MB");
        }
        
        // 文件类型验证（只支持图片）
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("只支持图片文件上传");
        }
    }
    /*
     * 创建 MinIO 存储桶（如果不存在）
     *
     * @param bucketName 存储桶名称
     */
    public void createBucketIfNotExists() throws Exception{
        boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
        );
        if(!exists){
            minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucketName).build()
            );
        }
    }
}
