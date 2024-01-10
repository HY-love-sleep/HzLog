package com.hy.handler;

import com.hy.disruptor.CleanedLogDisruptor;
import com.hy.entity.DBLogMessage;
import com.hy.entity.OriginLogMessage;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description: 清洗日志
 * Author: yhong
 * Date: 2024/1/9
 */
@Component
public class LogFilterHandler implements EventHandler<OriginLogMessage> {
    @Autowired
    private CleanedLogDisruptor disruptor;

    @Override
    public void onEvent(OriginLogMessage originLogMessage, long l, boolean b) throws Exception {
        // 1、清洗日志
        DBLogMessage dbLogMessage = clean(originLogMessage);
        assert dbLogMessage != null;
        // 2、将清洗好的日志Event存入CleanedLogDisruptor
        RingBuffer<DBLogMessage> ringBuffer = disruptor.getDisruptor().getRingBuffer();
        long sequence = ringBuffer.next();
        try {
            extracted(dbLogMessage, ringBuffer, sequence);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    /**
     * 将清洗后的dbLogMessage转为CleanedLogDisruptor里的event
     * @param dbLogMessage
     * @param ringBuffer
     * @param sequence
     */
    private static void extracted(DBLogMessage dbLogMessage, RingBuffer<DBLogMessage> ringBuffer, long sequence) {
        DBLogMessage event = ringBuffer.get(sequence);
        event.setTimestamp(dbLogMessage.getTimestamp());
        event.setEvent(dbLogMessage.getEvent());
        event.setTags(dbLogMessage.getTags());
        event.setMessage(dbLogMessage.getMessage());
        event.setFields(dbLogMessage.getFields());
        event.setDestination(dbLogMessage.getDestination());
        event.setClient(dbLogMessage.getClient());
        event.setUser(dbLogMessage.getUser());
        event.setQuery(dbLogMessage.getQuery());
        event.setSqlOut(dbLogMessage.getSqlOut());
        event.setPath(dbLogMessage.getPath());
        event.setType(dbLogMessage.getType());
        event.setResult(dbLogMessage.getResult());
        event.setOrganization(dbLogMessage.getOrganization());
    }

    /**
     * todo:清洗原始日志为DBLogMessage
     * @param originLogMessage
     * @return
     */
    private DBLogMessage clean(OriginLogMessage originLogMessage) {
        return null;
    }

}
