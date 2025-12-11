package org.example.testdemo.factory;

import org.example.testdemo.strategy.FileProcessStrategy;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 简单的运行时工厂（通过 Spring 容器按名称获取策略）。
 * type: "video" / "image" / 其它
 * 选择对应的文件处理策略
 */
@Component
public class FileProcessFactory {
    private final ApplicationContext context;

    public FileProcessFactory(ApplicationContext context) {
        this.context = context;
    }

    public FileProcessStrategy getStrategy(String type) {
        if ("video".equalsIgnoreCase(type)) {
            return context.getBean("videoFileProcessStrategy", FileProcessStrategy.class);
        } else if ("image".equalsIgnoreCase(type) || "avatar".equalsIgnoreCase(type)) {
            return context.getBean("imageFileProcessStrategy", FileProcessStrategy.class);
        } else {
            throw new IllegalArgumentException("不支持的文件类型: " + type);
        }
    }
}
