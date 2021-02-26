package com.particle.model.exception;

public abstract class BaseException extends RuntimeException {

    protected int errorCode = ErrorCode.COMMON_ERROR;


    public BaseException() {
        super();
    }

    public BaseException(String errorMsg) {
        super(errorMsg);
    }

    public BaseException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
    }

    public BaseException(String errorMsg, Throwable throwable) {
        super(errorMsg, throwable);
    }

    public BaseException(int errorCode, String errorMsg, Throwable throwable) {
        super(errorMsg, throwable);
        this.errorCode = errorCode;
    }

}
