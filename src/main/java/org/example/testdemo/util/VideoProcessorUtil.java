package org.example.testdemo.util;

import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class VideoProcessorUtil {

    private static final String TEMP_DIR="temp";//临时目录

    /*
     * 保存MultipartFile到临时目录
     */
    public Path saveTempFile(String originalFileName,byte[] bytes) throws  Exception {
        Path tempDir =Paths.get(TEMP_DIR);
        if(!Files.exists(tempDir)){
            Files.createDirectories(tempDir);
        }
        Path tempFile=tempDir.resolve(originalFileName);
        Files.write(tempFile,bytes);
        return tempFile;

    }
    
    /**
     * 压缩视频文件
     * @param inputFilePath 视频本地文件路径
     * @return 压缩后的视频文件路径
     */
    public String compress(Path inputFilePath) {
        // 这里实现视频压缩逻辑
        try {
            if(!Files.exists(inputFilePath)||Files.isDirectory(inputFilePath)){
                throw new IllegalArgumentException("输入路径不存在或不是文件");
            }
            String compressedFile="compressed_"+inputFilePath.getFileName().toString();
            Path output=inputFilePath.getParent().resolve(compressedFile);

            ProcessBuilder pb=new ProcessBuilder(
                    "ffmpeg",
                    "-i", inputFilePath.toString(),
                    "-c:v", "libx264",
                    "-crf", "20",
                    "-preset", "medium",
                    output.toString()
            );
            pb.redirectErrorStream(true);
            Process process=pb.start();
            //读取ffmpeg输出，防止阻塞
            try(BufferedReader br=new BufferedReader(new InputStreamReader(process.getInputStream()));){
                while(br.readLine()!=null){}
            }
            int exit=process.waitFor();
            if(exit!=0){
                throw new RuntimeException("视频压缩失败");
            }
            return output.toString();
            
        } catch (Exception e) {
            throw new RuntimeException("视频压缩失败: " + e.getMessage(), e);
        }
    }

    /**
     * 生成视频封面图
     * @param inputFilePath 视频文件路径
     * @return 封面图文件路径
     */
    public Path generateCover(Path inputFilePath) {
        // 这里实现封面图生成逻辑
        try {
            String coverName=inputFilePath.getFileName().toString().replace(".mp4","_cover.jpg");
            Path output=inputFilePath.getParent().resolve(coverName);

            ProcessBuilder pb=new ProcessBuilder(
                    "ffmpeg",
                    "-i", inputFilePath.toString(),
                    "-ss", "00:00:01.000",
                    "-frames:v", "1",
                    output.toString()
            );
            pb.redirectErrorStream(true);
            Process process=pb.start();

            try(BufferedReader br=new BufferedReader(new InputStreamReader(process.getInputStream()))){
                while(br.readLine()!=null){}
            }

            int exit=process.waitFor();
            if(exit!=0){
                throw new RuntimeException("封面图生成失败");
            }
            return output;
        } catch (Exception e) {
            throw new RuntimeException("封面图生成失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取视频时长
     * @param inputFilePath 视频文件路径
     * @return 视频时长（秒）
     */
    public int getDuration(Path inputFilePath) {
        // 这里实现视频时长获取逻辑
        try {
            ProcessBuilder pb=new ProcessBuilder(
                    "ffmpeg",
                    "-i", inputFilePath.toString()
            );
            pb.redirectErrorStream(true);
            Process process=pb.start();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.toLowerCase().contains("duration")) {
                        String[] parts = line.split("Duration:")[1].trim().split(",")[0].split(":");
                        double seconds = Integer.parseInt(parts[0]) * 3600
                                + Integer.parseInt(parts[1]) * 60
                                + Double.parseDouble(parts[2]);
                        return (int) seconds;
                    }
                }
            }
            throw new RuntimeException("未找到视频时长信息");
        } catch (Exception e) {
            throw new RuntimeException("获取视频时长失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 检查视频文件是否存在
     * @param videoUrl 视频文件路径
     * @return 文件是否存在
     */
    public boolean videoExists(String videoUrl) {
        try {
            File videoFile = new File(videoUrl);
            return videoFile.exists() && videoFile.isFile();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 清理临时文件
     */
    public void cleanup(Path filePath) {
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (Exception ignored) {}
    }
}