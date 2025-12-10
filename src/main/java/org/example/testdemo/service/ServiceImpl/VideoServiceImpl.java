package org.example.testdemo.service.ServiceImpl;

import com.alibaba.fastjson2.JSON;
import org.example.testdemo.dto.response.VideoResponse;
import org.example.testdemo.entity.Video;
import org.example.testdemo.mapper.VideoMapper;
import org.example.testdemo.service.Service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final Object lock = new Object();

    @Override
    public boolean save(Video video) {
        try {
            int result = videoMapper.insert(video);
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("保存视频信息失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateById(Video video) {
        try {
            int result = videoMapper.updateById(video);
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("更新视频信息失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<VideoResponse> getRecommendList() {
        return videoMapper.getRecommendList();
    }

    @Override
    public List<VideoResponse> getRecommendWithCache() {

        String key = "video:recommend";

        // 第一层缓存查询
        String cacheJson = redisTemplate.opsForValue().get(key);
        if (cacheJson != null) {
            return JSON.parseArray(cacheJson, VideoResponse.class);
        }

        // 双重检查锁
        synchronized (lock) {

            cacheJson = redisTemplate.opsForValue().get(key);
            if (cacheJson != null) {
                return JSON.parseArray(cacheJson, VideoResponse.class);
            }

            // 查数据库
            List<VideoResponse> list = getRecommendList();

            if (list == null || list.isEmpty()) {
                redisTemplate.opsForValue().set(key, "[]", 5, TimeUnit.MINUTES);
                return list;
            }

            // 写缓存（带随机过期）
            int expire = 10 + (int)(Math.random() * 5);
            redisTemplate.opsForValue().set(
                    key,
                    JSON.toJSONString(list),
                    expire,
                    TimeUnit.MINUTES
            );

            return list;
        }
    }
}
