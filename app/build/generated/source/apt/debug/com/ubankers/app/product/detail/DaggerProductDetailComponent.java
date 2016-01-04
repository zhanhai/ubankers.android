package com.ubankers.app.product.detail;

import dagger.MembersInjector;
import dagger.internal.MembersInjectors;
import dagger.internal.ScopedProvider;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class DaggerProductDetailComponent implements ProductDetailComponent {
  private Provider<ProductDetailPresenter> providesPresenterProvider;
  private Provider<ProductDetailView> providesViewProvider;
  private MembersInjector<ProductDetailActivity> productDetailActivityMembersInjector;

  private DaggerProductDetailComponent(Builder builder) {  
    assert builder != null;
    initialize(builder);
  }

  public static Builder builder() {  
    return new Builder();
  }

  private void initialize(final Builder builder) {  
    this.providesPresenterProvider = ScopedProvider.create(ProductDetailModule_ProvidesPresenterFactory.create(builder.productDetailModule));
    this.providesViewProvider = ScopedProvider.create(ProductDetailModule_ProvidesViewFactory.create(builder.productDetailModule));
    this.productDetailActivityMembersInjector = ProductDetailActivity_MembersInjector.create((MembersInjector) MembersInjectors.noOp(), providesPresenterProvider, providesViewProvider);
  }

  @Override
  public void inject(ProductDetailActivity activity) {  
    productDetailActivityMembersInjector.injectMembers(activity);
  }

  public static final class Builder {
    private ProductDetailModule productDetailModule;
  
    private Builder() {  
    }
  
    public ProductDetailComponent build() {  
      if (productDetailModule == null) {
        throw new IllegalStateException("productDetailModule must be set");
      }
      return new DaggerProductDetailComponent(this);
    }
  
    public Builder productDetailModule(ProductDetailModule productDetailModule) {  
      if (productDetailModule == null) {
        throw new NullPointerException("productDetailModule");
      }
      this.productDetailModule = productDetailModule;
      return this;
    }
  }
}

