package cn.com.ubankers.www.user.view;

import java.util.List;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.model.AccountBean;
import cn.com.ubankers.www.user.view.BindCfmpAdapter.ViewHolder;
import cn.com.ubankers.www.utils.Util;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountAdapter extends BaseAdapter {
	
	private List<AccountBean> AccountBean;
	private int count;
	private Context context;

	public  AccountAdapter(List<AccountBean> AccountBean, Context context ){
		this.AccountBean=AccountBean;
		this.context=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(AccountBean!=null){
			count = AccountBean.size();	
		}
		return count;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return AccountBean.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		AccountBean accountBean=AccountBean.get(arg0);
		 ViewHolder viewHolder =null;
		 if(arg1==null){
			   viewHolder = new ViewHolder();
	        	arg1 = LayoutInflater.from(context).inflate(R.layout.account_activity, null);
	        	viewHolder.description=	(TextView) arg1.findViewById(R.id.description);//产品名称
	        	viewHolder.status=(TextView) arg1.findViewById(R.id.status);//交易装填
	        	viewHolder.amount=(TextView) arg1.findViewById(R.id.amount);//交易金额
	        	viewHolder.startTime=(TextView) arg1.findViewById(R.id.startTime);//日期
	        	viewHolder.type=(TextView) arg1.findViewById(R.id.type);//交易类型
	        	arg1.setTag(viewHolder);
		 }else{
			 viewHolder = (ViewHolder)arg1.getTag();
		 }
		 if(accountBean.getType().equals("COMMISSION_IN")){
			 viewHolder.description.setVisibility(View.VISIBLE);
			 viewHolder.description.setText(accountBean.getDescription()+"");//产品名
	     	}else{
	     		viewHolder.description.setText("");//产品名
	     	}
		if(accountBean.getStatus().equals("SUCCESS")){
			viewHolder.status.setText("成功");//交易装填
			viewHolder.status.setTextColor(Color.GRAY);
		}else if(accountBean.getStatus().equals("NOTSTART")){
			viewHolder.status.setText("处理中");//未启动
			viewHolder.status.setTextColor(Color.RED);
		}else if(accountBean.getStatus().equals("INPROGRESS")){
			viewHolder.status.setText("处理中");//处理中
			viewHolder.status.setTextColor(Color.RED);
		}else if(accountBean.getStatus().equals("FAIL")){
			viewHolder.status.setText("失败");//失败
			viewHolder.status.setTextColor(Color.GRAY);
		}else if(accountBean.getStatus().equals("HOLD")){
			viewHolder.status.setText("处理中");//挂起
			viewHolder.status.setTextColor(Color.RED);
		}else if(accountBean.getStatus().equals("NOTAUDIT")){
			viewHolder.status.setText("失败");//审计失败
			viewHolder.status.setTextColor(Color.GRAY);
		}
		
		if(accountBean.getType().equals("WITHDRAWAL")||accountBean.getType().equals("COMMISSION_OUT")||accountBean.getType().equals("REDPOCKET_OUT")){
			viewHolder.amount.setText("-"+Util.Intercept1(accountBean.getAmount()+"") );//交易金额
		}else if(accountBean.getType().equals("RECHARGE")||accountBean.getType().equals("COMMISSION_RECHARGE")||accountBean.getType().equals("COMMISSION_IN")||accountBean.getType().equals("MARKETING_RECHARGE")||accountBean.getType().equals("REDPOCKET_IN")){
			viewHolder.amount.setText("+"+Util.Intercept1(accountBean.getAmount()+""));//交易金额
		}
     	
     	viewHolder.startTime.setText(Util.getStringTimedat(accountBean.getStartTime())+"");//日期
     	
     	if(accountBean.getType().equals("WITHDRAWAL")){
     		viewHolder.type.setText("提现");//交易类型
     	}else if(accountBean.getType().equals("RECHARGE")){
     		viewHolder.type.setText("充值");//交易类型
     	}else if(accountBean.getType().equals("COMMISSION_RECHARGE")){
     		viewHolder.type.setText("佣金入账");//交易类型
     	}else if(accountBean.getType().equals("COMMISSION_IN")){
     		viewHolder.type.setText("佣金转入");//交易类型
     	}else if(accountBean.getType().equals("COMMISSION_OUT")){
     		viewHolder.type.setText("佣金转出");//交易类型
     	}else if(accountBean.getType().equals("MARKETING_RECHARGE")){
     		viewHolder.type.setText("营销活动账号入账");//交易类型
     	}else if(accountBean.getType().equals("REDPOCKET_IN")){
     		viewHolder.type.setText("红包转入");//交易类型
     	}else if(accountBean.getType().equals("REDPOCKET_OUT")){
     		viewHolder.type.setText("红包转出");//交易类型
     	}
     	return arg1;
	}
	
	public class ViewHolder{
		TextView description,status,amount,startTime,type;
	} 

}
