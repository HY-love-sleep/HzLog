package com.hy.entity.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hy.entity.common.BaseLog;

/**
 * Description: 通用日志事件类
 * Author: yhong
 * Date: 2024/1/11
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogEvent {
    private BaseLog log;
    public BaseLog getLog() {
        return log;
    }

    public void setLog(BaseLog log) {
        this.log = log;
    }
}

