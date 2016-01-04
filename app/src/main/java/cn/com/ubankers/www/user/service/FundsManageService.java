package cn.com.ubankers.www.user.service;

import java.lang.reflect.Type;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.http.ParseUtils;
import cn.com.ubankers.www.user.controller.activity.FundsManageActivity;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.model.UserNewBean;
import cn.com.ubankers.www.utils.Util;
import cn.com.ubankers.www.widget.ProcessDialog;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Context;
import android.widget.Toast;

public class FundsManageService {
	
	private Context context;
	private UserBean userBean;
	private AsyncHttpClient client;
	private ProcessDialog myDialog;
	

	public FundsManageService( Context context){
		this.context=context;
		if(MyApplication.app.getUser()!=null){
			userBean=MyApplication.app.getUser();
		}
		client = MyApplication.app.getClient(context);
		if(myDialog==null){
			   myDialog= ProcessDialog.createDialog(context, "正在加载中...");
			}
		GetUser();
	}

	
	public void GetUser() {
		myDialog.show();
		client.get(context, HttpConfig.URL_USER_INFO, null, "application/json",
				new JsonHttpResponseHandler() {
					

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {	
								 JSONObject info = response.getJSONObject("result").getJSONObject("user");
								if (!info.isNull("extAttributes")) {
									JSONArray extAttributes = info.getJSONArray("extAttributes");
									Type type = new TypeToken<UserNewBean>() {}.getType();
									UserNewBean user = ParseUtils.parseUserInfo(info.toString(), type);
								    userBean = ParseUtils.parseUserBeanlg(user,context);
									MyApplication.app.setUser(userBean);
								    myDialog.dismiss();
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
						Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
						myDialog.dismiss();
					}
				});
		
	}
	
	public void initData(){
		myDialog.show();
		client.get( HttpConfig.URL_ACCOUNT_BALANCE+userBean.getUserId(), new JsonHttpResponseHandler(){	
			

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					boolean yey=response.getBoolean("success");
					
					if(yey){
						JSONObject	result=response.getJSONObject("result");
						JSONObject info=result.getJSONObject("info");
						String  totalAmount=info.getString("totalAmount"); 
						String totalBalance = info.getString("totalBalance");
						((FundsManageActivity) context).account_balance.setText(Util.Intercept1(totalBalance)+"元");
						myDialog.dismiss();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					myDialog.dismiss();
					e.printStackTrace();
				}
				
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				myDialog.dismiss();
				super.onFailure(statusCode, headers, responseString, throwable);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
				myDialog.dismiss();
			}
		});
		
//		http://uat.ubankers.com/pay/api/payGateway/queryLastUserAmount/107994
	}
}
