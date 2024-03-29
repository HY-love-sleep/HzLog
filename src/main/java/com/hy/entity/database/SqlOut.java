package com.hy.entity.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: yhong
 * Date: 2024/1/10
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SqlOut implements Serializable {
    private static final long serialVersionUID = 1L;
    private String dlanguage;
    private List<Map<String, String>> tables;
    private List<String> columns;

}
