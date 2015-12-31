package cn.com.ubankers.www.utils;

import java.lang.reflect.Type;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.authentication.controller.activity.BindActivity;
import cn.com.ubankers.www.authentication.controller.activity.LoginActivity;
import cn.com.ubankers.www.authentication.controller.activity.MainActivity;
import cn.com.ubankers.www.authentication.controller.activity.RegisterActivity;
import cn.com.ubankers.www.authentication.model.BindBean;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.http.ParseUtils;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.model.UserNewBean;
import cn.com.ubankers.www.widget.MyDialog;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mob.tools.utils.UIHandler;

public class LoginDialog extends Dialog implements OnClickListener,Callback ,PlatformActionListener  {
	private static final int MSG_ACTION_CCALLBACK = 2;
	private  int typeValue,type_all;
	private Context context;
	private BindBean bundBean;
	private  AlertDialog builder;
	private AsyncHttpClient client =null;
	public  MyDialog myDialog;
	private UserBean userBean;
	public LoginDialog(Context context,int type,int type_all) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.typeValue =type;
		this.type_all = type_all;
		ShareSDK.initSDK(context);
		if(myDialog==null){
			   myDialog=MyDialog.createDialog(context,"正在加载中...");
		}else{
			   myDialog=MyDialog.createDialog(context,"正在加载中...");
		}
		if(MyApplication.app.getUser()!=null){
			userBean=MyApplication.app.getUser();
		}
		client = MyApplication.app.getClient(context);
	}
    
	public void onLogin(){
	    builder = new AlertDialog.Builder(context).create();
		builder.show();
		builder.setCancelable(false);
		Window window = builder.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		window.setGravity(Gravity.CENTER);
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
//		lp.alpha = 0.85f;
		window.setAttributes(lp);
		window.setContentView(R.layout.login_dialog_layout);
		LinearLayout cancel =(LinearLayout) window.findViewById(R.id.cancel);
		TextView tv_register=(TextView) window.findViewById(R.id.register);
		TextView tv_login=(TextView) window.findViewById(R.id.login);
	    ImageView sinaweibo=(ImageView) window.findViewById(R.id.login_sinaweibo);
		ImageView login_wechat=(ImageView) window.findViewById(R.id.login_wechat);
		ImageView login_qq=(ImageView) window.findViewById(R.id.login_qq);
		cancel.setOnClickListener(this);
		tv_register.setOnClickListener(this);
	    tv_login.setOnClickListener(this);
		sinaweibo.setOnClickListener(this);
		login_wechat.setOnClickListener(this);
		login_qq.setOnClickListener(this);
		builder.setOnKeyListener(new DialogOnKeyListener());
	}
	/**
	 * Dialog 监听返回事件
	 * 
	 *
	 */
	 public class DialogOnKeyListener implements OnKeyListener {
		 
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0) {
					 if(typeValue==0){
							 Intent intent = new Intent(context,MainActivity.class);
							 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							 intent.putExtra("backType","back");
							 context.startActivity(intent); 
							 builder.dismiss();  
					 }else{
						 builder.dismiss();  
					 }
				}
				return false;
			}
	 
		}
    
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	   Intent intent=null;
	   switch(arg0.getId()){
	   case R.id.cancel:
		   this.dismiss();
		   if(typeValue==0){
			   builder.dismiss();  
			   intent = new Intent(context,MainActivity.class);
			   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			   intent.putExtra("backType","back");
			   context.startActivity(intent); 
		   }else{
			 builder.dismiss(); 
	       }
	 	   break;
	   case R.id.register:
		   this.dismiss();
		    intent = new Intent(context, RegisterActivity.class);
			context.startActivity(intent);
			builder.dismiss();
		   break;
	   case R.id.login:
		    this.dismiss();
		    intent = new Intent(context, LoginActivity.class);
			context.startActivity(intent);
			builder.dismiss();
		    break;
	  case R.id.login_sinaweibo:
		if(InstallAppUtils.isInstalled(context,"com.sina.weibo")==false){
				Toast.makeText(context, "请安装微博再登录", 1).show();
		}else{
			Platform sinaweibo = ShareSDK.getPlatform(SinaWeibo.NAME);
//			sinaweibo.SSOSetting(true);
//			sinaweibo.SSOSetting(true);
			this.dismiss();
			if (sinaweibo.isAuthValid()) {
				sinaweibo.removeAccount(true);
				ShareSDK.removeCookieOnAuthorize(true);}
				sinaweibo.setPlatformActionListener(this);
				sinaweibo.showUser(null);
				MyApplication.app.setUser(userBean);
				MyApplication.app.setClient(client);
				builder.dismiss();
				if(myDialog!=null){
					myDialog.show();
				}
				break;
		}
		break;
	  case R.id.login_wechat:
		  if(InstallAppUtils.isInstalled(context,"com.tencent.mm")==false){
				Toast.makeText(context, "请安装微信再登录", 1).show();
			}else{	
				 this.dismiss();
			Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
			 if (wechat.isAuthValid()) {
				 wechat.removeAccount(true);
				 ShareSDK.removeCookieOnAuthorize(true);} 
	      	wechat.setPlatformActionListener(this);
		    wechat.showUser(null);
		    wechat.authorize();
		   MyApplication.app.setUser(userBean);
			MyApplication.app.setClient(client);
		    builder.dismiss();
		    if(myDialog!=null){
				myDialog.show();
			}
			
			}
		  break;
	  case R.id.login_qq:
		  if(InstallAppUtils.isInstalled(context,"com.tencent.mobileqq")==false){
			  Toast.makeText(context, "请安装新qq再登录", 1).show();
			}else{
				 this.dismiss();
				Platform qq = ShareSDK.getPlatform(QQ.NAME);
				if (qq.isAuthValid()) {
					qq.removeAccount(true);
					ShareSDK.removeCookieOnAuthorize(true);}
					qq.setPlatformActionListener(this);
					qq.showUser(null);
					MyApplication.app.setUser(userBean);
					MyApplication.app.setClient(client);
				    builder.dismiss();
				    if(myDialog!=null){
						myDialog.show();
				}
				}
		  break;
	}
	   }

	 // 回调
	@Override
	public void onCancel(Platform platform, int action) {
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
	}
	/**
	 * 获取QQ，微信，新浪的信息
	 */
	@Override
	public void onComplete(Platform platform, int action,
			HashMap<String, Object> res) {
		String openId =null ;
		String nickName=null;
		BindBean  bean  = new BindBean();
		if (platform.getName().equals(QQ.NAME)) {
			 openId = platform.getDb().getUserId().toString();
			 nickName = platform.getDb().getUserName();
			 bean.setBundleId(openId);
			 bean.setPlatfromName("QQ");
			 bean.setPlatfromUserName(nickName);
		}else if(platform.getName().equals( SinaWeibo.NAME)) {
			 openId = platform.getDb().getUserId().toString();
			 nickName = platform.getDb().getUserName();
			 bean.setBundleId(openId);
			 bean.setPlatfromName("sina");
			 bean.setPlatfromUserName(nickName);
		}else if(platform.getName().equals(Wechat.NAME)){
			 openId=platform.getDb().get("unionid");//获取微信的unionid
			 nickName=res.get("nickname").toString();
			 bean.setBundleId(openId);
			 bean.setPlatfromName("weixin");
			 bean.setPlatfromUserName(nickName);
		}
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = bean;
		UIHandler.sendMessage(msg, this);
		System.out.println(res);
		// 获取资料
		platform.getDb().getUserName();// 获取用户名字
		platform.getDb().getUserIcon(); // 获取用户头像

	}
	// 分享失败的统计
	@Override
	public void onError(Platform platform, int action, Throwable t) {
		t.printStackTrace();
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = t;
		UIHandler.sendMessage(msg, this);	
	}
	// 回调handleMessage
				@Override
				public boolean handleMessage(Message msg) {
					switch (msg.arg1) {
					case 1: {
						  if(msg!=null){
							bundBean =  (BindBean) msg.obj; 
							BunldeVerify(bundBean);
						   }
					    }
						break;
					case 2: {
						Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show();// 失败
						if(typeValue==0){
							   builder.dismiss();  
							   Intent intent = new Intent(context,MainActivity.class);
							   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							   context.startActivity(intent);
							   myDialog.dismiss();
						   }else{
							   builder.dismiss(); 
							   myDialog.dismiss();
					       }
					}
						break;
					case 3: {
						Toast.makeText(context, "取消····", Toast.LENGTH_SHORT)// 取消
								.show();
						if(typeValue==0){
							   builder.dismiss();  
							   Intent intent = new Intent(context,MainActivity.class);
							   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							   context.startActivity(intent);
							   myDialog.dismiss();
						   }else{
							   builder.dismiss(); 
							   myDialog.dismiss();
					    }
					}
						break;
					}
					return false;
				}
				/**
				 * third login
				 * @param bundleBean
				 */
				private void BunldeVerify(final BindBean bundleBean){
					if(userBean==null){
						thirdLogin(bundleBean);
					}else{
						initData();
					}
				}
				/**
				 * third login
				 * @param response
				 */
				public void thirdLogin(final BindBean bundleBean){
					StringEntity entity =  null;
					try{
						JSONObject json = new JSONObject();
						json.put("openid", bundleBean.getBundleId());
						json.put("provider", bundleBean.getPlatfromName());
						entity = new StringEntity(json.toString(),"utf-8");
					}catch(Exception e){
						e.printStackTrace();
					}
					
					client.post(context, HttpConfig.URL_ChECKTHRIDVERIFY, entity, "application/json", new JsonHttpResponseHandler(){
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							try {
								boolean flag = response.getBoolean("success");
								JSONObject obj = response.getJSONObject("result");
								if (flag&& obj.getString("errorCode").equals("success")) {
									String info;
									try{
									    info =obj.getString("info");
									}catch (JSONException e) {
										e.printStackTrace();
										info =null;
									}
									Intent intent = null;
									Bundle bundle = new Bundle();
									if(info!=null&&info.equals("-1")){	
										myDialog.dismiss();
										intent = new Intent(context,BindActivity.class);
										bundle.putSerializable("bundleBean", bundleBean);
										bundle.putInt("type",typeValue);
										intent.putExtras(bundle);
										intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										context.startActivity(intent);	
									}else{
										 JSONObject json = obj.getJSONObject("info");
									     Type type = new TypeToken<UserNewBean>(){}.getType();
									     UserNewBean userNewBean =ParseUtils.parseUserInfo(json.toString(), type);
								         UserBean userBean = ParseUtils.parseUserBean(userNewBean,context);	
								         ParseUtils.saveSharePreferences(context,json.toString());
								         MyApplication.app.setUser(userBean);
										 MyApplication.app.setClient(client); 
										 for(int i=0;i<headers.length;i++){
												Log.e("...........", headers[i].getName()+"..."+headers[i].getValue());	
										          if(headers[i].getName().equals("Set-Cookie")) {
										        	  String  GSID  = null;
										        	  String name =headers[i].getName();
													  String value = headers[i].getValue();
													  if(value.contains("__GSID__")){ 								  
										        	  SharedPreferences preferences = context.getSharedPreferences("header", Context.MODE_PRIVATE);  
										        	  SharedPreferences.Editor editor = preferences.edit();
										        	  editor.putString("__GSID__",value);
										        	  editor.commit();
													 }
										          }
									          }
										 if(type_all==0){
												 intent = new Intent(context,MainActivity.class);
												 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
												 context.startActivity(intent);	
										 }
									}
								}else{
									
								}
							} catch (Exception e) {
								e.printStackTrace();
								//myDialog.dismiss();
							} finally {	
								myDialog.dismiss();
							}
						}
						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {	  
						}
						
					});

				}
				public void initData() {
					myDialog.show();
					client.get(context, HttpConfig.URL_USER_INFO, null, "application/json",
							new JsonHttpResponseHandler() {
								@Override
								public void onSuccess(int statusCode, Header[] headers,
										JSONObject response) {
									try {	
										Intent intent = null;
										boolean flag = response.getBoolean("success");
										JSONObject obj = response.getJSONObject("result");
										if (flag&& obj.getString("errorCode").equals("success")) {
											 JSONObject json = response.getJSONObject("result").getJSONObject("user");
											  Type type = new TypeToken<UserNewBean>(){}.getType();
											    UserNewBean userNewBean =ParseUtils.parseUserInfo(json.toString(), type);
										        UserBean userBean = ParseUtils.parseUserBean(userNewBean,context);	
										        ParseUtils.saveSharePreferences(context,json.toString());
										        MyApplication.app.setUser(userBean);
												MyApplication.app.setClient(client);

												/*Activity activity = (Activity) context;
											    activity.finish();*/
												 intent = new Intent(context, MainActivity.class);
												 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
												 context.startActivity(intent);	
											 }
									} catch (Exception e) {
										e.printStackTrace();
									}
									finally{
										myDialog.dismiss();
									}
								}

								@Override
								public void onFailure(int statusCode, Header[] headers,
										Throwable throwable, JSONObject errorResponse) {
									// TODO Auto-generated method stub
									super.onFailure(statusCode, headers, throwable,
											errorResponse);
									myDialog.dismiss();
								}
							});
					
				}
}
