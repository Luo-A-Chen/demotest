package org.example.testdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("video")
public class Video {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 视频标题
     */
    private String title;
     /**
     * 视频描述
     */
    private String description;

    /**
     * 视频上传用户id
     */
    private Integer userId;

    /**
     * 视频封面
     */
    private String cover;

    /**
     * 视频地址
     */
    private String videoUrl;

    /**
     * 视频时长
     */
    private Integer duration;

    /**
     * 视频状态 0-处理中 1-正常 2-删除 3-处理失败
     */
    private Integer status;

    /**
     * 视频类型 0-普通视频 1-广告视频
     */
    private Integer type;

    /**
     * 视频创建时间
     */
    private Date createTime;

    /**
     * 视频更新时间
     */
    private Date updateTime;
}
