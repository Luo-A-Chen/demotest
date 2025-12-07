package org.example.testdemo.dto.response;

public class BaseResponse<T> {
    private int code;
    private String message;
    private T data;

    public BaseResponse() {
    }

    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(),null);
    }
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }
    public static <T> BaseResponse<T> success(T data,String message) {
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(),message,data);
    }
    public static <T> BaseResponse<T> error() {
        return new BaseResponse<>(ResponseCode.FAIL.getCode(), ResponseCode.FAIL.getMessage(),null);
    }
    public static <T> BaseResponse<T> error(String message) {
        return new BaseResponse<>(ResponseCode.FAIL.getCode(),message,null);
    }
    public static <T> BaseResponse<T> error(int code,String message) {
        return new BaseResponse<>(code,message,null);
    }
     public int getCode() {
        return code;
    }
    public void setCode(int code) {
         this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
