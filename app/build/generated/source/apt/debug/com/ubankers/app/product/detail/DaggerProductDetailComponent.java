package com.ubankers.app.product.detail;

import com.ubankers.app.base.AppComponent;
import com.ubankers.app.base.session.Session;
import com.ubankers.app.member.model.MemberAPI;
import com.ubankers.app.product.model.ProductAPI;
import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.MembersInjectors;
import dagger.internal.ScopedProvider;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class DaggerProductDetailComponent implements ProductDetailComponent {
  private Provider<ProductAPI> productAPIProvider;
  private Provider<MemberAPI> memberAPIProvider;
  private Provider<Session> sessionProvider;
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
    this.productAPIProvider = new Factory<ProductAPI>() {
      @Override public ProductAPI get() {
        ProductAPI provided = builder.appComponent.productAPI();
        if (provided == null) {
          throw new NullPointerException("Cannot return null from a non-@Nullable component method");
        }
        return provided;
      }
    };
    this.memberAPIProvider = new Factory<MemberAPI>() {
      @Override public MemberAPI get() {
        MemberAPI provided = builder.appComponent.memberAPI();
        if (provided == null) {
          throw new NullPointerException("Cannot return null from a non-@Nullable component method");
        }
        return provided;
      }
    };
    this.sessionProvider = new Factory<Session>() {
      @Override public Session get() {
        Session provided = builder.appComponent.session();
        if (provided == null) {
          throw new NullPointerException("Cannot return null from a non-@Nullable component method");
        }
        return provided;
      }
    };
    this.providesPresenterProvider = ScopedProvider.create(ProductDetailModule_ProvidesPresenterFactory.create(builder.productDetailModule, productAPIProvider, memberAPIProvider, sessionProvider));
    this.providesViewProvider = ScopedProvider.create(ProductDetailModule_ProvidesViewFactory.create(builder.productDetailModule));
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

