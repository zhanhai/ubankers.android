package cn.com.ubankers.www.product.controller.frament;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.authentication.model.UserQuestBean;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.http.ParseUtils;
import cn.com.ubankers.www.product.controller.activity.AdPositionIdDetailsActivity;

import com.ubankers.app.product.detail.ProductDetailActivity;
import cn.com.ubankers.www.product.model.PagerBean;
import cn.com.ubankers.www.product.model.ProductDetail;
import cn.com.ubankers.www.product.service.ACache;
import cn.com.ubankers.www.product.service.ProductHttp;
import cn.com.ubankers.www.product.view.ProductListAdapter;
import cn.com.ubankers.www.product.view.ProductPagerAdapter;
import cn.com.ubankers.www.sns.controller.activity.SnsArticleActivity;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.utils.CompleteDialog;
import cn.com.ubankers.www.utils.LoginDialog;
import cn.com.ubankers.www.utils.NetWorking;
import cn.com.ubankers.www.utils.Tools;
import cn.com.ubankers.www.utils.XutilsHttp;
import cn.com.ubankers.www.widget.AutoScrollViewPager;
import cn.com.ubankers.www.widget.ProcessDialog;
import cn.com.ubankers.www.widget.MyListView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MingStartFragment extends Fragment {
	private List<ProductDetail> mianlistview;
	private ProductDetail ProductConstants;
	private UserQuestBean request;
	private Gson gson;
	private Activity activity;
	private MyListView mListView;
	private ProductListAdapter mAdaptermian;
	private int currentItem = 0; // 当前图片的索引号
	private int oldPosition = 0; // 上一次页面的位置
	private String text;
	private ImageView detail_loading;
	public static AutoScrollViewPager mViewPager;
	private ScheduledExecutorService scheduledExecutors;
	private ScheduledExecutorService scheduledExecutor;
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
	private AsyncHttpClient client;
	int[] tag = { 101 };
	String[] strArray = { "1", "4", "5", "7", "10", "11", "12" };
	private ProductHttp ProductHttp;
	private String Fragmentname = "MingStartFragment";
	private ACache mCache;
	private ProcessDialog progressDialog;
	private LoginDialog loginDialog;
	private CompleteDialog completeDialog;
	private Button refactor;
	private View refactorlayout;
	View myView1;
	private LinearLayout ll_view_scolc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (progressDialog == null) {
			progressDialog = ProcessDialog.createDialog(activity, "正在加载中...");
		}
		if (user != null && user.getUserRole() != null&& !user.getUserRole().equals("tourist")) {
			request = new UserQuestBean(tag, strArray, user.getUserRole(),1,100);
		} else {
			request = new UserQuestBean(tag, strArray, role,1,100);
		}
		loginDialog = new LoginDialog(activity,1,1);
		Bundle args = getArguments();
		text = args != null ? args.getString("text") : "";
		 ProductHttp = new ProductHttp(activity, tag, strArray, Fragmentname);
		
		 mCache = ACache.get(activity);
		 if (new NetWorking().isNetworkAvailable(activity) == false) {
				Toast.makeText(activity, "无法连接到网络，请检查网络设置。", 1).show();
				JSONObject object = mCache.getAsJSONObject(Fragmentname);
				mianlistview = ProductHttp.Analytic(object, 2);
				JSONArray array = mCache.getAsJSONArray("MakertFragmentimg");
				try {
					list = ProductHttp.Analyticimghq(array);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.obtainMessage(SET_NEWSLIST).sendToTarget();
			} else{
				dataRequest();
				initImage();
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
            	dataRequest(); 
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
		mViewPager.setOnPageChangeListener(pageListener);
		ll_view_scolc=(LinearLayout) myView.findViewById(R.id.ll_view_scolc);
		mListView.addHeaderView(myView);
		myView1.setVisibility(View.GONE);
//		mListView.addFooterView(myView1,null,false);		
		TextView item_textview = (TextView) view
				.findViewById(R.id.item_textview);
		detail_loading = (ImageView) view.findViewById(R.id.detail_loading);
		item_textview.setText(text);
		loginDialog.dismiss();
        mListView.setOnScrollListener(new MyScrollListener());
		return view;

	}
   /**
    * listener scroll state
    */
	private class MyScrollListener implements OnScrollListener{
		@Override
		public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub

			if(visibleItemCount==1){
				myView1.setVisibility(View.VISIBLE);
				mListView.addFooterView(myView1,null,false);	
				
	    	}
		}

		@Override
		public void onScrollStateChanged(AbsListView arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
		}
	public void dataRequest() {
		
		client = MyApplication.app.getClient(activity);
		StringEntity entity = null;
		int[] tag = { 101 };
		String[] strArray = { "1", "4", "5", "7", "10", "11", "12" };
		if (MyApplication.app.getUser() != null) {
			user = MyApplication.app.getUser();
		}
		if (user != null && user.getUserRole() != null
				&& !user.getUserRole().equals("tourist")) {
			request = new UserQuestBean(tag, strArray, user.getUserRole(),1,100);
		} else {
			request = new UserQuestBean(tag, strArray, role,1,100);
		}		
		try {	
			gson = new Gson();
			gson.toJson(request);
			String jsonRequestParams = gson.toJson(request);
			entity=new StringEntity(jsonRequestParams);
		} catch (Exception e) {
			// TODO Auto-generated catch block\
			e.printStackTrace();
		}
		progressDialog.show();
		client.post(activity, HttpConfig.URL_PRODUCTLIST, entity,
				"application/json", new JsonHttpResponseHandler() {
					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							JSONObject response) {
						JSONObject obj = response;
						mCache.put("MingStartFragment", obj);
						mCache.put("test_key3", "test value",120* ACache.TIME_DAY);
						try {
							mianlistview = new ArrayList<ProductDetail>();
							JSONArray list = obj.getJSONObject("result").getJSONArray("list");
							for (int i = 0; i < list.length(); i++) {
								Product = new ProductDetail();
								JSONObject object = list.getJSONObject(i);
								Product.setProductId(object.optString("id",""));						
								Product.setProductName(object.optString("productName",""));
								Product.setModuleId(object.optString("moduleId",""));					
								Product.setState(object.optInt("state",0));
								Product.setIsHot(object.optInt("isHot", 0));
								Product.setProductTerm(object.optString("productTerm", ""));
								Product.setCountProductRate(object.optString("countProductRate", ""));
								Product.setMinSureBuyPrice(object.optString("minSureBuyPrice",""));
							    Product.setRaisedProcessShow(object.optInt("raisedProcessShow",0));
								Product.setFace(object.optString("face",""));						
								mianlistview.add(Product);								
								handler.obtainMessage(SET_NEWSLIST).sendToTarget();
							}
							progressDialog.dismiss();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						progressDialog.dismiss();
					};

					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable,errorResponse);
						progressDialog.dismiss();
						Toast.makeText(activity, "请求超时，请重试", Toast.LENGTH_SHORT).show();
						
					}
				});
		progressDialog.dismiss();
	}
	public void initImage() {
		client = MyApplication.app.getClient(activity);
		list = new ArrayList<ImageView>();
		pagerList = new ArrayList<PagerBean>();
//		client.setUserAgent(" ronganapp");
		progressDialog.show();
		client.get(HttpConfig.URL_PRODUCT_TITLE_IMAGE,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						try {
							JSONObject obj = response.getJSONObject("result");
							boolean flag = response.getBoolean("success");
							if (flag) {
								JSONObject objet = obj.getJSONObject("data");
								JSONArray array = objet.getJSONArray("images");
								mCache.put("MakertFragmentimg", array);
								XutilsHttp 	Xutil=new XutilsHttp(activity);
								PagerBean pagerBean = new PagerBean();
//								ArrayList<PagerBean>  pagerList = new ArrayList<PagerBean>();
								for (int i = 0; i < array.length(); i++) {
									JSONObject object = array.getJSONObject(i);
									 pagerBean = ParseUtils.parsePctPager(object
											.toString());
									  pagerList.add(pagerBean);
								}
								for (int i = 0; i < pagerList.size(); i++) {
									ImageView imageView = new ImageView(activity);
									imageView.setScaleType(ScaleType.CENTER_CROP);
									Xutil.display(imageView, HttpConfig.HTTP_QUERY_URL+ pagerList.get(i).getImageUrl());
									list.add(imageView);
									ll_view_scolc.addView(Tools.getDot(activity, i));
									imageView.setOnClickListener(new MyOnclickListener(pagerList.get(i)));
									
								}
							}
							progressDialog.dismiss();
						} catch (Exception e) {
							e.printStackTrace();
							progressDialog.dismiss();
						}
						progressDialog.dismiss();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
						progressDialog.dismiss();
						Toast.makeText(activity, "请求超时，请重试", Toast.LENGTH_SHORT).show();
						
					}					
				});
		progressDialog.dismiss();
		}
	public class MyOnclickListener implements OnClickListener{
		private PagerBean pagerBean;
		public MyOnclickListener(PagerBean pagerBean){
			this.pagerBean = pagerBean;
		}
		@Override
		public void onClick(View arg0) {
			if(pagerBean.isProduct()==true&&!pagerBean.getPicUrl().equals("")){//是产品
				Intent intent = new Intent(activity,ProductDetailActivity.class);
				intent.putExtra("productId", pagerBean.getObjId());
				startActivity(intent);
			}else if(pagerBean.isArticle()==true&&!pagerBean.getPicUrl().equals("")){//是文章
				Intent intent = new Intent(activity,SnsArticleActivity.class);
				intent.putExtra("articleBeanId", pagerBean.getObjId());
				startActivity(intent);
			}else if(pagerBean.isOtherDefined()==true&&!pagerBean.getPicUrl().equals("")){//是其他
				Intent intent = new Intent(activity,AdPositionIdDetailsActivity.class);
				intent.putExtra("AdPositionIdUrl", pagerBean.getPicUrl());
				activity.startActivity(intent);
			}
		}
		
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
//		if (isVisibleToUser) {
//			ProductHttp = new ProductHttp(activity, tag, strArray, Fragmentname);
//			mianlistview = ProductHttp.dataRequest();
//			list = ProductHttp.initImage();
//		}
	}
	@Override
	public void onStart() {
		scheduledExecutors = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutors.scheduleAtFixedRate(new ScrollTask(),1, 3,TimeUnit.SECONDS);		
		super.onStart();
	}

	@Override
	public void onStop() {
		// 当Activity不可见的时候停止切换
		scheduledExecutors.shutdown();
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
				if(list!=null){
					mViewPager.setAdapter(new ProductPagerAdapter(activity, list));
				}
				if (mListView != null) {
					mAdaptermian = new ProductListAdapter(activity,
							mianlistview);
					mListView.setAdapter(mAdaptermian);
					mListView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							UserBean userBean = MyApplication.app.getUser();
							if (userBean == null) {
								loginDialog.onLogin();
							}
							 else {
								ProductDetail product = mianlistview.get(arg2 - 1);
								intent = new Intent(activity,
										ProductDetailActivity.class);
								if (intent != null) {
									/*intent.putExtra("productId",product.getProductId());
									intent.putExtra("productName", product.getProductName());
									intent.putExtra("face", product.getFace());*/
									Bundle bundle = new Bundle();
									bundle.putSerializable(ProductDetailActivity.EXTRA_PRODUCT_DETAIL, product);
									intent.putExtras(bundle);
									startActivity(intent);
								}
							/*else if (MyApplication.app.getUser()!= null&& request.getUserRole().equals("tourist")) {
								Intent intent = new Intent(activity,ChooseRoleActivity.class);
								startActivity(intent);
							}*/ 
							}
						}
					});
				}
				progressDialog.dismiss();
				break;
			case PICTURE_VIEW:
				mViewPager.getAdapter().notifyDataSetChanged();
				mViewPager.setCurrentItem(currentItem);// 切换当前显示的图片
				break;
			default:
			}
			super.handleMessage(msg);
		}

		private View View(int addListLayout) {
			// TODO Auto-generated method stub
			return null;
		}
	};
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
			ll_view_scolc.removeAllViews();
			for (int j = 0; j < list.size(); j++) {
				ll_view_scolc.addView(Tools.getDotPager(activity, j,
						position));
			}
		}
	};
}
