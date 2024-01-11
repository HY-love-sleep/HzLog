package com.hy.common;

public enum LogType {
    HostLog("host"),
    DatabaseLog("database"),
    VPNLog("vpn"),
    JumpLog("jump");

    private final String logTypeName;

    LogType(String logTypeName) {
        this.logTypeName = logTypeName;
    }

    public String getLogTypeName() {
        return logTypeName;
    }

    public static LogType fromString(String logTypeString) {
        for (LogType logType : LogType.values()) {
            if (logType.logTypeName.equalsIgnoreCase(logTypeString)) {
                return logType;
            }
        }
        throw new IllegalArgumentException("Unsupported log type: " + logTypeString);
    }
}

