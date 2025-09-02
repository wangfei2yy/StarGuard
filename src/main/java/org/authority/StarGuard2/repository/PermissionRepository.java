package org.authority.StarGuard2.repository;

import org.authority.StarGuard2.model.User;
import org.authority.StarGuard2.model.PermissionType;

import java.util.List;

/**
 * 权限管理Repository接口，定义了权限管理的核心操作
 * 
 * @author System
 * @version 1.0
 */
public interface PermissionRepository {
    /**
     * 获取所有用户列表
     * 
     * @return 用户列表
     */
    List<User> getAllUsers();

    /**
     * 获取特定用户的权限信息
     * 
     * @param username 用户名
     * @param host 主机地址
     * @return 用户对象，包含其权限信息
     */
    User getUserPermissions(String username, String host);

    /**
     * 授予权限给用户
     * 
     * @param username 用户名
     * @param host 主机地址
     * @param permissionType 权限类型
     * @param databaseName 数据库名称
     * @param allDatabases 是否是所有数据库的权限
     * @param withGrantOption 是否带有GRANT OPTION权限
     * @param scopeType 作用域类型（DATABASE/VIEW）
     * @param viewScope 视图作用域（SINGLE_VIEW/ALL_VIEWS_IN_DATABASE/ALL_VIEWS_IN_ALL_DATABASES）
     * @param viewName 视图名称
     * @return 操作结果，true表示成功
     */
    boolean grantPermission(String username, String host, PermissionType permissionType, 
                           String databaseName, boolean allDatabases, boolean withGrantOption,
                           String scopeType, String viewScope, String viewName);

    /**
     * 撤销用户的权限
     * 
     * @param username 用户名
     * @param host 主机地址
     * @param permissionType 权限类型
     * @param databaseName 数据库名称
     * @param allDatabases 是否是所有数据库的权限
     * @param scopeType 作用域类型（DATABASE/VIEW）
     * @param viewScope 视图作用域（SINGLE_VIEW/ALL_VIEWS_IN_DATABASE/ALL_VIEWS_IN_ALL_DATABASES）
     * @param viewName 视图名称
     * @return 操作结果，true表示成功
     */
    boolean revokePermission(String username, String host, PermissionType permissionType, 
                           String databaseName, boolean allDatabases,
                           String scopeType, String viewScope, String viewName);

    /**
     * 检查用户是否存在
     * 
     * @param username 用户名
     * @return true表示用户存在
     */
    boolean userExists(String username);

    /**
     * 执行自定义SQL语句
     * 
     * @param sql SQL语句
     * @return 操作结果，true表示成功
     */
    boolean executeSql(String sql);
}