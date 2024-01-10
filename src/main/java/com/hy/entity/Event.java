package com.hy.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description: event
 * Author: yhong
 * Date: 2024/1/10
 */
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    private String kind;
    private String category;
    private String type;
    private String outcome;
    private String zone;
    private LocalDateTime created;
    private String sequence;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}
