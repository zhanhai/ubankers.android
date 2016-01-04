package cn.com.ubankers.www.user.controller.fragment;

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

import java.util.ArrayList;
import java.util.List;

import cn.com.ubankers.www.R;
import com.ubankers.app.product.detail.ProductDetailActivity;
import cn.com.ubankers.www.user.model.InvestorOrderBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.service.InvestorOrderService;


public class InvestorOrderFragment extends Fragment{
	private Activity activity;
	private UserBean userBean;
	private ListView rongListView;
	private List<InvestorOrderBean> list;
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
		InvestorOrderService investorOrderService = new InvestorOrderService(activity,userBean.getUserId(),rongListView,list);
		investorOrderService.initData();
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		rongListView.setOnItemClickListener(new MyOnItemClickList() );
	}
	private class MyOnItemClickList implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			    InvestorOrderBean  investorOrderBean = list.get(arg2);
			    Intent intent = new Intent(getActivity(),ProductDetailActivity.class);
			    intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID,investorOrderBean.getProductId());
			    intent.putExtra(ProductDetailActivity.EXTRA_RESERVER_NAME,investorOrderBean.getReserveName());
			    startActivity(intent);
		}
	}
	
}
