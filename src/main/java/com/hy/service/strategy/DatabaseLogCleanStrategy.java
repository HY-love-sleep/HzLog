package com.hy.service.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hy.common.LogType;
import com.hy.entity.*;
import com.hy.service.LogCleanTemplate;
import com.hy.utils.ParseHaiNLog_HaiNan;
import com.hy.utils.SQLParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;

/**
 * Description: 数据库日志清洗策略
 * Author: yhong
 * Date: 2024/1/11
 */
@Slf4j
@Component
public class DatabaseLogCleanStrategy extends LogCleanTemplate implements LogCleanStrategy{

    @Override
    public DBLogMessage clean(OriginLogMessage originLog) throws Exception {
        return cleanLog(originLog, true, DBLogMessage.class);
    }

    @Override
    public boolean supports(LogType logType) {
        return logType == LogType.DatabaseLog;
    }

    @Override
    protected ObjectNode convertToJSON(OriginLogMessage rawLog) {
        String originLogMessage = rawLog.getOriginLogMessage();
        return ParseHaiNLog_HaiNan.parseCustomLog(originLogMessage);
    }

    /**
     * 将json对象转为需要的日志实体类
     * todo: 这个方法需要优化， 1、考虑使用规则引擎引入类似logstash的脚本完成转换；  2、内部一些重复使用的方法提取到工具类
     * @param jsonLog
     * @param entityClass
     * @return
     * @param <T>
     */
    @Override
    protected <T> T convertToEntity(ObjectNode jsonLog, Class<T> entityClass) throws Exception {
        if (entityClass.equals(DBLogMessage.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            DBLogMessage dbLogMessage = new DBLogMessage();
            log.info("jsonLog: {}", jsonLog);
            Event event = new Event();
            event.setKind("event").setCategory("database").setType("info").setOutcome(jsonLog.get("执行结果").asText())
                    .setZone("数据库审计").setCreated(new Date())
                    .setSequence("");
            dbLogMessage.setEvent(event);

            dbLogMessage.setMessage(jsonLog.toString());

            Destination destination = new Destination();
            destination.setIp(jsonLog.get("服务器IP").asText()).setPort(jsonLog.get("服务器端口").asText());
            dbLogMessage.setDestination(destination);

            Client client = new Client();
            client.setIp(jsonLog.get("客户端IP").asText()).setPort(jsonLog.get("客户端端口").asText());
            dbLogMessage.setClient(client);

            User user = new User();
            user.setName(jsonLog.get("数据库用户").asText());
            dbLogMessage.setUser(user);

            dbLogMessage.setQuery(jsonLog.get("SQL语句").asText());

            // 引入强哥的sql解析插件
            Map<String, Object> mysql = SQLParserUtil.getSqlOut(jsonLog.get("SQL语句").asText(), "mysql");
            String jsonString = objectMapper.writeValueAsString(mysql);
            SqlOut sqlOut = objectMapper.readValue(jsonString, SqlOut.class);
            dbLogMessage.setSqlOut(sqlOut);

            dbLogMessage.setPath(null);

            dbLogMessage.setType("database");

            Result result = new Result();
            result.setLatency(jsonLog.get("响应时间").asText()).setRows(Integer.valueOf(jsonLog.get("影响行数").asText()));
            dbLogMessage.setResult(result);

            Organization organization = new Organization();
            organization.setName("海南大数据").setPlatform("数据库审计");
            dbLogMessage.setOrganization(organization);

            return objectMapper.convertValue(dbLogMessage, entityClass);
        }
        return null;
    }
}
