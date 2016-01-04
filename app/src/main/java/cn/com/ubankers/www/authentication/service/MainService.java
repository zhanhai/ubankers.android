package cn.com.ubankers.www.authentication.service;

import java.io.File;
import java.lang.reflect.Type;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.authentication.controller.activity.LoginActivity;
import cn.com.ubankers.www.authentication.controller.activity.MainActivity;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.http.ParseUtils;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.model.UserNewBean;
import cn.com.ubankers.www.utils.UpdateUtils;
import cn.com.ubankers.www.widget.ProcessDialog;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainService {
	private Context context;
	private String userInfo;
	private Intent intent = null;
	private ProcessDialog myDialog;
	private UserBean userBean;
	private AsyncHttpClient client;
	private File tokenFile;
	private File headerFile;
	private SharedPreferences preferences;

	public MainService(Context context,int flag) {
		super();
		this.context = context;
		client = MyApplication.app.getClient(context);
		
		if(((MainActivity)context).type){
			tokenFile = new File("/data/data/" + context.getPackageName().toString()
					 + "/shared_prefs", "token.xml");
		    headerFile = new File("/data/data/" + context.getPackageName().toString()
				 + "/shared_prefs", "header.xml");
		    if(tokenFile.exists()){
				 preferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
			    userInfo = preferences.getString("userInfo", "");
			    if(userInfo!=null&&!userInfo.equals("")){
			    	Type type = new TypeToken<UserNewBean>(){}.getType();
			 		UserNewBean userNewBean =ParseUtils.parseUserInfo(userInfo, type);
			 	    userBean = ParseUtils.parseUserBean(userNewBean,context);
			 	    MyApplication.app.setUser(userBean);
			 	    
			 	   if(flag==0){
				 	    matinLogin(); 
				 	}
			    } 
		    }
		    
//		    if(headerFile.exists()){
//			    if(Tools.getCookie(context)!=null&&!Tools.getCookie(context).equals("")){
//				 	client.addHeader("Cookie",Tools.getCookie(context));
				 	    
				 	
//			    }
//		 	 }
		}
		UpdateUtils utils = new UpdateUtils(context, client);
		utils.getUpdateData(0);
	 	
	} 
	/**
	 * maintain login
	 */
	public void matinLogin(){
		    if(userBean!=null&&userBean.getUserId()!=null&&userBean.getLoginToken()!=null){
		    myDialog = ProcessDialog.createDialog(context, "正在加载中...");
		    myDialog.show();
			RequestParams params = new RequestParams();
			params.put("loginToken", userBean.getLoginToken());
			params.put("userId", userBean.getUserId());
			client.get(HttpConfig.URL_MAIN_LOGIN, params, new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					try {
						boolean flag = response.getBoolean("success");
						JSONObject obj = response.getJSONObject("result");
						if (flag&& obj.getString("errorCode").equals("success")){ 	
							
		                }else if(obj.getString("errorCode").equals("objectIsNull")){
		                	 Toast.makeText(context, "用户为空", Toast.LENGTH_SHORT).show();
		                }else if(obj.getString("errorCode").equals("tokenIsNull")){
		                	 Toast.makeText(context,"原令牌为空", Toast.LENGTH_SHORT).show();
		                }else if(obj.getString("errorCode").equals("tokenIsExpired")){
		                	 Toast.makeText(context,"令牌过期", Toast.LENGTH_SHORT).show();
		                	 intent = new Intent(context,LoginActivity.class);
		                	 context.startActivity(intent);
		                	 ((LoginActivity)context).finish();
		                }else if(obj.getString("errorCode").equals("tokenIsError")){
						  Toast.makeText(context, "令牌有误", Toast.LENGTH_SHORT).show();
					    }
						myDialog.dismiss();
		            }catch(Exception e){
		            	e.printStackTrace();
		            	myDialog.dismiss();
		            }
              }
				public void onFailure(int statusCode, Header[] headers, 
						Throwable throwable, JSONObject errorResponse) {
					Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
					myDialog.dismiss();
					};
					
					
          });
	}
	}
}