package com.hy.entity;

import java.io.Serializable;

/**
 * Description: 原始日志
 * Author: yhong
 * Date: 2024/1/9
 */
public class OriginLogMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    String originLogMessage;

    String logType;

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
