package org.example.testdemo.dto.response;

import lombok.Data;

@Data
public class VideoResponse {
    private Long id;//视频主键
    private String title;//视频标题
     private String description;//视频描述
     private String cover;//视频封面
     private String videoUrl;//视频地址
     private Integer duration;//视频时长
     private Integer status;//视频状态 0-处理中 1-正常 2-删除 3-处理失败
     private Integer type;//视频类型 0-普通视频 1-广告视频
}
