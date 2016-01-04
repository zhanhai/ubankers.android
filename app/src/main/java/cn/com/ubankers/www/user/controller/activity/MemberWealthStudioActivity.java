package cn.com.ubankers.www.user.controller.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.MembersBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.model.WealthBean;
import cn.com.ubankers.www.user.service.MemberWealthStudioService;
import cn.com.ubankers.www.user.view.MembersAdapter;
import cn.com.ubankers.www.widget.ActionItem;
import cn.com.ubankers.www.widget.CircleImg;
import cn.com.ubankers.www.widget.ProcessDialog;
import cn.com.ubankers.www.widget.TitlePopup;
import cn.com.ubankers.www.widget.TitlePopup.OnItemOnClickListener;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MemberWealthStudioActivity extends Activity implements OnClickListener{
	private Context context;
	private AsyncHttpClient client;
	private UserBean userBean;
	private ProcessDialog myDialog;
	private WealthBean wealthBean;
	private LinearLayout title_bar_back,catelogView;
	public CircleImg faceImage,avatar;
	private TextView tv25,editorView,treasure_signView,treasure_count,headerView11,headerView1,headerView3,tv22,tv2,text,administrator,phone,findsale;
	private ListView treasureListView;
	private View headerView,view;
	private List<MembersBean> memberList;
	private String totalCount,object;
	private JSONArray list;
	private MembersBean memberBean;
	private MembersAdapter adapter;
	private MemberWealthStudioService memberWealthStudioService;
	private TitlePopup titlePopup;
	private AlertDialog dialog;
	private Button cancel,sure,btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.member_treasure_layout);
		context = this;
		memberWealthStudioService = new MemberWealthStudioService(context);
		client = MyApplication.app.getClient(context);
		if(MyApplication.app.getUser()!=null){
			userBean = MyApplication.app.getUser();
		}
		if(myDialog==null){
		   myDialog= ProcessDialog.createDialog(MemberWealthStudioActivity.this, "正在加载中...");
		}
		Intent intent =this.getIntent();
		if(intent!=null){
			wealthBean = (WealthBean) intent.getSerializableExtra("wealthBean");
		}
		initView();
