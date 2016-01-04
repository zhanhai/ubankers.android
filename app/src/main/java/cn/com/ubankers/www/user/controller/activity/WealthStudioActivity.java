package cn.com.ubankers.www.user.controller.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.AttributesBean;
import cn.com.ubankers.www.user.model.MembersBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.model.UserNewBean;
import cn.com.ubankers.www.user.model.WealthBean;
import cn.com.ubankers.www.user.service.WealthStudioService;
import cn.com.ubankers.www.user.view.MembersAdapter;
import cn.com.ubankers.www.widget.ActionItem;
import cn.com.ubankers.www.widget.CircleImg;
import cn.com.ubankers.www.widget.ProcessDialog;
import cn.com.ubankers.www.widget.TitlePopup;
import cn.com.ubankers.www.widget.TitlePopup.OnItemOnClickListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WealthStudioActivity extends Activity implements OnClickListener {
	private AsyncHttpClient client;
	private WealthBean treasurerBean;
	private WealthBean wealthBean;
	private MembersBean memberBean;
	private AttributesBean attributesBean;
	private UserBean userBean;
	private UserNewBean user;
	private List<MembersBean> memberList;
	private MembersAdapter adapter;
	private Context context;
	private ListView treasureListView;
	private JSONArray list;	
	private TitlePopup titlePopup;
	private EditText cfmpSpceName, cfmpSpceSign,updateNo;
	private LinearLayout catelogView,title_bar_back;
	public  CircleImg faceImage,memberImageView,avatar;
	private TextView treasure_NameView,treasure_count,headerView1,headerView2,headerView3,headerView11,treasure_signView,
	                 memberNameView,memberMobileView,memberDateView,text,administrator,phone,updateTarget,editorView,tv2,tv4,tv22;
	private View headerView,bottom,view,vvi;
	private String object,totalCount,info,name, sign;
	private ProcessDialog myDialog;
	private AlertDialog dialog;
	private WealthStudioService wealthStudioService;		
	Button sure, cancel,disband_btn,btn;
	private RelativeLayout rl3, rl4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.treasure_layout);
		context = this;
		wealthStudioService = new WealthStudioService(context);
		client = MyApplication.app.getClient(context);
		if(MyApplication.app.getUser()!=null){
			userBean = MyApplication.app.getUser();
		}
		if(myDialog==null){
		   myDialog= ProcessDialog.createDialog(WealthStudioActivity.this, "正在加载中...");
		}
		Intent intent =this.getIntent();
		if(intent!=null){
			wealthBean = (WealthBean) intent.getSerializableExtra("wealthBean");
		}
		initView();
		getdata();
		
		/*if(wealthStudioService.userBean==null){
			Tools.onLogin(context,0);
			return;
		}*/
		wealthStudioService.initData();		
	}
	 private void initView(){
		 title_bar_back = (LinearLayout) findViewById(R.id.title_bar_back);
		 headerView = LayoutInflater.from(this).inflate(R.layout.treasure_header, null);	 
		 faceImage = (CircleImg)findViewById(R.id.memberImageView);
		 treasure_signView = (TextView)findViewById(R.id.treasure_signView);
		 headerView1 = (TextView) headerView.findViewById(R.id.headerView1);
		 headerView2 = (TextView) headerView.findViewById(R.id.headerView2);
		 headerView3 = (TextView) headerView.findViewById(R.id.headerView3);
		 headerView11 = (TextView)headerView.findViewById(R.id.headerView11); //业绩目标
		 updateTarget = (TextView)headerView.findViewById(R.id.updateTarget); //修改业绩目标
		 tv2 = (TextView)headerView.findViewById(R.id.tv2);//上月业绩
		 tv4 = (TextView)headerView.findViewById(R.id.tv4);//佣金总额
		 tv22 = (TextView)headerView.findViewById(R.id.tv22);//业绩目标
		 updateTarget.setOnClickListener(this);
		 treasure_count = (TextView)findViewById(R.id.treasure_count);
		 editorView = (TextView)findViewById(R.id.editorView);
		 rl3 = (RelativeLayout)findViewById(R.id.rl3);
		 rl4 = (RelativeLayout)findViewById(R.id.rl4);
		 vvi = (View)findViewById(R.id.vvi);		 
		 treasureListView = (ListView) findViewById(R.id.treasureListView);
		 catelogView = (LinearLayout) findViewById(R.id.catelogView);
		 titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		 titlePopup.setBackgroundDrawable(getResources().getDrawable(R.color.treasure_catelog));
		 titlePopup.setItemOnClickListener(new MyOnItemOnClickListener()); //发送邀请、解散工作室、联系银板客管理员
 		// 给标题栏弹窗添加子类
 		titlePopup.addAction(new ActionItem(this, "发送邀请"));
 		titlePopup.addAction(new ActionItem(this, "解散工作室"));
// 		titlePopup.addAction(new ActionItem(this, "联系银板客管理员"));
 		catelogView.setOnClickListener(this);
 		treasureListView.addHeaderView(headerView);		
 		editorView.setOnClickListener(this);
 		treasure_signView.setOnClickListener(this);
 		treasure_count.setOnClickListener(this);
 		rl3.setOnClickListener(this);
 		rl4.setOnClickListener(this);
 		vvi.setOnClickListener(this);
 		title_bar_back.setOnClickListener(this);
 		editorView.setText(wealthBean.getStudioName()); 
 		treasure_signView.setText(wealthBean.getSlogan()); 
	}
	 private void getdata(){
		 memberList = new ArrayList<MembersBean>();
		 client.post(WealthStudioActivity.this, HttpConfig.URL_LEADER_money, null, "application/json", new JsonHttpResponseHandler(){           
				//核心财富师查询 工作室业绩（上月）、工作室业绩目标、工作室佣金总金额
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					// TODO Auto-generated method stub
					try {
						if(commonJSON(response)){
							JSONObject obj = response.getJSONObject("result");
							boolean flag = response.getBoolean("success");
							String errCode = obj.getString("errorCode");
							JSONObject info = obj.getJSONObject("info");
							String a = info.getString("studioPerformance"); //上月工作室业绩
							String b = info.getString("totalCommission"); //佣金总金额
							String c = info.getString("performanceGoal"); //工作室业绩目标
							try {
								if(a.equals("null")){								
									tv2.setText(0+"");
								}else {
									tv2.setText(a);
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
							try {
								if(b.equals("null")){ //佣金总额
									tv4.setText(0+"");
								}else{
									tv4.setText(b);
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
							try {
								if(c.equals("null")){
									tv22.setText(0+"");
								}else {
									tv22.setText(c);
								}	
							} catch (Exception e) {
								// TODO: handle exception
							}	
														
							
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
								 adapter = new MembersAdapter(WealthStudioActivity.this, memberList);
							     treasureListView.setAdapter(adapter);
							     treasureListView.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> arg0,
											View arg1, int position, long arg3) {
										// TODO Auto-generated method stub
										Intent intent = new Intent(WealthStudioActivity.this,WorkspaceMemberActivity.class);
										Bundle bundle = new Bundle();
										bundle.putSerializable("memberBean", memberList.get(position-1));				
										intent.putExtras(bundle);
										intent.putExtra("position", position);
										startActivity(intent);
										
									}
								}); //点击成员列表
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
//     private void getAcitiveTreasureMember(){
//    	 myDialog.show();
//    	 memberList = new ArrayList<MembersBean>();
//    	 StringEntity entity=null;
//    	 JSONObject json=null;
//    	 try{
//    		 if(wealthBean!=null&&wealthBean.getStudioId()!=null){
//				  json = new JSONObject();
//				  json.put("studioId", wealthBean.getStudioId());
//				  entity = new StringEntity(json.toString(),"utf-8");
//    		 }
//    	 }catch(Exception e){
//    		 e.printStackTrace();
//    	 }    	
////    client.post(WealthStudioActivity.this, HttpConfig.URL_CFMP_MEMBERS, entity, "application/json", new JsonHttpResponseHandler(){
//    	client.post(WealthStudioActivity.this, HttpConfig.URL_ACTIVE_MEMBER, entity, "application/json", new JsonHttpResponseHandler(){
//    		 @Override
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
//							 adapter = new MembersAdapter(WealthStudioActivity.this, memberList);
//						     treasureListView.setAdapter(adapter);
//						     treasureListView.setOnItemClickListener(new OnItemClickListener() {
//
//								@Override
//								public void onItemClick(AdapterView<?> arg0,
//										View arg1, int position, long arg3) {
//									// TODO Auto-generated method stub
//									Intent intent = new Intent(WealthStudioActivity.this,WorkspaceMemberActivity.class);
//									Bundle bundle = new Bundle();
//									bundle.putSerializable("memberBean", memberList.get(position-1));				
//									intent.putExtras(bundle);
//									intent.putExtra("position", position);
//									startActivity(intent);
//									
//								}
//							}); //点击成员列表
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
//    	 });
//  }
     public void modifyTreasures(){   //修改财富师工作室名称&签名
    	    dialog = new AlertDialog.Builder(this).create();
			dialog.setCanceledOnTouchOutside(false);
			view = LayoutInflater.from(this).inflate(R.layout.revice_cfmp_spce_name, null);
			cfmpSpceName =(EditText) view.findViewById(R.id.cfmpSpceName); //名称
			cfmpSpceName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
			cfmpSpceSign =(EditText) view.findViewById(R.id.cfmpSpceSign); //签名
			cfmpSpceSign.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
			sure = (Button) view.findViewById(R.id.sure);
			cancel = (Button)view.findViewById(R.id.cancel);
    	    sure.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					if(cfmpSpceSign.getText().toString() == null || cfmpSpceSign.getText().toString().length()==0){
						if(cfmpSpceName.getText().toString() == null || cfmpSpceName.getText().toString().length()==0){
							Toast.makeText(WealthStudioActivity.this, "请完善财富师工作室名称",Toast.LENGTH_SHORT).show();
						}else{
							StringEntity entity = null;
							JSONObject json = null;
							try {
								if(wealthBean!=null&&wealthBean.getStudioId()!=null){
								      json = new JSONObject();
									  json.put("studioId", wealthBean.getStudioId());
									  json.put("studioName", cfmpSpceName.getText().toString());
//									  json.put("slogan", cfmpSpceSign.getText().toString()); //签名
									  entity = new StringEntity(json.toString(),"utf-8");
									  
									  client.post(WealthStudioActivity.this, HttpConfig.URL_MODIFY_STUDIO_INFO, entity, "application/json", new JsonHttpResponseHandler(){
								    		 @Override
												public void onSuccess(int statusCode, Header[] headers,
														JSONObject response) {
													try {
														JSONObject obj = response.getJSONObject("result");
														boolean flag = response.getBoolean("success");
														String errCode = obj.getString("errorCode");
														if(!obj.isNull("info")){
														info =obj.getString("info");										
														}
														if (flag && errCode.equals("success")) {
															 Gson gson = new Gson();
															 wealthBean = gson.fromJson(info, WealthBean.class);
															 editorView.setText(wealthBean.getStudioName()); 
//															 treasure_signView.setText(wealthBean.getSlogan()); 
													 	Toast.makeText(WealthStudioActivity.this, "工作室名称&签名修改成功", Toast.LENGTH_SHORT).show();
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
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					}else {
						if(cfmpSpceName.getText().toString() == null || cfmpSpceName.getText().toString().length()==0){
							Toast.makeText(WealthStudioActivity.this, "请完善财富师工作室名称",Toast.LENGTH_SHORT).show();
						}else{
							StringEntity entity = null;
							JSONObject json = null;
							try {
								if(wealthBean!=null&&wealthBean.getStudioId()!=null){
								      json = new JSONObject();
									  json.put("studioId", wealthBean.getStudioId());
									  json.put("studioName", cfmpSpceName.getText().toString());
									  json.put("slogan", cfmpSpceSign.getText().toString()); //签名
									  entity = new StringEntity(json.toString(),"utf-8");
									  client.post(WealthStudioActivity.this, HttpConfig.URL_MODIFY_STUDIO_INFO, entity, "application/json", new JsonHttpResponseHandler(){
								    		 @Override
												public void onSuccess(int statusCode, Header[] headers,
														JSONObject response) {
													try {
														JSONObject obj = response.getJSONObject("result");
														boolean flag = response.getBoolean("success");
														String errCode = obj.getString("errorCode");
														if(!obj.isNull("info")){
														info =obj.getString("info");										
														}
														if (flag && errCode.equals("success")) {
															 Gson gson = new Gson();
															 wealthBean = gson.fromJson(info, WealthBean.class);
															 editorView.setText(wealthBean.getStudioName()); 
															 treasure_signView.setText(wealthBean.getSlogan()); 
													 	Toast.makeText(WealthStudioActivity.this, "工作室名称&签名修改成功", Toast.LENGTH_SHORT).show();
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
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					}
					
					
					
//					if(cfmpSpceName.getText().toString() == null || cfmpSpceName.getText().toString().length()==0){
//						Toast.makeText(WealthStudioActivity.this, "请完善财富师工作室名称",Toast.LENGTH_SHORT).show();
//					}else if (cfmpSpceSign.getText().toString() == null || cfmpSpceSign.getText().toString().length()==0){
//						Toast.makeText(WealthStudioActivity.this, "请完善财富师工作室签名",Toast.LENGTH_SHORT).show();
//					}else{
//				    	 StringEntity entity=null;
//				    	 JSONObject json=null;
//				    	 try{
//				    		 if(wealthBean!=null&&wealthBean.getStudioId()!=null){
//								  json = new JSONObject();
//								  json.put("studioId", wealthBean.getStudioId());
//								  json.put("studioName", cfmpSpceName.getText().toString());
//								  json.put("slogan", cfmpSpceSign.getText().toString());
//								  entity = new StringEntity(json.toString(),"utf-8");
//				    		 }
//				    	 }catch(Exception e){
//				    		 e.printStackTrace();
//				    	 }
//				    	 client.post(WealthStudioActivity.this, HttpConfig.URL_MODIFY_STUDIO_INFO, entity, "application/json", new JsonHttpResponseHandler(){
//				    		 @Override
//								public void onSuccess(int statusCode, Header[] headers,
//										JSONObject response) {
//									try {
//										JSONObject obj = response.getJSONObject("result");
//										boolean flag = response.getBoolean("success");
//										String errCode = obj.getString("errorCode");
//										if(!obj.isNull("info")){
//										info =obj.getString("info");										
//										}
//										if (flag && errCode.equals("success")) {
//											 Gson gson = new Gson();
//											 wealthBean = gson.fromJson(info, WealthBean.class);
//											 editorView.setText(wealthBean.getStudioName()); 
//											 treasure_signView.setText(wealthBean.getSlogan()); 
//									 	Toast.makeText(WealthStudioActivity.this, "工作室名称&签名修改成功", Toast.LENGTH_SHORT).show();
//									 	dialog.dismiss();
//										}
//									} catch (JSONException e) {
//										e.printStackTrace();
//									}
//								}
//								@Override
//								public void onFailure(int statusCode, Header[] headers,
//										Throwable throwable, JSONObject errorResponse) {
//									// TODO Auto-generated method stub
//									super.onFailure(statusCode, headers, throwable, errorResponse);
//								}
//				    	 });
//					}
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
     
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.catelogView:
			titlePopup.show(catelogView);
			break;
		case R.id.title_bar_back:
			WealthStudioActivity.this.finish();
			overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			break;
		case R.id.editorView:
		case R.id.rl3:
		case R.id.rl4:
		case R.id.treasure_signView:
		case R.id.vvi:
			modifyTreasures(); //修改财富师工作室名称&签名
			break;			
		case R.id.updateTarget: //修改业绩目标
			updateTarget1();
			break;

		}
		
	}
	
	private void updateTarget1() {
		// TODO Auto-generated method stub
		dialog = new AlertDialog.Builder(this).create();
		dialog.setCanceledOnTouchOutside(false);
		view = LayoutInflater.from(this).inflate(R.layout.update_target, null);
		updateNo =(EditText) view.findViewById(R.id.updateNo);
		sure = (Button) view.findViewById(R.id.sure);
		cancel = (Button)view.findViewById(R.id.cancel);
		
		 sure.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String performanceGoal = updateNo.getText().toString();
					StringEntity entity=null;
			    	 JSONObject json=null;
					try {						
						json = new JSONObject();
						json.put("performanceGoal", performanceGoal);
						entity = new StringEntity(json.toString(),"utf-8");
					} catch (Exception e) {
						// TODO: handle exception
					}
					
					
					client.post(WealthStudioActivity.this, HttpConfig.URL_LEADER_UPDATE_SALE, entity, "application/json", new JsonHttpResponseHandler(){
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							// TODO Auto-generated method stub
							try {
								JSONObject obj = response.getJSONObject("result");
								boolean flag = response.getBoolean("success");
								String errCode = obj.getString("errorCode");
								if (flag && errCode.equals("success")) {
									dialog.dismiss();
									tv22.setText(updateNo.getText().toString());
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
							dialog.dismiss();
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

	public class MyOnItemOnClickListener implements OnItemOnClickListener {  //发送邀请、解散工作室、联系银板客管理员

		@Override
		public void onItemClick(ActionItem item, int position){ 
			Intent intent =null;
		 if(position==0){ //发送邀请
			 Bundle bundle = new Bundle();
			 bundle.putSerializable("wealthBean", wealthBean);
			 intent = new Intent(WealthStudioActivity.this,WealthspaceInvitationActivity.class);
			 intent.putExtras(bundle);
			 startActivity(intent);
			 WealthStudioActivity.this.finish();
		 }else if(position==1){	//解散		 			 
			 dialog = new AlertDialog.Builder(context).create();				 
			 dialog.setCanceledOnTouchOutside(false);				 
				view = LayoutInflater.from(context).inflate(R.layout.disband_cfmpspace, null);
				text = (TextView)view.findViewById(R.id.text);
				disband_btn = (Button)view.findViewById(R.id.disband_btn);
				cancel = (Button)view.findViewById(R.id.cancel);
				disband_btn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							closeTreasureMember();								
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
	 private void closeTreasureMember(){
		 myDialog.show();
    	 StringEntity entity=null;
    	 JSONObject json=null;
    	 try{
    		 if(wealthBean!=null&&wealthBean.getStudioId()!=null){
				  json = new JSONObject();
				  json.put("studioId", wealthBean.getStudioId());
				  entity = new StringEntity(json.toString(),"utf-8");
    		 }
    	 }catch(Exception e){
    		 e.printStackTrace();
    	 }
    	 client.post(WealthStudioActivity.this, HttpConfig.URL_CLOSE_STUDIO, entity, "application/json", new JsonHttpResponseHandler(){
    		 @Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					try {
						JSONObject obj = response.getJSONObject("result");
						boolean flag = response.getBoolean("success");
						String errCode = obj.getString("errorCode");
						if (flag && errCode.equals("success")) {
							 Bundle bundle = new Bundle();
							 bundle.putSerializable("wealthBean", wealthBean);
							 Intent intent = new Intent(WealthStudioActivity.this,UserCenterActivity.class);
							 intent.putExtras(bundle);
							 startActivity(intent);
							 WealthStudioActivity.this.finish();
							Toast.makeText(WealthStudioActivity.this, "财富工作室解散成功", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
						myDialog.dismiss();
					}
					    myDialog.dismiss();
				}
				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable, errorResponse);
				}
    	 });
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
}
