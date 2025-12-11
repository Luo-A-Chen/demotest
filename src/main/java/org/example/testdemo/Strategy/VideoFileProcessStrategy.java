package org.example.testdemo.strategy;

import org.example.testdemo.dto.response.FileProcessResponse;
import org.example.testdemo.strategy.storage.FileStorage;
import org.example.testdemo.util.VideoProcessorUtil;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;

@Component("videoFileProcessStrategy")
public class VideoFileProcessStrategy implements FileProcessStrategy {
    private final VideoProcessorUtil videoProcessorUtil;
    private final FileStorage storageClient;

    public VideoFileProcessStrategy(VideoProcessorUtil videoProcessorUtil, FileStorage storageClient) {
        this.videoProcessorUtil = videoProcessorUtil;
        this.storageClient = storageClient;
    }
    @Override
    public FileProcessResponse process(String originalFilename, byte[] bytes) throws Exception {
        // 1. 保存到临时目录（调用videoProcessorUtil工具类）
        Path tempPath = videoProcessorUtil.saveTempFile(originalFilename, bytes);

        try {
            // 2. 压缩视频（返回压缩后路径）
            String compressedPathStr = videoProcessorUtil.compress(tempPath);
            Path compressedPath = Path.of(compressedPathStr);

            // 3. 生成封面
            Path coverPath = videoProcessorUtil.generateCover(compressedPath);

            // 4. 获取时长
            int duration = videoProcessorUtil.getDuration(compressedPath);

            // 5. 上传压缩后的视频 & 封面到底层存储
            String videoObjectName = UUID.randomUUID().toString() + getExt(compressedPath.getFileName().toString());
            try (InputStream videoIn = new FileInputStream(compressedPath.toFile())) {
                String videoUrl = storageClient.store(videoObjectName, videoIn);

                String coverObjectName = UUID.randomUUID().toString() + getExt(coverPath.getFileName().toString());
                try (InputStream coverIn = new FileInputStream(coverPath.toFile())) {
                    String coverUrl = storageClient.store(coverObjectName, coverIn);

                    // 6. 返回结果
                    return new FileProcessResponse(videoUrl, coverUrl, duration);
                }
            }
        } finally {
            // 7. 清理临时文件（原始 + 压缩 + 封面）
            try { videoProcessorUtil.cleanup(tempPath); } catch (Exception ignored) {}
            // 其它临时文件名推导出来删除（compress、cover）
            try { videoProcessorUtil.cleanup(Path.of(tempPath.getParent().toString(), "compressed_" + tempPath.getFileName().toString())); } catch (Exception ignored) {}
            try { videoProcessorUtil.cleanup(Path.of(tempPath.getParent().toString(), tempPath.getFileName().toString().replace(".mp4","_cover.jpg"))); } catch (Exception ignored) {}
        }
    }

    private String getExt(String filename) {
        int idx = filename.lastIndexOf('.');
        return idx != -1 ? filename.substring(idx) : "";
    }
}
