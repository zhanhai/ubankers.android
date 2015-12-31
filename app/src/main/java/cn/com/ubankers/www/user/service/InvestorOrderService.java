package cn.com.ubankers.www.user.service;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.BuyRecordBean;
import cn.com.ubankers.www.user.model.InvestorOrderBean;
import cn.com.ubankers.www.user.view.InvestorBuyAdapter;
import cn.com.ubankers.www.user.view.InvestorOrderAdapter;
import cn.com.ubankers.www.utils.Tools;
import cn.com.ubankers.www.widget.MyDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
/**
 * 
 * @author 廖准华
 *投资记录的service
 */
public class InvestorOrderService {
	private AsyncHttpClient client;
	private MyDialog progressDialog;
	private Activity activity;
	private InvestorOrderAdapter adapter;
	private InvestorBuyAdapter invesadapter;
	private String userId;
	private ListView rongListView;
	private List<InvestorOrderBean> list;
	private List<BuyRecordBean>  Purchaselist= new ArrayList<BuyRecordBean>();
	
	public InvestorOrderService(Activity activity,String userId,ListView rongListView,List<InvestorOrderBean> list){
		this.activity = activity;
		this.userId = userId;
		this.list = list;
		this.rongListView = rongListView;
		if (progressDialog == null) {
			progressDialog = MyDialog.createDialog(activity,"正在加载中...");
		}else{
			progressDialog = MyDialog.createDialog(activity,"正在加载中...");
		}
		client = MyApplication.app.getClient(activity);
	}
	
	/**
	 * 获取投资记录的接口
	 */
	public void initData() {
		if(list.size()>0){
			list.clear();
		}
		progressDialog.show();
		StringEntity entity=null;
		try {	
			JSONObject json = new JSONObject();
			json.put("limit","");
			json.put("start","");
			json.put("userId",userId);
			entity =new StringEntity(json.toString());
		} catch (Exception e1) {
				e1.printStackTrace();
		}
		client.post(activity, HttpConfig.URL_INVESTORORDER, entity,"application/json", new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					boolean flag = response.getBoolean("success");
					if(flag){
						JSONObject jsonObject = response.getJSONObject("result");
						String errorCode = jsonObject.getString("errorCode");
						int totalCount = jsonObject.getInt("totalCount");
						if(errorCode.equals("success")){
							JSONArray jsonArray = jsonObject.getJSONArray("list");
							/**
							 * "begin": 0,
                				"cfmpId": 33565,
                				"cfmpName": "测试用户1",
                				"dir": "",
                				"end": 1000,
                				"endContactTime": 0,
                				"endContactTimeShow": "",
                				"examineState": 1,
                				"examineStateShow": "",
                				"isNotContact": 0,
                				"isNotContactShow": "",
                				"limit": 0,
                				"productId": 100563,
                				"productIds": "",
                				"productName": "泰岳梧桐在线教育产业新三板与并购基金",
                				"remarks": "",
                				"reserveId": 103273,
                				"reserveMobile": "15121148971",
                				"reserveName": "投资者1",
                				"reservePerson": 33565,
                				"reserveQuota": 1000000,
                				"reserveRole": 2,
                				"reserveTime": 1436421568996,
                				"reserveTimeShow": "2015-07-09",
                				"reserveVoucherId": "",
                				"serviceName": "",
                				"serviceStaff": "",
                				"sortName": "",
                				"start": 0,
                				"totalAmount": "",
                				"totalProNum": "",
                				"userId": 33564
							 */
							for(int i=0;i<jsonArray.length();i++){
								JSONObject object = jsonArray.getJSONObject(i);
								String productName = object.getString("productName");//产品名称
								String productId = object.getString("productId");//产品id
								int examineState = object.getInt("examineState");//预约状态
								String reserveTimeShow = object.getString("reserveTimeShow");//预约时间
								String reserveQuota = object.getString("reserveQuota");//预约金额
								String reserveId = object.getString("reserveId");//预约id
								String reserveName = object.getString("reserveName");//预约名称
								reserveQuota=Tools.InterceptTo(reserveQuota);
								InvestorOrderBean investorOrderBean = new InvestorOrderBean();
								investorOrderBean.setExamineState(examineState);
								investorOrderBean.setProductName(productName);
								investorOrderBean.setReserveQuota(reserveQuota);
								investorOrderBean.setReserveTimeShow(reserveTimeShow);
								investorOrderBean.setReserveId(reserveId);
								investorOrderBean.setProductId(productId);
								investorOrderBean.setReserveName(reserveName);
								list.add(investorOrderBean);
							}
							adapter = new InvestorOrderAdapter(activity, list, new MyCounterOrderListener());
							rongListView.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						}
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				progressDialog.dismiss();
				super.onSuccess(statusCode, headers, response);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				Log.e("jkdkjkdjkd","失败啦！！！！！");
				super.onFailure(statusCode, headers, responseString, throwable);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(activity, "请求超时，请重试", Toast.LENGTH_SHORT).show();

			}
		});
		
	}
	public class MyCounterOrderListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			InvestorOrderBean investorOrderBean =(InvestorOrderBean) arg0.getTag();
			final String reserveId = investorOrderBean.getReserveId();
			final AlertDialog dialog=new AlertDialog.Builder(activity).create();
			final View view = View.inflate(activity, R.layout.dialog_cancel_order, null);
			view.findViewById(R.id.cancel_bt).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					dialog.dismiss();
				}
			});
			view.findViewById(R.id.ok_bt).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(reserveId!=null&&!reserveId.equals("")){
						CountermandOrder(reserveId);
						dialog.dismiss();
					}	
				}
			});
			dialog.setView(view); 
			dialog.show();
		}	
	}
	//取消预约的接口
	private void CountermandOrder(String reserveId){
				StringEntity entity=null;
				try {	
					JSONObject json = new JSONObject();
					json.put("reserveId",reserveId);
					entity =new StringEntity(json.toString(),"utf-8");
				} catch (Exception e1) {
						e1.printStackTrace();
				}
				client.post(activity, HttpConfig.URL_INVESTORORDER_COUNTERMAND, entity, "application/json", new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(int statusCode,
							Header[] headers, JSONObject response) {
						try {
							boolean flag = response.getBoolean("success");
							if(flag){
								JSONObject jsonObject = response.getJSONObject("result");
								String errorCode = jsonObject.getString("errorCode");
								boolean info = jsonObject.getBoolean("info");
								if(errorCode.equals("success")&&info==true){
								 initData();
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(statusCode, headers, response);
					}
					@Override
					public void onFailure(int statusCode,
							Header[] headers,
							String responseString,
							Throwable throwable) {
						super.onFailure(statusCode, headers, responseString, throwable);
					}
					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable, errorResponse);
						Toast.makeText(activity, "请求超时，请重试", Toast.LENGTH_SHORT).show();
					}
				});
				}
	/**
	 * 获取投资记录已购买的接口
	 */
