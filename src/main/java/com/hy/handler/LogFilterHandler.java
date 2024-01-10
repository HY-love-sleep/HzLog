package com.hy.handler;

import com.hy.entity.OriginLogMessage;
import com.lmax.disruptor.EventHandler;
import org.springframework.stereotype.Component;

/**
 * Description: 清洗日志
 * Author: yhong
 * Date: 2024/1/9
 */
@Component
public class LogFilterHandler implements EventHandler<OriginLogMessage> {

    @Override
    public void onEvent(OriginLogMessage originLogMessage, long l, boolean b) throws Exception {

    }
}
