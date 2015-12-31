package cn.com.ubankers.www.user.view;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.MembersBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.widget.CircleImg;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MembersAdapter extends BaseAdapter{
	private MembersBean memberBean;
	private List<MembersBean> memberList;
	private int count=0;
	private Context context;
	private UserBean userBean;
   
	
	public MembersAdapter(Context context ,List<MembersBean> memberList) {
		super();
		this.memberList = memberList;
		this.context = context;
		if(MyApplication.app.getUser()!=null){
			userBean=MyApplication.app.getUser();
		}
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
			convertView = LayoutInflater.from(context).inflate(R.layout.sub_member_detail, null);
			viewHolder.memberImge =(CircleImg) convertView.findViewById(R.id.memberImageView);
			viewHolder.maintreasureImage =(ImageView) convertView.findViewById(R.id.maintreasureImage);
			viewHolder.memberName =(TextView) convertView.findViewById(R.id.memberNameView);
			viewHolder.memberMobile =(TextView) convertView.findViewById(R.id.memberMobileView);
			viewHolder.memberDate =(TextView) convertView.findViewById(R.id.memberDateView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		MyApplication.loadImage(HttpConfig.HTTP_IMAGE_QUERY_URL+memberBean.getUserface(), viewHolder.memberImge, null);
		if(memberBean.getLeaderFlag()==1){
//			if(memberBean.getUserId().equals(userBean.getUserId())){
//			if(position==0){
			viewHolder.maintreasureImage.setVisibility(View.VISIBLE);
			viewHolder.memberName.setTextColor(Color.rgb(0, 102, 204));
			viewHolder.memberMobile.setTextColor(Color.rgb(0, 102, 204));
			viewHolder.memberDate.setTextColor(Color.rgb(0, 102, 204));
		}
		viewHolder.memberName.setText(memberBean.getNickName());
		viewHolder.memberMobile.setText(memberBean.getMobile());
		viewHolder.memberDate.setText(getStringDate(memberBean.getJoinDate())+"");
		return convertView;
	}
     class ViewHolder{
    	 CircleImg memberImge;
    	 TextView  memberName;
    	 TextView  memberMobile;
    	 TextView  memberDate;
    	 ImageView maintreasureImage;
	  
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
