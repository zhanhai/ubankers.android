package cn.com.ubankers.www.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class InstallAppUtils {

	/**
	 * 判断应用是否已安装 
	 * @param context
	 * @param packageName
	 * @return
	 */ 
	 public static boolean isInstalled(Context context, String packageName) { 
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
	 public static boolean isRunning(Context context, String packageName) { 
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
}
