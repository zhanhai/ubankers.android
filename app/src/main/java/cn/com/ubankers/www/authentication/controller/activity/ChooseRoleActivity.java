package cn.com.ubankers.www.authentication.controller.activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.authentication.model.UserQuestBean;
import cn.com.ubankers.www.authentication.view.SelectRoleAdapter;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.RoleBean;
import cn.com.ubankers.www.user.model.UserBean;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;



public class ChooseRoleActivity extends Activity{
	private SelectRoleAdapter roleAdapter;
	public static Context context;
	private ArrayList<cn.com.ubankers.www.user.model.RoleBean> listString;
	private ListView listView;	
	private UserBean userBean;
	private RoleBean roleBean;
    private AsyncHttpClient client;
	private UserQuestBean request;
	private String role="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		MyApplication.getInstance().addActivity(this);
		context=this;	
		Intent intent =this.getIntent();
		Bundle bundle =intent.getExtras();
		if(bundle!=null){
		userBean =(UserBean) bundle.getSerializable("userBean");
		}
		client = MyApplication.app.getClient(context);
		showRoleDialog();				
	}
	
	public void showRoleDialog(){
		View roleListView=LayoutInflater.from(this).inflate(R.layout.role_list, null);
		listView=(ListView)roleListView.findViewById(R.id.list);
		listView.setOnItemClickListener(new RoleOnItemClickListener());
		listString = new ArrayList<RoleBean>();			
			if(listView!=null){
				for(int i=0; i<3;i++){
				RoleBean roleBean  = new RoleBean();
				roleBean.setType(i);
				listString.add(roleBean);
				}
				roleAdapter = new SelectRoleAdapter(context,listString);
				listView.setAdapter(roleAdapter);					
		}
		
		new AlertDialog.Builder(ChooseRoleActivity.this)
		.setView(roleListView)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {								
			    setRole(roleBean.getType()+1);
			    Intent intent = new Intent(ChooseRoleActivity.this,MainActivity.class);
			    startActivity(intent);
			    ChooseRoleActivity.this.finish();			
			}			
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			    Intent intent = new Intent(ChooseRoleActivity.this,MainActivity.class);
				startActivity(intent);
				ChooseRoleActivity.this.finish();				
			}
		})
		.setCancelable(false)
		.create().show();				
	}
	private void setRole(int type){
		StringEntity entity =null;
		JSONObject json = new JSONObject();
		try{
			json.put("userId","");
			json.put("catalog","product");
			json.put("name","role");
			json.put("value",type);
			entity = new StringEntity(json.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		client.post(ChooseRoleActivity.this, HttpConfig.URL_USEREXTEBD, entity, "application/json", new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				JSONObject obj;
				try {
					obj = response.getJSONObject("result");
			    	boolean flag = response.getBoolean("success");
			    	String errCode = obj.getString("errorCode");
				   if (flag && errCode.equals("success")) {
					    Bundle bundle = new Bundle();
						Intent intent = new Intent();
						intent.setClass(context, MainActivity.class);
						bundle.putSerializable("userBean", userBean);
						intent.putExtras(bundle);
						MyApplication.app.setUser(userBean);
						ChooseRoleActivity.this.startActivity(intent);
						ChooseRoleActivity.this.finish();
				   }
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			   }
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
			}
			
		});
		
	}

	public class RoleOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			roleBean =listString.get(arg2) ;
		    userBean=MyApplication.app.getUser();
		    if(roleBean.getType()==0){
		    	userBean.setUserRole("fundraiser");
		    }else if(roleBean.getType()==1){
		    	userBean.setUserRole("cfmp");
		    }else if(roleBean.getType()==2){
		    	userBean.setUserRole("investor");
		    }
		}
	}

}
