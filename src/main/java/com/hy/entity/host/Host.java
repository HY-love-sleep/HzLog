package com.hy.entity.host;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Author: yhong
 * Date: 2024/1/11
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Host implements Serializable {
    private static final long serialVersionUID = 1L;
    private String architecture;
    private List<String> mac;
    private Os os;
    private List<String> ip;
    private String hostname;
    private String id;
    private String name;
    private String containerized;

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public List<String> getMac() {
        return mac;
    }

    public void setMac(List<String> mac) {
        this.mac = mac;
    }

    public Os getOs() {
        return os;
    }

    public void setOs(Os os) {
        this.os = os;
    }

    public List<String> getIp() {
        return ip;
    }

    public void setIp(List<String> ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContainerized() {
        return containerized;
    }

    public void setContainerized(String containerized) {
        this.containerized = containerized;
    }
}
