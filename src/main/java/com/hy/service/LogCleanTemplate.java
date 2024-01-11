package com.hy.service;

import com.hy.entity.BaseLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hy.entity.OriginLogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Description: 日志清洗模板类
 * Author: yhong
 * Date: 2024/1/11
 */
@Slf4j
public abstract class LogCleanTemplate {

    // 1. 打印原始日志
    protected void printRawLog(OriginLogMessage rawLog, boolean shouldPrint) {
        if (shouldPrint) {
            log.info("origin log: {}", rawLog.getOriginLogMessage());
        }
    }

    // 2. 将原始日志转为JSON格式
    protected String convertToJSON(OriginLogMessage rawLog) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(rawLog.getOriginLogMessage());
        } catch (Exception e) {
            log.error("日志转换json出错， 原始日志：{}", e.getMessage());
            return null;
        }
    }

    // 3. 将JSON格式的原始日志转为对应的实体类
    protected abstract <T> T convertToEntity(String jsonLog, Class<T> entityClass);

    public final <T> T cleanLog(OriginLogMessage rawLog, boolean shouldPrint, Class<T> entityClass) {
        // 步骤1：打印原始日志
        printRawLog(rawLog, shouldPrint);

        // 步骤2：将原始日志转为JSON格式
        String jsonLog = convertToJSON(rawLog);

        assert jsonLog != null;

        // 步骤3：将JSON格式的原始日志转为对应的实体类
        return convertToEntity(jsonLog, entityClass);
    }
}

