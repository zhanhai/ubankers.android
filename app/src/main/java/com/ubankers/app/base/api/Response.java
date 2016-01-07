package com.ubankers.app.base.api;

/**
 *
 */
public class Response<T> {
    private boolean success;
    private Result<T> result;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Result<T> getResult() {
        return result;
    }

    public void setResult(Result<T> result) {
        this.result = result;
    }
}
