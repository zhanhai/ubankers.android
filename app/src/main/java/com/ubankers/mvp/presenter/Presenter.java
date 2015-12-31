package com.ubankers.mvp.presenter;

import android.app.Activity;
import android.app.Fragment;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subjects.BehaviorSubject;

/**
 * This is a base class for all presenters. Subclasses can override
 *
 * View is normally available between
 * {@link Activity#onResume()} and {@link Activity#onPause()},
 * {@link Fragment#onResume()} and {@link Fragment#onPause()},
 * {@link android.view.View#onAttachedToWindow()} and {@link android.view.View#onDetachedFromWindow()}.
 *
 * @param <V> a type of view bound with this presenter.
 */
public  class Presenter<V extends View> {

    protected BehaviorSubject<V> views = BehaviorSubject.create();

    /**
     * This method is being called when a view gets attached to it.
     * Normally this happens during {@link Activity#onResume()}, {@link Fragment#onResume()}
     * and {@link android.view.View#onAttachedToWindow()}.
     *
     * This method is intended for overriding.
     *
     * @param view a view that should be taken
     */
    public final void takeView(V view)
    {
        views.onNext(view);
    }

    /**
     * This method is being called when a view gets detached from the presenter.
     * Normally this happens during {@link Activity#onPause()} ()}, {@link Fragment#onPause()} ()}
     * and {@link android.view.View#onDetachedFromWindow()}.
     *
     * This method is intended for overriding.
     */
    public final void dropView() {
        views.onNext(null);
    }


    private class RenderCommand {
        final MvpCommand<V> command;
        final V view;

        public RenderCommand(final MvpCommand<V> command, V view){
            this.command = command;
            this.view = view;
        }

        public void render(){
            command.call(view);
        }
    }

    protected final void render(MvpCommand<V> command){
        Observable.combineLatest(
                views,
                Observable.just(command),
                new Func2<V, MvpCommand<V>, RenderCommand>() {
                    public RenderCommand call(V view, MvpCommand<V> command) {
                        if(view == null){
                            return null;
                        }

                        return new RenderCommand(command, view);
                    }
                })
                .filter(new Func1<RenderCommand, Boolean>(){
                    public Boolean call(RenderCommand renderCommand) {
                        return renderCommand != null;
                    }
                })
        .subscribe(
                new Action1<RenderCommand>(){
                    public void call(RenderCommand renderCommand) {
                        renderCommand.render();
                    }
                }
        );
    }




}
