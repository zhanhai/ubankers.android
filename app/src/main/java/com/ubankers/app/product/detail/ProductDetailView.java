package com.ubankers.app.product.detail;


import com.ubankers.app.product.model.Product;
import com.ubankers.mvp.presenter.View;

import cn.com.ubankers.www.sns.model.ArticleBean;
import cn.com.ubankers.www.widget.ProcessDialog;

public class ProductDetailView implements View {

    private ProductDetailActivity activity;
    private ProcessDialog progressDialog;

    public ProductDetailView(ProductDetailActivity activity){
        this.activity = activity;
        progressDialog = ProcessDialog.createDialog(activity, "正在加载中...");
    }

    void showProduct(Throwable error, Product product) {
        if(error == null) {
            activity.showProduct(product);
            activity.showReservation(product);
            activity.setProduct(product);
        }
        else{
            hideLoading();
            activity.showError(error);
        }
    }

    void cfmpQualificationStatus(boolean isQualified){
        activity.isQualifiedCfmp(isQualified);
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
