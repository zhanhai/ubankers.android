package com.ubankers.app.product.detail;


import com.loopj.android.http.JsonHttpResponseHandler;
import com.ubankers.app.base.api.Response;
import com.ubankers.app.base.session.Session;
import com.ubankers.app.member.model.CfmpQualificationStatus;
import com.ubankers.app.member.model.MemberAPI;
import com.ubankers.app.product.model.Product;
import com.ubankers.app.product.model.ProductAPI;
import com.ubankers.mvp.presenter.MvpCommand;
import com.ubankers.mvp.presenter.Presenter;

import org.apache.http.Header;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.http.ParseUtils;
import retrofit.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Singleton
public class ProductDetailPresenter extends Presenter<ProductDetailView> {

    @Inject ProductAPI productAPI;
    @Inject MemberAPI memberAPI;
    @Inject Session session;


    public void loadProductDetail(final String productId){
        onLoading();

        productAPI.getProduct(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<Product>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        int statusCode = -1;
                        if (e instanceof HttpException) {
                            statusCode = ((HttpException) e).code();
                        }

                        if (statusCode != 401) {
                            onProductLoaded(e, null);
                        } else {
                            onAuthenticationFailed();
                        }
                    }

                    @Override
                    public void onNext(Response<Product> productResponse) {
                        if (!productResponse.isSuccess()) {
                            if (productResponse.getResult().getErrorCode().equals("noLogin")) {
                                onAuthenticationFailed();
                            } else {
                                onProductLoaded(new Error("处理错误"), null);
                            }
                        } else {
                            onProductLoaded(null, productResponse.getResult().getInfo());
                        }
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

    public void verifyCfmpQualificationStatus(){

        memberAPI.isQualifiedCfmp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<CfmpQualificationStatus>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        int statusCode = -1;
                        if (e instanceof HttpException) {
                            statusCode = ((HttpException) e).code();
                        }

                        if (statusCode != 401) {
                            onProductLoaded(e, null);
                        } else {
                            onAuthenticationFailed();
                        }
                    }

                    @Override
                    public void onNext(Response<CfmpQualificationStatus> response) {
                        if (!response.isSuccess()) {
                            if (response.getResult().getErrorCode().equals("noLogin")) {
                                onAuthenticationFailed();
                            } else {
                                onProductLoaded(new Error("处理错误"), null);
                            }
                        } else {
                            final boolean isQualifiedCfmp = response.getResult().getInfo().isQualified();
                            render(new MvpCommand<ProductDetailView>() {
                                @Override
                                public void call(ProductDetailView view) {
                                    view.cfmpQualificationStatus(isQualifiedCfmp);
                                }
                            });
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



    void onProductLoaded(final Throwable t, final Product product) {

        render(new MvpCommand<ProductDetailView>() {
            @Override
            public void call(ProductDetailView view) {
                view.showProduct(t, product);
            }
        });
    }
}
