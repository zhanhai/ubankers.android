package com.ubankers.app.product.detail;

import android.webkit.WebView;

import com.ubankers.app.base.widget.BaseWebView;

import cn.com.ubankers.www.http.HttpConfig;

/**
 *
 */
public class ProductDetailWebView extends BaseWebView{

    private final ProductDetailActivity activity;

    public ProductDetailWebView(ProductDetailActivity activity, WebView webView) {
        super(activity, webView);

        this.activity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(String url) {
        if (url.contains("sns/_escape_app/articleDetail/")) {
            ((ProductDetailPresenter)activity.getPresenter()).loadArticle(url.substring(url.lastIndexOf("/") + 1, url.length()));
        } else {
            webView.loadUrl(url);
        }

        return true;
    }

    @Override
    public void onPageFinished(String url){
        activity.getView().hideLoading();
        activity.showProductLayout();
    }

    public void showProduct(String productId){
        webView.loadUrl(HttpConfig.URL_INFROMATION + productId);
    }
}
