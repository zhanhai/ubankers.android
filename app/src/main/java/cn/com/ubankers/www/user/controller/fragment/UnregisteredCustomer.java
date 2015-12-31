package cn.com.ubankers.www.user.controller.fragment;

import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.CustomerBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.widget.MyDialog;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class UnregisteredCustomer extends Fragment{
	private ListView rongListView;
	private Activity activity;
	private AsyncHttpClient client;
	private MyDialog progressDialog;
	private UserBean userBean;
	private ArrayList<CustomerBean> list;
	
	public void onAttach(Activity activity) {
		this.activity = activity;
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (progressDialog == null) {
			progressDialog = MyDialog.createDialog(activity,"正在加载中...");
		}else{
			progressDialog = MyDialog.createDialog(activity,"正在加载中...");
		}
		client = MyApplication.app.getClient(activity);
		 weiinitData();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = LayoutInflater.from(activity).inflate(R.layout.investor_order_list, container, false);
		rongListView=(ListView) view.findViewById(R.id.lv_investor);
		return view;	
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private void weiinitData() {
		progressDialog.show();
		list =new ArrayList<CustomerBean>();
		StringEntity entity=null;
		try {	
			JSONObject json = new JSONObject();
			json.put("cfmpId",userBean.getUserId());
			entity =new StringEntity(json.toString());
		} catch (Exception e1) {
				e1.printStackTrace();
		}
		client.post(activity,HttpConfig.URL_B_INVESTOR, entity,"application/json",new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					boolean flag = response.getBoolean("success");
				    if(flag){
							JSONObject jsonObject = response.getJSONObject("result");
							String totalCount = jsonObject.getString("totalCount");//总条数
							JSONArray jsonArray = jsonObject.getJSONArray("list");
							for(int i=0;i<jsonArray.length();i++){
								/**
								 *  "start": null,
					                "limit": null,
					                "unRegistedUserId": 62120,
					                "realName": "asdfad",
					                "idcard": "545645665151321564",
					                "mobile": "13564544564",
					                "delFlag": 0,
					                "addTime": 1437445802585,
					                "cfmpId": null
								 */
								CustomerBean investorBBena = new CustomerBean();
								JSONObject object = jsonArray.getJSONObject(i);
								int start = object.getInt("start");
								int limit = object.getInt("limit");
								String unRegistedUserId = object.getString("unRegistedUserId");
								String realName = object.getString("realName");
								String idcard = object.getString("idcard");
								String mobile = object.getString("mobile");
								int delFlag = object.getInt("delFlag");
								String addTime = object.getString("addTime");
								investorBBena.setAddTime(addTime);
								investorBBena.setDelFlag(delFlag);
								investorBBena.setIdcard(idcard);
								investorBBena.setLimit(limit);
								investorBBena.setMobile(mobile);
								investorBBena.setRealName(realName);
								investorBBena.setStart(start);
								investorBBena.setUnRegistedUserId(unRegistedUserId);
								list.add(investorBBena);
						    }
						}
				        progressDialog.dismiss();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
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
		progressDialog.dismiss();
	}
}
