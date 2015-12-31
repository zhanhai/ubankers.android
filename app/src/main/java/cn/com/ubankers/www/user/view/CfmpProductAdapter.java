package cn.com.ubankers.www.user.view;


import java.util.List;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.model.RecommendPtsBean;
import cn.com.ubankers.www.utils.Tools;
import cn.com.ubankers.www.utils.ViewHolder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CfmpProductAdapter extends BaseAdapter{
	private Context context;
	private List<RecommendPtsBean> list;
	private int count;

	public CfmpProductAdapter(Context context,List<RecommendPtsBean> list){
		this.context = context;
		this.list = list;
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
		ViewHolder viewHolder=null;
		RecommendPtsBean recommendPtsBean = list.get(position);
		if(view==null){
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.recomment_product, null);
			viewHolder.tv_productName=(TextView) view.findViewById(R.id.productName);
			viewHolder.tv_productRate=(TextView) view.findViewById(R.id.productRate);
			viewHolder.tv_addTime=(TextView) view.findViewById(R.id.addTime);
			viewHolder.tv_minSureBuyPrice=(TextView) view.findViewById(R.id.minSureBuyPrice);
			view.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) view.getTag();
		}
		if(recommendPtsBean!=null){
			if(recommendPtsBean.getMinSureBuyPrice()!=null){
				viewHolder.tv_minSureBuyPrice.setText(recommendPtsBean.getMinSureBuyPrice()+"元");
			}
			if(recommendPtsBean.getProductName()!=null){
				viewHolder.tv_productName.setText(recommendPtsBean.getProductName());
			}
			if(recommendPtsBean.getProductRate()!=null){
				if(recommendPtsBean.getProductRate().equals("浮动")){
					viewHolder.tv_productRate.setText(recommendPtsBean.getProductRate());
				}else{
					viewHolder.tv_productRate.setText(recommendPtsBean.getProductRate()+"%");
				}
			}
			if(recommendPtsBean.getAddTime()!=null){
				String addTime = Tools.getDateUtil1(recommendPtsBean.getAddTime());
				viewHolder.tv_addTime.setText(addTime);
			}
		}
		return view;
	}
	public class ViewHolder{
		TextView tv_productName;
		TextView tv_productRate;
		TextView tv_minSureBuyPrice;
		TextView tv_addTime;	
	}
    
}
