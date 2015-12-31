package cn.com.ubankers.www.user.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.CustomerBean;
import cn.com.ubankers.www.utils.BaseUtil;
//import cn.sharesdk.framework.statistics.a.c;


public class GroupinfoAdapter extends BaseAdapter{

	private List<CustomerBean> list;
	private LayoutInflater inflater;
	private HashMap<String, Integer> alphaIndexer;
	private HashMap<Integer, Boolean> currentStatus;
	private String[] sections;
	private int type;
	public HashMap<Integer, Boolean> getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(HashMap<Integer, Boolean> currentStatus) {
		this.currentStatus = currentStatus;
	}
	public void setCurrentPosition(int currentPosition) {
		boolean status = currentStatus.get(currentPosition);
		if (status) {
			currentStatus.put(currentPosition, false);
		} else {
			currentStatus.put(currentPosition, true);
		}
	}
	public GroupinfoAdapter(Context context,
			List<CustomerBean> list,int type) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.alphaIndexer = new HashMap<String, Integer>();	
		this.currentStatus = new HashMap<Integer, Boolean>();
		this.sections = new String[list.size()];
        this.type = type;
		for (int i = 0; i < list.size(); i++) {
			String name = getAlpha(BaseUtil.getPingYin(list.get(i).getNickName()));
			if (!alphaIndexer.containsKey(name)) {
				alphaIndexer.put(name, i);
			}
			currentStatus.put(i, false);
		}

		Set<String> sectionLetters = alphaIndexer.keySet();
		ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
		Collections.sort(sectionList);
		sections = new String[sectionList.size()];
		sectionList.toArray(sections);

	}

	public void refresh(List<CustomerBean> refreshList) {
		list = refreshList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void remove(int position) {
		list.remove(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.webmail_contact_item, null);
			holder = new ViewHolder();
			holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
			holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.iv = (ImageView) convertView.findViewById(R.id.investor_avatar);
            holder.number = (TextView) convertView.findViewById(R.id.number);
            convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CustomerBean clientBean = list.get(position);
		if(type==1){//非B类投资者
			if(!clientBean.getNickName().equals("")){
				 holder.name.setText(clientBean.getNickName()); 
			 }
		}else if(type==2){//B类投资者
			 holder.name.setText(clientBean.getRealName());
		}

		
		if (!clientBean.getUserFaceFileId().equals("")) {
			MyApplication.loadImage(HttpConfig.HTTP_IMAGE_QUERY_URL+clientBean.getUserFaceFileId(), holder.iv,null);
       }else {
    	   holder.iv.setBackgroundResource(R.drawable.personal_center);
	   }
		
		
		if(!clientBean.getNickName().equals("")){
			String currentStr = getAlpha(BaseUtil.getPingYin(clientBean.getNickName()));
			String previewStr = (position - 1) >= 0 ? getAlpha(BaseUtil.getPingYin(list.get(
					position - 1).getNickName())) : " ";

			if (!previewStr.equals(currentStr)) {
				holder.alpha.setVisibility(View.VISIBLE);
				holder.alpha.setText(currentStr);
			} else {
				holder.alpha.setVisibility(View.GONE);
			}
		}
		holder.number.setText(clientBean.getMobile());
		return convertView;
	}

	class ViewHolder {
        public TextView alpha,number;
		public TextView name;
		ImageView iv;
	}

	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);

		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}


}
