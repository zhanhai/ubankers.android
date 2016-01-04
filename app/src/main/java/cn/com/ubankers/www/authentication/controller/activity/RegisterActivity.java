package cn.com.ubankers.www.authentication.controller.activity;

import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
//import cn.com.ubankers.www.authentication.controller.activity.SMSBroadcastReceiver.MessageListener;
import cn.com.ubankers.www.authentication.model.BindBean;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.utils.Des;
import cn.com.ubankers.www.widget.ProcessDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RegisterActivity extends Activity implements OnClickListener {

	private Button registerBtn;
	private EditText register_username,register_password,register_confirm_password,register_verifycode,register_recommend;
	private TextView verifybutton;
	private RadioGroup radioGroup;
	private RelativeLayout radio_wealth_master,radio_capital_division,radio_investor;
	private TextView login_register_title,register_title,consent,cfmp_tv,
						capital_tv,investor_tv,textView2,textView1,textView3,voice_verification_code;
	private LinearLayout title_bar_back_btn;
	private LinearLayout login_layout;
	private RelativeLayout register_layout;
	private AsyncHttpClient client;
	private Context context;
	private Intent intent;
	private TimeCount timeCount;
	private SQLiteDatabase db;
	private UserBean userBean;
	private int type=3;
	private ImageView check_register,cfmp_iv,capital_iv,investor_iv;
	private String role = "tourist";
	private String phone;
	private Timer timer = new Timer();
	private boolean isExist = false;
	private BindBean bundleBean;
	private boolean ifNetWorkConnected = false;
	private TimeCount1 timeCount1;
	private ProcessDialog progressDialog;
	private Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(16[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
	private View vq;
	private SMSBroadcastReceiver mSMSBroadcastReceiver;
	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.regist_activity);
		// 设置title样式
		initView();
		context = RegisterActivity.this;
		if (isNetWorkConnected()) {
		} else {
			ifNetWorkConnected = true;
			Toast.makeText(getApplicationContext(), "无法连接到网络，请稍候再试", 1).show();
		}
		if (progressDialog == null){
			progressDialog = ProcessDialog.createDialog(this, "正在加载中...");
			}
		timeCount = new TimeCount(60000, 1000);
		timeCount1 = new TimeCount1(60000,1000);
		MyApplication.getInstance().addActivity(this);
		client = MyApplication.app.getClient(context);
	}
	
	public static String getDYnamicPassword(String content){
		String check ="(?<!\\d)\\d{6}(?!\\d)";
		Pattern continuousNumberPattern = Pattern.compile(check);
		Matcher m = continuousNumberPattern.matcher(content);
		String dynamicPassword = "";
		while(m.find()){
			if(m.group().length() == 6){
				System.out.print(m.group());
				dynamicPassword = m.group();
			}
		}
		return dynamicPassword;
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//生产广播处理
		mSMSBroadcastReceiver = new SMSBroadcastReceiver();
		//实例化过滤器并设置要过滤的广播
		IntentFilter intentFilter = new IntentFilter(ACTION);
		intentFilter.setPriority(Integer.MAX_VALUE);
		//注册广播
		this.registerReceiver(mSMSBroadcastReceiver, intentFilter);
		mSMSBroadcastReceiver.setOnReceivedMessageLiatener(new SMSBroadcastReceiver.MessageListener() {
			
			@Override
			public void onReceived(String message) {
				// TODO Auto-generated method stub
				register_verifycode.setText(getDYnamicPassword(message));
//				register_verifycode.setText(message);
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//注销短信监听广播
		this.unregisterReceiver(mSMSBroadcastReceiver);
	}
	

	/**
	 * 计时器
	 */
	public class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		public void onFinish() {
			verifybutton.setText("获取验证码");
			verifybutton.setClickable(true);
		}

		public void onTick(long millisUntilFinished) {
			verifybutton.setClickable(false);
			verifybutton.setText(millisUntilFinished / 1000 + "秒");
			if(millisUntilFinished/1000==1){
					voice_verification_code.setClickable(true);
					voice_verification_code.setText(Html.fromHtml("收不到短信? 使用"+"<font color=\"#0066cc\">语音验证码</font>"));
			}else{
					voice_verification_code.setClickable(false);
					voice_verification_code.setText(Html.fromHtml("<font color=\"#999999\">收不到短信? 使用语音验证码</font>"));
			}
		}
	}
	/**
	 * 语音验证码计时器
	 */
	public class TimeCount1 extends CountDownTimer {
		public TimeCount1(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		public void onFinish() {
			
		}

		public void onTick(long millisUntilFinished){
			textView3.setClickable(false);
			textView3.setText(millisUntilFinished /1000+"");
			if(millisUntilFinished/1000==1){
				textView1.setVisibility(View.GONE);
				textView2.setVisibility(View.GONE);
				textView3.setVisibility(View.GONE);
				verifybutton.setClickable(true);
				verifybutton.setBackgroundColor(Color.rgb(0,102,204));
			}else{
				verifybutton.setClickable(false);
				verifybutton.setBackgroundColor(Color.rgb(200, 199, 204));
			}
				
		}
	}

	/**
	 * 初始化控件和监听事件
	 */
	private void initView() {
		// 注册初始化
		vq = (View)findViewById(R.id.vq);
		register_title = (TextView) findViewById(R.id.register_title);
		consent = (TextView) findViewById(R.id.consent);
		consent.setText(Html.fromHtml("我已经阅读并同意"
				+ "<font color=\"#D51A1B\">《银板客用户注册协议》</font>"));
		voice_verification_code=(TextView) findViewById(R.id.voice_verification_code);
		voice_verification_code.setText(Html.fromHtml("收不到短信? 使用"+"<font color=\"#0066cc\">语音验证码</font>"));
		title_bar_back_btn = (LinearLayout) findViewById(R.id.title_bar_back_btn);
		register_title = (TextView) findViewById(R.id.register_title);
		registerBtn = (Button) findViewById(R.id.register_submit);
		register_username = (EditText) findViewById(R.id.register_username);
		register_password = (EditText) findViewById(R.id.register_password);
		register_confirm_password = (EditText) findViewById(R.id.register_confirm_password);
		register_verifycode = (EditText) findViewById(R.id.register_verifycode);
		register_recommend = (EditText) findViewById(R.id.register_recommend);
		textView2=(TextView) findViewById(R.id.textView2);
		textView3=(TextView) findViewById(R.id.textView3);
		textView1=(TextView) findViewById(R.id.textView1);
		verifybutton = (TextView) findViewById(R.id.verifybutton);
		register_layout = (RelativeLayout) findViewById(R.id.register_layout);
		radio_capital_division = (RelativeLayout) findViewById(R.id.radio_capital_division);
		radio_wealth_master = (RelativeLayout) findViewById(R.id.radio_wealth_master);
		radio_investor = (RelativeLayout) findViewById(R.id.radio_investor);
		registerBtn.setOnClickListener(new MyRegisterListener());
		radio_wealth_master.setOnClickListener(new MyOnclickListener());
		radio_capital_division.setOnClickListener(new MyCapitalClickListener());
		radio_investor.setOnClickListener(new MyInvestorClickListener());
		cfmp_iv =(ImageView) findViewById(R.id.cfmp);
		cfmp_tv=(TextView) findViewById(R.id.cfmp_tv);
		capital_iv =(ImageView) findViewById(R.id.capital);
		capital_tv=(TextView) findViewById(R.id.capital_tv);
		investor_iv=(ImageView) findViewById(R.id.investor);
		investor_tv=(TextView) findViewById(R.id.investor_tv);
		check_register = (ImageView) findViewById(R.id.check_register);
		consent = (TextView) findViewById(R.id.consent);
		consent.setOnClickListener(new ConsentOnclickListener());
		title_bar_back_btn.setOnClickListener(new TitleOnclickListener());
		verifybutton.setOnClickListener(new PhoneOnClickListener(0));//短信验证码
		voice_verification_code.setOnClickListener(new PhoneOnClickListener(1));//语音验证码
//		mSMSBroadcastReceiver = new SMSBroadcastReceiver();
//		mSMSBroadcastReceiver.setOnReceivedMessageLiatener(new MessageListener() {
//			
//			@Override
//			public void onReceived(String message) {
//				// TODO Auto-generated method stub
//				register_verifycode.setText(message);
//			}
//		});
	}
    public class MyOnclickListener implements OnClickListener{ //财富师

		@Override
		public void onClick(View arg0) {
			cfmp_iv.setImageDrawable(getResources().getDrawable(R.drawable.c_click));
			cfmp_tv.setTextColor(Color.BLACK);
			investor_iv.setImageDrawable(getResources().getDrawable(R.drawable.a_unclick));
			investor_tv.setTextColor(Color.rgb(153, 153, 153));
			capital_iv.setImageDrawable(getResources().getDrawable(R.drawable.b));
			capital_tv.setTextColor(Color.rgb(153, 153, 153));
			register_recommend.setVisibility(View.VISIBLE);
			vq.setVisibility(View.VISIBLE);
			type=2;
			role = "cfmp";			
		}
    	
    }
    public class MyInvestorClickListener implements OnClickListener{ //投资者

		@Override
		public void onClick(View arg0) {
			cfmp_iv.setImageDrawable(getResources().getDrawable(R.drawable.c));
			cfmp_tv.setTextColor(Color.rgb(153, 153, 153));
			investor_iv.setImageDrawable(getResources().getDrawable(R.drawable.a));
			investor_tv.setTextColor(Color.BLACK);
			capital_iv.setImageDrawable(getResources().getDrawable(R.drawable.b));
			capital_tv.setTextColor(Color.rgb(153, 153, 153));
			register_recommend.setVisibility(View.GONE);
			vq.setVisibility(View.GONE);
			type=3;
			role = "investor";
		}
    	
    }
    public class MyCapitalClickListener implements OnClickListener{ //资本师

		@Override
		public void onClick(View arg0) {
			capital_iv.setImageDrawable(getResources().getDrawable(R.drawable.capital_click));
			capital_tv.setTextColor(Color.BLACK);
			cfmp_iv.setImageDrawable(getResources().getDrawable(R.drawable.c));
			cfmp_tv.setTextColor(Color.rgb(153, 153, 153));
			investor_iv.setImageDrawable(getResources().getDrawable(R.drawable.a_unclick));
			investor_tv.setTextColor(Color.rgb(153, 153, 153));
			register_recommend.setVisibility(View.GONE);
			vq.setVisibility(View.GONE);
			type=1;
			role = "fundraiser";
		}
    	
    }
	public class PhoneOnClickListener implements OnClickListener {
		private int type;
		public PhoneOnClickListener(int type){
			this.type = type;
		}
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			phone = register_username.getText().toString().trim();
			Matcher m = p.matcher(phone);
			if (phone.length() == 11 && m.matches()) {
				progressDialog.show();
				client.get(HttpConfig.URL_REGISTER_IS_EXSIT + phone,
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(int statusCode,
									Header[] headers, JSONObject response) {
								boolean tag = false;
								try {
									JSONObject obj = response
											.getJSONObject("result");
									boolean flag = response
											.getBoolean("success");
									String errCode = obj.getString("errorCode");
									if (flag && errCode.equals("success")) {
										Toast.makeText(context, "手机号已被注册",
												Toast.LENGTH_SHORT).show();
										progressDialog.dismiss();
									} else {
										  StringEntity entity =null;
										try{
											String enc =Des.strEnc(phone,"ronganonline","yinbanke","123456");
										    byte[]bytes=enc.toString().getBytes();
											String mobile = Base64.encodeToString(bytes,Base64.DEFAULT);
											JSONObject json = new JSONObject();
											json.put("mobile", mobile);
											entity = new StringEntity(json.toString());
										}catch(Exception e){
											e.printStackTrace();
										}
										if(type==0){
											client.post(RegisterActivity.this,HttpConfig.URL_REQUEST_VERIFYCODE,entity,"application/json",
													new JsonHttpResponseHandler() {
														@Override
														public void onSuccess(
																int statusCode,
																Header[] headers,
																JSONObject response) {
															try {
																JSONObject obj = response.getJSONObject("result");
																boolean flag = response.getBoolean("success");
																String errCode = obj.getString("errorCode");
																if (flag&& errCode.equals("success")) {
																	timeCount.start();
																} else if (errCode != null&& errCode.equals("mobilephoneIsNull")) {
																	Toast.makeText(context,"手机号为空",Toast.LENGTH_SHORT).show();
																}else if(errCode != null&& errCode.equals("mobileIsNotValid")){
																	Toast.makeText(context,"手机号不合法",Toast.LENGTH_SHORT).show();
																}else if(errCode != null&& errCode.equals("sendMessageIntervalTooShort")){
																	Toast.makeText(context,"发送过于频繁",Toast.LENGTH_SHORT).show();
																}else if(errCode != null&& errCode.equals("sendMessageSystemError")){
																	Toast.makeText(context, "暂时无法处理请求，请稍后重试",
																			Toast.LENGTH_SHORT).show();
																}
																progressDialog.dismiss();
															} catch (Exception e) {
																e.printStackTrace();
																progressDialog.dismiss();
															}
														}
														@Override
														public void onFailure(
																int statusCode,
																Header[] headers,
																Throwable throwable,
																JSONObject errorResponse) {
															// TODO Auto-generated
															// method stub
														}
													});
									}else if(type==1){
										client.post(RegisterActivity.this,HttpConfig.VOICE_VERIFICATION_CODE,entity,"application/json",
												new JsonHttpResponseHandler() {
													@Override
													public void onSuccess(
															int statusCode,
															Header[] headers,
															JSONObject response) {
														try {
															JSONObject obj = response.getJSONObject("result");
															boolean flag = response.getBoolean("success");
															String errCode = obj.getString("errorCode");
															if (flag&& errCode.equals("success")) {
																textView1.setVisibility(View.VISIBLE);
																textView2.setVisibility(View.VISIBLE);
																textView3.setVisibility(View.VISIBLE);
																timeCount1.start();
															} else if (errCode != null&& errCode.equals("mobilephoneIsNull")) {
																Toast.makeText(context,"手机号为空",Toast.LENGTH_SHORT).show();
															}else if(errCode != null&& errCode.equals("mobileIsNotValid")){
																Toast.makeText(context,"手机号不合法",Toast.LENGTH_SHORT).show();
															}else if(errCode != null&& errCode.equals("sendMessageIntervalTooShort")){
																Toast.makeText(context,"发送过于频繁",Toast.LENGTH_SHORT).show();
															}else if(errCode != null&& errCode.equals("sendMessageSystemError")){
																Toast.makeText(context, "暂时无法处理请求，请稍后重试",
																		Toast.LENGTH_SHORT).show();
															}
															progressDialog.dismiss();
														} catch (Exception e) {
															e.printStackTrace();
															progressDialog.dismiss();
														}
													}
													@Override
													public void onFailure(
															int statusCode,
															Header[] headers,
															Throwable throwable,
															JSONObject errorResponse) {
														// TODO Auto-generated
														// method stub
													}
												});
									}
									progressDialog.dismiss();
								}
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
								}
							}
							
							@Override
							public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
								super.onFailure(statusCode, headers, throwable, errorResponse);
								Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
								progressDialog.dismiss();
							}
						});
				Log.i("Anthony", HttpConfig.URL_REQUEST_VERIFYCODE + phone);
			} else if (phone.length() == 0) {
				Toast.makeText(context, "手机号不能为空", Toast.LENGTH_SHORT).show();
				return;
			} else {
				Toast.makeText(context, "手机号无效", Toast.LENGTH_SHORT).show();
				return;
			}
		}

	}
	//点击用户协议
	public class ConsentOnclickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(RegisterActivity.this,AgreementActivity.class);
			startActivity(intent);
		}		
	}	
	
	// 注册监听事件

	public class MyRegisterListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			registerMethod(type);
		}
	}

	// 复选框状态
	public class MyOnCheckChangeListener implements
			android.widget.CompoundButton.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				registerBtn.setBackgroundResource(R.drawable.shap);
			} else {
				registerBtn.setBackgroundResource(R.drawable.shapgray);
			}
		}
	}

	// 处理
	// 返回按钮
	public class TitleOnclickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
