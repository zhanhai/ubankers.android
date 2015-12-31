package cn.com.ubankers.www.user.service;

import java.lang.reflect.Type;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.http.ParseUtils;
import cn.com.ubankers.www.user.controller.activity.MemberWealthCenterActivity;
import cn.com.ubankers.www.user.controller.activity.MemberWealthStudioActivity;
import cn.com.ubankers.www.user.controller.activity.UserCenterActivity;
import cn.com.ubankers.www.user.controller.activity.WealthCenterActivity;
import cn.com.ubankers.www.user.controller.activity.WealthMattersActivity;
import cn.com.ubankers.www.user.controller.activity.WealthStudioActivity;
import cn.com.ubankers.www.user.model.MoneyBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.model.UserNewBean;
import cn.com.ubankers.www.user.model.WealthBean;
import cn.com.ubankers.www.utils.CompleteDialog;
import cn.com.ubankers.www.utils.LoginDialog;
import cn.com.ubankers.www.utils.XutilsHttp;
import cn.com.ubankers.www.widget.MyDialog;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 
 * @author 廖准华 个人中心的service
 */
public class UserCenterService {
	private AsyncHttpClient client;
	private Context context;
	private String userUrl;
	
	private WealthBean wealthBean;
	public MyDialog myDialog;
	private Intent intent = null;
	private UserNewBean user;
	public UserBean userBean ;
	private LoginDialog loginDialog;
	private CompleteDialog completeDialog;
	private AlertDialog dialog;
	private View view;
	private TextView textday;
	private LinearLayout ll;
	public int dayn;
	

	public UserCenterService(Context context) {
		this.context = context;
		if(MyApplication.app.getUser()!=null){
			userBean=MyApplication.app.getUser();
		}
		client = MyApplication.app.getClient(context);
		if(myDialog==null){
			   myDialog=MyDialog.createDialog(context,"正在加载中...");
			}
		
		if(userBean!=null&&userBean.getUserMobile()!=null&&userBean.getUserMobile().equals("")){
			completeDialog = new CompleteDialog(context, userBean);
			completeDialog.createDialog(1);
			completeDialog.setOnKeyListener(new OnKeyListener() {  
		            @Override  
		            public boolean onKey(DialogInterface dialog, int keyCode,  
		                    KeyEvent event) {  
		                if (keyCode == KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0) { 
		                	  return false;  
		                }  
		                return false;  
		            }  
		        });
			return;
		}		
	  initData();
	}

	/**
	 * Get the user information
	 */

	public void initData() {
		client.get(context, HttpConfig.URL_USER_INFO, null, "application/json",
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {	
							if (commonJSON(response)) {
								 JSONObject info = response.getJSONObject("result").getJSONObject("user");
								if (!info.isNull("extAttributes")) {
									JSONArray extAttributes = info.getJSONArray("extAttributes");
									Type type = new TypeToken<UserNewBean>() {}.getType();
									 user = ParseUtils.parseUserInfo(info.toString(), type);
								     userBean = ParseUtils.parseUserBeanlg(user,context);
								     if (userBean!=null&&userBean.getUserFaceID()!=null) {
//										MyApplication.loadImage(HttpConfig.HTTP_IMAGE_QUERY_URL+userBean.getUserFaceID(), ((UserCenterActivity) context).faceImage,null);
										new XutilsHttp(context).display( ((UserCenterActivity) context).faceImage, HttpConfig.HTTP_IMAGE_QUERY_URL+userBean.getUserFaceID());
								     }else {
											((UserCenterActivity) context).faceImage.setBackgroundResource(R.drawable.personal_center);
									}
								     if(userBean!=null&&userBean.getUserName()!=null&&!userBean.getUserName().equals("")){
											((UserCenterActivity) context).recent_nickname.setText(userBean.getUserName());											
										}
								     showUserInfo(userBean);
								 }
								//showUserInfo(user);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						finally{
							myDialog.dismiss();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
						Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
						myDialog.dismiss();
					}
				});
		
	}
    
