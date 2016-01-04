package cn.com.ubankers.www.user.service;

import java.lang.reflect.Type;

import org.apache.http.Header;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.authentication.controller.activity.LoginActivity;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.http.ParseUtils;
import cn.com.ubankers.www.user.controller.activity.BankCardActivity;
import cn.com.ubankers.www.user.controller.activity.BusinessCardActivity;
import cn.com.ubankers.www.user.controller.activity.EmailActivity;
import cn.com.ubankers.www.user.controller.activity.IdCardActivity;
import cn.com.ubankers.www.user.controller.activity.SafetyCenterActivity;
import cn.com.ubankers.www.user.controller.activity.SettingModifyPsdActivity;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.model.UserNewBean;
import cn.com.ubankers.www.widget.ProcessDialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

public class SafetyCenterService {

	private UserBean userBean;
	private int idcard_statu;
	private int bankcard_statu;
	private Context context;
	private AsyncHttpClient client;
	private ProcessDialog myDialog;
	public SafetyCenterService(Context context) {
		this.context = context;
		if(MyApplication.app.getUser()!=null){
			userBean = MyApplication.app.getUser();
		}
		client = MyApplication.app.getClient(context);
		if(myDialog==null){
			   myDialog= ProcessDialog.createDialog(context, "正在加载中...");
			}
	}
	
	/**
	 * 身份证状态判断
	 */
	private void  idState(){
		if(MyApplication.app.getUser()!=null){
			userBean = MyApplication.app.getUser();
		}
		if(userBean!=null){
			if(userBean.getIdcard_status()!= null){
				if(userBean.getIdcard_status().equals("0")){
				((SafetyCenterActivity)context).idcard_status.setText("审核中");	
				}else if(userBean.getIdcard_status().equals("1")){
					((SafetyCenterActivity)context).idcard_status.setText("通过审核");	
				}else if(userBean.getIdcard_status().equals("2")){
					((SafetyCenterActivity)context).idcard_status.setText("未通过审核");	
				}
			}else{
				((SafetyCenterActivity)context).idcard_status.setText("未绑定");
			}
		}
	}
	/**
	 * 邮箱绑定状态判断
	 */
	private void  emailState(){
		if(userBean!=null){
		if(userBean.getEmail()!=null){
			if (userBean.getEmail().equals("")) {
				((SafetyCenterActivity)context).email_status.setText("未绑定");
			}else {
				((SafetyCenterActivity)context).email_status.setText("已绑定");
			}	
		}else{
			((SafetyCenterActivity)context).email_status.setText("未绑定");
		}
		}
	}
	/**
	 * 银行卡绑定状态判断
	 */
	private void  bankIdState(){
		if(MyApplication.app.getUser()!=null){
			userBean = MyApplication.app.getUser();
		}
		if(userBean!=null){
			if(userBean.getBankcard_status()!= null){			
					if (userBean.getBankcard_status().equals("0")) {
						((SafetyCenterActivity)context).bankcard_status.setText("审核中");
					} else if (userBean.getBankcard_status().equals("1")) {
						((SafetyCenterActivity)context).bankcard_status.setText("通过审核");
					} else if (userBean.getBankcard_status().equals("2")) {
						((SafetyCenterActivity)context).bankcard_status.setText("未通过审核");
					}
				} else {
					((SafetyCenterActivity)context).bankcard_status.setText("未认证");
				}
		}
	}
	/**
	 * 名片绑定状态判断
	 */
	private void isFileid(){
		if(MyApplication.app.getUser()!=null){
			userBean = MyApplication.app.getUser();
		}
		 if (userBean!= null) {
				if (userBean.getFileid()==null||userBean.getFileid().equals("")) {
					((SafetyCenterActivity)context).bankcard_mingpianzhuantai.setText("名片未上传");
				} else if(userBean.getBusinessCard().equals("0")){
					((SafetyCenterActivity)context).bankcard_mingpianzhuantai.setText("审核中");
				}else if(userBean.getBusinessCard().equals("1")){
					((SafetyCenterActivity)context).bankcard_mingpianzhuantai.setText("通过审核");
				}else if(userBean.getBusinessCard().equals("2")){
					((SafetyCenterActivity)context).bankcard_mingpianzhuantai.setText("未通过审核");
				}
			}
	}
	/**
	 * 安全绑定状态判断
	 */
	public void StateJudgment() {
		if(userBean!=null&&userBean.getUserRole()!=null&&userBean.getUserRole().equals("cfmp")) {
		  ((SafetyCenterActivity)context).layout_mingpian.setVisibility(View.VISIBLE);
		 }
		  idState();//身份证状态判断
		  emailState();//邮箱绑定状态判断
		  bankIdState();//银行卡绑定状态判断
		  isFileid();//名片绑定状态判断
	}

	// 名片跳转
	public Intent mingpian() {
		// TODO Auto-generated method stub
		Intent intent=null;
		if (userBean!= null&& userBean.getUserName() != null) {
		    intent = new Intent(context, BusinessCardActivity.class);
			intent.putExtra("userBean", userBean);
		} else {
			intent = new Intent(context, LoginActivity.class);
		}
		return intent;
	}

	// 绑定身份证跳转
	public Intent IdentityCard() {
		Intent intent=null;
		if (userBean!= null&& userBean.getUserName() != null) {
			intent = new Intent(context, IdCardActivity.class);
			intent.putExtra("userBean", userBean);
		} else {
			intent = new Intent(context, LoginActivity.class);
		}
		return intent;
	}

	// 绑定银行卡跳转
	public Intent Bank_Card() {
		Intent intent=null;
		if (userBean!= null&& userBean.getUserName() != null) {
			intent = new Intent(context, BankCardActivity.class);
			intent.putExtra("userBean", userBean);
		} else {
			intent = new Intent(context, LoginActivity.class);
		}
		return intent;
	}

	// 修改密码
	public Intent Change_Password() {
		Intent intent=null;
		if (userBean!= null&& userBean.getUserName() != null) {
			intent = new Intent(context, SettingModifyPsdActivity.class);
		} else {
			intent = new Intent(context, LoginActivity.class);
		}
		return intent;
	}

	// 邮箱认证
	public Intent Email_authentication() {
		Intent intent=null;
		if (userBean!= null&& userBean.getUserName() != null) {
			 intent = new Intent(context, EmailActivity.class);
			intent.putExtra("userBean", userBean);
		} else {
			 intent = new Intent(context, LoginActivity.class);
		}
		return intent;
	}
	public void initData() {
		myDialog.show();
		client.get(context, HttpConfig.URL_USER_INFO, null, "application/json",
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {	
							boolean flag = response.getBoolean("success");
							JSONObject obj = response.getJSONObject("result");
							if (flag&& obj.getString("errorCode").equals("success")) {
								 JSONObject json = response.getJSONObject("result").getJSONObject("user");
								  Type type = new TypeToken<UserNewBean>(){}.getType();
								    UserNewBean userNewBean =ParseUtils.parseUserInfo(json.toString(), type);
							        UserBean userBean = ParseUtils.parseUserBeanlg(userNewBean,context);	
							        ParseUtils.saveSharePreferences(context,json.toString());
							        MyApplication.app.setUser(userBean);
						            StateJudgment();	 
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						finally{
						myDialog.dismiss();
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
		
	}
}