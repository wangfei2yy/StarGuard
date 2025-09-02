package org.authority.StarGuard2.exception;

import org.authority.StarGuard2.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器，用于统一处理系统中的各种异常
 * 
 * @author System
 * @version 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理权限相关的异常
     * 
     * @param ex 权限异常
     * @param request HTTP请求
     * @return 统一的异常响应
     */
    @ExceptionHandler(PermissionException.class)
    @ResponseBody
    public ResponseEntity<ResponseDTO<?>> handlePermissionException(PermissionException ex, HttpServletRequest request) {
        logger.error("权限异常: {}", ex.getMessage(), ex);
        ResponseDTO<?> response = ResponseDTO.fail(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getCode()));
    }

    /**
     * 处理数据访问相关的异常
     * 
     * @param ex 数据访问异常
     * @param request HTTP请求
     * @return 统一的异常响应
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    public ResponseEntity<ResponseDTO<?>> handleDataAccessException(DataAccessException ex, HttpServletRequest request) {
        logger.error("数据库访问异常: {}", ex.getMessage(), ex);
        String message = "数据库操作失败，请联系管理员";
        if (ex.getCause() instanceof SQLException) {
            SQLException sqlEx = (SQLException) ex.getCause();
            message = "数据库错误: " + sqlEx.getMessage();
        }
        ResponseDTO<?> response = ResponseDTO.fail(500, message);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理参数校验异常
     * 
     * @param ex 参数校验异常
     * @param request HTTP请求
     * @return 统一的异常响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ResponseDTO<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        String errorMessage = bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        logger.error("参数校验异常: {}", errorMessage);
        ResponseDTO<?> response = ResponseDTO.fail(400, errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理所有其他未捕获的异常
     * 
     * @param ex 异常
     * @param request HTTP请求
     * @return 统一的异常响应
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ResponseDTO<?>> handleException(Exception ex, HttpServletRequest request) {
        logger.error("系统异常: {}", ex.getMessage(), ex);
        ResponseDTO<?> response = ResponseDTO.fail(500, "系统内部错误，请联系管理员");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}