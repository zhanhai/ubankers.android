package cn.com.ubankers.www.user.view;

import java.util.ArrayList;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.BindCfmpBean;



import cn.com.ubankers.www.widget.CircleImg;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CfmpAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<BindCfmpBean> list;
	private int count;

	public CfmpAdapter(Context context,ArrayList<BindCfmpBean> list){
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
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder =null;
		BindCfmpBean bindCfmpBean = list.get(position);
		if(view==null){
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.mycfmp_layout_list, null);
			viewHolder.cfmp_avatar =(CircleImg) view.findViewById(R.id.recent_icon);
			viewHolder.tv_nickName =(TextView) view.findViewById(R.id.recent_nickname);
			view.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) view.getTag();
		}   
		  if(bindCfmpBean!=null&&bindCfmpBean.getUserFaceFileId()!=null){
			MyApplication.loadImage(HttpConfig.HTTP_IMAGE_QUERY_URL+bindCfmpBean.getUserFaceFileId(),viewHolder.cfmp_avatar,null);
		  }
		  if(bindCfmpBean!=null&&bindCfmpBean.getNickName()!=null){
			  viewHolder.tv_nickName.setText(bindCfmpBean.getNickName()); 
		  }
		return view;
	}
	public class ViewHolder {
		CircleImg cfmp_avatar;
		TextView tv_nickName;
		
	}

}
