package cn.com.ubankers.www.user.controller.fragment;

import java.util.ArrayList;






import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.controller.activity.ReserverDetailsActivity;
import cn.com.ubankers.www.user.model.InvestorOrderBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.service.CfmpOrderService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class CfmpOrderFragment extends Fragment{
	private static Activity activity;
	private  UserBean userBean;
	private  ListView rongListView;
	private  ArrayList<InvestorOrderBean> list;
	@SuppressWarnings("static-access")
	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
		super.onAttach(activity);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		userBean = (UserBean) bundle.getSerializable("userBean");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(activity).inflate(R.layout.investor_order_list, container, false);
		rongListView=(ListView) view.findViewById(R.id.lv_investor);
		list = new ArrayList<InvestorOrderBean>();
		CfmpOrderService cos = new CfmpOrderService(activity, userBean.getUserId(), rongListView, list);
		cos.initData();
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		rongListView.setOnItemClickListener(new MyOnItemClickList());
	}
	private class MyOnItemClickList implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			InvestorOrderBean investorOrderBean2 = list.get(arg2);
			Intent intent = new Intent(activity,ReserverDetailsActivity.class);
			intent.putExtra("productId",investorOrderBean2.getProductId());
			intent.putExtra("productName",investorOrderBean2.getProductName());
			startActivity(intent);
		}
	}
}
