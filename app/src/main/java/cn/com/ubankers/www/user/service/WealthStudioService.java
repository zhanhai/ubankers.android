package cn.com.ubankers.www.user.service;

import java.lang.reflect.Type;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.http.ParseUtils;
import cn.com.ubankers.www.user.controller.activity.UserCenterActivity;
import cn.com.ubankers.www.user.controller.activity.WealthStudioActivity;
import cn.com.ubankers.www.user.model.AttributesBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.model.UserNewBean;
import cn.com.ubankers.www.user.model.WealthBean;
import cn.com.ubankers.www.utils.XutilsHttp;
import cn.com.ubankers.www.widget.MyDialog;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class WealthStudioService {
	private AsyncHttpClient client;
	private Context context;
	private String userUrl;
	
	private WealthBean wealthBean;
	private MyDialog myDialog;
	private Intent intent = null;
	private UserNewBean user;
	public UserBean userBean ;
	
	public WealthStudioService(Context context) {
		this.context = context;
		if (myDialog == null) {
			myDialog = new MyDialog(context);
		}
		client = MyApplication.app.getClient(context);
		if(MyApplication.app.getUser()!=null){
			userBean=MyApplication.app.getUser();
		}
		
	}
	
	public void initData() {
		myDialog.show();
		client.get(context, HttpConfig.URL_USER_INFO, null, "application/json",
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {	
							if (commonJSON(response)) {
								 JSONObject info = response.getJSONObject("result").getJSONObject("user");
								if (!info.isNull("extAttributes")) {
									JSONArray extAttributes = info.getJSONArray("extAttributes");
									Type type = new TypeToken<UserNewBean>() {}.getType();
									user = ParseUtils.parseUserInfo(info.toString(), type);
								 }
								showUserInfo(user);
							}
						} catch (Exception e) {
							e.printStackTrace();
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
		myDialog.dismiss();
	}
	
	private boolean commonJSON(JSONObject response){
		boolean status=false;
		try {
			JSONObject obj= response.getJSONObject("result");
			boolean flag = response.getBoolean("success");
			String errCode = obj.getString("errorCode");
			if(flag && errCode.equals("success")){
				status=true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
		
	}
	
	private void showUserInfo(UserNewBean userBean) {		
		for (int i = 0; i < userBean.getExtAttributes().size(); i++) {
			AttributesBean attributesBean = userBean.getExtAttributes().get(i);
			getUserFace(attributesBean);
		}
	}
	
	private void getUserFace(AttributesBean attributesBean) {
		// 显示用户头像
		if (attributesBean != null && attributesBean.getName() != null&& attributesBean.getName().equals("fileid")&&attributesBean.getCatalog().equals("userface")) {
//			MyApplication.loadImage(HttpConfig.HTTP_IMAGE_QUERY_URL+ attributesBean.getValue(), ((WealthStudioActivity) context).faceImage,null);
			new XutilsHttp(context).display(((WealthStudioActivity) context).faceImage,HttpConfig.HTTP_IMAGE_QUERY_URL+ attributesBean.getValue());
			((WealthStudioActivity) context).faceImage.setBackgroundResource(R.drawable.personal_center);
		}
	}

}
