package com.ubankers.app.product.detail;


import android.content.Context;

import com.ubankers.app.product.model.Product;
import com.ubankers.mvp.presenter.Presenter;
import com.ubankers.mvp.presenter.View;
import com.ubankers.mvp.presenter.ViewWithModel;

import cn.com.ubankers.www.sns.model.ArticleBean;
import cn.com.ubankers.www.widget.ProcessDialog;

public class ProductDetailView extends ViewWithModel<ProductDetailModel> {

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

            viewModel.setProduct(product);
        }
        else{
            hideLoading();
            activity.showError(error);
        }
    }

    void cfmpQualificationStatus(boolean isQualified){
        viewModel.isQualifiedCfmp(isQualified);
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

    @Override
    public void init() {
        viewModel = new ProductDetailModel();
        viewModel.init(activity.getIntent());
    }

    protected void isQualifiedCfmp(boolean isQualifiedCfmp){
        viewModel.isQualifiedCfmp(isQualifiedCfmp);
    }

    protected void setProduct(Product product){
        viewModel.setProduct(product);
    }

    public Product getProduct(){
        return viewModel.getProduct();
    }

    public boolean isQualifiedCfmp(){
        return viewModel.isQualifiedCfmp();
    }

    public String getProductId(){
        return viewModel.getProductId();
    }

    public String getReserverName(){
        return viewModel.getReserverName();
    }

    public Context getContext(){
        return activity;
    }
}
