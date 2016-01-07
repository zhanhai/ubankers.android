package com.ubankers.app.product.detail.share;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
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

import com.ubankers.app.product.detail.ProductDetailActivity;
import com.ubankers.app.product.model.Product;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.product.model.ProductDetail;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.utils.Tools;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


public class ProductShareDialog extends PopupWindow implements OnItemClickListener, PlatformActionListener {
	private TextView btn_take_photo, btn_pick_photo, btn_cancel;  
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
    private UserBean userBean;
    private Product productDetail;

   public ProductShareDialog(final ProductDetailActivity context) {
        super(context);  
        this.context = context;
        this.productDetail = context.getProduct();
        if(MyApplication.app.getUser()!=null){
			userBean=MyApplication.app.getUser();
		}
        LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        mMenuView = inflater.inflate(R.layout.share_layout, null); 
        gridview = (GridView) mMenuView.findViewById(R.id.gridview);  
        btn_cancel = (TextView) mMenuView.findViewById(R.id.btn_cancel);  
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
		String abstractString;
		if(isNetworkAvailable(context) == true){	
		if(productDetail.getProductId()!=null&& productDetail.getProductName()!=null){
		/*if(productDetail.getAbstracting().length()<140){
			abstractString = productDetail.getTitle();
		}else{
			abstractString = productDetail.getAbstracting().subSequence(0, 140).toString();
		}*/
		switch(arg2){	
		case SINANUM:
			if(isInstalled(context,"com.sina.weibo")==false){
				Toast.makeText(context, "请安装新浪微博再分享给好友", 1).show();
			}else{
			  SinaWeibo.ShareParams sinaWeibo = new SinaWeibo.ShareParams();
			  sinaWeibo.setTitle(productDetail.getProductName()+"@银板客");
			  String str = null;
			  if (productDetail.getCountProductRate() != null) {
					if (productDetail.getCountProductRate().equals("")
							|| productDetail.getCountProductRate().equals("-1")
							) {
							str = "浮动";
					} else {
						   str = productDetail.getCountProductRate()+"%";
					}
			  } else {
					str = "";
		      }
			  //新浪是直接把链接写在text里分享的
			  String text = "预期年化收益率:"+str+","+"产品期限:"+ productDetail.getProductTerm()+"个月,"+"起购金额:"+Tools.Intercept(productDetail.getMinSureBuyPrice())+"元";
			  sinaWeibo.text=text+HttpConfig.URL_INFROMATION+ productDetail.getProductId()+"?provide=weibo&cfmpId="+userBean.getUserId();
//			  if(userBean.getUserRole().equals("cfmp")){
//				  sinaWeibo.setUrl();
//			  }
			  if(productDetail.getFace()!=null&&!productDetail.getFace().equals("")){
				  	sinaWeibo.setImageUrl(HttpConfig.HTTP_IMAGE_QUERY_URL+ productDetail.getFace());
			  }
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
			if(isInstalled(context,"com.tencent.mm")==false){
				Toast.makeText(context, "请安装微信再分享到朋友圈", 1).show();
			}else{
			  WechatMoments.ShareParams wechatMoments = new WechatMoments.ShareParams();
			  wechatMoments.title = (productDetail.getProductName());
			  String str = null;
			  if (productDetail.getCountProductRate() != null) {
					if (productDetail.getCountProductRate().equals("")
							|| productDetail.getCountProductRate().equals("-1")
							) {
							str = "浮动";
					} else {
						   str = productDetail.getCountProductRate()+"%";
					}
			  } else {
					str = "";
		      }
			  
			  
			  String text=null;
			  String MinSureBuyPrice = productDetail.getMinSureBuyPrice();
			  if(MinSureBuyPrice==null||MinSureBuyPrice=="0"){
				  MinSureBuyPrice=0+"";	
				  text = "预期年化收益率:"+str+","+"产品期限:"+ productDetail.getProductTerm()+"个月,"+"起购金额:"+Tools.Intercept(MinSureBuyPrice)+"元";
			  }else{
				  text = "预期年化收益率:"+str+","+"产品期限:"+ productDetail.getProductTerm()+"个月,"+"起购金额:"+Tools.Intercept(productDetail.getMinSureBuyPrice())+"元";
			  }
			  
//			  String text = "预期年化收益率:"+str+","+"产品期限:"+productDetail.getProductTerm()+"个月,"+"起购金额:"+Tools.Intercept(productDetail.getMinSureBuyPrice())+"元";
			  wechatMoments.text = text;
			  if(userBean.getUserRole().equals("cfmp")){
				  String url = HttpConfig.URL_INFROMATION+ productDetail.getProductId()+"?provide=wx&cfmpId="+userBean.getUserId();
				  wechatMoments.url=url;
			  }
			  if(productDetail.getFace()!=null&&!productDetail.getFace().equals("")){
				  	wechatMoments.setImageUrl(HttpConfig.HTTP_IMAGE_QUERY_URL+ productDetail.getFace());
			  }
              wechatMoments.shareType =Platform.SHARE_WEBPAGE;
		      Platform localPlatform2 = ShareSDK.getPlatform(context, WechatMoments.NAME);
		      localPlatform2.setPlatformActionListener(this);
		      localPlatform2.SSOSetting(true);
		      localPlatform2.share(wechatMoments);
//		      Toast.makeText(context, "分享成功wechatMoments", 1).show();
			}
			break;
		case WECHATNUM:
				if(isInstalled(context,"com.tencent.mm")==false){
					Toast.makeText(context, "请安装微信再分享给好友", 1).show();
				}else{	
				  Wechat.ShareParams wechat = new Wechat.ShareParams();
				  wechat.title = (productDetail.getProductName());
				  String str = null;
				  if (productDetail.getCountProductRate() != null) {
						if (productDetail.getCountProductRate().equals("")
								|| productDetail.getCountProductRate().equals("-1")
								) {
								str = "浮动";
						} else {
							   str = productDetail.getCountProductRate()+"%";
						}
				  } else {
						str = "";
			      }
				  
				  
				  String text=null;
				  String MinSureBuyPrice = productDetail.getMinSureBuyPrice();
				  if(MinSureBuyPrice==null||MinSureBuyPrice=="0"){
					  MinSureBuyPrice=0+"";	
					  text = "预期年化收益率:"+str+","+"产品期限:"+ productDetail.getProductTerm()+"个月,"+"起购金额:"+Tools.Intercept(MinSureBuyPrice)+"元";
				  }else{
					  text = "预期年化收益率:"+str+","+"产品期限:"+ productDetail.getProductTerm()+"个月,"+"起购金额:"+Tools.Intercept(productDetail.getMinSureBuyPrice())+"元";
				  }
				  
				  
//				  String text = "预期年化收益率:"+str+","+"产品期限:"+productDetail.getProductTerm()+"个月,"+"起购金额:"+Tools.Intercept(productDetail.getMinSureBuyPrice())+"元";
				  wechat.text = text;
				  if(userBean.getUserRole().equals("cfmp")){
					  String url = HttpConfig.URL_INFROMATION+ productDetail.getProductId()+"?provide=wx&cfmpId="+userBean.getUserId();
					  wechat.url=url;
				  }
				  if(productDetail.getFace()!=null&&!productDetail.getFace().equals("")){
					  wechat.setImageUrl(HttpConfig.HTTP_IMAGE_QUERY_URL+ productDetail.getFace());
				  }
				  wechat.shareType =Platform.SHARE_WEBPAGE;
			      Platform localPlatform1 = ShareSDK.getPlatform(context, Wechat.NAME);
			      localPlatform1.SSOSetting(true);
			      localPlatform1.setPlatformActionListener(this);
			      localPlatform1.share(wechat);
//		      Toast.makeText(context, "分享成功wechat", 1).show();
				}
			break;
		case QQNUM:
			if(isInstalled(context,"com.tencent.mobileqq")==false){
				Toast.makeText(context, "请安装qq再分享给好友", 1).show();
			}else{
			  QQ.ShareParams qq = new QQ.ShareParams();
			  qq.title = (productDetail.getProductName());
			  String str = null;
			  if (productDetail.getCountProductRate() != null) {
					if (productDetail.getCountProductRate().equals("")
							|| productDetail.getCountProductRate().equals("-1")
							) {
							str = "浮动";
					} else {
						   str = productDetail.getCountProductRate()+"%";
					}
			  } else {
					str = "";
		      }
			  String text=null;
			  String MinSureBuyPrice = productDetail.getMinSureBuyPrice();
			  if(MinSureBuyPrice==null||MinSureBuyPrice=="0"){
				  MinSureBuyPrice=0+"";	
				  text = "预期年化收益率:"+str+","+"产品期限:"+ productDetail.getProductTerm()+"个月,"+"起购金额:"+Tools.Intercept(MinSureBuyPrice)+"元";
			  }else{
				  text = "预期年化收益率:"+str+","+"产品期限:"+ productDetail.getProductTerm()+"个月,"+"起购金额:"+Tools.Intercept(productDetail.getMinSureBuyPrice())+"元";
			  }
			 
			  qq.text=text;
			  if(userBean.getUserRole().equals("cfmp")){
				  String url = HttpConfig.URL_INFROMATION+ productDetail.getProductId()+"?provide=QQ&cfmpId="+userBean.getUserId();
//				  Log.e("url=================", url);
				  qq.titleUrl=url;
			  }
			  qq.setImageUrl(HttpConfig.HTTP_IMAGE_QUERY_URL+ productDetail.getFace());
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
} 
