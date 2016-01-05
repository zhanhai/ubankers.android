package com.ubankers.app.base.widget;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.utils.Tools;

/**
 *
 */
public class WebViewInitializer {
    public void init(Context context, WebView webView, WebViewClient webViewClient){
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);

        //设置cookie
        Tools.synCookies(context, HttpConfig.HTTP_QUERY_URL);

        webView.setWebViewClient(webViewClient);
    }
}
