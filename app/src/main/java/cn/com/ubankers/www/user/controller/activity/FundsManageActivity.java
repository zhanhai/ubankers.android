package cn.com.ubankers.www.user.controller.activity;

import java.io.ObjectOutputStream.PutField;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.R.layout;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.user.controller.activity.CfmpManagerAcyivity.back;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.service.FundsManageService;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FundsManageActivity extends Activity implements OnClickListener {

	private View zjgl_yonhuzhuce;
	private View zhanghulishi;
	public TextView account_balance;
	private View Charge;
	private View Withdraw;
	private String totalAmount;
	private UserBean userBean;
	private AlertDialog dialog;
	private FundsManageService FundsManageService;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.managedfund_activity);
		if(MyApplication.app.getUser()!=null){
			userBean = MyApplication.app.getUser();
		}
		initview();
		FundsManageService=new FundsManageService(this);
		FundsManageService.initData();
	}
	
	private void  initview() {
		zhanghulishi=findViewById(R.id.zhanghulishi);
		zjgl_yonhuzhuce=(View)findViewById(R.id.zjgl_yonhuzhuce);
		account_balance=(TextView)findViewById(R.id.account_balance);
		Charge=(View)findViewById(R.id.Charge);
		Withdraw=(View)findViewById(R.id.Withdraw);
		Withdraw.setOnClickListener(this);
		Charge.setOnClickListener(this);
		zjgl_yonhuzhuce.setOnClickListener(this);	
		zhanghulishi.setOnClickListener(this);
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		FundsManageService.initData();
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		 switch (arg0.getId()){
		 case R.id.zjgl_yonhuzhuce: //返回
			 FundsManageActivity.this.finish();
			 break;
		 case R.id.zhanghulishi://历史记录
		 Intent intent=new Intent(FundsManageActivity.this,AccountActivity.class);
			startActivity(intent);
			 break;
			 
		 case R.id.Charge://充值
			 Toast.makeText(FundsManageActivity.this, "该功能正在开发中", Toast.LENGTH_SHORT).show();
//			 Intent ChargeActivity=new Intent(FundsManageActivity.this,ChargeActivity.class);
//				startActivity(ChargeActivity);
				 break;
				 
		 case R.id.Withdraw://提现
			 userBean = MyApplication.app.getUser();
			 if(userBean!=null){	 
				 if(userBean.getBankcard_status()!= null){
					 if (userBean.getBankcard_status().equals("1")){
						if(userBean.getIdcard_status()!=null) {							
							if(userBean.getBankcard_status().equals("1")){
								Intent WithdrawActivity=new Intent(FundsManageActivity.this,WithdrawActivity.class);
								startActivity(WithdrawActivity);
							}else{
								FundsManageDialog("Idcard");
							}							
						}else{
							 FundsManageDialog("Idcard");
						}						 
					 }else{
						 FundsManageDialog("Bankcard");
					 }
				 }else{
					 FundsManageDialog("Bankcard");
				 }
				 
			}		
		 }
	} 	
	public void FundsManageDialog(final String str) { 
		dialog=new AlertDialog.Builder(this).create();
		View view = LayoutInflater.from(this).inflate( R.layout.funds_mnage_dialog, null);
		TextView toastid = (TextView)view.findViewById(R.id.toastid);
		if(str.equals("Bankcard")){
			toastid.setText("您未绑定银行卡或银行卡正在审核中");
		}else if(str.equals("Idcard")){
			toastid.setText("您未绑定身份证或身份证正在审核中");
		}
		dialog.setView(view); 
		dialog.show();
		view.findViewById(R.id.OK).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(str.equals("Bankcard")){
					Intent  BankCardActivity=new Intent(FundsManageActivity.this,BankCardActivity.class);
					BankCardActivity.putExtra("userBean",userBean);
					startActivity(BankCardActivity);
				}else if(str.equals("Idcard")){
					Intent   BankCardActivity=new Intent(FundsManageActivity.this,SafetyCenterActivity.class);
				   BankCardActivity.putExtra("userBean", userBean);
				   startActivity(BankCardActivity);
				}
				dialog.dismiss();
			}
			
		});

           }
	
}
