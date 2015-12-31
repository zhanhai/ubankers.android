package cn.com.ubankers.www.user.controller.fragment;


import java.util.ArrayList;


















import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.model.CfmpOrderBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.service.CfmpUnOrderService;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class CfmpUnOrderFragment extends Fragment{
	private  Activity activity;
	private  UserBean userBean;
	private  ListView rongListView;
	private  ArrayList<CfmpOrderBean> list = new ArrayList<CfmpOrderBean>();
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
		list = new ArrayList<CfmpOrderBean>();
		CfmpUnOrderService cus = new CfmpUnOrderService(activity,userBean.getUserId(), rongListView, list);
		cus.initData();
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
//		rongListView.setOnItemClickListener(new MyOnItemClickList() );
	}
}
