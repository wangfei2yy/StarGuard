package org.authority.StarGuard2.service;

import org.authority.StarGuard2.dto.GrantPermissionRequestDTO;
import org.authority.StarGuard2.dto.RevokePermissionRequestDTO;
import org.authority.StarGuard2.dto.UserPermissionDTO;

import java.util.List;

/**
 * 权限管理Service接口，定义了权限管理的业务逻辑操作
 * 
 * @author System
 * @version 1.0
 */
public interface PermissionService {
    /**
     * 获取所有用户列表
     * 
     * @return 用户列表DTO
     */
    List<UserPermissionDTO> getAllUsersWithPermissions();

    /**
     * 获取特定用户的权限信息
     * 
     * @param username 用户名
     * @param host
     * @return 用户权限DTO
     */
    UserPermissionDTO getUserPermissions(String username,String host);

    /**
     * 授予权限给用户
     * 
     * @param request 授权请求DTO
     * @return 操作结果，true表示成功
     */
    boolean grantPermission(GrantPermissionRequestDTO request);

    /**
     * 撤销用户的权限
     * 
     * @param request 撤销权限请求DTO
     * @return 操作结果，true表示成功
     */
    boolean revokePermission(RevokePermissionRequestDTO request);

    /**
     * 检查用户是否存在
     * 
     * @param username 用户名
     * @param host 主机地址
     * @return true表示用户存在
     */
    boolean userExists(String username, String host);

    /**
     * 批量授予权限给用户
     * 
     * @param requests 授权请求DTO列表
     * @return 操作结果，true表示全部成功
     */
    boolean batchGrantPermissions(List<GrantPermissionRequestDTO> requests);

    /**
     * 批量撤销用户的权限
     * 
     * @param requests 撤销权限请求DTO列表
     * @return 操作结果，true表示全部成功
     */
    boolean batchRevokePermissions(List<RevokePermissionRequestDTO> requests);
}