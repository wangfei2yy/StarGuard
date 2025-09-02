package org.authority.StarGuard2.controller;

import org.authority.StarGuard2.service.PermissionService;
import org.authority.StarGuard2.dto.GrantPermissionRequestDTO;
import org.authority.StarGuard2.dto.RevokePermissionRequestDTO;
import org.authority.StarGuard2.dto.UserPermissionDTO;
import org.authority.StarGuard2.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 权限管理Controller，处理与权限管理相关的HTTP请求
 * 
 * @author System
 * @version 1.0
 */
@RestController
@RequestMapping("/api/permission")
@Validated
public class PermissionController {
    private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);

    private final PermissionService permissionService;

    /**
     * 构造函数
     * 
     * @param permissionService 权限管理Service
     */
    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * 获取所有用户及其权限列表
     * 
     * @return 包含所有用户权限信息的响应
     */
    @GetMapping("/users")
    public ResponseDTO<List<UserPermissionDTO>> getAllUsersWithPermissions() {
        logger.info("接收获取所有用户及其权限列表的请求");
        List<UserPermissionDTO> userPermissionDTOs = permissionService.getAllUsersWithPermissions();
        return ResponseDTO.success(userPermissionDTOs);
    }

    /**
     * 获取特定用户的权限信息
     * 
     * @param username 用户名
     * @param host 主机地址
     * @return 包含用户权限信息的响应
     */
    @GetMapping("/users/{username}/{host}")
    public ResponseDTO<UserPermissionDTO> getUserPermissions(
            @PathVariable @NotEmpty(message = "用户名不能为空") String username,
            @PathVariable @NotEmpty(message = "主机地址不能为空") String host) {
        System.out.println("host ............"+host);
        logger.info("接收获取特定用户权限信息的请求: username={}, host={}", username, host);
        UserPermissionDTO userPermissionDTO = permissionService.getUserPermissions(username,host);
        return ResponseDTO.success(userPermissionDTO);
    }

    /**
     * 授予权限给用户
     * 
     * @param request 授权请求DTO
     * @return 操作结果响应
     */
    @PostMapping("/grant")
    public ResponseDTO<Boolean> grantPermission(@Valid @RequestBody GrantPermissionRequestDTO request) {
        logger.info("接收授予权限请求: {}", request);
        boolean success = permissionService.grantPermission(request);
        return ResponseDTO.success(success);
    }

    /**
     * 撤销用户的权限
     * 
     * @param request 撤销权限请求DTO
     * @return 操作结果响应
     */
    @PostMapping("/revoke")
    public ResponseDTO<Boolean> revokePermission(@Valid @RequestBody RevokePermissionRequestDTO request) {
        logger.info("接收撤销权限请求: {}", request);
        boolean success = permissionService.revokePermission(request);
        return ResponseDTO.success(success);
    }

    /**
     * 检查用户是否存在
     * 
     * @param username 用户名
     * @param host 主机地址
     * @return 用户存在状态响应
     */
    @GetMapping("/users/exists")
    public ResponseDTO<Boolean> checkUserExists(
            @RequestParam @NotEmpty(message = "用户名不能为空") String username,
            @RequestParam @NotEmpty(message = "主机地址不能为空") String host) {
        logger.info("接收检查用户是否存在请求: username={}, host={}", username, host);
        boolean exists = permissionService.userExists(username, host);
        return ResponseDTO.success(exists);
    }

    /**
     * 批量授予权限给用户
     * 
     * @param requests 授权请求DTO列表
     * @return 操作结果响应
     */
    @PostMapping("/batch/grant")
    public ResponseDTO<Boolean> batchGrantPermissions(@Valid @RequestBody List<GrantPermissionRequestDTO> requests) {
        logger.info("接收批量授予权限请求，共{}个请求", requests.size());
        boolean success = permissionService.batchGrantPermissions(requests);
        return ResponseDTO.success(success);
    }

    /**
     * 批量撤销用户的权限
     * 
     * @param requests 撤销权限请求DTO列表
     * @return 操作结果响应
     */
    @PostMapping("/batch/revoke")
    public ResponseDTO<Boolean> batchRevokePermissions(@Valid @RequestBody List<RevokePermissionRequestDTO> requests) {
        logger.info("接收批量撤销权限请求，共{}个请求", requests.size());
        boolean success = permissionService.batchRevokePermissions(requests);
        return ResponseDTO.success(success);
    }
}