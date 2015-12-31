package cn.com.ubankers.www.user.controller.activity;




import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.utils.NetReceiver;
import cn.com.ubankers.www.utils.NetReceiver.NetState;
import  cn.com.ubankers.www.widget.MyDialog;
import android.app.Activity;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class AboutOursActivity extends Activity implements OnClickListener{
	private WebView webView;
	private MyDialog progressDialog;
	private View aboutours_yonhuzhuce;
	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutours_activity);
		NetState connected = NetReceiver.isConnected(this);
		if(progressDialog==null){
			progressDialog=MyDialog.createDialog(AboutOursActivity.this,"正在加载中...");
		}
		
		initView();
		
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
       webView.loadUrl(HttpConfig.URL_ABOUT);
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
            public void onReceivedSslError(WebView view, SslErrorHandler handler,
            	SslError error) {
            	handler.proceed();
            	super.onReceivedSslError(view, handler, error);
            }
        });
 	}
	private void initView() {
		// TODO Auto-generated method stub
		webView= (WebView)findViewById(R.id.aboutours_webView);
		aboutours_yonhuzhuce=(View) findViewById(R.id.aboutours_yonhuzhuce);
		aboutours_yonhuzhuce.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.aboutours_yonhuzhuce:
			AboutOursActivity.this.finish();
			break;
		
		}
	}
}
