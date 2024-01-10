package com.hy.factory;

import com.hy.entity.OriginLogMessage;
import com.lmax.disruptor.EventFactory;

/**
 * Description: 原始日志工厂
 * Author: yhong
 * Date: 2024/1/9
 */
public class OriginLogMessageFactory implements EventFactory<OriginLogMessage> {

    @Override
    public OriginLogMessage newInstance() {
        return new OriginLogMessage();
    }
}
