package com.hy.factory;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;

/**
 * Description: 处理日志的线程工厂
 * Author: yhong
 * Date: 2024/1/9
 */
@Slf4j
public class LogThreadFactory implements ThreadFactory {
    private String prefix;

    public LogThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(prefix + "--Thread--");
        thread.setUncaughtExceptionHandler((t, e) -> {
            log.error("{}抛出异常：{}", t.getName(), e.getMessage());
        });
        return thread;
    }
}
