package cn.com.ubankers.www.utils;

import java.io.File;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.authentication.controller.activity.LoginActivity;
import cn.com.ubankers.www.authentication.controller.activity.MainActivity;
import cn.com.ubankers.www.authentication.controller.activity.SupplinfoActivity;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.product.controller.activity.ProductActivity;
import cn.com.ubankers.www.sns.controller.activity.SnsActivity;
import cn.com.ubankers.www.user.controller.activity.SettingActivity;
import cn.com.ubankers.www.user.controller.activity.UserCenterActivity;
import cn.com.ubankers.www.user.controller.activity.WealthStudioActivity;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.service.SettingService;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class CompleteDialog {
	private  Context context;
	private  AlertDialog builder;
	private TextView completeView,exit;
	private  UserBean userBean;
	private SettingService settingService;
	private boolean isExit; 
	private AsyncHttpClient client;
	public final static String DATA_URL = "/data/data/";  
    public final static String SHARED_TOKEN_XML = "token.xml";  
    public final static String SHARED_COOKIE_XML = "header.xml";  
    private Platform qq, weixin, weibo;
    private LinearLayout llback;
    private ImageView back;


	public CompleteDialog(Context context,UserBean userBean) {
		super();
		this.context = context;
		this.userBean =userBean;
		client = MyApplication.app.getClient(context);
	}

	public void  createDialog(final int type) {
	    builder = new AlertDialog.Builder(context).create();
	    builder.show();
	    builder.setCancelable(false);
	    Window window = builder.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		window.setGravity(Gravity.CENTER);
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		window.setAttributes(lp);
		window.setContentView(R.layout.complete_dialog);		
//      View view = LayoutInflater.from(context).inflate(R.layout.complete_dialog, null);
//      View view = LayoutInflater.from(context).inflate(R.layout.commplete_dialog, null);
        completeView = (TextView) window.findViewById(R.id.completeView);
        exit = (TextView)window.findViewById(R.id.exit);
        llback = (LinearLayout)window.findViewById(R.id.llback);
        back = (ImageView)window.findViewById(R.id.back);
        llback.setOnClickListener(new back());
        exit.setOnClickListener(new exit());
        completeView.setOnClickListener(new CompleteClickListener());
        builder.setCanceledOnTouchOutside(false);
        builder.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
				if(keyCode==KeyEvent.KEYCODE_BACK&&type==1){
					Intent intent=new Intent(context, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intent);
					return true;
				  }
				return false;
			}
		});
//        builder.setView(view);
       
	}
	private  class CompleteClickListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			 builder.dismiss();
			 Intent intent = new Intent(context, SupplinfoActivity.class);
			 Bundle bundle = new Bundle();
			 bundle.putSerializable("bundleBean", userBean);
			 intent.putExtras(bundle);
			 context.startActivity(intent);
		}
		
	}
	
	private class back implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(context, MainActivity.class);
			context.startActivity(intent);
		}
		
	}
	
	private class exit implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub	
			client.get(HttpConfig.URL_LOGOUT,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							try {
								JSONObject jsonObject = response
										.getJSONObject("result");
								String errorCode = jsonObject
										.getString("errorCode");
								boolean flag = response.getBoolean("success");
								if (flag && errorCode.equals("success")) {
									isExit = true;
									doLogout();
									builder.dismiss();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch
								// block
								e.printStackTrace();
							}
						}
					});
			
		}
		
	}
	
	private void doLogout() {
		if (isExit == true) {
			MyApplication.app.setUser(null);
			MyApplication.app.setClient(null);
			UserCenterActivity.layout_cfmp.setVisibility(View.GONE);
			UserCenterActivity.layout_fundraiser.setVisibility(View.GONE);
			UserCenterActivity.layout_investor.setVisibility(View.GONE);
			/** 删除SharedPreferences文件 **/  
			File tokenFile = new File(DATA_URL + context.getPackageName().toString()
					 + "/shared_prefs", SHARED_TOKEN_XML);
			
			File cookieFile = new File(DATA_URL + context.getPackageName().toString()
					 + "/shared_prefs", SHARED_COOKIE_XML); 
			
	        if (cookieFile.exists()) {  
	        	cookieFile.delete();  
	        } 
	        if (tokenFile.exists()) {  
	        	tokenFile.delete();  
	        } 
		}
		qq = ShareSDK.getPlatform(QQ.NAME);
		weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
		weixin = ShareSDK.getPlatform(Wechat.NAME);
		// 已经授权
		if (weibo.isAuthValid()) {
			weibo.removeAccount(true);
		} else if (weixin.isAuthValid()) {
			weixin.removeAccount(true);
		} else if (qq.isAuthValid()) {
			qq.removeAccount(true);
		}
		ShareSDK.removeCookieOnAuthorize(true);
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);
		builder.dismiss();
	}
	public void setOnKeyListener(OnKeyListener onKeyListener) {
		// TODO Auto-generated method stub
		
	}
}
