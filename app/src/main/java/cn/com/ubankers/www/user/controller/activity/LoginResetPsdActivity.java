package cn.com.ubankers.www.user.controller.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.authentication.controller.activity.LoginActivity;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.utils.Des;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginResetPsdActivity extends Activity {
	private Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(16[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
	private AsyncHttpClient client;
	private Context context;		
	private EditText newPassword, confirmPassword;
	private Button commitBtn;
	private UserBean userBean;
	private String role = "tourist";
	private SQLiteDatabase db;
	private Intent intent;
	
	private String mobileNumber,verifyCode;
	private LinearLayout back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.update_password);
		initView();
		context = this;
		client = MyApplication.app.getClient(context);
	    intent =this.getIntent();
		if(intent!=null){
		 Bundle bundle =intent.getExtras();
		 bundle.getString("mobileNumber");
		 verifyCode = bundle.getString("verifyCode");
		 mobileNumber = bundle.getString("mobileNumber");
		}
		MyApplication.getInstance().addActivity(this);
	}
	

	private void initView() {
		// TODO Auto-generated method stub		
		newPassword=(EditText)findViewById(R.id.new_password);
		confirmPassword=(EditText)findViewById(R.id.confirm_password);
		commitBtn=(Button)findViewById(R.id.commitBtn);
        back=(LinearLayout)findViewById(R.id.title_bar_back_btn);
		
		back.setOnClickListener(new BackOnClickListener());		
		commitBtn.setOnClickListener(new MyOnclickListener());				
	}
	
	public class BackOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			LoginResetPsdActivity.this.finish();
	        overridePendingTransition(
			android.R.anim.slide_in_left,
			android.R.anim.slide_out_right);
		}
		
	}
	
	/**
	 * 修改密码*/
	private class MyOnclickListener implements OnClickListener {
		String check1 = "[\\u4e00-\\u9fa5]";
		Pattern regex2 = Pattern.compile(check1);
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String password = newPassword.getText().toString().trim();
			String confirmP=confirmPassword.getText().toString().trim();
			if(password =="" || password.length() == 0 || password.equals("")){
				Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT).show();
			}else if(regex2.matcher(password).matches()){
				Toast.makeText(context, "密码格式不正确", Toast.LENGTH_SHORT).show();
			}else if(password.length()<6||password.length()>20){
				Toast.makeText(context, "密码不能小于6位且不能大于20位", Toast.LENGTH_SHORT).show();
			}else if(confirmP.equals("")||confirmP.length() == 0){
				Toast.makeText(context, "确认密码不能为空", Toast.LENGTH_SHORT).show();
			}else if(!(confirmP.equals(password))){
				Toast.makeText(context, "密码与确认密码不一致", Toast.LENGTH_SHORT).show();
			}else{
				StringEntity entity=null;
				try {							
				    JSONObject json = new JSONObject();
					json.put("mobilephone", mobileNumber);
					json.put("verifycode",  verifyCode);
					json.put("password",  newPassword.getText().toString());
					entity = new StringEntity(json.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}			
			client.post(LoginResetPsdActivity.this, HttpConfig.URL_RESET_PASSWORD, entity,"application/json", new JsonHttpResponseHandler(){
				
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					// TODO Auto-generated method stub
					try {
						JSONObject obj = response.getJSONObject("result");
						boolean flag = response.getBoolean("success");
						if (flag&&obj.getString("errorCode").equals("success")) {
							Toast.makeText(context, "ok",Toast.LENGTH_SHORT).show();
							Intent intent=new Intent(LoginResetPsdActivity.this, LoginActivity.class);
							LoginResetPsdActivity.this.startActivity(intent);
							LoginResetPsdActivity.this.finish();
					        overridePendingTransition(
							android.R.anim.slide_in_left,
							android.R.anim.slide_out_right);
						} else if(obj.getString("errorCode").equals("nameIsNull")){
							Toast.makeText(context, "用户名为空",Toast.LENGTH_SHORT).show();
						}else if(obj.getString("errorCode").equals("passwordIsNotTrue")){
							Toast.makeText(context, "密码不正确",Toast.LENGTH_SHORT).show();
						}else if(obj.getString("errorCode").equals("passwordIsNull")){
						    Toast.makeText(context, "密码为空",Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						
					}
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
				}
			});
			}
		}
		
	}
	
}
