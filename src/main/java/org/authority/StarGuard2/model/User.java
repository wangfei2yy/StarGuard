package org.authority.StarGuard2.model;

import java.util.List;

/**
 * 用户实体类，用于表示StarRocks数据库中的用户信息
 * 
 * @author System
 * @version 1.0
 */
public class User {
    private String username;
    private String host;
    private List<Permission> permissions;
    private boolean hasGrantOption;

    /**
     * 构造函数
     *  @param username 用户名
     *
     */
    public User(String username,String host) {
        this.username = username;
        this.host = host;
    }

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
     * 获取用户拥有的权限列表
     * 
     * @return 权限列表
     */
    public List<Permission> getPermissions() {
        return permissions;
    }

    /**
     * 设置用户拥有的权限列表
     * 
     * @param permissions 权限列表
     */
    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    /**
     * 检查用户是否拥有GRANT OPTION权限
     * 
     * @return true如果用户拥有GRANT OPTION权限
     */
    public boolean isHasGrantOption() {
        return hasGrantOption;
    }

    /**
     * 设置用户是否拥有GRANT OPTION权限
     * 
     * @param hasGrantOption 是否拥有GRANT OPTION权限
     */
    public void setHasGrantOption(boolean hasGrantOption) {
        this.hasGrantOption = hasGrantOption;
    }

    /**
     * 获取完整的用户标识（用户名@主机地址）
     * 
     * @return 完整的用户标识
     */
    public String getUserIdentity() {
        return "'" + username + "'@'" + host + "'";
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", host='" + host + '\'' +
                ", hasGrantOption=" + hasGrantOption +
                ", permissions=" + permissions +
                '}';
    }
}