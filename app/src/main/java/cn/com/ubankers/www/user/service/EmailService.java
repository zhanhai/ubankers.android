package cn.com.ubankers.www.user.service;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.controller.activity.EmailActivity;
import cn.com.ubankers.www.user.controller.activity.SafetyCenterActivity;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.utils.PhotoUtil;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class EmailService {
	private String userUrl;
	private Activity activity;
	private AsyncHttpClient client;
	private UserBean userBean;
	private String str;
	
	
	
public	EmailService(Activity activity ,UserBean userBean){
	this.activity=activity;
	this.userBean=userBean;
	client = MyApplication.app.getClient(activity);
	
}
	
	public void initData(String email){
		 userUrl =HttpConfig.HTTP_QUERY_URL+"/user/ajax/user/bindemail/email/binding/"+PhotoUtil.StringtoString(email);
		 client.get(userUrl, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				 
				// TODO Auto-generated method stub		
				 try {
						boolean yey=response.getBoolean("success");
						JSONObject jin=response.getJSONObject("result");
						String errorCode=jin.getString("errorCode");
						if(errorCode.equals("success")){
							onBackPressedlg("邮件已发送，请去邮箱激活");
							Toast.makeText(activity, "邮件已发送", Toast.LENGTH_SHORT).show();				
						}else if(errorCode.equals("emailExist")){
							Toast.makeText(activity, "该邮箱已被绑定", Toast.LENGTH_SHORT).show();
							onBackPressedlg("该邮箱已被绑定");
						}
				 
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	public void onBackPressedlg(final String str) { 
		this.str=str;
        new AlertDialog.Builder(activity).setTitle(str) 
            .setIcon(android.R.drawable.ic_dialog_info) 
            .setNegativeButton("确     定", new DialogInterface.OnClickListener() { 
         
                @Override 
                public void onClick(DialogInterface dialog, int which) { 
                // 点击“返回”后的操作,这里不设置没有任何操作
                	if(str.equals("邮件已发送，请去邮箱激活")){
                		activity.finish();
                	}else if(str.equals("该邮箱已被绑定")){
                		
                	}
                	
                } 
            }).show(); 
           } 
}
