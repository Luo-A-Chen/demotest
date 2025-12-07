package org.example.testdemo.service.Service;


import java.io.File;

// 文件存储服务接口
public interface FileStorageService {
        /**
         * 上传文件到存储服务
         *
         * @param file 本地文件
         * @return 文件访问路径
         */
        String save(File file);

        /**
         * 删除存储服务上的文件
         * @param fileUrl 文件访问路径
         * @return 是否删除成功
         */
        boolean delete(String fileUrl);
}
