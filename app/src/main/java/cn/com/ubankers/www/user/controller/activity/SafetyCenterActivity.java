package cn.com.ubankers.www.user.controller.activity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.service.SafetyCenterService;
import cn.com.ubankers.www.widget.ProcessDialog;

	public class SafetyCenterActivity extends Activity  implements OnClickListener {
		private static final String DB_NAME = "rongan.db";
		private View security_back;
		private View layout_shenfenyanzheng_center;
		private View layout_youxiangrenzheng_center;
		private View layout_yinhangkarenzheng_us;
		private SQLiteDatabase db;
		public  TextView idcard_status, email_status, bankcard_status;
		public  View layout_mingpian;
		public  TextView bankcard_mingpianzhuantai;
		private SafetyCenterService safetycenterService;
		public ProcessDialog myDialog;
		public Context context;
		private UserBean userBean;
		private static final int IDCARD_REQUSET = 1; 
		private static final int EMAIL_REQUSET = 2; 
		private static final int BANK_REQUSET = 3; 
		private static final int FEILD_REQUSET = 5; 


		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.securitycenteractivity);
			if(MyApplication.app.getUser()!=null){
				userBean=MyApplication.app.getUser();
			}
			MyApplication.getInstance().addActivity(this);
            context=this;
            intview();
            safetycenterService=new SafetyCenterService(context);
            safetycenterService.initData();
		}
		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
		    safetycenterService.initData();
			super.onResume();
		}
		@Override
		protected void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
					if(requestCode==IDCARD_REQUSET){
						try{
							String idState = data.getStringExtra("idcard");
							if(idState.equals("1")){//
								((SafetyCenterActivity)context).idcard_status.setText("通过审核");
							}else if(idState.equals("0")){//
								((SafetyCenterActivity)context).idcard_status.setText("审核中");
							}else if(idState.equals("2")){//
								((SafetyCenterActivity)context).idcard_status.setText("未通过审核");
							}
						}catch(Exception e){
							e.printStackTrace();
						}
						
					}else if(requestCode==EMAIL_REQUSET){	
						
					}else if(requestCode==BANK_REQUSET){
						try{
							if(!data.getStringExtra("bankcard").equals("")){
								String bankcard = data.getStringExtra("bankcard");
								if(bankcard.equals("1")){//
									((SafetyCenterActivity)context).bankcard_status.setText("通过审核");
								}else if(bankcard.equals("0")){
									((SafetyCenterActivity)context).bankcard_status.setText("审核中");
								}else if(bankcard.equals("2")){
									((SafetyCenterActivity)context).bankcard_status.setText("未通过审核");
								}
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}else if(requestCode==FEILD_REQUSET){
						try{
							if(!data.getStringExtra("businessCard").equals("")){
									String businessCard = data.getStringExtra("businessCard");
									if(businessCard.equals("0")){
										((SafetyCenterActivity)context).bankcard_mingpianzhuantai.setText("审核中");
									}else if(businessCard.equals("1")){
										((SafetyCenterActivity)context).bankcard_mingpianzhuantai.setText("通过审核");
									}else if(businessCard.equals("2")){
										((SafetyCenterActivity)context).bankcard_mingpianzhuantai.setText("未通过审核");
									}
									
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
		}
		private boolean commonJSON(JSONObject response){
			boolean status=false;
			try {
				JSONObject obj= response.getJSONObject("result");
				boolean flag = response.getBoolean("success");
				String errCode = obj.getString("errorCode");
				if(flag && errCode.equals("success")){
					status=true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return status;
			
		}

		public void intview() {
			security_back = (View) findViewById(R.id.security_back);
			layout_shenfenyanzheng_center = findViewById(R.id.layout_shenfenyanzheng_center);
			layout_youxiangrenzheng_center = findViewById(R.id.layout_youxiangrenzheng_center);
			layout_yinhangkarenzheng_us = findViewById(R.id.layout_yinhangkarenzheng_us);
			layout_mingpian = findViewById(R.id.layout_mingpian);
			idcard_status = (TextView) findViewById(R.id.idcard_status);
			email_status = (TextView) findViewById(R.id.email_status);
			bankcard_status = (TextView) findViewById(R.id.bankcard_status);
			bankcard_mingpianzhuantai = (TextView) findViewById(R.id.bankcard_mingpianzhuantai);
			if(userBean.getUserRole().equals("2")){
				layout_mingpian.setVisibility(View.VISIBLE);
			}			
			security_back.setOnClickListener(this);
			layout_mingpian.setOnClickListener(this);
			layout_shenfenyanzheng_center.setOnClickListener(this);
			layout_youxiangrenzheng_center.setOnClickListener(this);
			layout_yinhangkarenzheng_us.setOnClickListener(this);
			// 认证状态(0审核中，1通过审核，2未通过审核)
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()){
			case R.id.security_back:	
		        finish(); 
		        break;
			case R.id.layout_mingpian:
				startActivityForResult(safetycenterService.mingpian(), FEILD_REQUSET);
		         break;
			case R.id.layout_shenfenyanzheng_center:
				startActivityForResult(safetycenterService.IdentityCard(), IDCARD_REQUSET);
			     break;
			case  R.id.layout_yinhangkarenzheng_us:
				startActivityForResult(safetycenterService.Bank_Card(), BANK_REQUSET);
				 break; 
			case R.id.layout_youxiangrenzheng_center:
				startActivityForResult(safetycenterService.Email_authentication(), EMAIL_REQUSET);
				 break;
			}
		}
	
	
	}
