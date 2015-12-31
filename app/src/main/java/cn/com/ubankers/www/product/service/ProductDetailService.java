package cn.com.ubankers.www.product.service;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.ubankers.app.product.detail.ProductDetailActivity;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.http.ParseUtils;
import cn.com.ubankers.www.product.model.ProductDetail;
import cn.com.ubankers.www.product.model.ReserverBean;
import cn.com.ubankers.www.sns.controller.activity.SnsArticleActivity;
import cn.com.ubankers.www.sns.model.ArticleBean;
import cn.com.ubankers.www.user.model.CustomerBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.widget.MyDialog;

public class ProductDetailService {
	private ArrayList<CustomerBean> list;
	private String productId;
	private cn.com.ubankers.www.utils.MyDialog myDialog;
	private ProductDetail product ;
	private MyDialog progressDialog;
	private AsyncHttpClient client;
	private Context context;
	private UserBean userBean;
	private ReserverBean orderBean;
	private ArticleBean articleBean;
	public ProductDetailService(String productId,final Context context){
		this.context = context;
	    this.productId = productId;
		if (progressDialog == null) {
			progressDialog = MyDialog
					.createDialog(context,"正在加载中...");
		}
		client = MyApplication.app.getClient(context);
		if(MyApplication.app.getUser()!=null){
			userBean = MyApplication.app.getUser();
		}
		myDialog = new cn.com.ubankers.www.utils.MyDialog(context);
	}


