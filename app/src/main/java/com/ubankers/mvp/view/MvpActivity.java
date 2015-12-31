package com.ubankers.mvp.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.ubankers.mvp.factory.PresenterFactory;
import com.ubankers.mvp.factory.ReflectionPresenterFactory;
import com.ubankers.mvp.presenter.Presenter;
/**
 * This class is an example of how an activity could controls it's presenter.
 * You can inherit from this class or copy/paste this class's code to
 * create your own view implementation.
 *
 * @param <V> view
 * @param <P> presenter .
 */
public abstract class MvpActivity<V extends View, P extends Presenter<V>> extends Activity {

    private PresenterFactory<P> presenterFactory = ReflectionPresenterFactory.<P>fromViewClass(getClass());
    private PresenterLifecycleDelegate<V, P> presenterDelegate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle presenterState = new Bundle();
        if (savedInstanceState != null)
            presenterState.putBundle(View.PRESENTER_STATE_KEY, savedInstanceState.getBundle(View.PRESENTER_STATE_KEY));
        presenterState.putBundle(View.VIEW_INTENT_KEY, parseIntent());

        presenterDelegate  = new PresenterLifecycleDelegate<>(presenterFactory, presenterState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(View.PRESENTER_STATE_KEY, presenterDelegate.onSaveInstanceState());
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenterDelegate.onResume(getView(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenterDelegate.onPause(isFinishing());
    }

    /**
     * Parse the activity intent and then set the values into a bundle
     * @return
     */
    protected abstract Bundle parseIntent();

    /**
     *
     * @param activity
     * @return
     */
    protected abstract V getView(MvpActivity activity);
}
