package cn.com.ubankers.www.user.view;

import java.util.ArrayList;
import java.util.List;


import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.controller.activity.ReserverDetailsActivity;
import cn.com.ubankers.www.user.model.ReserverDetailsBean;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderDetailsAdapter extends BaseAdapter {
	private Activity activity;
	private List<ReserverDetailsBean> list;
	private OnClickListener listener,unlistener;
	private OnClickListener idListener,unIdListener;
	private int count,type;

	public OrderDetailsAdapter(Activity activity,
			ArrayList<ReserverDetailsBean> list, OnClickListener listener,OnClickListener unlistener,OnClickListener idListener,OnClickListener unIdListener,int type) {
		this.activity = activity;
		this.list = list;
		this.listener = listener;
		this.idListener = idListener;
		this.type = type;
		this.unlistener = unlistener;
		this.unIdListener = unIdListener;
	}

	@Override
	public int getCount() {
		if(list!=null){
			count = list.size();	
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ReserverDetailsBean orderDetailsBean = list.get(position);
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(activity).inflate(
					R.layout.order_details_list, null);
			viewHolder.tv_details_money = (TextView) view
					.findViewById(R.id.details_money);
			if(type==1){
				viewHolder.tv_idcard_certification = (TextView) view
						.findViewById(R.id.regist_idcard_certification);
				viewHolder.tv_large_certification = (TextView) view
						.findViewById(R.id.regist_large_certification);
				viewHolder.tv_idcard_certification.setVisibility(View.VISIBLE);
				viewHolder.tv_large_certification.setVisibility(View.VISIBLE);
			}else if(type==2){
				viewHolder.untv_idcard_certification = (TextView) view
						.findViewById(R.id.unregist_idcard_certification);
				viewHolder.untv_large_certification = (TextView) view
						.findViewById(R.id.unregist_large_certification);
				viewHolder.untv_idcard_certification.setVisibility(View.VISIBLE);
				viewHolder.untv_large_certification.setVisibility(View.VISIBLE);
			}
			
//			viewHolder.tv_details_productName = (TextView) view
//					.findViewById(R.id.details_productName);
			viewHolder.tv_details_reserveMobile = (TextView) view
					.findViewById(R.id.details_reserveMobile);
			viewHolder.tv_details_reserveName = (TextView) view
					.findViewById(R.id.details_reserveName);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tv_details_money.setText("￥"
				+ orderDetailsBean.getReserveQuota());
//		viewHolder.tv_details_productName.setText(orderDetailsBean
//				.getProductName());
		viewHolder.tv_details_reserveMobile.setText("手机:"+ orderDetailsBean.getReserveMobile());
		viewHolder.tv_details_reserveName.setText(orderDetailsBean.getReserveName());
		if(type==1){
				viewHolder.tv_large_certification.setOnClickListener(listener);
				viewHolder.tv_idcard_certification.setOnClickListener(idListener);
				viewHolder.tv_large_certification.setTag(orderDetailsBean);
				viewHolder.tv_idcard_certification.setTag(orderDetailsBean);
		}else if(type==2){
				viewHolder.untv_idcard_certification.setOnClickListener(unIdListener);
				viewHolder.untv_large_certification.setOnClickListener(unlistener);
				viewHolder.untv_large_certification.setTag(orderDetailsBean);
				viewHolder.untv_idcard_certification.setTag(orderDetailsBean);
		}
		if(orderDetailsBean!=null&!orderDetailsBean.getReserveVoucherId().equals("")){
			if(type==1){
				viewHolder.tv_large_certification.setText("查看打款凭证");
			}else if(type==2){
				viewHolder.untv_large_certification.setText("查看打款凭证");
			}
		}else{
			if(type==1){
				viewHolder.tv_large_certification.setText("上传打款凭证");
			}else if(type==2){
				viewHolder.untv_large_certification.setText("上传打款凭证");
			}
		}
		if(!orderDetailsBean.getIdcardBackFileId().equals("0")||!orderDetailsBean.getIdcardFrontFileId().equals("0")){
			if(type==1){
				viewHolder.tv_idcard_certification.setText("查看身份证");
			}else if(type==2){
				viewHolder.untv_idcard_certification.setText("查看身份证");
			}
		}else{
			if(type==1){
				viewHolder.tv_idcard_certification.setText("上传身份证");
			}else if(type==2){
				viewHolder.untv_idcard_certification.setText("上传身份证");
			}
			
		}
		return view;
	}

	public class ViewHolder {
		TextView tv_details_money, tv_idcard_certification, tv_details_reserveMobile,
				tv_details_reserveName, tv_large_certification,untv_idcard_certification,untv_large_certification;
	}
}