public void InvestmentBuying(String reserveId)	{
	
  if(list.size()>0){
		list.clear();
	}
	progressDialog.show();
	StringEntity entity1=null;
	try {	
		JSONObject json = new JSONObject();
		json.put("limit","1");
		json.put("start","1000");
		json.put("investorId",reserveId);
		entity1 =new StringEntity(json.toString());
	} catch (Exception e1) {
			e1.printStackTrace();
	}
	client.post(activity,HttpConfig.URL_INVESTORS_BUY_PRODUCT,entity1,"application/json", new JsonHttpResponseHandler(){
		public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
			try {
				boolean flag = response.getBoolean("success");
				if(flag){
					JSONObject jsonObject = response.getJSONObject("result");
					String errorCode = jsonObject.getString("errorCode");
					int totalCount = jsonObject.getInt("totalCount");
					if(errorCode.equals("success")){
						JSONArray jsonArray = jsonObject.getJSONArray("list");
						for(int i=0;i<jsonArray.length();i++){
							JSONObject object = jsonArray.getJSONObject(i);
							BuyRecordBean buyrecordBean=new BuyRecordBean();		
							buyrecordBean.setProductName(object.getString("productName")==null?"":object.getString("productName"));//产品名称
							buyrecordBean.setExpiryDateForInterest(object.getLong("expiryDateForInterest"));//到期日
							buyrecordBean.setPurchaseDate(object.getLong("purchaseDate"));//认购日
							buyrecordBean.setPurchaseAmount(object.getString("purchaseAmount"));//购买金额
							buyrecordBean.setValueDate(object.getLong("valueDate"));//起息日
							buyrecordBean.setState(object.getInt("state"));//支付状态
							Purchaselist.add(buyrecordBean);	
						}
						invesadapter=new InvestorBuyAdapter(activity, Purchaselist);
						rongListView.setAdapter(invesadapter);
					}
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			progressDialog.dismiss();
			super.onSuccess(statusCode, headers, response);
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			Log.e("jkdkjkdjkd","失败啦！！！！！");
			super.onFailure(statusCode, headers, responseString, throwable);
		}
		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			super.onFailure(statusCode, headers, throwable, errorResponse);
			Toast.makeText(activity, "请求超时，请重试", Toast.LENGTH_SHORT).show();
			progressDialog.dismiss();
		}
	});
	
}
}
