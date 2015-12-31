package cn.com.ubankers.www.user.view;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.impl.conn.LoggingSessionInputBuffer;

import com.handmark.pulltorefresh.library.internal.Utils;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.model.BindCfmpBean;
import cn.com.ubankers.www.user.model.BuyRecordBean;
import cn.com.ubankers.www.user.model.RecommendPtsBean;
import cn.com.ubankers.www.user.view.CfmpProductAdapter.ViewHolder;
import cn.com.ubankers.www.utils.Tools;
import cn.com.ubankers.www.utils.Util;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class BuyRecordAdapter extends BaseAdapter {
	
	
	private Context context;
	private ArrayList<BuyRecordBean> list;
	private int count;

	public BuyRecordAdapter(Context context,ArrayList<BuyRecordBean> list){		
		this.context = context;
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
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder viewHolder=null;
		BuyRecordBean buyRecordBean = list.get(position);			
		if(view==null){
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.buyrecord_list, null);
			viewHolder.cfmpName=(TextView) view.findViewById(R.id.cfmpName);
			viewHolder.cfmp_purchaseDate=(TextView) view.findViewById(R.id.cfmp_purchaseDate);
			viewHolder.cfmp_expiryDateForInterest=(TextView) view.findViewById(R.id.cfmp_expiryDateForInterest);
			viewHolder.cfmp_productName=(TextView) view.findViewById(R.id.cfmp_productName);
			viewHolder.cfmp_purchaseAmount=(TextView) view.findViewById(R.id.cfmp_purchaseAmount);
			viewHolder.cfmp_valueDate=(TextView) view.findViewById(R.id.cfmp_valueDate);			
			view.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) view.getTag();
			}	
		
		if(buyRecordBean.getCfmpName()!=null){
			viewHolder.cfmpName.setText(buyRecordBean.getCfmpName());			
		}
		viewHolder.cfmp_purchaseDate.setText("认购日"+ new Util().getStringTime(buyRecordBean.getPurchaseDate()));
		viewHolder.cfmp_expiryDateForInterest.setText("到期日"+new Util().getStringTime(buyRecordBean.getExpiryDateForInterest())+"");
		if(buyRecordBean.getProductName()!=null){
			viewHolder.cfmp_productName.setText(buyRecordBean.getProductName()+"");
		}
		viewHolder.cfmp_purchaseAmount.setText("¥"+buyRecordBean.getPurchaseAmount()+"");
		viewHolder.cfmp_valueDate.setText(new Util().getStringTime(buyRecordBean.getValueDate())+"");
		// TODO Auto-generated method stub
		return view;
	}
	
	public class ViewHolder{
		TextView cfmpName;
		TextView cfmp_purchaseDate;
		TextView cfmp_expiryDateForInterest;
		TextView cfmp_productName;	
		TextView cfmp_purchaseAmount;	
		TextView cfmp_valueDate;	
	}

}
