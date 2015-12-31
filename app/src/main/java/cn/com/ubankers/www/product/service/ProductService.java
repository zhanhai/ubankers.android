package cn.com.ubankers.www.product.service;

import java.util.ArrayList;
import java.util.List;

import com.loopj.android.http.AsyncHttpClient;

import android.R.array;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.product.controller.activity.ProductActivity;
import cn.com.ubankers.www.product.controller.frament.FamilyFragment;
import cn.com.ubankers.www.product.controller.frament.GlobaFragment;
import cn.com.ubankers.www.product.controller.frament.MainFragment;
import cn.com.ubankers.www.product.controller.frament.MakertFragment;
import cn.com.ubankers.www.product.controller.frament.MingStartFragment;
import cn.com.ubankers.www.product.controller.frament.PrivateFundFragment;
import cn.com.ubankers.www.product.controller.frament.TrustFragment;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.utils.UpdateUtils;
import cn.com.ubankers.www.utils.Util;

/**
 * author zhang
 */
public class ProductService {
	private ArrayList<Fragment> fragments;
	private View view;
	private int oldPosition = 0; // 上一次页面的位置
	private int currentItem = 0; // 当前图片的索引号
	private Context context;
	private String[] prodcuts={"明星产品","资管","信托","金融交易所","私募基金","环球投资","家族财富"};
    private int width;
    private AsyncHttpClient client; 
	public ProductService(Context context) {
		super();
		this.context =context;
		width =Util.getScreenWidth(context);
	}

	/**
	 * 
	 * 初始化Fragment
	 **/
	public ArrayList<Fragment> getFragments(UserBean userBean) {
		fragments = new ArrayList<Fragment>();
		Bundle bundle = new Bundle();
		bundle.putSerializable("userBean", userBean);
		MingStartFragment mingStartFragment = new MingStartFragment();
		mingStartFragment.setArguments(bundle);
		MainFragment mainFragment = new MainFragment();
		mainFragment.setArguments(bundle);
		TrustFragment trust = new TrustFragment();
		trust.setArguments(bundle);
		MakertFragment makertFragment = new MakertFragment();
		makertFragment.setArguments(bundle);
		PrivateFundFragment privateOfferingFund = new PrivateFundFragment();
		privateOfferingFund.setArguments(bundle);
		GlobaFragment globaFragment = new GlobaFragment();
		globaFragment.setArguments(bundle);
//		FamilyFragment familywealth = new FamilyFragment();
//		familywealth.setArguments(bundle);
		fragments.add(mingStartFragment);
		fragments.add(mainFragment);
		fragments.add(trust);
		fragments.add(makertFragment);
		fragments.add(privateOfferingFund);
		fragments.add(globaFragment);
//		fragments.add(familywealth);
		return fragments;
	}

	/**
	 * 获取点数控件
	 * 
	 * @param fragments
	 */
	public View getDotWidget(Context context, int position) {
		view = new View(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width/50, width/50);
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
	public View getDotPager(Context context, int position, int pagePosition) {
		oldPosition = pagePosition;
		currentItem = pagePosition;
		view = getDotWidget(context, position);
		if (position == oldPosition || currentItem == position) {
			view.setBackgroundResource(R.drawable.dot_normal);
		} else {
			view.setBackgroundResource(R.drawable.dot_focused);
		}
		((ProductActivity)context).productTitle.setText(prodcuts[pagePosition]);
		return view;
	}
	/**
	 * 关闭popwindow
	 * 
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void closePopwindow(){
		if (((ProductActivity)context).popWindow != null && ((ProductActivity)context).popWindow.isShowing()) {
			((ProductActivity)context).popWindow.dismiss();
			((ProductActivity)context).popwindow_imageView.setImageResource(R.drawable.plus);
			((ProductActivity)context).isPlus =false;
			((ProductActivity)context).productLayout.setAlpha(0.6f);
			((ProductActivity)context).popWindow = null;
		}
	}
	 /**
	  * 
	  */
	public void viewPagerChange(int i){
		  ((ProductActivity)context).mViewPager.setCurrentItem(i);
		  if(((ProductActivity)context).popWindow!=null&&((ProductActivity)context).popWindow.isShowing()){
		  ((ProductActivity)context).popWindow.dismiss();
		  }
	}
	public void intentToFragement(int checkedId){
		int i=0;
		switch(checkedId){
		case R.id.ming_radio:
			i=0;
			 ((ProductActivity)context).productTitle.setText("明星产品");
			break;
		case R.id.management_radio:
			i=1;
			 ((ProductActivity)context).productTitle.setText("资管");
			break;
		case R.id.trust_radio:
			((ProductActivity)context).productTitle.setText("信托");
			i=2;
			break;
		case R.id.financial_radio:
			((ProductActivity)context).productTitle.setText("金融交易所");
			i=3;
			break;
		case R.id.private_radio:
			((ProductActivity)context).productTitle.setText("私募基金");
			i=4;
			break;
		case R.id.global_radio:
			((ProductActivity)context).productTitle.setText("环球投资");
			i=5;
			break;
		/*case R.id.family_radio:
			((ProductActivity)context).productTitle.setText("家族财富");
			i=6;
			break;*/
		}
		viewPagerChange(i);
		 
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
