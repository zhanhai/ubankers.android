package cn.com.ubankers.www.user.controller.fragment;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.model.UserBean;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NoBuyRecordFragment extends Fragment {
	
	private UserBean userBean;
	private Activity activity;
	
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
		View view = LayoutInflater.from(activity).inflate(R.layout.investor_order_list, container, false); 	
		return view;
	}

}
