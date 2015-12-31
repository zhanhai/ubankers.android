package com.ubankers.app.product.detail;


import com.ubankers.app.product.model.Product;

import java.lang.ref.WeakReference;

import cn.com.ubankers.www.sns.model.ArticleBean;

public class ProductDetailView implements LCEView<Product> {

    private WeakReference<ProductDetailActivity> activity;

    public ProductDetailView(ProductDetailActivity activity){
        this.activity = new WeakReference<ProductDetailActivity>(activity);
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void showError(Throwable error) {

    }


    @Override
    public void showAuthenticaiton() {

    }

    @Override
    public void showContent(Product content) {

    }

    public void showArticle(ArticleBean articleBean){

    }
}
