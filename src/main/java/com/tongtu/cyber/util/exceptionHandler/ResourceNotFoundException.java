package com.tongtu.cyber.util.exceptionHandler;

/**
 * controller异常信息-基类-自定义异常类型(文件资源找不到报错)
 *
 * @author : 陈世恩
 * @date : 2024/5/29 15:40
 */

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super();
    }

    /**
     * Constructs an <code>IllegalArgumentException</code> with the
     * specified detail message.
     *
     * @param s the detail message.
     */
    public ResourceNotFoundException(String s) {
        super(s);
    }
}
