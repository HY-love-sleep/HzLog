package com.hy.factory;

import com.hy.entity.DBLogMessage;
import com.lmax.disruptor.EventFactory;

/**
 * Description: 清洗后的日志工厂
 * Author: yhong
 * Date: 2024/1/10
 */
public class DBLogMessageFactory implements EventFactory<DBLogMessage> {

    @Override
    public DBLogMessage newInstance() {
        return new DBLogMessage();
    }
}
