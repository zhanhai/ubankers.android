package com.ubankers.app.base;

import com.ubankers.app.base.session.Session;
import com.ubankers.app.member.MemberModule;
import com.ubankers.app.member.model.MemberAPI;
import com.ubankers.app.product.ProductModule;
import com.ubankers.app.product.model.ProductAPI;

import javax.inject.Singleton;

import dagger.Component;
import retrofit.Retrofit;

/**
 *
 */
@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, ProductModule.class, MemberModule.class})
public interface AppComponent {

    Session session();


    ProductAPI productAPI();
    MemberAPI memberAPI();
}
