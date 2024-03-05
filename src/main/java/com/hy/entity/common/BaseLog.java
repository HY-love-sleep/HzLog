package com.hy.entity.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 日志共有字段
 * Author: yhong
 * Date: 2024/1/11
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class BaseLog implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String timestamp;
    protected Event event;
    protected String[] tags;
    protected String message;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
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
