package cn.com.ubankers.www.sns.controller.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jauker.widget.BadgeView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.http.ParseUtils;
import com.ubankers.app.product.detail.ProductDetailActivity;
import cn.com.ubankers.www.product.model.ProductDetail;
import cn.com.ubankers.www.sns.model.ArticleBean;
import cn.com.ubankers.www.sns.model.CommentBean;
import cn.com.ubankers.www.sns.service.SnsArticleService;
import cn.com.ubankers.www.utils.NetReceiver;
import cn.com.ubankers.www.utils.NetReceiver.NetState;
import cn.com.ubankers.www.widget.SnsDialog;
import cn.sharesdk.framework.ShareSDK;

public class SnsArticleActivity extends Activity {
	public TextView argumentsView, collections, love, share, title_time;
	private int comment_count; // 评论的次数
	private ArticleBean articleBean;
	private StringEntity entity = null;
	private static AsyncHttpClient client;
	DisplayImageOptions options;
	public boolean loveFlag = false;
	public boolean collectionsFlag = false;
	public Context context;
	private View sharLayout;
	private EditText comment;
	public SnsDialog progressDialog;
	public  RelativeLayout arguments_layout, love_layout,
			collections_layout, share_layout, publish_comment;
	private static ArrayList<CommentBean> arrayList = new ArrayList<CommentBean>();
	private WebView webView;
	private String type;
	private RequestParams params = null;
	private ScrollView sv;
	private int readCount;
	private BadgeView badgeView;
	private View back;
	public SnsArticleService snsArticleService;
	private boolean productFlag=false;
	private String articleBeanId;
	private long timeout = 5000;
	private Handler mHandler = new Handler();
	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.sns_article_activity);
		context = this;
		ShareSDK.initSDK(context);
		client = MyApplication.app.getClient(context);
		snsArticleService = new SnsArticleService(SnsArticleActivity.this,client);
		if (progressDialog == null) {
			progressDialog = SnsDialog.createDialog(this);
		}
		Intent intent = this.getIntent();
		if (intent != null) {
			articleBean = (ArticleBean) intent.getSerializableExtra("articleBean");
			type = intent.getStringExtra("type");
			productFlag = intent.getBooleanExtra("productFlag",false);
			articleBeanId=intent.getStringExtra("articleBeanId");
		}
		initView();
		try{
				if(!productFlag){
					if(articleBean!=null){
						requestArticle(articleBean.get_id());	
					}else if(articleBeanId!=null){
						requestArticle(articleBeanId);
					}
				}else{
					requsetWebView();
				}
		}catch(Exception e){
			e.printStackTrace();
		}
		NetState connected = NetReceiver.isConnected(context);
		if (connected == connected.NET_NO) {
			Toast.makeText(context, "当前网络不可用", Toast.LENGTH_SHORT).show();
		}
		MyApplication.getInstance().addActivity(this);
	}

	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		client = MyApplication.app.getClient(context);
		if (progressDialog == null) {
			progressDialog = SnsDialog.createDialog(this);
		}
		snsArticleService = new SnsArticleService(SnsArticleActivity.this,client);
		Intent intent = this.getIntent();
		if (intent != null) {
			articleBean = (ArticleBean) intent.getSerializableExtra("articleBean");
			type = intent.getStringExtra("type");
			productFlag = intent.getBooleanExtra("productFlag",false);
			articleBeanId=intent.getStringExtra("articleBeanId");
		}
		initView();
		if(!productFlag){
			if(articleBean!=null){
				requestArticle(articleBean.get_id());	
			}else if(articleBeanId!=null){
				requestArticle(articleBeanId);
			}	
		}else{
			requsetWebView();
		}
		NetState connected = NetReceiver.isConnected(context);
	}
	private void initView() {
		back = (View) findViewById(R.id.back);
		webView = (WebView) findViewById(R.id.web_view);
		WebSettings settings = webView.getSettings();
		String userAgent = settings.getUserAgentString();
		settings.setUserAgentString(userAgent + " ronganapp");
		sv = (ScrollView) findViewById(R.id.sv);
		argumentsView = (TextView) findViewById(R.id.argumentsView);
		collections = (TextView) findViewById(R.id.collections);
		love = (TextView) findViewById(R.id.love);
		share = (TextView) findViewById(R.id.share);
		arguments_layout = (RelativeLayout) findViewById(R.id.arguments_layout);
		love_layout = (RelativeLayout) findViewById(R.id.love_layout);
		collections_layout = (RelativeLayout) findViewById(R.id.collections_layout);
		share_layout = (RelativeLayout) findViewById(R.id.share_layout);
		arguments_layout.setOnClickListener(new MyOnClickListener());
		collections_layout.setOnClickListener(new MyOnClickListener());
		love_layout.setOnClickListener(new MyOnClickListener());
		share_layout.setOnClickListener(new MyOnClickListener());
		title_time = (TextView) findViewById(R.id.title_time);
		back.setOnClickListener(new MyBackClickListener());    //这里的cookie就是上面保存的cookie  
		if(productFlag){
			if (articleBean!=null&&articleBean.isIs_favored()) {
				collections.setBackgroundResource(R.drawable.stared);
				collectionsFlag = true;	
			}
			if (articleBean!=null&&articleBean.isIs_voted()) {
				love.setBackgroundResource(R.drawable.hearted);
				loveFlag = true;
			}
			if (articleBean!=null&&articleBean.getComment_count()>=0) {
			argumentsView.setText(articleBean.getComment_count() + "");
			}
		}
	}
	// 获取文章详情接口
		public void requestArticle(String articleId) {
			progressDialog.show();
			client.get(HttpConfig.URL_ARTICLE_DETAIL+ articleId,
					new JsonHttpResponseHandler() {
						public void onSuccess(int statusCode,
								org.apache.http.Header[] headers,
								JSONObject response) {
							try {
								JSONObject object = response
										.getJSONObject("result");
								boolean flag = response.getBoolean("success");
								if (flag) {
									JSONObject obj = object.getJSONObject("article");
									articleBean=ParseUtils.parseArticle(obj);
									if (articleBean!=null&&articleBean.isIs_favored()) {
										collections.setBackgroundResource(R.drawable.stared);
										collectionsFlag = true;	
									}
									if (articleBean!=null&&articleBean.isIs_voted()) {
										love.setBackgroundResource(R.drawable.hearted);
										loveFlag = true;
									}
									if (articleBean!=null&&articleBean.getComment_count()>=0) {
									argumentsView.setText(articleBean.getComment_count() + "");
									}else{
										argumentsView.setText(0);
									}
									requsetWebView();
								}
								progressDialog.dismiss();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								progressDialog.dismiss();
							} finally {
								progressDialog.dismiss();
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							// TODO Auto-generated method stub
							super.onFailure(statusCode, headers, throwable,
									errorResponse);
							progressDialog.dismiss();
							Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
							
						}
					});
			progressDialog.dismiss();
		}
	
	 // WebView load h5 resourece 
	private void requsetWebView() {
//		progressDialog.show(); 
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setUseWideViewPort(true);
		
		//webView.getSettings().setJavaScriptEnabled(true);
	    //Tools.synCookies(MyApplication.app,HttpConfig.URL_ARTICL_H5 + articleBean.get_id());
		if(articleBean!=null&&articleBean.get_id()!=null){
			webView.loadUrl(HttpConfig.URL_ARTICL_H5 + articleBean.get_id());	
//			webView.loadDataWithBaseURL(null, HttpConfig.URL_ARTICL_H5 + articleBean.get_id(), "text/html", "utf-8", null);
		}else if(articleBeanId!=null){
			webView.loadUrl(HttpConfig.URL_ARTICL_H5 + articleBeanId);	
//			webView.loadDataWithBaseURL(null, HttpConfig.URL_ARTICL_H5 + articleBeanId, "text/html", "utf-8", null);
		}
		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				if(url.contains("product/_escape_app/productinfo/")){
					String productId = url.substring(url.lastIndexOf("/")+1, url.length());
					ProductDetail productDetail = new ProductDetail();
					productDetail.setProductId(productId);
					Intent intent = new Intent(context, ProductDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(ProductDetailActivity.KEY_PRODUCT_ID, productId);
					intent.putExtras(bundle);
					context.startActivity(intent);
				}else{
					view.loadUrl(url);
//					view.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
				}
				
				return true;
			}
			
//			@Override
//			public void onPageStarted(WebView view, String url, Bitmap favicon) {
//				// TODO Auto-generated method stub
//				super.onPageStarted(view, url, favicon);
//				timer = new Timer();
//				TimerTask tt = new TimerTask() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						if(SnsArticleActivity.this.webView.getProgress()<100){
//							Message msg = new Message();
//							msg.what = 1;
//							mHandler.sendMessage(msg);
//							
//							timer.purge();
//						}
//					}
//				};
//				timer.schedule(tt, timeout, 1);
//			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
//				progressDialog.dismiss();
			}
			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				handler.proceed();//接受ssl证书
				super.onReceivedSslError(view, handler, error);
			}
		});
