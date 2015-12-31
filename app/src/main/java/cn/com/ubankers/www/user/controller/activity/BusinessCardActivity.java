package cn.com.ubankers.www.user.controller.activity;


import java.io.File;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.utils.PhotoUtil;
import cn.com.ubankers.www.utils.XutilsHttp;
import cn.com.ubankers.www.widget.MyDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BusinessCardActivity extends Activity implements OnClickListener{
	private String[] items = new String[] { "选择本地图片", "拍照" };
	private String path;
	private BusinessCardActivity context;
	private AsyncHttpClient client;
	private UserBean userBean;
	private View mingpianbg_back;
	boolean flag;
	private String errorCode;
	private String userFaceId;
	private ImageView mingpian_tu;
	private Button mingpian_queding;
	private MyDialog progressDialog;
	private View newupload;
	private View mp_uploads;
	private Object up;
	private Button newup;
	private int STATEVARIABLES=0;
	private Handler handler;
	private TextView cadlog_redact;
	private ImageView bankcar_mark;
	private TextView bankcar_reason;
	public static int MY_SCAN_REQUEST_CODE = 100;
	@Override
	
    //Catalog_Failure_reason
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context =BusinessCardActivity.this;
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.mingpianrenzhenactivty);
		client = MyApplication.app.getClient(context);
	    Intent intent=getIntent();
		userBean=(UserBean) intent.getSerializableExtra("userBean");
		
		 if (progressDialog == null){
				progressDialog = MyDialog.createDialog(this,"正在加载中...");
				}
		 initView ();	 
		if(userBean.getBusinessCard()==null&&userBean.getFileid()==null){
			STATEVARIABLES=1;//未上传		
		}else if(userBean.getBusinessCard()!=null&&userBean.getBusinessCard().equals("2")){
			STATEVARIABLES=2;
			cadlog_redact.setVisibility(ImageView.VISIBLE);
			bankcar_mark.setVisibility(View.VISIBLE);
			bankcar_reason.setText("失败原因："+userBean.getCatalog_Failure_reason());
//		    mingpian_queding.setBackgroundColor(Color.rgb(14,73,140));
//			newup.setVisibility(View.VISIBLE);
//			newup.setOnClickListener(this);
			upaddminpian();	
		}else if(userBean.getBusinessCard()!=null&&userBean.getBusinessCard().equals("0")||userBean.getBusinessCard().equals("1")){
				 mingpian_queding.setBackgroundColor(Color.rgb(14,73,140));
				yishangchjuan();
			}
			
		}	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			 Intent intent = new Intent(context,SafetyCenterActivity.class);
			 intent.putExtra("businessCard",userBean.getFileid());
			 setResult(MY_SCAN_REQUEST_CODE,intent);
			 finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	public void yishangchjuan (){
		STATEVARIABLES=2;//审核中
		progressDialog.show();
		 new XutilsHttp(this).display(mingpian_tu,HttpConfig.HTTP_IMAGE_QUERY_URL+userBean.getFileid());
		mingpian_queding.setText("返回");
		progressDialog.dismiss();
	}
	
	public void initView (){
		mingpianbg_back=(View)findViewById(R.id.mingpianbg_back);
		mingpian_tu=(ImageView)findViewById(R.id.mingpian_tu);
		mingpian_queding=(Button)findViewById(R.id.mingpian_queding);	
		cadlog_redact=(TextView)findViewById(R.id.cadlog_redact);
		bankcar_mark=(ImageView)findViewById(R.id.bankcar_mark);
		bankcar_reason=(TextView)findViewById(R.id.bankcar_reason);
		cadlog_redact.setOnClickListener(this);
		mingpianbg_back.setOnClickListener(this);
		mingpian_tu.setOnClickListener(this);
		mingpian_queding.setOnClickListener(this);
	}
	
	public void upaddminpian(){
		mingpian_queding.setText("返回");
		mingpian_tu.setImageResource(R.drawable.jiazai);
		new XutilsHttp(this).display(mingpian_tu,HttpConfig.HTTP_IMAGE_QUERY_URL+userBean.getFileid());
	}
	
	private void showDialog() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
				.setTitle("上传身份证")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							PhotoUtil.doPickPhotoFromGallery(
									BusinessCardActivity.this, false);
							break;
						case 1:
							PhotoUtil.doTakePhoto(BusinessCardActivity.this);
							break;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {

			if (requestCode == PhotoUtil.CAMERA_WITH_DATA) {// 拍照获取图片
				path = PhotoUtil.getTakePhotoFile().getAbsolutePath();
				if (TextUtils.isEmpty(path)) {
					Toast.makeText(BusinessCardActivity.this, "发送失败",
							Toast.LENGTH_SHORT).show();
					return;
				}
			    getImageToView(path);
			}
			if (requestCode == PhotoUtil.PHOTO_PICKED_WITH_DATA) {// 从相册中获取
				path = PhotoUtil.getPicPath(data.getData(), this);
				if (TextUtils.isEmpty(path)) {
					Toast.makeText(BusinessCardActivity.this, "发送失败",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (data != null) {
					getImageToView(path);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void getImageToView(String path) {
		if (path != null) {
			Bitmap bitmap = PhotoUtil.getimage(path);
			Bitmap compressImage = PhotoUtil.compressImage(bitmap);
			String imageUrl = PhotoUtil.bitmaptoString(bitmap);
			mingpian_tu.setImageBitmap(compressImage);
			uploadingAvatar(imageUrl);
		}
	}
	
	public void uploadingAvatar(String imageUrl) {
		progressDialog.show();
		RequestParams params = new RequestParams();
		params.put("picData", imageUrl);
		client.post(BusinessCardActivity.this, HttpConfig.URL_UPLOADPHOTO,params, new JsonHttpResponseHandler() {
				@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							flag = response.getBoolean("success");
							if (flag) {
								JSONObject jsonObject = response
										.getJSONObject("result");
								errorCode = jsonObject.getString("errorCode");
								JSONObject info = jsonObject.getJSONObject("info");
								userFaceId = info.getString("ids");
								handler.obtainMessage(1).sendToTarget();
								progressDialog.dismiss();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						System.out.println("失败");
						progressDialog.dismiss();
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}
					@Override
					public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable, errorResponse);
						Toast.makeText(context, "请求超时，请重试。", Toast.LENGTH_SHORT).show();
						progressDialog.dismiss();
					}
				});
	
	}

	public void upshengmingpian(int i ) {
		if(i==0&&userFaceId==null){
			Toast.makeText(BusinessCardActivity.this,"请重新选择名片上传",Toast.LENGTH_SHORT).show();
		}else if(i==1&&userFaceId==null){
			Toast.makeText(BusinessCardActivity.this,"网络连接超时，请重新选择名片上传",Toast.LENGTH_SHORT).show();
		}else{
		progressDialog.show();
		
		if (flag == true && errorCode.equals("success")) {
			StringEntity entity1 = null;
			try {
				JSONArray json = new JSONArray();
				JSONObject  json1= new JSONObject();
				json1.put("catalog", "businessCard");
				json1.put("name", "fileid");
				json1.put("value", userFaceId);
				json.put(json1);
				JSONObject  json2= new JSONObject();
				json2.put("catalog", "businessCard");
				json2.put("name", "status");
				json2.put("value", "0");
				json.put(json2);
				entity1 = new StringEntity(json.toString(), "utf-8");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			client.post(BusinessCardActivity.this, HttpConfig.URL_AUTHENTICATION, entity1,
					"application/json", new JsonHttpResponseHandler() {
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							try {
								boolean flag2 = response.getBoolean("success");
								System.out.println("success+++" + flag2);
								JSONObject jsonObject2 = response.getJSONObject("result");
								String errorCode = jsonObject2.getString("errorCode");
								System.out.println("errorCode++" + errorCode);
								if (flag2 == true&& errorCode.equals("success")) {
									userBean.setBusinessCard("0");
									userBean.setFileid(userFaceId);
									MyApplication.app.setUser(userBean);
									progressDialog.dismiss();
									Toast.makeText(BusinessCardActivity.this,"名片已上传成功",Toast.LENGTH_SHORT).show();
								    Intent intent = new Intent(context,SafetyCenterActivity.class);
								    intent.putExtra("businessCard",userBean.getBusinessCard());
								    setResult(MY_SCAN_REQUEST_CODE,intent);
								    finish();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						};

						public void onFailure(int statusCode, Header[] headers,
								String responseString, Throwable throwable) {

							super.onFailure(statusCode, headers,
									responseString, throwable);
						};
						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							// TODO Auto-generated method stub
							super.onFailure(statusCode, headers, throwable, errorResponse);
							Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
							progressDialog.dismiss();
						}
					});
		  }
		}
		
	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.mingpianbg_back:
			Intent intent = new Intent(context,SafetyCenterActivity.class);
			intent.putExtra("businessCard",userBean.getBusinessCard());
			setResult(MY_SCAN_REQUEST_CODE,intent);
			finish();
			break;
		case R.id.mingpian_tu:
			if(STATEVARIABLES==1||STATEVARIABLES==3){
				showDialog();
				break;
			}else if(STATEVARIABLES==2){
				Intent TouchActivity = new Intent(BusinessCardActivity.this,Touch.class);
				if(userBean.getFileid()!=null){
					TouchActivity.putExtra("url",HttpConfig.HTTP_IMAGE_QUERY_URL+userBean.getFileid());
					startActivity(TouchActivity);
					break;
					}
			}	
		case R.id.mingpian_queding:
			if(STATEVARIABLES==1){
				upshengmingpian(1);
				break;
			}else if(STATEVARIABLES==2){
				Intent SafetyCenterActivity = new Intent(context,SafetyCenterActivity.class);
				SafetyCenterActivity.putExtra("businessCard",userBean.getFileid());
				 setResult(MY_SCAN_REQUEST_CODE,SafetyCenterActivity);
				 finish();
				 break;
			}else if(STATEVARIABLES==3){
				Intent intent1 = new Intent(context,SafetyCenterActivity.class);
				intent1.putExtra("businessCard","0");
				setResult(MY_SCAN_REQUEST_CODE,intent1);
				finish();
			    break;
			
		}
	case R.id.cadlog_redact:
			mingpian_tu.setImageResource(R.drawable.mingpianbg);
			mingpian_queding.setText("确定");
			bankcar_mark.setVisibility(View.GONE);
			bankcar_reason.setText("");
			STATEVARIABLES=1;
			 break;
		
	}

  handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what){
			case 1:
				
			}
			
		};
	};
	}
}
