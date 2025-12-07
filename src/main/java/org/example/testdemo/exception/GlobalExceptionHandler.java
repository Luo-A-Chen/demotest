package org.example.testdemo.exception;

import org.example.testdemo.dto.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleBusinessException(BusinessException e) {
        return BaseResponse.error(e.getCode(),e.getMessage());
    }
    // 处理数据库连接异常
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<?> handleSQLException(SQLException e) {
        return BaseResponse.error(500, "数据库连接异常: " + e.getMessage());
    }
    // 处理数据库连接超时异常
    @ExceptionHandler(org.springframework.jdbc.CannotGetJdbcConnectionException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<?> handleJdbcConnectionException(org.springframework.jdbc.CannotGetJdbcConnectionException e) {
        return BaseResponse.error(500, "数据库连接失败: " + e.getMessage());
    }
    // 处理MyBatis数据库操作异常
    @ExceptionHandler(org.apache.ibatis.exceptions.PersistenceException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<?> handlePersistenceException(org.apache.ibatis.exceptions.PersistenceException e) {
        return BaseResponse.error(500, "数据库操作异常: " + e.getMessage());
    }
    // 处理Spring数据访问异常
    @ExceptionHandler(org.springframework.dao.DataAccessException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<?> handleDataAccessException(org.springframework.dao.DataAccessException e) {
        return BaseResponse.error(500, "数据访问异常: " + e.getMessage());
    }
    // 处理所有其他未捕获的异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<?> handleException(Exception e) {
        return BaseResponse.error(500, "服务器内部错误: " + e.getMessage());
    }
}
