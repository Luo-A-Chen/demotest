package org.example.testdemo.service.Service;

import org.example.testdemo.dto.response.VideoResponse;
import org.example.testdemo.entity.Video;

import java.util.List;

// 数据库视频存储服务接口
public interface VideoService {
    
    /**
     * 保存视频信息
     * @param video 视频对象
     * @return 保存是否成功
     */
    boolean save(Video video);
    
    /**
     * 根据ID更新视频信息
     * @param video 视频对象
     * @return 更新是否成功
     */
    boolean updateById(Video video);

    /**
     * 获取推荐视频列表
     * @return 视频列表
     */
    List<VideoResponse> getRecommendList();

     /**
      * 获取推荐视频列表（带缓存）
      * @return 视频列表
      */
    List<VideoResponse> getRecommendWithCache();
}
