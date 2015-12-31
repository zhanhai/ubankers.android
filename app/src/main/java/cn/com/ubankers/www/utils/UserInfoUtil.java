package cn.com.ubankers.www.utils;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import cn.com.ubankers.www.authentication.controller.activity.ChooseRoleActivity;
import cn.com.ubankers.www.authentication.controller.activity.LoginActivity;
import cn.com.ubankers.www.authentication.controller.activity.MainActivity;
import cn.com.ubankers.www.http.ParseUtils;
import cn.com.ubankers.www.user.controller.activity.SettingActivity;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.model.UserNewBean;

import com.google.gson.reflect.TypeToken;

public class UserInfoUtil {

	/**
	 * save user information
	 * @param response
	 */
		public  static void saveUserInfo(Context context ,JSONObject response) {
					JSONObject obj;
					try {
						obj = response.getJSONObject("result");
						boolean flag = response.getBoolean("success");
						String errCode = obj.getString("errorCode");
						if (flag && errCode.equals("success")) {
							JSONObject json = obj.getJSONObject("info");
						    Type type = new TypeToken<UserNewBean>(){}.getType();
						     UserNewBean userNewBean =ParseUtils.parseUserInfo(json.toString(), type);
					         UserBean userBean = ParseUtils.parseUserBean(userNewBean,context);	    
						     //MyApplication.app.setUser(userBean);
						    // MyApplication.app.setClient(client);
							if (userBean != null) {
								Bundle bundle = new Bundle();
								Intent intent = new Intent();
								if(userBean!=null&&userBean.getUserRole()!=null&&!userBean.getUserRole().equals("tourist")){
									intent.setClass(context, MainActivity.class);
									ParseUtils.saveSharePreferences(context,json.toString());
								}else{
									intent.setClass(context, ChooseRoleActivity.class);
								}
								bundle.putSerializable("userBean", userBean);
								intent.putExtras(bundle);
								 context.startActivity(intent);
								((LoginActivity)context).finish();
								((SettingActivity)context).bExit = false;
							}
							Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}
}
