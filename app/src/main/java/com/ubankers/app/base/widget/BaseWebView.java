package com.ubankers.app.base.widget;

import android.content.Context;
import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.utils.Tools;

/**
 *
 */
public abstract class BaseWebView {
    protected final WebView webView;

    public BaseWebView(Context context, WebView webView){
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);

        this.webView = webView;

        //设置cookie
        Tools.synCookies(context.getApplicationContext(), HttpConfig.HTTP_QUERY_URL);

        this.webView.setWebViewClient(new BaseWebViewClient(this));
    }


    public abstract boolean shouldOverrideUrlLoading(String url);

    public abstract void onPageFinished(String url);
}

class BaseWebViewClient extends WebViewClient {
    private final BaseWebView view;

    public BaseWebViewClient(BaseWebView view){
        this.view = view;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return this.view.shouldOverrideUrlLoading(url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        this.view.onPageFinished(url);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();// 接受ssl证书
        super.onReceivedSslError(view, handler, error);
    }
}