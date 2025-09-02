package org.authority.StarGuard2.repository.impl;

import org.authority.StarGuard2.model.User;
import org.authority.StarGuard2.model.Permission;
import org.authority.StarGuard2.model.PermissionType;
import org.authority.StarGuard2.repository.PermissionRepository;
import org.authority.StarGuard2.exception.PermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 权限管理Repository的实现类，使用JDBC与StarRocks数据库交互
 * 
 * @author System
 * @version 1.0
 */
@Repository
public class PermissionRepositoryImpl implements PermissionRepository {
    private static final Logger logger = LoggerFactory.getLogger(PermissionRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;

    /**
     * 构造函数
     * 
     * @param jdbcTemplate Spring提供的JDBC模板
     */
    @Autowired
    public PermissionRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("获取所有用户列表");
        try {
            String sql = "SHOW USERS";
            List<Map<String, Object>> userMaps = jdbcTemplate.queryForList(sql);
            List<User> users = new ArrayList<>();

            for (Map<String, Object> userMap : userMaps) {
                System.out.println("user----------------"+userMap.get("User"));
                String userIdentity = (String) userMap.get("User");
                // 解析用户标识，格式为 'username'@'host'
                String[] parts = parseUserIdentity(userIdentity);
                String username = parts[0];
                String host = parts[1];
                
                User user = getUserPermissions(username,host);
                users.add(user);
            }
            
            logger.info("成功获取{}个用户", users.size());
            return users;
        } catch (Exception e) {
            logger.error("获取用户列表失败", e);
            throw new PermissionException("获取用户列表失败: " + e.getMessage(), e);
        }
    }

    @Override
    public User getUserPermissions(String username, String host) {
        logger.info("获取用户权限: username={},host={}", username, host);
        User user = new User(username, host);
        List<Permission> permissions = new ArrayList<>();
        boolean hasGrantOption = false;
        
        try {
            // 构建SQL语句
            String sql = String.format("SHOW GRANTS FOR '%s'@'%s'", username, host);
            System.out.println("查看权限sql---------------------"+sql);
            
            // 执行查询
            List<Map<String, Object>> grantMaps = jdbcTemplate.queryForList(sql);
            
            // 解析授权语句
            for (Map<String, Object> grantMap : grantMaps) {
                String grantStatement = (String) grantMap.get("Grants");
                List<Permission> permissionList = parseGrantStatement(grantStatement);
                if (permissionList != null && !permissionList.isEmpty()) {
                    permissions.addAll(permissionList);
                    for (Permission permission : permissionList) {
                        if (permission.isWithGrantOption()) {
                            hasGrantOption = true;
                        }
                    }
                }
            }
            
            user.setPermissions(permissions);
            user.setHasGrantOption(hasGrantOption);
            
            logger.info("成功获取用户权限，共{}个权限", permissions.size());
            return user;
        } catch (Exception e) {
            logger.error("获取用户权限失败: {}", e.getMessage(), e);
            throw new PermissionException("获取用户权限失败", e);
        }
    }

