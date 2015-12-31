package cn.com.ubankers.www.user.controller.activity;


import java.util.ArrayList;








import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.product.view.ProductFragmentAdapter;
import cn.com.ubankers.www.user.controller.fragment.BuyProductFragment;
import cn.com.ubankers.www.user.controller.fragment.InvestorOrderFragment;
import cn.com.ubankers.www.user.model.UserBean;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 
 * @author 廖准华
 * 投资者预约管理
 */
public class InvestorOrderActivity extends FragmentActivity implements OnClickListener{
		private UserBean userBean;
		public final static int investment_1 = 0;
		public final static int investment_2 = 1;
		private ViewPager viewPager;
		private TextView buyed_product,buy_product;
		private View investor_view1,investor_view2;
		private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.investment_record);
			MyApplication.app.addActivity(this);
			if(MyApplication.app.getUser()!=null){
				userBean=MyApplication.app.getUser();
			}
			initView();
			setChangelView();
		}
		private void initView() {
			buy_product = (TextView) findViewById(R.id.buy_product);
			buyed_product = (TextView) findViewById(R.id.buyed_product);
			viewPager=(ViewPager) findViewById(R.id.investment_fragments_viewPager);
			buy_product.setOnClickListener(this);
			buyed_product.setOnClickListener(this);
			investor_view1=findViewById(R.id.investor_view1);
			investor_view2=findViewById(R.id.investor_view2);
			xiugai_back = (LinearLayout) findViewById(R.id.xiugai_back);
			xiugai_back.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
			buy_product.setTextColor(Color.rgb(64, 64, 66));
			investor_view1.setVisibility(View.VISIBLE);
		}
		/**
		 * 初始化Fragment
		 * */
		private void initFragment() {
			Bundle bundle = new Bundle();
			InvestorOrderFragment investorOrderFragment = new InvestorOrderFragment();
			bundle.putSerializable("userBean",userBean);
			investorOrderFragment.setArguments(bundle);
			BuyProductFragment buyProductFragment = new BuyProductFragment();
			buyProductFragment.setArguments(bundle);
			fragments.add(investorOrderFragment);
			fragments.add(buyProductFragment);
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
					buy_product.setTextColor(Color.rgb(64, 64, 66));
					buyed_product.setTextColor(Color.rgb(200, 199, 204));
					investor_view1.setVisibility(View.VISIBLE);
					investor_view2.setVisibility(View.GONE);
					viewPager.setCurrentItem(arg0);
					break;
				case investment_2:
					buyed_product.setTextColor(Color.rgb(64, 64, 66));
					buy_product.setTextColor(Color.rgb(200, 199, 204));
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
			case R.id.buy_product:
				buy_product.setTextColor(Color.rgb(64, 64, 66));
				buyed_product.setTextColor(Color.rgb(200, 199, 204));
				investor_view1.setVisibility(View.VISIBLE);
				investor_view2.setVisibility(View.GONE);
				viewPager.setCurrentItem(0);
				break;
			case R.id.buyed_product:
				buyed_product.setTextColor(Color.rgb(64, 64, 66));
				buy_product.setTextColor(Color.rgb(200, 199, 204));
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
