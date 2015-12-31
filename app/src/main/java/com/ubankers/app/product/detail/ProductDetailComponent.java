package com.ubankers.app.product.detail;

import com.ubankers.app.product.ProductModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton @Component(modules = ProductModule.class)
public interface ProductDetailComponent {
    public void inject(ProductDetailView view);

    public ProductDetailPresenter presenter();
}
