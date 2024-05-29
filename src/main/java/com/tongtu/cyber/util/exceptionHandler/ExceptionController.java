package com.tongtu.cyber.util.exceptionHandler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试controller层 异常信息返回
 *
 * @author : 陈世恩
 * @date : 2024/5/29 15:46
 */
@Api(value = "ExceptionController", tags = "测试controller层 异常信息返回")
@RestController
@RequestMapping("/api")
public class ExceptionController {
    @ApiOperation(value = "测试参数校验异常返回")
    @GetMapping("/illegalArgumentException")
    public void throwException() {
        throw new IllegalArgumentException();
    }

    @ApiOperation(value = "测试资源异常返回")
    @GetMapping("/resourceNotFoundException")
    public void throwException2() {
        throw new ResourceNotFoundException();
    }
}