//		webView.loadUrl(HttpConfig.URL_ARTICL_H5 + articleBean.get_id());	
//		webView.loadUrl(HttpConfig.URL_ARTICL_H5 + articleBeanId);	
		
	}
   //签名档
	private void getProduct(String productId){
		progressDialog.show();
		client.get(context, HttpConfig.URL_PRODUCT_PARTICULARS+productId, null, "application/json", new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				JSONObject obj;
				super.onSuccess(statusCode, headers, response);
				try {
					boolean flag = response.getBoolean("success");
					if (flag) {
					 obj = response.getJSONObject("result");
					 JSONObject  info = obj.getJSONObject("info");
					 ProductDetail productDetail = ParseUtils.parseProduct(info);
					 
					}
					progressDialog.dismiss();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					progressDialog.dismiss();
				}
				progressDialog.dismiss();
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				progressDialog.dismiss();
				Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
				
			}			
		});
		progressDialog.dismiss();
	}
	public class MyOnClickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			snsArticleService.itemClick(arg0,articleBean);
		}
	}

	private Object getLast(Editable text, Class kind) {
		Object[] objs = text.getSpans(0, text.length(), kind);

		if (objs.length == 0) {
			return null;
		} else {
			for (int i = objs.length; i > 0; i--) {
				if (text.getSpanFlags(objs[i - 1]) == Spannable.SPAN_MARK_MARK) {
					return objs[i - 1];
				}
			}
			return null;
		}
	}



	/**
	 * TODO SnsArticleActivity is finish
	 * 
	 */
	private class MyBackClickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			snsArticleService.back();
		}
	}

	/**
	 * TODO argument widget skip event
	 * 
	 */
	private class MyArgumentOnClickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			snsArticleService.skipCommentActivity(articleBean);
		}
	}

	/**
	 * back widget implements
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			webView.goBack();
			snsArticleService.back();
		}
		return false;
	}
	
	
}
