package cn.com.ubankers.www.user.controller.fragment;

import java.util.ArrayList;


import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.model.CustomerBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.service.UnRegisteredUsersService;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class UnregisteredUsersFragment extends Fragment{
	private ListView rongListView;
	private Activity activity;
	private UserBean userBean;
	private ArrayList<CustomerBean> list;
	
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
		// TODO Auto-generated method stub
		
		View view = LayoutInflater.from(activity).inflate(R.layout.unregistered_users_fragment, container, false);
		rongListView=(ListView) view.findViewById(R.id.unregisted_users_fragment_listView);
		UnRegisteredUsersService urs = new UnRegisteredUsersService(userBean.getUserId(), activity, list, rongListView);
		urs.weiinitData();
		return view;	
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
