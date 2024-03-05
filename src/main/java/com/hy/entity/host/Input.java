package com.hy.entity.host;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Description:
 * Author: yhong
 * Date: 2024/1/11
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Input implements Serializable {
    private static final long serialVersionUID = 1L;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
