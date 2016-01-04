package cn.com.ubankers.www.product.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.authentication.model.UserQuestBean;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.http.ParseUtils;
import cn.com.ubankers.www.product.controller.activity.AdPositionIdDetailsActivity;
import com.ubankers.app.product.detail.ProductDetailActivity;
import cn.com.ubankers.www.product.model.PagerBean;
import cn.com.ubankers.www.product.model.ProductDetail;
import cn.com.ubankers.www.sns.controller.activity.SnsArticleActivity;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.utils.XutilsHttp;
import cn.com.ubankers.www.widget.ProcessDialog;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ProductHttp {

	private Gson gson;
	private String role = "tourist";
	private ArrayList<ImageView> list;
	private ArrayList<PagerBean> pagerList;
	private Activity activity;
	private int[] tag;
	private String[] strArray;
	private UserBean user;
	private UserQuestBean request;
	private AsyncHttpClient client;
	private static final String Image = "/width/720";
	public ProcessDialog progressDialog;
	private String frangmename;
	public ACache	mCache;
	private List<ProductDetail> mianlistview = new ArrayList<ProductDetail>();
	private Fragment fragment;

	public ProductHttp(Activity activity, int[] tag, String[] strArray,String fragmentname) {
		this.activity = activity;
		this.tag = tag;
		this.strArray = strArray;
		this.frangmename=fragmentname;
		mCache = ACache.get(activity);
		if (progressDialog == null){
			progressDialog = ProcessDialog.createDialog(activity, "正在加载中...");
			}
	}

	public List<ProductDetail> dataRequest() {
		client = MyApplication.app.getClient(activity);
		StringEntity entity = null;
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
			String jsonRequestParams = gson.toJson(request);
			entity = new StringEntity(jsonRequestParams.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.setUserAgent(" ronganapp");
		progressDialog.show();
		client.post(activity, HttpConfig.URL_PRODUCTLIST, entity,
				"application/json", new JsonHttpResponseHandler() {
					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							JSONObject response) {
						JSONObject obj = response;
						mCache.put(frangmename, obj);
						mCache.put("test_key3", "test value",120* ACache.TIME_DAY);
					 	Analytic(obj,1);
					 	progressDialog.dismiss();
					};

					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						progressDialog.dismiss();
						Toast.makeText(activity, "请求超时，请重试", Toast.LENGTH_SHORT).show();
						
					}
					
				});
		progressDialog.dismiss();
		return mianlistview;
	}

	public List<ImageView> initImage() {
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
								list=(ArrayList<ImageView>) Analyticimg(array);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						progressDialog.dismiss();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable,errorResponse);
						progressDialog.dismiss();
						Toast.makeText(activity, "请求超时，请重试", Toast.LENGTH_SHORT).show();
						
					}
				});
		progressDialog.dismiss();
		return list;

	}
	
	//解析淘金子list字段
	public List<ProductDetail> Analytic(JSONObject obj ,int j){
		if(j==2){
			List<ProductDetail> mianlistview = new ArrayList<ProductDetail>();
		}
		try {
			if(obj!=null){
				JSONArray list = obj.getJSONObject("result").getJSONArray("list");

				for (int i = 0; i < list.length(); i++) {
					ProductDetail Product = new ProductDetail();
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
					
				 MyApplication.app.setProduct(Product);
					mianlistview.add(Product);
				}

			}
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mianlistview;
	}

	
	public  List  Analyticimg(JSONArray array) throws JSONException{
		XutilsHttp 	Xutil=new XutilsHttp(activity);
		PagerBean pagerBean = new PagerBean();
		ArrayList<PagerBean>  pagerList = new ArrayList<PagerBean>();
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
			imageView.setOnClickListener(new MyOnclickListener(pagerList.get(i)));
			
		}
		return list;
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
				intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, pagerBean.getObjId());
				activity.startActivity(intent);
			}else if(pagerBean.isArticle()==true&&!pagerBean.getPicUrl().equals("")){//是文章
				Intent intent = new Intent(activity,SnsArticleActivity.class);
				intent.putExtra("articleBeanId", pagerBean.getObjId());
				activity.startActivity(intent);
			}else if(pagerBean.isOtherDefined()==true&&!pagerBean.getPicUrl().equals("")){//是其他
				Intent intent = new Intent(activity,AdPositionIdDetailsActivity.class);
				intent.putExtra("AdPositionIdUrl", pagerBean.getPicUrl());
				activity.startActivity(intent);
			}
		}
		
	}
	//图片缓存读取图片
	public  List  Analyticimghq(JSONArray array ) throws JSONException{
		XutilsHttp 	Xutil=new XutilsHttp(activity);
		PagerBean pagerBean = new PagerBean();
		ArrayList<ImageView> list = new ArrayList<ImageView>();
		ArrayList<PagerBean>  pagerList = new ArrayList<PagerBean>();
		if(array!=null){
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
			}
		}
		
		return list;
	}
}
