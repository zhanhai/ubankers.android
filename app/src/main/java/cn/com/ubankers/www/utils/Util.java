package cn.com.ubankers.www.utils;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;

/**
 * @author yangyu
 *	功能描述：常量工具类
 */
public class Util {
	/**
	 * 得到设备屏幕的宽度
	 */
	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 得到设备屏幕的高度
	 */
	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * 得到设备的密度
	 */
	public static float getScreenDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	/**
	 * 把密度转换为像素
	 */
	public static int dip2px(Context context, float px) {
		final float scale = getScreenDensity(context);
		return (int) (px * scale + 0.5);
	}

	/** 获取屏幕的宽度 */
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	public String  TimeShifting(int tine){
		long sd=tine;  
        Date dat=new Date(sd);  
        GregorianCalendar gc = new GregorianCalendar();   
        gc.setTime(dat);  
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
        String sb=format.format(gc.getTime());  
		return sb;	
	}
	
	
	public static String getStringTime(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(time);
		return sdf.format(date);
	}
	
	public static String getStringTimedat(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date(time);
		return sdf.format(date);
	}

	
	public static long Intercept2(String str) {
		long n =(long) (Double.valueOf(str).doubleValue()*100);
	    return n;
	}
	
	
	public static double Intercept3(String str) {
		double n = (Double.valueOf(str).doubleValue()/100);
	    return n;
	}
	public static String Intercept(String str) {
		double n = (Double.valueOf(str).doubleValue() / 10000);
		DecimalFormat df = new DecimalFormat("#,###.####");
		String m = df.format(n);
		return m;
	}
	public static String Intercept1(String str) {
		double n = (Double.valueOf(str).doubleValue()/100);
		DecimalFormat df = new DecimalFormat("#,###.####");
		String m = df.format(n);
		return m;
	}
	
	/**
	 * 判断是否是两位小数
	 * 
	 * @param number
	 *            金额
	 * @return
	 */
	public static boolean isD(String number) {
		Pattern p = Pattern.compile("^(([1-9]\\d*)|\\d)(\\.\\d{1,2})?$");
		Matcher m = p.matcher(number);
		return m.matches();
	}
	
}
