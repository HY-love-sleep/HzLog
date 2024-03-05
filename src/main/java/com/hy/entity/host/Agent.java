package com.hy.entity.host;

import java.io.Serializable;

/**
 * Description:
 * Author: yhong
 * Date: 2024/1/11
 */
public class Agent implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String hostname;
    private String version;
    private String type;
    private String ephemeral_id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEphemeral_id() {
        return ephemeral_id;
    }

    public void setEphemeral_id(String ephemeral_id) {
        this.ephemeral_id = ephemeral_id;
    }
}
