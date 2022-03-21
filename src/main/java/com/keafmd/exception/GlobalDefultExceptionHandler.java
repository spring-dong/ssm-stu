package com.keafmd.exception;

import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * 统一拦截异常
 *
 * @author yanlin
 * @version v1.3
 * @date 2018-10-18 下午2:27
 * @since v8.0
 **/
@RestControllerAdvice
public class GlobalDefultExceptionHandler {
    /**
     * 处理参数异常，一般用于校验body参数
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorMessage handleValidationBodyException(MethodArgumentNotValidException e) {
        for (ObjectError s : e.getBindingResult().getAllErrors()) {
            return new ErrorMessage("Invalid_Request_Parameter", s.getObjectName() + ": " + s.getDefaultMessage());
        }
        return new ErrorMessage("Invalid_Request_Parameter", "未知参数错误");
    }

    /**
     * 主动throw的异常
     *
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    public ErrorMessage handleUnProccessableServiceException(ServiceException e, HttpServletResponse response) {
        response.setStatus(e.getStatusCode().value());
        return new ErrorMessage(e.getErrorCode(), e.getMessage());
    }
}