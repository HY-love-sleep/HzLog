package com.hy.entity;

import java.io.Serializable;

/**
 * Description: 原始日志
 * Author: yhong
 * Date: 2024/1/9
 */
public class LogMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    String originLogMessage;

    public String getOriginLogMessage() {
        return originLogMessage;
    }

    public void setOriginLogMessage(String originLogMessage) {
        this.originLogMessage = originLogMessage;
    }
}
