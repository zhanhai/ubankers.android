package cn.com.ubankers.www.user.controller.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.zxing.WriterException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.model.WealthBean;
import cn.com.ubankers.www.utils.EncodingHandler;
import cn.com.ubankers.www.widget.MyDialog;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class WealthspaceInvitationActivity extends Activity implements OnItemClickListener,PlatformActionListener,OnClickListener{
	private Context context;
	private WealthBean wealthBean;
	private MyDialog progressDialog;
	private AsyncHttpClient client;
	private UserBean userBean;
	private String encryptUserId;
	private Bitmap qrCodeBitmap;
	private View investor_add_view;
	private ImageView recommend_cfmpEncode;
	private int images[] = {R.drawable.logo_wechat,R.drawable.logo_qq};
	private String names[]={"微信","腾讯QQ"};
	private GridView recommend_gridview;
	private String recommenderUrl ="" ;
	private static final int WECHATNUM=0;
	private static final int QQNUM=1;
    private static final int MSG_TOAST = 1;
    private static final int MSG_ACTION_CCALLBACK = 2;
	private static final int MSG_CANCEL_NOTIFY = 3;
	private LinearLayout back;
	public String info="";
	private String url2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		ShareSDK.initSDK(context);
		this.setContentView(R.layout.wealthspace_invitation_activity);
		Intent intent = this.getIntent();
		if(intent!=null){
			wealthBean = (WealthBean) intent.getSerializableExtra("wealthBean");
		}
		if(progressDialog==null){
			progressDialog=MyDialog.createDialog(WealthspaceInvitationActivity.this,"正在加载中...");
		}
		client = MyApplication.app.getClient(context);
		if(MyApplication.app.getUser()!=null){
			userBean=MyApplication.app.getUser();
		}
		url2 = getUrl();	
		if(!url2.equals("")){
			Create2DCode(userBean);	
		}
//		Create2DCode(userBean);
		initView();
		MyApplication.getInstance().addActivity(this);
	}
	private String getUrl() {
		// TODO Auto-generated method stub
		client.get(HttpConfig.URL_URL, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				try {
					JSONObject obj = response.getJSONObject("result");
					boolean flag = response.getBoolean("success");
					String errCode = obj.getString("errorCode");
					if(errCode.equals("success")&&flag==true){
						info = obj.getString("info");
						Create2DCode(userBean);
						Log.i("", info+"");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
				Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
			}
		});
		return info;
	}
	private void initView() {
		// TODO Auto-generated method stub
		back = (LinearLayout)findViewById(R.id.back);
		back.setOnClickListener(this);
		investor_add_view=(View)findViewById(R.id.investor_add_view);
		recommend_cfmpEncode=(ImageView)findViewById(R.id.recommend_cfmpEncode);
		recommend_gridview = (GridView) findViewById(R.id.recommend_gridview);
		recommend_gridview.setNumColumns(2);
//		if(Create2DCode(userBean)!=null){
//			   recommend_cfmpEncode.setImageBitmap(Create2DCode(userBean));
//		   }
		ArrayList<HashMap<String, Object>> item = new ArrayList<HashMap<String, Object>>();
	       for (int i = 0; i < images.length; i++) {
	          HashMap<String, Object> map = new HashMap<String, Object>();
	          map.put("itemImage", images[i]);
	          map.put("itemName", names[i]);
	            item.add(map);
	            SimpleAdapter saImageItems = new SimpleAdapter(context, item,R.layout.gridview_items,new String[] { "itemImage", "itemName" }, new int[] { R.id.ItemImage, R.id.ItemName });  
	            // 添加并且显示  
	            recommend_gridview.setAdapter(saImageItems);  
	            // 添加消息处理  
	            recommend_gridview.setOnItemClickListener(this); 
	       }
	}
	private Bitmap Create2DCode(UserBean userBean){
		try {
		   qrCodeBitmap = EncodingHandler.createQRCode(info, 200);
		   recommend_cfmpEncode.setImageBitmap(qrCodeBitmap);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
return qrCodeBitmap;
}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub	
		recommenderUrl =HttpConfig.URL_RECOMMENFD_CFMP+encryptUserId;
		switch(arg2){		
		case WECHATNUM:
				if(isInstalled(context,"com.tencent.mm")==false){
					Toast.makeText(context, "请安装微信再分享给好友", 1).show();
				}else{	
			  Wechat.ShareParams wechat = new Wechat.ShareParams();
			  wechat.title = ("Let's be Ubankers");
			  wechat.text = ("带您在有品、有趣料、有钱赚的OFIS平台创建金融新世界,邀您做财富师邀请您成为银板客财富师链接");
			  wechat.url=info;
			  wechat.shareType =Platform.SHARE_WEBPAGE;
		      Platform localPlatform1 = ShareSDK.getPlatform(context, Wechat.NAME);
		      localPlatform1.SSOSetting(true);		     
		      localPlatform1.setPlatformActionListener(this);
		      ShareSDK.removeCookieOnAuthorize(true);
		      localPlatform1.removeAccount();
		      localPlatform1.share(wechat);
				}
			break;	
		case QQNUM:
			if(isInstalled(context,"com.tencent.mobileqq")==false){
				Toast.makeText(context, "请安装qq再分享给好友", 1).show();
			}else{
			  QQ.ShareParams qq = new QQ.ShareParams();
			  qq.title = ("Let's be Ubankers");
			  qq.text=("带您在有品、有趣料、有钱赚的OFIS平台创建金融新世界,邀您做财富师邀请您成为银板客财富师链接");
			  qq.titleUrl=info;
		      Platform localPlatform4 = ShareSDK.getPlatform(context, QQ.NAME);
		      localPlatform4.SSOSetting(true);
		      localPlatform4.setPlatformActionListener(this);
		      ShareSDK.removeCookieOnAuthorize(true);
		      localPlatform4.removeAccount();
		      localPlatform4.share(qq);
			}
			break;
		}
		
	 }
		public boolean handleMessage(Message msg) {
			switch (msg.arg1) {
			case 1: {
				// 成功
				Toast.makeText(this, "分享成功", 10000).show();
				System.out.println("分享回调成功------------");
			}
				break;
			case 2: {
				// 失败
				Toast.makeText(this, "分享失败", 10000).show();
			}
				break;
			case 3: {
				// 取消
				Toast.makeText(this, "分享取消", 10000).show();
			}
				break;
			}
			return false;
		}
		
		/**
		 * 判断应用是否已安装 
		 * @param context
		 * @param packageName
		 * @return
		 */ 
		 private boolean isInstalled(Context context, String packageName) { 
			    boolean hasInstalled = false; 
			    PackageManager pm = context.getPackageManager(); 
			    List<PackageInfo> list = pm.getInstalledPackages(PackageManager.PERMISSION_GRANTED); 
			    for (PackageInfo p : list) { 
			        if (packageName != null && packageName.equals(p.packageName)) { 
			            hasInstalled = true; 
			            break; 
			        } 
			    } 
			    return hasInstalled; 
			}
			/**
			 * 判断应用是否正在运行
			 * @param context
			 * @param packageName
			 * @return
			 */ 
			private boolean isRunning(Context context, String packageName) { 
			    ActivityManager am = (ActivityManager) context 
			            .getSystemService(Context.ACTIVITY_SERVICE); 
			    List<RunningAppProcessInfo> list = am.getRunningAppProcesses(); 
			    for (RunningAppProcessInfo appProcess : list) { 
			        String processName = appProcess.processName; 
			        if (processName != null && processName.equals(packageName)) { 
			            return true; 
			        } 
			    } 
			    return false; 
			}

	@Override
	public void onCancel(Platform platform, int action) {
		// TODO Auto-generated method stub
		// 取消
					Message msg = new Message();
					msg.what = MSG_ACTION_CCALLBACK;
					msg.arg1 = 3;
					msg.arg2 = action;
					msg.obj = platform;
	}
	@Override
	public void onComplete(Platform platform, int action, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		// 成功
					Message msg = new Message();
					msg.what = MSG_ACTION_CCALLBACK;
					msg.arg1 = 1;
					msg.arg2 = action;
					msg.obj = platform;
	}
	@Override
	public void onError(Platform platform, int action, Throwable t) {
		// TODO Auto-generated method stub
		t.printStackTrace();
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = t;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){		
		case R.id.back:
			WealthspaceInvitationActivity.this.finish();			
		}
	}

}
