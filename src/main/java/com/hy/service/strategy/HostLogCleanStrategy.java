package com.hy.service.strategy;


import com.hy.common.LogType;
import com.hy.entity.host.HostLogMessage;
import com.hy.entity.common.OriginLogMessage;
import org.springframework.stereotype.Component;

/**
 * Description: 主机日志清洗策略
 * Author: yhong
 * Date: 2024/1/11
 */
@Component
public class HostLogCleanStrategy implements LogCleanStrategy {

    @Override
    public HostLogMessage clean(OriginLogMessage originLog) {
        return null;
    }

    @Override
    public boolean supports(LogType logType) {
        return logType == LogType.HostLog;
    }
}
