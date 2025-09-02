package org.authority.StarGuard2.service.impl;

import org.authority.StarGuard2.model.User;
import org.authority.StarGuard2.model.Permission;
import org.authority.StarGuard2.model.PermissionType;
import org.authority.StarGuard2.repository.PermissionRepository;
import org.authority.StarGuard2.service.PermissionService;
import org.authority.StarGuard2.dto.GrantPermissionRequestDTO;
import org.authority.StarGuard2.dto.RevokePermissionRequestDTO;
import org.authority.StarGuard2.dto.UserPermissionDTO;
import org.authority.StarGuard2.exception.PermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限管理Service的实现类，实现了权限管理的业务逻辑
 * 
 * @author System
 * @version 1.0
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

    private final PermissionRepository permissionRepository;

    /**
     * 构造函数
     * 
     * @param permissionRepository 权限管理Repository
     */
    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<UserPermissionDTO> getAllUsersWithPermissions() {
        logger.info("获取所有用户及其权限信息");
        try {
            List<User> users = permissionRepository.getAllUsers();
            List<UserPermissionDTO> userPermissionDTOs = new ArrayList<>();

            for (User user : users) {
                UserPermissionDTO dto = convertToDTO(user);
                userPermissionDTOs.add(dto);
            }

            logger.info("成功获取{}个用户的权限信息", userPermissionDTOs.size());
            return userPermissionDTOs;
        } catch (Exception e) {
            logger.error("获取用户权限信息失败", e);
            throw new PermissionException("获取用户权限信息失败: " + e.getMessage(), e);
        }
    }

    @Override
    public UserPermissionDTO getUserPermissions(String username, String host) {
        logger.info("获取特定用户的权限信息: username={}, host={}", username,host);
        try {
            // 验证参数
            if (username == null || username.trim().isEmpty()) {
                throw new PermissionException("用户名不能为空");
            }
            if (host == null || host.trim().isEmpty()) {
                throw new PermissionException("主机地址不能为空");
            }
            
            // 检查用户是否存在
            if (!permissionRepository.userExists(username)) {
                throw new PermissionException("用户不存在: " + username);
            }

            User user = permissionRepository.getUserPermissions(username, host);
            UserPermissionDTO dto = convertToDTO(user);

            logger.info("成功获取用户权限信息");
            return dto;
        } catch (PermissionException e) {
            logger.error("获取用户权限信息失败", e);
            throw e;
        } catch (Exception e) {
            logger.error("获取用户权限信息失败", e);
            throw new PermissionException("获取用户权限信息失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean grantPermission(GrantPermissionRequestDTO request) {
        logger.info("授予权限: {}", request);
        try {
            // 验证请求参数
            validateGrantRequest(request);

            // 处理每个权限类型
            for (String permissionTypeStr : request.getPermissionTypes()) {
                PermissionType permissionType = PermissionType.fromString(permissionTypeStr);
                if (permissionType == null) {
                    throw new PermissionException("无效的权限类型: " + permissionTypeStr);
                }

                boolean success = permissionRepository.grantPermission(
                        request.getUsername(),
                        request.getHost(),
                        permissionType,
                        request.getDatabaseName(),
                        request.isAllDatabases(),
                        request.isWithGrantOption(),
                        request.getScopeType(),
                        request.getViewScope(),
                        request.getViewName());

                if (!success) {
                    throw new PermissionException("权限授予失败: " + permissionTypeStr);
                }
            }

            logger.info("权限授予成功");
            return true;
        } catch (PermissionException e) {
            logger.error("权限授予失败", e);
            throw e;
        } catch (Exception e) {
            logger.error("权限授予失败", e);
            throw new PermissionException("权限授予失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean revokePermission(RevokePermissionRequestDTO request) {
        logger.info("撤销权限: {}", request);
        try {
            // 验证请求参数
            validateRevokeRequest(request);

            // 处理每个权限类型
            for (String permissionTypeStr : request.getPermissionTypes()) {
                PermissionType permissionType = PermissionType.fromString(permissionTypeStr);
                if (permissionType == null) {
                    throw new PermissionException("无效的权限类型: " + permissionTypeStr);
                }

                boolean success = permissionRepository.revokePermission(
                        request.getUsername(),
                        request.getHost(),
                        permissionType,
                        request.getDatabaseName(),
                        request.isAllDatabases(),
                        request.getScopeType(),
                        request.getViewScope(),
                        request.getViewName());

                if (!success) {
                    throw new PermissionException("权限撤销失败: " + permissionTypeStr);
                }
            }

            logger.info("权限撤销成功");
            return true;
        } catch (PermissionException e) {
            logger.error("权限撤销失败", e);
            throw e;
        } catch (Exception e) {
            logger.error("权限撤销失败", e);
            throw new PermissionException("权限撤销失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean userExists(String username, String host) {
        logger.info("检查用户是否存在: username={}, host={}", username, host);
        try {
            boolean exists = permissionRepository.userExists(username);
            logger.info("用户存在检查结果: {}", exists);
            return exists;
        } catch (Exception e) {
            logger.error("检查用户是否存在失败", e);
            throw new PermissionException("检查用户是否存在失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean batchGrantPermissions(List<GrantPermissionRequestDTO> requests) {
        logger.info("批量授予权限，共{}个请求", requests.size());
        try {
            for (GrantPermissionRequestDTO request : requests) {
                grantPermission(request);
            }
            logger.info("批量授予权限成功");
            return true;
        } catch (Exception e) {
            logger.error("批量授予权限失败", e);
            throw new PermissionException("批量授予权限失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean batchRevokePermissions(List<RevokePermissionRequestDTO> requests) {
        logger.info("批量撤销权限，共{}个请求", requests.size());
        try {
            for (RevokePermissionRequestDTO request : requests) {
                revokePermission(request);
            }
            logger.info("批量撤销权限成功");
            return true;
        } catch (Exception e) {
            logger.error("批量撤销权限失败", e);
            throw new PermissionException("批量撤销权限失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将User对象转换为UserPermissionDTO
     * 
     * @param user 用户对象
     * @return 用户权限DTO
     */
    private UserPermissionDTO convertToDTO(User user) {
        UserPermissionDTO dto = new UserPermissionDTO();
        dto.setUsername(user.getUsername());
        dto.setHost(user.getHost());
        dto.setHasGrantOption(user.isHasGrantOption());

        List<UserPermissionDTO.PermissionDTO> permissionDTOs = new ArrayList<>();
        if (user.getPermissions() != null) {
            for (Permission permission : user.getPermissions()) {
                UserPermissionDTO.PermissionDTO permissionDTO = new UserPermissionDTO.PermissionDTO();
                permissionDTO.setPermissionType(permission.getPermissionType().toGrantString());
                permissionDTO.setDatabaseName(permission.getDatabaseName());
                permissionDTO.setAllDatabases(permission.isAllDatabases());
                permissionDTO.setWithGrantOption(permission.isWithGrantOption());
                permissionDTOs.add(permissionDTO);
            }
        }

        dto.setPermissions(permissionDTOs);
        return dto;
    }

    /**
     * 验证授权请求参数
     * 
     * @param request 授权请求DTO
     */
    private void validateGrantRequest(GrantPermissionRequestDTO request) {
        if (request == null) {
            throw new PermissionException("授权请求不能为空");
        }

        if (request.getPermissionTypes() == null || request.getPermissionTypes().isEmpty()) {
            throw new PermissionException("权限类型不能为空");
        }

        if (request.isAllDatabases() && (request.getDatabaseName() == null || request.getDatabaseName().trim().isEmpty())) {
            throw new PermissionException("当指定所有数据库时，数据库名称不能为空");
        }

        // 验证VIEW相关权限的字段
        if ("VIEW".equals(request.getScopeType())) {
            if (request.getViewScope() == null || request.getViewScope().trim().isEmpty()) {
                throw new PermissionException("视图作用域不能为空");
            }
            
            if ("SINGLE_VIEW".equals(request.getViewScope())) {
                if (request.getViewName() == null || request.getViewName().trim().isEmpty()) {
                    throw new PermissionException("视图名称不能为空");
                }
                if (request.getDatabaseName() == null || request.getDatabaseName().trim().isEmpty()) {
                    throw new PermissionException("视图所在数据库名称不能为空");
                }
            } else if ("ALL_VIEWS_IN_DATABASE".equals(request.getViewScope())) {
                if (request.getDatabaseName() == null || request.getDatabaseName().trim().isEmpty()) {
                    throw new PermissionException("数据库名称不能为空");
                }
            }
            // ALL_VIEWS_IN_ALL_DATABASES 不需要额外的验证
        }
        
        // 验证MATERIALIZED_VIEW相关权限的字段
        if ("MATERIALIZED_VIEW".equals(request.getScopeType())) {
            if (request.getViewScope() == null || request.getViewScope().trim().isEmpty()) {
                throw new PermissionException("物化视图作用域不能为空");
            }
            
            if ("SINGLE_VIEW".equals(request.getViewScope())) {
                if (request.getViewName() == null || request.getViewName().trim().isEmpty()) {
                    throw new PermissionException("物化视图名称不能为空");
                }
                if (request.getDatabaseName() == null || request.getDatabaseName().trim().isEmpty()) {
                    throw new PermissionException("物化视图所在数据库名称不能为空");
                }
            } else if ("ALL_VIEWS_IN_DATABASE".equals(request.getViewScope())) {
                if (request.getDatabaseName() == null || request.getDatabaseName().trim().isEmpty()) {
                    throw new PermissionException("数据库名称不能为空");
                }
            }
            // ALL_VIEWS_IN_ALL_DATABASES 不需要额外的验证
        }
        
        // 验证TABLE相关权限的字段
        if ("TABLE".equals(request.getScopeType())) {
            if (request.isAllDatabases() && (request.getDatabaseName() == null || request.getDatabaseName().trim().isEmpty())) {
                throw new PermissionException("当指定所有数据库时，数据库名称不能为空");
            }
        }
        
        // SYSTEM权限不需要额外的验证，只要指定了作用域类型即可

        // 检查用户是否存在
        if (!permissionRepository.userExists(request.getUsername())) {
            throw new PermissionException("用户不存在: " + request.getUsername() + "@" + request.getHost());
        }
    }

    /**
     * 验证撤销权限请求参数
     * 
     * @param request 撤销权限请求DTO
     */
    private void validateRevokeRequest(RevokePermissionRequestDTO request) {
        if (request == null) {
            throw new PermissionException("撤销权限请求不能为空");
        }

        if (request.getPermissionTypes() == null || request.getPermissionTypes().isEmpty()) {
            throw new PermissionException("权限类型不能为空");
        }

        if (request.isAllDatabases() && (request.getDatabaseName() == null || request.getDatabaseName().trim().isEmpty())) {
            throw new PermissionException("当指定所有数据库时，数据库名称不能为空");
        }

        // 验证VIEW相关权限的字段
        if ("VIEW".equals(request.getScopeType())) {
            if (request.getViewScope() == null || request.getViewScope().trim().isEmpty()) {
                throw new PermissionException("视图作用域不能为空");
            }
            
            if ("SINGLE_VIEW".equals(request.getViewScope())) {
                if (request.getViewName() == null || request.getViewName().trim().isEmpty()) {
                    throw new PermissionException("视图名称不能为空");
                }
                if (request.getDatabaseName() == null || request.getDatabaseName().trim().isEmpty()) {
                    throw new PermissionException("视图所在数据库名称不能为空");
                }
            } else if ("ALL_VIEWS_IN_DATABASE".equals(request.getViewScope())) {
                if (request.getDatabaseName() == null || request.getDatabaseName().trim().isEmpty()) {
                    throw new PermissionException("数据库名称不能为空");
                }
            }
            // ALL_VIEWS_IN_ALL_DATABASES 不需要额外的验证
        }
        
        // 验证MATERIALIZED_VIEW相关权限的字段
        if ("MATERIALIZED_VIEW".equals(request.getScopeType())) {
            if (request.getViewScope() == null || request.getViewScope().trim().isEmpty()) {
                throw new PermissionException("物化视图作用域不能为空");
            }
            
            if ("SINGLE_VIEW".equals(request.getViewScope())) {
                if (request.getViewName() == null || request.getViewName().trim().isEmpty()) {
                    throw new PermissionException("物化视图名称不能为空");
                }
                if (request.getDatabaseName() == null || request.getDatabaseName().trim().isEmpty()) {
                    throw new PermissionException("物化视图所在数据库名称不能为空");
                }
            } else if ("ALL_VIEWS_IN_DATABASE".equals(request.getViewScope())) {
                if (request.getDatabaseName() == null || request.getDatabaseName().trim().isEmpty()) {
                    throw new PermissionException("数据库名称不能为空");
                }
            }
            // ALL_VIEWS_IN_ALL_DATABASES 不需要额外的验证
        }

        // 检查用户是否存在
        if (!permissionRepository.userExists(request.getUsername())) {
            throw new PermissionException("用户不存在: " + request.getUsername() + "@" + request.getHost());
        }
    }
}