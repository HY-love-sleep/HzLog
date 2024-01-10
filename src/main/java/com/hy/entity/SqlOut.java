package com.hy.entity;

import java.io.Serializable;

/**
 * Description:
 * Author: yhong
 * Date: 2024/1/10
 */
public class SqlOut implements Serializable {
    private static final long serialVersionUID = 1L;
    private String dlanguage;
    private String[] tables;
    private String[] columns;
    private String databaseName;

    public String getDlanguage() {
        return dlanguage;
    }

    public void setDlanguage(String dlanguage) {
        this.dlanguage = dlanguage;
    }

    public String[] getTables() {
        return tables;
    }

    public void setTables(String[] tables) {
        this.tables = tables;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
