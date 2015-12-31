package cn.com.ubankers.www.user.controller.activity;

import java.util.ArrayList;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.R.layout;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.product.view.ProductFragmentAdapter;
import cn.com.ubankers.www.user.controller.fragment.BuyRecordFragment;
import cn.com.ubankers.www.user.controller.fragment.NoBuyRecordFragment;
import cn.com.ubankers.www.user.controller.fragment.RegisteredUsersFragment;
import cn.com.ubankers.www.user.controller.fragment.UnregisteredUsersFragment;
import cn.com.ubankers.www.user.model.UserBean;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BuyRecordActivity extends FragmentActivity implements OnClickListener {	
	private UserBean userBean;
	private ViewPager viewPager;
	private View buy_record_back;
	private TextView nobuy_recordtext;
	private View nobuy_recordview;
	private View buy_recordview;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private TextView buy_record_text;
	private View buy_record;
	private View nobuy_record;
	public final static int investment_1 = 0;
	public final static int investment_2 = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.buy_record_activity);
		MyApplication.getInstance().addActivity(this);
		if(MyApplication.app.getUser()!=null){
			userBean=MyApplication.app.getUser();
		}
		initView();
		initFragment();
	}	
	
	private void initView() {
		viewPager=(ViewPager)findViewById(R.id.buy_record_viewPager);
		buy_record_back=(View)findViewById(R.id.buy_record_back);
		buy_record_text=(TextView)findViewById(R.id.buy_record_text);
		nobuy_recordtext=(TextView)findViewById(R.id.nobuy_recordtext);
		buy_recordview=(View)findViewById(R.id.buy_recordview);
		nobuy_recordview=(View)findViewById(R.id.nobuy_recordview);
		buy_record=(View)findViewById(R.id.buy_record);
		nobuy_record=(View)findViewById(R.id.nobuy_record);
		buy_record_text.setTextColor(Color.rgb(50, 50, 50));
		nobuy_recordtext.setTextColor(Color.rgb(153, 153, 151));
		buy_recordview.setVisibility(View.VISIBLE);
		nobuy_recordview.setVisibility(View.GONE);
		buy_record.setOnClickListener(this);
		nobuy_record.setOnClickListener(this);	
		buy_record_back.setOnClickListener(this);
	}
	
	
	private void initFragment() {
		Bundle bundle = new Bundle();
		BuyRecordFragment BuyRecordFragment = new BuyRecordFragment();
		NoBuyRecordFragment NoBuyRecordFragment = new NoBuyRecordFragment();
		bundle.putSerializable("userBean",userBean);
		BuyRecordFragment.setArguments(bundle);
		NoBuyRecordFragment.setArguments(bundle);
		fragments.add(BuyRecordFragment);
		fragments.add(NoBuyRecordFragment);		
		ProductFragmentAdapter mAdapetr = new ProductFragmentAdapter(getSupportFragmentManager(),fragments);
		viewPager.setOffscreenPageLimit(0);
		viewPager.setAdapter(mAdapetr);
		viewPager.setOnPageChangeListener(pageListener);
	}
	public OnPageChangeListener pageListener = new OnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int position) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}
		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			switch (arg0) {
			case investment_1:
				buy_record_text.setTextColor(Color.rgb(50, 50, 50));
				nobuy_recordtext.setTextColor(Color.rgb(153, 153, 151));				
				buy_recordview.setVisibility(View.VISIBLE);
				nobuy_recordview.setVisibility(View.GONE);
				viewPager.setCurrentItem(arg0);
				
				break;
			case investment_2:
				buy_record_text.setTextColor(Color.rgb(153, 153, 151));
				nobuy_recordtext.setTextColor(Color.rgb(50, 50, 50));
				buy_recordview.setVisibility(View.GONE);
				nobuy_recordview.setVisibility(View.VISIBLE);
				viewPager.setCurrentItem(arg0);
				break;
			}
		}
	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.buy_record:
			buy_record_text.setTextColor(Color.rgb(50, 50, 50));
			nobuy_recordtext.setTextColor(Color.rgb(153, 153, 151));
			buy_recordview.setVisibility(View.GONE);
			nobuy_recordview.setVisibility(View.VISIBLE);
			viewPager.setCurrentItem(0);
			break;
		case R.id.nobuy_record:	
			buy_record_text.setTextColor(Color.rgb(153, 153, 151));
			nobuy_recordtext.setTextColor(Color.rgb(50, 50, 50));
			buy_recordview.setVisibility(View.VISIBLE);
			nobuy_recordview.setVisibility(View.GONE);
			viewPager.setCurrentItem(1);
			break;
			
		case R.id.buy_record_back:
			finish();
		}
		
	}

	
	
}
