package cn.com.ubankers.www.user.controller.activity;

import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.authentication.controller.activity.LoginActivity;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.product.service.ACache;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.utils.PhotoUtil;
import cn.com.ubankers.www.utils.Regular;
import cn.com.ubankers.www.utils.XutilsHttp;
import cn.com.ubankers.www.widget.ProcessDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class IdCardActivity extends Activity implements OnClickListener {
	private String[] items = new String[] { "选择本地图片", "拍照" };
	private String path;
	private ImageView shenfenzhenga, shenfenzhengb;
	private EditText shengfenyanheng_Name;
	private EditText shengfenyanheng_haoma;
	private AsyncHttpClient client;
	private String Name;
	private String haoma;
	/* 请求码 */
	private int SHENFENGFANMIAN;
	private int mian;
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	HashMap<String, String> listshumu = new HashMap<String, String>();
	boolean flag;
	String errorCode;
	private Button shengfengyanzheng_queding;
	private UserBean userBean;
	private IdCardActivity context;
	private View popuveiw;
	private ImageView shenfengzheng_tp;;
	private ProcessDialog myDialog;
	private LinearLayout shenfen_back;
	private int STATEVARIABLES = 1;
	private String idState="0";
	private TextView redact;
	private ACache mCache;
	private ImageView idcard_mark;
	private TextView idcard_reason;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shenfenyanzhengactivity);
		context = IdCardActivity.this;
		if (myDialog == null) {
			myDialog = ProcessDialog.createDialog(this, "正在加载中...");
		}
		client = MyApplication.app.getClient(context);
		if(MyApplication.app.getUser()!=null){
		   userBean = MyApplication.app.getUser();
		}
	   if(userBean.getUserName() != null&&userBean.getIdcard_name()==null){
		   STATEVARIABLES=1;
		}
	    mCache = ACache.get(this);
	    mCache.put("frontfid", "");
		mCache.put("backfid", "");
		initView();
		MyApplication.getInstance().addActivity(this);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(context,SafetyCenterActivity.class);
			intent.putExtra("idcard","0");
			setResult(RESULT_REQUEST_CODE, intent);
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void initView() {
		shengfengyanzheng_queding = (Button) findViewById(R.id.shengfengyanzheng_queding);
		shenfenzhenga = (ImageView) findViewById(R.id.shenfenzhenga);
		shenfenzhengb = (ImageView) findViewById(R.id.shenfenzhengb);
		redact=	(TextView)findViewById(R.id.redact);
		shenfen_back = (LinearLayout) findViewById(R.id.shenfen_back);
		shengfenyanheng_Name = (EditText) findViewById(R.id.shengfenyanheng_Name);
		shengfenyanheng_haoma = (EditText) findViewById(R.id.shengfenyanheng_haoma);
		shenfengzheng_tp = (ImageView) findViewById(R.id.shenfengzheng_tp);
		idcard_mark=(ImageView)findViewById(R.id.idcard_mark);
		idcard_reason=(TextView)findViewById(R.id.idcard_reason);
		shenfen_back.setOnClickListener(this);
		shenfenzhengb.setOnClickListener(this);	
		shenfenzhenga.setOnClickListener(this);
		shengfengyanzheng_queding.setOnClickListener(this);
		redact.setOnClickListener(this);
		if (userBean != null && userBean.getIdcard_status()!= null) {
			bangding();
			if(userBean.getIdcard_status().equals("2")){
				redact.setVisibility(View.VISIBLE);
				idcard_mark.setVisibility(View.VISIBLE);
				idcard_reason.setText("审核失败原因:"+userBean.getIdcard_Failure_reason());
			}
		}
	}
	public void bangding() {
		int changdu;
		shenfenzhenga.setImageResource(R.drawable.jiazai);
		shenfenzhengb.setImageResource(R.drawable.jiazai);
		shengfenyanheng_Name.setEnabled(false);
		shengfenyanheng_Name.setText(userBean.getIdcard_name());
		String haomaa = userBean.getIdcard_no();
		try {
			changdu = haomaa.length();
		} catch (Exception e) {
			// TODO: handle exception
			haomaa = "                           ";
			changdu = 16;
		}

		String jiequhaoma = haomaa.substring(0, changdu -10) + "*******"
				+ haomaa.substring(changdu - 4);
		shengfenyanheng_haoma.setEnabled(false);
		shengfenyanheng_haoma.setText(jiequhaoma);
		shengfengyanzheng_queding.setText("返回");
		STATEVARIABLES = 2;
		shengfengyanzheng_queding.setOnClickListener(this);
	    new XutilsHttp(this).display(shenfenzhenga,HttpConfig.HTTP_IMAGE_QUERY_URL+userBean.getIdcard_frontfid());
		new XutilsHttp(this).display(shenfenzhengb,HttpConfig.HTTP_IMAGE_QUERY_URL+ userBean.getIdcard_backfid());

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
									IdCardActivity.this, false);
							break;
						case 1://
							PhotoUtil.doTakePhoto(IdCardActivity.this);
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
		myDialog.show();
		super.onActivityResult(requestCode, resultCode, data);
		// 结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {
			if (requestCode == PhotoUtil.CAMERA_WITH_DATA) {// 拍照获取图片
				path = PhotoUtil.getTakePhotoFile().getAbsolutePath();
				if (TextUtils.isEmpty(path)) {
					Toast.makeText(IdCardActivity.this, "发送失败",
							Toast.LENGTH_SHORT).show();
					return;
				}
				getImageToView(path);
			}

			if (requestCode == PhotoUtil.PHOTO_PICKED_WITH_DATA) {// 从相册中获取
				path = PhotoUtil.getPicPath(data.getData(), this);
				if (TextUtils.isEmpty(path)) {
					Toast.makeText(IdCardActivity.this, "发送失败",
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
			String imageUrl = PhotoUtil.bitmaptoString(bitmap);
				uploadingAvatar(imageUrl,bitmap);
		}
	}

	public void uploadingAvatar(String imageUrl,final Bitmap bitmap) {
		myDialog.show();
		RequestParams params = new RequestParams();
		params.put("picData", imageUrl);
		client.post(IdCardActivity.this, HttpConfig.URL_UPLOADPHOTO, params, new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							flag = response.getBoolean("success");
							if (flag) {
								JSONObject jsonObject = response
										.getJSONObject("result");
								errorCode = jsonObject.getString("errorCode");
								JSONObject info = jsonObject
										.getJSONObject("info");
								String userFaceId = info.getString("ids");
								if (SHENFENGFANMIAN== 1) {
									mCache.put("frontfid", userFaceId);
									shenfenzhenga.setImageBitmap(bitmap);
								} else if (SHENFENGFANMIAN == 2) {
									mCache.put("backfid", userFaceId);
									shenfenzhengb.setImageBitmap(bitmap);
								}
								myDialog.dismiss();

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
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}
					@Override
					public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
						super.onFailure(statusCode, headers, throwable, errorResponse);
						Toast.makeText(context, "请求超时，请重试。", Toast.LENGTH_SHORT).show();
						myDialog.dismiss();
					}
				});
		
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(context,SafetyCenterActivity.class);
		intent.putExtra("idcard","0");
		setResult(RESULT_REQUEST_CODE, intent);
		finish();
		super.onBackPressed();
	}
	public void upshengfrenzheng() {
		myDialog.show();
		StringEntity entity1 = null;
		try {
			JSONArray json = new JSONArray();
			JSONObject json1 = new JSONObject();
			json1.put("value", Name);
			json1.put("catalog", "idcard");
			json1.put("name", "realname");
			json.put(json1);
			JSONObject json2 = new JSONObject();
			json2.put("value", haoma);
			json2.put("catalog", "idcard");
			json2.put("name", "no");		
			json.put(json2);
			JSONObject json3 = new JSONObject();
			json3.put("value", mCache.getAsString("frontfid"));
			json3.put("catalog", "idcard");
			json3.put("name", "frontfid");
			json.put(json3);
			JSONObject json4 = new JSONObject();
			json4.put("value",mCache.getAsString("backfid"));
			json4.put("catalog", "idcard");
			json4.put("name", "backfid");
			json.put(json4);
			JSONObject json5 = new JSONObject();
			json5.put("value", "0");
			json5.put("catalog", "idcard");
			json5.put("name", "status");
			json.put(json5);
			entity1 = new StringEntity(json.toString(), "utf-8");
			System.out.println(entity1);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		client.post(IdCardActivity.this, HttpConfig.URL_AUTHENTICATION,
				entity1, "application/json", new JsonHttpResponseHandler() {
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							boolean flag2 = response.getBoolean("success");				
							JSONObject jsonObject2 = response.getJSONObject("result");
							String errorCode = jsonObject2.getString("errorCode");	
							if (flag2 == true && errorCode.equals("success")) {
								userBean.setIdcard_name(Name);// 真实姓名
								userBean.setIdcard_no(haoma);// 身份证号
								userBean.setIdcard_status("0");// 状态
								userBean.setIdcard_frontfid(listshumu.get("frontfid"));
								userBean.setIdcard_backfid(listshumu.get("backfid"));
								MyApplication.app.setUser(userBean);
								idState="1";
								Toast.makeText(IdCardActivity.this,
										"资料提交成功，请耐心等待审核结果", Toast.LENGTH_SHORT)
										.show();
								Intent intent = new Intent(context,SafetyCenterActivity.class);
								intent.putExtra("idcard","0");
								setResult(RESULT_REQUEST_CODE, intent);
								IdCardActivity.this.finish();
								myDialog.show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					};

					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {

						super.onFailure(statusCode, headers, responseString,
								throwable);
					};
					
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

	@Override
	public void onClick(View v) {
		Bitmap mBitmap;
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.shenfen_back:
			//数据是使用Intent返回
            Intent intent = new Intent();
            intent.putExtra("idState", idState);
            this.setResult(RESULT_OK, intent);
			IdCardActivity.this.finish();
			break;
		case R.id.shenfenzhenga:
			SHENFENGFANMIAN = 1;
			if (STATEVARIABLES==1) {
				showDialog();
			} else if(STATEVARIABLES==2){
				Intent TouchActivity = new Intent(IdCardActivity.this,Touch.class);
				if(userBean.getIdcard_frontfid()!=null){
					TouchActivity.putExtra("url",HttpConfig.HTTP_IMAGE_QUERY_URL+userBean.getIdcard_frontfid());
					startActivity(TouchActivity);
				}else{
					Toast.makeText(IdCardActivity.this,
							"未上传图片", Toast.LENGTH_SHORT)
							.show();
				}
			}else {		
				Intent intent1 = new Intent(IdCardActivity.this,LoginActivity.class);
				startActivity(intent1);
			}

			break;

		case R.id.shenfenzhengb:
			SHENFENGFANMIAN = 2;
			if (STATEVARIABLES==1) {
				showDialog();
			} else if(STATEVARIABLES==2){
				Intent TouchActivity = new Intent(IdCardActivity.this,Touch.class);
				if(userBean.getIdcard_backfid()!=null){
					TouchActivity.putExtra("url",HttpConfig.HTTP_IMAGE_QUERY_URL+ userBean.getIdcard_backfid());
					startActivity(TouchActivity);
				     }else{
					  Toast.makeText(IdCardActivity.this,"未上传图片", Toast.LENGTH_SHORT).show();
				    }
				}else{
				Intent intent1 = new Intent(IdCardActivity.this,LoginActivity.class);
				startActivity(intent1);
					
			}
			break;

		case R.id.shengfengyanzheng_queding:
			if (STATEVARIABLES == 1) {
				Name = shengfenyanheng_Name.getText().toString().trim();
				haoma = shengfenyanheng_haoma.getText().toString().trim();
				if (Regular.checkid(haoma) == false) {
					Toast.makeText(IdCardActivity.this, "请输入合法身份证",
							Toast.LENGTH_SHORT).show();
				} else if (Name == null || Name.equals("")) {
					Toast.makeText(IdCardActivity.this, "请输入姓名",
							Toast.LENGTH_SHORT).show();
				} else {
					myDialog.show();
					upshengfrenzheng();
					myDialog.dismiss();
				}
			} else if (STATEVARIABLES == 2) {
				IdCardActivity.this.finish();
			}

			break;
			
		case R.id.redact:
			STATEVARIABLES=1;
			idcard_mark.setVisibility(View.GONE);
			idcard_reason.setText("");
			shengfengyanzheng_queding.setText("确定");
			mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.shenfenzhengb);
			shenfenzhengb.setImageBitmap(mBitmap);
			mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.shenfenzhenga);
			shenfenzhenga.setImageBitmap(mBitmap);
			shengfenyanheng_haoma.setEnabled(true);
			shengfenyanheng_Name.setEnabled(true);
			shengfenyanheng_haoma.setText("");
			shengfenyanheng_Name.setText("");
			break;
		}
	}
}