	/**
	 * @param attributesBean
	 * show user info
	 */
	private void showUserInfo(UserBean userBean) {
			if(userBean!=null&&userBean.getUserName()!=null&&!userBean.getUserName().equals("")){
			((UserCenterActivity) context).recent_nickname.setText(userBean.getUserName());
			/*Log.e("aaaaaaaaaaa", userBean.getBase().getRealName());*/
		}
//			else{
//			((UserCenterActivity) context).recent_nickname.setText(userBean.getUserName());
//		}
		
	  if (userBean!=null&&userBean.getUserFaceID()!=null) {
//				MyApplication.loadImage(HttpConfig.HTTP_IMAGE_QUERY_URL+userBean.getUserFaceID(), ((UserCenterActivity) context).faceImage,null);
				new XutilsHttp(context).display(((UserCenterActivity) context).faceImage, HttpConfig.HTTP_IMAGE_QUERY_URL+userBean.getUserFaceID());
	  }else {
				((UserCenterActivity) context).faceImage.setBackgroundResource(R.drawable.personal_center);
		}
			getUserRole(userBean); //判断用户角色
	}

	/**
	 * @param attributesBean
	 * Get the user role
	 */
	private void getUserRole(UserBean userBean) {
		// 判断用户角色
		if (userBean != null && userBean.getUserRole()!= null
				&&userBean.getUserRole().equals("fundraiser")) {
				((UserCenterActivity) context).layout_fundraiser.setVisibility(View.VISIBLE);
				((UserCenterActivity) context).role.setText("资本师");
			} else if (userBean != null && userBean.getUserRole()!= null
					&&userBean.getUserRole().equals("cfmp")) {
				((UserCenterActivity) context).layout_cfmp.setVisibility(View.VISIBLE);
				((UserCenterActivity) context).role.setText("财富师");
			} else if (userBean != null && userBean.getUserRole()!= null
					&&userBean.getUserRole().equals("investor")) {
				((UserCenterActivity) context).layout_investor.setVisibility(View.VISIBLE);
				((UserCenterActivity) context).role.setText("投资者");
			}
	}
	/**
	 * @param imageUrl
	 * client Upload users face
	 */
	public void uploadingAvatar(String imageUrl) {
		myDialog.show();
		RequestParams params = new RequestParams();
		params.put("picData", imageUrl);
		client.post(context, HttpConfig.URL_UPLOADPHOTO, params,new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							if (commonJSON(response)) {
								JSONObject info = response.getJSONObject("result").getJSONObject("info");
								String userFaceId = info.getString("ids");
								MyApplication.setUserFaceId(userFaceId);
								getExtends();
								myDialog.dismiss();
								myDialog.setCancelable(true);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							myDialog.dismiss();
							myDialog.setCancelable(true);
						}
					}
					@Override
					public void onFinish() {
						super.onFinish();
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
						super.onFailure(statusCode, headers, throwable, errorResponse);
						Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
						myDialog.dismiss();
					}
					@Override
					public void onRetry(int retryNo) {
						super.onRetry(retryNo);
					}
				});
	}

	/**
	 * For information on user extensions
	 */
	private void getExtends() {
		JSONObject json = new JSONObject();
		StringEntity entity1 = null;
		try {
			json.put("catalog", "userface");
			json.put("name", "fileid");
			json.put("value", MyApplication.getUserFaceId());
			entity1 = new StringEntity(json.toString(), "utf-8");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		client.post(context, HttpConfig.URL_USEREXTEBD, entity1,
				"application/json", new JsonHttpResponseHandler() {
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						if(commonJSON(response)) {
//							MyApplication.loadImage(HttpConfig.HTTP_IMAGE_QUERY_URL+MyApplication.getUserFaceId(), ((UserCenterActivity) context).faceImage,null);
							new XutilsHttp(context).display(((UserCenterActivity) context).faceImage, HttpConfig.HTTP_IMAGE_QUERY_URL+MyApplication.getUserFaceId());
							Toast.makeText(context, "上传成功！！", 0).show();
						}
					  };

					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						Log.e("jdjjdjd", "失败啦！！！！");
						super.onFailure(statusCode, headers, responseString,
								throwable);
					};
					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable, errorResponse);
						Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
						myDialog.dismiss();
					}
				});
	}
	/**
	 * users can balance
	 */
	public void inityue() {
		client.get(HttpConfig.URL_MONEY+user.getBase().getId(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				try {
					if (commonJSON(response)) {
						JSONObject info = response.getJSONObject("result").getJSONObject("info");
						MoneyBean moneyBean =ParseUtils.parseMoney(info.toString());
					}
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
				Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
				myDialog.dismiss();
			}

		});
	}

	/** 
	 * Studio to create wealth 
	 */
	public void checkTreasure() {
		StringEntity entity = null;
		client.get(context, HttpConfig.URL_CHECK_WEALTHS, entity,
				"application/json", new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
//							if(commonJSON(response)){
								
								JSONObject obj = response.getJSONObject("result");
								String errorCode = obj.getString("errorCode");
								JSONObject info = response.getJSONObject("result").getJSONObject("info");
								if(errorCode.equals("noStudio")){ //可以创建工作室
									if(info.length()==0){
										intent = new Intent(context,WealthMattersActivity.class);
										context.startActivity(intent);
									}else {
										try {
											dayn=info.getInt("createNeedDays");
											dialog = new AlertDialog.Builder(context).create();				 
											 dialog.setCanceledOnTouchOutside(false);				 
												view = LayoutInflater.from(context).inflate(R.layout.create_space_days, null);
												textday = (TextView)view.findViewById(R.id.dayuu);										
												ll = (LinearLayout)view.findViewById(R.id.ll);
												textday.setText(dayn+"");
												ll.setOnClickListener(new OnClickListener() {
														@Override
														public void onClick(View arg0) {
															
															dialog.dismiss();								
														}
													});
												dialog.setView(view);										
												dialog.show();
										} catch (Exception e) {
											e.printStackTrace();
										}
										
									}

								}else if(errorCode.equals("success")){
									skipStudio(info.toString());
									Log.i("", info+"");
								}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
		
					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
						Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				});
	}
	
	/**
	 * AsyncHttpClient success
	 */
	private boolean commonJSON(JSONObject response){
		boolean status=false;
		try {
			JSONObject obj= response.getJSONObject("result");
			boolean flag = response.getBoolean("success");
			String errCode = obj.getString("errorCode");
			if(flag && errCode.equals("success")){
				status=true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
		
	}

	/**
	 * Jump wealth studio 
	 */
	public void skipStudio(String param) {
		wealthBean = ParseUtils.parseWealth(param);
		Bundle bundle = new Bundle();
		bundle.putSerializable("wealthBean", wealthBean);
		if(wealthBean.getLeaderId().equals(userBean.getUserId())){ //领袖登录
		
		if (wealthBean.getAuditStatus() == 1|| wealthBean.getAuditStatus() == 2) {//1.未提交审核2.已提交审核中
			intent = new Intent(context, WealthCenterActivity.class);
		} else if (wealthBean.getAuditStatus() == 3) { //通过审核
			intent = new Intent(context, WealthStudioActivity.class);
		} else if (wealthBean.getAuditStatus() == 4) {
			
			intent = new Intent(context, WealthCenterActivity.class);
		}
		intent.putExtras(bundle);
		context.startActivity(intent);
		}else { //成员登录
			if(wealthBean.getAuditStatus() == 3){ //成员
				intent = new Intent(context, MemberWealthStudioActivity.class);
			}else if(wealthBean.getAuditStatus() == 1 || wealthBean.getAuditStatus() == 2){//成员1.未提交审核2.已提交审核中
				intent = new Intent(context, MemberWealthCenterActivity.class);
			}else if (wealthBean.getAuditStatus() == 4){
				intent = new Intent(context, MemberWealthCenterActivity.class);
			}
			intent.putExtras(bundle);
			context.startActivity(intent);
		}
	}
}
