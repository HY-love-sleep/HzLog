package com.hy.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hy.entity.DBLogMessage;
import com.hy.entity.LogEvent;
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
public class LogSenderHandler implements EventHandler<LogEvent> {
    private final RestHighLevelClient client;
    @Autowired
    public LogSenderHandler(RestHighLevelClient client) {
        this.client = client;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${spring.es.index}")
    private String index;

    @Override
    public void onEvent(LogEvent logEvent, long l, boolean b) throws Exception {
        IndexRequest indexRequest = new IndexRequest(index);
        String messageString = objectMapper.writeValueAsString(logEvent.getLog());
        indexRequest.source(messageString, XContentType.JSON);
        String result = "";
        try {
            // 发送清洗好的日志到es指定索引中
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
            result = indexResponse.getResult().toString();
        } catch (IOException e) {
            log.error("写入es索引失败， 索引{}, 响应状态:{}, error:{}", index, result, e.getMessage());
        }
    }
}
