package com.hy.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hy.entity.DBLogMessage;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Description: 发送日志到es
 * Author: yhong
 * Date: 2024/1/9
 */
@Slf4j
@Component
public class LogSenderHandler implements EventHandler<DBLogMessage> {
    private final RestHighLevelClient client;
    @Autowired
    public LogSenderHandler(RestHighLevelClient client) {
        this.client = client;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${spring.es.index}")
    private String index;

    @Override
    public void onEvent(DBLogMessage dbLogMessage, long l, boolean b) throws Exception {
        IndexRequest indexRequest = new IndexRequest(index);
        String messageString = objectMapper.writeValueAsString(dbLogMessage);
        indexRequest.source(messageString, XContentType.JSON);

        try {
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
            // 检查响应状态，例如 indexResponse.getResult()
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
