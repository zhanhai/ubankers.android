package cn.com.ubankers.www.user.view;


import java.util.ArrayList;
import java.util.List;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.BindCfmpBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BindCfmpAdapter extends BaseAdapter {
	private Context context;
	private List<BindCfmpBean> list;
	private OnClickListener onclickListener;
	private int count;


	public BindCfmpAdapter(Context context,ArrayList<BindCfmpBean> list,String userId,OnClickListener onclickListener){
		this.context = context;
		this.list = list;
		this.onclickListener = onclickListener;
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

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		       BindCfmpBean cfmpBoundBean = list.get(position);
		        ViewHolder viewHolder =null;
		        if(view==null){
		        	viewHolder = new ViewHolder();
		        	view = LayoutInflater.from(context).inflate(R.layout.mycfmp_layout_list, null);
		        	viewHolder.recent_icon=(ImageView) view.findViewById(R.id.recent_icon);
		        	viewHolder.productName=(TextView) view.findViewById(R.id.recent_nickname);
		        	viewHolder.bound_cfmp_list=(TextView) view.findViewById(R.id.bound_cfmp_list);
		        	viewHolder.approve=(TextView) view.findViewById(R.id.approve);
		        	viewHolder.check_cfmp=(ImageView) view.findViewById(R.id.check_cfmp);
		        	viewHolder.bound_cfmp_list.setOnClickListener(onclickListener);
		        	view.setTag(viewHolder);
		        	//对监听对象保存
		        	viewHolder.bound_cfmp_list.setTag(cfmpBoundBean);
		        }else{
		        	 viewHolder = (ViewHolder) view.getTag();
					 //重新获得监听对象
		        }
		        viewHolder.bound_cfmp_list.setText("绑定");
		        viewHolder.bound_cfmp_list.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_edittext));
		        viewHolder.productName.setText(cfmpBoundBean.getNickName());
		        MyApplication.loadImage(HttpConfig.HTTP_IMAGE_QUERY_URL+cfmpBoundBean.getUserFaceFileId(),viewHolder.recent_icon,null);
		        if(cfmpBoundBean.getProductRealNameStatus()=="1"){
		        	viewHolder.approve.setVisibility(View.VISIBLE);
		        	viewHolder.approve.setText("认证财富师");
		        	viewHolder.check_cfmp.setVisibility(View.VISIBLE);
		        }
		        return view;
	}
	public class ViewHolder{
		TextView productName,bound_cfmp_list,approve;
		ImageView recent_icon,check_cfmp;
	} 


}
