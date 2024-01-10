package com.hy.handler;

import com.hy.entity.DBLogMessage;
import com.lmax.disruptor.EventHandler;
import org.springframework.stereotype.Component;

/**
 * Description: 发送日志到es
 * Author: yhong
 * Date: 2024/1/9
 */
@Component
public class LogSenderHandler implements EventHandler<DBLogMessage> {

    @Override
    public void onEvent(DBLogMessage dbLogMessage, long l, boolean b) throws Exception {

    }
}
