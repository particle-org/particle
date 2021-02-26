package com.particle.model.exception;

public class ProphetException extends BaseException {

    public ProphetException() {
        super();
    }

    public ProphetException(int code, String errorMsg) {
        super(code, errorMsg);
    }

    public ProphetException(int code, String errorMsg, Throwable throwable) {
        super(code, errorMsg, throwable);
    }
}
