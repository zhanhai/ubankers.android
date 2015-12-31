package cn.com.ubankers.www.user.controller.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.model.InvestorOrderBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.service.InvestorOrderService;

public class BuyProductFragment extends Fragment{
	private UserBean userBean;
	private Activity activity;
	private ListView rongListView;
	private ArrayList<InvestorOrderBean> list;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
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
		investorOrderService.InvestmentBuying(userBean.getUserId());
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}	
}
