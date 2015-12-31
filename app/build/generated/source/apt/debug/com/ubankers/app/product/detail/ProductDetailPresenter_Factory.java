package com.ubankers.app.product.detail;

import dagger.internal.Factory;
import javax.annotation.Generated;

@Generated("dagger.internal.codegen.ComponentProcessor")
public enum ProductDetailPresenter_Factory implements Factory<ProductDetailPresenter> {
INSTANCE;

  @Override
  public ProductDetailPresenter get() {  
    return new ProductDetailPresenter();
  }

  public static Factory<ProductDetailPresenter> create() {  
    return INSTANCE;
  }
}

