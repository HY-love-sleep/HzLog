package com.hy.disruptor;

import com.hy.entity.OriginLogMessage;
import com.hy.factory.OriginLogMessageFactory;
import com.hy.factory.LogThreadFactory;
import com.hy.handler.LogFilterHandler;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Description: 原始日志存放到disruptor进行处理
 * Author: yhong
 * Date: 2024/1/9
 */
@Component
@Slf4j
public class OriginLogDisruptor {
    private Disruptor<OriginLogMessage> disruptor;

    public OriginLogDisruptor() {
        this.disruptor = null;
    }

    @Autowired
    private LogFilterHandler filterHandler;

    @PostConstruct
    public void init() {
        this.disruptor = new Disruptor<OriginLogMessage>(
            new OriginLogMessageFactory(),
                1024 * 1024,
                new LogThreadFactory("OriginLOG"),
                ProducerType.MULTI,
                new BlockingWaitStrategy()
        );

        this.disruptor.handleEventsWith(filterHandler);
        this.disruptor.start();
        log.info("OriginLogDisruptor init success!");
    }

    public Disruptor<OriginLogMessage> getDisruptor() {
        return this.disruptor;
    }
}
