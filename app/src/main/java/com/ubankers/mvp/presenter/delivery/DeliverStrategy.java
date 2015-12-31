package com.ubankers.mvp.presenter.delivery;

import com.ubankers.mvp.view.View;

import rx.Observable;

/**
 *
 */
public interface DeliverStrategy<V extends View, T> extends Observable.Transformer<T, Delivery<V, T>>{
}
