package com.ubankers.app.product.detail;

import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ubankers.mvp.view.MvpActivity;

import javax.inject.Inject;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.product.listener.MyCfmpOrderListener;
import cn.com.ubankers.www.product.listener.MyInvestorOrderListener;
import cn.com.ubankers.www.product.model.ProductDetail;
import cn.com.ubankers.www.product.service.ProductDetailService;
import cn.com.ubankers.www.product.view.ProductShareDialog;
import cn.com.ubankers.www.sns.controller.activity.SnsArticleActivity;
import cn.com.ubankers.www.sns.model.ArticleBean;
import cn.com.ubankers.www.utils.LoginDialog;
import cn.com.ubankers.www.utils.Tools;
import cn.com.ubankers.www.widget.ActionItem;
import cn.com.ubankers.www.widget.MyDialog;
import cn.com.ubankers.www.widget.TitlePopup;
import cn.com.ubankers.www.widget.TitlePopup.OnItemOnClickListener;

public class ProductDetailActivity extends MvpActivity<ProductDetailView, ProductDetailPresenter> implements OnClickListener{

	public final static String KEY_PRODUCT_DETAIL = "com.ubankers.app.product.detail.ProductDetailActivity.Product";
    public final static String KEY_PRODUCT_ID = "com.ubankers.app.product.detail.ProductDetailActivity.ProductID";
    public final static String KEY_RESERVER_NAME = "com.ubankers.app.product.detail.ProductDetailActivity.ReserverName";

    public final static String KEY_CLIENT_ID = "com.ubankers.app.product.detail.ProductDetailActivity.ClientID";
    public final static String KEY_CLIENT_NAME = "com.ubankers.app.product.detail.ProductDetailActivity.ClientName";
    public final static String KEY_CLIENT_MOBILE = "com.ubankers.app.product.detail.ProductDetailActivity.ClientMobile";
    public final static String KEY_CLIENT_TYPE = "com.ubankers.app.product.detail.ProductDetailActivity.ClientType";

	public static final int request =200;

    private ProductDetailComponent component;
    @Inject ProductDetailPresenter presenter;

	private MyDialog progressDialog;
	private View back;
	public  WebView productWebView;
	public static LinearLayout productLayout;
	public static TextView order;
	private String productId;
    private String reserverName;
	private String nickName;
	public String id;
	private String number;
	private TitlePopup titlePopup;
	private LinearLayout product_classification;
	private ProductDetail productDetail;
	private int fied;//判断财富师名片有没有审核通过
	public static RelativeLayout order_rl;

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        initView();

        parseIntent();
        injectDependency();

