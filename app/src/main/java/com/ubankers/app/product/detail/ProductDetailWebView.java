package com.ubankers.app.product.detail;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;

import com.ubankers.app.base.widget.BaseWebView;

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
            activity.loadArticle(url);
        } else {
            view.loadUrl(url);
        }

        return true;
    }

    @Override
    public void onPageFinished(String url){
        activity.showProductLayout();
    }
}
