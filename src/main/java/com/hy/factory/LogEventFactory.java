package com.hy.factory;

import com.hy.entity.common.LogEvent;
import com.lmax.disruptor.EventFactory;

/**
 * Description: 通用日志事件类工厂
 * Author: yhong
 * Date: 2024/1/11
 */
public class LogEventFactory implements EventFactory<LogEvent> {

    @Override
    public LogEvent newInstance() {
        return new LogEvent();
    }
}
