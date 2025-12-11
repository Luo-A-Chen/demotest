package org.example.testdemo.service.Service;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;

/*
 ** 文件存储策略接口，策略模式
 *  后续可以拓展更多的存储策略
 */
public interface FileStorageStrategy {
        /**
         * 上传文件到存储服务
         *
         * @param file 本地文件
         * @return 文件访问路径
         */
        String save(File file);

        /**
         * 上传MultipartFile到存储服务
         *
         * @param multipartFile Spring MultipartFile
         * @return 文件访问路径
         */
        String saveMultipartFile(MultipartFile multipartFile);

        /**
         * 删除存储服务上的文件
         * @param fileUrl 文件访问路径
         * @return 是否删除成功
         */
        boolean delete(String fileUrl);
}
