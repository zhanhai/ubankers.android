package cn.com.ubankers.www.authentication.controller.activity;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.widget.MyDialog;
import cn.com.ubankers.www.utils.NetReceiver;
import cn.com.ubankers.www.utils.NetReceiver.NetState;
import android.app.Activity;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.widget.ImageView;

public class AgreementActivity extends Activity  implements OnClickListener{
	private WebView webView;
	private View title_yonhuzhuce;
	private MyDialog myDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agreement_activity);
		if (myDialog == null) {
			myDialog = MyDialog.createDialog(AgreementActivity.this,"正在加载中...");
		}
		NetState connected = NetReceiver.isConnected(this);
		if (connected == connected.NET_NO) {
			Toast.makeText(this, "当前网络不可用", Toast.LENGTH_SHORT).show();
		}
		intentView();
		getWebView();
		MyApplication.getInstance().addActivity(this);
	}

	// 初始化活动所需控件
	private void intentView() {
		// TODO Auto-generated method stub
		webView = (WebView) findViewById(R.id.agreenment_webView);
		title_yonhuzhuce = (View) findViewById(R.id.title_yonhuzhuce);
		title_yonhuzhuce.setOnClickListener(this);
	}

	// WebView加载H5资源
	private void getWebView() {
		myDialog.show();
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.loadUrl(HttpConfig.URL_REGISTRATIONPROTOCOL);
		// 使网页用WebView打开
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				if(myDialog!=null){
				   myDialog.dismiss();
				}
			}
			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				handler.proceed();//接受ssl证书
				super.onReceivedSslError(view, handler, error);
			}
		});
	}
    //所有控件的监听事件
	@Override
	public void onClick(View arg0) {
	// TODO Auto-generated method stub
	 switch(arg0.getId()){
	 case R.id.title_yonhuzhuce:
		 AgreementActivity.this.finish();
		 break;
	 }
	}
}
