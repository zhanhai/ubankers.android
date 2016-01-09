package com.ubankers.app.base.api;

import retrofit.HttpException;
import rx.Subscriber;

/**
 *
 */
public abstract class Consumer<D> extends Subscriber<Response<D>> {

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        int statusCode = -1;
        if (e instanceof HttpException) {
            statusCode = ((HttpException) e).code();
        }

        if (statusCode == 401) {
            onAuthenticationFailed();
        } else {
            onException(e);
        }
    }

    @Override
    public void onNext(Response<D> response) {
        if (response.isSuccess()) {
            onData(response.getResult().getInfo());
            return;
        }

        // Handle errors
        if (response.getResult().getErrorCode().equals("noLogin")) {
            onAuthenticationFailed();
        } else {
            onException(new Error("处理错误"));
        }
    }

    protected abstract void onAuthenticationFailed();
    protected abstract void onException(Throwable t);
    protected abstract void onData(D data);
}
