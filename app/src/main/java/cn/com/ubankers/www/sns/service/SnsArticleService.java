package cn.com.ubankers.www.sns.service;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.authentication.controller.activity.LoginActivity;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.sns.controller.activity.CommentActivity;
import cn.com.ubankers.www.sns.controller.activity.SnsArticleActivity;
import cn.com.ubankers.www.sns.model.ArticleBean;
import cn.com.ubankers.www.utils.LoginDialog;
import cn.com.ubankers.www.utils.NetReceiver;
import cn.com.ubankers.www.utils.NetReceiver.NetState;
import cn.com.ubankers.www.widget.SharePopupWindow;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SnsArticleService {

	private Context context;
	private String description;
	private SharePopupWindow shareWindow;
	private AsyncHttpClient client;
	private LoginDialog loginDialog;

	public SnsArticleService(Context context, AsyncHttpClient client) {
		super();
		this.context = context;
		this.client = client;
		loginDialog = new LoginDialog(context, 1,2);
	}

	// 取消点赞接口
	public void callVote(ArticleBean articleBean) {
		String params = null;
		try {
			String articleId = articleBean.get_id();
			params = articleId + "/votes";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.delete(context, HttpConfig.URL_LOVE + params,
				new JsonHttpResponseHandler() {
					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							JSONObject response) {
						try {
							JSONObject errorCode=null;
							boolean flag = response.getBoolean("success");
							if(!response.isNull("error")){
								errorCode  = response.getJSONObject("error");
							}
							if (flag) {
								Toast.makeText(context, "取消喜欢",
										Toast.LENGTH_SHORT).show();
								((SnsArticleActivity) context).love
										.setBackgroundResource(R.drawable.heart);
							} else if(!errorCode.isNull("desc")) {
									description = errorCode.getString("desc");
								if(description!=null&& description.equals("2")){
									TimeOutMethod();
								}else{
									Toast.makeText(context, "异常",Toast.LENGTH_SHORT).show();
								}	
							}
						}catch (JSONException e) {
							// TODO Auto-generated catch block
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
						
					}
				});
	}

	// 点赞接口
	public void vote(ArticleBean articleBean) {
		String params = null;
		try {
			StringEntity entity;
			String articleId = articleBean.get_id();
			params = articleId + "/votes";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.post(HttpConfig.URL_LOVE + params,
				new JsonHttpResponseHandler() {
					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							JSONObject response) {
						try {
							JSONObject errorCode=null;
							boolean flag = response.getBoolean("success");
							if(!response.isNull("error")){
								errorCode  = response.getJSONObject("error");
							}
							if (flag) {
								Toast.makeText(context, "喜欢",
										Toast.LENGTH_SHORT).show();
								((SnsArticleActivity) context).love
										.setBackgroundResource(R.drawable.hearted);
						   } else if(!errorCode.isNull("desc")) {
								description = errorCode.getString("desc");
							if(description!=null&& description.equals("2")){
								TimeOutMethod();
							}else{
								Toast.makeText(context, "异常",Toast.LENGTH_SHORT).show();
							}	
						}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
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
					}
				});
	}
	// 收藏接口
	public void collections(ArticleBean articleBean) {
		String params = null;
		try {
			String articleId = articleBean.get_id();
			params = articleId + "/favor";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.post(HttpConfig.URL_COLLECTIONS + params,
				new JsonHttpResponseHandler() {
					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							JSONObject response) {

						try {
							JSONObject errorCode=null;
							boolean flag = response.getBoolean("success");
							if(!response.isNull("error")){
								errorCode  = response.getJSONObject("error");
							}
							if (flag){
								Toast.makeText(context, "收藏",
										Toast.LENGTH_SHORT).show();
								((SnsArticleActivity) context).collections
										.setBackgroundResource(R.drawable.stared);
							} else if(!errorCode.isNull("desc")) {
								description = errorCode.getString("desc");
							if(description!=null&&description.equals("2")){
								TimeOutMethod();
							}else{
								Toast.makeText(context, "异常",Toast.LENGTH_SHORT).show();
							}	
						  }
						} catch (JSONException e) {
							// TODO Auto-generated catch block
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
					}
				});
	}

	//取消 收藏接口
	public void callCollections(ArticleBean articleBean) {
		String params = null;
		try {
			String articleId = articleBean.get_id();
			params = articleId + "/favor";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.delete(HttpConfig.URL_COLLECTIONS + params,
				new JsonHttpResponseHandler() {
					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							JSONObject response) {
						try {
							JSONObject errorCode=null;
							boolean flag = response.getBoolean("success");
							if(!response.isNull("error")){
								errorCode  = response.getJSONObject("error");
							}
							if (flag) {
								Toast.makeText(context, "取消收藏",
										Toast.LENGTH_SHORT).show();
								((SnsArticleActivity) context).collections
										.setBackgroundResource(R.drawable.star);
							} else if(!errorCode.isNull("desc")) {
								description = errorCode.getString("desc");
							if(description!=null&& description.equals("2")){
								TimeOutMethod();
							}else{
								Toast.makeText(context, "异常",Toast.LENGTH_SHORT).show();
							}
						  }
						} catch (JSONException e) {
							// TODO Auto-generated catch block
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
					}
				});

	}

	// 对于session超时处理
	public void TimeOutMethod() {
		Toast.makeText(context, "用户登录超时,请重新登录", Toast.LENGTH_SHORT).show();
		MyApplication.app.setUser(null);
		MyApplication.app.setClient(null);
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);
		((SnsArticleActivity) context).finish();

	}

	public void back(){
		((SnsArticleActivity) context).finish();	
	}

	// bottom four widget item click
	@SuppressWarnings("static-access")
	public void itemClick(View arg0, ArticleBean articleBean) {
		NetState connected = NetReceiver.isConnected(context);
		if (connected == connected.NET_NO) {
			Toast.makeText(context, "当前网络不可用", Toast.LENGTH_SHORT).show();
		} else {
			if (MyApplication.app.getUser() != null
					&& arg0.getId() != R.id.share_layout) {
				switch (arg0.getId()) {
				case R.id.collections_layout:
					clickCollection(articleBean);
					break;
				case R.id.love_layout:
					clickLove(articleBean);
					break;
				case R.id.arguments_layout:
					skipCommentActivity(articleBean);
					break;
				}
			} else if (arg0.getId() == R.id.share_layout) {
				clickShare(articleBean);
			} else {
				loginDialog.onLogin();// 用户没有登录弹出登录页面
			}
		}
	}

	public void clickCollection(ArticleBean articleBean) {
		if (!((SnsArticleActivity) context).collectionsFlag) {
			collections(articleBean);
			((SnsArticleActivity) context).collectionsFlag = true;
		} else {
			callCollections(articleBean);
			((SnsArticleActivity) context).collectionsFlag = false;
		}
	}

	public void clickLove(ArticleBean articleBean) {
		if (!((SnsArticleActivity) context).loveFlag) {
			vote(articleBean);
			((SnsArticleActivity) context).loveFlag = true;
		} else {
			callVote(articleBean);
			((SnsArticleActivity) context).loveFlag = false;
		}
	}

	public void clickShare(ArticleBean articleBean) {
		shareWindow = new SharePopupWindow(context, null, articleBean);
		// 显示窗口
		shareWindow.showAtLocation(((SnsArticleActivity) context).share_layout,
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

	}

	// current activity skip CommentActivity
	public void skipCommentActivity(ArticleBean articleBean) {
		Intent intent = new Intent(context, CommentActivity.class);
		Bundle bundle = new Bundle();
		if (bundle != null) {
			bundle.putSerializable("articleBean", articleBean);
			intent.putExtras(bundle);
		}
		context.startActivity(intent);
	}

	/**
	 * TODO popwindow dismiss
	 */
	private OnClickListener itemsOnClick = new OnClickListener() {
		public void onClick(View v) {
			shareWindow.dismiss();

		}
	};

}
