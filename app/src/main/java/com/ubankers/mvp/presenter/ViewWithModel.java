package com.ubankers.mvp.presenter;

/**
 *
 */
public abstract class ViewWithModel<M> implements View{
    protected M viewModel ;

    public abstract void init();
}
