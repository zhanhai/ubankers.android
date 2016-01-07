package com.ubankers.app.product;

import com.ubankers.app.product.model.ProductAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Retrofit;

/**
 *
 */
@Module
public class ProductModule {

    @Provides
    @Singleton
    ProductAPI provideProductAPI(Retrofit retrofit){
        return retrofit.create(ProductAPI.class);
    }
}
