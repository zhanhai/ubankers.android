package cn.com.ubankers.www.authentication.controller.activity;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.product.controller.activity.ProductActivity;
import cn.com.ubankers.www.sns.controller.activity.SnsActivity;
import cn.com.ubankers.www.user.controller.activity.SettingActivity;
import cn.com.ubankers.www.user.controller.activity.UserCenterActivity;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.model.UserNewBean;
import cn.com.ubankers.www.authentication.model.BindBean;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.http.ParseUtils;
import cn.com.ubankers.www.widget.MyDialog;
import cn.com.ubankers.www.widget.ClearEditTextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BindActivity extends Activity implements OnClickListener {
	private Context context;
	private MyDialog myDialog;
	private Button bangding_submit,nobing_submit;
	private ClearEditTextView bangding_password, bangding_username;
	private TextView bangding_line;
	private View title_bar_back_btn;
	private BindBean bundleBean;
	private AsyncHttpClient client;
	private UserBean userBean;
	private int typeValue;
	private String role="";
	private Platform qq, weixin, weibo;
	private Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(16[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
	private TextView Prompt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		this.setContentView(R.layout.bundle_activity);
		Intent intent = this.getIntent();
		if (intent != null) {
			bundleBean = (BindBean) intent.getSerializableExtra("bundleBean");
			typeValue =  intent.getIntExtra("type",1);
		}
		if (myDialog == null) {
			myDialog = MyDialog.createDialog(this,"正在加载中...");
		}
		   initView();
		   MyApplication.getInstance().addActivity(this);

	}
	private void initView() {
		bangding_password = (ClearEditTextView) findViewById(R.id.bangding_password);
		bangding_username = (ClearEditTextView) findViewById(R.id.bangding_username);
		title_bar_back_btn = findViewById(R.id.title_bar_back_btn);
		bangding_submit = (Button) findViewById(R.id.bangding_submit);
		nobing_submit = (Button) findViewById(R.id.nobing_submit);
		Prompt=(TextView)findViewById(R.id.Prompt);
		bangding_submit.setOnClickListener(this);
		nobing_submit.setOnClickListener(this);
		title_bar_back_btn.setOnClickListener(this);
		String source = ",你好！请输入已在银板客注册的账号及密码，与第三方账号关联！";
		Prompt.setText(Html.fromHtml(bundleBean.getPlatfromUserName()+
				 "<font color=\"#000000\">"+source+"</font>"));
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bangding_submit:
			bingSubmit(bundleBean);
			break;
		case R.id.nobing_submit:
			insertSubmit(bundleBean);
			break;
		case R.id.title_bar_back_btn:
			Intent intent = new Intent(context,MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			BindActivity.this.finish();
			overridePendingTransition(
					android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			break;
		}
	}
	private void insertSubmit(final BindBean bundleBean){
		StringEntity entity = null;
		if (bundleBean.getBundleId()!= null&& bundleBean.getPlatfromName() != null&&bundleBean.getPlatfromUserName()!=null) {
			try {
				JSONObject json = new JSONObject();
				json.put("openid", bundleBean.getBundleId());
				json.put("nickname", bundleBean.getPlatfromUserName());
				json.put("provider", bundleBean.getPlatfromName());
				entity = new StringEntity(json.toString(),"utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		myDialog.show();
		client = MyApplication.app.getClient(context);
		client.post(context, HttpConfig.URL_INSERT, entity, "application/json",new JsonHttpResponseHandler() {
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					boolean flag = response.getBoolean("success");
					JSONObject obj = response.getJSONObject("result");
					String errorCode = obj.getString("errorCode");
					JSONObject json = obj.getJSONObject("info");
					if(errorCode.equals("success")&&flag){
						for(int i=0;i<headers.length;i++){
							Log.e("...........", headers[i].getName()+"..."+headers[i].getValue());	
				          if(headers[i].getName().equals("Set-Cookie")) {
				        	  String  GSID  = null;
				        	  String name =headers[i].getName();
							  String value = headers[i].getValue();
							  if(value.contains("__GSID__")){ 								  
				        	  SharedPreferences preferences = context.getSharedPreferences("header", Context.MODE_PRIVATE);  
				        	  SharedPreferences.Editor editor = preferences.edit();
				        	  editor.putString("__GSID__",value);
				        	  editor.commit();
							 }
				            }
				          }
						myDialog.dismiss();
					    Type type = new TypeToken<UserNewBean>(){}.getType();
					    UserNewBean userNewBean =ParseUtils.parseUserInfo(json.toString(), type);
				         UserBean userBean = ParseUtils.parseUserBean(userNewBean,context);	    
						MyApplication.app.setUser(userBean);
						MyApplication.app.setClient(client);
						Intent intent = new Intent(context,MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
				}catch(JSONException e){
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
			Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
				myDialog.dismiss();
			}
		});
		
	}
	private void bingSubmit(BindBean bundleBean) {
		String mobilePhone = bangding_username.getText().toString().trim();
		String password = bangding_password.getText().toString().trim();
		String check1 = "[\\u4e00-\\u9fa5]";
		Pattern regex2 = Pattern.compile(check1);
		Matcher m = p.matcher(mobilePhone);
		if (bundleBean.getBundleId() != null
				&& bundleBean.getPlatfromName() != null) {
			if (!m.matches()) {
				Toast.makeText(context, "用户名无效", Toast.LENGTH_SHORT).show();
			} else if (password == "" || password.length() == 0
					|| password.equals("")) {
				Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT).show();
			} else if (regex2.matcher(password).matches()) {
				Toast.makeText(context, "密码格式不正确", Toast.LENGTH_SHORT).show();
			} else if (password.length() < 6 || password.length() > 20) {
				Toast.makeText(context, "密码不能小于6位且不能大于20位", Toast.LENGTH_SHORT)
						.show();
			} else {
				StringEntity entity = null;
				try {
					JSONObject json = new JSONObject();
					json.put("openid", bundleBean.getBundleId());
					json.put("mobile", mobilePhone);
					json.put("password", password);
					json.put("provider", bundleBean.getPlatfromName());
					entity = new StringEntity(json.toString(),"utf-8");
				} catch (Exception e) {
					e.printStackTrace();
				}
				client = MyApplication.app.getClient(context);
				client.post(context, HttpConfig.URL_BIND, entity, "application/json", 
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(int statusCode, Header[] headers,
									JSONObject response) {
								try {
									boolean flag = response.getBoolean("success");
									JSONObject obj = response.getJSONObject("result");
									String errorCode = obj.getString("errorCode");
									if(!flag){
										if(errorCode.equals("QQ")){
											 Toast.makeText(context, "该账号已绑定QQ", Toast.LENGTH_SHORT)
												.show();
									    }else if(errorCode.equals("用户不存在.请注册")){
									    	 Toast.makeText(context, errorCode.toString(), Toast.LENGTH_SHORT)
												.show();
									    }else if (errorCode.equals("校验失败(密码错误)")) {
									    	 Toast.makeText(context, errorCode.toString(), Toast.LENGTH_SHORT)
												.show();
									    }else if(errorCode.equals("weixin")){
									    	Toast.makeText(context, "该账号已绑定微信", Toast.LENGTH_SHORT)
											.show();
									    }else if(errorCode.equals("sina")){
									    	Toast.makeText(context, "该账号已绑定新浪", Toast.LENGTH_SHORT)
											.show();
									    }
									}else{
										if (flag&& errorCode.equals("success")) {
											for(int i=0;i<headers.length;i++){
												Log.e("...........", headers[i].getName()+"..."+headers[i].getValue());	
									          if(headers[i].getName().equals("Set-Cookie")) {
									        	  String  GSID  = null;
									        	  String name =headers[i].getName();
												  String value = headers[i].getValue();
												  if(value.contains("__GSID__")){ 								  
									        	  SharedPreferences preferences = context.getSharedPreferences("header", Context.MODE_PRIVATE);  
									        	  SharedPreferences.Editor editor = preferences.edit();
									        	  editor.putString("__GSID__",value);
									        	  editor.commit();
												 }
									          }
									          }
										 JSONObject json = obj.getJSONObject("info");
									     Type type = new TypeToken<UserNewBean>(){}.getType();
									     UserNewBean userNewBean =ParseUtils.parseUserInfo(json.toString(), type);
								         UserBean userBean = ParseUtils.parseUserBean(userNewBean,context);	    
								         MyApplication.app.setUser(userBean);
								         MyApplication.app.setClient(client);
								         ParseUtils.saveSharePreferences(context,json.toString());
								         Intent intent = new Intent(context,MainActivity.class);
										 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										 startActivity(intent);
									overridePendingTransition(
											android.R.anim.slide_in_left,
											android.R.anim.slide_out_right);
									((SettingActivity)context).bExit = false;
								 Toast.makeText(context, "授权成功", Toast.LENGTH_SHORT)
										.show();
							      }	
							      }
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
								}
							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, Throwable throwable,
									JSONObject errorResponse) {
								 Toast.makeText(context, "授权成功", Toast.LENGTH_SHORT)
									.show();
							}
						});
			}
		} else {
			 Toast.makeText(context, "授权不成功", Toast.LENGTH_SHORT)
				.show();
	        }	
	   }
}