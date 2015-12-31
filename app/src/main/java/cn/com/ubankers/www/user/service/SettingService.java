package cn.com.ubankers.www.user.service;

import java.io.File;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.authentication.controller.activity.LoginActivity;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.controller.activity.AboutOursActivity;
import cn.com.ubankers.www.user.controller.activity.SafetyCenterActivity;
import cn.com.ubankers.www.user.controller.activity.SettingActivity;
import cn.com.ubankers.www.user.controller.activity.UserCenterActivity;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

public class SettingService {
	private Context context;
	private Platform qq, weixin, weibo;
	private boolean isExit;   
	/**SharedPreferences中储存数据的路径**/  
    public final static String DATA_URL = "/data/data/";  
    public final static String SHARED_TOKEN_XML = "token.xml";  
    public final static String SHARED_COOKIE_XML = "header.xml";  

	public SettingService(Context context,boolean isExit) {
		super();
		this.context = context;
		this.isExit = isExit;
	}

	/*
	 * 关于我们
	 * */
	public void intentToAbourt() {
		Intent intent = new Intent(context, AboutOursActivity.class);
		context.startActivity(intent);
	}

	public void intentToSafety() {
		Intent intent = new Intent(context, SafetyCenterActivity.class);
		context.startActivity(intent);
	}

	public void logout() {
		((SettingActivity) context).client.get(HttpConfig.URL_LOGOUT,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							JSONObject jsonObject = response.getJSONObject("result");
							String errorCode = jsonObject.getString("errorCode");
							boolean flag = response.getBoolean("success");
							if (flag && errorCode.equals("success")) {
								isExit = true;
								doLogout();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch
							// block
							e.printStackTrace();
						}
					}
					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable, errorResponse);
						Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
					}
				});
	}
    //退出登录的操作
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
	        Intent roleIntent = new Intent("ACTION_NAME"); 								
            context.sendBroadcast(roleIntent);
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
	}
}
