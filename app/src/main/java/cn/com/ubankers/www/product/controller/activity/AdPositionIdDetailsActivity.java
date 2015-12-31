package cn.com.ubankers.www.product.controller.activity;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.controller.activity.AboutOursActivity;
import cn.com.ubankers.www.utils.NetReceiver;
import cn.com.ubankers.www.utils.NetReceiver.NetState;
import cn.com.ubankers.www.widget.MyDialog;
import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class AdPositionIdDetailsActivity extends Activity{
	private MyDialog progressDialog;
	private String AdPositionIdUrl;
	private WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adpositiondetails_layout);
		webView =(WebView) findViewById(R.id.details_webView);
		Intent intent = this.getIntent();
		if (intent != null) {
			AdPositionIdUrl = intent.getStringExtra("AdPositionIdUrl");
		}
		NetState connected = NetReceiver.isConnected(this);
		if(progressDialog==null){
			progressDialog=MyDialog.createDialog(AdPositionIdDetailsActivity.this,"正在加载中...");
		}
		
		if(connected==connected.NET_NO){
				Toast.makeText(this, "当前网络不可用",Toast.LENGTH_SHORT).show();
		}else{
			
			 progressDialog.show();
			 requsetWebView();
//			 webView.loadUrl(HttpConfig.URL_ABOUT);
			 progressDialog.dismiss();
		}
		 MyApplication.getInstance().addActivity(this);	 
	}
	private void requsetWebView(){
	       //WebView加载web资源
	 	   webView.getSettings().setSupportZoom(true);
	 	   webView.getSettings().setBuiltInZoomControls(true);
	 	   webView.getSettings().setUseWideViewPort(true);
	       webView.loadUrl(AdPositionIdUrl);
	       //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
	       webView.setWebViewClient(new WebViewClient(){
	            @Override
	       public boolean shouldOverrideUrlLoading(WebView view, String url) {
	             // TODO Auto-generated method stub
	                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
	              view.loadUrl(url);
	             return true;
	         }
	            @Override
	    		public void onPageFinished(WebView view, String url) {
	    			// TODO Auto-generated method stub
	    			super.onPageFinished(view, url);
	    			progressDialog.dismiss();
	            }
	            @Override
	            public void onReceivedSslError(WebView view,
	            	SslErrorHandler handler, SslError error) {
	            	handler.proceed();
	            	super.onReceivedSslError(view, handler, error);
	            }
	        });
	 	}
}
