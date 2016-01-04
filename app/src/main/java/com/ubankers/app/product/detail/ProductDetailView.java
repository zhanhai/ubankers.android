package com.ubankers.app.product.detail;


import com.ubankers.mvp.presenter.View;

import cn.com.ubankers.www.product.model.ProductDetail;
import cn.com.ubankers.www.sns.model.ArticleBean;
import cn.com.ubankers.www.widget.ProcessDialog;

public class ProductDetailView implements View {

    private ProductDetailActivity activity;
    private ProcessDialog progressDialog;

    public ProductDetailView(ProductDetailActivity activity){
        this.activity = activity;
        progressDialog = ProcessDialog.createDialog(activity, "正在加载中...");
    }

    void showProductDetail(Throwable error, ProductDetail product) {
        hideLoading();
        if(error == null) {
            activity.showProductDetail(product);
            activity.showReservation(product);
        }
        else{
            activity.showError(error);
        }
    }

    void showArticle( Throwable error, ArticleBean article){
        hideLoading();

        if(error == null){
            activity.showArticle(article);
        }
        else{
            activity.showError(error);
        }
    }

    void showAuthentication(){
        activity.showAuthenticationRequired();
    }

    void showLoading() {
        progressDialog.show();
    }

    void hideLoading(){
        progressDialog.dismiss();
    }
}
