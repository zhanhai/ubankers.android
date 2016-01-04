package cn.com.ubankers.www.user.controller.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.MembersBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.model.WealthBean;
import cn.com.ubankers.www.user.model.WorkRecordBean;
import cn.com.ubankers.www.user.view.WorkRecordAdapter;
import cn.com.ubankers.www.widget.CircleImg;
import cn.com.ubankers.www.widget.ProcessDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WorkspaceMemberActivity extends Activity implements OnClickListener{
	private Context context;
	private AsyncHttpClient client;
	private UserBean userBean;
	private ProcessDialog myDialog;
	private WealthBean wealthBean;
	private MembersBean memberBean;
	public  CircleImg memberImageView;
	public TextView memberNameView,memberMobileView,memberDateView,tv4,namenew;
	public Button deleteMember;
	public LinearLayout back;
	private int currentPosition;
	private AlertDialog dialog;
	private View view;
	private TextView name;
	private Button sure,cancel;
	private ListView workRecordlistView;
	private WorkRecordAdapter adapter;
	private List<WorkRecordBean> recordList;
	private List<MembersBean> memberList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.workspace_member_activity);
		context = this;
		client = MyApplication.app.getClient(context);
		if(MyApplication.app.getUser()!=null){
			userBean = MyApplication.app.getUser();
		}
		if(myDialog==null){
		   myDialog= ProcessDialog.createDialog(WorkspaceMemberActivity.this, "正在加载中...");
		}		
		Intent intent =this.getIntent();
		currentPosition = intent.getIntExtra("position", 0);
		if(intent!=null){
			wealthBean = (WealthBean) intent.getSerializableExtra("wealthBean");
			memberBean = (MembersBean)intent.getSerializableExtra("memberBean");
		}
		
		initView();
		intdata();
	}

	private void intdata() {
		// TODO Auto-generated method stub
		myDialog.show();
   	 memberList = new ArrayList<MembersBean>();
   	 StringEntity entity=null;
   	 JSONObject json=null;
   	 try{
   		 if(memberBean!=null&&memberBean.getUserId()!=null){
				  json = new JSONObject();
				  json.put("cfmpId", memberBean.getUserId());
				  json.put("pageIndex", 0);
				  json.put("pageCount", 10);
				  entity = new StringEntity(json.toString(),"utf-8");
   		 }
   	 }catch(Exception e){
   		 e.printStackTrace();
   	 } 
   	 client.post(WorkspaceMemberActivity.this, HttpConfig.URL_LEADER_FIND_MEMBER, entity, "application/json", new JsonHttpResponseHandler(){
   		 @Override
   		public void onSuccess(int statusCode, Header[] headers,
   				JSONObject response) {
   			// TODO Auto-generated method stub
   			try {
   				JSONObject obj = response.getJSONObject("result");
   				boolean flag = response.getBoolean("success");
   				String errCode = obj.getString("errorCode");
   				JSONObject info = obj.getJSONObject("info");
   				String a = info.getString("memberTotalPerformance");
   				if(a.equals("null")){
   					tv4.setText(0+"");
   				}else{
   					tv4.setText(a);
   				}   				
//   				tv4.setText(a);
   				myDialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}
   		}
   		 @Override
   		public void onFailure(int statusCode, Header[] headers,
   				Throwable throwable, JSONObject errorResponse) {
   			// TODO Auto-generated method stub
   			super.onFailure(statusCode, headers, throwable, errorResponse);
   			Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
			myDialog.dismiss();
   		}
   	 });
	}

	private void initView() {
		// TODO Auto-generated method stub
		tv4 = (TextView)findViewById(R.id.tv4);
		memberImageView = (CircleImg)findViewById(R.id.memberImageView);
		memberNameView = (TextView)findViewById(R.id.memberNameView);
		memberMobileView = (TextView)findViewById(R.id.memberMobileView);
		memberDateView = (TextView)findViewById(R.id.memberDateView);
		deleteMember = (Button)findViewById(R.id.deleteMember);
		workRecordlistView = (ListView)findViewById(R.id.workRecordlistView);//工作记录listView
		back = (LinearLayout)findViewById(R.id.back);
		back.setOnClickListener(this);
		deleteMember.setOnClickListener(this);
		memberNameView.setText(memberBean.getNickName());
		memberMobileView.setText(memberBean.getMobile());
		memberDateView.setText(getStringDate(memberBean.getJoinDate())+"");	
		if(memberBean.getUserId().equals(userBean.getUserId())){
			deleteMember.setVisibility(View.GONE);
		}
		
	}
	
	public static String getStringDate(Long time ){
		 String strs = "";
		 try {
			 if( time != null ){
				 Date date = new Date(time);
				 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				 strs = sdf.format(date);
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 return strs;
	 }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.back:
			WorkspaceMemberActivity.this.finish();
			overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			break;
		case R.id.deleteMember: //移除成员
			deleteMember();
			break;
	}

}

	private void deleteMember() { //核心财富师移除成员
		// TODO Auto-generated method stub
		dialog = new AlertDialog.Builder(this).create();
		dialog.setCanceledOnTouchOutside(false);
		view = LayoutInflater.from(this).inflate(R.layout.delete_member, null);
		name =(TextView) view.findViewById(R.id.name);
		name.setText(memberBean.getNickName());
		sure = (Button) view.findViewById(R.id.sure);
		cancel = (Button)view.findViewById(R.id.cancel);
		
		 sure.setOnClickListener(new OnClickListener() {	
			 
				@Override
				public void onClick(View v) {
			    	 StringEntity entity=null;
			    	 JSONObject json=null;
			    	 try{
			    		 if(memberBean!=null&&memberBean.getUserId()!=null){
							  json = new JSONObject();
							  json.put("removeCfmpId", memberBean.getUserId());
							  entity = new StringEntity(json.toString(),"utf-8");
			    		 }
			    	 }catch(Exception e){
			    		 e.printStackTrace();
			    	 }
			    	 client.post(WorkspaceMemberActivity.this, HttpConfig.URL_LEADER_OUT_MEMBER, entity, "application/json", new JsonHttpResponseHandler(){
			    		 @Override
			    		public void onSuccess(int statusCode, Header[] headers,
			    				JSONObject response) {
			    			//核心财富师移除成员
			    			 try {
								JSONObject obj = response.getJSONObject("result");
								boolean flag = response.getBoolean("success");
								String errCode = obj.getString("errorCode");
								if(flag && errCode.equals("success")){
									dialog.dismiss();
									
									newdialog();
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
			    		}			    				    		 

						@Override
			    		public void onFailure(int statusCode, Header[] headers,
			    				Throwable throwable, JSONObject errorResponse) {
			    			// TODO Auto-generated method stub
			    			super.onFailure(statusCode, headers, throwable, errorResponse);
			    			Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
							myDialog.dismiss();
			    		}
			    	 });
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
	
	private void newdialog() {
		// TODO Auto-generated method stub
		dialog = new AlertDialog.Builder(this).create();
		dialog.setCanceledOnTouchOutside(false);
		view = LayoutInflater.from(this).inflate(R.layout.new_dialog, null);
		namenew =(TextView) view.findViewById(R.id.namenew);
		namenew.setText(memberBean.getNickName());
		sure = (Button) view.findViewById(R.id.sure);
		sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				
				Intent intent = new Intent(WorkspaceMemberActivity.this, UserCenterActivity.class);
				startActivity(intent);
				WorkspaceMemberActivity.this.finish();
			}
		});
		 dialog.setView(view);
		 dialog.show();		
	}
	
}
