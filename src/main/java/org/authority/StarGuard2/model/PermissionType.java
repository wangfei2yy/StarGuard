package org.authority.StarGuard2.model;

/**
 * 数据库权限类型枚举
 * 定义StarRocks数据库支持的所有权限类型
 */
public enum PermissionType {
    // 基础权限类型
    ALTER("ALTER"),
    DROP("DROP"),
    CREATE_TABLE("CREATE TABLE"),
    CREATE_VIEW("CREATE VIEW"),
    CREATE_FUNCTION("CREATE FUNCTION"),
    CREATE_MATERIALIZED_VIEW("CREATE MATERIALIZED VIEW"),
    REFRESH_MATERIALIZED_VIEW("REFRESH MATERIALIZED VIEW"),
    ALTER_MATERIALIZED_VIEW("ALTER MATERIALIZED VIEW"),
    DROP_MATERIALIZED_VIEW("DROP MATERIALIZED VIEW"),
    SELECT_MATERIALIZED_VIEW("SELECT MATERIALIZED VIEW"),
    ALL_PRIVILEGES("ALL PRIVILEGES"),
    DELETE("DELETE"),
    INSERT("INSERT"),
    SELECT("SELECT"),
    EXPORT("EXPORT"),
    UPDATE("UPDATE"),
    // System权限类型
    CREATE_RESOURCE_GROUP("CREATE RESOURCE GROUP"),
    CREATE_RESOURCE("CREATE RESOURCE"),
    CREATE_EXTERNAL_CATALOG("CREATE EXTERNAL CATALOG"),
    REPOSITORY("REPOSITORY"),
    BLACKLIST("BLACKLIST"),
    FILE("FILE"),
    OPERATE("OPERATE"),
    CREATE_STORAGE_VOLUME("CREATE STORAGE VOLUME"),
    SECURITY("SECURITY"),
    // 角色授权类型
    ROLE_GRANT("ROLE_GRANT");

    private final String value;

    /**
     * 构造函数
     * @param value 权限的字符串表示
     */
    PermissionType(String value) {
        this.value = value;
    }

    /**
     * 获取权限的字符串表示
     * @return 权限字符串
     */
    public String getValue() {
        return value;
    }

    /**
     * 转换为用于GRANT语句的权限字符串
     * @return 权限字符串
     */
    public String toGrantString() {
        return value;
    }

    /**
     * 判断是否是ALTER权限
     * @return 是否是ALTER权限
     */
    public boolean isAlter() {
        return this == ALTER || this == ALL_PRIVILEGES;
    }

    /**
     * 判断是否是DROP权限
     * @return 是否是DROP权限
     */
    public boolean isDrop() {
        return this == DROP || this == ALL_PRIVILEGES;
    }

    /**
     * 判断是否是CREATE TABLE权限
     * @return 是否是CREATE TABLE权限
     */
    public boolean isCreateTable() {
        return this == CREATE_TABLE || this == ALL_PRIVILEGES;
    }

    /**
     * 判断是否是CREATE VIEW权限
     * @return 是否是CREATE VIEW权限
     */
    public boolean isCreateView() {
        return this == CREATE_VIEW || this == ALL_PRIVILEGES;
    }

    /**
     * 判断是否是CREATE FUNCTION权限
     * @return 是否是CREATE FUNCTION权限
     */
    public boolean isCreateFunction() {
        return this == CREATE_FUNCTION || this == ALL_PRIVILEGES;
    }

    /**
     * 判断是否是CREATE MATERIALIZED VIEW权限
     * @return 是否是CREATE MATERIALIZED VIEW权限
     */
    public boolean isCreateMaterializedView() {
        return this == CREATE_MATERIALIZED_VIEW || this == ALL_PRIVILEGES;
    }

    /**
     * 判断是否是REFRESH MATERIALIZED VIEW权限
     * @return 是否是REFRESH MATERIALIZED VIEW权限
     */
    public boolean isRefreshMaterializedView() {
        return this == REFRESH_MATERIALIZED_VIEW || this == ALL_PRIVILEGES;
    }

    /**
     * 判断是否是ALTER MATERIALIZED VIEW权限
     * @return 是否是ALTER MATERIALIZED VIEW权限
     */
    public boolean isAlterMaterializedView() {
        return this == ALTER_MATERIALIZED_VIEW || this == ALL_PRIVILEGES;
    }

    /**
     * 判断是否是DROP MATERIALIZED VIEW权限
     * @return 是否是DROP MATERIALIZED VIEW权限
     */
    public boolean isDropMaterializedView() {
        return this == DROP_MATERIALIZED_VIEW || this == ALL_PRIVILEGES;
    }

    /**
     * 判断是否是SELECT MATERIALIZED VIEW权限
     * @return 是否是SELECT MATERIALIZED VIEW权限
     */
    public boolean isSelectMaterializedView() {
        return this == SELECT_MATERIALIZED_VIEW || this == ALL_PRIVILEGES;
    }

    /**
     * 判断是否包含所有权限
     * @return 是否包含所有权限
     */
    public boolean isAllPrivileges() {
        return this == ALL_PRIVILEGES;
    }

    /**
     * 根据字符串值获取权限类型
     * @param value 权限字符串
     * @return 权限类型枚举值
     * @throws IllegalArgumentException 当字符串不匹配任何权限类型时抛出
     */
    public static PermissionType fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Permission value cannot be null or empty");
        }
        
        String normalizedValue = value.trim().toUpperCase();
        
        for (PermissionType type : values()) {
            if (type.getValue().equalsIgnoreCase(normalizedValue)) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("Unknown permission type: " + value);
    }

    /**
     * 检查是否是有效的权限类型
     * @param value 权限字符串
     * @return 是否有效
     */
    public static boolean isValidPermission(String value) {
        try {
            fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 获取所有权限类型的字符串表示
     * @return 权限类型字符串数组
     */
    public static String[] getAllPermissionValues() {
        String[] values = new String[values().length];
        int i = 0;
        for (PermissionType type : values()) {
            values[i++] = type.getValue();
        }
        return values;
    }
}