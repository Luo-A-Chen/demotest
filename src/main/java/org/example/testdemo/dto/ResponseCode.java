package org.example.testdemo.dto;

public enum ResponseCode {
    SUCCESS(200,"操作成功"),
    FAIL(400,"失败"),
    UNAUTHORIZED(401,"非管理员"),
    FORBIDDEN(403,"禁止访问"),
    NOT_FOUND(404,"资源不存在"),
    INTERNAL_SERVER_ERROR(500,"内部服务器错误");

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
