package com.ubankers.app.product.detail;

import com.ubankers.app.base.dagger.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(modules = ProductDetailModule.class)
public interface ProductDetailComponent {
    void inject(ProductDetailActivity activity);
}
