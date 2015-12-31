package cn.com.ubankers.www.user.service;

import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.controller.activity.ReserverDetailsActivity;
import cn.com.ubankers.www.user.model.ReserverDetailsBean;
import cn.com.ubankers.www.user.view.OrderDetailsAdapter;
import cn.com.ubankers.www.utils.PhotoUtil;
import cn.com.ubankers.www.utils.Tools;
import cn.com.ubankers.www.utils.XutilsHttp;
import cn.com.ubankers.www.widget.MyDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 * 
 * @author 廖准华
 *产品已确定预约产品的详情的service
 */
public class ReserverDetailsService {
	private String userId;
	private ArrayList<ReserverDetailsBean> list;
	private ListView details_lv;
    private AsyncHttpClient client;
	private OrderDetailsAdapter adapter;
	private static ImageView idPicFront,idPicBack;
    private int idsFrontItem;
    private static ImageView PayImgae;
    private TextView regist_payDialog_close,dialog_close,unregist_payDialog_close,unregist_dialog_close;
    private int idsBackItem;
    private boolean idFlag=false;
    private ReserverDetailsBean  orderDetailsBean;
    private ReserverDetailsActivity activity;
    private String productId;
    private MyDialog myDialog;
	public ReserverDetailsService(String userId,String productId,ArrayList<ReserverDetailsBean> list,ListView details_lv,ReserverDetailsActivity activity){
		this.userId = userId;
		this.list = list;
		this.details_lv = details_lv;
		this.activity = activity;
		this.productId = productId;
		client = MyApplication.app.getClient(activity);
		if(myDialog==null){
			myDialog = MyDialog.createDialog(activity,"正在加载中...");
		}
	}
	
	
	
