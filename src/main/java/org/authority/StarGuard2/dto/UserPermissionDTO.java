package org.authority.StarGuard2.dto;

import org.authority.StarGuard2.model.Permission;
import java.util.List;

/**
 * 用户权限DTO类，用于向前端展示用户的权限信息
 * 
 * @author System
 * @version 1.0
 */
public class UserPermissionDTO {
    private String username;
    private String host;
    private List<PermissionDTO> permissions;
    private boolean hasGrantOption;

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
    public List<PermissionDTO> getPermissions() {
        return permissions;
    }

    /**
     * 设置用户拥有的权限列表
     * 
     * @param permissions 权限列表
     */
    public void setPermissions(List<PermissionDTO> permissions) {
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
     * 权限DTO内部类，用于表示单个权限信息
     */
    public static class PermissionDTO {
        private String permissionType;
        private String databaseName;
        private boolean allDatabases;
        private boolean withGrantOption;

        /**
         * 获取权限类型
         * 
         * @return 权限类型
         */
        public String getPermissionType() {
            return permissionType;
        }

        /**
         * 设置权限类型
         * 
         * @param permissionType 权限类型
         */
        public void setPermissionType(String permissionType) {
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
    }
}