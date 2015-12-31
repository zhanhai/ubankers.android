package com.ubankers.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ubankers.mvp.factory.PresenterFactory;
import com.ubankers.mvp.factory.PresenterStorage;
import com.ubankers.mvp.presenter.Presenter;

/**
 * This class adopts a View lifecycle to the Presenter`s lifecycle.
 *
 * @param <P> a type of the presenter.
 */
public final class PresenterLifecycleDelegate<V extends View, P extends Presenter<V>> {

    private static final String PRESENTER_KEY = "presenter";

    @Nullable
    private P presenter;

    public PresenterLifecycleDelegate(@Nullable PresenterFactory<P> presenterFactory, Bundle presenterState) {

        // Try to load presenter from global registry(i.e: {@link PresenterStorage})
        presenter = PresenterStorage.INSTANCE.getPresenter(presenterFactory.idOfPresenter());

        // If no presenter found, either it has not been created yet or it was reclaimed
        // Anyway, we need to create a new one.
        if(presenter == null) {
            presenter = presenterFactory.createPresenter();
            PresenterStorage.INSTANCE.add(presenterFactory.idOfPresenter(), presenter);
        }

        // Ask presenter to start from the specified state
        presenter.start(presenterState);
    }


    /**
     * {@link android.app.Activity#onSaveInstanceState(Bundle)}, {@link android.app.Fragment#onSaveInstanceState(Bundle)}, {@link android.view.View#onSaveInstanceState()}.
     */
    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();

        if (presenter != null) {
            Bundle presenterBundle = new Bundle();
            presenter.save(presenterBundle);
            bundle.putBundle(PRESENTER_KEY, presenterBundle);
        }

        return bundle;
    }


    /**
     * {@link android.app.Activity#onResume()}, {@link android.app.Fragment#onResume()}, {@link android.view.View#onAttachedToWindow()}
     */
    public void onResume(V view) {
        if (presenter == null){
            return;
        }

        presenter.takeView(view);
    }

    /**
     * {@link android.app.Activity#onPause()}, {@link android.app.Fragment#onPause()}, {@link android.view.View#onDetachedFromWindow()}
     */
    public void onPause(boolean willViewBeFinished) {
        if (presenter == null) {
            return;
        }

        presenter.dropView();

        // If view will be finished, reset the presenter.
        if (willViewBeFinished) {
            presenter.reset();
            presenter = null;
        }
    }

}
