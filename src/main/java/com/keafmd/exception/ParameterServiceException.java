package com.keafmd.exception;

import org.springframework.http.HttpStatus;

/**
 * @author yanlin
 * @version v1.3
 * @date 2018-10-18 下午3:00
 * @since v8.0
 **/
public class ParameterServiceException extends ServiceException {
    private static final long serialVersionUID = 8362753245631601878L;

    public ParameterServiceException(String errorCode, String message) {
        super(errorCode, message);
        this.statusCode = HttpStatus.UNPROCESSABLE_ENTITY;
    }
}