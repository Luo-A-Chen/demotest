package org.example.testdemo.exception;

import org.example.testdemo.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleBusinessException(BusinessException e) {
        return BaseResponse.error(e.getCode(),e.getMessage());
    }
}
