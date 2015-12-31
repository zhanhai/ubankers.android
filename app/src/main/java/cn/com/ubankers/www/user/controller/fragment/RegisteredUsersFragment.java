package cn.com.ubankers.www.user.controller.fragment;

import java.util.ArrayList;





import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.controller.activity.CustomerDetailActivity;
import cn.com.ubankers.www.user.model.CustomerBean;
import cn.com.ubankers.www.user.service.RegisteredUsersService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class RegisteredUsersFragment extends Fragment{
	private ListView rongListView;
	private Activity activity;
	private ArrayList<CustomerBean> list;
	
	public void onAttach(Activity activity) {
		this.activity = activity;
		super.onAttach(activity);
	}
//已注册
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		/*rongListView.setOnItemClickListener(new MyOnItemClickList());*/
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = LayoutInflater.from(activity).inflate(R.layout.registered_users_fragment_list, container, false);
		rongListView=(ListView) view.findViewById(R.id.registered_users_listView);
		RegisteredUsersService rus = new RegisteredUsersService(activity, list, rongListView);
		rus.initData();
		return view;	
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	/*private class MyOnItemClickList implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Intent intent = new Intent(activity,CustomerDetailActivity.class);
			startActivity(intent);
		}
	}*/
}
