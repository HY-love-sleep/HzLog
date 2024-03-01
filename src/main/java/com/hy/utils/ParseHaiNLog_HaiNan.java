package com.hy.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hy.entity.DBLogMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: 解析海南数据库审计日志
 * Author: yhong
 * Date: 2024/1/12
 */
public class ParseHaiNLog_HaiNan {
    // 解析成json格式
    public static ObjectNode parseCustomLog(String log) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode logObject = objectMapper.createObjectNode();

        // 使用正则表达式匹配键值对
        String keyValueRegex = "(.+?):(.+?)(?=(?:\\||$))|SQL语句:(.*)";
        Pattern pattern = Pattern.compile(keyValueRegex);
        Matcher matcher = pattern.matcher(log);

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            if (key != null && value != null) {
                key = key.trim().substring(1);
                value = value.trim();
                logObject.put(key, value);
            }
            String sql = matcher.group(3);
            if (sql != null) {
                sql = sql.trim();
                logObject.put("SQL语句", sql);
            }
        }

        return logObject;
    }

    // 保存 jsonKey 到 entityKey 间的映射关系
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
        return fieldMappings;
    }

    // json转entity
    // todo:后续这个方法写到模版方法的convertToEntity中
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


    public static void main(String[] args) {
        String log = "DBSEC CEF:10.112.21.203:15023|发生时间:2024-01-05 08:11:11|服务器IP:drdsusrzt5f7vuoo.drds.res.inter-hnzwy.com|服务器端口:3306|数据库实例名:mysql|数据库名:godzilla|客户端IP:10.111.136.141|客户端端口:40640|应用用户:无|数据库用户:GODZILLA|风险类型:无|风险级别:无|引擎动作:放行|规则名称:无|操作类型:SELECT|响应时间:228(us)|执行结果:成功|影响行数:11|SQL语句:SELECT*FROM `SYNC_INFO` WHERE(UID=? AND PTS>=?)ORDER BY PTS ASC LIMIT 1000/*Y*/\n\u0000";
        ObjectNode logObject = parseCustomLog(log);
        if (logObject != null) {
            System.out.println("jsonNode: " + logObject);
        }
        DBLogMessage dbLogMessage = mapJsonToEntity(logObject, DBLogMessage.class);
        System.out.println(dbLogMessage);
    }
}
