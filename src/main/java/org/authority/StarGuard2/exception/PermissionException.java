package org.authority.StarGuard2.exception;

/**
 * 权限相关的异常类，用于表示权限操作过程中出现的异常
 * 
 * @author System
 * @version 1.0
 */
public class PermissionException extends RuntimeException {
    private int code;

    /**
     * 构造函数
     * 
     * @param message 异常消息
     */
    public PermissionException(String message) {
        super(message);
        this.code = 500;
    }

    /**
     * 构造函数
     * 
     * @param code 异常状态码
     * @param message 异常消息
     */
    public PermissionException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造函数
     * 
     * @param message 异常消息
     * @param cause 异常原因
     */
    public PermissionException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
    }

    /**
     * 构造函数
     * 
     * @param code 异常状态码
     * @param message 异常消息
     * @param cause 异常原因
     */
    public PermissionException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * 获取异常状态码
     * 
     * @return 异常状态码
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置异常状态码
     * 
     * @param code 异常状态码
     */
    public void setCode(int code) {
        this.code = code;
    }
}