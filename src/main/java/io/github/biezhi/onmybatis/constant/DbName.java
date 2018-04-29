package io.github.biezhi.onmybatis.constant;

/**
 * @author ssnoodles
 * @version 1.0
 * Create at 2018/4/28 16:17
 */
public enum DbName {
    MYSQL("Mysql"),
    POSTGRESQL("Postgresql"),
    ORACLE("Oracle");

    private String name;

    DbName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
