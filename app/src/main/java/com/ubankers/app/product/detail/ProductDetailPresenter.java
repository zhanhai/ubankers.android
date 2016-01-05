package com.ubankers.app.product.detail;


import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.ubankers.app.base.session.Session;
import com.ubankers.app.product.model.ProductAPI;
import com.ubankers.mvp.presenter.MvpCommand;
import com.ubankers.mvp.presenter.Presenter;

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

@Singleton
public class ProductDetailPresenter extends Presenter<ProductDetailView> {

    @Inject ProductAPI productAPI;
    @Inject Session session;


    public void loadProductDetail(String productId){
        onLoading();

        MyApplication.getClient().get(HttpConfig.URL_PRODUCT_PARTICULARS + productId,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        onProductLoaded(statusCode, headers, response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        onProductLoaded(statusCode, headers, throwable, errorResponse);
                    }
                });
    }


    /**
     * 获取文章详情
     * @param articleId
     */
    public void loadArticle(final String articleId){
        MyApplication.getClient().get(HttpConfig.URL_ARTICLE_DETAIL + articleId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                JSONObject obj = response.optJSONObject("result");
                if (obj == null) {
                    return;
                }

                final JSONObject article = obj.optJSONObject("article");
                render(new MvpCommand<ProductDetailView>() {
                    @Override
                    public void call(ProductDetailView view) {
                        view.showArticle(null, ParseUtils.parseArticle(article));
                    }
                });

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, final Throwable throwable, JSONObject errorResponse) {
                render(new MvpCommand<ProductDetailView>() {
                    @Override
                    public void call(ProductDetailView view) {
                        view.showArticle(throwable, null);
                    }
                });
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
                        int isAQualifiedCFMP = jsonObject2.getInt("qualified");
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


    void onLoading() {
        render(new MvpCommand<ProductDetailView>() {
            @Override
            public void call(ProductDetailView view) {
                view.showLoading();
            }
        });
    }


    void onAuthenticationFailed() {
        session.invalidate();

        render(new MvpCommand<ProductDetailView>() {
            @Override
            public void call(ProductDetailView view) {
                view.showAuthentication();
            }
        });
    }

    void onProductLoaded(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
        if (statusCode != 401) {
            onProductLoaded(new Error("处理错误"), null);
        }
        else{
            onAuthenticationFailed();
        }
    }

    void onProductLoaded(int statusCode, Header[] headers, JSONObject response){
        boolean flag = response.optBoolean("success", false);

        if (!flag) {
            JSONObject object = response.optJSONObject("result");
            String errorCode = object.optString("errorCode", "");
            if (errorCode.equals("noLogin")) {
                onAuthenticationFailed();
            } else {
                onProductLoaded(new Error("处理错误"), null);
            }
            return;
        }

        JSONObject obj = response.optJSONObject("result");
        if (obj == null) {
            onProductLoaded(new Error("处理错误"), null);
            return;
        }

        String errorCode = obj.optString("errorCode");
        JSONObject info = obj.optJSONObject("info");

        if (!"success".equals(errorCode) || info == null) {
            onProductLoaded(new Error("处理错误"), null);
            return;
        }

        JSONObject reserveOptions = null;
        JSONObject saleOptions = null;

        ProductDetail product = new ProductDetail();
        product.setProductId(info.optString("id", "0"));
        product.setProductName(info.optString("productName", ""));
        product.setModuleId(info.optString("moduleId", ""));
        product.setState(info.optInt("state", 0));
        product.setIsHot(info.optInt("isHot", 0));
        product.setProductTerm(info.optString("productTerm", ""));
        product.setCountProductRate(info.optString("countProductRate", ""));
        product.setMinSureBuyPrice(info.optString("minSureBuyPrice", ""));
        product.setRaisedProcessShow(info.optInt("raisedProcessShow", 0));

        saleOptions = info.optJSONObject("saleOptions");//预售
        if (saleOptions != null && saleOptions.length() > 0) {
            reserveOptions = saleOptions.optJSONObject("reserveOptions");//预定
            if (reserveOptions != null && reserveOptions.length() > 0) {
                product.setPayType(reserveOptions.optString("payType", ""));//支付类型
                product.setEndTime(reserveOptions.optString("endTime", ""));//预定结束时间： 毫秒
                product.setMaxMoney(reserveOptions.optInt("maxMoney"));//最大金额(以万来计算)
                product.setMinMoney(reserveOptions.optInt("minMoney"));//最小金额(以万来计算)
                product.setIncrementalMoney(reserveOptions.optInt("incrementalMoney"));//递增金额
                product.setCanReserve(reserveOptions.optBoolean("canReserve"));//能否预约
                product.setFace(reserveOptions.optString("face", ""));//能否预约
            }
        }

        onProductLoaded(null, product);
    }

    void onProductLoaded(final Throwable t, final ProductDetail product) {

        render(new MvpCommand<ProductDetailView>() {
            @Override
            public void call(ProductDetailView view) {
                view.showProductDetail(t, product);
            }
        });
    }
}
