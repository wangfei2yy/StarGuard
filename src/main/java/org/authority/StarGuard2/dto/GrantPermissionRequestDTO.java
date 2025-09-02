package org.authority.StarGuard2.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 授权请求DTO类，用于处理前端提交的授权请求
 * 
 * @author System
 * @version 1.0
 */
public class GrantPermissionRequestDTO {
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "主机地址不能为空")
    private String host;

    @NotNull(message = "权限类型不能为空")
    private List<String> permissionTypes;

    private String databaseName;

    private boolean allDatabases;
    private boolean withGrantOption;
    
    private String scopeType;
    private String viewScope;
    private String viewName;

    /**
     * 获取用户名
     * 
     * @return 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     * 
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取主机地址
     * 
     * @return 主机地址
     */
    public String getHost() {
        return host;
    }

    /**
     * 设置主机地址
     * 
     * @param host 主机地址
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 获取权限类型列表
     * 
     * @return 权限类型列表
     */
    public List<String> getPermissionTypes() {
        return permissionTypes;
    }

    /**
     * 设置权限类型列表
     * 
     * @param permissionTypes 权限类型列表
     */
    public void setPermissionTypes(List<String> permissionTypes) {
        this.permissionTypes = permissionTypes;
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
     * 获取作用域类型
     * 
     * @return 作用域类型
     */
    public String getScopeType() {
        return scopeType;
    }

    /**
     * 设置作用域类型
     * 
     * @param scopeType 作用域类型
     */
    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    /**
     * 获取视图作用域
     * 
     * @return 视图作用域
     */
    public String getViewScope() {
        return viewScope;
    }

    /**
     * 设置视图作用域
     * 
     * @param viewScope 视图作用域
     */
    public void setViewScope(String viewScope) {
        this.viewScope = viewScope;
    }

    /**
     * 获取视图名称
     * 
     * @return 视图名称
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * 设置视图名称
     * 
     * @param viewName 视图名称
     */
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public String toString() {
        return "GrantPermissionRequestDTO{" +
                "username='" + username + '\'' +
                ", host='" + host + '\'' +
                ", permissionTypes=" + permissionTypes +
                ", databaseName='" + databaseName + '\'' +
                ", allDatabases=" + allDatabases +
                ", withGrantOption=" + withGrantOption +
                ", scopeType='" + scopeType + '\'' +
                ", viewScope='" + viewScope + '\'' +
                ", viewName='" + viewName + '\'' +
                '}';
    }
}