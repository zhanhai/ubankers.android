package com.ubankers.app.product.detail;

import com.ubankers.mvp.view.MvpActivity;
import dagger.MembersInjector;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class ProductDetailActivity_MembersInjector implements MembersInjector<ProductDetailActivity> {
  private final MembersInjector<MvpActivity<ProductDetailView>> supertypeInjector;
  private final Provider<ProductDetailPresenter> presenterProvider;
  private final Provider<ProductDetailView> viewProvider;

  public ProductDetailActivity_MembersInjector(MembersInjector<MvpActivity<ProductDetailView>> supertypeInjector, Provider<ProductDetailPresenter> presenterProvider, Provider<ProductDetailView> viewProvider) {  
    assert supertypeInjector != null;
    this.supertypeInjector = supertypeInjector;
    assert presenterProvider != null;
    this.presenterProvider = presenterProvider;
    assert viewProvider != null;
    this.viewProvider = viewProvider;
  }

  @Override
  public void injectMembers(ProductDetailActivity instance) {  
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    supertypeInjector.injectMembers(instance);
    instance.presenter = presenterProvider.get();
    instance.view = viewProvider.get();
  }

  public static MembersInjector<ProductDetailActivity> create(MembersInjector<MvpActivity<ProductDetailView>> supertypeInjector, Provider<ProductDetailPresenter> presenterProvider, Provider<ProductDetailView> viewProvider) {  
      return new ProductDetailActivity_MembersInjector(supertypeInjector, presenterProvider, viewProvider);
  }
}

