package org.example.testdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.testdemo.dto.response.VideoResponse;
import org.example.testdemo.entity.Video;

import java.util.List;

@Mapper
public interface VideoMapper extends BaseMapper<Video> {

    List<VideoResponse> getRecommendList();
}
