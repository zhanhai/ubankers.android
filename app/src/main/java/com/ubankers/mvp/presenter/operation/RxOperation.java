package com.ubankers.mvp.presenter.operation;

import rx.Subscription;

/**
 *
 */
public interface RxOperation {
    int getType();

    Subscription call();
}
