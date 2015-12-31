package cn.com.ubankers.www.user.controller.activity;


import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.model.WealthBean;
import cn.com.ubankers.www.user.service.UserCenterService;
import cn.com.ubankers.www.utils.CompleteDialog;
import cn.com.ubankers.www.utils.LoginDialog;
import cn.com.ubankers.www.utils.PhotoUtil;
import cn.com.ubankers.www.widget.CircleImg;
import cn.com.ubankers.www.widget.MyDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;


public class UserCenterActivity extends Activity implements OnClickListener{
	private String path;
	public  CircleImg  faceImage; 
	private  ImageView  title_bar_back_btn;
	private String[] items=new String[]{"选择本地图片", "拍照"};
	private SQLiteDatabase db;	
	private LinearLayout layout_myrong,layout_capital_manager,layout_buy_record,layout_myrongf;
	public  TextView login_regist_tv,recent_nickname;
	private View setting;
	private ImageView cfmpHourse_img,requestNo,denlu,cfmpimg;
	/*头像名称*/
	private static final String IMAGE_FILE_NAME="faceImage.jpg";
	/*请求码*/
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	public static  LinearLayout layout_cfmp,layout_fundraiser,layout_investor;
	public  static RelativeLayout userCenterHead; 
	public  static FrameLayout fl_bg;
	public  TextView fundraiser_keyongyue,cfmpHourse;
	private LinearLayout product_status_slect;
	private LinearLayout layout_product_cast_manager;
	private LinearLayout layout_cfmpHourse;
	public TextView investor_yue;
	public TextView available_balance;
	public TextView role;
	public  Context context;
	private UserCenterService userCenterService;
	public  ImageView editorView;
	private WealthStudioActivity wealthStudioActivity;
	private WealthBean wealthBean;
	private AsyncHttpClient client;
	private String info;
	private MyDialog myDialog;
	private Intent intent = null;
	private LoginDialog loginDialog;
	private AlertDialog dialog;
	private View view;
	private EditText treasureHourse_name;
	private Button buildView,canclell;
	private LinearLayout rl3;
	private CompleteDialog completeDialog;
	private UserBean userBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		this.setContentView(R.layout.usercenter_activity);
		context = this;
		client = MyApplication.app.getClient(context);
		
		initView();
		
//		registerBoradcastReceiver();
		initUserInfo();
		
