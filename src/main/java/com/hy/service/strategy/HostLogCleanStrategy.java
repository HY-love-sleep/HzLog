package com.hy.service.strategy;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hy.common.LogType;
import com.hy.entity.common.BaseLog;
import com.hy.entity.host.HostLogMessage;
import com.hy.entity.common.OriginLogMessage;
import com.hy.service.LogCleanTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static sun.reflect.misc.FieldUtil.getField;

/**
 * Description: 主机日志清洗策略
 * Author: yhong
 * Date: 2024/1/11
 */
@Component
@Slf4j
public class HostLogCleanStrategy extends LogCleanTemplate implements LogCleanStrategy {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public HostLogMessage clean(OriginLogMessage originLog) throws Exception {
        return cleanLog(originLog, true, HostLogMessage.class);
    }

    @Override
    public boolean supports(LogType logType) {
        return logType == LogType.HostLog;
    }

    @Override
    protected ObjectNode convertToJSON(OriginLogMessage rawLog) throws JsonProcessingException {
        // 主机的日志数据一般都是json格式的， 不需要额外的转换
        String originLogMessage = rawLog.getOriginLogMessage();
        return objectMapper.readValue(originLogMessage, ObjectNode.class);
    }

    @Override
    protected <T> T convertToEntity(ObjectNode jsonLog, Class<T> entityClass) throws Exception {
        if (!entityClass.equals(HostLogMessage.class)) {
            log.error("日志类型不匹配， entityClass：{}", entityClass);
            return null;
        }
        log.info("jsonLog: {}", jsonLog);
        // 能够直接映射的部分转换为实体类
        HostLogMessage hostLogMessage = objectMapper.treeToValue(jsonLog, HostLogMessage.class);

        // 无法直接映射的部分通过自定义Map来映射
        HostLogMessage res = mapJsonToEntity(jsonLog, hostLogMessage);

        return objectMapper.convertValue(res, entityClass);
    }

    // 定义原始日志与标准日志间的字段映射， 这个map的定义后续由前端页面配置
    private static <T> Map<String, String> getFieldMappings() {
        // 添加字段映射关系 k-实体类字段， v-json字段
        Map<String, String> fieldMappings = new HashMap<>();
        fieldMappings.put("@timestamp", "timestamp");

        return fieldMappings;
    }

    public static <T> T mapJsonToEntity(JsonNode jsonNode, T entity) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Map<String, String> fieldMappings = getFieldMappings();

            for (Map.Entry<String, String> entry : fieldMappings.entrySet()) {
                String jsonFieldName = entry.getKey();
                String entityFieldName = entry.getValue();

                if (jsonNode.has(jsonFieldName)) {
                    String fieldValue = jsonNode.get(jsonFieldName).asText();
                    objectMapper.readerForUpdating(entity).readValue(String.format("{\"%s\":\"%s\"}", entityFieldName, fieldValue));
                }
            }
        } catch (Exception e) {
            log.error("exception: {}", e.getMessage());
        }

        return entity;
    }

}