	//获取已确定预约的接口
	public void initData(final int type) {
		
		StringEntity entity=null;
		try {
			if(type==1){//已注册用户
				JSONObject json = new JSONObject();
				json.put("limit","");
				json.put("start","");
				json.put("cfmpId",userId);
				json.put("isBInvestor",1);
				json.put("productId",productId);
				entity =new StringEntity(json.toString(),"utf-8");
			}else if(type==2){//未注册用户
				JSONObject json = new JSONObject();
				json.put("limit","");
				json.put("start","");
				json.put("cfmpId",userId);
				json.put("isBInvestor",2);
				json.put("productId",productId);
				entity =new StringEntity(json.toString(),"utf-8");
			}
		} catch (Exception e1) {
				e1.printStackTrace();
		}
		client.post(activity, HttpConfig.URL_RESERVE_DETAILS, entity,"application/json", new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					boolean flag = response.getBoolean("success");
					if(flag){
						JSONObject jsonObject = response.getJSONObject("result");
						String errorCode = jsonObject.getString("errorCode");
						String totalCount = jsonObject.getString("totalCount");
						if(errorCode.equals("success")){
							if(list.size()>0){
								list.clear();
							}
							JSONArray jsonArray = jsonObject.getJSONArray("list");
							for(int i=0;i<jsonArray.length();i++){
								ReserverDetailsBean orderDetailsBean = new ReserverDetailsBean();
								JSONObject object = jsonArray.getJSONObject(i);
								String reserveId = object.getString("reserveId");
								String reserveName = object.getString("reserveName");
								String reserveMobile = object.getString("reserveMobile");
								String productName = object.getString("productName");
								String reserveQuota = object.getString("reserveQuota");
								String reserveVoucherId = object.getString("reserveVoucherId");
								String idcardBackFileId = object.getString("idcardBackFileId");
								String idcardFrontFileId = object.getString("idcardFrontFileId");
								String userId = object.getString("userId");
								reserveQuota=Tools.InterceptTo(reserveQuota);
								orderDetailsBean.setReserveId(reserveId);
								orderDetailsBean.setProductName(productName);
								orderDetailsBean.setReserveMobile(reserveMobile);
								orderDetailsBean.setReserveQuota(reserveQuota);
								orderDetailsBean.setReserveName(reserveName);
								orderDetailsBean.setReserveVoucherId(reserveVoucherId);
								orderDetailsBean.setIdcardBackFileId(idcardBackFileId);
								orderDetailsBean.setIdcardFrontFileId(idcardFrontFileId);
								orderDetailsBean.setUserId(userId);
								list.add(orderDetailsBean);
							}
						}
						adapter = new OrderDetailsAdapter(activity,list,new UploadPayOnclickListener(),new UnUploadPayOnclickListener(),new UploadIDOnclickListener(),new UnUploadIDOnclickListener(),type);
						details_lv.setAdapter(adapter);
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(activity, "请求超时，请重试", Toast.LENGTH_SHORT).show();

			}
		});
		
		
	}
	 //已注册用户的打款凭证的监听
	private class UploadPayOnclickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			orderDetailsBean = (ReserverDetailsBean) arg0.getTag();
			SharedPreferences sp = activity.getSharedPreferences("orderDetailsBeanSp",Context.MODE_PRIVATE);
			Editor edit = sp.edit();
			edit.putString("ReserveId", orderDetailsBean.getReserveId());
			edit.commit();
			AlertPayDialog(orderDetailsBean,1);
		}	
	}
	 //未注册用户的打款凭证的监听
	private class UnUploadPayOnclickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			orderDetailsBean = (ReserverDetailsBean) arg0.getTag();
			SharedPreferences sp = activity.getSharedPreferences("orderDetailsBeanSp",Context.MODE_PRIVATE);
			Editor edit = sp.edit();
			edit.putString("ReserveId", orderDetailsBean.getReserveId());
			edit.commit();
			AlertPayDialog(orderDetailsBean,2);
		}	
	}
	 
	 //已注册用户的身份证
	 private class UploadIDOnclickListener implements OnClickListener{
 
		@Override
		public void onClick(View arg0) {
			    orderDetailsBean = (ReserverDetailsBean)arg0.getTag();
			    SharedPreferences sp = activity.getSharedPreferences("orderDetailsBeanSp",Context.MODE_PRIVATE);
				Editor edit = sp.edit();
				edit.putString("ReserveId", orderDetailsBean.getReserveId());
				edit.commit();
				AlertDialogdialog(orderDetailsBean,1); 
		}	
	}
	 //未注册用户的身份证
	 private class UnUploadIDOnclickListener implements OnClickListener{
		 
			@Override
			public void onClick(View arg0) {
				    orderDetailsBean = (ReserverDetailsBean)arg0.getTag();
				    SharedPreferences sp = activity.getSharedPreferences("orderDetailsBeanSp",Context.MODE_PRIVATE);
					Editor edit = sp.edit();
					edit.putString("ReserveId", orderDetailsBean.getReserveId());
					edit.commit();
					AlertDialogdialog(orderDetailsBean,2); 
			}	
		}
	 
	 
	 private void AlertDialogdialog(ReserverDetailsBean reserverDetailsBean,final int type) {
			final AlertDialog dialog=new AlertDialog.Builder(activity).create();
			if(orderDetailsBean.getIdcardBackFileId()=="0"&&orderDetailsBean.getIdcardFrontFileId()=="0"){
				dialog.setTitle("上传身份证");
			}else{
				dialog.setTitle("修改身份证");
			}
			
			final View view = LayoutInflater.from(activity).inflate( R.layout.shenfenyanzhengshangchuan_dialog, null);
			idPicFront=(ImageView)view.findViewById(R.id.dialog_a);//身份证正面
			idPicBack=(ImageView)view.findViewById(R.id.dialog_b);//身份证反面
			if(type==1){
					dialog_close=(TextView)view.findViewById(R.id.dialog_close);//已注册用户的关闭按钮
					dialog_close.setVisibility(View.VISIBLE);
					dialog_close.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							initData(1);
							dialog.dismiss();
						}
					});
			}else if(type==2){
					unregist_dialog_close=(TextView) view.findViewById(R.id.unregist_dialog_close);//未注册用户的关闭按钮
					unregist_dialog_close.setVisibility(View.VISIBLE);
					unregist_dialog_close.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							initData(2);
							dialog.dismiss();
						}
					});
			}
			idPicFront.setScaleType(ScaleType.CENTER_CROP);
			idPicBack.setScaleType(ScaleType.CENTER_CROP);
			if(reserverDetailsBean!=null&&reserverDetailsBean.getIdcardBackFileId()!=null&&!reserverDetailsBean.getIdcardBackFileId().equals("")){
//				MyApplication.loadImage(HttpConfig.HTTP_IMAGE_QUERY_URL+reserverDetailsBean.getIdcardBackFileId(),idPicBack,null);
				 new XutilsHttp(activity).display(idPicBack,HttpConfig.HTTP_IMAGE_QUERY_URL+reserverDetailsBean.getIdcardBackFileId());
			}
			if(reserverDetailsBean!=null&&reserverDetailsBean.getIdcardFrontFileId()!=null&&!reserverDetailsBean.getIdcardFrontFileId().equals("")){
//				MyApplication.loadImage(HttpConfig.HTTP_IMAGE_QUERY_URL+reserverDetailsBean.getIdcardFrontFileId(),idPicFront,null);
				new XutilsHttp(activity).display(idPicFront,HttpConfig.HTTP_IMAGE_QUERY_URL+reserverDetailsBean.getIdcardFrontFileId());
			}
			idPicFront.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					setUsername("idFlag",1);
					setUsername("idsFrontItem",1);
					setUsername("idsBackItem",0);
