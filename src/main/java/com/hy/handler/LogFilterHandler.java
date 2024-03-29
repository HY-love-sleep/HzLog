package com.hy.handler;

import com.hy.common.LogType;
import com.hy.disruptor.CleanedLogDisruptor;
import com.hy.entity.common.BaseLog;
import com.hy.entity.common.LogEvent;
import com.hy.entity.common.OriginLogMessage;
import com.hy.service.strategy.LogCleanStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description: 清洗日志
 * Author: yhong
 * Date: 2024/1/9
 */
@Slf4j
@Component
public class LogFilterHandler implements WorkHandler<OriginLogMessage> {

    private final CleanedLogDisruptor disruptor;

    private final List<LogCleanStrategy> strategies;

    @Autowired
    public LogFilterHandler(CleanedLogDisruptor disruptor, List<LogCleanStrategy> strategies) {
        this.disruptor = disruptor;
        this.strategies = strategies;
    }


    @Override
    public void onEvent(OriginLogMessage originLogMessage) throws Exception {
        // 1、清洗日志
        BaseLog baseLog = cleanLog(originLogMessage);
        // 2、将清洗好的日志Event存入CleanedLogDisruptor
        RingBuffer<LogEvent> ringBuffer = disruptor.getDisruptor().getRingBuffer();
        long sequence = ringBuffer.next();
        try {
            LogEvent logEvent = ringBuffer.get(sequence);
            logEvent.setLog(baseLog);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    // 通过传入日志类型由策略方法来决定调用哪种日志的清洗逻辑；
    private BaseLog cleanLog(OriginLogMessage originLogMessage) throws Exception {
        LogType logType = LogType.fromString(originLogMessage.getLogType());
        return strategies.stream()
                .filter(strategies ->
                    strategies.supports(logType)
                )
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("日志类型不匹配, 日志类型： " + logType.getLogTypeName()))
                .clean(originLogMessage);
    }
}
