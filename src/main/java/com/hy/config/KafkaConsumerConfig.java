package com.hy.config;

import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * Description: kafka客户端配置类
 * Author: yhong
 * Date: 2024/1/11
 */
@Slf4j
@Configuration
public class KafkaConsumerConfig implements AsyncConfigurer {

    @Value("${spring.kafka.bootstrap.servers}")
    private String servers;
    @Value("${spring.kafka.group.id}")
    private String groupId;
    @Value("${spring.kafka.max.poll.records}")
    private String maxPollRecords;
    @Value("${spring.kafka.key.deserializer}")
    private String keyDeserializer;
    @Value("${spring.kafka.value.deserializer}")
    private String valueDeserializer;

    @Value("${spring.kafka.topic}")
    private String topic;

    @Bean(destroyMethod = "close")
    public KafkaConsumer<String, String> kafkaConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", servers);
        props.put("group.id", groupId);
        props.put("max.poll.records", maxPollRecords);
        props.put("key.deserializer", keyDeserializer);
        props.put("value.deserializer", valueDeserializer);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));
        return consumer;
    }
}

