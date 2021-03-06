package cn.com.ubankers.www.user.service;

import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.widget.ListView;
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
 *已注册的客户的Service
 */
public class RegisteredUsersService {
	private Activity activity;
	private ArrayList<CustomerBean> list;
	private AsyncHttpClient client;
	private ProcessDialog progressDialog;
	private ListView rongListView;

	public RegisteredUsersService(Activity activity,ArrayList<CustomerBean> list,ListView rongListView){
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
	
	public void initData() {
		list =new ArrayList<CustomerBean>();
		if(list.size()>0){
			list.clear();
		}
		progressDialog.show();
		StringEntity entity=null;
		try {	
			JSONObject json = new JSONObject();
			json.put("limit","");
			json.put("start","");
			entity =new StringEntity(json.toString());
		} catch (Exception e1) {
				e1.printStackTrace();
		}
		client.post(activity, HttpConfig.URL_REGISTER_INVESTOR, entity,"application/json",new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					boolean flag = response.getBoolean("success");
					if(flag){
						JSONObject jsonObject = response.getJSONObject("result");
						String errorCode = jsonObject.getString("errorCode");
						if(errorCode.equals("success")){
							String totalCount = jsonObject.getString("totalCount");
							JSONArray jsonArray = jsonObject.getJSONArray("list");
							/**
							 * id//主键
							   nickName//昵称
							   mobile//手机号
							   email//邮箱
							   addTime//创建时间时间戳
							   userType//用户类型 1资本2财富3投资
							   bankAccountNo//银行卡号
							   bankAccountName;//银行卡持卡人姓名（用扩展表的真实姓名）
							   bankAccountStatus;//银行卡号认证情况
							   productRealName;//真实姓名
							   productRealNameStatus;//真实姓名认证情况
							   idcardFrontFileId;//身份证正面文件id
							   idcardBackFileId;//身份证背面面文件id
							   bindId;//投资者绑定财富师的id
							   memo;//投资者绑定财富师备注
							 */
							
							for(int i=0;i<jsonArray.length();i++){
								CustomerBean clientbean=new CustomerBean();
								JSONObject object = jsonArray.getJSONObject(i);
								try {
									clientbean.setId(object.getString("id"));//投资者的id
								} catch (Exception e) {
									// TODO: handle exception
									clientbean.setId("");
								}try {
									clientbean.setAddTime(object.getString("addTime")) ;//创建时间时间戳
								} catch (Exception e) {
									// TODO: handle exception
									clientbean.setAddTime("") ;//创建时间时间戳
								}try {
									clientbean.setUserType(object.getInt("userType"));//用户类型 1资本2财富3投资
								} catch (Exception e) {
									// TODO: handle exception
									clientbean.setUserType(0);//用户类型 1资本2财富3投资
								}try {
									clientbean.setBankAccountNo(object.getString("bankAccountNo"));//银行卡号
								} catch (Exception e) {
									// TODO: handle exception
									clientbean.setBankAccountNo("");//银行卡号
								}try {
									clientbean.setBankAccountName(object.getString("bankAccountName"));//银行卡持卡人姓名（用扩展表的真实姓名）
								} catch (Exception e) {
									// TODO: handle exception
									clientbean.setBankAccountName("");//银行卡持卡人姓名（用扩展表的真实姓名）
								}try {
									clientbean.setBankAccountStatus(object.getString("bankAccountStatus"));//银行卡号认证情况
								} catch (Exception e) {
									// TODO: handle exception
									clientbean.setBankAccountStatus("");//银行卡号认证情况
								}try {
									clientbean.setProductRealName( object.getString("productRealName"));////真实姓名
								} catch (Exception e) {
									// TODO: handle exception
									clientbean.setProductRealName("");////真实姓名
								}try {
									clientbean.setNickName(object.getString("nickName"));//昵称
								} catch (Exception e) {
									// TODO: handle exception
									clientbean.setNickName("");//昵称
								}try {
									clientbean.setMobile(object.getString("mobile"));//手机号
								} catch (Exception e) {
									// TODO: handle exception
									clientbean.setMobile("");//手机号
								}try {
									clientbean.setEmail(object.getString("email"));//邮箱
								} catch (Exception e) {
									// TODO: handle exception
									clientbean.setEmail("");//邮箱
								}try {
									clientbean.setBindId(object.getString("bindId"));//投资者绑定财富师的id
								} catch (Exception e) {
									// TODO: handle exception
									clientbean.setBindId("");//投资者绑定财富师的id
								}try {
									clientbean.setProductRealNameStatus(object.getString("productRealNameStatus"));//真实姓名认证情况
								} catch (Exception e) {
									// TODO: handle exception
									clientbean.setProductRealNameStatus("");//真实姓名认证情况
								}try {
									clientbean.setIdcardFrontFileId(object.getString("idcardFrontFileId"));//身份证正面文件id
								} catch (Exception e) {
									// TODO: handle exception
									clientbean.setIdcardFrontFileId("");//身份证正面文件id
								}try {
									clientbean.setIdcardBackFileId(object.getString("idcardBackFileId"));//身份证背面面文件id
								} catch (Exception e) {
									// TODO: handle exception
									clientbean.setIdcardBackFileId("");//身份证背面面文件id
								}try {
									clientbean.setMemo(object.getString("memo"));
								} catch (Exception e) {
									// TODO: handle exception
									clientbean.setMemo("");
								}
							list.add(clientbean);
							CustomerAdapter adapter = new CustomerAdapter(activity,list,1);
							rongListView.setAdapter(adapter);
							}
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
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}
}
