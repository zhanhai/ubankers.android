package cn.com.ubankers.www.user.view;


import java.util.List;







import cn.com.ubankers.www.R;



import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.product.model.ProductDetail;
import cn.com.ubankers.www.user.model.InvestorOrderBean;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InvestorOrderAdapter extends BaseAdapter {
	private Activity activity;
	private List<InvestorOrderBean> list;
	private OnClickListener  onClickListener;
	private ProductDetail product;
	private int count;

	public InvestorOrderAdapter(Activity activity,List<InvestorOrderBean> list,OnClickListener  onClickListener){
		this.activity = activity;
		this.list = list;
		this.onClickListener = onClickListener;	
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
	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		        InvestorOrderBean investorOrderBean = list.get(position);
		        ViewHolder viewHolder =null;
		        if(view==null){
		        	viewHolder = new ViewHolder();
		        	view = LayoutInflater.from(activity).inflate(R.layout.list_investment_record, null);
		        	viewHolder.productName=(TextView) view.findViewById(R.id.text1);
		        	viewHolder.order_time=(TextView) view.findViewById(R.id.order_time);
		        	viewHolder.order_money=(TextView) view.findViewById(R.id.order_money);
		        	viewHolder.buttonOrder=(TextView) view.findViewById(R.id.buttonOrder);
		        	viewHolder.expected_annual_income = (TextView) view.findViewById(R.id.buttonOrder);
		        	if(investorOrderBean.getExamineState()==0){
		        		viewHolder.buttonOrder.setOnClickListener(onClickListener);	
		        	}
		        	viewHolder.buttonOrder.setTag(investorOrderBean);
		        	view.setTag(viewHolder);
		        	//对监听对象保存
		        }else{
		        	viewHolder = (ViewHolder) view.getTag();
		        }
		        if(MyApplication.app.getProduct()!=null){
					product = MyApplication.app.getProduct();
				}
		        viewHolder.productName.setText(investorOrderBean.getProductName());
		        viewHolder.order_time.setText("预约日："+investorOrderBean.getReserveTimeShow());
		        viewHolder.order_money.setText(investorOrderBean.getReserveQuota());
		        if(investorOrderBean.getProductId().equals(product.getProductId())){
		        	if(product.getCountProductRate().equals("-1")){
		        		 viewHolder.expected_annual_income.setText("浮动");
		        	}else{
		        	     viewHolder.expected_annual_income.setText(product.getCountProductRate()+"%");
		        	}
		        }		       
				if(investorOrderBean.getExamineState()==1){
					viewHolder.buttonOrder.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_gray));
					viewHolder.buttonOrder.setText("已确认");
				}else if(investorOrderBean.getExamineState()==0){
					viewHolder.buttonOrder.setText("取消预约");
					viewHolder.buttonOrder.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_edittext));
				}
		        return view;
	}
	public class ViewHolder{
		TextView productName,order_time,order_money,expected_annual_income,buttonOrder;
	} 


}
