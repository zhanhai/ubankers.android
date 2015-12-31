package cn.com.ubankers.www.authentication.controller.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.authentication.service.LoginService;
import cn.com.ubankers.www.user.controller.activity.UserCenterActivity;
import cn.com.ubankers.www.user.controller.activity.VerifyCodeActivity;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.widget.MyDialog;
import cn.sharesdk.framework.ShareSDK;

public class LoginActivity extends Activity 
		{
	private Context context;
	private Button loginBtn;
	private TextView login_line, register,forgetPassword;
	private View back;
	public EditText userName, password;
	private SQLiteDatabase db;
	private ImageView login_qq, login_sinaweibo, login_wechat;
	public MyDialog myDialog;
	private boolean ifNetWorkConnected = false;
	private static final int MSG_ACTION_CCALLBACK = 2;
	private UserBean userBean;
	private LoginService loginService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login_activity);
		userBean=MyApplication.app.getUser();
		// 设置title样式
		context = LoginActivity.this;
		ShareSDK.initSDK(this);
		loginService = new LoginService(context);
		initView();
		if (isNetWorkConnected()) {
		} else {
			ifNetWorkConnected = true;
			Toast.makeText(getApplicationContext(), "无法连接到网络，请稍候再试", 1).show();
		}
//		if (myDialog == null){
//			myDialog = MyDialog.createDialog(this,"正在加载中...");
//		}
		MyApplication.getInstance().addActivity(this);
 	}

	private void initView() {
		// 登录初始化		
		loginBtn = (Button) findViewById(R.id.LoginBtn);
		userName = (EditText) findViewById(R.id.loginName);
		password = (EditText) findViewById(R.id.loginPassword);
		loginBtn.setOnClickListener(new MyOnclickListener());
		back = findViewById(R.id.back);
		back.setOnClickListener(new BackOnclickListener());
//		userName.setText(userBean.getUserName());	 
		userName.setText(getUsername());	 
		forgetPassword=(TextView)findViewById(R.id.forget_password);
		forgetPassword.setOnClickListener(new ForgetPasswordListener());
	}
	
	 @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		LoginActivity.this.finish();
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}
	
	/**
	 * 用户忘记密码监听
	 */
	 public class ForgetPasswordListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(LoginActivity.this, VerifyCodeActivity.class);
			startActivity(intent);
			LoginActivity.this.finish();
		}
		
	}
	public class registerListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// UserCenterActivity.this.finish();
			// overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
			LoginActivity.this.startActivity(intent);
		}
	}

	
	/**
     * normal login   
	 */
	private class MyOnclickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			loginService.getLogin();
		}
	}
	/**
	 * back widget Listener
	 */
	public class BackOnclickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {  
//			Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
			
			LoginActivity.this.finish();
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			UserCenterActivity.fl_bg.setVisibility(View.GONE);
			UserCenterActivity.userCenterHead.setVisibility(View.GONE);
		}
	}
	/**
	 * 判断网络是否可用
	 * 
	 */
	public boolean isNetWorkConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}
	
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK&&event.getRepeatCount() == 0) {
////			Intent intent = new Intent(getApplicationContext(),MainActivity.class);
////			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////			startActivity(intent);
//			LoginActivity.this.finish();  
//			overridePendingTransition(android.R.anim.slide_in_left,
//					android.R.anim.slide_out_right);
//			     return true;
//		}
//		// 拦截MENU按钮点击事件，让他无任何操作
//		if (keyCode == KeyEvent.KEYCODE_MENU) {
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
		
		private String getUsername() { 
			SharedPreferences pref = getPreferences(Activity.MODE_PRIVATE); 
			String username = pref.getString("username", "");//如果没有，默认为"" 
			return username; 
			} 
		public void setUsername(String str) { 
			SharedPreferences pref = getPreferences(Activity.MODE_PRIVATE); 
			Editor edit = pref.edit();edit.putString("username", str); 
			edit.commit(); 
			} 
}
