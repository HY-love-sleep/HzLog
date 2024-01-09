package com.hy.factory;

import com.hy.entity.LogMessage;
import com.lmax.disruptor.EventFactory;

/**
 * Description: 原始日志工厂
 * Author: yhong
 * Date: 2024/1/9
 */
public class LogMessageFactory implements EventFactory<LogMessage> {

    @Override
    public LogMessage newInstance() {
        return new LogMessage();
    }
}
