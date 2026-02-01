package com.xgz.cli.exception;

import cn.dev33.satoken.exception.NotLoginException;
import com.xgz.cli.enums.ResultCode;
import com.xgz.cli.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author: Xgz
 * @date: 2026/1/31
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 请求方式错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleException(HttpRequestMethodNotSupportedException ex) {
        return new Result(ResultCode.REQUEST_METHOD_ERROR.getCode(), ResultCode.REQUEST_METHOD_ERROR.getMessage());
    }

    /**
     * post请求参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidHandler(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        String msg =
                result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.joining("; "));
        log.error("global exception handler[MethodArgumentNotValidException] message is:{}", msg, ex);
        List<ParamsErrorVO> paramsErrorList = result.getFieldErrors().stream().map(
                item -> new ParamsErrorVO(item.getField(), item.getDefaultMessage())).collect(Collectors.toList());
        log.info("paramsErrorList is :[{}]", paramsErrorList);
        return new Result(ResultCode.BAD_REQUEST.getCode(), ResultCode.BAD_REQUEST.getMessage(), paramsErrorList);
    }

    /**
     * get请求校验参数异常
     */
    @ExceptionHandler(BindException.class)
    public Result bindExceptionHandler(BindException ex) {
        BindingResult result = ex.getBindingResult();
        String msg =
                result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.joining("; "));
        log.error("global exception handler[BindException] message is :{}", msg, ex);
        List<ParamsErrorVO> paramsErrorList = result.getFieldErrors().stream().map(item -> new ParamsErrorVO(item.getField(), item.getDefaultMessage())).collect(Collectors.toList());
        log.info("paramsErrorList is :[{}]", paramsErrorList);
        return new Result(ResultCode.BAD_REQUEST.getCode(), ResultCode.BAD_REQUEST.getMessage(), paramsErrorList);
    }

    /**
     * 请求校验参数异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result constraintViolationHandler(ConstraintViolationException ex) {
        List<String> msgList = new ArrayList<>();
        for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
            msgList.add(constraintViolation.getMessage());
        }
        String msg = msgList.stream().collect(Collectors.joining("; "));
        log.info("msg is :[{}]", msg);
        return new Result(ResultCode.BAD_REQUEST.getCode(), ResultCode.BAD_REQUEST.getMessage(), msg);
    }

    /**
     * 自定义异常捕获拦截
     */
    @ExceptionHandler(CustomException.class)
    public Result<String> baseExceptionHandler(CustomException ex) {
        log.error("global exception handler[CustomException] message is:{}", ex.getMessage(), ex);
        return new Result(ResultCode.SERVER_ERROR.getCode(), ex.getMessage(), null);
    }

    /**
     * token校验异常拦截
     */
    @ExceptionHandler(NotLoginException.class)
    public Result<String> notLoginHandler(NotLoginException ex) {
        log.error("global exception handler[NotLoginException]", ex);
        return new Result(ResultCode.TOKEN_FAILURE.getCode(), ResultCode.TOKEN_FAILURE.getMessage());
    }

    /**
     * 全局异常拦截
     */
    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception ex) {
        log.error("global exception handler[Exception] message is:{}", ex.getMessage(), ex);
        return Result.fail(ResultCode.SERVER_ERROR.getCode(), ResultCode.SERVER_ERROR.getMessage());
    }
}
