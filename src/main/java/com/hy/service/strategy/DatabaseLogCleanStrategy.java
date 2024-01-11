package com.hy.service.strategy;

import com.hy.entity.BaseLog;
import com.hy.entity.DBLogMessage;
import com.hy.entity.OriginLogMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Description: 数据库日志清洗策略
 * Author: yhong
 * Date: 2024/1/11
 */
@Component
public class DatabaseLogCleanStrategy implements LogCleanStrategy{

    @Override
    public DBLogMessage clean(OriginLogMessage originLog) {
        return null;
    }

    @Override
    public boolean supports(String logType) {
        return StringUtils.equals("database", logType);
    }
}
