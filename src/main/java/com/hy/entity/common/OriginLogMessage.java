package com.hy.entity.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Description: 原始日志
 * Author: yhong
 * Date: 2024/1/9
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OriginLogMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    String originLogMessage;

    String logType;

    public OriginLogMessage(String originLogMessage, String logType) {
        this.originLogMessage = originLogMessage;
        this.logType = logType;
    }

    public OriginLogMessage() {
    }

    public String getOriginLogMessage() {
        return originLogMessage;
    }

    public void setOriginLogMessage(String originLogMessage) {
        this.originLogMessage = originLogMessage;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }
}
