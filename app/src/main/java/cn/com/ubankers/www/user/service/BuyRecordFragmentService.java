package cn.com.ubankers.www.user.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.widget.ListView;
import android.widget.Toast;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.BuyRecordBean;
import cn.com.ubankers.www.user.view.BuyRecordAdapter;
import cn.com.ubankers.www.widget.ProcessDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

public class BuyRecordFragmentService {	
	private static final ResponseHandlerInterface JsonHttpResponseHandler = null;
	
	private AsyncHttpClient client;
	private Activity activity;
	private ListView investorlist;
	private ProcessDialog progressDialog;
	
	public BuyRecordFragmentService(Activity activity,ListView investorlist){	
		this.activity=activity;
		this.investorlist=investorlist;
		if (progressDialog == null) {
			progressDialog = ProcessDialog.createDialog(activity, "正在加载中...");
		}else{
			progressDialog = ProcessDialog.createDialog(activity, "正在加载中...");
		}		
		client = MyApplication.app.getClient(activity);
	}	
	
	//财富师已购买记录
	public List  Requests(String str){
		  progressDialog.show();
		 final ArrayList BuyRecordlist=new ArrayList<BuyRecordBean>();
		StringEntity entity = null;		
		try {
			JSONObject json= new JSONObject();
			json.put("cfmpId",str);
			json.put("start","1");
			json.put("limit","1000");
			entity =new StringEntity(json.toString(),"utf-8");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
client.post(activity,HttpConfig.URL_CFMP_BUY_PRODUCT, entity,"application/json", new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				try {
					boolean flag2 = response.getBoolean("success");
					JSONObject jsonObject2 = response.getJSONObject("result");
					String errorCode = jsonObject2.getString("errorCode");
					if(flag2 == true && errorCode.equals("success")){
						JSONArray jsonArray = jsonObject2.getJSONArray("list");
						for(int i=0;i<jsonArray.length();i++){
							BuyRecordBean  buyrecord=new BuyRecordBean();
							JSONObject object = jsonArray.getJSONObject(i);
							buyrecord.setCfmpName(object.getString("investorRealName")==null?"":object.getString("investorRealName"));//财富师"
							buyrecord.setValueDate(object.getLong("valueDate")); //起息日（毫秒）
							buyrecord.setExpiryDateForInterest(object.getLong("expiryDateForInterest"));//到期日（毫秒）
							buyrecord.setProductName(object.getString("productName"));//购买产品名称
							buyrecord.setPurchaseAmount(object.getString("purchaseAmount"));//财富师"
							buyrecord.setPurchaseDate(object.getLong("purchaseDate"));//购买时间（毫秒）
							buyrecord.setState(object.getInt("state"));//状态"	
							BuyRecordlist.add(buyrecord);
						}	
						BuyRecordAdapter buyRecordAdapter=new BuyRecordAdapter(activity, BuyRecordlist);
						investorlist.setAdapter(buyRecordAdapter);
						progressDialog.dismiss();
						String strg="123456";
				        String str="http://ip+port/product/ajax/checkBankValid/{"+strg+"}";
					}
					
					if( errorCode.equals("noLogin")){
						Toast.makeText(activity, "未登陆",0).show();
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
				progressDialog.dismiss();
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
			Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			super.onFailure(statusCode, headers, throwable, errorResponse);
			Toast.makeText(activity, "请求超时，请重试", Toast.LENGTH_SHORT).show();
			progressDialog.dismiss();
			}
			
		});		
		return null;
		
	}

}
