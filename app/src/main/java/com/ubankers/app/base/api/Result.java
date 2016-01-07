package com.ubankers.app.base.api;

/**
 *
 */
public class Result<T> {
    private String errorCode;
    private T info;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }
}
