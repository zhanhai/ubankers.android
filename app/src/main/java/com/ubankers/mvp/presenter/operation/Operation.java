package com.ubankers.mvp.presenter.operation;

import com.ubankers.mvp.presenter.delivery.DeliverStrategy;
import com.ubankers.mvp.presenter.delivery.Delivery;
import com.ubankers.mvp.view.View;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func0;

/**
 * Reactive operation which can be observed.
 */
public class Operation<V extends View, T> implements RxOperation{
    private final int type;
    private final Func0<Observable<T>> operator;
    private final DeliverStrategy<V, T> deliverStrategy;
    private final Action1<Delivery<V, T>> subscriber;


    public Operation(final int type, final Func0<Observable<T>> operator,
                     final DeliverStrategy<V, T> deliverStrategy,
                     final Action1<Delivery<V, T>> subscriber){
        this.type = type;
        this.operator = operator;
        this.deliverStrategy = deliverStrategy;
        this.subscriber = subscriber;
    }


    @Override
    public Subscription call(){
        return operator.call()
                .compose(deliverStrategy)
                .subscribe(subscriber);
    }

    @Override
    public int getType() {
        return type;
    }
}
