package org.example.testdemo.service.ServiceImpl;

import io.minio.*;
import org.springframework.beans.factory.annotation.Value;
import org.example.testdemo.service.Service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

@Service("minioStorageServiceImpl")
public class MinioStorageServiceImpl implements FileStorageService {

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    private final MinioClient minioClient;

    @Autowired
    public MinioStorageServiceImpl(MinioClient minioClient) {
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
