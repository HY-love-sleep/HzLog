package com.hy.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hy.disruptor.OriginLogDisruptor;
import com.hy.entity.OriginLogMessage;
import com.lmax.disruptor.RingBuffer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * Description: 拉模式的kafka消费者
 * Author: yhong
 * Date: 2024/1/10
 */
@Slf4j
@Component
public class KafkaPollConsumer {
    private KafkaConsumer<String, String> consumer;

    @Autowired
    private OriginLogDisruptor disruptor;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaPollConsumer() {
        this.consumer = null;
    }

    @PostConstruct
    public void init() {
        initializeConsumer();
        startConsuming();
    }


    public void initializeConsumer() {
        Properties props = new Properties();
        // 配置Kafka连接属性
        props.put("bootstrap.servers", "192.168.174.132:9092");
        props.put("group.id", "log-info-group");
        props.put("max.poll.records", "500"); // 设置每次拉取的最大记录数为100
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        this.consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("log-info"));
    }


    @Async("asyncExecutor")
    public void startConsuming() {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(2000));
            for (ConsumerRecord<String, String> record : records) {
                OriginLogMessage message = new OriginLogMessage();
                message.setOriginLogMessage(record.value());
                RingBuffer<OriginLogMessage> ringBuffer = disruptor.getDisruptor().getRingBuffer();
                long sequence = ringBuffer.next();
                try {
                    OriginLogMessage event = ringBuffer.get(sequence);
                    event.setOriginLogMessage(message.getOriginLogMessage());
                } finally {
                    ringBuffer.publish(sequence);
                }
            }
        }
    }

}
