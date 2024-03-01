package com.hy.entity;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.ToString;
import sun.security.krb5.internal.crypto.Des;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Description: 清洗后的日志
 * Author: yhong
 * Date: 2024/1/10
 */
@ToString
public class DBLogMessage extends BaseLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, String> fields;
    private Destination destination;
    private Client client;
    private User user;
    private String query;
    private SqlOut sqlOut;
    private String path;
    private String type;
    private Result result;
    private Organization organization;

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public SqlOut getSqlOut() {
        return sqlOut;
    }

    public void setSqlOut(SqlOut sqlOut) {
        this.sqlOut = sqlOut;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @JsonSetter("destination.ip")
    public void setDestinationIp(String ip) {
        if (destination == null) {
            destination = new Destination();
        }
        destination.setIp(ip);
    }

    @JsonSetter("destination.port")
    public void setDestinationPort(String port) {
        if (destination == null) {
            destination = new Destination();
        }
        destination.setPort(port);
    }

    @JsonSetter("client.ip")
    public void setClientIp(String ip) {
        if (client == null) {
            client = new Client();
        }
        client.setIp(ip);
    }

    @JsonSetter("client.port")
    public void setClientPort(String port) {
        if (client == null) {
            client = new Client();
        }
        client.setPort(port);
    }

    @JsonSetter("user.name")
    public void setUserName(String name) {
        if (user == null) {
            user = new User();
        }
        user.setName(name);
    }

    @JsonSetter("result.rows")
    public void setResultRows(int rows) {
        if (result == null) {
            result = new Result();
        }
        result.setRows(rows);
    }

    @JsonSetter("result.latency")
    public void setResultLatency(String latency) {
        if (result == null) {
            result = new Result();
        }
        result.setLatency(latency);
    }
}
