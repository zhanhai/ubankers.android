package cn.com.ubankers.www.user.view;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.model.InvestorOrderBean;

public class CfmpOrderAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<InvestorOrderBean> list;
	private int count;

	public CfmpOrderAdapter(Activity activity,ArrayList<InvestorOrderBean> list){
		this.activity = activity;
		this.list = list;
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
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder=null;
		InvestorOrderBean investorOrderBean = list.get(position);
		if(view==null){
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(activity).inflate(R.layout.cfmp_investor_list, null);
			viewHolder.tv_productName =(TextView) view.findViewById(R.id.productName);
			viewHolder.tv_totalAmount =(TextView) view.findViewById(R.id.totalAmount);
			viewHolder.tv_totalProNum =(TextView) view.findViewById(R.id.totalProNum);
			view.setTag(viewHolder);
		}else{
			viewHolder =(ViewHolder) view.getTag();
		}
		if(investorOrderBean!=null&&investorOrderBean.getProductName()!=null){
			viewHolder.tv_productName.setText(investorOrderBean.getProductName().toString());	
		}
		if(investorOrderBean!=null&&investorOrderBean.getTotalAmount()!=null){
			viewHolder.tv_totalAmount.setText("￥"+investorOrderBean.getTotalAmount().toString());
		}
		if(investorOrderBean!=null&&investorOrderBean.getTotalProNum()!=null){
		viewHolder.tv_totalProNum.setText(investorOrderBean.getTotalProNum().toString());
		}
		return view;
	}
	public class ViewHolder{
		TextView tv_productName;
		TextView tv_totalAmount;
		TextView tv_totalProNum;
		
	}

}
