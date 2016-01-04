package cn.com.ubankers.www.user.service;

import java.util.ArrayList;


import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.controller.activity.OrderManagementActivity;
import cn.com.ubankers.www.user.model.CfmpOrderBean;
import cn.com.ubankers.www.user.view.CfmpUnOrderAdapter;
import cn.com.ubankers.www.utils.Tools;
import cn.com.ubankers.www.widget.ProcessDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
/**
 * 
 * @author 廖准华
 *未确定预约的service
 */
public class CfmpUnOrderService {
	private AsyncHttpClient client;
	private ProcessDialog progressDialog;
	private Activity activity;
	private String userId;
	private ListView rongListView;
	private ArrayList<CfmpOrderBean> list;
	private CfmpUnOrderAdapter adapter;
	public CfmpUnOrderService(Activity activity,String userId,ListView rongListView,ArrayList<CfmpOrderBean> list){
		this.activity = activity;
		this.userId = userId;
		this.rongListView = rongListView;
		this.list = list;
		if (progressDialog == null) {
			progressDialog = ProcessDialog.createDialog(activity, "正在加载中...");
		}else{
			progressDialog = ProcessDialog.createDialog(activity, "正在加载中...");
		}
		client = MyApplication.app.getClient(activity);
	}
	/**
	 * 获取未确认预约的接口
	 */
	public  void initData() {
		if(list.size()>0){
			list.clear();
		}
		progressDialog.show();
		StringEntity entity=null;
		try {	
			JSONObject json = new JSONObject();
			json.put("limit","");
			json.put("start","");
			json.put("cfmpId",userId);
			json.put("isBInvestor",1);
			entity =new StringEntity(json.toString());
		} catch (Exception e1) {
				e1.printStackTrace();
		}
		client.post(activity,  HttpConfig.URL_CFMPUNORDER, entity,"application/json", new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					boolean flag = response.getBoolean("success");
					if(flag){
						JSONObject jsonObject = response.getJSONObject("result");
						String errorCode = jsonObject.getString("errorCode");
						int totalCount = jsonObject.getInt("totalCount");
						if(errorCode.equals("success")){
							JSONArray jsonArray = jsonObject.getJSONArray("list");
							/**
							 *      "begin": 0,
					                "cfmpId": 33565,
					                "cfmpName": "测试用户1",
					                "dir": "",
					                "end": 1000,
					                "endContactTime": 0,
					                "endContactTimeShow": "",
					                "examineState": 0,
					                "examineStateShow": "",
					                "isNotContact": 0,
					                "isNotContactShow": "",
					                "limit": 0,
					                "productId": 100563,
					                "productIds": "",
					                "productName": "泰岳梧桐在线教育产业新三板与并购基金",
					                "remarks": "",
					                "reserveId": 104982,
					                "reserveMobile": "15121148971",
					                "reserveName": "投资者1",
					                "reservePerson": 33564,
					                "reserveQuota": 1000000,
					                "reserveRole": 1,
					                "reserveTime": 1436865664946,
					                "reserveTimeShow": "2015-07-14",
					                "reserveVoucherId": "",
					                "serviceName": "",
					                "serviceStaff": "",
					                "sortName": "",
					                "start": 0,
					                "totalAmount": "",
					                "totalProNum": "",
					                "userId": 33564
							 */
							for(int i=0;i<jsonArray.length();i++){
								JSONObject object = jsonArray.getJSONObject(i);
								String productName = object.getString("productName");//产品名称
								String productId = object.getString("productId");//产品id
								int examineState = object.getInt("examineState");//预约状态
								String reserveTimeShow = object.getString("reserveTimeShow");//预约时间
								String reserveQuota = object.getString("reserveQuota");//预约金额
								reserveQuota=Tools.InterceptTo(reserveQuota);
								String reserveId = object.getString("reserveId");//预约id
								String reserveName = object.getString("reserveName");//预约名称
								String reserveMobile=object.getString("reserveMobile");//预约电话
								CfmpOrderBean cfmpOrderBean = new CfmpOrderBean();
								cfmpOrderBean.setExamineState(examineState);
								cfmpOrderBean.setProductName(productName);
								cfmpOrderBean.setReserveQuota(reserveQuota);
								cfmpOrderBean.setReserveTimeShow(reserveTimeShow);
								cfmpOrderBean.setReserveId(reserveId);
								cfmpOrderBean.setProductId(productId);
								cfmpOrderBean.setReserveName(reserveName);
								cfmpOrderBean.setReserveMobile(reserveMobile);
								list.add(cfmpOrderBean);
							}
							adapter = new CfmpUnOrderAdapter(activity, list , new MyCountermandOrderListener());
							rongListView.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						}
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				progressDialog.dismiss();
				super.onSuccess(statusCode, headers, response);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				Log.e("jkdkjkdjkd","失败啦！！！！！");
				super.onFailure(statusCode, headers, responseString, throwable);
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
		
	}
	public class MyCountermandOrderListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			CfmpOrderBean cfmpOrderBean = (CfmpOrderBean) arg0.getTag();
			String reserveId = cfmpOrderBean.getReserveId();
			if(reserveId!=null&&!reserveId.equals("")){
				   CountermandOrder(reserveId);
			}
		 
		}
			
	}
	//确认预约的接口
		private void CountermandOrder(String reserveId){
					StringEntity entity=null;
					try {	
						JSONObject json = new JSONObject();
						json.put("reserveId",reserveId);
						entity =new StringEntity(json.toString(),"utf-8");
					} catch (Exception e1) {
							e1.printStackTrace();
					}
					client.post(activity,HttpConfig.URL_CFMP_COUNTERMAND, entity, "application/json", new JsonHttpResponseHandler(){
						@Override
						public void onSuccess(int statusCode, Header[] headers,
													JSONObject response) {
							try {
								boolean flag = response.getBoolean("success");
								if(flag){
									JSONObject jsonObject = response.getJSONObject("result");
									String errorCode = jsonObject.getString("errorCode");
									boolean info = jsonObject.getBoolean("info");
									if(errorCode.equals("success")&&info==true){
										initData();
										Intent intent=new Intent(activity,OrderManagementActivity.class);
										activity.startActivity(intent);
										activity.finish();
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
								Throwable throwable, JSONObject errorResponse) {
							// TODO Auto-generated method stub
							super.onFailure(statusCode, headers, throwable, errorResponse);
							Toast.makeText(activity, "请求超时，请重试", Toast.LENGTH_SHORT).show();
							progressDialog.dismiss();
						}
						
					});
		}
}