//			Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
//			startActivity(intent);
			RegisterActivity.this.finish();
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
		}
	}

	// 注册的方法
	private void registerMethod(final int type) { //注册
			StringEntity entity = null;
			String check1 = "[\\u4e00-\\u9fa5]";
			Pattern regex2 = Pattern.compile(check1);
			String phone = register_username.getText().toString().trim();
			String password = register_password.getText().toString().trim();
			String verifycode = register_verifycode.getText().toString().trim();
			String passwordConfirm = register_confirm_password.getText().toString().trim();
			String recommend = register_recommend.getText().toString().trim();
			Matcher m = p.matcher(phone);
			if (!m.matches()) {
				Toast.makeText(context, "用户名无效", Toast.LENGTH_SHORT).show();
			} else if (password == "" || password.length() == 0
					|| password.equals("")) {
				Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT).show();
			} else if (regex2.matcher(password).matches()) {
				Toast.makeText(context, "密码格式不正确", Toast.LENGTH_SHORT).show();
			} else if (password.length() < 6||password.length()>20) {
				Toast.makeText(context, "密码不能小于6位且不能大于20位", Toast.LENGTH_SHORT).show();
			} else if (passwordConfirm.equals("")
					|| passwordConfirm.length() == 0) {
				Toast.makeText(context, "确认密码不能为空", Toast.LENGTH_SHORT).show();
			} else if (!(passwordConfirm.equals(password))) {
				Toast.makeText(context, "两次输入的密码不一致", Toast.LENGTH_SHORT)
						.show();
			} else if (verifycode.equals("") || verifycode.length() == 0) {
				Toast.makeText(context, "验证码不能为空", Toast.LENGTH_SHORT).show();
			} 
			else {
				JSONObject json = new JSONObject();
				try {
					/**
					 * {"type":"3","mobile":"18516581281","password":"123456","veifyCode":"2891"}
					 */
							json.put("mobile", register_username.getText());
							json.put("type", type);
							json.put("password", register_password.getText());
							json.put("veifyCode", register_verifycode.getText());
							json.put("recommender", register_recommend.getText());
							json.put("recommenderFlag","0");
							entity = new StringEntity(json.toString(),"utf-8");
					Log.i("Anthony", HttpConfig.URL_REGIST + entity);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
				Log.i("Anthony", HttpConfig.URL_REGIST + entity);
				if(HttpConfig.URL_REGIST!=null&&entity!=null){
					progressDialog.show();
				client.post(context, HttpConfig.URL_REGIST, entity, "application/json",
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(int statusCode,
									Header[] headers, JSONObject response) {
								try {
									userBean = new UserBean();
									JSONObject obj = response.getJSONObject("result");
									boolean flag = response.getBoolean("success");
									String errCode = obj.getString("errorCode");
									if (flag&& errCode.equals("success")) {
										JSONObject object = obj.getJSONObject("user");
										userBean.setUserId(object.get("id") == null|| object.get("id").toString().equals("null") ? "": object.getString("id"));
										userBean.setUserName(object.get("mobile") == null|| object.get("mobile").toString().equals("null") ? "": object.getString("mobile"));
										userBean.setUserRole(role);
										if (type == 2) {
											userExtend(userBean);
										} 
										else {
											Toast.makeText(context, "注册成功",Toast.LENGTH_SHORT).show();
											

											intent = new Intent();
											intent.setClass(context,LoginActivity.class);
												startActivity(intent);	
											RegisterActivity.this.finish();
											overridePendingTransition(
													android.R.anim.slide_in_left,
													android.R.anim.slide_out_right);
										}
									} else if (errCode != null&& errCode.equals("mobileIsNull")) {
										Toast.makeText(context, "电话号码无效",
												Toast.LENGTH_SHORT).show();
									} else if (errCode != null&& errCode.equals("verifycodeError")) {
										Toast.makeText(context, "验证码错误",Toast.LENGTH_SHORT).show();
									} else if (errCode != null&& errCode.equals("mobileIsExist")) {
										Toast.makeText(context, "手机号已存在",Toast.LENGTH_SHORT).show();
									}else if(errCode != null&& errCode.equals("recommenderIsNotCfmpOrRonganStaff")){
										Toast.makeText(context, "推荐人不存在",Toast.LENGTH_SHORT).show();
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								progressDialog.dismiss();
							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, Throwable throwable,
									JSONObject errorResponse) {
								// TODO Auto-generated method stub
								Toast.makeText(context, "注册失败",
										Toast.LENGTH_SHORT).show();
							}
							@Override
							public void onFailure(int statusCode,
									Header[] headers, Throwable throwable,
									JSONArray errorResponse) {
								// TODO Auto-generated method stub
								super.onFailure(statusCode, headers, throwable, errorResponse);
								Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
								progressDialog.dismiss();
							}
						});
				}
//			}
		}

	// 保存用户扩展信息
	private void userExtend(final UserBean userBean) {
		
		if(register_recommend.getText().length()==0){
			Toast.makeText(context, "注册成功",Toast.LENGTH_SHORT).show();			
			intent = new Intent();
			intent.setClass(context,LoginActivity.class);
				startActivity(intent);	
			RegisterActivity.this.finish();
			overridePendingTransition(
					android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
		}else{
			StringEntity entity = null;
			try {
				JSONObject json = new JSONObject();
				json.put("userId", userBean.getUserId());
				json.put("catalog", userBean.getUserRole());
				json.put("name", "recommender");
				json.put("value", register_recommend.getText());
				entity = new StringEntity(json.toString());
				Log.i("Anthony", HttpConfig.URL_USEREXTEBD + entity);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.i("Anthony", HttpConfig.URL_USEREXTEBD + entity);
			client.post(context, HttpConfig.URL_USEREXTEBD, entity, "application/json",
					new JsonHttpResponseHandler() {
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							try {
								JSONObject object = response.getJSONObject("result");
								String errorCode = object.getString("errorCode");
								if (errorCode != null&& errorCode.equals("success")) {
									StringEntity entityString = null;
									try {
										JSONObject json = new JSONObject();
										json.put("userId", userBean.getUserId());
										json.put("catalog", userBean.getUserRole());
										json.put("name", "recommender");
										json.put("value",register_recommend.getText());
										entityString = new StringEntity(json.toString());			
									} catch (Exception e) {
										e.printStackTrace();
									}
									RequestParams params = new RequestParams();
									params.put("userId", userBean.getUserId());
									client.post(context, HttpConfig.URL_SENDEMAIURL, entityString, "application/json",
											new JsonHttpResponseHandler() {
												public void onSuccess(int statusCode,Header[] headers,
														JSONObject response) {
													JSONObject obj;
													try {
														obj = response.getJSONObject("result");
														boolean flag = response.getBoolean("success");
														String errCode = obj.getString("errorCode");
														if (flag&& errCode.equals("success")) {
															Toast.makeText(context,"注册成功",Toast.LENGTH_SHORT).show();									
																intent = new Intent();
																intent.setClass(context,LoginActivity.class);
																startActivity(intent);
															RegisterActivity.this.finish();
															overridePendingTransition(
																	android.R.anim.slide_in_left,
																	android.R.anim.slide_out_right);
														}
														progressDialog.dismiss();
													} catch (JSONException e) {
														// TODO Auto-generated catch
														// block
														e.printStackTrace();
														progressDialog.dismiss();
													}
												};

												public void onFailure(int statusCode,Header[] headers,Throwable throwable,
														JSONObject errorResponse) {
													Toast.makeText(context,"邮件发送失败",Toast.LENGTH_SHORT).show();
												};
											});
								} else if (errorCode != null&& errorCode.equals("userNoLogin")) {
									Toast.makeText(context, "用户名未登入",Toast.LENGTH_SHORT).show();
								} else if (errorCode != null&& errorCode.equals("attrIsNotVaild ")) {
									Toast.makeText(context, "参数有误",Toast.LENGTH_SHORT).show();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							// TODO Auto-generated method stub
							Toast.makeText(context, "注册失败", Toast.LENGTH_SHORT).show();
						}
						
						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONArray errorResponse) {
							// TODO Auto-generated method stub
							super.onFailure(statusCode, headers, throwable, errorResponse);
							Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
							progressDialog.dismiss();
						}
					});
		}		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * 判断网络是否可用
	 * 
	 */
	public boolean isNetWorkConnected() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		RegisterActivity.this.finish();
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
		
	}
}