//					idFlag =true;
//					idsFrontItem=1;
					activity.showDialog();
				}
			});
			idPicBack.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
//					idFlag =true;
//					idsBackItem=1;
					setUsername("idFlag",1);
					setUsername("idsBackItem",1);
					setUsername("idsFrontItem",0);
					activity.showDialog();
				}
			});
			dialog.setView(view); 
			dialog.show();
			dialog.setCanceledOnTouchOutside(false);
			dialog.setOnKeyListener(new OnKeyListener() {      
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0)
		            {
						if(type==1){
							initData(1);
						}else if(type==2){
							initData(2);
						}
						dialog.dismiss();
		            }
		            return false;
				}
			});

		}
		 private void AlertPayDialog(ReserverDetailsBean reserverDetailsBean,final int type) {
			final AlertDialog dialog=new AlertDialog.Builder(activity).create();
			if(reserverDetailsBean!=null&&reserverDetailsBean.getReserveVoucherId()!=null&&!reserverDetailsBean.getReserveVoucherId().equals("")){
				dialog.setTitle("修改打款凭证");
			}else{
				dialog.setTitle("上传打款凭证");
			}
			final View view = LayoutInflater.from(activity).inflate( R.layout.pay_upload_dialog, null);
			PayImgae=(ImageView)view.findViewById(R.id.pay_image);//凭证正面
			if(type==1){
				    regist_payDialog_close=(TextView)view.findViewById(R.id.regist_payDialog_close);//已注册的关闭按钮
				    regist_payDialog_close.setVisibility(View.VISIBLE);
	
				    
				    regist_payDialog_close.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							initData(1);
							dialog.dismiss();
						}
					});
			}else if(type==2){
					unregist_payDialog_close=(TextView) view.findViewById(R.id.unregist_payDialog_close);//未注册的关闭按钮
					unregist_payDialog_close.setVisibility(View.VISIBLE);
					unregist_payDialog_close.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								initData(2);
								dialog.dismiss();
							}
						});
			}
			PayImgae.setScaleType(ScaleType.CENTER_CROP);
			
			
		    if(reserverDetailsBean!=null&&reserverDetailsBean.getReserveVoucherId()!=null&&!reserverDetailsBean.getReserveVoucherId().equals("")){
//				MyApplication.loadImage(HttpConfig.HTTP_IMAGE_QUERY_URL+reserverDetailsBean.getReserveVoucherId(),PayImgae,null);	
		    	 new XutilsHttp(activity).display(PayImgae,HttpConfig.HTTP_IMAGE_QUERY_URL+reserverDetailsBean.getReserveVoucherId());
			}
		    PayImgae.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					activity.showDialog();
				}
			});
			dialog.setView(view); 
			dialog.show();
			dialog.setCanceledOnTouchOutside(false);
			dialog.setOnKeyListener(new OnKeyListener() {      
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0)
		            {
						if(type==1){
							initData(1);
						}else if(type==2){
							initData(2);
						}
						dialog.dismiss();
		            }
		            return false;
				}
		    });
	 }
		 /**
			 * 保存裁剪之后的图片数据
			 * 
			 * @param picdata
			 */
	public void getImageToView(String path) {
		if (path != null) {
				Bitmap bitmap = PhotoUtil.getimage(path);
				String imageUrl = PhotoUtil.bitmaptoString(bitmap);
				uploadingAvatar(imageUrl);
		}
	}
	//上传头像的接口
	public void uploadingAvatar(String imageUrl){
				myDialog.show();
				SharedPreferences settings = activity.getSharedPreferences("orderDetailsBeanSp",Context.MODE_PRIVATE);
				final String ReserveId = settings.getString("ReserveId",null);
				RequestParams params = new RequestParams();
				params.put("picData", imageUrl);
				client.post(activity,HttpConfig.URL_UPLOADPHOTO,params,new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
					int idFlag=getUsername("idFlag");
					int idsFrontItem=	getUsername("idsFrontItem");
				    int idsBackItem=getUsername("idsBackItem");
//						("idFlag","1");
						try {
							boolean flag = response.getBoolean("success");
							JSONObject jsonObject = response.getJSONObject("result");
							String errorCode = jsonObject.getString("errorCode");
							JSONObject info = jsonObject.getJSONObject("info");
							final String userFaceId = info.getString("ids");
							String commonPicUrl = null;
							if(flag==true&&errorCode.equals("success")){
								JSONObject json1 = new JSONObject();
								StringEntity entity1 = null;				try {   
									if(idFlag==1&&idsFrontItem==1){
										JSONObject jsonFront = new JSONObject();
										jsonFront.put("idcardFrontFileId",userFaceId);
										jsonFront.put("reserveId",ReserveId);
										entity1 = new StringEntity(jsonFront.toString(),"utf-8");
										commonPicUrl=HttpConfig.URL_IDS_PIC;
									}else if(idFlag==1&&idsBackItem==1){
										JSONObject jsonBack = new JSONObject();
										jsonBack.put("idcardBackFileId",userFaceId);
										jsonBack.put("reserveId",ReserveId);
										entity1 = new StringEntity(jsonBack.toString(),"utf-8");
										commonPicUrl=HttpConfig.URL_IDS_PIC;
									}else{
										json1.put("reserveId",ReserveId);
										json1.put("reserveVoucherId",userFaceId);
										entity1 = new StringEntity(json1.toString(),"utf-8");
										commonPicUrl=HttpConfig.URL_PAY_PIC;
									}
									myDialog.dismiss();
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								client.post(activity,commonPicUrl,entity1,"application/json",new JsonHttpResponseHandler(){
									public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
										int idFlag=getUsername("idFlag");
										int idsFrontItem=	getUsername("idsFrontItem");
										int idsBackItem=getUsername("idsBackItem");
										
										try {
												boolean flag2 = response.getBoolean("success");
												JSONObject jsonObject2 = response.getJSONObject("result");
												String errorCode = jsonObject2.getString("errorCode");
												if(flag2==true&&errorCode.equals("success")){
													Toast.makeText(activity, "上传成功", Toast.LENGTH_SHORT).show();
													if(idFlag==1){//idPicFront
														if(idsFrontItem==1&&idsBackItem!=1){
															new XutilsHttp(activity).display(idPicFront,HttpConfig.HTTP_IMAGE_QUERY_URL+userFaceId);	
															idsFrontItem=0;
															setUsername("idsFrontItem",0);
															setUsername("idFlag",0);
//															setUsername("idsFrontItem",1);
//															idFlag=false;
														}
														if(idsBackItem==1&&idsFrontItem!=1){
//															MyApplication.loadImage(HttpConfig.HTTP_IMAGE_QUERY_URL+userFaceId,idPicBack,null);
															 new XutilsHttp(activity).display(idPicBack,HttpConfig.HTTP_IMAGE_QUERY_URL+userFaceId);	
															setUsername("idsBackItem",0);
															setUsername("idFlag",0);
//															idsBackItem=0;			
//															idFlag=false;
														}
													}
													else{
//														MyApplication.loadImage(HttpConfig.HTTP_IMAGE_QUERY_URL+userFaceId,PayImgae,null);	
														 new XutilsHttp(activity).display(PayImgae,HttpConfig.HTTP_IMAGE_QUERY_URL+userFaceId);	
													}
												}
										} catch (JSONException e) {
											e.printStackTrace();
											if(idsFrontItem==1){
												setUsername("idsFrontItem",0);
												setUsername("idFlag",0);
//												idsFrontItem=0;
//												idFlag=false;
											}else if(idsBackItem==1){
												setUsername("idsFrontItem",0);
												setUsername("idFlag",0);
//												idsBackItem=0;
//												idFlag=false;
											}
										}
									};
									public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
										Log.e("jdjjdjd","失败啦！！！！");
										super.onFailure(statusCode, headers, responseString, throwable);
									};
								});
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							
						}
					}
					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						Log.e("jdjjdjd","失败啦！！！！");
						super.onFailure(statusCode, headers, responseString, throwable);
					}
					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						super.onFailure(statusCode, headers, throwable, errorResponse);
						Toast.makeText(activity, "请求超时，请重试。", Toast.LENGTH_SHORT).show();
						myDialog.dismiss();
					}
				});
			}
	
	
	
		 private SharedPreferences getSharedPreferences(String string, int i) {
		// TODO Auto-generated method stub
		return null;
	}
			private int getUsername(String str) { 
				SharedPreferences pref =activity.getPreferences(Activity.MODE_PRIVATE); 
				int username = pref.getInt(str, 0);//如果没有，默认为"" 
				return username; 
				} 
			public void setUsername(String str,int i) { 
				SharedPreferences pref =activity.getPreferences(Activity.MODE_PRIVATE); 
				Editor edit = pref.edit();
				edit.putInt(str, i); 
				edit.commit(); 
				} 
}
