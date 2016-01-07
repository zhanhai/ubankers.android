package com.ubankers.app.product.detail;


import com.ubankers.app.base.dagger.ActivityScope;
import com.ubankers.app.base.session.Session;
import com.ubankers.app.member.model.MemberAPI;
import com.ubankers.app.product.model.ProductAPI;

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
    ProductDetailPresenter providesPresenter(ProductAPI productAPI, MemberAPI memberAPI, Session session){
        ProductDetailPresenter presenter = new ProductDetailPresenter();
        presenter.memberAPI = memberAPI;
        presenter.productAPI = productAPI;
        presenter.session = session;

        return presenter;
    }
}
