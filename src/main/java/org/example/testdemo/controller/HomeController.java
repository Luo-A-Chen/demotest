package org.example.testdemo.controller;

import org.example.testdemo.dto.response.BaseResponse;
import org.example.testdemo.dto.response.VideoResponse;
import org.example.testdemo.service.Service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private VideoService videoService;

    @GetMapping("/recommend")
    public BaseResponse<List<VideoResponse>> recommend() {
        return BaseResponse.success(videoService.getRecommendWithCache());
    }
}

