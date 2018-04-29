package io.github.biezhi.onmybatis.model;

import java.io.Serializable;

public class GeneratorParam implements Serializable {

    private String connection;
    private String dataBase;
    private String port;
    private String userId;
    private String userPass;
    private String modelPath;
    private String mappingPath;
    private String mapperPath;
    private String buildPath;
    private String[] tableNames;
    private String[] modelNames;
    private String isHump;
    private String isAlltable;
    private String mapperPlugin;
    private String dbType;

    public GeneratorParam() {

    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getDataBase() {
        return dataBase;
    }

    public void setDataBase(String dataBase) {
        this.dataBase = dataBase;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public String getMappingPath() {
        return mappingPath;
    }

    public void setMappingPath(String mappingPath) {
        this.mappingPath = mappingPath;
    }

    public String getMapperPath() {
        return mapperPath;
    }

    public void setMapperPath(String mapperPath) {
        this.mapperPath = mapperPath;
    }

    public String[] getTableNames() {
        return tableNames;
    }

    public void setTableNames(String[] tableNames) {
        this.tableNames = tableNames;
    }

    public String[] getModelNames() {
        return modelNames;
    }

    public void setModelNames(String[] modelNames) {
        this.modelNames = modelNames;
    }

    public String getBuildPath() {
        return buildPath;
    }

    public void setBuildPath(String buildPath) {
        this.buildPath = buildPath;
    }

    public String getIsHump() {
        return isHump;
    }

    public void setIsHump(String isHump) {
        this.isHump = isHump;
    }

    public String getIsAlltable() {
        return isAlltable;
    }

    public void setIsAlltable(String isAlltable) {
        this.isAlltable = isAlltable;
    }

    public String getMapperPlugin() {
        return mapperPlugin;
    }

    public void setMapperPlugin(String mapperPlugin) {
        this.mapperPlugin = mapperPlugin;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}