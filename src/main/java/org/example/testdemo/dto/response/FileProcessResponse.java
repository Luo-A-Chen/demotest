package org.example.testdemo.dto.response;

import lombok.Data;

/**
 * 文件处理结果DTO
 */
@Data
public class FileProcessResponse {
    private String mainUrl;   // 对于视频就是视频地址，对于图片就是图片地址
    private String coverUrl;  // 视频封面（图片）— 图片时可以为 null
    private Integer duration; // 秒，视频时长
    public FileProcessResponse() {}
    public FileProcessResponse(String mainUrl, String coverUrl, Integer duration) {
        this.mainUrl = mainUrl;
        this.coverUrl = coverUrl;
        this.duration = duration;
    }
}
