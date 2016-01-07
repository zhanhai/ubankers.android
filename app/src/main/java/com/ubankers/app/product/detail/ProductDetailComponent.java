package com.ubankers.app.product.detail;

import com.ubankers.app.base.AppComponent;
import com.ubankers.app.base.dagger.ActivityScope;
import com.ubankers.app.product.ProductModule;

import dagger.Component;

@ActivityScope
@Component(dependencies = {AppComponent.class}, modules = {ProductDetailModule.class})
public interface ProductDetailComponent {
    void inject(ProductDetailActivity activity);
}
