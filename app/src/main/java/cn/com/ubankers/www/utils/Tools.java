package cn.com.ubankers.www.utils;


import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.LinearLayout;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.http.HttpConfig;

import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.PersistentCookieStore;


public class Tools   {
	private static int oldPosition = 0; // 上一次页面的位置
	private static int currentItem = 0; // 当前图片的索引号
	private static View view;
	/**
	 * 检查是否存在sdcard
	 * */
	public static boolean hasSdcard(){
		String state=Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)){
			return false;
		}else {
			return false;
		}		
	}
	/**
	 * 
	 * @param time 要转换的utc格式的时间
	 * @return 返回日期格式的时间
	 */
	public static String getDate(String time){
		String str=null;
		Calendar cal = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
	 	Date date;
		try {
			date = (Date)format.parse(time.trim());
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.HOUR, +8);
		 	str = format.format(cal.getTime());
		 	return str;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
		
	}
	/**
	 * 
	 * @param time 要转换的utc格式的时间
	 * @return 返回日期格式的时间
	 */
	public static String getDateM(String time){
		String str=null;
		Calendar cal = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
	 	Date date;
		try {
			date = (Date)format.parse(time.trim());
			format = new SimpleDateFormat("yyyy-MM-dd");
			cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.HOUR, +8);
		 	str = format.format(cal.getTime());
		 	return str;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
		
	}
	/**
	 * 
	 * @param time 要转换的时间戳
	 * @return 返回日期格式的时间
	 */
	public static String getDateUtil(String time){
		String time_date=null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(Long.parseLong(time));
		time_date= formatter.format(calendar.getTime());
		return time_date;
	}
	/**
	 * 
	 * @param time 要转换的时间戳
	 * @return 返回日期格式的时间
	 */
	public static String getDateUtil1(String time){
		String time_date=null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(Long.parseLong(time));
		time_date= formatter.format(calendar.getTime());
		return time_date;
	}
	/**
	* 是否是合法的手机号
	* 
	* @param context
	* @return
	*/
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(16[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	/**
	 * 
	 * @param current 当前输入的数字
	 * @param min 最小值
	 * @param max 最大值
	 * @return 判断数字在某个范围内
	 */
	public static boolean rangeInDefined(int current, int min, int max)  
    {  
        return Math.max(min, current) == Math.min(current, max);  
    }
	/**
	 * 
	 * @param current 当前输入的数字
	 * @param min 最小值
	 * @param max 最大值
	 * @return 判断数字在某个范围内
	 */
	public static boolean rangeLongDefined(long current, long min, long max)  
    {  
        return Math.max(min, current) == Math.min(current, max);  
    } 
	/**
	 * 把金额以万为单位来计算
	 * @param str
	 * @return
	 */
	public static String Intercept(String str){
		double n= (Double.valueOf(str).doubleValue()/10000);
		DecimalFormat df = new DecimalFormat("#,###.####");
		String m = df.format(n);
		return m;
	}
	/**
	 * 把金额分割
	 * @param str
	 * @return
	 */
	public static String InterceptTo(String str){
		double n= (Double.valueOf(str).doubleValue());
		DecimalFormat df = new DecimalFormat("#,###.####");
		String m = df.format(n);
		return m;
	}
	/** 
	  * 设置webView的cookie 
	  */  
	 public static void synCookies(Context context, String url) {
//		  CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
//		  cookieSyncManager.sync();
		  CookieManager cookieManager = CookieManager.getInstance();
		  cookieManager.setAcceptCookie(true); 
		  cookieManager.removeAllCookie(); 
		  cookieManager.setCookie(url, "__GSID__="+getCookieClient(context));
		  CookieSyncManager.getInstance().sync();
    }

	 /**
	  * 获取标准Cookie
	  */
	/* public static String getCookie(Context context){
		 String value = "";
		 PersistentCookieStore myCookieStore = new PersistentCookieStore(context);  
	     List<Cookie> cookies = myCookieStore.getCookies();  
	     for (Cookie cookie : cookies) {
	    	 if(cookie.getDomain().equals("uat.ubankers.com")){
	    		 if(cookie.getName().equals("__GSID__")){
	    			 value = cookie.getValue();
	    		 }
	    	 }else if(cookie.getDomain().equals("www.ubankers.com")){
	    		 if(cookie.getName().equals("__GSID__")){
	    			 value = cookie.getValue();
	    		 }
	    	 }
	     }
	     Log.e(".....................", value);
		 SharedPreferences preferences = context.getSharedPreferences("header", Context.MODE_PRIVATE);   
	     if(!preferences.getString("__GSID__", "").equals("")){
	    	 value = preferences.getString("__GSID__", "");
	    	 
	     }
	     return value;
	 }*/
	 /**
	  * 获取标准Cookie
	  */
	 private static String getCookieClient(Context context){
		 String value = null;
		 PersistentCookieStore myCookieStore = new PersistentCookieStore(context);  
	     List<Cookie> cookies = myCookieStore.getCookies();  
	     for (Cookie cookie : cookies) {
	    	 if(cookie.getDomain().equals(HttpConfig.UDOMAIN)){
	    		 if(cookie.getName().equals("__GSID__")){
	    			 value = cookie.getValue();
	    		 }
	    	 }else if(cookie.getDomain().equals(HttpConfig.WDOMAIN)){
	    		 if(cookie.getName().equals("__GSID__")){
	    			 value = cookie.getValue();
	    		 }
	    	 }
	     }
	     return value;
	 }
	 
	/* *//** 
	     * 获取标准 Cookie   
	  *//*  
	 public static String getCookieText(Context context) {  
	    	 PersistentCookieStore myCookieStore = new PersistentCookieStore(context);  
		     List<Cookie> cookies = myCookieStore.getCookies();  
		     for (Cookie cookie : cookies) {  
		     }  
	         StringBuffer sb = new StringBuffer();  
		     for (int i = 0; i < cookies.size(); i++) {  
		             Cookie cookie = cookies.get(i);  
		             if(cookie.toString().contains("121.43.72.190")){
		             String cookieName = cookie.getName();  
		             String cookieValue = cookie.getValue();
		             Log.e("jdjdjjdj", cookie.getName() + " = " + cookie.getValue()); 
		            SharedPreferences sharedPreferences = context.getSharedPreferences("token",Context.MODE_PRIVATE);
		 			SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                     String domain=cookie.getDomain();   
		            if (!TextUtils.isEmpty(cookieName)  
		                    && !TextUtils.isEmpty(cookieValue)) {  
		                sb.append(cookieName + "=");  
		                sb.append(cookieValue + ";");  
		                sb.append("domain=" +domain);  
		            } 
		            editor.putString("sessionId ",sb.toString() );
		 			editor.commit();//提交修改
		     }  
		     }
		     return sb.toString();  
	    } */
	 /**
	　　* 获取版本号
	　　* @return 当前应用的版本号
	　　*/
	 public static String getVersion(Context context) {
		String version = null;
	    try {
	           PackageManager manager = context.getPackageManager();
	           PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
	           version = info.versionName;
			   return version;
	 	} catch (Exception e) {
		    e.printStackTrace();
		}
		return null;
	 }	
	 /**
		 * 获取开始时点数的位置
		 * 
		 * @param fragments
		 */
		public static View getDot(Context context, int position) {
			view = getDotWidget(context, position);
			if (oldPosition == position) {
				view.setBackgroundResource(R.drawable.dot_normal);
			} else {
				view.setBackgroundResource(R.drawable.dot_focused);
			}
			return view;
		}
		/**
		 * 获取开始时点数的位置
		 * 
		 * @param fragments
		 */
		public static View getDot1(Context context, int position) {
			view = getDotWidget(context, position);
			if (oldPosition == position) {
				view.setBackgroundResource(R.drawable.dot_normal);
			} else {
				view.setBackgroundResource(R.drawable.dot1_focused);
			}
			return view;
		}

		/**
		 * 获取滑动时点数的位置
		 * 
		 * @param fragments
		 */
		public static View getDotPager(Context context, int position, int pagePosition) {
			oldPosition = pagePosition;
			currentItem = pagePosition;
			view = getDotWidget(context, position);
			if (position == oldPosition || currentItem == position) {
				view.setBackgroundResource(R.drawable.dot_normal);
			} else {
				view.setBackgroundResource(R.drawable.dot_focused);
			}
			return view;
		}
		/**
		 * 获取滑动时点数的位置
		 * 
		 * @param fragments
		 */
		public static View getDotPager1(Context context, int position, int pagePosition) {
			oldPosition = pagePosition;
			currentItem = pagePosition;
			view = getDotWidget(context, position);
			if (position == oldPosition || currentItem == position) {
				view.setBackgroundResource(R.drawable.dot_normal);
			} else {
				view.setBackgroundResource(R.drawable.dot1_focused);
			}
			return view;
		}
		/**
		 * 获取点数控件
		 * 
		 * @param fragments
		 */
		public static View getDotWidget(Context context, int position) {
			int width =Util.getScreenWidth(context);
			view = new View(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width/50, width/50);
			params.setMargins(5, 0, 5, 0);
			view.setLayoutParams(params);
			return view;
		}
		//使用此函数来获取 SSLSocketFactory
		public static SSLSocketFactory getSocketFactory(Context context) {
				        // TODO Auto-generated method stub
				        SSLSocketFactory sslFactory = null;
				        try {
				            KeyStore keyStore = KeyStore.getInstance("BKS");
				            InputStream instream = context.getResources().openRawResource(
				                    R.raw.certstore);
				            keyStore.load(instream, "123456".toCharArray());
				            sslFactory = new MySSLSocketFactory(keyStore);
				        } catch (KeyStoreException e1) {
				            // TODO Auto-generated catch block
				            e1.printStackTrace();
				        } catch (NoSuchAlgorithmException e1) {
				            // TODO Auto-generated catch block
				            e1.printStackTrace();
				        } catch (CertificateException e1) {
				            // TODO Auto-generated catch block
				            e1.printStackTrace();
				        } catch (IOException e1) {
				            // TODO Auto-generated catch block
				            e1.printStackTrace();
				        } catch (UnrecoverableKeyException e1) {
				            // TODO Auto-generated catch block
				            e1.printStackTrace();
				        } catch (KeyManagementException e) {
				            // TODO Auto-generated catch block
				            e.printStackTrace();
				        }
				        return sslFactory;
		}
			
			
}


