package cn.com.ubankers.www.product.controller.activity;

import java.util.ArrayList;
import java.util.List;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.product.model.ProductDetail;
import cn.com.ubankers.www.product.service.ProductHttp;
import cn.com.ubankers.www.product.service.ProductService;
import cn.com.ubankers.www.product.view.ProductFragmentAdapter;
import cn.com.ubankers.www.user.model.UserBean;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * author zhangpengfei
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ProductActivity extends FragmentActivity implements
		OnClickListener {
	public ViewPager mViewPager;
	private UserBean userBean;
	private Bundle bundle;
	public  Context context;
	private ArrayList<Fragment> fragments;
	public static LinearLayout scrollView, productLayout;
	private int currentItem = 0; // 当前图片的索引号
	private int oldPosition = 0; // 上一次页面的位置
	private View view, layoutMiddle;
	public PopupWindow popWindow;
	private HorizontalScrollView horizontalListView;
	public ImageView popwindow_imageView;
	private int Click = 1;
	private View popwindow_layout;
	private RadioButton ming_radio, management_radio, trust_radio,
			financial_radio, private_radio, family_radio, global_radio;
	private RadioGroup genderGroup;
	private List<ImageView> list;
	public TextView productTitle;
	private ProductService productService;
	int[] tag = { 101 };
	String[] strArray = { "1", "4", "5", "7", "10", "11", "12" };
	private ProductHttp ProductHttp;
	private String Fragmentname = "MingStartFragment";
	private List<ProductDetail> mianlistview;
	public boolean  isPlus;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);
		context = this;
		productService = new ProductService(context);
		bundle = this.getIntent().getExtras();
		if (bundle != null) {
			userBean = (UserBean) bundle.getSerializable("userBean");
		}
		initView();
		initFragment();
		getDot(fragments);	
		MyApplication.getInstance().addActivity(this);
	}

	// 初始化layout控件
	private void initView() {
		
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		scrollView = (LinearLayout) findViewById(R.id.product_view_scolc);
		productLayout = (LinearLayout) findViewById(R.id.productLayout);
		productTitle = (TextView) findViewById(R.id.productTitle);
		popwindow_imageView = (ImageView) findViewById(R.id.popwindow_imageView);
		layoutMiddle = getLayoutInflater().inflate(R.layout.popwindow_down,
				null, false);// popwindow子view
		horizontalListView = (HorizontalScrollView) layoutMiddle
				.findViewById(R.id.horizontalListView);
		popwindow_layout = findViewById(R.id.popwindow_layout);
		genderGroup = (RadioGroup) layoutMiddle
				.findViewById(R.id.radioGroup_radio);
		ming_radio = (RadioButton) layoutMiddle.findViewById(R.id.ming_radio);// 明星产品
		management_radio = (RadioButton) layoutMiddle
				.findViewById(R.id.management_radio);// 资管
		trust_radio = (RadioButton) layoutMiddle.findViewById(R.id.trust_radio);// 信托
		financial_radio = (RadioButton) layoutMiddle
				.findViewById(R.id.financial_radio);// 金融交易所产品
		private_radio = (RadioButton) layoutMiddle
				.findViewById(R.id.private_radio);
//		family_radio = (RadioButton) layoutMiddle
//				.findViewById(R.id.family_radio);// 家族财富
		global_radio = (RadioButton) layoutMiddle
				.findViewById(R.id.global_radio);// 环球投资
	    productTitle.setText("明星产品");
		popwindow_layout.setOnClickListener(this);
		genderGroup.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
	}

	// 各个产品项的监听事件
	private class MyOnCheckedChangeListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			
			productService.intentToFragement(checkedId);
		}
	}

	// 加载所有的fragments
	private void initFragment() {
		fragments = productService.getFragments(userBean);
		ProductFragmentAdapter mAdapetr = new ProductFragmentAdapter(
				getSupportFragmentManager(), fragments);
		mViewPager.setOffscreenPageLimit(6); 
		mViewPager.setAdapter(mAdapetr);
		mViewPager.setOnPageChangeListener(pageListener);
	}

	/**
	 * 获取首页的点数
	 * 
	 * @param fragments
	 */
	private void getDot(ArrayList<Fragment> fragments) {
		scrollView.removeAllViews();
		for (int i = 0; i < fragments.size(); i++) {
			scrollView.addView(productService.getDot(context, i));
		}
	}

	/**
	 * ViewPager切换监听方法
	 * */
	public OnPageChangeListener pageListener = new OnPageChangeListener() {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			scrollView.removeAllViews();
			if (popWindow != null && popWindow.isShowing()) {
				productService.viewPagerChange(position);
				popwindow_imageView.setImageResource(R.drawable.plus);
				productLayout.setAlpha(0.6f);
				isPlus =false;
			}
			for (int j = 0; j < fragments.size(); j++) {
				scrollView.addView(productService.getDotPager(context, j,
						position));
			}
		}
	};
	//
	private void showPopWindow(View parent) {
		if (popWindow != null && popWindow.isShowing()) {
			popWindow.dismiss();
		} else {
			popWindow = new PopupWindow(layoutMiddle,
					LayoutParams.MATCH_PARENT, LayoutParams.FILL_PARENT, true);
			popWindow.update();
			WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			int xPos = windowManager.getDefaultDisplay().getWidth() / 2
					- popWindow.getWidth() / 2; //显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
			popWindow.showAsDropDown(parent, xPos, 0);//设置popupwindow的位置
			popWindow.setTouchable(true); //设置popupwindow可点击
			popWindow.setOutsideTouchable(false); //设置popupwindow外部可点击
			popWindow.setFocusable(false); //获取焦点
			layoutMiddle.setOnTouchListener(new myOnTouchListener());
		}
	}
	/**
	 * 当点击popwindow外部关闭popwindow
	 * 
	 */
	public class myOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			productService.closePopwindow();
			return false;
		}
	}
	@SuppressLint("NewApi")
	@Override
	public void onClick(View arg0) {
		showPopWindow(arg0);
		switch (arg0.getId()) {
		case R.id.popwindow_layout:
			if (popWindow != null && popWindow.isShowing()) {
				productLayout.setAlpha(1);
				isPlus =true;
				popwindow_imageView.setImageResource(R.drawable.plus_xhdpi);
			} else {
				popwindow_imageView.setImageResource(R.drawable.plus);
				productLayout.setAlpha(0.6f);
				isPlus =false;
			}
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			     productService.ExitDialog(context).show();  
			     return true;
		}
		// 拦截MENU按钮点击事件，让他无任何操作
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
