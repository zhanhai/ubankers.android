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
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
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

public class MemberWealthCenterActivity extends Activity implements OnItemClickListener,PlatformActionListener,OnClickListener{
	private Context context;
	private WealthBean wealthBean;
	private ProcessDialog progressDialog;
	private AsyncHttpClient client;
	private UserBean userBean;
	private TextView login_title,AddOrder_no,investorAdd,AddOrder,text1,text2,text;
	private View investor_add_view,add_list_view,AddOrderView,investorAddView,back,view;
	private ListView add_listView;
	private LinearLayout AddOrder_layout,investorAdd_layout,catelogView;
	private ImageView recommend_cfmpEncode;
	private GridView recommend_gridview;
	private int images[] = {R.drawable.logo_wechat,R.drawable.logo_qq};
	private String names[]={"微信","腾讯QQ"};
	private TitlePopup titlePopup;
	private AlertDialog dialog;
	private Button ok,sure,cancel;
	private String encryptUserId,unicodeName,unicodeNam,url;
	private Bitmap qrCodeBitmap;
	private List<MembersBean> allMemberList;
	private String totalCount,object;
	private JSONArray list;
	private MembersBean memberBean;
	private CfmpMembersAdapter adapter;
	private String recommenderUrl ="" ;
	private static final int WECHATNUM=0;
    private static final int QQNUM=1;
    public String info="";
	private String url2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		ShareSDK.initSDK(context);
		this.setContentView(R.layout.member_wealth_center_activity);
		Intent intent =this.getIntent();
		if(intent!=null){
			wealthBean = (WealthBean) intent.getSerializableExtra("wealthBean");
		}
		if(progressDialog==null){
			progressDialog= ProcessDialog.createDialog(MemberWealthCenterActivity.this, "正在加载中...");
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
				progressDialog.dismiss();
			}
		});
		return info;
	}

	private void initView() {
		// TODO Auto-generated method stub

		   login_title = (TextView)findViewById(R.id.login_title);
		   investor_add_view=(View)findViewById(R.id.investor_add_view);
		   add_list_view=(View)findViewById(R.id.add_list_view);

		   add_listView=(ListView)findViewById(R.id.add_listView);
		   AddOrder_no = (TextView)findViewById(R.id.AddOrder_no);
		   AddOrder_layout=(LinearLayout)findViewById(R.id.AddOrder_layout);
		   investorAdd_layout=(LinearLayout)findViewById(R.id.investorAdd_layout);
		   investorAdd=(TextView)findViewById(R.id.investorAdd);
		   AddOrder=(TextView)findViewById(R.id.AddOrder);
		   AddOrderView=(View)findViewById(R.id.AddOrderView);
		   investorAddView=(View)findViewById(R.id.investorAddView);

		   
		   back=findViewById(R.id.back);
		   catelogView=(LinearLayout)findViewById(R.id.catelogView);
		   recommend_cfmpEncode=(ImageView)findViewById(R.id.recommend_cfmpEncode);
//		   if(Create2DCode(userBean)!=null){
//			   recommend_cfmpEncode.setImageBitmap(Create2DCode(userBean));
//		   }
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
	    		titlePopup.addAction(new ActionItem(this, "退出工作室"));
	    		if(wealthBean.getAuditStatus()==2){
	    			login_title.setText("审核中");
	    		}
	    		else if(wealthBean.getAuditStatus()==4){ //财富师工作室审核未通过。。。。此功能未完成，，等待PC
	    			login_title.setText("审核中");
	    			dialog = new AlertDialog.Builder(this).create();
	    			dialog.setCanceledOnTouchOutside(false);
	    			view = LayoutInflater.from(this).inflate(R.layout.member_not_pass, null);
	    			text1 =(TextView) view.findViewById(R.id.text1);
	    			text2 =(TextView) view.findViewById(R.id.text2);
	    			ok = (Button) view.findViewById(R.id.ok);
	    			ok.setOnClickListener(new OnClickListener() {
	    					@Override
	    					public void onClick(View arg0) {
	    						// TODO Auto-generated method stub   						
	    						dialog.dismiss();
	    						Intent intent = new Intent(MemberWealthCenterActivity.this, UserCenterActivity.class);
	    						startActivity(intent);
	    					}
	    				});
	    			
	    			dialog.setView(view);
	    			dialog.show();
	    		}
	    		
	    		
	    		
	    		titlePopup.setItemOnClickListener(new MyOnItemOnClickListener()); //退出工作室
	    
	}
	
	public class MyOnItemOnClickListener implements OnItemOnClickListener { //退出工作室

		@Override
		public void onItemClick(ActionItem item, int position){ 
			dialog = new AlertDialog.Builder(context).create();				 
			 dialog.setCanceledOnTouchOutside(false);				 
				view = LayoutInflater.from(context).inflate(R.layout.sign_out_workspace, null);
				text = (TextView)view.findViewById(R.id.text);
				sure = (Button) view.findViewById(R.id.sure);
				cancel = (Button)view.findViewById(R.id.cancel);
				sure.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							signOut();
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
	
	private void signOut() { //退出工作室
		// TODO Auto-generated method stub
	 StringEntity entity=null;
   	 JSONObject json=null;
   	 try{
   		 if(wealthBean!=null&&wealthBean.getStudioId()!=null){
				  json = new JSONObject();
				  json.put("studioId", wealthBean.getStudioId());
				  json.put("quitReason", "");
				  entity = new StringEntity(json.toString(),"utf-8");
				  Log.i("entity", entity+"");
   		 }
   	 }catch(Exception e){
   		 e.printStackTrace();
   	 }
   	 client.post(MemberWealthCenterActivity.this, HttpConfig.URL_MEMBER_OUT_SPACE, entity, "application/json", new JsonHttpResponseHandler(){
   		 @Override
   		public void onSuccess(int statusCode, Header[] headers,
   				JSONObject response) {
   			// TODO Auto-generated method stub
   			 Log.i("response", response+"");
   			try {
				JSONObject obj = response.getJSONObject("result");
				boolean flag = response.getBoolean("success");
				String errCode = obj.getString("errorCode");
				if (flag && errCode.equals("success")) {
					 Bundle bundle = new Bundle();
					 bundle.putSerializable("wealthBean", wealthBean);
					 Intent intent = new Intent(MemberWealthCenterActivity.this,UserCenterActivity.class);
					 intent.putExtras(bundle);
					 startActivity(intent);
					 MemberWealthCenterActivity.this.finish();
					Toast.makeText(MemberWealthCenterActivity.this, "财富工作室退出成功", Toast.LENGTH_SHORT).show();
					dialog.dismiss();
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
			progressDialog.dismiss();
		}
   	 });
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
	
	   public class BackOnClickListener implements OnClickListener{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MemberWealthCenterActivity.this.finish();
//		        overridePendingTransition(
//				android.R.anim.slide_in_left,
//				android.R.anim.slide_out_right);
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

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

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
//			AddOrderView.setBackgroundResource(0xff0066cc);
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
   	 client.post(MemberWealthCenterActivity.this, HttpConfig.URL_CFMP_MEMBERS, entity, "application/json", new JsonHttpResponseHandler(){
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
							 adapter = new CfmpMembersAdapter(MemberWealthCenterActivity.this, allMemberList);
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
					progressDialog.dismiss();
				}
   	 });
 }

}
