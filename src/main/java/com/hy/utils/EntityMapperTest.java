package com.hy.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hy.entity.database.DBLogMessage;

import java.util.HashMap;
import java.util.Map;

public class EntityMapperTest {

    public static <T> T mapJsonToEntity(JsonNode jsonNode, Class<T> entityClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        T entity = null;

        try {
            entity = entityClass.newInstance();

            Map<String, String> fieldMappings = getFieldMappings(entityClass);

            for (Map.Entry<String, String> entry : fieldMappings.entrySet()) {
                String jsonFieldName = entry.getKey();
                String entityFieldName = entry.getValue();

                if (jsonNode.has(jsonFieldName)) {
                    String fieldValue = jsonNode.get(jsonFieldName).asText();
                    objectMapper.readerForUpdating(entity).readValue(String.format("{\"%s\":\"%s\"}", entityFieldName, fieldValue));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entity;
    }

    private static <T> Map<String, String> getFieldMappings(Class<T> entityClass) {
        // 添加字段映射关系
        Map<String, String> fieldMappings = new HashMap<>();
        fieldMappings.put("服务器IP", "destination.ip");
        fieldMappings.put("服务器端口", "destination.port");
        fieldMappings.put("客户端IP", "client.ip");
        fieldMappings.put("客户端端口", "client.port");
        fieldMappings.put("数据库用户", "user.name");
        fieldMappings.put("SQL语句", "query");
        fieldMappings.put("响应时间", "result.latency");
        fieldMappings.put("影响行数", "result.rows");
        // 添加其他字段映射...

        return fieldMappings;
    }

    public static void main(String[] args) {
        String jsonString = "{\"服务器IP\":\"10.112.21.203\",\"服务器端口\":\"3306\",\"客户端IP\":\"10.111.136.141\",\"客户端端口\":\"40640\"}";
        JsonNode jsonNode;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonNode = objectMapper.readTree(jsonString);

            DBLogMessage dbLogMessage = mapJsonToEntity(jsonNode, DBLogMessage.class);
            System.out.println(dbLogMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
