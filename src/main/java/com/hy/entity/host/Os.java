package com.hy.entity.host;

import java.io.Serializable;

/**
 * Description:
 * Author: yhong
 * Date: 2024/1/11
 */
public class Os implements Serializable {
    private static final long serialVersionUID = 1L;
    private String family;
    private String kernel;
    private String version;
    private String platform;
    private String name;

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getKernel() {
        return kernel;
    }

    public void setKernel(String kernel) {
        this.kernel = kernel;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