//		getAcitiveTreasureMember();
		getdata();
		memberWealthStudioService.initData();
		
	}
	
	private void getdata(){
//		 myDialog.show();
    	 memberList = new ArrayList<MembersBean>();
    	 StringEntity entity=null;
    	 JSONObject json=null;
    	 try{   		
				  json = new JSONObject();
				  json.put("pageIndex", 1);
				  json.put("pageCount", 10);
				  entity = new StringEntity(json.toString(),"utf-8");
    		 
    	 }catch(Exception e){
    		 e.printStackTrace();
    	 }  
		 client.post(MemberWealthStudioActivity.this, HttpConfig.URL_LEADER_money, entity, "application/json", new JsonHttpResponseHandler(){           
//			 client.post(MemberWealthStudioActivity.this, HttpConfig.URL_MEMBER_FIND, entity, "application/json", new JsonHttpResponseHandler(){           
				//核心财富师查询 工作室业绩（上月）、工作室业绩目标、工作室佣金总金额
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					// TODO Auto-generated method stub
					try {
						if(commonJSON(response)){
//							JSONObject info = response.getJSONObject("result").getJSONObject("info");
							JSONObject obj = response.getJSONObject("result");
							boolean flag = response.getBoolean("success");
							String errCode = obj.getString("errorCode");
							JSONObject info = obj.getJSONObject("info");
							String a = info.getString("studioPerformance"); //上月工作室业绩
//							String b = info.getString("totalCommission"); //佣金总金额
							String c = info.getString("performanceGoal"); //工作室业绩目标
							String f = info.getString("memberPerformance"); //个人业绩
							if(a.equals("null")){
								tv22.setText(0+"");
							}else{
								tv22.setText(a);
							}
							if(c.equals("null")){
								tv25.setText(0+"");
							}else {
								tv25.setText(c);
							}
							if(f.equals("null")){
								tv2.setText(0+"");
							}else {
								tv2.setText(f);
							}
//							tv22.setText(a);
//							tv2.setText(f);
//							tv25.setText(c);
							if(!info.isNull("memberCount")){
								totalCount =info.getString("memberCount");
								treasure_count.setText(totalCount+"人");
							}
							if(!info.isNull("memberList")){
							    list = info.getJSONArray("memberList");
								}
							if (flag && errCode.equals("success")) {
								for(int i=0;i<list.length();i++){
									try {
										 object = list.getString(i);
										 Gson gson = new Gson();
									     memberBean = gson.fromJson(object, MembersBean.class);   
									     memberList.add(memberBean);
			                        } catch (Exception e) {
			                             e.printStackTrace();
			                        }									     
								}
								 adapter = new MembersAdapter(MemberWealthStudioActivity.this, memberList);
							     treasureListView.setAdapter(adapter);							     
							}
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
				}
				
			});
	 }
	
	private boolean commonJSON(JSONObject response){
		boolean status=false;
		try {
			JSONObject obj= response.getJSONObject("result");
			boolean flag = response.getBoolean("success");
			String errCode = obj.getString("errorCode");
			if(flag && errCode.equals("success")){
				status=true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
		
	}

//	private void getAcitiveTreasureMember() {
//		// TODO Auto-generated method stub
//
//   	 myDialog.show();
//   	 memberList = new ArrayList<MembersBean>();
//   	 StringEntity entity=null;
//   	 JSONObject json=null;
//   	 try{
//   		 if(wealthBean!=null&&wealthBean.getStudioId()!=null){
//				  json = new JSONObject();
//				  json.put("studioId", wealthBean.getStudioId());
//				  entity = new StringEntity(json.toString(),"utf-8");
//   		 }
//   	 }catch(Exception e){
//   		 e.printStackTrace();
//   	 }    	
//
//   	client.post(MemberWealthStudioActivity.this, HttpConfig.URL_ACTIVE_MEMBER, entity, "application/json", new JsonHttpResponseHandler(){
//   		 @Override
//				public void onSuccess(int statusCode, Header[] headers,
//						JSONObject response) {
//					try {
//						JSONObject obj = response.getJSONObject("result");
//						boolean flag = response.getBoolean("success");
//						String errCode = obj.getString("errorCode");
//						if(!obj.isNull("totalCount")){
//							totalCount =obj.getString("totalCount");
//						}						
//						if(!obj.isNull("list")){
//					    list = obj.getJSONArray("list");
//						}
//						if (flag && errCode.equals("success")) {
//							for(int i=0;i<list.length();i++){
//								try {
//									 object = list.getString(i);
//									 Gson gson = new Gson();
//								     memberBean = gson.fromJson(object, MembersBean.class);   
//								     memberList.add(memberBean);
//		                        } catch (Exception e) {
//		                             e.printStackTrace();
//		                        }									     
//							}
//							 adapter = new MembersAdapter(MemberWealthStudioActivity.this, memberList);
//						     treasureListView.setAdapter(adapter);						     
//						}
//						treasure_count.setText(totalCount+"人");
//						myDialog.dismiss();
//					} catch (JSONException e) {
//						e.printStackTrace();
//						myDialog.dismiss();
//					}
//				}
//				@Override
//				public void onFailure(int statusCode, Header[] headers,
//						Throwable throwable, JSONObject errorResponse) {
//					// TODO Auto-generated method stub
//					super.onFailure(statusCode, headers, throwable, errorResponse);
//				}
//   	 });
// 
//	}

	private void initView() {
		// TODO Auto-generated method stub
//		findsale = (TextView)findViewById(R.id.findsale);//查看个人业绩
		title_bar_back = (LinearLayout) findViewById(R.id.title_bar_back);
		catelogView = (LinearLayout) findViewById(R.id.catelogView);
		faceImage = (CircleImg)findViewById(R.id.memberImageView);
		editorView = (TextView)findViewById(R.id.editorView);
		treasure_signView = (TextView)findViewById(R.id.treasure_signView);
		treasure_count = (TextView)findViewById(R.id.treasure_count);
		treasureListView = (ListView) findViewById(R.id.treasureListView); //成员列表
		tv22 = (TextView)findViewById(R.id.tv22); //工作室业绩
		tv2 = (TextView)findViewById(R.id.tv2); //个人业绩
		tv25 = (TextView)findViewById(R.id.tv25);//业绩目标
		editorView.setText(wealthBean.getStudioName()); 
		treasure_signView.setText(wealthBean.getSlogan()); 
		catelogView.setOnClickListener(this);
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		titlePopup.setBackgroundDrawable(getResources().getDrawable(R.color.treasure_catelog));
		 titlePopup.setItemOnClickListener(new MyOnItemOnClickListener()); //发送邀请、解散工作室、联系银板客管理员
		// 给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(this, "发送邀请"));
		titlePopup.addAction(new ActionItem(this, "退出工作室"));
//		titlePopup.addAction(new ActionItem(this, "联系银板客管理员"));
		title_bar_back.setOnClickListener(this);
//		findsale.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.catelogView:
			titlePopup.show(catelogView);
			break;
		case R.id.title_bar_back:
			MemberWealthStudioActivity.this.finish();
			overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			break;
//		case R.id.findsale: //查看个人业绩
//			findSale();
//			break;
		}
	}
	
//	private void findSale() {
//		// TODO Auto-generated method stub
//		Intent intent = new Intent(MemberWealthStudioActivity.this,MemberSpaceActivity.class);
//		Bundle bundle = new Bundle();
//		memberList.toString();
//		Log.i("memberList.toString()", memberList.toString());
//		bundle.putSerializable("memberBean", memberList.get(1));				
//		intent.putExtras(bundle);
//		startActivity(intent);
//	}

	public class MyOnItemOnClickListener implements OnItemOnClickListener {  //发送邀请、解散工作室、联系银板客管理员

		@Override
		public void onItemClick(ActionItem item, int position){ 
			Intent intent =null;
		 if(position==0){ //发送邀请
			 Bundle bundle = new Bundle();
			 bundle.putSerializable("wealthBean", wealthBean);
			 intent = new Intent(MemberWealthStudioActivity.this,WealthspaceInvitationActivity.class);
			 intent.putExtras(bundle);
			 startActivity(intent);
			 MemberWealthStudioActivity.this.finish();
		 }else if(position==1){	//退出		 			 
			 dialog = new AlertDialog.Builder(context).create();				 
			 dialog.setCanceledOnTouchOutside(false);				 
				view = LayoutInflater.from(context).inflate(R.layout.signout_workspace, null);
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
//		 else if(position == 2){//联系银板客管理员
//			 dialog = new AlertDialog.Builder(context).create();				 
//			 dialog.setCanceledOnTouchOutside(false);				 
//				view = LayoutInflater.from(context).inflate(R.layout.contact_ubankers_administrator, null);
//				avatar = (CircleImg)view.findViewById(R.id.avatar); //银板客管理员头像
//				administrator = (TextView)view.findViewById(R.id.administrator); //银板客管理员名字
//				phone = (TextView)view.findViewById(R.id.phone);//银板客管理员电话
//				cancel = (Button)view.findViewById(R.id.cancel);
//				btn = (Button)view.findViewById(R.id.btn); //拨打银板客管理员电话
//				btn.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View arg0) {
//							try {
//								String strInput = phone.getText().toString().trim();								
//								Intent myIntentDial = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+strInput));
//								startActivity(myIntentDial);
//								dialog.dismiss();
//							} catch (Exception e) {
//								// TODO: handle exception
//								Log.e("++++++++", e.toString());
//							}							
//						}
//					});
//				cancel.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						dialog.dismiss();
//					}
//				});
//				dialog.setView(view);
//				dialog.show();
//		 }
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
   	 client.post(MemberWealthStudioActivity.this, HttpConfig.URL_MEMBER_OUT_SPACE, entity, "application/json", new JsonHttpResponseHandler(){
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
					 Intent intent = new Intent(MemberWealthStudioActivity.this,UserCenterActivity.class);
					 intent.putExtras(bundle);
					 startActivity(intent);
					 MemberWealthStudioActivity.this.finish();
					Toast.makeText(MemberWealthStudioActivity.this, "财富工作室退出成功", Toast.LENGTH_SHORT).show();
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
		}
   	 });
	}

}
