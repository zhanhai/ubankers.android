package cn.com.ubankers.www.user.controller.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.service.EmailService;
import cn.com.ubankers.www.utils.PhotoUtil;
import cn.com.ubankers.www.utils.Regular;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EmailActivity extends Activity implements OnClickListener {
	private EditText youxiang_email;
	private String email;
	private Button youxiang_queren;
	private String userUrl;
	private View youxiang_back;
	private AsyncHttpClient client;
	private UserBean userBean;
	private  int buttstate;
	private Activity activity;
	EmailService emailservice;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity=this;	
		Intent intent=getIntent();
		userBean=(UserBean) intent.getSerializableExtra("userBean");
		emailservice=new EmailService(activity,userBean);
		client = MyApplication.app.getClient(activity);
		setContentView(R.layout.youxiangrenzhengactivity);
		initView();
		if(userBean.getEmail()==null){
			buttstate=1;
			bangding();
		}else{
			if(!userBean.getEmail().equals("")&&userBean.getEmail()!=null){	
				buttstate=1;
				bangding();		
			}else if(userBean.getEmail().equals("")){
				buttstate=2;//没有绑定
			}
		}
		
		 MyApplication.getInstance().addActivity(this);	
			
	}
	public void initView(){
		youxiang_email=(EditText)findViewById(R.id.youxiang_email);
		 youxiang_queren=(Button)findViewById(R.id.youxiang_queren);
		 youxiang_back=(View)findViewById(R.id.youxiang_back);	
		 youxiang_back.setOnClickListener(this);
		 youxiang_queren.setOnClickListener(this);
	}
	public void bangding(){
		youxiang_email.setText(userBean.getEmail());
		youxiang_email.clearFocus();
		youxiang_email.setFocusable(false);
		youxiang_queren.setText("返回");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		super.onBackPressed();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.youxiang_back:
			//数据是使用Intent返回
            intent = new Intent();
            intent.putExtra("buttstate", buttstate);
            this.setResult(RESULT_OK, intent);
			EmailActivity.this.finish();
			break;
		case R.id.youxiang_queren:	
			if(buttstate==1){ //绑定
				EmailActivity.this.finish();			
			}else if(buttstate==2){ //未绑定
				QuanRen();
			}
			
		default:
			break;
		}
	}
	public void QuanRen( ){
		email =youxiang_email.getText().toString().trim();
		 if(!email.equals("")){
				if(Regular.checkEmail(email)==true){	
					emailservice.initData(email);				
				}else{
					 Toast.makeText(EmailActivity.this, "邮箱不合法请重新输入", Toast.LENGTH_SHORT).show();
				} 
		 }else{
			 Toast.makeText(EmailActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
		 }
				}
	
	

}


