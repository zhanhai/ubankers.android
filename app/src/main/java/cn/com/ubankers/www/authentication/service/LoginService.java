package cn.com.ubankers.www.authentication.service;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.authentication.controller.activity.ChooseRoleActivity;
import cn.com.ubankers.www.authentication.controller.activity.LoginActivity;
import cn.com.ubankers.www.authentication.model.BindBean;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.http.ParseUtils;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.model.UserNewBean;
import cn.com.ubankers.www.utils.Tools;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class LoginService {
	private Context context;
	private AsyncHttpClient client;

	public LoginService(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		client = MyApplication.app.getClient(context);
	}
  /**
   *  normal login 
   */
	public void getLogin(){
//		if(((LoginActivity)context).myDialog!=null){
//			((LoginActivity)context).myDialog.show();
//		}
		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(16[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
		String password = ((LoginActivity)context).password.getText().toString();
		String name =((LoginActivity)context).userName.getText().toString();
		((LoginActivity)context).setUsername(name);
		Matcher m = p.matcher(name);
		if (!m.matches()) {
			Toast.makeText(context, "用户名不正确", Toast.LENGTH_SHORT).show();
			
		} else if (password == ""|| password.equals("")) {
			Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT).show();
			
		} else {
			RequestParams params = new RequestParams();
			params.put("loginName", name);
			params.put("password", password);
			params.put("verificationCode", "1212");
			client.setUserAgent("android/"+Tools.getVersion(context));
			if(((LoginActivity)context).myDialog!=null){
				((LoginActivity)context).myDialog.show();
			}
			client.post(HttpConfig.URL_LOGIN, params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					//headers
					try {
						JSONObject obj = response.getJSONObject("result");
						boolean flag = response.getBoolean("success");						
						
						if (flag&& obj.getString("errorCode").equals("success")) {
							if(response!=null){
								saveUserInfo(response);	
							}
						} else{
							if (obj.getString("errorCode").equals(
									"nameIsNull")) {
								Toast.makeText(context, "用户名为空",Toast.LENGTH_SHORT).show();
								((LoginActivity)context).myDialog.dismiss();
							} else if (obj.getString("errorCode").equals("passwordIsNotTrue")) {
								Toast.makeText(context, "密码不正确",Toast.LENGTH_SHORT).show();
								((LoginActivity)context).myDialog.dismiss();
							} else if (obj.getString("errorCode").equals("unactiveuser")) {
								Toast.makeText(context, "用户未激活",Toast.LENGTH_SHORT).show();
								((LoginActivity)context).myDialog.dismiss();
							} else if (obj.getString("errorCode").equals("passwordIsNull")) {
								Toast.makeText(context, "密码为空",Toast.LENGTH_SHORT).show();
								((LoginActivity)context).myDialog.dismiss();
							} else if (obj.getString("errorCode").equals("nameNotExist")) {
								Toast.makeText(context, "用户名不存在",Toast.LENGTH_SHORT).show();
								((LoginActivity)context).myDialog.dismiss();
						  }
							((LoginActivity)context).myDialog.dismiss();
					   }
					} catch (Exception e) {
						e.printStackTrace();
					} finally {

					}
				}
				@Override
				public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
					super.onFailure(statusCode, headers, throwable, errorResponse);
					Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
					((LoginActivity)context).myDialog.dismiss();
				}
			});
		}
	}
	/**
	 * third login检查第三方登录是否绑定
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
		client.setUserAgent("android/"+Tools.getVersion(context));
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
//						Intent intent = null;
						Bundle bundle = new Bundle();
						if(info!=null&&!info.equals("-1")){	
							saveUserInfo(response);
						}else{
							((LoginActivity)context).myDialog.dismiss();
							((LoginActivity)context).finish();
//							intent = new Intent(context,BindActivity.class);
//							bundle.putSerializable("bundleBean", bundleBean);
//							intent.putExtras(bundle);
						}
//						context.startActivity(intent);	
					}else{
						
					}
				} catch (Exception e) {
					e.printStackTrace();
					((LoginActivity)context).myDialog.dismiss();
				} finally {	
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
				((LoginActivity)context).myDialog.dismiss();
			}						
		});
		
		
	}
	/**
	 * save user information
	 * @param response
	 */
		private void saveUserInfo(JSONObject response) {
					JSONObject obj;
					try {
						obj = response.getJSONObject("result");
						boolean flag = response.getBoolean("success");
						String errCode = obj.getString("errorCode");
						if (flag && errCode.equals("success")) {
							JSONObject json = obj.getJSONObject("info");
						    Type type = new TypeToken<UserNewBean>(){}.getType();
						     UserNewBean userNewBean =ParseUtils.parseUserInfo(json.toString(), type);
					         UserBean userBean = ParseUtils.parseUserBean(userNewBean,context);	    
						      MyApplication.app.setUser(userBean);
						      MyApplication.app.setClient(client);
							if (userBean != null) {
								Bundle bundle = new Bundle();
								Intent intent = new Intent();
								if(userBean!=null&&userBean.getUserRole()!=null&&!userBean.getUserRole().equals("tourist")){
									((LoginActivity)context).finish();
//									intent.setClass(context, MainActivity.class);
//									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									ParseUtils.saveSharePreferences(context,json.toString());
								}else{
									intent.setClass(context, ChooseRoleActivity.class);
									 context.startActivity(intent);
									((LoginActivity)context).finish();
								}
								bundle.putSerializable("userBean", userBean);
//								intent.putExtras(bundle);
//								 context.startActivity(intent);
								((LoginActivity)context).finish();
							}
							Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}
		
		
}
