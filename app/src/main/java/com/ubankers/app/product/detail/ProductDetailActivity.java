package com.ubankers.app.product.detail;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.ubankers.app.base.session.Session;
import com.ubankers.app.product.detail.reserve.CfmpReserveAction;
import com.ubankers.app.product.detail.reserve.InvestorReserveAction;
import com.ubankers.app.product.detail.share.ProductSharePopup;
import com.ubankers.app.product.model.Product;
import com.ubankers.mvp.presenter.Presenter;
import com.ubankers.mvp.view.MvpActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.sns.controller.activity.SnsArticleActivity;
import cn.com.ubankers.www.sns.model.ArticleBean;
import cn.com.ubankers.www.utils.LoginDialog;
import cn.com.ubankers.www.widget.TitlePopup;

public class ProductDetailActivity extends MvpActivity<ProductDetailView>{

    // 启动activity时提供的intent
	public final static String EXTRA_PRODUCT_DETAIL = "com.ubankers.app.product.detail.ProductDetailActivity.Product";
    public final static String EXTRA_PRODUCT_ID = "com.ubankers.app.product.detail.ProductDetailActivity.ProductID";
    public final static String EXTRA_RESERVER_NAME = "com.ubankers.app.product.detail.ProductDetailActivity.ReserverName";

    // 财富师选择预约的投资者时提供的intent
    public final static String EXTRA_CLIENT_ID = "com.ubankers.app.product.detail.ProductDetailActivity.ClientID";
    public final static String EXTRA_CLIENT_NAME = "com.ubankers.app.product.detail.ProductDetailActivity.ClientName";
    public final static String EXTRA_CLIENT_MOBILE = "com.ubankers.app.product.detail.ProductDetailActivity.ClientMobile";
    public final static String EXTRA_CLIENT_TYPE = "com.ubankers.app.product.detail.ProductDetailActivity.ClientType";

    // 启动activity: {@link RegisterIntervorActivity} “财富师选择预约投资者”的requestCode
	public static final int REQUEST_CFMP_SELECT_INVESTOR = 200;

    // 投资者类型（已注册、未注册的B类)
    public static final int INVESTOR_TYPE_REGISTERED = 1;
    public static final int INVESTOR_TYPE_B = 2;

    private ProductDetailComponent component;
    @Inject ProductDetailPresenter presenter;
    @Inject ProductDetailView view;
    @Inject Session session;

	@Bind(R.id.back) View back;
	@Bind(R.id.productweb_view) WebView productWebView;
	@Bind(R.id.product_noH5Layout) LinearLayout productLayout;
	@Bind(R.id.prod) TextView reservationButton;
	@Bind(R.id.product_classification) LinearLayout productClassification;
    @BindDrawable(R.color.treasure_catelog)  Drawable bgOfRecommendationPopup;

    private ProductDetailWebView productDetailWebView;
    private TitlePopup productSharePopup;
    private CfmpReserveAction cfmpReserveAction;

    private ProductDetailModel viewModel = new ProductDetailModel();


	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        injectDependency();
        initViewModel();
        initView();
    }

    @Override protected  void onDestroy(){
        super.onDestroy();
        component = null;
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CFMP_SELECT_INVESTOR ){ //财富师在预约活动中，选择待预约的投资者
            cfmpReserveAction.onInvestorSelected(resultCode, data);
        }
    }

    @OnClick(R.id.back)
    public void onBackButtonClicked(){
        finish();
    }

    @OnClick(R.id.product_classification)
    public void onProductClassificationClick() {
        productSharePopup.show(productClassification);
    }


   void showAuthenticationRequired() {
        Toast.makeText(this, "产品详情需要登录后才能查看", Toast.LENGTH_SHORT).show();

        session.invalidate();
        LoginDialog loginDialog = new LoginDialog(this, 0, 0);
        loginDialog.onLogin();
    }

    void showProduct(Product product) {
        productDetailWebView.showProduct(product.getProductId());
    }

    void showError(Throwable e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    void showArticle(ArticleBean articleBean){
        Intent intent = new Intent(this, SnsArticleActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("articleBean", articleBean);
        bundle.putBoolean("productFlag", true);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    void showReservation(Product product){
        if (!product.canReserve()) {
            return;
        }
        reservationButton.setVisibility(View.VISIBLE);

        if (session.isInvestor()) {
            //投资者预约
            reservationButton.setOnClickListener(new InvestorReserveAction(this, viewModel.getReserverName(), product));
        } else if (session.isCfmp()) {
            //财富师给投资者预约
            cfmpReserveAction = new CfmpReserveAction(this, product);
            reservationButton.setOnClickListener(cfmpReserveAction);
        }
    }

    void showProductLayout(){
        productLayout.setVisibility(View.VISIBLE);
    }


    private void injectDependency(){
        component = DaggerProductDetailComponent.builder()
                .appComponent(((MyApplication)getApplication()).getComponent())
                .productDetailModule(new ProductDetailModule(this))
                .build();
        component.inject(this);
    }

    private void initViewModel(){
        viewModel.init(presenter, getIntent());
    }

    private void initView() {
        setContentView(R.layout.product_details_activity);
        ButterKnife.bind(this);

        productDetailWebView = new ProductDetailWebView(this, productWebView);
        initProductSharePopupIfUserIsCfmp();
    }

    private void initProductSharePopupIfUserIsCfmp(){
        if(!session.isCfmp()){
            return;
        }

        productClassification.setVisibility(View.VISIBLE);
        productSharePopup = new ProductSharePopup(this, productClassification, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        productSharePopup.setBackgroundDrawable(bgOfRecommendationPopup);
    }

    @Override
    protected Presenter<ProductDetailView> getPresenter() {
        return presenter;
    }

    @Override
    protected ProductDetailView getView() {
        return view;
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
}