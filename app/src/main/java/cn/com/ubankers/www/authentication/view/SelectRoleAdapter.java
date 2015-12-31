package cn.com.ubankers.www.authentication.view;

import java.util.List;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.model.RoleBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectRoleAdapter extends BaseAdapter {
	Context context;
	List<RoleBean> list;
	private int count;
	public SelectRoleAdapter(Context context, List<RoleBean> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list != null) {
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
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		RoleBean roleBean = list.get(arg0);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.role_item, parent, false);
			viewHolder.roleLayout =(LinearLayout) convertView.findViewById(R.id.roleLayout); 
			viewHolder.capital = (ImageView) convertView
					.findViewById(R.id.capital);
			viewHolder.cap = (TextView) convertView.findViewById(R.id.cap);
			viewHolder.capText = (TextView) convertView
					.findViewById(R.id.capText);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (roleBean.getType() == 0) {
			viewHolder.capital.setImageResource(R.drawable.capital);
			viewHolder.cap.setText("资本师");
			viewHolder.capText.setText("金融理财产品提供方的管理人、是资本运作、投资管理的行业专家。");

		} else if (roleBean.getType() == 1) {
			LinearLayout.LayoutParams params  =new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 70, 0, 100);
			viewHolder.roleLayout.setLayoutParams(params);
			viewHolder.capital.setImageResource(R.drawable.wealth);
			viewHolder.cap.setText("财富师");
			viewHolder.capText.setText("为客户提供个性化资产配置方案的专业理财规划人员。");

		} else if (roleBean.getType() == 2) {
			LinearLayout.LayoutParams params  =new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 70, 0, 120);
			viewHolder.roleLayout.setLayoutParams(params);
			viewHolder.capital.setImageResource(R.drawable.investment);
			viewHolder.cap.setText("投资者");
			viewHolder.capText.setText("需要财富师、资本师提供服务，有财富管理需求的个人或机构。");
		}
		return convertView;
	}

	class ViewHolder {
		LinearLayout roleLayout;
		ImageView capital;
		TextView cap, capText;
	}
}