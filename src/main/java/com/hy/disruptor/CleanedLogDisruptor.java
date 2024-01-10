package com.hy.disruptor;

import com.hy.entity.DBLogMessage;
import com.hy.factory.DBLogMessageFactory;
import com.hy.factory.LogThreadFactory;
import com.hy.handler.LogSenderHandler;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Description: 存放清洗后的日志， 将其写入ES相关索引
 * Author: yhong
 * Date: 2024/1/10
 */
@Component
@Slf4j
public class CleanedLogDisruptor {

    @Autowired
    private LogSenderHandler logSenderHandler;
    private Disruptor<DBLogMessage> disruptor;

    public CleanedLogDisruptor() {
        this.disruptor = null;
    }

    @PostConstruct
    public void init() {
        this.disruptor = new Disruptor<DBLogMessage>(
                new DBLogMessageFactory(),
                1024 * 1024,
                new LogThreadFactory("CleanedLOG"),
                ProducerType.MULTI,
                new BlockingWaitStrategy()
        );

        this.disruptor.handleEventsWith(logSenderHandler);
        this.disruptor.start();
        log.info("CleanedLogDisruptor init success!");
    }

    public Disruptor<DBLogMessage> getDisruptor() {
        return this.disruptor;
    }
}
