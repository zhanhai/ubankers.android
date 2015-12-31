package cn.com.ubankers.www.user.controller.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.zxing.WriterException;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.utils.EncodingHandler;
import cn.com.ubankers.www.utils.PhotoUtil;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CfmpRecommerActivity extends Activity  implements OnItemClickListener, PlatformActionListener,OnClickListener{
	private GridView recommend_gridview;
	private int images[] = {R.drawable.logo_sinaweibo,R.drawable.friend,R.drawable.logo_wechat,R.drawable.logo_qq,};
	private String names[]={"新浪微博","朋友圈","微信好友","QQ"};
    private static final int WECHATNUM=2;
    private static final int WECHATMENTSNUM=1;
    private static final int SINANUM=0;
    private static final int QQNUM=3;
	private static final int MSG_TOAST = 1;
	private static final int MSG_ACTION_CCALLBACK = 2;
	private static final int MSG_CANCEL_NOTIFY = 3;
	private Context context;
	private ClipboardManager mClipboard = null;
	private UserBean userBean;
	private String recommenderUrl ="" ;
	private ImageView recommend_cfmpEncode;
	private TextView cancel,copy_linked;
	private String encryptUserId;
	private Bitmap qrCodeBitmap;
	private LinearLayout back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		ShareSDK.initSDK(context);
		this.setContentView(R.layout.recommend_cfmp);
		if(MyApplication.app.getUser()!=null){
			userBean=MyApplication.app.getUser();
		}
		Create2DCode(userBean);
		initView();
		MyApplication.getInstance().addActivity(this);
	}
	private Bitmap Create2DCode(UserBean userBean){
		String ZxingString =userBean.getUserId()+"|"+userBean.getUserMobile();
	    byte[]bytes=ZxingString.toString().getBytes();
	    encryptUserId= Base64.encodeToString(bytes,Base64.NO_WRAP);
		if (encryptUserId!=null&&!encryptUserId.equals("")) {
				//根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
				try {
				   qrCodeBitmap = EncodingHandler.createQRCode(HttpConfig.URL_RECOMMOND_CFMP+encryptUserId, 350);
				} catch (WriterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				Toast.makeText(context, "推荐人连接为空", Toast.LENGTH_SHORT).show();
			}
		return qrCodeBitmap;
	}
   private void initView(){
	   cancel=(TextView)findViewById(R.id.recommend_cancel);
	   copy_linked=(TextView)findViewById(R.id.copy_linked);
	   back=(LinearLayout)findViewById(R.id.title_bar_back_btn);
	   recommend_cfmpEncode=(ImageView)findViewById(R.id.recommend_cfmpEncode);
	   if(Create2DCode(userBean)!=null){
		   recommend_cfmpEncode.setImageBitmap(Create2DCode(userBean));
	   }
	   recommend_gridview = (GridView) findViewById(R.id.recommend_gridview);
	   copy_linked.setOnClickListener(this);
	   cancel.setOnClickListener(new BackOnClickListener());	
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
   }
   
   public class BackOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			CfmpRecommerActivity.this.finish();
	        overridePendingTransition(
			android.R.anim.slide_in_left,
			android.R.anim.slide_out_right);
		}
	}
   
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub	
		recommenderUrl =HttpConfig.URL_RECOMMOND_CFMP+encryptUserId;
		switch(arg2){	
		case WECHATNUM:
				if(isInstalled(context,"com.tencent.mm")==false){
					Toast.makeText(context, "请安装微信再分享给好友", 1).show();
				}else{	
					  Wechat.ShareParams wechat = new Wechat.ShareParams();
					  wechat.title = ("Let's be Ubankers");
					  wechat.text = ("带您在有品、有趣料、有钱赚的OFIS平台创建金融新世界,邀您做财富师邀请您成为银板客财富师链接");
					  wechat.url=recommenderUrl;
					  wechat.shareType =Platform.SHARE_WEBPAGE;
					  wechat.setImageData(PhotoUtil.drawableToBitmap(getResources().getDrawable(R.drawable.icon)));
				      Platform localPlatform1 = ShareSDK.getPlatform(context, Wechat.NAME);
				      localPlatform1.SSOSetting(true);
				      localPlatform1.setPlatformActionListener(this);
				      localPlatform1.share(wechat);
				     // Toast.makeText(context, "分享成功wechat", 10000).show();
				}
			break;
		case WECHATMENTSNUM:
			if(isInstalled(context,"com.tencent.mm")==false){
				Toast.makeText(context, "请安装微信再分享到朋友圈", 1).show();
			}else{
			  WechatMoments.ShareParams wechatMoments = new WechatMoments.ShareParams();
			  wechatMoments.title = ("Let's be Ubankers");
			  wechatMoments.text = ("邀请您成为银板客财富师链接");
			  wechatMoments.url=recommenderUrl;
			  wechatMoments.shareType =Platform.SHARE_WEBPAGE;
			  wechatMoments.setImageData(PhotoUtil.drawableToBitmap(getResources().getDrawable(R.drawable.icon)));
		      Platform localPlatform2 = ShareSDK.getPlatform(context, WechatMoments.NAME);
		      localPlatform2.setPlatformActionListener(this);
		      localPlatform2.SSOSetting(true);
		      localPlatform2.share(wechatMoments);
		      //Toast.makeText(context, "分享成功wechatMoments", 10000).show();
			}
			break;
			
		case SINANUM:
			if(isInstalled(context,"com.sina.weibo")==false){
				Toast.makeText(context, "请安装新浪微博再分享给好友", 1).show();
			}else{
			  SinaWeibo.ShareParams sinaWeibo = new SinaWeibo.ShareParams();
			  sinaWeibo.setTitle("Let's be Ubankers");
			  //新浪是直接把链接写在text里分享的
			  sinaWeibo.text=("邀请您成为银板客财富师链接"+recommenderUrl);
//			  String path = "file:///android_asset/icon.png";
//			  sinaWeibo.imageUrl=path;
//			  sinaWeibo.setUrl(recommenderUrl);
//			  sinaWeibo.setImageData(PhotoUtil.drawableToBitmap(getResources().getDrawable(R.drawable.icon)));
//			  sinaWeibo.setImageData(PhotoUtil.getRes("icon", context));
//			  sinaWeibo.setImagePath(arg0);
		      Platform localPlatform3 = ShareSDK.getPlatform(context, SinaWeibo.NAME);
		      localPlatform3.SSOSetting(true);
		      localPlatform3.setPlatformActionListener(this);
		      ShareSDK.removeCookieOnAuthorize(true);
		      localPlatform3.removeAccount();
		      localPlatform3.share(sinaWeibo);
			}
			break;
		case QQNUM:
			if(isInstalled(context,"com.tencent.mobileqq")==false){
				Toast.makeText(context, "请安装qq再分享给好友", 1).show();
			}else{
			  QQ.ShareParams qq = new QQ.ShareParams();
			  qq.title = ("Let's be Ubankers");
			  qq.text=("带您在有品、有趣料、有钱赚的OFIS平台创建金融新世界,邀您做财富师邀请您成为银板客财富师链接");
			  qq.titleUrl=recommenderUrl;
			  String path = "file:///android_asset/icon.png";
			  qq.setImageUrl(path);
//			  qq.setImagePath(path);
//			  qq.setImageData(PhotoUtil.drawableToBitmap(context.getResources().getDrawable(R.drawable.icon)));
//			  Bitmap bmp=BitmapFactory.decodeResource(context.getResources(), R.drawable.icon);
//			  qq.setImageData(bmp);
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
		// Handler.sendMessage(msg, ShareActivity.this);

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
		// UIHandler.sendMessage(msg,this);

	}

	@Override
	public void onError(Platform platform, int action, Throwable t) {
		// 失敗
		// 打印错误信息,print the error msg
		t.printStackTrace();
		// 错误监听,handle the error msg
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = t;
		// Handler.sendMessage(msg, this);

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
	 * 
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
		 * 
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
		@SuppressLint("NewApi")
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch(arg0.getId()){
			case R.id.copy_linked:
			    mClipboard = (ClipboardManager) getSystemService(context.CLIPBOARD_SERVICE);
			    recommenderUrl =HttpConfig.URL_RECOMMOND_CFMP+encryptUserId;
			    mClipboard.setText(recommenderUrl.toString());
			    Toast.makeText(CfmpRecommerActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
			   break;
			}
		
		}
}
