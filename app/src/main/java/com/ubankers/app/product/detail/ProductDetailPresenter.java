package com.ubankers.app.product.detail;


import com.loopj.android.http.JsonHttpResponseHandler;
import com.ubankers.app.base.api.Consumer;
import com.ubankers.app.base.api.Invoke;
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

        Invoke.api(productAPI.getProduct(productId))
                .call(new ProductApiConsumer(this));
    }


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
        Invoke.api(memberAPI.isQualifiedCfmp())
                .call(new MemberApiConsumer(this));
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

    void onCfmpQualificationStatus(final Throwable t, final boolean isQualifiedCfmp){
        render(new MvpCommand<ProductDetailView>() {
            @Override
            public void call(ProductDetailView view) {
                view.cfmpQualificationStatus(t, isQualifiedCfmp);
            }
        });
    }
}

class ProductApiConsumer extends Consumer<Product>
{
    private ProductDetailPresenter presenter;

    ProductApiConsumer(ProductDetailPresenter presenter){
        this.presenter = presenter;
    }

    @Override
    protected void onAuthenticationFailed() {
        presenter.onAuthenticationFailed();
    }

    @Override
    protected void onException(Throwable t) {
        presenter.onProductLoaded(t, null);
    }

    @Override
    protected void onData(Product data) {
        presenter.onProductLoaded(null, data);
    }
}

class MemberApiConsumer extends Consumer<CfmpQualificationStatus>
{
    private ProductDetailPresenter presenter;

    MemberApiConsumer(ProductDetailPresenter presenter){
        this.presenter = presenter;
    }

    @Override
    protected void onAuthenticationFailed() {
        presenter.onAuthenticationFailed();
    }

    @Override
    protected void onException(Throwable t) {
        presenter.onCfmpQualificationStatus(t, false);
    }

    @Override
    protected void onData(CfmpQualificationStatus  cfmpQualificationStatus) {
        presenter.onCfmpQualificationStatus(null, cfmpQualificationStatus.isQualified());
    }
}