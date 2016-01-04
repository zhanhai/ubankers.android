package cn.com.ubankers.www.user.service;

import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.widget.ListView;
import android.widget.Toast;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.CustomerBean;
import cn.com.ubankers.www.user.view.CustomerAdapter;
import cn.com.ubankers.www.widget.ProcessDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @author 廖准华
 *未注册客户的service
 */
public class UnRegisteredUsersService {
	private String userId;
	private Activity activity;
	private ArrayList<CustomerBean> list;
	private ListView rongListView;
	private AsyncHttpClient client;
	private ProcessDialog progressDialog;

	public UnRegisteredUsersService(String userId,Activity activity,ArrayList<CustomerBean> list,ListView rongListView){
		this.userId = userId;
		this.activity = activity;
		this.list = list;
		this.rongListView = rongListView;
		if (progressDialog == null) {
			progressDialog = ProcessDialog.createDialog(activity, "正在加载中...");
		}else{
			progressDialog = ProcessDialog.createDialog(activity, "正在加载中...");
		}
		client = MyApplication.app.getClient(activity);
	}
	
	public void weiinitData() {
		progressDialog.show();
		list =new ArrayList<CustomerBean>();
		if(list.size()>0){
			list.clear();
		}
		StringEntity entity=null;
		try {	
			JSONObject json = new JSONObject();
			json.put("cfmpId",userId);
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
								 * [{"amount":24000000,
								 * "idcard":"330382198801114545"
								 * ,"realName":"b类投资者3",
								 * "count":15,
								 * "unRegistedUserId":108772,
								 * "mobile":"18721746224"}]
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
								
								 investorBBena.setAmount(object.getString("amount")==null?"":object.getString("amount")) ;
								 investorBBena.setMobile(object.getString("idcard")==null?"":object.getString("idcard")); 
								 investorBBena.setNickName(object.getString("realName")==null?"":object.getString("realName"));								 
//								int start = object.getInt("start");
//								int limit = object.getInt("limit");
//								String unRegistedUserId = object.getString("unRegistedUserId");
//								String realName = object.getString("realName");
//								String idcard = object.getString("idcard");
//								String mobile = object.getString("mobile");
//								int delFlag = object.getInt("delFlag");
//								String addTime = object.getString("addTime");
//								investorBBena.setAddTime(addTime);
//								investorBBena.setDelFlag(delFlag);
//								investorBBena.setIdcard(idcard);
//								investorBBena.setLimit(limit);
//								investorBBena.setMobile(mobile);
//								investorBBena.setRealName(realName);
//								investorBBena.setStart(start);
//								investorBBena.setUnRegistedUserId(unRegistedUserId);
								 
								list.add(investorBBena);
						    }
							CustomerAdapter adapter = new CustomerAdapter(activity,list,2);
							rongListView.setAdapter(adapter);
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
	}
}
