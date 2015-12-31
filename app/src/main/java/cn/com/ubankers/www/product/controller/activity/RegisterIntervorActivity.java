package cn.com.ubankers.www.product.controller.activity;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.CustomerBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.view.GroupinfoAdapter;
import cn.com.ubankers.www.utils.BaseUtil;
import cn.com.ubankers.www.utils.QuickAlphabeticBar;
import cn.com.ubankers.www.utils.WebmailContactComparator;
import cn.com.ubankers.www.widget.MyDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.ubankers.app.product.detail.ProductDetailActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class RegisterIntervorActivity extends Activity implements TextWatcher{
	private UserBean userBean;
	private AsyncHttpClient client;
	private CustomerBean clientbean;
	private ArrayList<CustomerBean> list = new ArrayList<CustomerBean>();
	private ArrayList<CustomerBean> list_s = new ArrayList<CustomerBean>();
	private MyDialog progressDialog;
	private QuickAlphabeticBar alpha;
	private EditText edtFindContact;
	private LinearLayout title_bar_back_btn;
	private ListView list_intervor;
	private GroupinfoAdapter adapter;
	private int type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_intervor_layout);
		if (progressDialog == null) {
			progressDialog = MyDialog.createDialog(RegisterIntervorActivity.this,"正在加载中...");
		}
		if(MyApplication.app.getUser()!=null){
			userBean = MyApplication.app.getUser();
		}
		client = MyApplication.app.getClient(this);
		MyApplication.app.addActivity(this);
		Intent intent = getIntent();
	    type = intent.getIntExtra("type", 0);
		initView();
		if(type==1){//非B类投资者
			getInvestor();
		}else if(type==2){//B类投资者
			getInvestorB();
		}
	}
	 private void initView() {
		 edtFindContact = (EditText) findViewById(R.id.edtFindContact);
		 title_bar_back_btn = (LinearLayout) findViewById(R.id.title_bar_back_btn);
		 title_bar_back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		list_intervor = (ListView) findViewById(R.id.list_contacts);
		list_intervor.setDivider(null);
		alpha = (QuickAlphabeticBar) findViewById(R.id.fast_scroller);
        alpha.setVisibility(View.GONE);
		
	}
	 @Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;

			default:
				break;
			}
			return super.onOptionsItemSelected(item);
		}
		@Override
		public void onBackPressed() {
			finish();
			super.onBackPressed();
		}
		/**
		 * 查看某产品绑定财富师可预约的投资者(财富师操作)
		 */
		private void getInvestor() {
			StringEntity entity=null;
			try {	
				JSONObject json = new JSONObject();
				json.put("cfmpId",userBean.getUserId());
				entity =new StringEntity(json.toString());
			} catch (Exception e1) {
					e1.printStackTrace();
			}
			client.post(getApplicationContext(), HttpConfig.URL_INVESTOR, entity,"application/json",new JsonHttpResponseHandler(){
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
									clientbean=new CustomerBean();
									JSONObject object = jsonArray.getJSONObject(i);
									try {
										clientbean.setUserFaceFileId(object.getString("userFaceFileId"));//获取头像的id
									} catch (Exception e) {
										// TODO: handle exception
										clientbean.setUserFaceFileId("");
									}
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
										clientbean.setProductRealName(object.getString("productRealName"));////真实姓名
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
								}
								// 搜索联系人监听
							    edtFindContact.addTextChangedListener(RegisterIntervorActivity.this);
								if (list != null && list.size() > 0) {
									Collections.sort(list, new WebmailContactComparator());
									setAdapter(list);
								}
							}
						}
						progressDialog.dismiss();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						// 搜索联系人监听
					    edtFindContact.addTextChangedListener(RegisterIntervorActivity.this);
						if (list != null && list.size() > 0) {
							Collections.sort(list, new WebmailContactComparator());
							setAdapter(list);
						}
					}
