package com.ubankers.app.product.model;

import com.ubankers.app.base.api.Response;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import rx.Observable;

/**
 *
 */
public interface ProductAPI {

    @GET("/product/ajax/rd/product/common/get/{productId}")
    @Headers("Cache-Control: no-cache")
    Observable<Response<Product>> getProduct(@Path("productId") String productId);
}
