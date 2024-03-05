package com.hy.service.strategy;

import com.hy.common.LogType;
import com.hy.entity.common.BaseLog;
import com.hy.entity.common.OriginLogMessage;

/**
 * Description: 日志清洗策略接口
 * 使用策略模式方便对于新格式的日志，只需要添加新的策略， 无需修改现有代码；
 *
 * 使用：
 * @Autowired
 * private List<LogCleanStrategy> strategies;
 *
 * public DBLogMessage cleanLog(OriginLogMessage originLogMessage, String logType) {
 *     LogCleanStrategy strategy = strategies.stream()
 *         .filter(s -> s.supports(logType))
 *         .findFirst()
 *         .orElseThrow(() -> new IllegalArgumentException("No strategy found for log type: " + logType));
 *
 *     return strategy.clean(originLogMessage);
 * }
 *
 * Author: yhong
 * Date: 2024/1/11
 */
public interface LogCleanStrategy {
    BaseLog clean(OriginLogMessage originLog) throws Exception;

    boolean supports(LogType logType);
}
