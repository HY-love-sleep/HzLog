package com.hy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hy.entity.OriginLogMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Description: kafka生产者
 * todo: 修改为通用类， 可以发送任何类型的消息
 * Author: yhong
 * Date: 2024/1/15
 */
@Slf4j
@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Value("${spring.kafka.topic}")
    private String topic;

    public void sendLogsToKafka(List<OriginLogMessage> logMessages) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        for (OriginLogMessage log : logMessages) {
            String s = objectMapper.writeValueAsString(log);
            kafkaTemplate.send(topic, s);
        }
    }

    // 异步发送
    public void AsyncSendStudentsToKafka(List<OriginLogMessage> logs) {
        CompletableFuture[] futures = new CompletableFuture[logs.size()];
        ObjectMapper objectMapper = new ObjectMapper();
        for (int i = 0; i < logs.size(); i++) {
            OriginLogMessage log = logs.get(i);

            futures[i] = CompletableFuture.runAsync(() -> {
                try {
                    kafkaTemplate.send(topic, objectMapper.writeValueAsString(log));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures);
        try {
            allOf.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("kafka 发送消息失败：{}", e.getMessage());
        }
    }
}
