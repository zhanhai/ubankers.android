package cn.com.ubankers.www.sns.controller.activity;




import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.sns.model.ArticleBean;
import cn.com.ubankers.www.utils.NetReceiver;
import cn.com.ubankers.www.utils.Tools;
import cn.com.ubankers.www.utils.NetReceiver.NetState;
import cn.com.ubankers.www.widget.ProcessDialog;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CommentActivity<MessageAdapter> extends Activity implements OnClickListener{
	public static EditText comment;
	public static Button release,reply;
	private static ArticleBean articleBean;
	public static Context context;
	public static  LinearLayout publish_comment;
	private LinearLayout title_bar_back_btn;
	private static ProcessDialog progressDialog;
	private WebView webView;
	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment);
		context = this;
		MyApplication.app.addActivity(this);
		Intent intent = getIntent();
		articleBean = (ArticleBean) intent.getSerializableExtra("articleBean");
		initView();
		if(progressDialog==null){
			progressDialog= ProcessDialog.createDialog(CommentActivity.this, "正在加载中...");
		}else{
			progressDialog= ProcessDialog.createDialog(CommentActivity.this, "正在加载中...");
		}
		NetState connected = NetReceiver.isConnected(this);
		if(connected==connected.NET_NO){
			Toast.makeText(this, "当前网络不可用",Toast.LENGTH_SHORT).show();
		}else{
			getWebView();
		}
	}
	private void initView() {
		title_bar_back_btn = (LinearLayout) findViewById(R.id.title_bar_back_btn);
		title_bar_back_btn.setOnClickListener(this);
		comment = (EditText) findViewById(R.id.comment);
		webView =(WebView) findViewById(R.id.webView);
		WebSettings settings = webView.getSettings();
	    String userAgent = settings.getUserAgentString();
		settings.setUserAgentString(userAgent + " ronganapp");
	}
	// WebView加载H5资源
	@SuppressLint("SetJavaScriptEnabled")
	private void getWebView() { 
		progressDialog.show();
		//支持缩放
		webView.getSettings().setSupportZoom(true);
		//支持缩放
		webView.getSettings().setBuiltInZoomControls(true);
		//设置此属性，可任意比例缩放
		webView.getSettings().setUseWideViewPort(true);
		//webview必须设置支持Javascript
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.requestFocus();
		//给webView设置cookie
		Tools.synCookies(context,HttpConfig.HTTP_QUERY_URL);
//		Tools.synCookies(context,HttpConfig.HTTP_QUERY_URL+"/sns/_escape_app/articleComment/"+articleBean.get_id());
		webView.loadUrl(HttpConfig.HTTP_QUERY_URL+"/sns/_escape_app/articleComment/"+articleBean.get_id());
		MWebViewClient mWebViewClient = new MWebViewClient(webView,
				getApplicationContext(),progressDialog);
		webView.setWebViewClient(mWebViewClient);
		MWebChromeClient mWebChromeClient = new MWebChromeClient(
				getApplicationContext());
		webView.setWebChromeClient(mWebChromeClient);
		
	}

	@Override
	public void onClick(View view) {
		if(view.getId()==R.id.title_bar_back_btn){
			finish();
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		 webView.goBack();
		 deleteDatabase("webview.db");  
         deleteDatabase("webviewCache.db"); 
	}
}
