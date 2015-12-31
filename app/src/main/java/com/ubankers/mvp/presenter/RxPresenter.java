package com.ubankers.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import com.ubankers.mvp.presenter.delivery.DeliverFirst;
import com.ubankers.mvp.presenter.delivery.DeliverLatestCache;
import com.ubankers.mvp.presenter.delivery.DeliverReplay;
import com.ubankers.mvp.presenter.delivery.Delivery;
import com.ubankers.mvp.presenter.operation.Operation;
import com.ubankers.mvp.presenter.operation.OperationFactory;
import com.ubankers.mvp.presenter.operation.RxOperation;
import com.ubankers.mvp.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.internal.util.SubscriptionList;
import rx.subjects.BehaviorSubject;

/**
 * This is an extension of {@link Presenter} which provides RxJava functionality.
 *
 * @param <V> a type of view.
 */
public abstract class RxPresenter<V extends View> extends Presenter<V> {

    private final BehaviorSubject<V> views = BehaviorSubject.create();
    private final SubscriptionList subscriptions = new SubscriptionList();
    private final OperationFactory<V> operationFactory = new OperationFactory<>(views);

    private final HashMap<Integer, RxOperation> operationRegistry = new HashMap<>();
    private final HashMap<Integer, Subscription> operationSubscriptions = new HashMap<>();

    /**
     * Returns an {@link rx.Observable} that emits the current attached view or null.
     * See {@link BehaviorSubject} for more information.
     *
     * @return an observable that emits the current attached view or null.
     */
    public Observable<V> view() {
        return views;
    }

    public OperationFactory<V> factory(){
        return operationFactory;
    }

    /**
     * Registers a subscription to automatically unsubscribe it during onReset.
     * See {@link SubscriptionList#add(Subscription) for details.}
     *
     * @param subscription a subscription to add.
     */
    public void add(Subscription subscription) {
        subscriptions.add(subscription);
    }

    /**
     * Removes and unsubscribes a subscription that has been registered with {@link #add} previously.
     * See {@link SubscriptionList#remove(Subscription)} for details.
     *
     * @param subscription a subscription to remove.
     */
    public void remove(Subscription subscription) {
        subscriptions.remove(subscription);
    }

    /**
     * A restartable is any RxJava observable that can be started (subscribed) and
     * should be automatically restarted (re-subscribed) after a process restart if
     * it was still subscribed at the moment of saving presenter's state.
     *
     * Registers a factory. Re-subscribes the restartable after the process restart.
     *
     * @param operation id of the restartable
     */
    public void addOperation(RxOperation operation) {
        operationRegistry.put(operation.getType(), operation);
    }

    /**
     * Starts the given restartable.
     *
     * @param operationType id of the restartable
     */
    public void execute(int operationType) {
        cancel(operationType);
        operationSubscriptions.put(operationType, operationRegistry.get(operationType).call());
    }

    /**
     * Unsubscribes a restartable
     *
     * @param operationType id of a restartable.
     */
    public void cancel(int operationType) {
        Subscription subscription = operationSubscriptions.get(operationType);
        if (subscription != null)
            subscription.unsubscribe();
    }




    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    protected void onReset() {
        views.onCompleted();
        subscriptions.unsubscribe();
        for (Map.Entry<Integer, Subscription> entry : operationSubscriptions.entrySet())
            entry.getValue().unsubscribe();
    }




}
