package cn.com.ubankers.www.user.controller.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.MembersBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.model.WealthBean;
import cn.com.ubankers.www.user.view.CfmpMembersAdapter;
import cn.com.ubankers.www.utils.EncodingHandler;
import cn.com.ubankers.www.widget.ActionItem;
import cn.com.ubankers.www.widget.ProcessDialog;
import cn.com.ubankers.www.widget.TitlePopup;
import cn.com.ubankers.www.widget.TitlePopup.OnItemOnClickListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class WealthCenterActivity extends Activity  implements OnItemClickListener, PlatformActionListener,OnClickListener{
	private GridView recommend_gridview;
	private int images[] = {R.drawable.logo_wechat,R.drawable.logo_qq};
	private String names[]={"微信","腾讯QQ"};
    private static final int WECHATNUM=0;
    private static final int QQNUM=1;
	private static final int MSG_TOAST = 1;
	private static final int MSG_ACTION_CCALLBACK = 2;
	private static final int MSG_CANCEL_NOTIFY = 3;
	private Context context;
	private ClipboardManager mClipboard = null;
	private UserBean userBean;
	private String recommenderUrl ="" ;
	private ImageView recommend_cfmpEncode;
	private TextView requestHourse,day,tt;
	private String encryptUserId,url,unicodeNam,unicodeName;
	
	private Bitmap qrCodeBitmap;
	private TitlePopup titlePopup;
	private AsyncHttpClient client;
	private WealthBean wealthBean;
	private LinearLayout AddOrder_layout;
	private LinearLayout investorAdd_layout,catelogView,llreq;
	private TextView AddOrder,textresult;
	private TextView investorAdd,text;
	private View AddOrderView,investorAddView,investor_add_view,add_list_view,back;
	private String object;
	private MembersBean memberBean;
	private List<MembersBean> allMemberList;
	private ProcessDialog progressDialog;
	private String totalCount;
	private JSONArray list;
	private ListView add_listView;
	private CfmpMembersAdapter adapter;
	private AlertDialog dialog;
	private View view;
	private TextView text1,text2,AddOrder_no,login_title;
	private Button ok,disband_btn,applybtn,cancel;
	public String info="";
	private String url2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		ShareSDK.initSDK(context);
		this.setContentView(R.layout.treasure_hoursebuilding);
		Intent intent =this.getIntent();
		if(intent!=null){
			wealthBean = (WealthBean) intent.getSerializableExtra("wealthBean");
		}
		if(progressDialog==null){
			progressDialog= ProcessDialog.createDialog(WealthCenterActivity.this, "正在加载中...");
		}
		client = MyApplication.app.getClient(context);
		if(MyApplication.app.getUser()!=null){
			userBean=MyApplication.app.getUser();
		}
		url2 = getUrl();	
		if(!url2.equals("")){
			Create2DCode(userBean);	
		}
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
	private Bitmap Create2DCode(UserBean userBean){
				try {
				   qrCodeBitmap = EncodingHandler.createQRCode(info, 350);
				   recommend_cfmpEncode.setImageBitmap(qrCodeBitmap);
				} catch (WriterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		return qrCodeBitmap;
	}
   private void initView(){
	   llreq = (LinearLayout)findViewById(R.id.llreq);
	   login_title = (TextView)findViewById(R.id.login_title);
	   investor_add_view=(View)findViewById(R.id.investor_add_view);
	   add_list_view=(View)findViewById(R.id.add_list_view);
	   requestHourse=(TextView)findViewById(R.id.requestHourse);
	   day=(TextView)findViewById(R.id.day); //倒计时天数
	   day.setVisibility(View.GONE);

	   add_listView=(ListView)findViewById(R.id.add_listView);
	   AddOrder_no = (TextView)findViewById(R.id.AddOrder_no);
	   AddOrder_layout=(LinearLayout)findViewById(R.id.AddOrder_layout);
	   investorAdd_layout=(LinearLayout)findViewById(R.id.investorAdd_layout);
	   investorAdd=(TextView)findViewById(R.id.investorAdd);
	   AddOrder=(TextView)findViewById(R.id.AddOrder);
	   AddOrderView=(View)findViewById(R.id.AddOrderView);
	   investorAddView=(View)findViewById(R.id.investorAddView);
	   requestHourse.setOnClickListener(this);
	   
	   back=findViewById(R.id.back);
	   catelogView=(LinearLayout)findViewById(R.id.catelogView);
	   recommend_cfmpEncode=(ImageView)findViewById(R.id.recommend_cfmpEncode);
	   catelogView.setOnClickListener(this);
	   AddOrder_layout.setOnClickListener(this);
	   AddOrder_no.setOnClickListener(this);
	   investorAdd_layout.setOnClickListener(this);
	   AddOrder.setOnClickListener(this);
	   investorAdd.setOnClickListener(this);
	   recommend_gridview = (GridView) findViewById(R.id.recommend_gridview);
	   recommend_gridview.setNumColumns(2);
	   back.setOnClickListener(new BackOnClickListener());	
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
            // 实例化标题栏弹窗
    		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    		titlePopup.setBackgroundDrawable(getResources().getDrawable(R.color.treasure_catelog));
    		// 给标题栏弹窗添加子类
    		titlePopup.addAction(new ActionItem(this, "解散工作室"));
    		if(wealthBean.getAuditStatus()==2){
    			requestHourse.setText("申请审核中");
    			login_title.setText("审核中");
    			requestHourse.setBackgroundColor(Color.rgb(144, 144, 144));
    			llreq.setBackgroundColor(Color.rgb(144, 144, 144));
    		}else if(wealthBean.getAuditStatus()==1){
    			login_title.setText("审核中");
    			requestHourse.setOnClickListener(this);	
    		}else if(wealthBean.getAuditStatus()==3){
    			requestHourse.setText("审核通过");
    			requestHourse.setBackgroundColor(Color.rgb(0, 102, 204));
    		}else if(wealthBean.getAuditStatus()==4){ //财富师工作室审核未通过
    			requestHourse.setText("再次申请(剩余");
    			login_title.setText("审核中");
    			requestHourse.setBackgroundColor(Color.rgb(0, 102, 204));
    			day.setVisibility(View.VISIBLE);
    			day.setBackgroundColor(Color.rgb(0, 102, 204));
    			int a = wealthBean.getAuditNeedDays();
    			day.setText(wealthBean.getAuditNeedDays()+"天)");
    			 			
    			dialog = new AlertDialog.Builder(this).create();
    			dialog.setCanceledOnTouchOutside(false);
    			view = LayoutInflater.from(this).inflate(R.layout.examine_not_pass, null);
    			text1 =(TextView) view.findViewById(R.id.text1);
    			text2 =(TextView) view.findViewById(R.id.text2);
    			textresult = (TextView)view.findViewById(R.id.textresult);
    			textresult.setText(wealthBean.getAuditMemo());
    			ok = (Button) view.findViewById(R.id.ok);
    			ok.setOnClickListener(new OnClickListener() {
    					@Override
    					public void onClick(View arg0) {
    						// TODO Auto-generated method stub   						
    						dialog.dismiss();
    					}
    				});
    			
    			dialog.setView(view);
    			dialog.show();
    		}
    		
    		
    		
    		titlePopup.setItemOnClickListener(new MyOnItemOnClickListener()); //解除工作室
    }
   public class BackOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			WealthCenterActivity.this.finish();
//	        overridePendingTransition(
//			android.R.anim.slide_in_left,
//			android.R.anim.slide_out_right);
		}
		
	}
   
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub	
		recommenderUrl =HttpConfig.URL_RECOMMENFD_CFMP+url;
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
		      localPlatform1.share(wechat);
				}
			break;	
		case QQNUM:
			if(isInstalled(context,"com.tencent.mobileqq")==false){
				Toast.makeText(context, "请安装qq再分享给好友", 1).show();
			}else{
			  QQ.ShareParams qq = new QQ.ShareParams();
			  qq.title = ("Let's be Ubankers");
			  qq.text = ("带您在有品、有趣料、有钱赚的OFIS平台创建金融新世界,邀您做财富师邀请您成为银板客财富师链接");			  
			  qq.titleUrl=info;
			  String path = "file:///android_asset/icon.png";
			  qq.setImageUrl(path);
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
	public void onCancel(Platform platform, int action) {
		// 取消
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = platform;

	}
	@Override
	public void onComplete(Platform platform, int action,
			HashMap<String, Object> arg2) {
		// 成功
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = platform;
	}
	@Override
	public void onError(Platform platform, int action, Throwable t) {
		t.printStackTrace();
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = t;
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
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch(view.getId()){
			case R.id.catelogView:
				titlePopup.show(catelogView);
				break;
			case R.id.AddOrder_layout:
			case R.id.AddOrder:
			case R.id.AddOrder_no:
				AddOrder_no.setVisibility(View.VISIBLE);
				investor_add_view.setVisibility(View.GONE);
				add_list_view.setVisibility(View.VISIBLE);
				investorAddView.setVisibility(View.GONE);
				AddOrderView.setVisibility(View.VISIBLE);
				AddOrderView.setBackgroundColor(0xff0066cc);
				getAllTreasureMember();
				break;
			case R.id.investorAdd_layout:
			case R.id.investorAdd:
				AddOrder_no.setVisibility(View.GONE);
				investor_add_view.setVisibility(View.VISIBLE);
				add_list_view.setVisibility(View.GONE);
				AddOrderView.setVisibility(View.GONE);
				investorAddView.setBackgroundColor(0xff0066cc);
				investorAddView.setVisibility(View.VISIBLE);
				break;
			case R.id.requestHourse: //申请财富师工作室
			    if(wealthBean.getAuditStatus()==1){
			    	submit();
	    		}else if(wealthBean.getAuditStatus()==3){ //审核通过
	    			Intent intent= new Intent(WealthCenterActivity.this,WealthStudioActivity.class);
	    			 Bundle bundle = new Bundle();
					 bundle.putSerializable("wealthBean", wealthBean);
					 intent.putExtras(bundle);
	    			startActivity(intent);
	    		}else if(wealthBean.getAuditStatus()==4){	
	    		}else if(wealthBean.getAuditStatus() ==2){

	    		}else if(wealthBean.getAuditStatus()==2){
	    			
	    		}
				break;
			}
		 }
		private void getAllTreasureMember(){
	    	 progressDialog.show();
	    	 allMemberList = new ArrayList<MembersBean>();
	    	 StringEntity entity=null;
	    	 JSONObject json=null;
	    	 try{
	    		 if(wealthBean!=null&&wealthBean.getStudioId()!=null){
					  json = new JSONObject();
					  json.put("studioId", wealthBean.getStudioId());
					  entity = new StringEntity(json.toString(),"utf-8");
	    		 }
	    	 }catch(Exception e){
	    		 e.printStackTrace();
	    	 }
	    	 client.post(WealthCenterActivity.this, HttpConfig.URL_CFMP_MEMBERS, entity, "application/json", new JsonHttpResponseHandler(){
	    		 @Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							JSONObject obj = response.getJSONObject("result");
							boolean flag = response.getBoolean("success");
							String errCode = obj.getString("errorCode");
							if(!obj.isNull("totalCount")){
								totalCount =obj.getString("totalCount");
								AddOrder_no.setText(obj.getString("totalCount"));
							}
							if(!obj.isNull("list")){
						    list = obj.getJSONArray("list");
							}
							if (flag && errCode.equals("success")) {
								for(int i=0;i<list.length();i++){
									try {
										 object = list.getString(i);
										 Gson gson = new Gson();
									     memberBean = gson.fromJson(object, MembersBean.class);     
			                        } catch (Exception e) {
			                             e.printStackTrace();
			                        }	
									allMemberList.add(memberBean);
								}
								 adapter = new CfmpMembersAdapter(WealthCenterActivity.this, allMemberList);
								 add_listView.setAdapter(adapter);
							}
							progressDialog.dismiss();
						} catch (JSONException e) {
							e.printStackTrace();
							progressDialog.dismiss();
						}
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
		
		private void submit(){
			dialog = new AlertDialog.Builder(context).create();				 
			 dialog.setCanceledOnTouchOutside(false);				 
				view = LayoutInflater.from(context).inflate(R.layout.apply_cfmpspace, null);
				text = (TextView)view.findViewById(R.id.text);
				applybtn = (Button)view.findViewById(R.id.applybtn);
				applybtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							submitRequset();
							dialog.dismiss();
							login_title.setText("审核中");
							llreq.setBackgroundColor(Color.rgb(144, 144, 144));
						}
					});
				dialog.setView(view);
				dialog.show();
		}		
		private void submitRequset(){		
			StringEntity entity = null;
			JSONObject json = null;
			try{
				if(wealthBean!=null&&wealthBean.getStudioId()!=null){
				  json = new JSONObject();
				  json.put("studioId", wealthBean.getStudioId());
				  entity = new StringEntity(json.toString(),"utf-8");
				}else{
					return;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			client.post(context, HttpConfig.URL_SUBMIT_WEALTH, entity, "application/json", new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					
					try {
						JSONObject obj = response.getJSONObject("result");
						boolean flag = response.getBoolean("success");
						String errCode = obj.getString("errorCode");
						if (flag && errCode.equals("success")) {
							requestHourse.setText("申请审核中");
//							requestHourse.setBackgroundColor(Color.rgb(0, 102, 204));
							llreq.setBackgroundColor(Color.rgb(144, 144, 144));
							requestHourse.setBackgroundColor(Color.rgb(144, 144, 144));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
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
		public class MyOnItemOnClickListener implements OnItemOnClickListener { //解散工作室

			@Override
			public void onItemClick(ActionItem item, int position){ 
				dialog = new AlertDialog.Builder(context).create();				 
				 dialog.setCanceledOnTouchOutside(false);				 
					view = LayoutInflater.from(context).inflate(R.layout.disband_cfmpspace, null);
					text = (TextView)view.findViewById(R.id.text);
					disband_btn = (Button)view.findViewById(R.id.disband_btn);
					cancel = (Button)view.findViewById(R.id.cancel);
					disband_btn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								closeTreasureMember();								
							}
						});
					cancel.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
					dialog.setView(view);
					dialog.show();
			 }
			}
      private void closeTreasureMember(){
	   progressDialog.show();
	   StringEntity entity=null;
	   JSONObject json=null;
	   try{
		   if(wealthBean!=null&&wealthBean.getStudioId()!=null){
			  json = new JSONObject();
			  json.put("studioId", wealthBean.getStudioId());
			  entity = new StringEntity(json.toString(),"utf-8");
		    }
	    }catch(Exception e){
		 e.printStackTrace();
	   }
	  client.post(WealthCenterActivity.this, HttpConfig.URL_CLOSE_STUDIO, entity, "application/json", new JsonHttpResponseHandler(){
		 @Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					JSONObject obj = response.getJSONObject("result");
					boolean flag = response.getBoolean("success");
					String errCode = obj.getString("errorCode");
					if (flag && errCode.equals("success")) {
						Toast.makeText(WealthCenterActivity.this, "财富工作室解散成功", Toast.LENGTH_SHORT).show();
						requestHourse.setText("申请财富师工作室");
						requestHourse.setBackgroundColor(Color.rgb(0, 102, 204));
						 Intent intent  = new Intent(WealthCenterActivity.this,UserCenterActivity.class);
						 Bundle bundle = new Bundle();
						 bundle.putSerializable("wealthBean", wealthBean);
						 intent.putExtras(bundle);
						 startActivity(intent);
						 WealthCenterActivity.this.finish();
					}
				 } catch (JSONException e) {
					e.printStackTrace();
					progressDialog.dismiss();
				 }
				progressDialog.dismiss();
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
}
