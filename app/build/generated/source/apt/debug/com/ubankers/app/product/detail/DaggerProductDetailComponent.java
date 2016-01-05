package com.ubankers.app.product.detail;

import com.ubankers.app.base.AppComponent;
import com.ubankers.app.base.session.Session;
import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.MembersInjectors;
import dagger.internal.ScopedProvider;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class DaggerProductDetailComponent implements ProductDetailComponent {
  private Provider<ProductDetailPresenter> providesPresenterProvider;
  private Provider<ProductDetailView> providesViewProvider;
  private Provider<Session> sessionProvider;
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
    this.sessionProvider = new Factory<Session>() {
      @Override public Session get() {
        Session provided = builder.appComponent.session();
        if (provided == null) {
          throw new NullPointerException("Cannot return null from a non-@Nullable component method");
        }
        return provided;
      }
    };
    this.productDetailActivityMembersInjector = ProductDetailActivity_MembersInjector.create((MembersInjector) MembersInjectors.noOp(), providesPresenterProvider, providesViewProvider, sessionProvider);
  }

  @Override
  public void inject(ProductDetailActivity activity) {  
    productDetailActivityMembersInjector.injectMembers(activity);
  }

  public static final class Builder {
    private ProductDetailModule productDetailModule;
    private AppComponent appComponent;
  
    private Builder() {  
    }
  
    public ProductDetailComponent build() {  
      if (productDetailModule == null) {
        throw new IllegalStateException("productDetailModule must be set");
      }
      if (appComponent == null) {
        throw new IllegalStateException("appComponent must be set");
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
  
    public Builder appComponent(AppComponent appComponent) {  
      if (appComponent == null) {
        throw new NullPointerException("appComponent");
      }
      this.appComponent = appComponent;
      return this;
    }
  }
}

