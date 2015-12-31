package cn.com.ubankers.www.sns.controller.activity;

import java.util.ArrayList;


import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.product.view.ProductFragmentAdapter;
import cn.com.ubankers.www.sns.service.SnsService;
import cn.com.ubankers.www.sns.view.SnsFragmentAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SnsActivity extends FragmentActivity implements OnClickListener {
	private LinearLayout scrollView;
	public ViewPager mViewPager;
	private int currentItem = 0; // 当前图片的索引号
	private ArrayList<Fragment> fragments;
	public ImageView popwindow_imageView;
	public static Context context;
	private SnsService snsService;
	public LinearLayout productLayout;
	public PopupWindow popWindow;
	private View layoutMiddle;
	public TextView productTitle;
	private HorizontalScrollView horizontalListView;
	private RadioButton sns_article, sns_video, sns_view, sns_home;
	private RadioGroup radioGroup_radio;
	private View popwindow_layout;
	public static boolean isContinue = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);
		context = this;
		snsService = new SnsService(context);
		initView();
		initFragment();
		//showDot(fragments);
		MyApplication.getInstance().addActivity(this);
	}

	// 初始化layout控件
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		scrollView = (LinearLayout) findViewById(R.id.product_view_scolc);
		productLayout = (LinearLayout) findViewById(R.id.productLayout);
		productTitle = (TextView) findViewById(R.id.productTitle);
		popwindow_imageView = (ImageView) findViewById(R.id.popwindow_imageView);
		popwindow_imageView.setVisibility(View.GONE);
		layoutMiddle = getLayoutInflater().inflate(R.layout.snspopwindow_down,null);
		horizontalListView = (HorizontalScrollView) layoutMiddle.findViewById(R.id.sns_horizontalListView);
		popwindow_layout = findViewById(R.id.popwindow_layout);
		radioGroup_radio = (RadioGroup) layoutMiddle.findViewById(R.id.radioGroup_radio);
		sns_article = (RadioButton) layoutMiddle.findViewById(R.id.sns_article);// 文章
		sns_video = (RadioButton) layoutMiddle.findViewById(R.id.sns_video);// 视频
		sns_view = (RadioButton) layoutMiddle.findViewById(R.id.sns_view);// 视频
		sns_home = (RadioButton) layoutMiddle.findViewById(R.id.sns_home);// 主页
//		popwindow_layout.setOnClickListener(this);
		radioGroup_radio.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
		productTitle.setText("文章");
	}
	
	
	// 各个产品项的监听事件
	private class MyOnCheckedChangeListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			snsService.intentToFragement(checkedId);
		}

	}


	/**
	 * 初始化Fragment
	 **/
	private void initFragment() {
		fragments = snsService.getFragment();
		SnsFragmentAdapter mAdapetr = new SnsFragmentAdapter(
				getSupportFragmentManager(), fragments);
		// mViewPager.setOffscreenPageLimit(0);
		mViewPager.setOffscreenPageLimit(0); 
		mViewPager.setAdapter(mAdapetr);	
		mViewPager.setOnPageChangeListener(pageListener);
		//按下时不继续定时滑动,弹起时继续定时滑动
		mViewPager.setOnTouchListener(new View.OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
						case MotionEvent.ACTION_MOVE:
							isContinue = false;
							break;
						case MotionEvent.ACTION_UP:
							isContinue = true;
							break;
						default:
							isContinue = true;
							break;
						}
						return false;
					}
				});
	}
	/**
	 * 处理定时切换广告栏图片的句柄
	 */
	private final Handler viewHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			mViewPager.getAdapter().notifyDataSetChanged();
			mViewPager.setCurrentItem(msg.what);
			super.handleMessage(msg);
		}

	};
	
	/**
	 * 获取首页的点数
	 * 
	 * @param fragments
	 */
	private void showDot(ArrayList<Fragment> fragments) {
		scrollView.removeAllViews();
		for (int i = 0; i < fragments.size(); i++) {
			scrollView.addView(snsService.getDot(context, i));
		}
	}

	/**
	 * PopWindow
	 **/
	private void showPopWindow(View parent) {
		if (popWindow != null && popWindow.isShowing()) {
			popWindow.dismiss();
		} else {
			popWindow = new PopupWindow(layoutMiddle,
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,true);
			popWindow.update();
			popWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
			popWindow.setTouchable(true); // 设置popupwindow可点击
			popWindow.setOutsideTouchable(false); // 设置popupwindow外部可点击
			popWindow.setFocusable(false); // 获取焦点
			WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
			int xPos = windowManager.getDefaultDisplay().getWidth() / 2
					- popWindow.getWidth() / 2;
			popWindow.showAsDropDown(parent, xPos, 0);// 设置popupwindow的位置
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
			snsService.closePopwindow();
			return false;
		}
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
//			Log.e("kjjkdkjd=====", "onPageScrollStateChanged()");
		}
		@Override
		public void onPageScrolled(int position, float arg1, int arg2) {
//			Log.e("kjjkdkjd=====", "onPageScrolled()");
		}
		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			scrollView.removeAllViews();
			if (popWindow != null && popWindow.isShowing()) {
				snsService.viewPagerChange(position);
				popwindow_imageView.setImageResource(R.drawable.plus);
				productLayout.setAlpha(0.6f);
			}
			for (int j = 0; j < fragments.size(); j++) {
				scrollView.addView(snsService.getDotPager(context, j, position));
			}
		}
	};
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		showPopWindow(arg0);
		switch (arg0.getId()) {
		case R.id.popwindow_layout:
			if (popWindow != null && popWindow.isShowing()) {
				productLayout.setAlpha(1);
				popwindow_imageView.setImageResource(R.drawable.plus_xhdpi);
			} else {
				popwindow_imageView.setImageResource(R.drawable.plus);
				productLayout.setAlpha(0.6f);
			}
			break;
		default:
			break;
		}
	}
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				    ExitDialog(context).show();  
				     return true;
			}
			// 拦截MENU按钮点击事件，让他无任何操作
			if (keyCode == KeyEvent.KEYCODE_MENU) {
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}
	 /**
		 * 退出应用程序
		 * @param context
		 * @return
		 */
		 public  Dialog ExitDialog(Context context) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("退出应用");
			builder.setMessage("是否退出应用？");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					MyApplication.app.exitApply();
					MyApplication.getInstance().exit();
					System.exit(0);
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			});
			return builder.create();
		}

}
