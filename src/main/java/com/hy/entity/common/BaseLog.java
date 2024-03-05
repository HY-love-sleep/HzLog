package com.hy.entity.common;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 日志共有字段
 * Author: yhong
 * Date: 2024/1/11
 */
public class BaseLog implements Serializable {
    private static final long serialVersionUID = 1L;
    private Date timestamp;
    private Event event;
    private String[] tags;
    private String message;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
