package com.ubankers.app.product.detail;


import com.ubankers.app.base.dagger.ActivityScope;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ProductDetailModule {

    private ProductDetailActivity activity;

    public ProductDetailModule(ProductDetailActivity activity){
        this.activity = activity;
    }

    @ActivityScope
    @Provides
    ProductDetailView providesView(){
        return new ProductDetailView(activity);
    }

    @ActivityScope
    @Provides
    ProductDetailPresenter providesPresenter(){
        return new ProductDetailPresenter();
    }
}
