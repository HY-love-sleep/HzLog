package com.hy.disruptor;

import com.hy.entity.LogEvent;
import com.hy.factory.LogEventFactory;
import com.hy.factory.LogThreadFactory;
import com.hy.handler.LogSenderHandler;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * Description: 存放清洗后的日志， 将其写入ES相关索引
 * Author: yhong
 * Date: 2024/1/10
 */
@Component
@Slf4j
public class CleanedLogDisruptor {
    private static final int CONSUMER_NUMS = 5;

    @Autowired
    private LogSenderHandler logSenderHandler;
    private Disruptor<LogEvent> disruptor;

    public CleanedLogDisruptor() {
        this.disruptor = null;
    }

    @PostConstruct
    public void init() {
        this.disruptor = new Disruptor<LogEvent>(
                new LogEventFactory(),
                1024 * 1024,
                new LogThreadFactory("CleanedLOG"),
                ProducerType.MULTI,
                new BlockingWaitStrategy()
        );

        // int numConsumers = Runtime.getRuntime().availableProcessors(); // 获取可用处理器核心数
        WorkHandler<LogEvent>[] handlers = new LogSenderHandler[CONSUMER_NUMS];
        Arrays.fill(handlers, logSenderHandler);
        this.disruptor.handleEventsWithWorkerPool(handlers);
        this.disruptor.start();
        log.info("CleanedLogDisruptor init success!");
    }

    public Disruptor<LogEvent> getDisruptor() {
        return this.disruptor;
    }
}
