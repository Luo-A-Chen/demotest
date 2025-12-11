package org.example.testdemo.controller;

import org.example.testdemo.dto.Request.VideoRequest;
import org.example.testdemo.entity.Video;
import org.example.testdemo.dto.response.BaseResponse;
import org.example.testdemo.service.ServiceImpl.AsyncVideoServiceImpl;
import org.example.testdemo.service.Service.FileStorageStrategy;
import org.example.testdemo.service.Service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private AsyncVideoServiceImpl asyncVideoService;

    @Autowired
    @Qualifier("minioStorageServiceImpl")
    private FileStorageStrategy fileStorageService;

    @PostMapping("/upload")
    public BaseResponse<Video> upload(@RequestPart("info") VideoRequest req,
                                      @RequestPart("file") MultipartFile file) throws IOException {

        //1.获取视频内容，获取文件字节数组
        byte[] bytes = file.getBytes();
        String originalFileName = file.getOriginalFilename();

        //2.插入数据库
        Video video = new Video();
        video.setTitle(req.getTitle());
        video.setDescription(req.getDescription());
        video.setStatus(0);
        videoService.save(video);

        //3.提交任务处理到线程池
        asyncVideoService.processVideo(video, bytes, originalFileName);

        return BaseResponse.success(video);
    }
}
