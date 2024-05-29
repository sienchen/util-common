package com.tongtu.cyber.util.exceptionHandler;

import com.tongtu.cyber.common.api.vo.Result;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * controller层 错误信息返回
 *
 * @author : 陈世恩
 * @date : 2024/5/29 15:39
 */
@ControllerAdvice(assignableTypes = {ExceptionController.class})
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)// 拦截所有异常, 这里只是为了演示，一般情况一种异常对应一个方法处理
    public ResponseEntity<Result> exceptionHandler(IllegalArgumentException e, HttpServletRequest request) {
        ExceptionCode exceptionCode = ExceptionCode.REQUEST_VALIDATION_FAILED;
        Result result = new Result();
        result.setCode(exceptionCode.getCode());
        result.setMessage(exceptionCode.getMessage());
        result.setResult(request.getRequestURI());
        return ResponseEntity.status(exceptionCode.getStatus()).body(result);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class) //拦截指定异常
    public ResponseEntity<Result> handleResourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest request) {
        ExceptionCode exceptionCode = ExceptionCode.RESOURCE_NOT_FOUND;
        Result result = new Result();
        result.setCode(exceptionCode.getCode());
        result.setMessage(exceptionCode.getMessage());
        result.setResult(request.getRequestURI());
        return new ResponseEntity<>(result, new HttpHeaders(), exceptionCode.getStatus());
    }
}
