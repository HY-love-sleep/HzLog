package com.hy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hy.entity.common.OriginLogMessage;
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
            log.debug("origin log: {}", rawLog.getOriginLogMessage());
        }
    }

    // 2. 将原始日志转为JSON格式
    protected abstract ObjectNode convertToJSON(OriginLogMessage rawLog) throws JsonProcessingException;

    // 3. 将JSON格式的原始日志转为对应的实体类
    protected abstract <T> T convertToEntity(ObjectNode jsonNode, Class<T> entityClass) throws Exception;

    // 定义模板方法，固定日志清洗流程。 模板方法不可以被重写
    public final <T> T cleanLog(OriginLogMessage rawLog, boolean shouldPrint, Class<T> entityClass) throws Exception {

        printRawLog(rawLog, shouldPrint);

        ObjectNode jsonNode = convertToJSON(rawLog);

        assert jsonNode != null;

        return convertToEntity(jsonNode, entityClass);
    }
}

