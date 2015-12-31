package com.ubankers.mvp.presenter.operation;

import android.support.annotation.Nullable;

import com.ubankers.mvp.presenter.delivery.DeliverFirst;
import com.ubankers.mvp.presenter.delivery.DeliverLatestCache;
import com.ubankers.mvp.presenter.delivery.DeliverReplay;
import com.ubankers.mvp.presenter.delivery.DeliverStrategy;
import com.ubankers.mvp.presenter.delivery.Delivery;
import com.ubankers.mvp.view.View;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;

/**
 *
 */
public class OperationFactory<V extends View> {
    private Observable<V> views;

    public OperationFactory(Observable<V> views){
        this.views = views;
    }


    /**
     * This is a shortcut that can be used instead of combining together
     * {@link #deliverFirst()},
     * {@link #generateDispatcher(Action2, Action2)}.
     *
     * @param operationType     an id of the restartable.
     * @param observableFactory a factory that should return an Observable when the restartable should run.
     * @param onNext            a callback that will be called when received data should be delivered to view.
     * @param onError           a callback that will be called if the source observable emits onError.
     * @param <T>               the type of the observable.
     */
    public final <T> Operation<V, T> operationWithFirstDelivery(int operationType, final Func0<Observable<T>> observableFactory,
                                                        final Action2<V, T> onNext, @Nullable final Action2<V, Throwable> onError) {

        return new Operation(operationType, observableFactory, this.<T>deliverFirst(), generateDispatcher(onNext, onError));
    }

    /**
     * This is a shortcut for calling {@link #operationWithFirstDelivery(int, Func0, Action2, Action2)} with the last parameter = null.
     */
    public final <T> Operation<V, T> operationWithFirstDelivery(int operationType, final Func0<Observable<T>> observableFactory, final Action2<V, T> onNext) {
        return operationWithFirstDelivery(operationType, observableFactory, onNext, null);
    }

    /**
     * This is a shortcut that can be used instead of combining together
     * {@link #deliverLatestCache()},
     * {@link #generateDispatcher(Action2, Action2)}.
     *
     * @param operationType     an id of the restartable.
     * @param observableFactory a factory that should return an Observable when the restartable should run.
     * @param onNext            a callback that will be called when received data should be delivered to view.
     * @param onError           a callback that will be called if the source observable emits onError.
     * @param <T>               the type of the observable.
     */
    public final <T> Operation<V, T> operationWithLatestCacheDelivery(int operationType, final Func0<Observable<T>> observableFactory,
                                                              final Action2<V, T> onNext, @Nullable final Action2<V, Throwable> onError) {

        return new Operation(operationType, observableFactory, this.<T>deliverLatestCache(),
                generateDispatcher(onNext, onError));
    }

    /**
     * This is a shortcut for calling {@link #operationWithLatestCacheDelivery(int, Func0, Action2, Action2)} with the last parameter = null.
     */
    public final <T> Operation<V, T> operationWithLatestCacheDelivery(int operationType, final Func0<Observable<T>> observableFactory, final Action2<V, T> onNext) {
        return operationWithLatestCacheDelivery(operationType, observableFactory, onNext, null);
    }

    /**
     * This is a shortcut that can be used instead of combining together
     * {@link #deliverReplay()},
     * {@link #generateDispatcher(Action2, Action2)}.
     *
     * @param operationType     an id of the restartable.
     * @param observableFactory a factory that should return an Observable when the restartable should run.
     * @param onNext            a callback that will be called when received data should be delivered to view.
     * @param onError           a callback that will be called if the source observable emits onError.
     * @param <T>               the type of the observable.
     */
    public final <T> Operation<V, T> operationWithReplayDelivery(int operationType, final Func0<Observable<T>> observableFactory,
                                                         final Action2<V, T> onNext, @Nullable final Action2<V, Throwable> onError) {

        return new Operation(operationType, observableFactory, this.<T>deliverReplay(),generateDispatcher(onNext, onError));
    }

    /**
     * This is a shortcut for calling {@link #operationWithReplayDelivery(int, Func0, Action2, Action2)} with the last parameter = null.
     */
    public final <T> Operation<V, T> addOperationWithReplayDelivery(int operationType, final Func0<Observable<T>> observableFactory, final Action2<V, T> onNext) {
        return operationWithReplayDelivery(operationType, observableFactory, onNext, null);
    }

    /**
     * Returns an {@link rx.Observable.Transformer} that couples views with data that has been emitted by
     * the source {@link rx.Observable}.
     *
     * {@link #deliverLatestCache} keeps the latest onNext value and emits it each time a new view gets attached.
     * If a new onNext value appears while a view is attached, it will be delivered immediately.
     *
     * @param <T> the type of source observable emissions
     */
    protected final <T> DeliverStrategy<V, T> deliverLatestCache() {
        return new DeliverLatestCache<>(views);
    }

    /**
     * Returns an {@link rx.Observable.Transformer} that couples views with data that has been emitted by
     * the source {@link rx.Observable}.
     *
     * {@link #deliverFirst} delivers only the first onNext value that has been emitted by the source observable.
     *
     * @param <T> the type of source observable emissions
     */
     <T> DeliverStrategy<V, T> deliverFirst() {
        return new DeliverFirst<>(views);
    }

    /**
     * Returns an {@link rx.Observable.Transformer} that couples views with data that has been emitted by
     * the source {@link rx.Observable}.
     *
     * {@link #deliverReplay} keeps all onNext values and emits them each time a new view gets attached.
     * If a new onNext value appears while a view is attached, it will be delivered immediately.
     *
     * @param <T> the type of source observable emissions
     */
    protected final <T> DeliverStrategy<V, T> deliverReplay() {
        return new DeliverReplay<>(views);
    }

    /**
     * Returns a method that can be used for manual restartable chain build. It returns an Action1 that splits
     * a received {@link Delivery} into two {@link Action2} onNext and onError calls.
     *
     * @param onNext  a method that will be called if the delivery contains an emitted onNext value.
     * @param onError a method that will be called if the delivery contains an onError throwable.
     * @param <T>     a type on onNext value.
     * @return an Action1 that splits a received {@link Delivery} into two {@link Action2} onNext and onError calls.
     */
    <T> Action1<Delivery<V, T>> generateDispatcher(final Action2<V, T> onNext, @Nullable final Action2<V, Throwable> onError) {
        return new Action1<Delivery<V, T>>() {
            @Override
            public void call(Delivery<V, T> delivery) {
                delivery.split(onNext, onError);
            }
        };
    }

    /**
     * This is a shortcut for calling {@link #generateDispatcher(Action2, Action2)} when the second parameter is null.
     */
    protected  <T> Action1<Delivery<V, T>> generateDispatcher(Action2<V, T> onNext) {
        return generateDispatcher(onNext, null);
    }
}
