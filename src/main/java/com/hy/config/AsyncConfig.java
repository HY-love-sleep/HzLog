package com.hy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Description: 异步任务执行器
 * Author: yhong
 * Date: 2024/1/10
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "asyncExecutor")
    public TaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // 设置核心线程池大小
        executor.setMaxPoolSize(10); // 设置最大线程池大小
        executor.setQueueCapacity(20); // 设置队列容量
        executor.setThreadNamePrefix("async-"); // 设置线程名前缀
        executor.initialize();
        return executor;
    }
}
