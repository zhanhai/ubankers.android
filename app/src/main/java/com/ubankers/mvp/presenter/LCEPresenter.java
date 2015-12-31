package com.ubankers.mvp.presenter;


import android.support.annotation.CallSuper;

import com.ubankers.mvp.view.LCEView;

import java.lang.ref.WeakReference;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subjects.BehaviorSubject;

public abstract class LCEPresenter<T, V extends LCEView<T>> extends Presenter<V>{

    private enum LCEViewState{
       LOAD, SHOW, ERROR, AUTHENTICATION;
    }

    private class LCEResult<T> {
        private final T content;
        private final Throwable error;

        public LCEResult(final T  content){
            this.content = content;
            this.error = null;
        }

        public LCEResult(final Throwable error){
            this.content = null;
            this.error = error;
        }
    }


    protected  class StateAwareView {
        private final WeakReference<V> view;
        private final LCEViewState state;
        private final LCEResult<T> result;

        private StateAwareView(final WeakReference<V> view, final LCEViewState state, final LCEResult<T> result){
            this.view = view;
            this.state = state;
            this.result = result;
        }
    }

    private final BehaviorSubject<WeakReference<V>> views = BehaviorSubject.create();

    private final BehaviorSubject<LCEViewState> states = BehaviorSubject.create();

    private final BehaviorSubject<LCEResult<T>> results = BehaviorSubject.create();

    private StateAwareView snapshot;

    public LCEPresenter(){
        watchChanges();
    }


    protected final void onLoading(){
        states.onNext(LCEViewState.LOAD);
    }

    protected final void onContentLoaded(final T content){
        results.onNext(new LCEResult<T>(content));
        states.onNext(LCEViewState.SHOW);
    }

    protected final void onErrorThrowed(final Throwable error){
        results.onNext(new LCEResult<T>(error));
        states.onNext(LCEViewState.ERROR);
    }



    protected final void renderView(){
        if (snapshot == null) {
            return;
        }

        WeakReference<V> view = snapshot.view;
        switch(snapshot.state){
            case LOAD:
                view.get().showLoading();
                break;
            case SHOW:
                view.get().showContent(snapshot.result.content);
                break;
            case ERROR:
                view.get().showError(snapshot.result.error);
                break;
            case AUTHENTICATION:
                view.get().showAuthenticaiton();
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    protected void onTakeView(V view) {
        views.onNext(new WeakReference<>(view));
    }

    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    protected void onDropView() {
        views.onNext(null);
    }

    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    protected void onReset() {
        views.onCompleted();
    }

    private  void watchChanges(){
        Observable<StateAwareView> stateChanges =
                Observable.combineLatest(
                        views, states,
                        new Func2<WeakReference<V>, LCEViewState, StateAwareView>() {
                            @Override
                            public StateAwareView call(WeakReference<V> view, LCEViewState state) {
                                return view == null ? null : new StateAwareView(view, state, null);
                            }
                        })
                        .filter(new Func1<StateAwareView, Boolean>() {
                                    @Override
                                    public Boolean call(StateAwareView stateAwareView) {
                                        return stateAwareView != null;
                                    }
                                }
                        );

        Observable<StateAwareView> combinedChanges =
                Observable.combineLatest(
                        stateChanges,
                        results.startWith((LCEResult<T>) null),
                        new Func2<StateAwareView, LCEResult<T>, StateAwareView>() {
                            @Override
                            public StateAwareView call(StateAwareView stateAwareView, LCEResult<T> tlceResult) {
                                return new StateAwareView(stateAwareView.view, stateAwareView.state, tlceResult);
                            }
                        }
                );


        combinedChanges.subscribe(new Action1<StateAwareView>() {
            @Override
            public void call(StateAwareView stateAwareView) {
                snapshot = stateAwareView;
                renderView();
            }
        });
    }
}
