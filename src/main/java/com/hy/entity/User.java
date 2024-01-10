package com.hy.entity;

import java.io.Serializable;

/**
 * Description:
 * Author: yhong
 * Date: 2024/1/10
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
