package cn.com.ubankers.www.user.service;

import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.http.ParseUtils;
import cn.com.ubankers.www.user.controller.activity.InverstorToCfmpActivity;
import cn.com.ubankers.www.user.model.AddCfmpBean;
import cn.com.ubankers.www.user.model.BindCfmpBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.view.BindCfmpAdapter;
import cn.com.ubankers.www.user.view.CfmpAdapter;
import cn.com.ubankers.www.user.view.QuerencfmpAdapter;
import cn.com.ubankers.www.widget.MyDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 
 * @author 廖准华 我的财富师的service
 */
public class InverstorToCfmpService {
	private ArrayList<BindCfmpBean> list = new ArrayList<BindCfmpBean>();
	private Context context;
	private AsyncHttpClient client;
	private String investorId;
	private AddCfmpBean addCfmpBean;
	private UserBean userBean;
	private ArrayList<BindCfmpBean> list_bound = new ArrayList<BindCfmpBean>();
	private ArrayList<BindCfmpBean> list_cfmp = new ArrayList<BindCfmpBean>();
	private MyDialog progressDialog;

	public InverstorToCfmpService(Context context) {
		this.context = context;
		client = MyApplication.app.getClient(context);
		if (MyApplication.app.getUser() != null) {
			userBean = MyApplication.app.getUser();
		}
		if (progressDialog == null) {
			progressDialog = MyDialog.createDialog(context,"正在加载中...");
		} else {
			progressDialog = MyDialog.createDialog(context,"正在加载中...");
		}
	}

	// 获取绑定的财富师
	public void initData() {
		progressDialog.show();
		if (list.size() > 0) {
			list.clear();
		}
		client.get(HttpConfig.URL_CFMPPICTURE, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					if (commonJSON(response)) {
						if (!response.getJSONObject("result").isNull("info")) {
							JSONObject object = response.getJSONObject("result").getJSONObject("info");
							list.add(ParseUtils.parseBindCfmp(object.toString()));
							CfmpAdapter adapter = new CfmpAdapter(context, list);
							((InverstorToCfmpActivity) context).listView1.setAdapter(adapter);
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
				Log.e("jdjdjjdjd", "失败啦！！！！！");
				super.onFailure(statusCode, headers, responseString, throwable);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
				progressDialog.dismiss();
			}
		});
	}

	// 投资者查询财富师
	public void selectCfmp(String mobile) {
		if(list_bound.size()>0){
			list_bound.clear();
		}
//		progressDialog.show();
		client.get(HttpConfig.URL_SELECTCFMP + mobile,new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
						try {
							if (commonJSON(response)) {
								if (!response.getJSONObject("result").isNull("info")) {
									JSONObject object = response.getJSONObject("result").getJSONObject("info");
									{
										list_bound.add(ParseUtils.parseBindCfmp(object.toString()));
									}
								}
								BindCfmpAdapter adapter = new BindCfmpAdapter(context, list_bound, userBean.getUserId(),new MyCountermandOrderListener());
								((InverstorToCfmpActivity) context).listView1.setAdapter(adapter);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//						progressDialog.dismiss();
					}
					
					@Override
					public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
						
						if(statusCode==500){
							try {
								boolean flag = errorResponse.getBoolean("success");
								if(flag==false){
									Toast.makeText(context, "没有搜索到对应财富师", Toast.LENGTH_SHORT).show();
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
						}else {
							Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	public class MyCountermandOrderListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			BindCfmpBean cfmpBoundBean = (BindCfmpBean) view.getTag();
			if (cfmpBoundBean.getProductRealNameStatus().equals("1")) {// 认证通过审核
				((InverstorToCfmpActivity) context).listView1.setVisibility(View.VISIBLE);
				boundList(cfmpBoundBean.getId());
				((InverstorToCfmpActivity) context).bound_cfmp.setVisibility(View.GONE);
			} else if (cfmpBoundBean.getProductRealNameStatus().equals("")) {
				final AlertDialog dialog = new AlertDialog.Builder(context).create();
				final View view1 = View.inflate(context,R.layout.unbond_cfmp_layout, null);
				view1.findViewById(R.id.cfmp_okay).setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								dialog.dismiss();
							}
						});
				dialog.setView(view1);
				dialog.show();
			} else if (cfmpBoundBean.getProductRealNameStatus().equals("0")) {// 认证审核中
				Toast.makeText(context, "该财富师身份审核中，不能绑定", Toast.LENGTH_SHORT)
						.show();
			} else if (cfmpBoundBean.getProductRealNameStatus().equals("2")) {// 认证未通过审核
				Toast.makeText(context, "认证未通过审核", Toast.LENGTH_SHORT).show();
			}
		}

	}

	private void boundList(String cfmpId) {
		progressDialog.show();
		JSONObject json = new JSONObject();
		StringEntity entity = null;
		try {
			json.put("investorId", userBean.getUserId());
			json.put("cfmpId", cfmpId);
			entity = new StringEntity(json.toString(), "utf-8");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		client.post(context, HttpConfig.URL_GETCFMPIS, entity,
				"application/json", new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							if (commonJSON(response)) {
								JSONObject object = response.getJSONObject("result").getJSONObject("info");
								addCfmpBean = ParseUtils.parseAddCfmp(object.toString());
								getUser(addCfmpBean);
							}
						} catch (JSONException E) {
							E.printStackTrace();
						}
						progressDialog.dismiss();
						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}
					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable, errorResponse);
						Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();

					}
				});
	}

	/**
	 * investors have or have not the binding wealth division
	 */
	public String isBindCfmp() {
		progressDialog.show();
		client.get(HttpConfig.URL_INVESTOR_BOUND,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							JSONArray jsonArray = null;
							if (commonJSON(response)) {
								if (!response.getJSONObject("result").isNull("list")) {
									jsonArray = response.getJSONObject("result").getJSONArray("list");
									for (int i = 0; i < jsonArray.length(); i++) {
										investorId = jsonArray.getString(i);
									}
									if (investorId == null|| investorId.equals(null)) {
										((InverstorToCfmpActivity) context).bound_cfmp.setVisibility(View.VISIBLE);
									} else {
										((InverstorToCfmpActivity) context).listView1.setVisibility(View.VISIBLE);
										initData();
									}
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
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}
					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable, errorResponse);
						Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();

					}
				});
		return investorId;
	}

	/**
     * 
     */
	private void getUser(AddCfmpBean addCfmpBean) {
		progressDialog.show();
		RequestParams params = new RequestParams();
		if (addCfmpBean.getCfmpId() != null) {
			params.put("0", addCfmpBean.getCfmpId());
		}
		client.get(HttpConfig.URL_GETUSER, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							if (commonJSON(response)) {
								JSONObject object = response.getJSONObject("result").getJSONObject("info");
								list_cfmp.add(ParseUtils.parseBindCfmp(object.toString()));
								QuerencfmpAdapter adapter = new QuerencfmpAdapter(context, list_cfmp);
								((InverstorToCfmpActivity) context).listView1.setAdapter(adapter);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						progressDialog.dismiss();
						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						Log.e("jdjjdjd", "失败啦！！！！");
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}
					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable, errorResponse);
						Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
						progressDialog.dismiss();
					}
				});
	}

	/**
	 * AsyncHttpClient success
	 */
	private boolean commonJSON(JSONObject response) {
		boolean status = false;
		try {
			JSONObject obj = response.getJSONObject("result");
			boolean flag = response.getBoolean("success");
			String errCode = obj.getString("errorCode");
			if (flag && errCode.equals("success")) {
				status = true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;

	}
}