	//投资者预约的接口
	public void InvestorOrder(String money_amount,String phone_number,String order_name,EditText et_name,final AlertDialog dialog){
			JSONObject json = new JSONObject();
			StringEntity entity = null;
			try {
					json.put("userId",userBean.getUserId());
					json.put("reserveQuota", money_amount);
					json.put("productId",productId);
					json.put("reserveMobile",phone_number);
					json.put("reserveName",order_name);
					entity = new StringEntity(json.toString(),"utf-8");
			} catch (Exception e1) {
					e1.printStackTrace();
			}
			client.post(context,HttpConfig.URL_INVESTOR_ORDER_PRODUCT, entity, "application/json",new JsonHttpResponseHandler(){
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						Log.e("hjdhhdhd","成功啦！！！！！");
						try {
							boolean flag = response.getBoolean("success");
							if(flag){
								JSONObject jsonObject = response.getJSONObject("result");
								String errorCode = jsonObject.getString("errorCode");
								orderBean = new ReserverBean();
								if(errorCode.equals("success")){
									successSetVuale(jsonObject);
									dialog.dismiss();
									myDialog.successDialog(1);
								}else if(errorCode.equals("noInvestor")){
									myDialog.errorDialog();
								}
							}else{
								dialog.dismiss();
								myDialog.errorDialog();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					};
					public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
							myDialog.errorDialog();
					};
					@Override
					public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable, errorResponse);
					Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
					progressDialog.dismiss();
					}
		});
		try{
				et_name.setText(orderBean.getReserveName());
			    if(et_name.getText().toString().trim().equals(orderBean.getReserveName())){
					et_name.clearFocus();
					et_name.setFocusable(false);
				}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//投资者预约成功的设值
	private void successSetVuale(JSONObject jsonObject){
		try {
				JSONObject info = jsonObject.getJSONObject("info");
				String userId = info.getString("userId");//用户的id
				String productId = info.getString("productId");//产品id
				int examineState = info.getInt("examineState");//财富师审批状态
				String serviceStaff = info.getString("serviceStaff");//接线客服
				int cfmpId = info.getInt("cfmpId");//财富师id
				int reserveId = info.getInt("reserveId");//预约的id
				String reserveName=info.getString("reserveName");//预约名称
				String reserveQuota = info.getString("reserveQuota");//预约额度
				String reserveMobile = info.getString("reserveMobile");//预约电话
				int reserveRole = info.getInt("reserveRole");//预约角色
				String reserveTime = info.getString("reserveTime");//预约时间
				String remarks = info.getString("remarks");//预约备注
				int endContactTime = info.getInt("endContactTime");//客服最后联系时间
				int isNotContact = info.getInt("isNotContact");//客服是否已联系
//				userBean.setUserId(userId);
//				product.setProductId(productId);
				orderBean.setExamineState(examineState);
				orderBean.setServiceStaff(serviceStaff);
				orderBean.setCfmpId(cfmpId);
				orderBean.setReserveId(reserveId);
				orderBean.setReserveName(reserveName);
				orderBean.setReserveQuota(reserveQuota);
				orderBean.setReserveMobile(reserveMobile);
				orderBean.setReserveRole(reserveRole);
				orderBean.setReserveTime(reserveTime);
				orderBean.setRemarks(remarks);
				orderBean.setEndContactTime(endContactTime);
				orderBean.setIsNotContact(isNotContact);
		}catch(JSONException e){
			e.printStackTrace();
		}
	}
	//财富师给投资者预约的接口
	public void cfmpOrder(int isBInvestor,String money,String number,String name,final AlertDialog dialog){
		JSONObject json = new JSONObject();
		StringEntity entity = null;
		try {
			    if(isBInvestor==1){
						json.put("isBInvestor",isBInvestor);
						json.put("reserveQuota", money);
						json.put("productId",productId);
						json.put("reserveMobile",number);
						json.put("reserveName",name);
						json.put("cfmpId",userBean.getUserId());
						json.put("userId", ((ProductDetailActivity)context).id);
						entity = new StringEntity(json.toString(),"utf-8");
			    }else if(isBInvestor==2){
				    	json.put("isBInvestor",isBInvestor);
						json.put("reserveQuota", money);
						json.put("productId",productId);
						json.put("reserveMobile",number);
						json.put("reserveName",name);
						json.put("cfmpId",userBean.getUserId());
						entity = new StringEntity(json.toString(),"utf-8");
			    }
		} catch (Exception e1) {
				e1.printStackTrace();
		}
		client.post(context,HttpConfig.URL_CFMP_BOUND_INVERTOR, entity, "application/json", new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				try {
					boolean flag = response.getBoolean("success");
					if(flag==true){
						orderBean = new ReserverBean();
						JSONObject jsonObject = response.getJSONObject("result");
						String errorCode = jsonObject.getString("errorCode");
						if(errorCode.equals("success")){
							successOrderCfmpValue(jsonObject);
							dialog.dismiss();
							myDialog.successDialog(0);
						}else if(errorCode.equals("noInvestor")){
							myDialog.errorDialog();
						}
					}else{
						dialog.dismiss();
						myDialog.orderCfmpDialog();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				myDialog.errorDialog();
			};
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
				progressDialog.dismiss();
			}
		});
	}
	//财富师预约成功设置值
	private void successOrderCfmpValue(JSONObject jsonObject){
		try{
				JSONObject info = jsonObject.getJSONObject("info");
		//		String userId = info.getString("userId");//用户的id
				String productId = info.getString("productId");//产品id
				int examineState = info.getInt("examineState");//财富师审批状态
				String serviceStaff = info.getString("serviceStaff");//接线客服
				int cfmpId = info.getInt("cfmpId");//财富师id
				int reserveId = info.getInt("reserveId");//预约的id
				String reserveName=info.getString("reserveName");//预约名称
				String reserveQuota = info.getString("reserveQuota");//预约额度
				String reserveMobile = info.getString("reserveMobile");//预约电话
				int reserveRole = info.getInt("reserveRole");//预约角色
				String reserveTime = info.getString("reserveTime");//预约时间
				String remarks = info.getString("remarks");//预约备注
				int endContactTime = info.getInt("endContactTime");//客服最后联系时间
				int isNotContact = info.getInt("isNotContact");//客服是否已联系
//				product.setProductId(productId);
				orderBean.setExamineState(examineState);
				orderBean.setServiceStaff(serviceStaff);
				orderBean.setCfmpId(cfmpId);
				orderBean.setReserveId(reserveId);
				orderBean.setReserveName(reserveName);
				orderBean.setReserveQuota(reserveQuota);
				orderBean.setReserveMobile(reserveMobile);
				orderBean.setReserveRole(reserveRole);
				orderBean.setReserveTime(reserveTime);
				orderBean.setRemarks(remarks);
				orderBean.setEndContactTime(endContactTime);
				orderBean.setIsNotContact(isNotContact);
		}catch(JSONException e){
			e.printStackTrace();
		}
	}
	public void goBack(){
		((ProductDetailActivity)context).finish();
	}
	
}
