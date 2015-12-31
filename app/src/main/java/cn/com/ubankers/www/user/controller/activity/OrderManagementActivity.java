package cn.com.ubankers.www.user.controller.activity;




import java.util.ArrayList;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.product.view.ProductFragmentAdapter;
import cn.com.ubankers.www.user.controller.fragment.CfmpOrderFragment;
import cn.com.ubankers.www.user.controller.fragment.CfmpUnOrderFragment;
import cn.com.ubankers.www.user.model.UserBean;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderManagementActivity extends FragmentActivity implements OnClickListener{
	    private UserBean userBean;
	    private LinearLayout unconfirmed_order,confirmed_order;
	    private ViewPager viewPager;
	    private View investor_view1,investor_view2;
	    public final static int investment_1 = 0;
		public final static int investment_2 = 1;
		private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		private TextView tv_unconfirmed_order,tv_confirmed_order;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			MyApplication.app.addActivity(this);
			if(MyApplication.app.getUser()!=null){
				userBean=MyApplication.app.getUser();
			}
			setContentView(R.layout.cfmp_ordermanager);
			initView();
			setChangelView();
		}
		private void initView() {
			unconfirmed_order = (LinearLayout) findViewById(R.id.unconfirmed_order);
			confirmed_order = (LinearLayout) findViewById(R.id.confirmed_order);
			viewPager=(ViewPager) findViewById(R.id.investment_fragments_viewPager);
			unconfirmed_order.setOnClickListener(this);
			confirmed_order.setOnClickListener(this);
			investor_view1=findViewById(R.id.investor_view1);
			investor_view2=findViewById(R.id.investor_view2);
			tv_unconfirmed_order=(TextView) findViewById(R.id.tv_unconfirmed_order);
			tv_confirmed_order=(TextView) findViewById(R.id.tv_confirmed_order);
			//tv_confirmed_order
			xiugai_back = (LinearLayout) findViewById(R.id.xiugai_back);
			xiugai_back.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
			tv_unconfirmed_order.setTextColor(Color.rgb(64, 64, 66));
			investor_view1.setVisibility(View.VISIBLE);
			
		}
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		}

		/**
		 * 初始化Fragment
		 * */
		private void initFragment() {
			Bundle bundle = new Bundle();
			CfmpUnOrderFragment cfmpUnOrderFragment = new CfmpUnOrderFragment();
			bundle.putSerializable("userBean",userBean);
			cfmpUnOrderFragment.setArguments(bundle);
			CfmpOrderFragment cfmpOrderFragment = new CfmpOrderFragment();
			bundle.putSerializable("userBean",userBean);
			cfmpOrderFragment.setArguments(bundle);
			fragments.add(cfmpUnOrderFragment);
			fragments.add(cfmpOrderFragment);
			ProductFragmentAdapter mAdapetr = new ProductFragmentAdapter(
			getSupportFragmentManager(), fragments);
			viewPager.setOffscreenPageLimit(0);
			viewPager.setAdapter(mAdapetr);
			viewPager.setOnPageChangeListener(pageListener);
		}

		/**
		 * 当栏目项发生变化时候调用
		 * */
		private void setChangelView() {
			initFragment();
		}

		/**
		 * 换行切换任务
		 * 
		 * @author Anthony
		 * 
		 */
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
					tv_unconfirmed_order.setTextColor(Color.rgb(64, 64, 66));
					tv_confirmed_order.setTextColor(Color.rgb(200, 199, 204));
					investor_view1.setVisibility(View.VISIBLE);
					investor_view2.setVisibility(View.GONE);
					viewPager.setCurrentItem(arg0);
					break;
				case investment_2:
					tv_confirmed_order.setTextColor(Color.rgb(64, 64, 66));
					tv_unconfirmed_order.setTextColor(Color.rgb(200, 199, 204));
			        investor_view1.setVisibility(View.GONE);
			        investor_view2.setVisibility(View.VISIBLE);
			        viewPager.setCurrentItem(arg0);
					break;
				}
			}
		};
		private LinearLayout xiugai_back;
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.unconfirmed_order:
				tv_unconfirmed_order.setTextColor(Color.rgb(64, 64, 66));
				tv_confirmed_order.setTextColor(Color.rgb(200, 199, 204));
				investor_view1.setVisibility(View.VISIBLE);
				investor_view2.setVisibility(View.GONE);
				viewPager.setCurrentItem(0);
				break;
			case R.id.confirmed_order:
				tv_confirmed_order.setTextColor(Color.rgb(64, 64, 66));
				tv_unconfirmed_order.setTextColor(Color.rgb(200, 199, 204));
				investor_view1.setVisibility(View.GONE);
				investor_view2.setVisibility(View.VISIBLE);
				viewPager.setCurrentItem(1);
				break;
			}

		}
		@Override
		public void onBackPressed() {
			finish();
			super.onBackPressed();
		}
}