    @Override
    @Transactional
    public boolean grantPermission(String username, String host, PermissionType permissionType, 
                                 String databaseName, boolean allDatabases, boolean withGrantOption,
                                 String scopeType, String viewScope, String viewName) {
        logger.info("授予权限: username={}, host={}, permissionType={}, databaseName={}, allDatabases={}, withGrantOption={}, scopeType={}, viewScope={}, viewName={}",
                username, host, permissionType, databaseName, allDatabases, withGrantOption, scopeType, viewScope, viewName);
        try {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("GRANT ").append(permissionType.toGrantString()).append(" ON ");
            
            if ("VIEW".equals(scopeType)) {
                if ("SINGLE_VIEW".equals(viewScope)) {
                    sqlBuilder.append("VIEW `").append(databaseName).append("`.`").append(viewName).append("`");
                } else if ("ALL_VIEWS_IN_DATABASE".equals(viewScope)) {
                    sqlBuilder.append("ALL VIEWS IN DATABASE `").append(databaseName).append("`");
                } else if ("ALL_VIEWS_IN_ALL_DATABASES".equals(viewScope)) {
                    sqlBuilder.append("ALL VIEWS IN ALL DATABASES");
                }
            } else if ("MATERIALIZED_VIEW".equals(scopeType)) {
                if ("SINGLE_VIEW".equals(viewScope)) {
                    sqlBuilder.append("MATERIALIZED VIEW `").append(databaseName).append("`.`").append(viewName).append("`");
                } else if ("ALL_VIEWS_IN_DATABASE".equals(viewScope)) {
                    sqlBuilder.append("ALL MATERIALIZED VIEWS IN DATABASE `").append(databaseName).append("`");
                } else if ("ALL_VIEWS_IN_ALL_DATABASES".equals(viewScope)) {
                    sqlBuilder.append("ALL MATERIALIZED VIEWS IN ALL DATABASES");
                }
            } else if ("TABLE".equals(scopeType)) {
                if (allDatabases) {
                    sqlBuilder.append("ALL TABLES IN ALL DATABASES");
                } else {
                    sqlBuilder.append("ALL TABLES IN DATABASE `").append(databaseName).append("`");
                }
            } else if ("SYSTEM".equals(scopeType)) {
                sqlBuilder.append("SYSTEM");
            } else {
                if (allDatabases) {
                    sqlBuilder.append("ALL DATABASES");
                } else {
                    sqlBuilder.append("DATABASE `").append(databaseName).append("`");
                }
            }
            
            // 检查是否是角色授权，如果是，使用角色授权格式
            if (permissionType == PermissionType.ROLE_GRANT) {
                // 角色授权格式: GRANT 'role' TO 'user'@'host'
                sqlBuilder.setLength(0); // 清空之前的内容
                sqlBuilder.append("GRANT '").append(databaseName).append("' TO '").append(username).append("'@'").append(host).append("'");
            } else {
                // 标准授权格式: GRANT permission ON object TO 'user'@'host'
                sqlBuilder.append(" TO '").append(username).append("'@'").append(host).append("'");
            }
            
            if (withGrantOption) {
                sqlBuilder.append(" WITH GRANT OPTION");
            }
            
            String sql = sqlBuilder.toString();
            logger.debug("执行授权SQL: {}", sql);
            
            jdbcTemplate.execute(sql);
            logger.info("权限授予成功");
            return true;
        } catch (Exception e) {
            logger.error("权限授予失败", e);
            throw new PermissionException("权限授予失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean revokePermission(String username, String host, PermissionType permissionType, 
                                 String databaseName, boolean allDatabases, 
                                 String scopeType, String viewScope, String viewName) {
        logger.info("撤销权限: username={}, host={}, permissionType={}, databaseName={}, allDatabases={}, scopeType={}, viewScope={}, viewName={}",
                username, host, permissionType, databaseName, allDatabases, scopeType, viewScope, viewName);
        try {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("REVOKE ").append(permissionType.toGrantString()).append(" ON ");
            
            if ("VIEW".equals(scopeType)) {
                if ("SINGLE_VIEW".equals(viewScope)) {
                    sqlBuilder.append("VIEW `").append(databaseName).append("`.`").append(viewName).append("`");
                } else if ("ALL_VIEWS_IN_DATABASE".equals(viewScope)) {
                    sqlBuilder.append("ALL VIEWS IN DATABASE `").append(databaseName).append("`");
                } else if ("ALL_VIEWS_IN_ALL_DATABASES".equals(viewScope)) {
                    sqlBuilder.append("ALL VIEWS IN ALL DATABASES");
                }
            } else if ("MATERIALIZED_VIEW".equals(scopeType)) {
                if ("SINGLE_VIEW".equals(viewScope)) {
                    sqlBuilder.append("MATERIALIZED VIEW `").append(databaseName).append("`.`").append(viewName).append("`");
                } else if ("ALL_VIEWS_IN_DATABASE".equals(viewScope)) {
                    sqlBuilder.append("ALL MATERIALIZED VIEWS IN DATABASE `").append(databaseName).append("`");
                } else if ("ALL_VIEWS_IN_ALL_DATABASES".equals(viewScope)) {
                    sqlBuilder.append("ALL MATERIALIZED VIEWS IN ALL DATABASES");
                }
            } else if ("TABLE".equals(scopeType)) {
                if (allDatabases) {
                    sqlBuilder.append("ALL TABLES IN ALL DATABASES");
                } else {
                    sqlBuilder.append("ALL TABLES IN DATABASE `").append(databaseName).append("`");
                }
            } else if ("SYSTEM".equals(scopeType)) {
                sqlBuilder.append("SYSTEM");
            } else {
                if (allDatabases) {
                    sqlBuilder.append("ALL DATABASES");
                } else {
                    sqlBuilder.append("DATABASE `").append(databaseName).append("`");
                }
            }
            
            // 检查是否是角色授权，如果是，使用角色撤销格式
            if (permissionType == PermissionType.ROLE_GRANT) {
                // 角色撤销格式: REVOKE 'role' FROM 'user'@'host'
                sqlBuilder.setLength(0); // 清空之前的内容
                sqlBuilder.append("REVOKE '").append(databaseName).append("' FROM '").append(username).append("'@'").append(host).append("'");
            } else {
                // 标准撤销格式: REVOKE permission ON object FROM 'user'@'host'
                sqlBuilder.append(" FROM '").append(username).append("'@'").append(host).append("'");
            }
            
            String sql = sqlBuilder.toString();
            logger.debug("执行撤销SQL: {}", sql);
            
            jdbcTemplate.execute(sql);
            logger.info("权限撤销成功");
            return true;
        } catch (Exception e) {
            logger.error("权限撤销失败", e);
            throw new PermissionException("权限撤销失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean userExists(String username) {
        logger.info("检查用户是否存在: username={}, host={}", username);
        try {
            String sql = "SHOW USERS";
            List<Map<String, Object>> userMaps = jdbcTemplate.queryForList(sql);
            
            for (Map<String, Object> userMap : userMaps) {
                String userIdentity = (String) userMap.get("User");
                String[] parts = parseUserIdentity(userIdentity);
                if (parts[0].equals(username)) {
                    logger.info("用户存在");
                    return true;
                }
            }
            
            logger.info("用户不存在");
            return false;
        } catch (Exception e) {
            logger.error("检查用户是否存在失败", e);
            throw new PermissionException("检查用户是否存在失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean executeSql(String sql) {
        logger.info("执行自定义SQL语句");
        logger.debug("自定义SQL: {}", sql);
        try {
            jdbcTemplate.execute(sql);
            logger.info("SQL执行成功");
            return true;
        } catch (Exception e) {
            logger.error("SQL执行失败", e);
            throw new PermissionException("SQL执行失败: " + e.getMessage(), e);
        }
    }

    /**
     * 解析用户标识字符串，提取用户名和主机地址
     * 
     * @param userIdentity 用户标识字符串，格式为 'username'@'host'
     * @return 包含用户名和主机地址的数组
     */
    private String[] parseUserIdentity(String userIdentity) {
        // 移除引号并分割用户名和主机
        userIdentity = userIdentity.replace("'", "");
        int atIndex = userIdentity.indexOf('@');
        String username = userIdentity.substring(0, atIndex);
        String host = userIdentity.substring(atIndex + 1);
        return new String[]{username, host};
    }

    /**
     * 解析授权语句，提取权限信息
     * 
     * @param grantStatement 授权语句字符串
     * @return 权限对象列表，如果解析失败则返回null
     */
    private List<Permission> parseGrantStatement(String grantStatement) {
        try {
            // 示例授权语句: GRANT SELECT ON database.table TO 'user'@'host' WITH GRANT OPTION
            grantStatement = grantStatement.trim();
            if (!grantStatement.startsWith("GRANT ")) {
                return null;
            }

            // 检查是否是角色授权格式: GRANT 'role' TO 'user'@'host'
            int toIndex = grantStatement.indexOf(" TO ");
            if (toIndex > 0) {
                String beforeToPart = grantStatement.substring(6, toIndex).trim();
                
                // 如果字符串包含引号且不包含" ON "，则认为是角色授权格式
                if (beforeToPart.contains("'") && !beforeToPart.contains(" ON ")) {
                    // 提取角色名称（去掉引号）
                    String roleName = beforeToPart.replace("'", "");
                    
                    // 创建角色授权类型的权限对象
                    Permission permission = new Permission(PermissionType.ROLE_GRANT, "ROLE: " + roleName);
                    
                    // 检查是否带有GRANT OPTION
                    boolean withGrantOption = grantStatement.contains("WITH GRANT OPTION");
                    permission.setWithGrantOption(withGrantOption);
                    
                    List<Permission> permissions = new ArrayList<>();
                    permissions.add(permission);
                    return permissions;
                }
            }

            // 标准授权格式处理
            // 提取权限类型
            int onIndex = grantStatement.indexOf(" ON ");
            if (onIndex <= 0) {
                logger.warn("授权语句格式不正确，缺少 ON 部分: {}", grantStatement);
                return null;
            }
            
            String permissionPart = grantStatement.substring(6, onIndex).trim();
            List<PermissionType> permissionTypes = new ArrayList<>();
            
            // 处理多个权限类型用逗号分隔的情况
            if (permissionPart.contains(",")) {
                // 解析所有权限类型
                String[] permissions = permissionPart.split(",");
                for (String perm : permissions) {
                    String trimmedPerm = perm.trim();
                    try {
                        PermissionType pt = PermissionType.fromString(trimmedPerm);
                        if (pt != null) {
                            permissionTypes.add(pt);
                        }
                    } catch (IllegalArgumentException e) {
                        // 忽略无效的权限类型，继续尝试下一个
                        logger.warn("未知的权限类型: {}", trimmedPerm);
                    }
                }
            } else {
                // 单权限类型处理
                try {
                    PermissionType pt = PermissionType.fromString(permissionPart);
                    if (pt != null) {
                        permissionTypes.add(pt);
                    }
                } catch (IllegalArgumentException e) {
                    // 如果找不到精确匹配的权限类型，记录警告
                    logger.warn("未知的权限类型: {}", permissionPart);
                }
            }
            
            if (permissionTypes.isEmpty()) {
                // 如果没有找到有效的权限类型，默认为ALL_PRIVILEGES
                permissionTypes.add(PermissionType.ALL_PRIVILEGES);
            }

            // 提取数据库部分，处理TO后面可能有USER关键字的情况
            toIndex = grantStatement.indexOf(" TO ", onIndex);
            if (toIndex <= 0) {
                logger.warn("授权语句格式不正确，缺少 TO 部分: {}", grantStatement);
                return null;
            }
            
            String databasePart = grantStatement.substring(onIndex + 4, toIndex).trim();
            boolean allDatabases = databasePart.equals("ALL DATABASES");
            // 处理ALL TABLES IN DATABASE格式
            boolean allTablesInDatabase = databasePart.startsWith("ALL TABLES IN DATABASE");
            
            String databaseName;
            if (allDatabases) {
                databaseName = "ALL DATABASES";
            } else if (allTablesInDatabase) {
                // 从ALL TABLES IN DATABASE格式中提取数据库名
                databaseName = extractDatabaseName(databasePart.replaceFirst("^ALL TABLES IN ", ""));
            } else {
                databaseName = extractDatabaseName(databasePart);
            }

            // 检查是否带有GRANT OPTION
            boolean withGrantOption = grantStatement.contains("WITH GRANT OPTION");
            
            // 为每个权限类型创建一个Permission对象
            List<Permission> resultPermissions = new ArrayList<>();
            for (PermissionType pt : permissionTypes) {
                Permission permission = new Permission(pt, databaseName);
                permission.setWithGrantOption(withGrantOption);
                resultPermissions.add(permission);
            }

            return resultPermissions;
        } catch (Exception e) {
            logger.warn("解析授权语句失败: {}", grantStatement, e);
            return null;
        }
    }

    /**
     * 从数据库部分字符串中提取数据库名称
     * 
     * @param databasePart 数据库部分字符串，格式为 DATABASE database_name 或 ALL TABLES IN DATABASE database_name
     * @return 数据库名称
     */
    private String extractDatabaseName(String databasePart) {
        if (databasePart.startsWith("DATABASE ")) {
            return databasePart.substring(10).replace("`", "").trim();
        }
        return databasePart.replace("`", "").trim();
    }
}