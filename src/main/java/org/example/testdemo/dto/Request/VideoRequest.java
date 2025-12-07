package org.example.testdemo.dto.Request;

import lombok.Data;

@Data
public class VideoRequest {
        /**
         * 视频标题
         */
        private String title;

        /**
         * 视频描述
         */
        private String description;

}
