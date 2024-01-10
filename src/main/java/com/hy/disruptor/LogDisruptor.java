package com.hy.disruptor;

import com.hy.entity.LogMessage;
import com.hy.factory.LogMessageFactory;
import com.hy.factory.LogThreadFactory;
import com.hy.handler.LogFilterHandler;
import com.hy.handler.LogSenderHandler;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
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
public class LogDisruptor {
    private Disruptor<LogMessage> disruptor;

    public LogDisruptor() {
        this.disruptor = null;
    }

    @Autowired
    private LogFilterHandler filterHandler;

    @Autowired
    private LogSenderHandler senderHandler;

    @PostConstruct
    public void init() {
        this.disruptor = new Disruptor<LogMessage>(
            new LogMessageFactory(),
                1024 * 1024,
                new LogThreadFactory("LOG"),
                ProducerType.SINGLE,
                new BlockingWaitStrategy()
        );

        // 链式添加消费者程序， 先进行日志清洗， 再进行数据入库
        // todo: 1、构建多线程消费者工作池  or 2、构建两个disruptor，分别进行清洗和入库， 内部再使用线程池
        EventHandlerGroup<LogMessage> logMessageEventHandlerGroup = this.disruptor.handleEventsWith(filterHandler);
        logMessageEventHandlerGroup.then(senderHandler);

        this.disruptor.start();
        log.info("Disruptor init success!");
    }

    public Disruptor<LogMessage> getDisruptor() {
        return this.disruptor;
    }
}
