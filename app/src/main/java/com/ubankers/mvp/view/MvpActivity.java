package com.ubankers.mvp.view;

import android.app.Activity;

import com.ubankers.mvp.presenter.Presenter;
import com.ubankers.mvp.presenter.View;

/**
 * Base class of activity support mvp pattern.
 *
 * @param <V>
 */
public abstract class MvpActivity<V extends View, P extends Presenter<V>> extends Activity{

    @Override
    protected void onResume() {
        super.onResume();

        getPresenter().takeView(getView());
    }

    @Override
    protected void onPause() {
        super.onPause();

        getPresenter().dropView();
    }

    protected abstract P getPresenter();
    protected abstract V getView();
}
