package com.hy.service.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hy.common.LogType;
import com.hy.entity.common.Event;
import com.hy.entity.common.Organization;
import com.hy.entity.common.OriginLogMessage;
import com.hy.entity.database.DBLogMessage;
import com.hy.entity.database.SqlOut;
import com.hy.service.LogCleanTemplate;
import com.hy.utils.ParseHaiNLog_HaiNan;
import com.hy.utils.SQLParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
        return cleanLog(originLog, false, DBLogMessage.class);
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
     * @param jsonLog
     * @param entityClass
     * @return
     * @param <T>
     */
    // todo: 下面写死的字段也提取到ParseHaiNLog_HaiNan.mapJsonToEntity里去
    @Override
    protected <T> T convertToEntity(ObjectNode jsonLog, Class<T> entityClass) throws Exception {
        if (entityClass.equals(DBLogMessage.class)) {
            // log.info("jsonLog: {}", jsonLog);
            ObjectMapper objectMapper = new ObjectMapper();
            // 将能映射的先映射上去
            DBLogMessage dbLogMessage = ParseHaiNLog_HaiNan.mapJsonToEntity(jsonLog, DBLogMessage.class);
            // 自定义的手动set或者定义map来set
            Event event = new Event();
            event.setKind("event").setCategory("database").setType("info").setOutcome(jsonLog.get("执行结果").asText())
                    .setZone("数据库审计").setCreated(new Date())
                    .setSequence("");
            dbLogMessage.setEvent(event);

            dbLogMessage.setMessage(jsonLog.toString());

            // 引入强哥的sql解析插件
            Map<String, Object> mysql = SQLParserUtil.getSqlOut(jsonLog.get("SQL语句").asText(), "mysql");
            String jsonString = objectMapper.writeValueAsString(mysql);
            SqlOut sqlOut = objectMapper.readValue(jsonString, SqlOut.class);
            dbLogMessage.setSqlOut(sqlOut);

            dbLogMessage.setPath(null);

            dbLogMessage.setType("database");

            // Result result = new Result();
            // result.setLatency(jsonLog.get("响应时间").asText()).setRows(Integer.valueOf(jsonLog.get("影响行数").asText()));
            // dbLogMessage.setResult(result);

            Organization organization = new Organization();
            organization.setName("海南大数据").setPlatform("数据库审计");
            dbLogMessage.setOrganization(organization);

            return objectMapper.convertValue(dbLogMessage, entityClass);
        }
        return null;
    }
}
