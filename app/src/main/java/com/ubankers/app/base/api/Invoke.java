package com.ubankers.app.base.api;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class Invoke<D> {
    private Observable<Response<D>> api;

    private Invoke(Observable<Response<D>> api){
        this.api = api;
    }

    public void call(Consumer<D> consumer){
        api.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

    public static <D> Invoke api(Observable<Response<D>> api){
        return new Invoke(api);
    }
}
