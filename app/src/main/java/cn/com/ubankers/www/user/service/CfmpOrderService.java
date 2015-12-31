package cn.com.ubankers.www.user.service;

import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.InvestorOrderBean;
import cn.com.ubankers.www.user.view.CfmpOrderAdapter;
import cn.com.ubankers.www.utils.Tools;
import cn.com.ubankers.www.widget.MyDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
/**
 * 
 * @author 廖准华
 *已确定预约的service
 */
public class CfmpOrderService {
	private Activity activity;
	private String userId;
	private ListView rongListView;
	private ArrayList<InvestorOrderBean> list;
	private CfmpOrderAdapter adapter;
	private  AsyncHttpClient client;
	private MyDialog progressDialog;
	private  InvestorOrderBean investorOrderBean;
	public CfmpOrderService(Activity activity,String userId,ListView rongListView,ArrayList<InvestorOrderBean> list){
		this.activity = activity;
		this.userId = userId;
		this.rongListView = rongListView;
		this.list = list; 
		if (progressDialog == null) {
			progressDialog = MyDialog.createDialog(activity,"正在加载中...");
		}else{
			progressDialog = MyDialog.createDialog(activity,"正在加载中...");
		}
		client = MyApplication.app.getClient(activity);
	}
	public  void initData() {
		if(list.size()>0){
			list.clear();
		}
		progressDialog.show();
		StringEntity entity=null;
		try {
            JSONObject json = new JSONObject();
			json.put("cfmpId",userId);
			json.put("limit","");
			json.put("start","");
			entity =new StringEntity(json.toString());
		} catch (Exception e1) {
				e1.printStackTrace();
		}
		client.post(activity,HttpConfig.URL_INVESTOR_ORDER, entity,"application/json", new JsonHttpResponseHandler(){
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
							 *  "begin": 0,
				                "cfmpId": 0,
				                "cfmpName": "",
				                "dir": "",
				                "end": 1000,
				                "endContactTime": 0,
				                "endContactTimeShow": "",
				                "examineState": 0,
				                "examineStateShow": "",
				                "isNotContact": 0,
				                "isNotContactShow": "",
				                "limit": 0,
				                "productId": 270528,
				                "productIds": "",
				                "productName": "首泓投资-首创美国投资基金一号",
				                "remarks": "",
				                "reserveId": 0,
				                "reserveMobile": "",
				                "reserveName": "",
				                "reservePerson": 0,
				                "reserveQuota": 0,
				                "reserveRole": 0,
				                "reserveTime": 0,
				                "reserveTimeShow": "",
				                "reserveVoucherId": "",
				                "serviceName": "",
				                "serviceStaff": "",
				                "sortName": "",
				                "start": 0,
				                "totalAmount": "1000000",
				                "totalProNum": "1",
				                "userId": 0
							 */
							for(int i=0;i<jsonArray.length();i++){
								JSONObject object = jsonArray.getJSONObject(i);
								String productName = object.getString("productName");//产品名称
								String productId = object.getString("productId");//产品id
//								int examineState = object.getInt("examineState");//预约状态
								String reserveTimeShow = object.getString("reserveTimeShow");//预约时间
								String reserveQuota = object.getString("reserveQuota");//预约金额
								String reserveId = object.getString("reserveId");//预约id
								String reserveName = object.getString("reserveName");//预约名称
								String totalAmount = object.getString("totalAmount");//总金额
								String totalProNum = object.getString("totalProNum");//总记录
								totalAmount=Tools.InterceptTo(totalAmount);
								investorOrderBean = new InvestorOrderBean();
//								investorOrderBean.setExamineState(examineState);
								investorOrderBean.setProductName(productName);
								investorOrderBean.setReserveQuota(reserveQuota);
								investorOrderBean.setReserveTimeShow(reserveTimeShow);
								investorOrderBean.setReserveId(reserveId);
								investorOrderBean.setProductId(productId);
								investorOrderBean.setReserveName(reserveName);
								investorOrderBean.setTotalAmount(totalAmount);
								investorOrderBean.setTotalProNum(totalProNum);
								list.add(investorOrderBean);
							}
							adapter = new CfmpOrderAdapter(activity, list);
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
				progressDialog.dismiss();
			}
		});
		
	}
}
