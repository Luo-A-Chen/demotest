package org.example.testdemo.strategy.storage;

import java.io.InputStream;

/**
 * 文件存储接口
 */
public interface FileStorage {
    /**
     * 存储文件流，返回外部可访问的 URL
     * @param objectName 存储时使用的对象名
     * @param inputStream 文件输入流
     * @return 文件对外访问 URL
     */
    String store(String objectName, InputStream inputStream);

    /**
     * 删除文件
     * @param url 文件对外访问 URL
     * @return 是否删除成功
     */
    boolean delete(String url);
}
