package com.hy.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description: event
 * Author: yhong
 * Date: 2024/1/10
 */
@Data
@Accessors(chain = true)
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    private String kind;
    private String category;
    private String type;
    private String outcome;
    private String zone;
    private LocalDateTime created;
    private String sequence;

}
