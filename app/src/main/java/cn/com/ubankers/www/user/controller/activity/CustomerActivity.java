package cn.com.ubankers.www.user.controller.activity;
import java.util.ArrayList;



import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.product.view.ProductFragmentAdapter;
import cn.com.ubankers.www.user.controller.fragment.RegisteredUsersFragment;
import cn.com.ubankers.www.user.controller.fragment.UnregisteredUsersFragment;
import cn.com.ubankers.www.user.model.UserBean;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author 廖准华
 * 我的客户
 */

public class CustomerActivity extends  FragmentActivity implements OnClickListener{
	private UserBean userBean;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private EditText mycliuent_chaxun;
	private View zhucelayout;
	private View weizhucelayout;
	private View weizhuceview;
	private TextView weizhuce;
	private TextView zhuce;
	private View zhuceview;
	private ViewPager viewPager;
	public final static int investment_1 = 0;
	public final static int investment_2 = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myclient_activity);
		MyApplication.getInstance().addActivity(this);
		if(MyApplication.app.getUser()!=null){
			userBean=MyApplication.app.getUser();
		}
		initView();
		initFragment();
	}
	private void initView() {
		viewPager=(ViewPager)findViewById(R.id.cliuent_fragments_viewPager);
		LinearLayout back =(LinearLayout) findViewById(R.id.mycliuent_back);
		mycliuent_chaxun=(EditText)findViewById(R.id.mycliuent_chaxun);
		zhucelayout=(View)findViewById(R.id.zhucelayout);
		zhuce=(TextView)findViewById(R.id.zhuce);
		zhuceview=(View)findViewById(R.id.zhuceview);
		weizhucelayout=(View)findViewById(R.id.weizhucelayout);
		weizhuce=(TextView)findViewById(R.id.weizhuce);
		weizhuceview=(View)findViewById(R.id.weizhuceview);
		weizhuce.setTextColor(Color.rgb(153, 153, 151));
		zhuce.setTextColor(Color.rgb(50, 50, 50));
		zhuceview.setVisibility(View.VISIBLE);
		zhucelayout.setOnClickListener(this);
		weizhucelayout.setOnClickListener(this);
		findViewById(R.id.myclient_sousuo).setOnClickListener(this);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	private void initFragment() {
		Bundle bundle = new Bundle();
		UnregisteredUsersFragment weizhuceyongfu = new UnregisteredUsersFragment();
		RegisteredUsersFragment zhuceyongfu = new RegisteredUsersFragment();
		bundle.putSerializable("userBean",userBean);
		weizhuceyongfu.setArguments(bundle);
		fragments.add(weizhuceyongfu);
		fragments.add(zhuceyongfu);
		
		ProductFragmentAdapter mAdapetr = new ProductFragmentAdapter(getSupportFragmentManager(), fragments);
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
				zhuce.setTextColor(Color.rgb(50, 50, 50));
				weizhuce.setTextColor(Color.rgb(153, 153, 151));
				weizhuceview.setVisibility(View.GONE);
				zhuceview.setVisibility(View.VISIBLE);
				viewPager.setCurrentItem(arg0);
				break;
			case investment_2:
				zhuce.setTextColor(Color.rgb(153, 153, 151));
				weizhuce.setTextColor(Color.rgb(50, 50, 50));
				weizhuceview.setVisibility(View.VISIBLE);
				zhuceview.setVisibility(View.GONE);
		        viewPager.setCurrentItem(arg0);
				break;
			}
		}
	};
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.zhucelayout:
			zhuce.setTextColor(Color.rgb(50, 50, 50));
			weizhuce.setTextColor(Color.rgb(153, 153, 151));
			weizhuceview.setVisibility(View.GONE);
			zhuceview.setVisibility(View.VISIBLE);
			viewPager.setCurrentItem(0);
			break;
		case R.id.weizhucelayout:
			zhuce.setTextColor(Color.rgb(153, 153, 151));
			weizhuce.setTextColor(Color.rgb(50, 50, 50));
			weizhuceview.setVisibility(View.VISIBLE);
			zhuceview.setVisibility(View.GONE);
			viewPager.setCurrentItem(1);
			break;
		case R.id.myclient_sousuo:
			Toast.makeText(CustomerActivity.this, "该功能正在开发中", Toast.LENGTH_SHORT).show();
			break;
		}

	}
}
