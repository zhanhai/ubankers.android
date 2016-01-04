package cn.com.ubankers.www.user.controller.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.authentication.controller.activity.SMSBroadcastReceiver;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.utils.Des;
import cn.com.ubankers.www.widget.ProcessDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class VerifyCodeActivity extends Activity{
	private EditText mobileNumber,verifyCode;
	private Button nextBtn;
	private TextView verifyBtn,voice_verification_code,textView3,textView2,textView1;
	private String phone;
	private Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(16[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
	private AsyncHttpClient client;
	private Context context;
	private TimeCount timeCount;
	private LinearLayout back;
	private TimeCount1 timeCount1;
	private ProcessDialog progressDialog;
	private SMSBroadcastReceiver mSMSBroadcastReceiver;
	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.update_code);
		initView();
		context = this;
		client = MyApplication.app.getClient(context);
		if (progressDialog == null){
			progressDialog = ProcessDialog.createDialog(this, "正在加载中...");
		}
		timeCount = new TimeCount(60000, 1000);
		timeCount1 = new TimeCount1(60000,1000);
		MyApplication.getInstance().addActivity(this);
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		mobileNumber=(EditText)findViewById(R.id.mobileNumber);
		verifyCode=(EditText)findViewById(R.id.verifycode);
		verifyBtn=(TextView)findViewById(R.id.verifybutton);
		nextBtn=(Button)findViewById(R.id.next_Button);
		back=(LinearLayout)findViewById(R.id.title_bar_back_btn);
		voice_verification_code=(TextView) findViewById(R.id.voice_verification_code);
		voice_verification_code.setText(Html.fromHtml("收不到短信? 使用"+"<font color=\"#0066cc\">语音验证码</font>"));
		textView2=(TextView) findViewById(R.id.textView2);
		textView3=(TextView) findViewById(R.id.textView3);
		textView1=(TextView) findViewById(R.id.textView1);
		back.setOnClickListener(new BackOnClickListener());
		verifyBtn.setOnClickListener(new VerifyCodeOnClickListener(0));//短信验证码
		voice_verification_code.setOnClickListener(new VerifyCodeOnClickListener(1));//语音验证码
		nextBtn.setOnClickListener(new NextBtnOnClickListener());
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
				verifyCode.setText(getDYnamicPassword(message));
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
	
	public class BackOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			VerifyCodeActivity.this.finish();
	        overridePendingTransition(
			android.R.anim.slide_in_left,
			android.R.anim.slide_out_right);
		}
		
	}

	/**
	 * 计时器
	 */
	public class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		public void onFinish() {
			verifyBtn.setText("获取验证码");
			verifyBtn.setClickable(true);
		}

		public void onTick(long millisUntilFinished) {
			verifyBtn.setClickable(false);
			verifyBtn.setText(millisUntilFinished / 1000 + "秒");
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
				verifyBtn.setClickable(true);
				verifyBtn.setBackgroundColor(Color.rgb(0,102,204));
			}else{
				verifyBtn.setClickable(false);
				verifyBtn.setBackgroundColor(Color.rgb(200, 199, 204));
			}
				
		}
	}

	
	public class NextBtnOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub	
					StringEntity entity=null;
					phone = mobileNumber.getText().toString().trim();
					Matcher m = p.matcher(phone);
					if(phone.length() == 11&& m.matches()){
					try {							
					    JSONObject json = new JSONObject();
						json.put("mobilephone", phone);
						json.put("verifycode", verifyCode.getText().toString());
						entity = new StringEntity(json.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
					client.post(VerifyCodeActivity.this, HttpConfig.URL_RESET_CONFIRM_VERIFYCODE, entity, "application/json",new JsonHttpResponseHandler(){
						
						@Override
						public void onSuccess(int statusCode,
								Header[] headers, JSONObject response) {
							// TODO Auto-generated method stub
							try {
								JSONObject obj=response.getJSONObject("result");
								boolean flag=response.getBoolean("success");
								String errCode=obj.getString("errorCode");
								if(flag && errCode.equals("success")){
									Intent intent=new Intent(VerifyCodeActivity.this, LoginResetPsdActivity.class);
									Bundle bundle=new Bundle();
									bundle.putString("mobileNumber", mobileNumber.getText().toString());
									bundle.putString("verifyCode", verifyCode.getText().toString());
									intent.putExtras(bundle);
									VerifyCodeActivity.this.startActivity(intent);
									VerifyCodeActivity.this.finish();
							        overridePendingTransition(
									android.R.anim.slide_in_left,
									android.R.anim.slide_out_right);
								}else if(errCode !=null && errCode.equals("verfiycodeNotMatch")){
									Toast.makeText(context, "验证码有误", Toast.LENGTH_SHORT).show();
								}
								else if(errCode !=null && errCode.equals("verfiycodeNotFound")){
									Toast.makeText(context, "验证码不存在", Toast.LENGTH_SHORT).show();
								}else if( errCode.equals("verfiycodeIsNull")){
								Toast.makeText(context, "请输入验证码", Toast.LENGTH_SHORT).show();	
								}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						   }
						
						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							// TODO Auto-generated method stub
							super.onFailure(statusCode, headers, throwable, errorResponse);
							Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
						}
						});	
		          }else{
						Toast.makeText(context, "手机号不正确", Toast.LENGTH_SHORT).show();
					}
	 }
}
	
	/**
	 * 获取验证码*/
	public class VerifyCodeOnClickListener implements OnClickListener{
		private int type;
		public VerifyCodeOnClickListener(int type){
			this.type = type;
		}
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			phone = mobileNumber.getText().toString().trim();
			Matcher m = p.matcher(phone);
			if(phone.length() == 11 && m.matches()){
				client.get(HttpConfig.URL_PHONE_IS_USED+phone, new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						// TODO Auto-generated method stub
						try {
							JSONObject obj=response.getJSONObject("result");
							boolean flag=response.getBoolean("success");
							String errCode=obj.getString("errorCode");
							if(flag && errCode.equals("success")){
								StringEntity entity=null;
								try {							
									String enc =Des.strEnc(phone,"ronganonline","yinbanke","123456");
								    byte[]bytes=enc.toString().getBytes();
								    String mobile = Base64.encodeToString(bytes,Base64.DEFAULT);
								    JSONObject json = new JSONObject();
									json.put("mobile", mobile);
									entity = new StringEntity(json.toString());
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
								if(type==0){
										client.post(VerifyCodeActivity.this, HttpConfig.URL_RESET_VERIFYCODE, entity, "application/json",new JsonHttpResponseHandler(){
											
											@Override
											public void onSuccess(int statusCode,
													Header[] headers, JSONObject response) {
												// TODO Auto-generated method stub
												try {
													JSONObject obj=response.getJSONObject("result");
													boolean flag=response.getBoolean("success");
													String errCode=obj.getString("errorCode");
													if(flag && errCode.equals("success")){
														timeCount.start();
													}else if(errCode != null&& errCode.equals("verifycodeError")) {
												      Toast.makeText(context, "验证码错误",
														Toast.LENGTH_SHORT).show();
													}
												} catch (Exception e) {
													// TODO: handle exception
													e.printStackTrace();
												}
											}
											
											@Override
											public void onFailure(int statusCode,
													Header[] headers, Throwable throwable,
													JSONObject errorResponse) {
												// TODO Auto-generated method stub
												super.onFailure(statusCode, headers, throwable, errorResponse);
											}
										});
								}else if(type==1){
									client.post(VerifyCodeActivity.this,HttpConfig.FORGET_PASSWORD_VOICE_VERIFICATION_CODE,entity,"application/json",
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
							}else{
								Toast.makeText(context, "手机号没注册", Toast.LENGTH_SHORT).show();
								
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
						
						
						
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
		}
		
	}

}
