package cn.com.ubankers.www.user.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.user.model.MembersBean;
import cn.com.ubankers.www.widget.CircleImg;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CfmpMembersAdapter extends BaseAdapter {
	private MembersBean memberBean;
	private List<MembersBean> memberList;
	private int count=0;
	private Context context;
   
	
	public CfmpMembersAdapter(Context context ,List<MembersBean> memberList) {
		super();
		this.memberList = memberList;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(memberList!=null){
			count =memberList.size();
		}
		return count;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return memberList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder =null;
		memberBean = (MembersBean)memberList.get(position);
		if(convertView==null){
			viewHolder= new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.all_treasure_add_layout, null);
			viewHolder.memberName =(TextView) convertView.findViewById(R.id.treasure_nickName);
			viewHolder.memberMobile =(TextView) convertView.findViewById(R.id.treasure_mobile);
			viewHolder.memberDate =(TextView) convertView.findViewById(R.id.treasure_addTime);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.memberName.setText(memberBean.getNickName());
		viewHolder.memberMobile.setText(memberBean.getMobile());
		viewHolder.memberDate.setText(getStringDate(memberBean.getJoinDate())+"");
		return convertView;
	}
     class ViewHolder{
    	 TextView  memberName;
    	 TextView  memberMobile;
    	 TextView  memberDate;
    	
	  
     }
     /**
      * 
      * @param 要转换成的日期格式
      * @param  要转换的日期
      * @return
      */

     /**
	  * 将时间戳转换为可读日期 <br>
	  * 格式化日期为yyyy-MM-dd形式 
	  * @param time	时间戳
	  * @return	格式化日期
	  */
	 public static String getStringDate(Long time ){
		 String strs = "";
		 try {
			 if( time != null ){
				 Date date = new Date(time);
				 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				 strs = sdf.format(date);
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 return strs;
	 }
	
}
