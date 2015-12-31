package com.ubankers.mvp.view;

import android.app.Activity;

import com.ubankers.mvp.presenter.Presenter;
import com.ubankers.mvp.presenter.View;

/**
 * Base class of activity support mvp pattern.
 *
 * @param <V>
 */
public abstract class MvpActivity<V extends View> extends Activity{

    protected Presenter<V> presenter;


    protected final void setPresenter(Presenter<V> presenter){
        this.presenter = presenter;
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.takeView(getView());
    }

    @Override
    protected void onPause() {
        super.onPause();

        presenter.dropView();
    }

    protected abstract V getView();
}
