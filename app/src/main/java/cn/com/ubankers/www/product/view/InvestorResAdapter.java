package cn.com.ubankers.www.product.view;

import java.util.List;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.model.CustomerBean;
import cn.com.ubankers.www.utils.ViewHolder;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class InvestorResAdapter extends BaseAdapter  {
	private Context context;
	private List<CustomerBean> list;
	private int count;

	public InvestorResAdapter(Context context,int titleId,List<CustomerBean> list){
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
		ViewHolder viewHolder;
		 CustomerBean clientBean = list.get(position);
			if(view==null){
				viewHolder = new ViewHolder();
				view= LayoutInflater.from(context).inflate(R.layout.cfmp_investor,null);
				viewHolder.tv_productRealName=(TextView) view.findViewById(R.id.productRealName);
				viewHolder.tv_mobile=(TextView) view.findViewById(R.id.mobile);
				view.setTag(viewHolder);
			}else{
				viewHolder =(ViewHolder) view.getTag();
			}
			viewHolder.tv_mobile.setText(clientBean.getMobile());
			viewHolder.tv_productRealName.setText(clientBean.getNickName());

		return view;
	}
  class ViewHolder{
	  TextView tv_productRealName,tv_mobile;
  }
}
