package cn.com.ubankers.www.user.controller.activity;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.service.settingModifyPsdService;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SettingModifyPsdActivity extends Activity  implements OnClickListener {

	private View xiugai_back;
	private EditText xiugaimima_oldPassword;
	private EditText xiugaimima_Passwordb;
	private EditText xiugaimima_Password;
	private Button xiugai_minma;
	private String oldPassword;
	private String Password;
	private String Passwordb;
	private AsyncHttpClient client;
	private String  mimaUrl;
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 this.activity=this;
		setContentView(R.layout.xiugaimimaactivity);
		MyApplication.getInstance().addActivity(this);
		
		initView();
	    MyApplication.getInstance().addActivity(this);	
	   
	}
	public void initView(){
		xiugai_back=(View)findViewById(R.id.xiugai_back);
		xiugai_minma=(Button)findViewById(R.id.xiugai_minma);
		xiugaimima_oldPassword=(EditText)findViewById(R.id.xiugaimima_oldPassword);
		xiugaimima_Password=(EditText)findViewById(R.id.xiugaimima_Password);
		xiugaimima_Passwordb=(EditText)findViewById(R.id.xiugaimima_Passwordb);
		xiugai_back.setOnClickListener(this);
		xiugai_minma.setOnClickListener(this);
	 
	}
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.xiugai_back:
			SettingModifyPsdActivity.this.finish();
			break;
			
		case R.id.xiugai_minma:
			ClickOK();	
			break;

		default:
			break;
		}
	}
	
	//点击确定按钮
			private void  ClickOK(){
				oldPassword=xiugaimima_oldPassword.getText().toString();
				Password=xiugaimima_Password.getText().toString();
				Passwordb=xiugaimima_Passwordb.getText().toString();
				if(Password.length()<6){
					Toast.makeText(SettingModifyPsdActivity.this, "请输入6位以上密码", Toast.LENGTH_SHORT).show();
				}else{
					if(Password.equals(Passwordb)){
						if(oldPassword.equals(Password)||oldPassword.equals(Passwordb)){
							Toast.makeText(SettingModifyPsdActivity.this, "新密码与旧密码一致", Toast.LENGTH_SHORT).show();
						}else{
							//点击请求网络请求修改密码
							settingModifyPsdService settingModifyPsdService=new settingModifyPsdService(activity, oldPassword, Password);
							settingModifyPsdService.ChangePassword();
						}
						
					}else{
						Toast.makeText(SettingModifyPsdActivity.this, "两次输入密码不一样", Toast.LENGTH_SHORT).show();
					}
				}
			}
}