        presenter.attachView(this);
    }

    @SuppressWarnings("static-access")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == request && resultCode == 200){
            int type = data.getIntExtra(KEY_CLIENT_TYPE, 0);
            if(type==1){

                nickName = data.getStringExtra(KEY_CLIENT_NAME);
                id = data.getStringExtra(KEY_CLIENT_ID);
                number=data.getStringExtra(KEY_CLIENT_MOBILE);

                try{
                    if(!nickName.equals("") || !number.equals("") ||!id.equals("")){
                        MyCfmpOrderListener.registered_name.setText(nickName);
                        MyCfmpOrderListener.etNumber.setText(number);
                        MyCfmpOrderListener.registered_name.clearFocus();
                        MyCfmpOrderListener.registered_name.setFocusable(false);
                        MyApplication.app.setOrderInvestorId(id);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else if(type==2){

                nickName = data.getStringExtra(KEY_CLIENT_NAME);
                number=data.getStringExtra(KEY_CLIENT_MOBILE);

                try{
                    if(!nickName.equals("")|| !number.equals("")){
                        MyCfmpOrderListener.registered_name.setText(nickName);
                        MyCfmpOrderListener.etNumber.setText(number);
                        MyCfmpOrderListener.registered_name.clearFocus();
                        MyCfmpOrderListener.registered_name.setFocusable(false);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override public String getProductId(){
        return productId;
    }

    @Override public void showAuthenticationRequired() {
        Toast.makeText(this, "产品详情需要登录后才能查看", Toast.LENGTH_SHORT).show();

        MyApplication.app.setUser(null);
        MyApplication.app.setClient(null);
        LoginDialog loginDialog = new LoginDialog(this, 0, 0);
        loginDialog.onLogin();
    }

    @Override public void showLoading() {
		if (progressDialog == null) {
			progressDialog = MyDialog.createDialog(this, "正在加载中...");
		}

		progressDialog.show();
    }

    @Override public void showContent(ProductDetail product) {
        progressDialog.dismiss();

        productWebView.loadUrl(HttpConfig.URL_INFROMATION + productId);
        showReservation(product);

    }

    @Override public void showError(Throwable e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    @Override public void showArticle(ArticleBean articleBean){
        Intent intent = new Intent(this, SnsArticleActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("articleBean", articleBean);
        bundle.putBoolean("productFlag", true);
        intent.putExtras(bundle);
        this.startActivity(intent);

    }

    private void showReservation(ProductDetail product){
        if (!product.isCanReserve()) {
            return;
        }

        order.setVisibility(View.VISIBLE);

        if (MyApplication.isCurrentUserAInvestor()) {
            //投资者预约
            order.setOnClickListener(new MyInvestorOrderListener(this, reserverName, product));
        } else if (MyApplication.isCurrentUserACFMP()) {
            //财富师给投资者预约
            order.setOnClickListener(new MyCfmpOrderListener(this, product, id, reserverName));
        }
    }


    public void setProductDetailComponent(ProductDetailComponent productDetailComponent) {
        this.component = productDetailComponent;
    }


	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.product_classification:
				titlePopup.show(product_classification);
			break;

		default:
			break;
		}
		
	}
	

    protected void injectDependency(){
        component = DaggerProductDetailComponent.create();
        component.inject(this);

        presenter = component.presenter();
    }


    private void parseIntent(){
        Intent intent = this.getIntent();
        if (intent== null) {
            return;
        }
        productId = intent.getStringExtra(KEY_PRODUCT_ID);
        reserverName = intent.getStringExtra(KEY_RESERVER_NAME);
        productDetail = (ProductDetail) intent.getSerializableExtra(KEY_PRODUCT_DETAIL);

		if(productDetail != null && productDetail.getProductId()!= null){
			productId = productDetail.getProductId();
		}
    }


    private void initView() {
        setContentView(R.layout.product_details_activity);

        productWebView = (WebView) findViewById(R.id.productweb_view);
        productLayout = (LinearLayout) findViewById(R.id.product_noH5Layout);
        back = findViewById(R.id.back);

        back.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View arg0) {
                finish();
            }
        });

        order =(TextView) findViewById(R.id.prod); //预约
        order_rl = (RelativeLayout)findViewById(R.id.prod_rl);
        product_classification =(LinearLayout) findViewById(R.id.product_classification);

        if(MyApplication.isCurrentUserACFMP()){
            product_classification.setVisibility(View.VISIBLE);
            titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            titlePopup.setBackgroundDrawable(getResources().getDrawable(R.color.treasure_catelog));
            titlePopup.setItemOnClickListener(new OnItemOnClickListener() {

                @Override
                public void onItemClick(ActionItem item, int position) {
                    if (position == 0) {//分享
                        if (fied == 0) {
                            cn.com.ubankers.www.utils.MyDialog dialog = new cn.com.ubankers.www.utils.MyDialog(ProductDetailActivity.this);
                            dialog.businessCardDialog();
                        } else {
                            ProductShareDialog psd = new ProductShareDialog(ProductDetailActivity.this, null, productDetail);
                            // 显示窗口
                            psd.showAtLocation(product_classification, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                        }
                    }


                }
            }); //推荐，分享

            titlePopup.addAction(new ActionItem(this, "分享"));
            product_classification.setOnClickListener(this);
        }

        initWebView();
    }

    private void initWebView(){
        productWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        productWebView.getSettings().setSupportZoom(false);
        productWebView.getSettings().setBuiltInZoomControls(false);
        productWebView.getSettings().setUseWideViewPort(true);
        productWebView.getSettings().setJavaScriptEnabled(true);
        //给webView设置cookie
        Tools.synCookies(this, HttpConfig.URL_INFROMATION + productId);

        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        productWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.contains("sns/_escape_app/articleDetail/")) {
                    String articleId = url.substring(url.lastIndexOf("/") + 1, url.length());
                    presenter.loadArticle(articleId);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                productLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                handler.proceed();// 接受ssl证书
                super.onReceivedSslError(view, handler, error);
            }
        });
    }
}
	