package com.ubankers.mvp.view;


public interface LCEView<T> extends View{
    void showLoading();

    void showError(Throwable error);

    void showAuthenticaiton();

    void showContent (T content);
}
