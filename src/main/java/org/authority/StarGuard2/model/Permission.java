package org.authority.StarGuard2.model;

import java.util.List;

/**
 * 权限实体类，用于表示用户在数据库上的权限信息
 * 
 * @author System
 * @version 1.0
 */
public class Permission {
    private PermissionType permissionType;
    private String databaseName;
    private boolean allDatabases;
    private boolean withGrantOption;

    /**
     * 构造函数
     * 
     * @param permissionType 权限类型
     * @param databaseName 数据库名称
     */
    public Permission(PermissionType permissionType, String databaseName) {
        this.permissionType = permissionType;
        this.databaseName = databaseName;
        this.allDatabases = "ALL DATABASES".equalsIgnoreCase(databaseName);
    }

    /**
     * 获取权限类型
     * 
     * @return 权限类型
     */
    public PermissionType getPermissionType() {
        return permissionType;
    }

    /**
     * 设置权限类型
     * 
     * @param permissionType 权限类型
     */
    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
    }

    /**
     * 获取数据库名称
     * 
     * @return 数据库名称
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * 设置数据库名称
     * 
     * @param databaseName 数据库名称
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
        this.allDatabases = "ALL DATABASES".equalsIgnoreCase(databaseName);
    }

    /**
     * 检查是否是所有数据库的权限
     * 
     * @return true如果是所有数据库的权限
     */
    public boolean isAllDatabases() {
        return allDatabases;
    }

    /**
     * 设置是否是所有数据库的权限
     * 
     * @param allDatabases 是否是所有数据库的权限
     */
    public void setAllDatabases(boolean allDatabases) {
        this.allDatabases = allDatabases;
    }

    /**
     * 检查是否拥有GRANT OPTION权限
     * 
     * @return true如果拥有GRANT OPTION权限
     */
    public boolean isWithGrantOption() {
        return withGrantOption;
    }

    /**
     * 设置是否拥有GRANT OPTION权限
     * 
     * @param withGrantOption 是否拥有GRANT OPTION权限
     */
    public void setWithGrantOption(boolean withGrantOption) {
        this.withGrantOption = withGrantOption;
    }

    /**
     * 构建GRANT语句的权限部分
     * 
     * @return GRANT语句的权限部分字符串
     */
    public String buildGrantStatement() {
        StringBuilder builder = new StringBuilder();
        builder.append(permissionType.toGrantString());
        builder.append(" ON ");
        if (allDatabases) {
            builder.append("ALL DATABASES");
        } else {
            builder.append("DATABASE ").append(databaseName);
        }
        if (withGrantOption) {
            builder.append(" WITH GRANT OPTION");
        }
        return builder.toString();
    }

    /**
     * 构建REVOKE语句的权限部分
     * 
     * @return REVOKE语句的权限部分字符串
     */
    public String buildRevokeStatement() {
        StringBuilder builder = new StringBuilder();
        builder.append(permissionType.toGrantString());
        builder.append(" ON ");
        if (allDatabases) {
            builder.append("ALL DATABASES");
        } else {
            builder.append("DATABASE ").append(databaseName);
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return "Permission{" +
                "permissionType=" + permissionType +
                ", databaseName='" + databaseName + '\'' +
                ", allDatabases=" + allDatabases +
                ", withGrantOption=" + withGrantOption +
                '}';
    }
}