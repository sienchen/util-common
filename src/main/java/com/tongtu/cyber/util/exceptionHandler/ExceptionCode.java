package com.tongtu.cyber.util.exceptionHandler;

import org.springframework.http.HttpStatus;

/**
 * controller层 异常信息返回
 *
 * @author : 陈世恩
 * @date : 2024/5/29 16:32
 */
public enum ExceptionCode {
    RESOURCE_NOT_FOUND(1001, HttpStatus.NOT_FOUND, "未找到该资源"),
    REQUEST_VALIDATION_FAILED(1002, HttpStatus.BAD_REQUEST, "请求参数格式验证失败");
    private final int code;

    private final HttpStatus status;

    private final String message;

    ExceptionCode(int code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
    public int getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