		Intent intent =this.getIntent();
		if(intent!=null){
			wealthBean = (WealthBean) intent.getSerializableExtra("wealthBean");
		}
		MyApplication.getInstance().addActivity(this);
	}
	
	private void initUserInfo() {
		userBean=MyApplication.app.getUser();
		client = MyApplication.app.getClient(context);
		userCenterService = new UserCenterService(context);
		if(userBean==null){
			loginDialog = new LoginDialog(UserCenterActivity.this,0,0);
			loginDialog.onLogin();	
			return;
		}
		if(userBean==null){
			setting.setVisibility(View.GONE);
			fl_bg.setVisibility(View.GONE);
		}else{
			fl_bg.setVisibility(View.VISIBLE);
			setting.setVisibility(View.VISIBLE);
			setting.setOnClickListener(this);//设置
			userCenterHead.setVisibility(View.VISIBLE);
		}
		
	}
    
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		initView();
//		registerBoradcastReceiver();
		initUserInfo();
		
	}
	
	private void initView() {
		layout_cfmp = (LinearLayout) findViewById(R.id.layout_cfmp);
		layout_fundraiser = (LinearLayout) findViewById(R.id.layout_fundraiser);
		layout_investor = (LinearLayout) findViewById(R.id.layout_investor);
		fundraiser_keyongyue=(TextView) findViewById(R.id.fundraiser_keyongyue);
		role=(TextView) findViewById(R.id.jueseming);//角色名称
		investor_yue =(TextView)findViewById(R.id.investor_yue);
		userCenterHead=(RelativeLayout) findViewById(R.id.userCenterHead);
		editorView=(ImageView) findViewById(R.id.editorView);
		recent_nickname = (TextView)findViewById(R.id.recent_nickname);
		editorView.setOnClickListener(this);
		fl_bg=(FrameLayout) findViewById(R.id.fl_bg);
		rl3 = (LinearLayout)findViewById(R.id.rl3);
		rl3.setOnClickListener(this);
		faceImage=(CircleImg) findViewById(R.id.avatar);
		setting =findViewById(R.id.setting);
		faceImage.setOnClickListener(this);
		//投资者
		findViewById(R.id.layout_investment_record).setOnClickListener(this);//投资记录
		findViewById(R.id.layout_cfmp_recommend).setOnClickListener(this);// 财富师推荐产品			
		findViewById(R.id.layout_mycfmp).setOnClickListener(this);//我的财富师
		findViewById(R.id.layout_capital_managerp).setOnClickListener(this);//资金管理
		findViewById(R.id.layout_myrongp).setOnClickListener(this);//我的融圈儿		
		findViewById(R.id.layout_Personal_informationc).setOnClickListener(this);//个人信息
		//财富师
		findViewById(R.id.layout_capital_manager).setOnClickListener(this);//资金管理
		findViewById(R.id.layout_buy_record).setOnClickListener(this);//购买记录
		findViewById(R.id.layout_myrong).setOnClickListener(this);//我的融圈儿
		findViewById(R.id.layout_myclient).setOnClickListener(this);//我的客户
		findViewById(R.id.layout_order_manager).setOnClickListener(this);//预约管理
		findViewById(R.id.layout_invitefriend).setOnClickListener(this);//财富师推荐财富师
        findViewById(R.id.layout_cfmpHourse).setOnClickListener(this);//财富师工作室
        findViewById(R.id.cfmpHourse).setOnClickListener(this);//财富师工作室
        findViewById(R.id.cfmpimg).setOnClickListener(this);
        findViewById(R.id.cfmpHourse_img).setOnClickListener(this); //财富师工作室
        findViewById(R.id.requestNo).setOnClickListener(this); //邀请加入工作室通知       
        findViewById(R.id.layout_Personal_informationp).setOnClickListener(this);//个人信息
      //资本师
		findViewById(R.id.layout_myrongf).setOnClickListener(this);//我的融圈儿
		findViewById(R.id.layout_capital_managerf).setOnClickListener(this);//资金管理
		findViewById(R.id.product_status_slectf).setOnClickListener(this);//产品状态查询
		findViewById(R.id.layout_product_cast_managerf).setOnClickListener(this);//产品投后管理
		findViewById(R.id.layout_Personal_informationf).setOnClickListener(this);//个人信息
//		login_regist_tv.setOnClickListener(new OnClickLoginListener());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			//backMothed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/*//返回主界面的方法
	private void backMothed(){
		//Intent intent=new Intent(UserCenterActivity.this, MainActivity.class);
		//startActivity(intent);
		UserCenterActivity.this.finish();
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
	}*/
	/*@Override
	public void onBackPressed() {
		backMothed();
		super.onBackPressed();
	}*/
	/*选择显示对话框*/
		private void showDialog() {
			// TODO Auto-generated method stub
			new AlertDialog.Builder(this)
			.setTitle("设置头像")
			.setItems(items, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:
						PhotoUtil.doPickPhotoFromGallery(UserCenterActivity.this,false);
						break;
					case 1:
						PhotoUtil.doTakePhoto(UserCenterActivity.this);
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
			
			Log.i("TAG", "onActivityResult"+"requestCode="+requestCode+"\n resultCode="+resultCode+"\n data="+data);
			
			//结果码不等于取消时候
			if (resultCode != RESULT_CANCELED) {

				if (requestCode == PhotoUtil.CAMERA_WITH_DATA) {//拍照获取图片
					path = PhotoUtil.getTakePhotoFile().getAbsolutePath();
					if (TextUtils.isEmpty(path)) {
						Toast.makeText(UserCenterActivity.this,"发送失败",
								Toast.LENGTH_SHORT).show();
						return;
					}
					File file = new File(path);
					if (file.exists()) {
						PhotoUtil.doCropPhoto(UserCenterActivity.this, file);
					}
				}
				if (requestCode == PhotoUtil.PHOTO_PICKED_WITH_DATA) {//从相册中获取
					path = PhotoUtil.getPicPath(data.getData(),this);
					if (TextUtils.isEmpty(path)) {
						Toast.makeText(UserCenterActivity.this,"发送失败",
								Toast.LENGTH_SHORT).show();
						return;
					}
//					Bitmap bitmap = BitmapFactory.decodeFile(path);
//					path=PhotoUtil.saveThePicture(bitmap);
					File file = new File(path);
					if (file.exists()) {
						PhotoUtil.doCropPhoto(UserCenterActivity.this, file);
					}
				}
				if (requestCode == PhotoUtil.CROP_PHOTO_WITH_DATA) {//裁剪
					if(data!=null){
						getImageToView(path);
					}
				}
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
		/**
		 * 裁剪图片方法实现
		 * 
		 * @param uri
		 */	
		public void startPhotoZoom(Uri uri) {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			// 设置裁剪
			intent.putExtra("crop", "true");
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			// outputX outputY 是裁剪图片宽高
			intent.putExtra("outputX", 320);
			intent.putExtra("outputY", 320);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, RESULT_REQUEST_CODE);
		}
		/**
		 * 保存裁剪之后的图片数据
		 * 
		 * @param picdata
		 */
		private void getImageToView(String path) {
			if (path != null) {
				Bitmap bitmap = PhotoUtil.getimage(path);
				String imageUrl = PhotoUtil.bitmaptoString(bitmap);
				userCenterService.uploadingAvatar(imageUrl);
			}
		}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.capital_manager://资金管理
				Intent intent = new Intent(context, FundsManageActivity.class);
				startActivity(intent);
				break;
		case R.id.layout_capital_managerp://投资者
			Intent intent2 = new Intent(context, FundsManageActivity.class);
			startActivity(intent2);
		    break;
		case R.id.layout_myrongp:
			Toast.makeText(UserCenterActivity.this, "该功能正在开发中", Toast.LENGTH_SHORT).show();
		    break;
		case R.id.layout_myrongf:
			Toast.makeText(UserCenterActivity.this, "该功能正在开发中", Toast.LENGTH_SHORT).show();
			break;
		case R.id.layout_capital_managerf://资本师资金管理
			Intent intent3 = new Intent(context, FundsManageActivity.class);
			startActivity(intent3);
			break;
	    case R.id.layout_myrong: //我的融圈儿
			    Toast.makeText(UserCenterActivity.this, "该功能正在开发中", Toast.LENGTH_SHORT).show();
			    break;
		case R.id.layout_capital_manager: //财富师资金管理
		    	Intent intent1 = new Intent(context, FundsManageActivity.class);
				startActivity(intent1);
		    	break;
	    case R.id.layout_buy_record:
		    	Intent intent_BuyRecor =new Intent(context,BuyRecordActivity.class);
		    	startActivity(intent_BuyRecor);
		    	break;
		case R.id.layout_investment_record://投资记录
				Intent intent_record = new Intent(context,InvestorOrderActivity.class);
				startActivity(intent_record);
				break;
		case R.id.layout_cfmp_recommend://财富师推荐产品
				Intent intent_recommend = new Intent(context,CfmpRecommendPtsActivity.class);
				startActivity(intent_recommend);
				break;
		case R.id.layout_mycfmp://我的财富师	
				Intent intent_mycfmp = new Intent(context,InverstorToCfmpActivity.class);
				startActivity(intent_mycfmp);
				break;
		case R.id.product_status_slectf://产品状态查询
				Toast.makeText(UserCenterActivity.this, "该功能正在开发中", Toast.LENGTH_SHORT).show();
//				Intent intent_slect = new Intent(context,CfmpStateAcyivity.class);
//				startActivity(intent_slect);
				break;
		case R.id.layout_product_cast_managerf://产品投后管理
				Toast.makeText(UserCenterActivity.this, "该功能正在开发中", Toast.LENGTH_SHORT).show();
//				Intent intent_manager = new Intent(context,CfmpManagerAcyivity.class);
//				startActivity(intent_manager);
				break;
		case R.id.layout_myclient://我的客户
				Intent intent_myclient = new Intent(context,CustomerActivity.class);
				startActivity(intent_myclient);
				break;
		case R.id.layout_order_manager://预约管理
				Intent intent_order_manager = new  Intent(context,OrderManagementActivity.class);
				startActivity(intent_order_manager);
				break;
		case R.id.layout_invitefriend://财富师推荐财富师
				Intent intent_invitefriend = new Intent(context, CfmpRecommerActivity.class);
				startActivity(intent_invitefriend);
				break;
		case R.id.layout_cfmpHourse://财富工作室
		case R.id.cfmpimg:
		case R.id.cfmpHourse:
		case R.id.cfmpHourse_img:
		userCenterService.checkTreasure();
				break;
			case R.id.requestNo:
				break;
			case R.id.title_bar_back_btn://返回
				//backMothed();
				break;
			case R.id.avatar://设置头像
				if(userCenterService.userBean!=null&&!userCenterService.userBean.equals("tourist")){
					showDialog();
				}else{
					loginDialog.onLogin();
				}
				break;
			case R.id.setting://设置
				Intent intent_setting = new Intent(getApplicationContext(),SettingActivity.class);
				startActivity(intent_setting);
				break;
			case R.id.editorView:
			case R.id.rl3:
				modifyTreasures();
				break;
			case R.id.layout_Personal_informationc://财富师的个人信息
				Intent intent_informationc = new Intent(context, SafetyCenterActivity.class);
				context.startActivity(intent_informationc);
				break;
			case R.id.layout_Personal_informationp://投资者的个人信息
				Intent intent_informationp = new Intent(context, SafetyCenterActivity.class);
				context.startActivity(intent_informationp);
				break;
			case R.id.layout_Personal_informationf://资本师的个人信息
				Intent intent_informationf = new Intent(context, SafetyCenterActivity.class);
				context.startActivity(intent_informationf);
				break;
			default:
				break;
		}
		
	}
	
	
//	InputFilter emojiFilter = new InputFilter ( ) {
//
//		Pattern emoji = Pattern . compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern . UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
//		@Override
//		public CharSequence filter ( CharSequence source , int start ,
//				int end , Spanned dest , int dstart ,int dend ) {
//
//		Matcher emojiMatcher = emoji . matcher ( source ) ;
//
//		if ( emojiMatcher . find ( ) )
//		{
//		return "" ;
//		}
//		return null ;
//		}
//		} ;
	
	
	
	public void modifyTreasures(){
	
		String check1 = "[\\u4e00-\\u9fa5|\\w]+";
		final Pattern regex2 = Pattern.compile(check1);
			
		 dialog = new AlertDialog.Builder(this).create();
			dialog.setCanceledOnTouchOutside(false);
			view = LayoutInflater.from(this).inflate(R.layout.update_personalcenter_nickname, null);
			treasureHourse_name =(EditText) view.findViewById(R.id.treasureHourse_name);
			
			buildView = (Button) view.findViewById(R.id.buildView);
			canclell = (Button)view.findViewById(R.id.canclell);
			buildView.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
//						System.out.println(treasureHourse_name.getText().toString()+"222222222222");
						String nn = treasureHourse_name.getText().toString();
						Matcher m = regex2.matcher(nn);
						if(treasureHourse_name.getText().toString() == null || treasureHourse_name.getText().toString().length()==0){
							Toast.makeText(UserCenterActivity.this, "请完善用户昵称",Toast.LENGTH_SHORT).show();
						}else if(!m.matches()){
							Toast.makeText(UserCenterActivity.this, "用户名格式不正确",Toast.LENGTH_SHORT).show();
						}
						
						else{
							client.get(HttpConfig.URL_PERSONAL_NAME+treasureHourse_name.getText().toString(), new JsonHttpResponseHandler(){
						   		 @Override
						   		public void onSuccess(int statusCode, Header[] headers,
						   				JSONObject response) {
						   			 try {
						   				JSONObject obj= response.getJSONObject("result");
						   				boolean flag = response.getBoolean("success");
						   				String errCode = obj.getString("errorCode");
						   				if(flag && errCode.equals("success")){
						   					JSONObject json = obj.getJSONObject("userBase");
												try {
													userBean.setUserName((json.get("nickName")==null || json.get("nickName").toString().equals("null"))?"0":json.getString("nickName"));											
												} catch (Exception e) {
													// TODO: handle exception
												}						   					
						   					recent_nickname.setText(userBean.getUserName()); 																   					
						   					Toast.makeText(UserCenterActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
						   					dialog.dismiss();
						   				}
									} catch (Exception e) {
										e.printStackTrace();
									}
						   			
						   			super.onSuccess(statusCode, headers, response);
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
						
					}
				});
			canclell.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			dialog.setView(view);
			dialog.show();
 }
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				    ExitDialog(context).show();  
				     return true;
			}
			// 拦截MENU按钮点击事件，让他无任何操作
			if (keyCode == KeyEvent.KEYCODE_MENU) {
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}
	 /**
		 * 退出应用程序
		 * @param context
		 * @return
		 */
		 public  Dialog ExitDialog(Context context) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("退出应用");
			builder.setMessage("是否退出应用？");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					MyApplication.app.exitApply();
					MyApplication.getInstance().exit();
					System.exit(0);
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			});
			return builder.create();
		}

}
