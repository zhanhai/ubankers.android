package cn.com.ubankers.www.sns.controller.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.http.ParseUtils;
import cn.com.ubankers.www.product.controller.activity.ProductActivity;
import cn.com.ubankers.www.product.service.ACache;
import cn.com.ubankers.www.sns.controller.activity.SnsActivity;
import cn.com.ubankers.www.sns.controller.activity.SnsArticleActivity;
import cn.com.ubankers.www.sns.model.ArticleBean;
import cn.com.ubankers.www.sns.view.ArticleListAdapter;
import cn.com.ubankers.www.sns.view.SnsPictureAdapter;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.utils.NetWorking;
import cn.com.ubankers.www.utils.XutilsHttp;
import cn.com.ubankers.www.widget.AutoScrollViewPager;
import cn.com.ubankers.www.widget.SnsDialog;
/*import cn.com.ubankers.www.view.MyListView;
 import cn.com.ubankers.www.view.MyViewPager;
 import cn.com.ubankers.www.view.MyViewPager.OnSingleTouchListener;*/
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.graphics.Bitmap;

public class ArticleFragment extends Fragment {

	static {
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.cache_bg)
				.showImageOnFail(R.drawable.cache_bg)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	private ListView rongListView;
	Activity activity;
	private int currentItem = 0; // 当前图片的索引号
	private ScheduledExecutorService scheduledExecutorService;
	public final static int PICTURE_VIEW = 1;
	public final static int ADAPTER_REFRESH  = 2;
	public final static int ADAPTER_LOADING=3;
	JSONArray array1;
	private TextView title;
	private UserBean userBean;
	private ArrayList<String> tiltes;
	private ArrayList<ImageView> list;
	private static ArrayList<ArticleBean> articleList;
	private LayoutInflater inflater;
	private View myView;
	private List<ArticleBean> listTitle ;
	private AutoScrollViewPager viewPager;
	public final static int SET_ARTICLE = 0;
	public static final int DETAILREQUSET = 1;
	private List<String> contents;
	private ArticleListAdapter adapter;
	private PullToRefreshListView mPullToRefreshListView;
	private ArticleBean articleBean;
	public SnsDialog snsDialog;
	private int start = 1;
	private int total = 1;
	private int totalpage = 1;
	private int currentPage = 0;
	private AsyncHttpClient client;
	private ACache mCache;
	private Handler mHandler;
	public static DisplayImageOptions options;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		mCache = ACache.get(activity);
		if (snsDialog == null) {
			snsDialog = SnsDialog.createDialog(activity);
		}
		client = MyApplication.app.getClient(activity);
		mHandler = new Handler();
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
		View view = LayoutInflater.from(activity).inflate(
				R.layout.article_fragment, container, false);
		mPullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.rongListView);
		rongListView = mPullToRefreshListView.getRefreshableView();
		myView = inflater.from(activity).inflate(R.layout.rong_view_header,null);
		viewPager = (AutoScrollViewPager) myView.findViewById(R.id.rongvp_page);
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		title = (TextView) myView.findViewById(R.id.title);
		rongListView.addHeaderView(myView);
		articleList = new ArrayList<ArticleBean>();
		if (articleList.size() == 0) {
			if (new NetWorking().isNetworkAvailable(activity) == false) {
				tiltes = new ArrayList<String>();
				JSONArray articlejson = mCache.getAsJSONArray("articlejson");
				snsJSONObjectToBean(articlejson);
			} else {
				initImage();
			}

		}
		
		if (new NetWorking().isNetworkAvailable(activity) == false) {
			JSONArray JSONArray = mCache.getAsJSONArray("snsjosn");
			adapter = new ArticleListAdapter(getActivity(),
					JSONObjectToBean(JSONArray));
			rongListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		} else {
			initData(0);
//			adapter = new ArticleListAdapter(getActivity(), articleList);
//			rongListView.setAdapter(adapter);

		}
		
		
		rongListView.setOnItemClickListener(new MyOnItemClickListener());
		mPullToRefreshListView.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
		// 设置上拉下拉事件
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						String str = DateUtils.formatDateTime(activity,
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						if (refreshView.isHeaderShown()) {
							// 获取刷新时间，设置刷新时间格式
							mPullToRefreshListView.getLoadingLayoutProxy()
									.setRefreshingLabel("正在刷新");
							mPullToRefreshListView.getLoadingLayoutProxy()
									.setPullLabel("下拉刷新");
							mPullToRefreshListView.getLoadingLayoutProxy()
									.setReleaseLabel("释放开始刷新");
							mPullToRefreshListView.getLoadingLayoutProxy()
									.setLastUpdatedLabel("最后更新时间：" + str);
							// 下拉刷新业务代码						
							mHandler.postDelayed(new Runnable() {
								@Override
								public void run() {
								initData(0);			
								}
							}, 2000);

						} else {
							mPullToRefreshListView.getLoadingLayoutProxy()
									.setRefreshingLabel("正在加载");
							mPullToRefreshListView.getLoadingLayoutProxy()
									.setPullLabel("上拉加载更多");
							mPullToRefreshListView.getLoadingLayoutProxy()
									.setReleaseLabel("释放开始啊加载");
							mPullToRefreshListView.getLoadingLayoutProxy()
									.setLastUpdatedLabel("最后加载时间:" + str);
							// 上拉加载更多业务代码
							
							mHandler.postDelayed(new Runnable() {
								@Override
								public void run() {
									initData(1);
								}
							}, 2000);
							
						}
					}
				});
		rongListView.setOnScrollListener(new MyScrollListener());
		return view;
	}
	/**
	    * listener scroll state
	    */
		private class MyScrollListener implements OnScrollListener{
		@Override
		public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			SnsActivity snsActivity = (SnsActivity) getActivity();
			if(firstVisibleItem>=1){
				snsActivity.productLayout.setBackgroundColor(0xff555555);
				snsActivity.productLayout.setAlpha(1);
	    	}else{
	    		snsActivity.productLayout.setBackgroundColor(0xff555555);
	    		snsActivity.productLayout.setAlpha(0.6f);
	    	}
		}
		@Override
		public void onScrollStateChanged(AbsListView arg0, int scrollState) {
			// TODO Auto-generated method stub
			switch (scrollState) {  
	        case OnScrollListener.SCROLL_STATE_IDLE: //    
	            System.out.println("停止..."); 
	            mPullToRefreshListView.setEnabled(true);
	            break;  
	        case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:  
	        	System.out.println("正在滚动..."); 
	        	mPullToRefreshListView.setEnabled(false);
	            break;  
	        case OnScrollListener.SCROLL_STATE_FLING:    
	            System.out.println("开始滚动...");  

	            break;  
	        }  
			
		}
	  }
	private void snsJSONObjectToBean(JSONArray array) {
		if(array!=null){
		list = new ArrayList<ImageView>();
		listTitle = new ArrayList<ArticleBean>();
		tiltes = new ArrayList<String>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject object;
			try {
				object = array.getJSONObject(i);
				listTitle.add(ParseUtils.parseArticle(object));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = 0; i < listTitle.size(); i++) {
			ImageView imageView = new ImageView(getActivity());
			MyApplication.loadImage(HttpConfig.HTTP_QUERY_URL
					+ listTitle.get(i).getCover(), imageView, null);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setOnClickListener(new MytOnClickListener(listTitle
					.get(i)));
			list.add(imageView);
			tiltes.add(listTitle.get(i).getTitle());
			title.setText(listTitle.get(0).getTitle());
		}
		viewPager.setAdapter(new SnsPictureAdapter(getActivity(), list));
		}
	}
	/**
	 * 对JSONObject数据解析
	 */
	private ArrayList JSONObjectToBean(JSONArray array) {
		try {
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				articleList.add(ParseUtils.parseArticle(object));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return articleList;
	}

	/**
	 * 请求网络接口获取数据
	 */
	private void initData(final int type) {
//		if ((articleList.size()-3)%30==0){
//			new XutilsHttp(activity).clearCache();
//		}
		snsDialog.show();
		if (articleList.size() == 0) {
			client.get(HttpConfig.URL_TITLE_IMAGE,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							JSONObject obj;
							try {
								obj = response.getJSONObject("result");
								boolean flag = response.getBoolean("success");
								if (flag) {
								    array1 = obj.getJSONArray("referralArticle");
									mCache.put("snsjosn", array1);
								Message message	=handler.obtainMessage(); // 通过Handler加载数据
								message.what=ADAPTER_LOADING;
								message.obj=array1;
								handler.sendMessage(message);
								}
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								snsDialog.dismiss();
							}
							snsDialog.dismiss();
						}
						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							// TODO Auto-generated method stub
							super.onFailure(statusCode, headers, throwable,
									errorResponse);
							snsDialog.dismiss();
							Toast.makeText(activity, "请求超时，请重试", Toast.LENGTH_SHORT).show();
							
						}
					});
			
		} else {
			String ArticleLimit;
			totalpage = total / 5 + 1;
			if (type == 0) {
				if (totalpage > currentPage && currentPage == 0) {
					start = currentPage * 5 + 1;
				} else {
					start = (currentPage) * 5 + 1;
				}
			} else {
				if (totalpage > currentPage && currentPage == 0) {
					start = currentPage * 5 + 1;
				} else {
					start = (currentPage) * 5 + 1;
				}
			}
			ArticleLimit = HttpConfig.URL_ARTICLE + "start=" + start+ "&limit=5";
			RequestParams parmas = new RequestParams();
			client.get(ArticleLimit, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					try {
						JSONObject obj = response.getJSONObject("result");
						total = obj.getInt("total");
						if (articleList.size() - list.size() >= total&& type == 1) {
							snsDialog.dismiss();
							Toast.makeText(activity, "没有数据加载", 1).show();
							
						} else {
							boolean flag = response.getBoolean("success");
							if (flag) {
								currentPage++;
								JSONArray array = obj.getJSONArray("article");
								JSONObjectToBean(array);
								
							}
							snsDialog.dismiss();
						}
						// adapter.notifyDataSetChanged();
						handler.obtainMessage(ADAPTER_REFRESH).sendToTarget(); // 通过Handler加载更多刷新

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						snsDialog.dismiss();
					}
					snsDialog.dismiss();
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable,errorResponse);
					snsDialog.dismiss();
					Toast.makeText(activity, "请求超时，请重试", Toast.LENGTH_SHORT).show();							
				}				
			});
			snsDialog.dismiss();
		}

	}

	/** 此方法意思为fragment是否可见 ,可见时候加载数据 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser) {
			// fragment可见时加载数据
			if (articleList != null && articleList.size() != 0) {
				// handler.obtainMessage(SET_ARTICLE).sendToTarget();
				if (rongListView != null) {
					rongListView.setAdapter(adapter);
					// adapter.notifyDataSetChanged();
				}
			} else {
				/*
				 * new Thread(new Runnable() {
				 * 
				 * @Override public void run() { // TODO Auto-generated method
				 * stub try { Thread.sleep(2); } catch (InterruptedException e)
				 * { // // TODO Auto-generated catch block e.printStackTrace();
				 * } handler.obtainMessage(SET_ARTICLE).sendToTarget(); }
				 * }).start();
				 */
			}
		} else {
			// fragment不可见时不执行操作
			if (articleList != null && articleList.size() != 0) {
				articleList.clear();
				// adapter.notifyDataSetChanged();
			}
		}
		// super.setUserVisibleHint(isVisibleToUser);
	}

	public void initImage() {
		list = new ArrayList<ImageView>();
		listTitle = new ArrayList<ArticleBean>();
		tiltes = new ArrayList<String>();
		snsDialog.show();
		tiltes = new ArrayList<String>();
		client.get(HttpConfig.URL_TITLE_IMAGE, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					JSONObject obj = response.getJSONObject("result");
					boolean flag = response.getBoolean("success");
					if (flag) {
						JSONArray array = obj.getJSONArray("topArticle");
						mCache.put("articlejson",array);
						snsJSONObjectToBean(array);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					snsDialog.dismiss();
				}
				snsDialog.dismiss();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				snsDialog.dismiss();
				Toast.makeText(activity, "请求超时，请重试", Toast.LENGTH_SHORT).show();
				
			}
		});
		snsDialog.dismiss();
	}

	public class MytOnClickListener implements OnClickListener {
		Object bean;

		public MytOnClickListener(Object bean) {
			this.bean = bean;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (bean instanceof ArticleBean) {
				Intent intent = new Intent(activity, SnsArticleActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("articleBean", (ArticleBean) bean);
				intent.putExtras(bundle);
				startActivityForResult(intent, DETAILREQUSET);
			}

		}
	}

	public class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (rongListView != null) {
				Intent intent = new Intent(activity, SnsArticleActivity.class);
				ArticleBean articleBean = articleList.get(arg2 - 2);
				Bundle bundle = new Bundle();
				bundle.putSerializable("articleBean", articleBean);
				intent.putExtras(bundle);
				startActivityForResult(intent, DETAILREQUSET);
			}
		}
	}

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
	 * @author zhangpengfei
	 * 
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {
				currentItem = (currentItem + 1) % list.size();
				handler.obtainMessage(PICTURE_VIEW).sendToTarget(); // 通过Handler切换图片
			}
		}
	}

	/**
	 * 详情跳转文章列表重定向
	 * 
	 */

	/*
	 * @Override public void onActivityResult(int requestCode, int resultCode,
	 * Intent data) { // super.onActivityResult(requestCode, resultCode, data);
	 * switch (resultCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK case -1:
	 * articleList.clear(); initImage(); currentPage=0; initData(0);
	 * rongListView.setAdapter(adapter); //adapter.notifyDataSetChanged();
	 * break; default: break; } }
	 */

	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		@Override
		public void onPageScrollStateChanged(final int position) {
			// TODO Auto-generated method stub
//			Log.e("jdjkjkdkj","滑动的状态");
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
//			Log.e("jkdjkdjkkjd","滑动");
			currentItem = arg0;
			title.setText(tiltes.get(arg0));
			oldPosition = arg0;
		}

		@Override
		public void onPageSelected(final int arg0) {
			// TODO Auto-generated method stub
			currentItem = arg0;
			title.setText(tiltes.get(arg0));
			oldPosition = arg0;
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case ADAPTER_LOADING:
				JSONArray array2=(JSONArray) msg.obj;
				articleList=JSONObjectToBean(array2);
				adapter = new ArticleListAdapter(getActivity(),articleList);
				rongListView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				break;
				
			case ADAPTER_REFRESH:
				mPullToRefreshListView.onRefreshComplete();
				break;
			/*
			 * case SET_ARTICLE: // detail_loading.setVisibility(View.GONE); //
			 * mAdapter = new NewsAdapter(activity, newsList); //
			 * mListView.setAdapter(mAdapter); viewPager.setAdapter(new
			 * ImageCoverAdapter(activity, list));
			 * viewPager.setOnPageChangeListener(new MyPageChangeListener());
			 * adapter = new ArticleAllAdapter(activity, articleList); break;
			 */
			case PICTURE_VIEW:
				viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
				break;
			default:
			super.handleMessage(msg);
			}
		}
	};
}
