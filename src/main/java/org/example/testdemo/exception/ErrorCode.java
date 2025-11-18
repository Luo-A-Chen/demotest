package org.example.testdemo.exception;

public enum ErrorCode {
    ACCOUNT_OR_PASSWORD_ERROR(400,"账号或密码错误"),
    UNAUTHORIZED(401,"非管理员"),
    FORBIDDEN(403,"禁止访问"),
    NOT_FOUND(404,"账户或资源不存在"),
    INTERNAL_SERVER_ERROR(500,"内部服务器错误"),
    PASSWORD_NOT_MATCH(400,"密码不匹配"),
    ACCOUNT_EXISTED(400,"账号已存在"),
    ACCOUNT_INVALID(400,"账号格式错误");
    private final int code;
    private final String message;
    ErrorCode(int code, String message) {
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
