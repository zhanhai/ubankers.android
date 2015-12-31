package cn.com.ubankers.www.product.controller.frament;


import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.authentication.model.UserQuestBean;
import com.ubankers.app.product.detail.ProductDetailActivity;
import cn.com.ubankers.www.product.model.PagerBean;
import cn.com.ubankers.www.product.model.ProductDetail;
import cn.com.ubankers.www.product.service.ACache;
import cn.com.ubankers.www.product.service.ProductHttp;
import cn.com.ubankers.www.product.view.ProductListAdapter;
import cn.com.ubankers.www.product.view.ProductPagerAdapter;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.utils.CompleteDialog;
import cn.com.ubankers.www.utils.LoginDialog;
import cn.com.ubankers.www.utils.NetWorking;
import cn.com.ubankers.www.utils.Tools;
import cn.com.ubankers.www.widget.AutoScrollViewPager;
import cn.com.ubankers.www.widget.MyListView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

public class MakertFragment extends Fragment {
	List<ProductDetail> mianlistview;
	ProductDetail ProductConstants;
	UserQuestBean request;
	Gson gson;
	Activity activity;
	MyListView mListView;

	private ProductListAdapter mAdaptermian;
	String text;
	ImageView detail_loading;
	private AutoScrollViewPager mViewPager;
	private ScheduledExecutorService scheduledExecutorService;
	private List<ImageView> list;
	public final static int SET_NEWSLIST = 0;
	public final static int PICTURE_VIEW = 1;
	private String role = "tourist";
	private ProductDetail Product;
	private Intent intent;
	private UserBean user;
	private static final String Image = "/width/720";
	private List<PagerBean> pagerList;
	private View view;
	private int currentItem = 0; // 当前图片的索引号
	private int oldPosition = 0; // 上一次页面的位置
	private AsyncHttpClient client;
	int[] tag = { 412 };
	String[] strArray = { "1", "4", "5", "7", "10", "11", "12" };
	private ProductHttp ProductHttp;
	private String Fragmentname = "MakertFragment";
	private ACache mCache;
	private LoginDialog loginDialog;
	private CompleteDialog completeDialog;
	View myView1;
	private LinearLayout ll_view_scolc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ProductHttp = new ProductHttp(activity, tag, strArray, Fragmentname);
		Bundle args = getArguments();
		text = args != null ? args.getString("text") : "";
		loginDialog = new LoginDialog(activity,1,1);
		mCache = ACache.get(activity);
		if (new NetWorking().isNetworkAvailable(activity) == false) {
			Toast.makeText(activity, "无法连接到网络，请检查网络设置。", 1).show();
			JSONObject object = mCache.getAsJSONObject(Fragmentname);
			handler.obtainMessage(SET_NEWSLIST).sendToTarget();
			mianlistview = ProductHttp.Analytic(object, 2);
			JSONArray array = mCache.getAsJSONArray("MakertFragmentimg");
			try {
				list = ProductHttp.Analyticimghq(array);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			mianlistview=ProductHttp.dataRequest();
			list=ProductHttp.initImage();
			getDot(list, ll_view_scolc);
			handler.obtainMessage(SET_NEWSLIST).sendToTarget();
		}
		 registerBoradcastReceiver();
		super.onCreate(savedInstanceState);
	}
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
		@Override  
        public void onReceive(Context context, Intent intent) {  
            String action = intent.getAction();  
            if(action.equals("ACTION_NAME")){  
            	mianlistview=ProductHttp.dataRequest();
            	handler.obtainMessage(SET_NEWSLIST).sendToTarget();
            }  
            abortBroadcast();
        }  
          
    }; 
       
    public void registerBoradcastReceiver(){ 
    	  //注册广播   
        IntentFilter myIntentFilter = new IntentFilter();  
        myIntentFilter.addAction("ACTION_NAME");       
       activity.registerReceiver(mBroadcastReceiver, myIntentFilter);  
    }
	
	/**
	 * 获取首页的点数
	 * 
	 * @param fragments
	 */
	private void getDot(List<ImageView> list,LinearLayout ll_view_scolc) {
		for (int i = 0; i < list.size(); i++) {
			ll_view_scolc.addView(Tools.getDot(activity, i));
		}
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.activity = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.main_fragment, container, false);
		mListView = (MyListView) view.findViewById(R.id.mListView);
		View myView = inflater.from(activity).inflate(R.layout.view_header,
				null);
		myView1 = inflater.from(activity).inflate(R.layout.view_footer,
				null);
		mViewPager = (AutoScrollViewPager) myView.findViewById(R.id.vp_page);
		ll_view_scolc=(LinearLayout) myView.findViewById(R.id.ll_view_scolc);
		mViewPager.setAdapter(new ProductPagerAdapter(activity, list));
		mViewPager.setOnPageChangeListener(new MyPageChangeListener());
		mListView.addHeaderView(myView);
		myView1.setVisibility(View.GONE);
