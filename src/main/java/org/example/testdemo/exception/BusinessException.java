package org.example.testdemo.exception;

public class BusinessException extends RuntimeException {
    private final int code;
    public BusinessException(int code,String message) {
        super(message);
        this.code = code;
    }
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }
    public int getCode() {
        return code;
    }
}
