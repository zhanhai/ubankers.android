package com.ubankers.app.product.detail;

import android.app.Activity;
import dagger.MembersInjector;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class ProductDetailActivity_MembersInjector implements MembersInjector<ProductDetailActivity> {
  private final MembersInjector<Activity> supertypeInjector;
  private final Provider<ProductDetailPresenter> presenterProvider;

  public ProductDetailActivity_MembersInjector(MembersInjector<Activity> supertypeInjector, Provider<ProductDetailPresenter> presenterProvider) {  
    assert supertypeInjector != null;
    this.supertypeInjector = supertypeInjector;
    assert presenterProvider != null;
    this.presenterProvider = presenterProvider;
  }

  @Override
  public void injectMembers(ProductDetailActivity instance) {  
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    supertypeInjector.injectMembers(instance);
    instance.presenter = presenterProvider.get();
  }

  public static MembersInjector<ProductDetailActivity> create(MembersInjector<Activity> supertypeInjector, Provider<ProductDetailPresenter> presenterProvider) {  
      return new ProductDetailActivity_MembersInjector(supertypeInjector, presenterProvider);
  }
}

