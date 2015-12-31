package cn.com.ubankers.www.user.view;

import java.util.List;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.model.WorkRecordBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WorkRecordAdapter extends BaseAdapter{
	
	private WorkRecordBean recordBean;
	private List<WorkRecordBean> recordList;
	private int count=0;
	private Context context;
   
	
	public WorkRecordAdapter(Context context ,List<WorkRecordBean> recordList) {
		super();
		this.recordList = recordList;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(recordList!=null){
			count =recordList.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return recordList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder =null;
		recordBean = (WorkRecordBean)recordList.get(position);
		if(convertView==null){
			viewHolder= new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.workrecord_list, null);
			viewHolder.productName =(TextView) convertView.findViewById(R.id.productName);
			viewHolder.saleMoney =(TextView) convertView.findViewById(R.id.saleMoney);
			viewHolder.getMoney =(TextView) convertView.findViewById(R.id.getMoney);
			viewHolder.createTime =(TextView) convertView.findViewById(R.id.createTime);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}
	
	class ViewHolder{
		TextView productName,saleMoney,getMoney,createTime;
	}

}
