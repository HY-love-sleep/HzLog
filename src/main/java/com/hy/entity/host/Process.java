package com.hy.entity.host;

import java.io.Serializable;

/**
 * Description:
 * Author: yhong
 * Date: 2024/1/11
 */
public class Process implements Serializable {
    private static final long serialVersionUID = 1L;
    private String pid;
    private String name;
    private String working_directory;
    private String args;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorking_directory() {
        return working_directory;
    }

    public void setWorking_directory(String working_directory) {
        this.working_directory = working_directory;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }
}
