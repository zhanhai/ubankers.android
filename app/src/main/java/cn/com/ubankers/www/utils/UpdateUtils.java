package cn.com.ubankers.www.utils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;




import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.widget.UpdateDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UpdateUtils {
    private Context context;
    private AsyncHttpClient client;
    private int platform=1;
    private String version;
    public UpdateUtils(Context context,AsyncHttpClient client) {
	this.context=context;
	this.client = client;
    }
    public void getUpdateData(final int type) {
		client.setTimeout(10000);
		String versionName = getVersionName();
		client.get(HttpConfig.URL_UPDATE_VERSION+versionName+"/"+platform, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				Log.i("updateUrlResponse", response.toString());
				try {
					String message=null;
					boolean  flag = response.getBoolean("success");
					if (flag) {
						JSONObject reuslt = response.getJSONObject("result");
						JSONObject  object =reuslt.getJSONObject("info");
						String fileUrl = object.getString("fileUrl");
						String fileSize = object.getString("fileSize");
						int update = object.getInt("update");
						if(update==1&&fileUrl!=null&&!fileUrl.equals("")){
							try{
									JSONObject releaseNote = object.getJSONObject("releaseNote");
									//服务器的版本号
									version = releaseNote.getString("version");
									message = releaseNote.getString("message");
									}catch(JSONException e){
										e.printStackTrace();
									}
									//当前的版本号
									String version2 = Tools.getVersion(context);
									String version1=version.replace(".","");
									version2=version2.replace(".","");
									int parseInt = Integer.parseInt(version1.toString().trim());
									int parseInt2 = Integer.parseInt(version2.toString().trim());
									if(parseInt>parseInt2){
										showAlertDialog(message, true, fileUrl, fileSize,update);
									}
		//							else {
		//								showAlertDialogSuccess();
		//							}
					   }else if(update==0&&fileUrl.equals("")){
						   if(type==1){
									showAlertDialogSuccess();
						   }
					   }else if(update==2){//强制升级
						   try{
								JSONObject releaseNote = object.getJSONObject("releaseNote");
								//服务器的版本号
								version = releaseNote.getString("version");
								message = releaseNote.getString("message");
								}catch(JSONException e){
									e.printStackTrace();
						   }
						   showAlertDialogForce(message, true, fileUrl, fileSize, update);
					   }

					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
				}
			}
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(context, "网络连接超时，请稍候再试",
						Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
			@Override
			public void onFailure(int statusCode,
					org.apache.http.Header[] headers,
					java.lang.String responseString,
					java.lang.Throwable throwable) {
			}
		});
	}
    //升级版本的dialog
	public void showAlertDialog(String message, final boolean isUpdate,
			final String fileUrl, final String fileSize, final int update) {
		UpdateDialog.Builder builder = new UpdateDialog.Builder(context);
		builder.setTitle("已有最新版本："+version);
		builder.setMessage(message+ "\n");
		builder.setPositiveButton("升级", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (isUpdate) {
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri content_url = Uri.parse(fileUrl);
					intent.setData(content_url);
					context.startActivity(intent);
				}
			}
		});
		builder.setNegativeButton("暂不升级  ",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

	       builder.create().show();
	       
	}
	//升级版本的dialog
		public void showAlertDialogForce(String message, final boolean isUpdate,
				final String fileUrl, final String fileSize, final int update) {
			UpdateDialog.Builder builder = new UpdateDialog.Builder(context);
			builder.setTitle("已有最新版本："+version);
			builder.setMessage(message+ "\n");
			builder.setPositiveButton("升级", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					if (isUpdate) {
						Intent intent = new Intent();
						intent.setAction("android.intent.action.VIEW");
						Uri content_url = Uri.parse(fileUrl);
						intent.setData(content_url);
						context.startActivity(intent);
					}
				}
			});
			builder.setNegativeButton("退出",
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							MyApplication.app.exitApply();
							MyApplication.getInstance().exit();
							System.exit(0);
						}
					});

		       builder.create().show();
		       
		}
	private void showAlertDialogSuccess(){
		UpdateDialog.Builder builder = new UpdateDialog.Builder(context);
		builder.setTitle("版本升级");
		builder.setMessage("已是最新版本");
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

	       builder.create().show();
	}

	public String getVersionName() {
	// 获取packagemanager的实例
	PackageManager packageManager = context.getPackageManager();
	// getPackageName()是你当前类的包名，0代表是获取版本信息
	PackageInfo packInfo = null;
	try {
	    packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
	} catch (NameNotFoundException e) {
	    // TODO 自动生成的 catch 块
	    e.printStackTrace();
	}
	if (null != packInfo)
	    return packInfo.versionName;
	else
	    return "1.0.6.4";
    }
}