//					super.onSuccess(statusCode, headers, response);
				}
				@Override
				public void onFailure(int statusCode, Header[] headers,
						String responseString, Throwable throwable) {
				    Log.e("dkjdjkdjkjkd","失败啦！！！！");
					super.onFailure(statusCode, headers, responseString, throwable);
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable, errorResponse);
					Toast.makeText(RegisterIntervorActivity.this, "请求超时，请重试", Toast.LENGTH_SHORT).show();
					
				}
			});
		}
		//财富师获取B类投资者列表
		private void getInvestorB(){
			progressDialog.show();
			StringEntity entity=null;
			try {	
				JSONObject json = new JSONObject();
				json.put("cfmpId",userBean.getUserId());
				entity =new StringEntity(json.toString());
			} catch (Exception e1) {
					e1.printStackTrace();
			}
			client.post(getApplicationContext(),HttpConfig.URL_B_INVESTOR, entity, "application/json",new JsonHttpResponseHandler(){
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
							// 搜索联系人监听
						    edtFindContact.addTextChangedListener(RegisterIntervorActivity.this);
							if (list != null && list.size() > 0) {
								Collections.sort(list, new WebmailContactComparator());
								setAdapter(list);
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
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable, errorResponse);
					Toast.makeText(RegisterIntervorActivity.this, "请求超时，请重试", Toast.LENGTH_SHORT).show();
				}
     		});
		}
		private void setAdapter(final ArrayList<CustomerBean> list) {
			adapter = new GroupinfoAdapter(this, list,type);
			list_intervor.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			alpha.init(RegisterIntervorActivity.this);
			alpha.setListView(list_intervor);
			ViewTreeObserver vto2 = alpha.getViewTreeObserver();
			vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					alpha.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					alpha.setHight(alpha.getHeight());
				}
			});
            alpha.setVisibility(View.GONE);
			list_intervor.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					CustomerBean clientBean=null;
					if(edtFindContact.getText().length()==0){
						 clientBean = list.get(position);
					}else{
						 clientBean = list_s.get(position);
					}										
					if(type==1){//非B类投资者
						Intent intent = new Intent(getApplicationContext(),ProductDetailActivity.class);
					    if(!clientBean.getNickName().equals("")){
							intent.putExtra(ProductDetailActivity.KEY_CLIENT_NAME, clientBean.getNickName());
						}
						intent.putExtra(ProductDetailActivity.KEY_CLIENT_ID, clientBean.getId());
						intent.putExtra(ProductDetailActivity.KEY_CLIENT_MOBILE, clientBean.getMobile());
						intent.putExtra(ProductDetailActivity.KEY_CLIENT_TYPE, 1);
						setResult(200, intent);
						finish();
					}else if(type==2){//B类投资者
						Intent intent = new Intent(getApplicationContext(),ProductDetailActivity.class);
						intent.putExtra(ProductDetailActivity.KEY_CLIENT_NAME, clientBean.getRealName());
						intent.putExtra(ProductDetailActivity.KEY_CLIENT_MOBILE, clientBean.getMobile());
						intent.putExtra(ProductDetailActivity.KEY_CLIENT_TYPE, 2);
						setResult(200,intent);
						finish();
					}
				}
			});
		}
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onTextChanged(CharSequence s, int arg1, int arg2,
				int arg3) {
			if (list != null && list.size() > 0) {
				if (TextUtils.isEmpty(s)) {
					adapter.refresh(list);
					list_s.clear();
				} else {
					search(s.toString());
				}
			}
			
		}
		/**
		 * 按号码-拼音搜索联系人
		 * 
		 * @param str
		 */

		public void search(String str) {
			list_s.clear();

			for (CustomerBean cb : list) {
				if(type==1){
						if((cb.getNickName().indexOf(str) >= 0)
								|| (cb.getNickName().indexOf(str) >= 0)
								|| (BaseUtil.getPingYin(cb.getNickName()).toLowerCase()
										.indexOf(str.toLowerCase()) >= 0)){
							list_s.add(cb);
						}
				}else if(type==2){
					if ((cb.getRealName().indexOf(str) >= 0)
							|| (cb.getRealName().indexOf(str) >= 0)
							|| (BaseUtil.getPingYin(cb.getRealName()).toLowerCase()
									.indexOf(str.toLowerCase()) >= 0)) {
						list_s.add(cb);
					}
				}
			}
			adapter.refresh(list_s);
		}
}
