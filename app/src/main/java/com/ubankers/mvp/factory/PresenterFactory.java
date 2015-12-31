package com.ubankers.mvp.factory;

import com.ubankers.mvp.presenter.Presenter;

public interface PresenterFactory<P extends Presenter> {
    P createPresenter();

    String idOfPresenter();
}
