package com.hy.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(100,
                200,
                2000,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(10000), r -> {
            Thread t = new Thread(r);
            t.setName("disruptor-thread-pool");
            t.setUncaughtExceptionHandler((t1, e) -> {
                log.error("{} ---异常：{}", t1.getName(), e.getMessage());
            });
            return t;
        },
                new ThreadPoolExecutor.CallerRunsPolicy());  // 交由主线程处理
    }
}
