package cn.com.ubankers.www.authentication.controller.activity;



import java.util.ArrayList;

import com.loopj.android.http.AsyncHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.authentication.service.WelcomeService;
import cn.com.ubankers.www.authentication.view.GuidePicAdapter;
import cn.com.ubankers.www.utils.ReceiveMsgService;
import cn.com.ubankers.www.utils.Tools;

public class WelcomeActivity extends Activity {
	public static Context context;
	private boolean conncetState = true;
	private ReceiveMsgService receiveMsgService;
	private long time1;
	private long time2;
	private ViewPager mViewPager;
	private int platform = 1;
	private WelcomeService welcomeService;
	//private GuidePicAdapter guidePicAdapter;
	private TimeCount timeCount;
	private int lastValue = -1;
	public static boolean flag = false;
	public AsyncHttpClient client;
	private LinearLayout ll_view_scolc;
	private ArrayList<ImageView> guidePage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		context = WelcomeActivity.this;
		welcomeService = new WelcomeService(context);
		client = MyApplication.app.getClient(context);
		//读取SharedPreferences中需要的数据
        SharedPreferences preferences = getSharedPreferences("count",MODE_WORLD_READABLE);
        int count = preferences.getInt("count", 0);
        //判断程序与第几次运行，如果是第一次运行则跳转到引导页面
        if (count == 0) {
        	initView();
        }else{
        	timeCount = new TimeCount(1000, 1000);
    		timeCount.start();
        }
        Editor editor = preferences.edit();
        //存入数据
        editor.putInt("count", ++count);
        //提交修改
        editor.commit();
        MyApplication.getInstance().addActivity(this);
	}
	private void initView() {
		guidePage = welcomeService.guidePage();
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		ll_view_scolc=(LinearLayout) findViewById(R.id.ll_view_scolc);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		GuidePicAdapter guidePicAdapter = new GuidePicAdapter(context,guidePage);
		mViewPager.setAdapter(guidePicAdapter);
		for(int i=0;i<guidePage.size();i++){
			ll_view_scolc.addView(Tools.getDot1(context, i));
		}
	}
	private class MyOnPageChangeListener implements OnPageChangeListener {
		@SuppressWarnings("static-access")
		@Override
		//当滑动状态改变时调用
		public void onPageScrollStateChanged(int arg0) {
			 if(arg0 == 0){
	        	   if(lastValue == welcomeService.images.length-1){
	        		   welcomeService.loginToApplition();
	        	   }
	          }	    
		}
		//当前页面被滑动时调用
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			lastValue = arg0;
		}
		//当新的页面被选中时调用
		@Override
		public void onPageSelected(int position) {
			ll_view_scolc.removeAllViews();
			for (int j = 0; j < guidePage.size(); j++) {
				ll_view_scolc.addView(Tools.getDotPager1(context, j,
						position));
			}
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.finish();
		super.onBackPressed();
	}

	/**
	 * 计时器
	 */
	public class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
       
		public void onFinish() {
			welcomeService.loginToApplition();
		}
		public void onTick(long millisUntilFinished) {

		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Return the kind of action being performed -- 
		//one of either ACTION_DOWN, ACTION_MOVE, ACTION_UP, or ACTION_CANCEL.
		int action = event.getAction();
		/**
		 * 屏幕上的每一次触摸都会被onTouchEvent捕获到，可以从event得到其x，y的值。
		 * 特别注意：并且得到的是你当前的触点的x，y坐标的值，也就是说，往左划动的话，
		 * 你的x的值是变小的。
		 */
		float x = event.getX();
		System.out.println("onTouchEvent--" + x);
		switch (action) {
		//按下
		case MotionEvent.ACTION_DOWN:
			if(lastValue == welcomeService.images.length-1){
     		  welcomeService.loginToApplition();
     	   	}
			break;
		//移动
		case MotionEvent.ACTION_MOVE:
		
			break;
		//松手
		case MotionEvent.ACTION_UP:
			break;
		}
		return true;
	}
}