package com.hy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hy.disruptor.OriginLogDisruptor;
import com.hy.entity.common.OriginLogMessage;
import com.lmax.disruptor.RingBuffer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Description: kafka拉取消息
 * Author: yhong
 * Date: 2024/1/11
 */
@Slf4j
@Service
public class KafkaPollMessageService implements DisposableBean {
    private final KafkaConsumer<String, String> consumer;
    private final OriginLogDisruptor disruptor;
    private volatile boolean running = true;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public KafkaPollMessageService(KafkaConsumer<String, String> consumer, OriginLogDisruptor disruptor) {
        this.consumer = consumer;
        this.disruptor = disruptor;
    }

    /**
     * 异步拉取kafka消息
     * disruptor发布消息到origin_log_ringbuffer中
     */
    @Async("asyncExecutor")
    public void startConsuming() {
        log.info("kafka consumer startConsuming!");
        while (running) {
            ConsumerRecords<String, String> records;
            try {
                records = consumer.poll(Duration.ofMillis(2000));
                for (ConsumerRecord<String, String> record : records) {
                    OriginLogMessage message = objectMapper.readValue(record.value(), OriginLogMessage.class);

                    RingBuffer<OriginLogMessage> ringBuffer = disruptor.getDisruptor().getRingBuffer();
                    long sequence = ringBuffer.next();
                    try {
                        OriginLogMessage event = ringBuffer.get(sequence);
                        event.setOriginLogMessage(message.getOriginLogMessage());
                        event.setLogType(message.getLogType());
                    } finally {
                        ringBuffer.publish(sequence);
                    }
                }
            } catch (Exception e) {
                log.error("kafka 拉取消息失败， error:{}", e.getMessage());
            }
        }
    }

    // 继承了DisposableBean接口, spring容器关闭时自动调用destroy方法
    @Override
    public void destroy() throws Exception {
        this.running = false;
        if (consumer != null) {
            consumer.close();
        }
    }
}