//		mListView.addFooterView(myView1,null,false);
		TextView item_textview = (TextView) view
				.findViewById(R.id.item_textview);
		detail_loading = (ImageView) view.findViewById(R.id.detail_loading);
		item_textview.setText(text);
		mListView.setOnScrollListener(new MyScrollListener());
		return view;
	}
	private class MyPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int position) {
			ll_view_scolc.removeAllViews();
			for (int j = 0; j < list.size(); j++) {
				ll_view_scolc.addView(Tools.getDotPager(activity, j,
						position));
			}
		}

	}
	 /**
	    * listener scroll state
	    */
	private class MyScrollListener implements OnScrollListener{
		@Override
		public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub

			if(visibleItemCount==1){
				mListView.addFooterView(myView1,null,false);	
				myView1.setVisibility(View.VISIBLE);
	    	}
		}

		@Override
		public void onScrollStateChanged(AbsListView arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
		}
//		private class MyScrollListener implements OnScrollListener{
//		@Override
//		public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//			// TODO Auto-generated method stub
//			ProductActivity productActivity = (ProductActivity) getActivity();
//			if(firstVisibleItem>=1){
//	    		productActivity.productLayout.setBackgroundColor(0xff555555);
//	    		productActivity.productLayout.setAlpha(1);
//	    	}else{
//	    		productActivity.productLayout.setBackgroundColor(0xff555555 );
//	    		productActivity.productLayout.setAlpha(0.6f);
//	    	}
//		}
//		@Override
//		public void onScrollStateChanged(AbsListView arg0, int scrollState) {
//			// TODO Auto-generated method stub
//			switch (scrollState) {  
//	        case OnScrollListener.SCROLL_STATE_IDLE: //    
//	            System.out.println("停止..."); 
//	            break;  
//	        case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:  
//	        	System.out.println("正在滚动..."); 
//	            break;  
//	        case OnScrollListener.SCROLL_STATE_FLING:    
//	            System.out.println("开始滚动...");  
//
//	            break;  
//	        }  
//			
//		}
//	  }
//	@Override
//	public void setUserVisibleHint(boolean isVisibleToUser) {
//		// TODO Auto-generated method stub
//		super.setUserVisibleHint(isVisibleToUser);
//		if (isVisibleToUser == true) {
//			mianlistview = ProductHttp.dataRequest();
//			list = ProductHttp.initImage();
//			handler.obtainMessage(SET_NEWSLIST).sendToTarget();
//		}
//	}

	@Override
	public void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 3,
				TimeUnit.SECONDS);
		super.onStart();
	}

	@Override
	public void onStop() {
		// 当Activity不可见的时候停止切换
		scheduledExecutorService.shutdown();
		super.onStop();
	}

	/**
	 * 换行切换任务
	 * 
	 * @author Anthony
	 * 
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (mViewPager) {
				System.out.println("currentItem: " + currentItem);
				currentItem = (currentItem + 1) % list.size();
				handler.obtainMessage(PICTURE_VIEW).sendToTarget(); // 通过Handler切换图片
			}
		}

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			// TODO Auto-generated method stub
			switch (msg.what) {
			case SET_NEWSLIST:
				// detail_loading.setVisibility(View.GONE);
				// mAdapter = new NewsAdapter(activity, newsList);
				// mListView.setAdapter(mAdapter);
				System.out.println(mianlistview.toString());
				mAdaptermian = new ProductListAdapter(activity, mianlistview);
				if (mListView != null) {
					mListView.setAdapter(mAdaptermian);
					mListView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							UserBean userBean = MyApplication.app.getUser();
							if (MyApplication.app.getUser() == null) {
								loginDialog.onLogin();
							}else{
								ProductDetail product = mianlistview.get(arg2 - 1);
								intent = new Intent(activity,ProductDetailActivity.class);
								if (intent != null) {
									/*intent.putExtra("productId",
											product.getProductId());
									intent.putExtra("productName", product.getProductName());
									intent.putExtra("face", product.getFace());*/
									Bundle bundle = new Bundle();
									bundle.putSerializable(ProductDetailActivity.KEY_PRODUCT_DETAIL, product);
									intent.putExtras(bundle);
									startActivity(intent);
								}
							}
						}
					});
				}
				break;
			case PICTURE_VIEW:
				mViewPager.getAdapter().notifyDataSetChanged();
				mViewPager.setCurrentItem(currentItem);// 切换当前显示的图片
				break;
			default:
			}
			super.handleMessage(msg);
		}
	};
}
