package org.example.testdemo.strategy;

import org.example.testdemo.dto.response.FileProcessResponse;

/**
 * 文件处理策略
 * 图片，直接保存，返回rul
 * 视频，压缩xxxx，返回url+封面+时长
 */
public interface FileProcessStrategy {
    /**
     * 处理文件（字节数组），返回处理后可对外访问的结果（可能包含多个 URL）
     * @param originalFilename 原始文件名（用于推断扩展名）
     * @param bytes 文件字节
     * @return 处理结果
     */
    FileProcessResponse process(String originalFilename, byte[] bytes) throws Exception;

}
