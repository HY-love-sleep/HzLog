package com.hy.service.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hy.common.LogType;
import com.hy.entity.BaseLog;
import com.hy.entity.DBLogMessage;
import com.hy.entity.OriginLogMessage;
import com.hy.service.LogCleanTemplate;
import com.hy.utils.ParseHaiNLog_HaiNan;
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

    @Override
    protected ObjectNode convertToJSON(OriginLogMessage rawLog) {
        String originLogMessage = rawLog.getOriginLogMessage();
        return ParseHaiNLog_HaiNan.parseCustomLog(originLogMessage);
    }

    @Override
    protected <T> T convertToEntity(ObjectNode jsonLog, Class<T> entityClass) {
        return null;
    }
}
