package com.keafmd.exception;

import java.io.Serializable;

/**
 * @author yanlin
 * @version v1.3
 * @date 2018-10-18 下午2:36
 * @since v8.0
 **/
public class ErrorMessage implements Serializable {

    private static final long serialVersionUID = 8065583911104112360L;
    private String errorCode;
    private String errorMessage;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorMessage(String errorCode, String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorMessage() {
        super();
    }
}