package cn.com.ubankers.www.user.controller.fragment;

import java.util.ArrayList;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.user.controller.activity.ReserverDetailsActivity;
import cn.com.ubankers.www.user.model.ReserverDetailsBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.service.ReserverDetailsService;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class RegisteredFragment extends Fragment {
	private Activity activity;
	private ListView registeredfragment_listview;
	private ReserverDetailsService rds;
	private UserBean userBean;
	ArrayList<ReserverDetailsBean> list= new ArrayList<ReserverDetailsBean>();
	private String productId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		userBean = (UserBean) bundle.getSerializable("userBean");
		productId = bundle.getString("productId");
		if(MyApplication.app.getUser()!=null){
			userBean=MyApplication.app.getUser();
		}
	}
	
	public void onAttach(Activity activity) {
		this.activity = activity;
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(activity).inflate(R.layout.registeredfragment, container, false);
		registeredfragment_listview=(ListView) view.findViewById(R.id.registeredfragment_listview);
		rds = new ReserverDetailsService(userBean.getUserId(),productId,list,registeredfragment_listview,(ReserverDetailsActivity)activity);
		rds.initData(1);
		return view;
	}

}
