package com.ubankers.app.product.detail;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.ubankers.app.product.model.Product;
import com.ubankers.app.product.model.ProductAPI;
import com.ubankers.mvp.presenter.LCEPresenter;
import com.ubankers.mvp.presenter.RxPresenter;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.http.ParseUtils;
import cn.com.ubankers.www.product.model.ProductDetail;
import cn.com.ubankers.www.utils.LoginDialog;
import rx.Observable;
import rx.functions.Func0;
import rx.subjects.BehaviorSubject;

@Singleton
public class ProductDetailPresenter extends LCEPresenter<Product, ProductDetailView> {

    private static final String PRODUCT_ID_KEY = "product_id";

    @Inject
    ProductAPI productAPI;

    private String productId;



    @Override
    protected void onStart(@Nullable Bundle savedState){
        if(savedState == null){
            return;
        }

        // We need to consider whether to change current prodcut id value.
        String savedProductId = savedState.getString(PRODUCT_ID_KEY);
        if(productId != null && productId.equals(savedProductId)) {
            // If productId was not changed, nothing to do
            return;
        }
        else{
            reset();

            productId = savedProductId;
            loadProductDetail();
        }
    }


    @Override
    protected void onReset() {
        super.onReset();

        productId = null;
    }

    @Override
    public void onSave(@NonNull Bundle state)
    {
        if(productId != null){
            state.putString(PRODUCT_ID_KEY, productId);
        }
    }


    public void loadProductDetail(){
        onLoading();

    }


    //签名档
    public void loadArticle(final ProductDetailView view, final String articleId){
        MyApplication.getClient().get(HttpConfig.URL_ARTICLE_DETAIL + articleId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                JSONObject obj = response.optJSONObject("result");
                if (obj == null) {
                    return;
                }

                JSONObject article = obj.optJSONObject("article");
                view.showArticle(ParseUtils.parseArticle(article));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                view.showError(throwable);
            }

        });
    }

    public void checkQualificationStatusOfCFMP(final Context context){

        MyApplication.getClient().post(HttpConfig.URL_QUALIFIED_WEALTH_DIVISION, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                try {
                    boolean flag = response.getBoolean("success");
                    if (flag) {
                        JSONObject jsonObject = response.getJSONObject("result");
                        JSONObject jsonObject2 = jsonObject.getJSONObject("info");
                        isAQualifiedCFMP = jsonObject2.getInt("qualified");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable,
                        errorResponse);
                if (statusCode == 401) {
                    try {
                        boolean flag = errorResponse.getBoolean("success");
                        if (flag == false) {
                            JSONObject jsonObject = errorResponse.getJSONObject("result");
                            String errorCode = jsonObject.getString("errorCode");
                            if (errorCode.equals("noLogin")) {
                                Toast.makeText(context, "用户未登录", Toast.LENGTH_SHORT).show();
                                MyApplication.app.setUser(null);
                                MyApplication.app.setClient(null);
                                LoginDialog loginDialog = new LoginDialog(context, 0, 0);
                                loginDialog.onLogin();
                            }
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}
