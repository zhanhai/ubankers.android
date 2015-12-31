package com.ubankers.mvp.presenter;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ubankers.mvp.view.View;

/**
 * This is a base class for all presenters. Subclasses can override
 * {@link #onStart} , {@link #onReset}, {@link #onSave},
 * {@link #onTakeView}, {@link #onDropView}.
 * <p/>
 *
 * View is normally available between
 * {@link Activity#onResume()} and {@link Activity#onPause()},
 * {@link Fragment#onResume()} and {@link Fragment#onPause()},
 * {@link android.view.View#onAttachedToWindow()} and {@link android.view.View#onDetachedFromWindow()}.
 *
 * @param <V> a type of view bound with this presenter.
 */
public abstract class Presenter<V extends View> {

    /**
     * start the presenter.
     */
    public final void start(Bundle presenterState) { onStart(presenterState);}

    /**
     * Reset the presenter to the initial state
     */
    public final void reset() {
        onReset();
    }

    /**
     * Saves the presenter.
     */
    public final void save(Bundle state) {
        onSave(state);
    }

    /**
     * Attaches a view to the presenter.
     *
     * @param view a view to attach.
     */
    public final void takeView(V view) {
        onTakeView(view);
    }

    /**
     * Detaches the presenter from a view.
     */
    public final void dropView() {
        onDropView();
    }

    /**
     * This method should be called when view was created.
     *
     * <p> View state of presenter will be set according to the passed in savedState. </p>
     *
     * This method is intended for overriding.
     *
     * @param savedState If the presenter is being re-instantiated after a process restart then this Bundle
     *                   contains the data it supplied in {@link #onSave}.
     */
    protected abstract void onStart(@Nullable Bundle savedState) ;

    /**
     * This method is being called when a user leaves view and so inner state will be reset to original state.
     *
     * This method is intended for overriding.
     */
    protected abstract void onReset();

    /**
     * A returned state is the state that will be passed to {@link #onStart(Bundle)} for a new presenter instance after a process restart.
     *
     * This method is intended for overriding.
     *
     * @param state a non-null bundle which should be used to put presenter's state into.
     */
    protected abstract void onSave(Bundle state) ;

    /**
     * This method is being called when a view gets attached to it.
     * Normally this happens during {@link Activity#onResume()}, {@link Fragment#onResume()}
     * and {@link android.view.View#onAttachedToWindow()}.
     *
     * This method is intended for overriding.
     *
     * @param view a view that should be taken
     */
    protected abstract void onTakeView(V view) ;

    /**
     * This method is being called when a view gets detached from the presenter.
     * Normally this happens during {@link Activity#onPause()} ()}, {@link Fragment#onPause()} ()}
     * and {@link android.view.View#onDetachedFromWindow()}.
     *
     * This method is intended for overriding.
     */
    protected abstract void onDropView() ;
}
