package com.ubankers.mvp.presenter;

/**
 * MVP command used to render view
 */
public interface MvpCommand<V extends View> {
    void call(V view);
}
