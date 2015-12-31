package cn.com.ubankers.www.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.controller.activity.SafetyCenterActivity;

public class MyDialog {
	    private Context context;
		public MyDialog(Context context){
	    	this.context = context;
	    }
		//预约失败的dialog
		public void errorDialog(){
			final AlertDialog dialog_success=new AlertDialog.Builder(context).create();
			final View view = View.inflate(context, R.layout.order_error_layout, null);
			view.findViewById(R.id.error_okay).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					dialog_success.dismiss();
				}
			});
			dialog_success.setView(view); 
			dialog_success.show();
		}
		//预约成功的dialog
		public void successDialog(int type){
			final AlertDialog dialog_success=new AlertDialog.Builder(context).create();
			final View view = View.inflate(context, R.layout.order_success_layout, null);
			view.findViewById(R.id.success_okay).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					dialog_success.dismiss();
				}
			});
			if(type==1){
				TextView tv_detail = (TextView) view.findViewById(R.id.detail);
				tv_detail.setText("请到个人中心-投资记录查看详情。");
			}
			dialog_success.setView(view); 
			dialog_success.show();
		}
		//绑定财富师弹出的dialog
		public void shoeBoundDialog(){
			final AlertDialog dialog_success=new AlertDialog.Builder(context).create();
			final View view = View.inflate(context, R.layout.bound_cfmp_layout, null);
			view.findViewById(R.id.success_bound).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					dialog_success.dismiss();
				}
			});
			dialog_success.setView(view); 
			dialog_success.show();
		}
		//给已注册的投资者预约产品的错误提示的dialog
		public void orderCfmpDialog(){
			final AlertDialog dialog_success=new AlertDialog.Builder(context).create();
			final View view = View.inflate(context, R.layout.order_error_cfmp_layout, null);
			view.findViewById(R.id.success_bound).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					dialog_success.dismiss();
				}
			});
			dialog_success.setView(view); 
			dialog_success.show();
		}
		//财富师名片审核的dialog
		public void businessCardDialog(){
			final AlertDialog dialog_success=new AlertDialog.Builder(context).create();
			final View view = View.inflate(context, R.layout.business_card_layout, null);
			view.findViewById(R.id.business_card_bound).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(context, SafetyCenterActivity.class);
					context.startActivity(intent);
				}
			});
			dialog_success.setView(view); 
			dialog_success.show();
		}
		
}
