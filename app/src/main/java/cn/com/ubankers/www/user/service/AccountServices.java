package cn.com.ubankers.www.user.service;

import java.util.ArrayList;


import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.controller.activity.AccountActivity;
import cn.com.ubankers.www.user.model.AccountBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.view.AccountAdapter;
import cn.com.ubankers.www.widget.ProcessDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Context;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class AccountServices {
	
	private Context context;
	private UserBean userBean;
	private AsyncHttpClient client;
	private ProcessDialog myDialog;
	private Handler handler;
	ArrayList<AccountBean> list =new ArrayList<AccountBean>();
	AccountAdapter AccountAdapter;
	public AccountServices(Context context,Handler handler){
		this.context=context;
		this.handler=handler;
		
		
		
		if(MyApplication.app.getUser()!=null){
			userBean=MyApplication.app.getUser();
		}
		client = MyApplication.app.getClient(context);
		if(myDialog==null){
			   myDialog= ProcessDialog.createDialog(context, "正在加载中...");
			}
	}
	//账户流水记录
	public void initData(final int i){
		
		myDialog.show();
		
		StringEntity entity=null;
		try {	
			JSONObject json = new JSONObject();
			json.put("businessType",0);
			json.put("start",i);
			json.put("limit",15);
			entity =new StringEntity(json.toString());
		} catch (Exception e1) {
				e1.printStackTrace();
		}
		
//		{"bid":124174,"tid":0,"ownerType":"USER","ownerId"
//			:107994,"description":"","amount":100000,"status":"SUCCESS","businessType":"WITHDRAWAL","startTime":1443575371000
//			,"finishTime":1443575392000,"balance":8474750,"summary":"扣除提现费用0元","productName":null,"success":false
//			}
		client.post(context,HttpConfig.RUNNING_WATER, entity,"application/json",new JsonHttpResponseHandler(){

			private ArrayAdapter<String> mAdapter;

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
				boolean	success=response.getBoolean("success");
				
				if(success){
					JSONObject	result=response.getJSONObject("result");
					JSONObject	info=result.getJSONObject("info");
					int count=info.getInt("count");
					JSONArray data=info.getJSONArray("data");
					for (int i = 0; i < data.length(); i++) {
						AccountBean accountBean=new AccountBean();
						JSONObject	object= data.getJSONObject(i);
						accountBean.setDescription(object.getString("description"));
						accountBean.setStatus(object.getString("status"));
						accountBean.setAmount(object.getInt("amount"));
						accountBean.setStartTime(object.getLong("startTime"));
						accountBean.setType(object.getString("businessType"));
						list.add(accountBean);
					   }
					
					if( data.length()==0){
						Toast.makeText(context, "没有更多数据", 0).show();
					}else{
						if(AccountAdapter==null){
							 AccountAdapter=new AccountAdapter(list, context);
						}
						 AccountAdapter.notifyDataSetChanged();
						 if(i==1){
							 ((AccountActivity) context).account_listView1.setAdapter(AccountAdapter);
						 }
						
					}
					
					myDialog.dismiss();
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
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
		
		
	}
	
	
   public void initDatasx(final int i){
		list.clear();
		myDialog.show();
		StringEntity entity=null;
		try {	
			JSONObject json = new JSONObject();
			json.put("businessType",0);
			json.put("start",i);
			json.put("limit",15);
			entity =new StringEntity(json.toString());
		} catch (Exception e1) {
				e1.printStackTrace();
		}
		
//		{"bid":124174,"tid":0,"ownerType":"USER","ownerId"
//			:107994,"description":"","amount":100000,"status":"SUCCESS","businessType":"WITHDRAWAL","startTime":1443575371000
//			,"finishTime":1443575392000,"balance":8474750,"summary":"扣除提现费用0元","productName":null,"success":false
//			}
		client.post(context,HttpConfig.RUNNING_WATER, entity,"application/json",new JsonHttpResponseHandler(){

			private ArrayAdapter<String> mAdapter;

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
				boolean	success=response.getBoolean("success");
					if(success){
					JSONObject	result=response.getJSONObject("result");
					JSONObject	info=result.getJSONObject("info");
					int count=info.getInt("count");
					JSONArray data=info.getJSONArray("data");
					for (int i = 0; i < data.length(); i++) {
						AccountBean accountBean=new AccountBean();
						JSONObject	object= data.getJSONObject(i);
						accountBean.setDescription(object.getString("description"));
						accountBean.setStatus(object.getString("status"));
						accountBean.setAmount(object.getInt("amount"));
						accountBean.setStartTime(object.getLong("startTime"));
						accountBean.setType(object.getString("businessType"));
						list.add(accountBean);
					   }
					if(AccountAdapter==null){
						 AccountAdapter=new AccountAdapter(list, context);
					}
					 AccountAdapter.notifyDataSetChanged();
					 if(i==1){
						 ((AccountActivity) context).account_listView1.setAdapter(AccountAdapter);
					 }
					
					myDialog.dismiss();
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
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
		
		
	}
	
	
//	http://uat.ubankers.com/pay/api/userbusiness/tradelist
}
