package cn.com.ubankers.www.widget;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.sns.model.ArticleBean;
import cn.com.ubankers.www.utils.HtmlTagHandler;
import cn.com.ubankers.www.utils.NetWorking;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class SharePopupWindow extends PopupWindow implements OnItemClickListener, PlatformActionListener {
	private TextView btn_take_photo, btn_pick_photo, btn_cancel,share_tv;  
    private View mMenuView;  
    private GridView gridview;
    private static final int SINANUM=0;
    private static final int WECHATMENTSNUM=1;
    private static final int WECHATNUM=2;
    private static final int QQNUM=3;
    private boolean sinaFlag=false;
    private boolean qqFlag=false;
    private int images[] = {R.drawable.logo_sinaweibo,R.drawable.friend,R.drawable.logo_wechat,R.drawable.logo_qq,};
    private String names[]={"新浪微博","朋友圈","微信好友","QQ"};
    private Context context;
    private Handler handler;
    private ArticleBean articleBean;
    private AsyncHttpClient client;

   public SharePopupWindow(final Context context,OnClickListener itemsOnClick, ArticleBean articleBean) {  
        super(context);  
        this.context = context;
        this.articleBean =articleBean;
        client = MyApplication.app.getClient(context);
        LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        mMenuView = inflater.inflate(R.layout.share_layout, null); 
        gridview = (GridView) mMenuView.findViewById(R.id.gridview);  
        btn_cancel = (TextView) mMenuView.findViewById(R.id.btn_cancel); 
        share_tv=(TextView) mMenuView.findViewById(R.id.share_tv);
        share_tv.setVisibility(View.VISIBLE);
        ArrayList<HashMap<String, Object>> item = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < images.length; i++) {
          HashMap<String, Object> map = new HashMap<String, Object>();
          map.put("itemImage", images[i]);
          map.put("itemName", names[i]);
            item.add(map);
            SimpleAdapter saImageItems = new SimpleAdapter(context, item,R.layout.gridview_items,new String[] { "itemImage", "itemName" }, new int[] { R.id.ItemImage, R.id.ItemName });  
            // 添加并且显示  
            gridview.setAdapter(saImageItems);  
            // 添加消息处理  
           gridview.setOnItemClickListener(this); 
       }
        btn_cancel.setOnClickListener(new OnClickListener() {  
  
            public void onClick(View v) {  
   
                dismiss();  
            }  
        }); 
        
      //  btn_pick_photo.setOnClickListener(itemsOnClick);  
       // btn_take_photo.setOnClickListener(itemsOnClick);  
        this.setContentView(mMenuView);  
        this.setWidth(LayoutParams.FILL_PARENT);  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        this.setFocusable(true); 
        this.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);  
        this.setBackgroundDrawable(dw);  
        mMenuView.setOnTouchListener(new OnTouchListener() {         
            public boolean onTouch(View v, MotionEvent event) {  
                  
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();  
                int y=(int) event.getY();  
                if(event.getAction()==MotionEvent.ACTION_UP){  
                    if(y<height){  
                        dismiss();  
                    }  
                }                 
                return true;  
            }  
        });  
        
      handler = new Handler(){
    	  public void handleMessage(Message msg) {
             /* if(sinaFlag||qqFlag){
            	  switch (msg.arg1) {
              case 1:
            	  Toast.makeText(context, "分享成功", 10000).show();
            	  break;
              case 2:
            	  Toast.makeText(context, "分享失败", 10000).show();
            	  break;
              case 3:
            	  Toast.makeText(context, "分享取消", 10000).show();
            	  break;
                 } 
              }*/
             }
        
    };
    }
  /*  public boolean handleMessage(Message paramMessage)
    {
      switch (paramMessage.arg1)
      {
      default:
      case 1:
    	  Toast.makeText(context, "分享成功", 10000).show();
    	  break;
      case 2:
    	  Toast.makeText(context, "分享失败", 10000).show();
    	  break;
      case 3:
    	  Toast.makeText(context, "分享取消", 10000).show();
    	  break;
      }
      while (true)
      {
        
        Toast.makeText(context, "分享成功", 10000).show();
        continue;
        Toast.makeText(context, "分享失败", 10000).show();
        continue;
        Toast.makeText(context, "分享取消", 10000).show();
        return false;
      }
    }*/
	@Override
	public void onCancel(Platform paramPlatform, int paramInt) {
		// TODO Auto-generated method stub
		 Message localMessage = new Message();
		    localMessage.what = 2;
		    localMessage.arg1 = 3;
		    localMessage.arg2 = paramInt;
		    localMessage.obj = paramPlatform;
		    handler.sendMessage(localMessage) ;
	}
	@Override
	public void onComplete(Platform paramPlatform, int paramInt, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		     Message localMessage = new Message();
		     localMessage.what = 2;
		     localMessage.arg1 = 1;
		     localMessage.arg2 = paramInt;
		     localMessage.obj = paramPlatform;
		     handler.sendMessage(localMessage) ;
	}
	@Override
	public void onError(Platform arg0, int paramInt, Throwable paramThrowable) {
		// TODO Auto-generated method stub
		 {
			    paramThrowable.printStackTrace();
			    Message localMessage = new Message();
			    localMessage.what = 2;
			    localMessage.arg1 = 2;
			    localMessage.arg2 = paramInt;
			    localMessage.obj = paramThrowable;
			    handler.sendMessage(localMessage) ;
			  }

	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub	
		String abstractString="";
		String title="";
		if(isNetworkAvailable(context) == true){	
		if(articleBean!=null&&articleBean.getAbstracting()!=null){
			abstractString = articleBean.getAbstracting();
		}else{
			abstractString = articleBean.getAbstracting().subSequence(0, 140).toString();
		}
		if(articleBean!=null&&articleBean.getTitle()!=null){
			title=articleBean.getTitle();
		}
		switch(arg2){	
		case SINANUM:
			shareUrlService();
			if(isInstalled(context,"com.sina.weibo")==false){
				Toast.makeText(context, "请安装新浪微博再分享给好友", 1).show();
			}else{
			  SinaWeibo.ShareParams sinaWeibo = new SinaWeibo.ShareParams();
			  sinaWeibo.setTitle(title+"@银板客");
			  sinaWeibo.text=(title+"@银板客"+HttpConfig.URl_SHARE_ARTICLE+articleBean.get_id());
			  sinaWeibo.setImageUrl(HttpConfig.HTTP_QUERY_URL+articleBean.getCover());
		      Platform localPlatform3 = ShareSDK.getPlatform(context, SinaWeibo.NAME);
		      localPlatform3.SSOSetting(true);
		      localPlatform3.setPlatformActionListener(this);
		      ShareSDK.removeCookieOnAuthorize(true);
		      localPlatform3.removeAccount();
		      localPlatform3.share(sinaWeibo);
		      sinaFlag=true;
			}
			break;
		case WECHATMENTSNUM:
			shareUrlService();
			if(isInstalled(context,"com.tencent.mm")==false){
				Toast.makeText(context, "请安装微信再分享到朋友圈", 1).show();
			}else{
			  Platform localPlatform2 = ShareSDK.getPlatform(context, WechatMoments.NAME);
			  WechatMoments.ShareParams wechatMoments = new WechatMoments.ShareParams();
			  wechatMoments.title = (title);
			  wechatMoments.text = (abstractString);
			  wechatMoments.url=HttpConfig.URl_SHARE_ARTICLE+articleBean.get_id();
			  wechatMoments.setImageUrl(HttpConfig.HTTP_QUERY_URL+articleBean.getCover());
			  wechatMoments.shareType =localPlatform2.SHARE_WEBPAGE;  
		      localPlatform2.setPlatformActionListener(this);
		      localPlatform2.SSOSetting(true);
		      //ShareSDK.removeCookieOnAuthorize(true);
		     // localPlatform2.removeAccount();
		      localPlatform2.share(wechatMoments);
		      //Toast.makeText(context, "分享成功wechatMoments", 10000).show();
			}
			break;
		case WECHATNUM:
			shareUrlService();
				if(isInstalled(context,"com.tencent.mm")==false){
					Toast.makeText(context, "请安装微信再分享给好友", 1).show();
				}else{	
			  Platform localPlatform1 = ShareSDK.getPlatform(context, Wechat.NAME);
			  Wechat.ShareParams wechat = new Wechat.ShareParams();
			  wechat.setTitle(title);	
			  wechat.setText(abstractString);
			  wechat.setUrl(HttpConfig.URl_SHARE_ARTICLE+articleBean.get_id());
			  wechat.setImageUrl(HttpConfig.HTTP_QUERY_URL+articleBean.getCover());
			  wechat.setShareType(localPlatform1. SHARE_WEBPAGE);
		      localPlatform1.SSOSetting(true);
		      localPlatform1.setPlatformActionListener(this);
		     // ShareSDK.removeCookieOnAuthorize(true);
		     // localPlatform1.removeAccount();
		      localPlatform1.share(wechat);
		     // Toast.makeText(context, "分享成功wechat", 10000).show();
				}
			break;
		case QQNUM:
			shareUrlService();
			if(isInstalled(context,"com.tencent.mobileqq")==false){
				Toast.makeText(context, "请安装qq再分享给好友", 1).show();
			}else{
			  QQ.ShareParams qq = new QQ.ShareParams();
			  qq.title = (title);
			  qq.text=(abstractString);
			  qq.titleUrl=HttpConfig.URl_SHARE_ARTICLE+articleBean.get_id();
			  qq.setImageUrl(HttpConfig.HTTP_QUERY_URL+articleBean.getCover());
		      Platform localPlatform4 = ShareSDK.getPlatform(context, QQ.NAME);
		      localPlatform4.SSOSetting(true);
		      localPlatform4.setPlatformActionListener(this);
		      ShareSDK.removeCookieOnAuthorize(true);
		      localPlatform4.removeAccount();
		      localPlatform4.share(qq);
		      qqFlag=true;
			}
			break;
	   }	
		}else{
			Toast.makeText(context, "当前网络不可用",Toast.LENGTH_SHORT).show();
		}
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
		public boolean isNetworkAvailable(Context context) {
	        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
	        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        
	        if (connectivityManager == null)
	        {
	            return false;
	        }
	        else
	        {
	            // 获取NetworkInfo对象
	            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
	            
	            if (networkInfo != null && networkInfo.length > 0)
	            {
	                for (int i = 0; i < networkInfo.length; i++)
	                {
	                    // 判断当前网络状态是否为连接状态
	                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
	                    {
	                        return true;
	                    }
	                }
	            }
	        }
	        return false;
	    }
		//分享的接口
		private void shareUrlService(){
			if(articleBean!=null&&articleBean.get_id()!=null){
			String shareUrl = HttpConfig.HTTP_QUERY_URL+"/sns/api/articles/"+articleBean.get_id()+"/share";
			client.get(shareUrl, new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					try {
						boolean flag = response.getBoolean("success");
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
} 
