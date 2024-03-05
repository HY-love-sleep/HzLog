package com.hy.entity.host;

import java.io.Serializable;

/**
 * Description:
 * Author: yhong
 * Date: 2024/1/11
 */
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
