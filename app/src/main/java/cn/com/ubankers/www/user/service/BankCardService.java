package cn.com.ubankers.www.user.service;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.controller.activity.BankCardActivity;
import cn.com.ubankers.www.user.controller.activity.SafetyCenterActivity;
import cn.com.ubankers.www.widget.MyDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class BankCardService {
	private Context context;
	private AsyncHttpClient client;
	private MyDialog myDialog;

	public BankCardService(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
		client = MyApplication.app.getClient(context);
		if(myDialog==null){
			 myDialog=MyDialog.createDialog(context,"正在加载中...");
			}
	}
	
	public void BankCardCheck(final String yinghangkahao){
		client.get(HttpConfig.URL_BANK_CARD_CHECK+yinghangkahao,new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				try {
					boolean success = response.getBoolean("success");
					JSONObject result=response.getJSONObject("result");
				     String	errorCode=result.getString("errorCode");
					if(success){
					     if(errorCode.equals("success")){
					    	 UpBankCard(yinghangkahao);
					     }
					}else{
						if(errorCode.equals("bankcardNumber_not_supported")){
					    	 Toast.makeText(context, "银行卡不支持", 0).show();
					     }else if(errorCode.equals("bankcardNumber_length_error")){
					    	 Toast.makeText(context, "银行卡长度不正确", 0).show();
					     }else{
					    	 Toast.makeText(context, errorCode, 0).show();
					     }
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
				super.onFailure(statusCode, headers, responseString, throwable);
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
	
	public void UpBankCard(final String yinghangkahao) {
		myDialog.show();
		StringEntity entity = null;
		try {
			JSONArray json1 = new JSONArray();
			JSONObject jsonkh = new JSONObject();
			jsonkh.put("value", yinghangkahao);
			jsonkh.put("name", "no");
			jsonkh.put("catalog", "bankcard");
			jsonkh.put("userId", "");
			json1.put(jsonkh);
			JSONObject jsonzt = new JSONObject();
			jsonzt.put("value",0);
			jsonzt.put("name", "status");
			jsonzt.put("catalog", "bankcard");
			jsonzt.put("userId", "");
			json1.put(jsonzt);
			entity = new StringEntity(json1.toString());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		client.post(context, HttpConfig.URL_AUTHENTICATION, entity,"application/json", new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						try {
							boolean yey = response.getBoolean("success");
							JSONObject jin = response.getJSONObject("result");
							String errorCode = jin.getString("errorCode");
							System.out.println("errorCode++" + errorCode);
							if (errorCode.equals("success")) {
								Toast.makeText(context,"申请成功，请耐心等待审核结果", Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(context, SafetyCenterActivity.class);
								intent.putExtra("bankcard","0");
								((BankCardActivity) context).finish();
								myDialog.dismiss();
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
						myDialog.dismiss();
						super.onFailure(statusCode, headers, responseString,
								throwable);
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
	}

}
