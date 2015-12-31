package cn.com.ubankers.www.user.view;

import java.util.List;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.model.CustomerBean;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomerAdapter extends BaseAdapter {

	
	private Activity activity;
	private List<CustomerBean> client;
	private int count;
	private TextView mycliuent_kehuid;
	private TextView mycliuent_dianhua;
	private TextView mycliuent_daoqiri;
	private int i;

	public CustomerAdapter(Activity activity, List<CustomerBean> client,int i){
		this.i=i;
		this.activity = activity;
		this.client = client;
		/*initPopWindow();*/
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(client!=null){
			count =client.size();	
			}
			return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (client != null && client.size() != 0) {
			return client.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CustomerBean clientben=client.get(position);
		ViewHolder viewHolder =null;
		
		if(convertView==null){
			viewHolder = new ViewHolder();
			convertView = View.inflate(activity, R.layout.mycliuent_list, null);
			viewHolder.imgview_go=(ImageView)convertView.findViewById(R.id.imgview_go);
			viewHolder.mycliuent_kehuid=(TextView)convertView.findViewById(R.id.mycliuent_kehuid);
			viewHolder.mycliuent_dianhua=(TextView)convertView.findViewById(R.id.mycliuent_dianhua);
			viewHolder.mycliuent_daoqiri=(TextView)convertView.findViewById(R.id.mycliuent_daoqiri);
			convertView.setTag(viewHolder);
		}else{
			viewHolder =(ViewHolder) convertView.getTag();
		}
		if(i==1){
			viewHolder.imgview_go.setVisibility(View.VISIBLE);
		}
		if(clientben!=null){
		if(clientben.getNickName()!=null){
			viewHolder.mycliuent_kehuid.setText(clientben.getNickName());
		}
		if(clientben.getAmount()!=null){
			viewHolder.mycliuent_daoqiri.setText(clientben.getAmount());
		}
		
		if(clientben.getMobile()!=null){
			viewHolder.mycliuent_dianhua.setText(clientben.getMobile());
		}
		}
		return convertView;
	}
	public class ViewHolder{
		TextView mycliuent_kehuid;
		TextView mycliuent_dianhua;
		TextView mycliuent_daoqiri;
		View imgview_go;
	}

}
