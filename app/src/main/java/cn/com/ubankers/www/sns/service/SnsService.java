package cn.com.ubankers.www.sns.service;

import java.util.ArrayList;

import com.loopj.android.http.PersistentCookieStore;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.product.controller.activity.ProductActivity;
import cn.com.ubankers.www.sns.controller.activity.SnsActivity;
import cn.com.ubankers.www.sns.controller.fragment.ArticleFragment;
import cn.com.ubankers.www.sns.controller.fragment.FocuseFragment;
import cn.com.ubankers.www.sns.controller.fragment.PersonalFragment;
import cn.com.ubankers.www.sns.controller.fragment.VodieFragment;
import cn.com.ubankers.www.utils.Util;

public class SnsService {
	private ArrayList<Fragment> fragments;
	private View view;
	private int oldPosition = 0; // 上一次页面的位置
	private int currentItem = 0; // 当前图片的索引号
	private Context context ;
	private String[] articles={"文章","个人主页","视频","观点"};
	private int width;
  
	public SnsService(Context context) {
		super();
		this.context = context;
		width =Util.getScreenWidth(context);
	}

	/**
	 * 
	 * 初始化Fragment
	 **/
	public ArrayList<Fragment> getFragment() {
		fragments = new ArrayList<Fragment>();
		ArticleFragment articleFragment = new ArticleFragment();
		//FocuseFragment focuseFragment = new FocuseFragment();
		//VodieFragment vodieFragment = new VodieFragment();
		PersonalFragment personalFragment = new PersonalFragment();
		fragments.add(articleFragment);
		//fragments.add(focuseFragment);
		//fragments.add(vodieFragment);
		//fragments.add(personalFragment);
		return fragments;
	}

	/**
	 * 获取点数控件
	 * 
	 * @param fragments
	 */
	public View getDotWidget(Context context, int position) {
		view = new View(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width/50,width/50);
		params.setMargins(5, 0, 5, 0);
		view.setLayoutParams(params);
		return view;
	}

	/**
	 * 获取开始时点数的位置
	 * 
	 * @param fragments
	 */
	public View getDot(Context context, int position) {
		view = getDotWidget(context, position);
		if (oldPosition == position) {
			view.setBackgroundResource(R.drawable.dot_normal);
		} else {
			view.setBackgroundResource(R.drawable.dot_focused);
		}
		return view;
	}

	/**
	 * 获取滑动时点数的位置
	 * 
	 * @param fragments
	 */
	public View getDotPager(Context context, int position,int pagePosition) {
		oldPosition =pagePosition;
		currentItem =pagePosition;
		view = getDotWidget(context, position);
		if (position == oldPosition || currentItem == position) {
			view.setBackgroundResource(R.drawable.dot_normal);
		} else {
			view.setBackgroundResource(R.drawable.dot_focused);
		}
		((SnsActivity)context).productTitle.setText(articles[pagePosition]);
		return view;
	}
	/**
	 * 各个模块之间的切换
	 */
	public void viewPagerChange(int i){
		((SnsActivity)context).mViewPager.getAdapter().notifyDataSetChanged();
		  ((SnsActivity)context).mViewPager.setCurrentItem(i);
		  if(((SnsActivity)context).popWindow!=null&&((SnsActivity)context).popWindow.isShowing()){
		  ((SnsActivity)context).popWindow.dismiss();
		  }
	}
	public void intentToFragement(int checkedId){
		int i=0;
		switch(checkedId){
		case R.id.sns_article:
			i=0;
			 ((SnsActivity)context).productTitle.setText("文章");
			break;
		case R.id.sns_video:
		    i=1;
			 ((SnsActivity)context).productTitle.setText("视频");
			break;
		case R.id.sns_view:
			((SnsActivity)context).productTitle.setText("观点");
			i=2;
			break;
		case R.id.sns_home:
			((SnsActivity)context).productTitle.setText("个人主页");
			i=3;
			break;
		}
		viewPagerChange(i);
		 
	}
	/**
	 * 关闭popwindow
	 * 
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void closePopwindow(){
		if (((SnsActivity)context).popWindow != null && ((SnsActivity)context).popWindow.isShowing()) {
			((SnsActivity)context).popWindow.dismiss();
			((SnsActivity)context).popwindow_imageView.setImageResource(R.drawable.plus);
			((SnsActivity)context).productLayout.setAlpha(0.6f);
	}
	}
}
