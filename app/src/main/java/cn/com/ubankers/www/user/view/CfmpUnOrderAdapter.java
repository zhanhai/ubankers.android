package cn.com.ubankers.www.user.view;



import java.util.ArrayList;


import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.model.CfmpOrderBean;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CfmpUnOrderAdapter extends BaseAdapter {
	private ArrayList<CfmpOrderBean> list;
	private Activity activity;
	private OnClickListener onClickListener;
	private int count;
	public CfmpUnOrderAdapter(Activity activity,ArrayList<CfmpOrderBean> list,OnClickListener onClickListener){
		this.activity = activity;
      this.list = list;
		this.onClickListener = onClickListener;
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
		        CfmpOrderBean cfmpOrderBean = list.get(position);
		        ViewHolder viewHolder =null;
		        if(view==null){
		        	viewHolder = new ViewHolder();
		        	view = LayoutInflater.from(activity).inflate(R.layout.cfmp_orders_list, null);
		        	viewHolder.reserveName=(TextView) view.findViewById(R.id.reserveName);
		        	viewHolder.reserveMobile=(TextView) view.findViewById(R.id.reserveMobile);
		        	viewHolder.order_productName=(TextView) view.findViewById(R.id.order_productName);
		        	viewHolder.reserveQuota=(TextView) view.findViewById(R.id.reserveQuota);
		        	viewHolder.order_quren=(TextView) view.findViewById(R.id.order_quren);
		        	viewHolder.order_quren.setOnClickListener(onClickListener);
		        	view.setTag(viewHolder);
		        	//对监听对象保存
		        	viewHolder.order_quren.setTag(cfmpOrderBean);
		        }else{
		        	viewHolder = (ViewHolder) view.getTag();
					 //重新获得监听对象
		        }	
		        viewHolder.reserveName.setText(cfmpOrderBean.getReserveName());
		        viewHolder.reserveMobile.setText("手机:"+cfmpOrderBean.getReserveMobile());
		        viewHolder.order_productName.setText(cfmpOrderBean.getProductName());
		        viewHolder.reserveQuota.setText(cfmpOrderBean.getReserveQuota()+"元");
		        return view;
	}
	public class ViewHolder{
		TextView reserveName,reserveMobile,order_productName,reserveQuota,order_quren;
	} 
	
	
}
