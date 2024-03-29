package com.hy.entity.host;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Description:
 * Author: yhong
 * Date: 2024/1/11
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Source implements Serializable {
    private static final long serialVersionUID = 1L;
    private String ip;
    private String port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
