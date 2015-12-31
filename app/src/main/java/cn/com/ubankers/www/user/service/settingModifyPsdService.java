package cn.com.ubankers.www.user.service;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.widget.Toast;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.controller.activity.SettingModifyPsdActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class settingModifyPsdService {
	
	
	private Activity activity;
	private AsyncHttpClient client;
	private String oldPassword;
	private String Password;

	public settingModifyPsdService(Activity activity,String oldPassword,String Password){
		this.activity=activity;
	    this.oldPassword=oldPassword;
	    this.Password=Password;
	    client = MyApplication.app.getClient(activity);
	}
	//修改密码请求
	public void ChangePassword(){
	String	mimaUrl =HttpConfig.HTTP_QUERY_URL+"/user/ajax/user/modifyUserPwd";
		StringEntity entity=null;
		try {    
			JSONObject json = new JSONObject();
			json.put("oldPassword",oldPassword);
			json.put("password",Password);
			entity =new StringEntity(json.toString());
		} catch (Exception e1) {
				e1.printStackTrace();
		}
		client.post(activity,mimaUrl,entity,"application/json", new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				try {
					boolean yey=response.getBoolean("success");
					JSONObject jin=response.getJSONObject("result");
					String errorCode=jin.getString("errorCode");
					if(errorCode.equals("success")){
						Toast.makeText(activity, "密码修改成功", Toast.LENGTH_SHORT).show();
						activity.finish();
					}else if(errorCode.equals("passwordIsNotTrue")){				
						Toast.makeText(activity, "原始密码输入不正确", Toast.LENGTH_SHORT).show();
					}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				super.onSuccess(statusCode, headers, response);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				System.out.println("失败");
				super.onFailure(statusCode, headers, responseString, throwable);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(activity, "请求超时，请重试", Toast.LENGTH_SHORT).show();
			}
		});
		
	}

}
