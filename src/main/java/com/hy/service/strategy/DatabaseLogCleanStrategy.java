package com.hy.service.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hy.common.LogType;
import com.hy.entity.BaseLog;
import com.hy.entity.DBLogMessage;
import com.hy.entity.OriginLogMessage;
import com.hy.service.LogCleanTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Description: 数据库日志清洗策略
 * Author: yhong
 * Date: 2024/1/11
 */
@Slf4j
@Component
public class DatabaseLogCleanStrategy extends LogCleanTemplate implements LogCleanStrategy{

    @Override
    public DBLogMessage clean(OriginLogMessage originLog) {
        return cleanLog(originLog, true, DBLogMessage.class);
    }

    @Override
    public boolean supports(LogType logType) {
        return logType == LogType.DatabaseLog;
    }

    /**
     * 需要根据实际的字段匹配日志模板
     * @param jsonLog
     * @param entityClass
     * @return
     * @param <T>
     */
    @Override
    protected <T> T convertToEntity(String jsonLog, Class<T> entityClass) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonLog, entityClass);
        } catch (Exception e) {
            log.error("ConvertJsonToEntity false , cause by :{}", e.getMessage());
            return null;
        }
    }
}
