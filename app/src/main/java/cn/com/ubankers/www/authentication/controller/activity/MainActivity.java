package cn.com.ubankers.www.authentication.controller.activity;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.authentication.service.MainService;
import cn.com.ubankers.www.product.controller.activity.ProductActivity;
import cn.com.ubankers.www.sns.controller.activity.SnsActivity;
import cn.com.ubankers.www.user.controller.activity.UserCenterActivity;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.utils.Util;

public class MainActivity extends TabActivity {
	private RadioGroup group;
	private RadioButton radioButton0, radioButton1, radioButton2;// ,radioButton3;
	private TabHost tabHost;
	public static final String TAB_HOME = "product";
	public static final String TAB_MES = "sns";
	// public static final String TAB_TOPIC="knowledge";
	public static final String TAB_TOUCH = "gerenzhongxin";
	private Drawable myImage, myImage1, myImage2;// myImage3;
	private Resources res;
	private UserBean userBean;
	private Bundle bundle;
	private Intent intent1, intent2, intent3;// intent4;
	private int width, mScreenWidth;
	private Context context;
	private MainService mainService;
	public boolean type=false;
	private String backType = null;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(this.getIntent()!=null){
			type=this.getIntent().getBooleanExtra("type", false);
			backType=this.getIntent().getStringExtra("backType");
		}
		setContentView(R.layout.activity_main);
		context = this;
		mScreenWidth = Util.getWindowsWidth(this);
		width = mScreenWidth / 16; // 一个Item宽度为屏幕的1/4
		tabHost = getTabHost();
		res = tabHost.getResources();
		initView();
		try{
			if(backType==null){
				mainService = new MainService(context,0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		MyApplication.getInstance().addActivity(this);
	}

	private void initView() {
		group = (RadioGroup) findViewById(R.id.main_radio);
		radioButton0 = (RadioButton) findViewById(R.id.radio_button0);
		radioButton1 = (RadioButton) findViewById(R.id.radio_button1);
		radioButton2 = (RadioButton) findViewById(R.id.radio_button2);
		// radioButton3 = (RadioButton) findViewById(R.id.radio_button3);
		myImage = res.getDrawable(R.drawable.productblue);
		myImage1 = res.getDrawable(R.drawable.snsgray);
		// myImage2 = res.getDrawable(R.drawable.topicgray);
		myImage2 = res.getDrawable(R.drawable.usergray);
		getImage();
		intent1 = new Intent(this, ProductActivity.class);
		intent2 = new Intent(this, SnsActivity.class);
		// intent3 =new Intent(this,KnowLedgeActivity.class);
		intent3 = new Intent(this, UserCenterActivity.class);
		Intent intent = new Intent();
		bundle = intent.getExtras();
		if (bundle != null) {
			userBean = (UserBean) bundle.getSerializable("userBean");
			bundle.putSerializable("userBean", userBean);
			intent1.putExtras(bundle);
			intent2.putExtras(bundle);
			intent3.putExtras(bundle);
			// intent4.putExtras(bundle);
		}
		tabHost.addTab(tabHost.newTabSpec(TAB_HOME).setIndicator(TAB_HOME)
				.setContent(intent1));
		tabHost.addTab(tabHost.newTabSpec(TAB_MES).setIndicator(TAB_MES)
				.setContent(intent2));
		// tabHost.addTab(tabHost.newTabSpec(TAB_TOPIC).setIndicator(TAB_TOPIC).setContent(intent3));
		tabHost.addTab(tabHost.newTabSpec(TAB_TOUCH).setIndicator(TAB_TOUCH)
				.setContent(intent3));
		group.setOnCheckedChangeListener(new MyCheckedChangeListener());

	}

	/**
	 * 
	 * 四个模块之间的切换功能
	 * 
	 */
	private class MyCheckedChangeListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(RadioGroup arg0, int checkedId) {
			// TODO Auto-generated method stub
			switch (checkedId) {
			case R.id.radio_button0:
				tabHost.setCurrentTabByTag(TAB_HOME);
				radioButton0.setTextColor(0xff0066cc);
				radioButton1.setTextColor(0xff666666);
				radioButton2.setTextColor(0xff666666);
				// radioButton3.setTextColor(0xff333333);
				myImage = res.getDrawable(R.drawable.productblue);
				myImage1 = res.getDrawable(R.drawable.snsgray);
				// myImage2 = res.getDrawable(R.drawable.topicgray);
				myImage2 = res.getDrawable(R.drawable.usergray);
				getImage();
				break;
			case R.id.radio_button1:
				tabHost.setCurrentTabByTag(TAB_MES);
				radioButton0.setTextColor(0xff666666);
				radioButton1.setTextColor(0xff0066cc);
				radioButton2.setTextColor(0xff666666);
				// radioButton3.setTextColor(0xff333333);
				myImage = res.getDrawable(R.drawable.productgray);
				myImage1 = res.getDrawable(R.drawable.snsblue);
				// myImage2 = res.getDrawable(R.drawable.topicgray);
				myImage2 = res.getDrawable(R.drawable.usergray);
				getImage();
				break;
			/*
			 * case R.id.radio_button2: tabHost.setCurrentTabByTag(TAB_TOPIC);
			 * radioButton0.setTextColor(0xff333333);
			 * radioButton1.setTextColor(0xff333333);
			 * radioButton2.setTextColor(0xff0066cc);
			 * radioButton3.setTextColor(0xff333333); myImage =
			 * res.getDrawable(R.drawable.productgray); myImage1 =
			 * res.getDrawable(R.drawable.snsgray); myImage2 =
			 * res.getDrawable(R.drawable.topicblue); myImage3 =
			 * res.getDrawable(R.drawable.usergray); getImage(); break;
			 */
			case R.id.radio_button2:
				tabHost.setCurrentTabByTag(TAB_TOUCH);
				radioButton0.setTextColor(0xff666666);
				radioButton1.setTextColor(0xff666666);
				// radioButton2.setTextColor(0xff333333);
				radioButton2.setTextColor(0xff0066cc);
				myImage = res.getDrawable(R.drawable.productgray);
				myImage1 = res.getDrawable(R.drawable.snsgray);
				// myImage2 = res.getDrawable(R.drawable.topicgray);
				myImage2 = res.getDrawable(R.drawable.userblue);
				getImage();
				break;
			default:
				break;
			}
		}
	}
	private void getImage() {
		myImage.setBounds(1, 1, width, width);
		myImage1.setBounds(1, 1, width, width);
		myImage2.setBounds(1, 1, width, width);
		// myImage3.setBounds(1, 1, width, width);
		radioButton0.setCompoundDrawables(null, myImage, null, null);
		radioButton1.setCompoundDrawables(null, myImage1, null, null);
		radioButton2.setCompoundDrawables(null, myImage2, null, null);
		// radioButton3.setCompoundDrawables(null, myImage3, null, null);
	}
}