package com.keafmd.exception;

import org.springframework.http.HttpStatus;

/**
 * @author yanlin
 * @version v1.3
 * @date 2018-10-18 下午2:50
 * @since v8.0
 **/
public abstract class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 8109469326798389194L;
    protected HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;


    private String errorCode;


    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public ServiceException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}