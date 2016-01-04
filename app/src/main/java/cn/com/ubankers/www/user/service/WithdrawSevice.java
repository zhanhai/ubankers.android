package cn.com.ubankers.www.user.service;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.controller.activity.WithdrawActivity;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.utils.Util;
import cn.com.ubankers.www.widget.ProcessDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class WithdrawSevice {
	
	private Context context;
	private UserBean userBean;
	private AsyncHttpClient client;
	private ProcessDialog myDialog;
	private AlertDialog dialog;
	private TextView withdraw_data_Toast;

	public WithdrawSevice(Context context){
		this.context=context;
		if(MyApplication.app.getUser()!=null){
			userBean=MyApplication.app.getUser();
		}
		client = MyApplication.app.getClient(context);
		if(myDialog==null){
			   myDialog= ProcessDialog.createDialog(context, "正在加载中...");
			}
		initData();
	}
	
	
	/**
	 * 可用余额
	 * 
	 * @param amount
	 *            金额
	 * @return
	 */
	public void initData(){
		myDialog.show();
		//http://uat.ubankers.com/pay/api/withdraw/limit
		client.get(HttpConfig.THE_BALANCE_OF, new JsonHttpResponseHandler(){
		

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				
				try {
					Boolean success=response.getBoolean("success");
					if(success){
						
						JSONObject result=	response.getJSONObject("result");
						JSONObject info=result.getJSONObject("info");
						String	amount=info.getString("amount");
						((WithdrawActivity) context).withdraw_balance.setText("可用余额："+Util.Intercept1(amount)+"元");	
						 Intent mIntent = new Intent("ACTION_NAME"); 
						 String   amounta=Util.Intercept3(amount)+"";
				            mIntent.putExtra("amount", amounta);
				            context.sendBroadcast(mIntent);
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



	/**
	 * 提现
	 * 
	 * @param amount
	 *            提现请求金额
	 * @return
	 */
	public void WithdrawalAmount(String amount) {
		// TODO Auto-generated method stub
		StringEntity entity=null;	
		try {
			JSONObject json = new JSONObject();
			json.put("amount", amount);
			entity =new StringEntity(json.toString());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		client.post(context, HttpConfig.CASH_WITHDRAWAL,entity, "application/json",new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				try {
					boolean success=response.getBoolean("success");
					if(success){
						JSONObject	result=	response.getJSONObject("result");
					    boolean	info=result.getBoolean("info");
					    if(info){
					    	String	errorCode = result.getString("errorCode");
					    	if(errorCode.equals("success")){
					    		withdrawManageDialog();
//					    	((WithdrawActivity)context).Withdrawal_amount.setText("");
					    	
					    		
					    	}else{
					    		Toast.makeText(context, "提交失败", 0).show();
					    	}
					    	
					    }else{
					    	Toast.makeText(context, "提交失败", 0).show();
					    }
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(context, "提交失败", 0).show();
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
	
	
	/**
	 * 提示框
	 * 
	 * @param amount
	 *           计算提现费率
	 * @return
	 */
	
//	http://uat.ubankers.com/pay/api/withdraw/feeData 
	public void WithdrawalfeeData( final String feeData){
		StringEntity entity=null;	
		try {
			JSONObject json = new JSONObject();
			json.put("amount", feeData);
			entity =new StringEntity(json.toString());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		client.post(context,HttpConfig.CASH_WITHDRAWA,entity, "application/json",new JsonHttpResponseHandler(){
			@Override
			 public void onSuccess(int statusCode, Header[] headers, 
					 JSONObject response) {
			try {
				boolean	success=	response.getBoolean("success");
				if(success){
					JSONObject	result=	response.getJSONObject("result");
					JSONObject info=result.getJSONObject("info");
					String errorCode  =result.getString("errorCode");
					if(errorCode.equals("success")){
					String	amount=info.getString("amount");
					WithdrawDataDialog(amount,feeData);
					//((WithdrawActivity)context).cost.setText(amount);
					
					}
				}
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			};
			
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
	
	/**
	 * 提示框
	 * 
	 * @param amount
	 *            金额
	 * @return
	 */
	public void withdrawManageDialog () { 
		dialog=new AlertDialog.Builder(context).create();
		View view = LayoutInflater.from(context).inflate(R.layout.withdraw_dialog, null);
		dialog.setView(view); 
		dialog.show();
		view.findViewById(R.id.withdraw_OK).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				((WithdrawActivity)context).finish();
				dialog.dismiss();
			}
			
		});
		
           }
	
	
	public void WithdrawDataDialog(String str,final String strfeeData) { 
		String  string=Util.Intercept1(str);
		dialog=new AlertDialog.Builder(context).create();
		View view = LayoutInflater.from(context).inflate(R.layout.withdraw_data_dialog, null);
		withdraw_data_Toast=(TextView)view.findViewById(R.id.withdraw_data_Toast);
		withdraw_data_Toast.setText(Html.fromHtml("<font color=\"#000000\">提现需要扣除</font>"+string+"<font color=\"#000000\">元手续费是否操作?</font>"
				));
		dialog.setView(view); 
		dialog.show();
		view.findViewById(R.id.withdraw_data_withdraw_OK).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				WithdrawalAmount(strfeeData);
				dialog.dismiss();
			}
			
		});
		
           }

}
