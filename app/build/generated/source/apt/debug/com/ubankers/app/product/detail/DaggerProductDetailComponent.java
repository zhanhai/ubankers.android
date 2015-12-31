package com.ubankers.app.product.detail;

import com.ubankers.app.product.ProductModule;

import dagger.internal.MembersInjectors;
import dagger.internal.ScopedProvider;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class DaggerProductDetailComponent implements ProductDetailComponent {
  private Provider<ProductDetailPresenter> productDetailPresenterProvider;

  private DaggerProductDetailComponent(Builder builder) {  
    assert builder != null;
    initialize(builder);
  }

  public static Builder builder() {  
    return new Builder();
  }

  public static ProductDetailComponent create() {  
    return builder().build();
  }

  private void initialize(final Builder builder) {  
    this.productDetailPresenterProvider = ScopedProvider.create(ProductDetailPresenter_Factory.create());
  }

  @Override
  public void inject(ProductDetailView view) {  
    MembersInjectors.noOp().injectMembers(view);
  }

  @Override
  public ProductDetailPresenter presenter() {  
    return productDetailPresenterProvider.get();
  }

  public static final class Builder {
    private ProductModule productModule;
  
    private Builder() {  
    }
  
    public ProductDetailComponent build() {  
      if (productModule == null) {
        this.productModule = new ProductModule();
      }
      return new DaggerProductDetailComponent(this);
    }
  
    public Builder productModule(ProductModule productModule) {
      if (productModule == null) {
        throw new NullPointerException("productModule");
      }
      this.productModule = productModule;
      return this;
    }
  }
}

