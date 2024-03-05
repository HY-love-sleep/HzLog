package com.hy.entity.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Description:
 * Author: yhong
 * Date: 2024/1/10
 */
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
}
