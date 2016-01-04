package cn.com.ubankers.www.user.controller.activity;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.WealthBean;
import cn.com.ubankers.www.utils.LoginDialog;
import cn.com.ubankers.www.utils.NetReceiver;
import cn.com.ubankers.www.utils.NetReceiver.NetState;
import cn.com.ubankers.www.widget.ProcessDialog;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class WealthMattersActivity extends Activity implements OnClickListener {
	private ImageView aboutTreasure_Image;
	private LinearLayout ll;
	private ProcessDialog myDialog;
	private TextView buildTeamView;
	private EditText treasureHourse_name,treasureHourse_sign;
	private View view;
	private Button buildView,callView;
	private AlertDialog dialog;
	private AsyncHttpClient client;
	private String info;
	private WealthBean wealthBean;
	private String studioName;
	private String slogan;
	private Context context;
	private int fied;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutcmfp_activity);
		NetState connected = NetReceiver.isConnected(this);
		context = this;
		if(connected==connected.NET_NO){
				Toast.makeText(this, "当前网络不可用",Toast.LENGTH_SHORT).show();
		}
		if(myDialog==null){
			myDialog= ProcessDialog.createDialog(WealthMattersActivity.this, "正在加载中...");
		}
		 client = MyApplication.app.getClient(context);
		 initView();
		 MyApplication.getInstance().addActivity(this);	 
	}
	private void initView() {
		// TODO Auto-generated method stub
		buildTeamView = (TextView) findViewById(R.id.buildTeamView);
		aboutTreasure_Image = (ImageView) findViewById(R.id.aboutTreasure_Image);
		ll=(LinearLayout)findViewById(R.id.linel);
		ll.setOnClickListener(this);
		buildTeamView.setOnClickListener(this);
		aboutTreasure_Image.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.buildTeamView:
			client.post(HttpConfig.URL_QUALIFIED_WEALTH_DIVISION, new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					try {
						boolean flag = response.getBoolean("success");
						if(flag){
							JSONObject jsonObject = response.getJSONObject("result");
							JSONObject jsonObject2 = jsonObject.getJSONObject("info");
							fied =jsonObject2.getInt("qualified");
							
							if(fied == 1){
							    dialog = new AlertDialog.Builder(context).create();
								dialog.setCanceledOnTouchOutside(false);
								view = LayoutInflater.from(context).inflate(R.layout.treasure_dialog, null);
								treasureHourse_name =(EditText) view.findViewById(R.id.treasureHourse_name);
								treasureHourse_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
								treasureHourse_sign =(EditText) view.findViewById(R.id.treasureHourse_sign);
								treasureHourse_sign.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
								buildView = (Button) view.findViewById(R.id.buildView);
								buildView.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View arg0) {
											// TODO Auto-generated method stub
											System.out.println(treasureHourse_name.getText().toString()+"222222222222");
											if(treasureHourse_name.getText().toString() == null || treasureHourse_name.getText().toString().length()==0){
												Toast.makeText(WealthMattersActivity.this, "请完善工作室名称",Toast.LENGTH_SHORT).show();
											}else {
												buildTreasurer(treasureHourse_name.getText().toString() ,treasureHourse_sign.getText().toString());
											}
											
										}
									});
								dialog.setView(view);
								dialog.show();
							}else {
								 AlertDialog.Builder alert = new AlertDialog.Builder(context);
					                alert.setTitle("提示");
					                alert.setMessage("您的名片未进行认证，暂不能进行该操作");
					                alert.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
					                public void onClick(DialogInterface dialog, int whichButton) {
					                  
					                  }
					                });
					                alert.show();
							}
							
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					super.onFailure(statusCode, headers, throwable,
							errorResponse);
					if(statusCode==401){
						try {
							boolean flag = errorResponse.getBoolean("success");
							if(flag==false){
								JSONObject jsonObject = errorResponse.getJSONObject("result");
								String errorCode = jsonObject.getString("errorCode");
								if(errorCode.equals("noLogin")){
									Toast.makeText(context, "用户未登录", Toast.LENGTH_SHORT).show();
									MyApplication.app.setUser(null);
									MyApplication.app.setClient(null);
									LoginDialog loginDialog = new LoginDialog(context,0,0);
									loginDialog.onLogin();
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				}
			});			
			break;
		case R.id.linel:
		case R.id.aboutTreasure_Image:
			WealthMattersActivity.this.finish();
			break;
		}
	}
	//创建财富工作室
	private void buildTreasurer(String studioName,String slogan){
		
		StringEntity entity = null;
		JSONObject json =null ;
		try{
			json = new JSONObject();
			json.put("studioName", studioName);
			json.put("slogan", slogan);
			entity = new StringEntity(json.toString(),"utf-8");	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		client.post(this, HttpConfig.URL_CREAT_STUDIO, entity, "application/json", new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				
				try {
					JSONObject obj = response.getJSONObject("result");
					boolean flag = response.getBoolean("success");
					String errCode = obj.getString("errorCode");
					if(!obj.isNull("info")){
					info =obj.getString("info");
					}
					if (flag && errCode.equals("success")) {
					 Gson gson = new Gson();
					 wealthBean = gson.fromJson(info, WealthBean.class);
					 Intent intent  = new Intent(WealthMattersActivity.this,WealthCenterActivity.class);
					 Bundle bundle = new Bundle();
					 bundle.putSerializable("wealthBean", wealthBean);
					 intent.putExtras(bundle);
					 startActivity(intent);
					 dialog.dismiss();
					 WealthMattersActivity.this.finish();
					}else{
						Toast.makeText(WealthMattersActivity.this, "已经申请过了",Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
		});
	}
}
