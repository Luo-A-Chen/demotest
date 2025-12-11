package org.example.testdemo.service.ServiceImpl;

import jakarta.annotation.Resource;
import org.example.testdemo.entity.Video;
import org.example.testdemo.service.Service.AsyncVideoService;
import org.example.testdemo.service.Service.FileStorageStrategy;
import org.example.testdemo.service.Service.VideoService;
import org.example.testdemo.util.VideoProcessorUtil;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.concurrent.Executor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AsyncVideoServiceImpl implements AsyncVideoService {
    @Resource
    private Executor videoExecutor;

    @Resource
    private VideoService videoService;

    @Resource
    private VideoProcessorUtil videoProcessorUtil;

    @Resource(name ="minioStorageServiceImpl")
    private FileStorageStrategy minioService;

    @Override
    public void processVideo(Video video, byte[] videoBytes, String originalFileName) {
        videoExecutor.execute(() -> {
            Path tempFile=null;
            Path compressedFile=null;
            Path coverFile=null;
            try{
                //0.保存临时文件
                tempFile= videoProcessorUtil.saveTempFile(originalFileName,videoBytes);
                //1.压缩视频
                compressedFile =Path.of(videoProcessorUtil.compress(tempFile));
                //2.生成封面图
                coverFile= videoProcessorUtil.generateCover(compressedFile);
                //3.获取视频时长
                int duration= videoProcessorUtil.getDuration(compressedFile);
                //3.5 上传压缩视频和封面到Minio
                String newVideoUrl = minioService.save(compressedFile.toFile());
                String coverUrl = minioService.save(coverFile.toFile());
                //4.更新视频信息
                Video update = new Video();
                update.setId(video.getId());
                update.setVideoUrl(newVideoUrl);
                update.setCover(coverUrl);
                update.setDuration(duration);
                update.setStatus(1);
                //5.更新数据库
                videoService.updateById(update);
            }catch(Exception e){
                //6.处理失败更新数据库
                Video fail = new Video();
                fail.setId(video.getId());
                fail.setStatus(3);
                videoService.updateById(fail);
                log.error("视频处理失败 视频ID:{} 异常信息:{}", video.getId(), e.getMessage(), e);
            }finally{
                //7.清理临时文件
                if (tempFile != null) videoProcessorUtil.cleanup(tempFile);
                if (compressedFile != null) videoProcessorUtil.cleanup(compressedFile);
                if (coverFile != null) videoProcessorUtil.cleanup(coverFile);
            }
        });
    }
}
