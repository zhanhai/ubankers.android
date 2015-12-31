package cn.com.ubankers.www.user.view;

import java.util.ArrayList;
import java.util.List;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.model.BuyRecordBean;
import cn.com.ubankers.www.user.view.BuyRecordAdapter.ViewHolder;
import cn.com.ubankers.www.utils.Util;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InvestorBuyAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<BuyRecordBean> list;
	private int count;
	private Util util;

	public InvestorBuyAdapter(Context context,List<BuyRecordBean> purchaselist){		
		this.context=context;
		this.list=(ArrayList<BuyRecordBean>) purchaselist;
		util=new Util();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		BuyRecordBean buyRecordBean = list.get(arg0);		
		if(convertView==null){
			viewHolder= new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.investor_listview, null);
			viewHolder.investor_name=(TextView)convertView.findViewById(R.id.investor_name);
			viewHolder.investor_state=(TextView) convertView.findViewById(R.id.investor_state);
			viewHolder.investor_stexpiryDateForInterest=(TextView) convertView.findViewById(R.id.investor_stexpiryDateForInterest);
			viewHolder.investor_stpurchaseAmount=(TextView) convertView.findViewById(R.id.investor_stpurchaseAmount);
			viewHolder.investor_purchaseAmount=(TextView) convertView.findViewById(R.id.investor_purchaseAmount);
			viewHolder.investor_valueDate=(TextView) convertView.findViewById(R.id.investor_valueDate);			
			convertView.setTag(viewHolder);
		}else{			
			viewHolder = (ViewHolder) convertView.getTag();
		}			
//		0-等待支付，1-完成支付，2-交易已关闭，3-还息兑付中，4-已兑付。
		String State="";
		if(buyRecordBean.getState()==0){
			State="等待支付";
			viewHolder.investor_state.setText(State);
			viewHolder.investor_state.setTextColor(Color.rgb(255,0,0));
		}
//		else if(buyRecordBean.getState()==1){
//			State="完成支付";
//			viewHolder.investor_state.setText(State);
//		}
		if(buyRecordBean.getState()==2){
			State="交易已关闭";
			viewHolder.investor_state.setTextColor(Color.rgb(153,153,153));
			viewHolder.investor_state.setText(State);
		}if(buyRecordBean.getState()==3){
			State="还息兑付中";
			viewHolder.investor_state.setText(State);
			viewHolder.investor_state.setTextColor(Color.rgb(0,102,204));
		}if(buyRecordBean.getState()==0){
			State="已兑付";
			viewHolder.investor_state.setTextColor(Color.rgb(51,51,51));
			viewHolder.investor_state.setText(State);
		}
		
		viewHolder.investor_name.setText(buyRecordBean.getProductName());
		viewHolder.investor_stpurchaseAmount.setText("认购日"+util.getStringTime(buyRecordBean.getPurchaseDate())+"");
        viewHolder.investor_state.setText(State);
		viewHolder.investor_stexpiryDateForInterest.setText("到期日"+util.getStringTime(buyRecordBean.getExpiryDateForInterest())+"");
		viewHolder.investor_purchaseAmount.setText("¥"+buyRecordBean.getPurchaseAmount()+"");
		viewHolder.investor_valueDate.setText(util.getStringTime(buyRecordBean.getValueDate())+"");	
		// TODO Auto-generated method stub
		return convertView;
	}

	public class ViewHolder{
		TextView investor_stpurchaseAmount;
		TextView investor_name;
		TextView investor_state;
		TextView investor_stexpiryDateForInterest;
		TextView cfmp_productName;	
		TextView investor_purchaseAmount;	
		TextView investor_valueDate;	
	}

}
