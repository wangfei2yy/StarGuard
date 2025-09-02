package org.authority.StarGuard2.dto;

/**
 * 统一响应DTO类，用于统一API响应格式
 * 
 * @param <T> 响应数据的泛型类型
 * @author System
 * @version 1.0
 */
public class ResponseDTO<T> {
    private int code;
    private String message;
    private T data;

    /**
     * 构造函数
     */
    public ResponseDTO() {
    }

    /**
     * 构造函数
     * 
     * @param code 响应状态码
     * @param message 响应消息
     * @param data 响应数据
     */
    public ResponseDTO(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 创建成功响应
     * 
     * @param data 响应数据
     * @param <T> 响应数据的泛型类型
     * @return 成功响应对象
     */
    public static <T> ResponseDTO<T> success(T data) {
        return new ResponseDTO<>(200, "操作成功", data);
    }

    /**
     * 创建成功响应（无数据）
     * 
     * @param <T> 响应数据的泛型类型
     * @return 成功响应对象
     */
    public static <T> ResponseDTO<T> success() {
        return new ResponseDTO<>(200, "操作成功", null);
    }

    /**
     * 创建失败响应
     * 
     * @param message 失败消息
     * @param <T> 响应数据的泛型类型
     * @return 失败响应对象
     */
    public static <T> ResponseDTO<T> fail(String message) {
        return new ResponseDTO<>(500, message, null);
    }

    /**
     * 创建失败响应
     * 
     * @param code 失败状态码
     * @param message 失败消息
     * @param <T> 响应数据的泛型类型
     * @return 失败响应对象
     */
    public static <T> ResponseDTO<T> fail(int code, String message) {
        return new ResponseDTO<>(code, message, null);
    }

    /**
     * 获取响应状态码
     * 
     * @return 响应状态码
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置响应状态码
     * 
     * @param code 响应状态码
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 获取响应消息
     * 
     * @return 响应消息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置响应消息
     * 
     * @param message 响应消息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取响应数据
     * 
     * @return 响应数据
     */
    public T getData() {
        return data;
    }

    /**
     * 设置响应数据
     * 
     * @param data 响应数据
     */
    public void setData(T data) {
        this.data = data;
    }
}