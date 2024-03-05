package com.hy.entity.host;

import com.hy.entity.common.BaseLog;
import com.hy.entity.common.Destination;
import com.hy.entity.common.Organization;
import com.hy.entity.common.User;

import java.io.Serializable;

/**
 * Description: 主机日志
 * Author: yhong
 * Date: 2024/1/11
 */
public class HostLogMessage extends BaseLog implements Serializable {
    private static final long serialVersionUID = 1L;
    private Process process;
    private Host host;
    private Input input;
    private Agent agent;
    private String fields;
    private Source source;
    private Destination destination;
    private User user;
    private Organization organization;

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
