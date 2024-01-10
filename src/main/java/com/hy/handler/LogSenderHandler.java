package com.hy.handler;

import com.hy.entity.DBLogMessage;
import com.lmax.disruptor.EventHandler;
import org.springframework.stereotype.Component;

/**
 * Description: 发送日志到es
 * Author: yhong
 * Date: 2024/1/9
 */
// todo: 不通类型日志对应不同模板，可应用策略模式实现
@Component
public class LogSenderHandler implements EventHandler<DBLogMessage> {

    @Override
    public void onEvent(DBLogMessage originLogMessage, long l, boolean b) throws Exception {

    }
}
