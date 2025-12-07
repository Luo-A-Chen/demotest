package org.example.testdemo.service.Service;

import org.example.testdemo.entity.Video;

// 异步视频处理服务接口
public interface AsyncVideoService {


    void processVideo(Video video, byte[] videoBytes, String originalFileName);
}
