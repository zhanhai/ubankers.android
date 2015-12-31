package cn.com.ubankers.www.user.controller.activity;

import java.io.File;
import java.util.ArrayList;




















import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.product.view.ProductFragmentAdapter;
import cn.com.ubankers.www.user.controller.fragment.NoRegisteredFragment;
import cn.com.ubankers.www.user.controller.fragment.RegisteredFragment;
import cn.com.ubankers.www.user.model.ReserverDetailsBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.service.ReserverDetailsService;
import cn.com.ubankers.www.utils.PhotoUtil;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author 廖准华
 *预约详情
 */
public class ReserverDetailsActivity extends FragmentActivity implements OnClickListener{
	private UserBean userBean;
	private Context context;
	private String path;
	private String productId,productName;
	private ListView details_lv;
	private TextView tv_details_productName;
	private ArrayList<ReserverDetailsBean> list = new ArrayList<ReserverDetailsBean>();
    private LinearLayout title_bar_back_btn;
    @SuppressWarnings("unused")
	private boolean idFlag=false;
    private ReserverDetailsService rds;
    private String[] items=new String[]{"选择本地图片", "拍照"};
	/*请求码*/
	private  final int RESULT_REQUEST_CODE = 2;
	private View convention_record_back;
	private ViewPager convention_viewPager;
	private View noconvention_recordview;
	private TextView noconvention_recordtext;
	private TextView convention_record_text;
    public final static int investment_1 = 0;
	public final static int investment_2 = 1;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private View convention_recordview;
	private TextView tv_convention_telt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.order_details);
		setContentView(R.layout.convention_classification_activity);
		if(MyApplication.app.getUser()!=null){
			userBean=MyApplication.app.getUser();
		}
		context = this;
		Intent intent = getIntent();
		if(intent!=null){
			productId=intent.getStringExtra("productId");
			productName=intent.getStringExtra("productName");
		}
		initView();
		rds = new ReserverDetailsService(userBean.getUserId(), productId, list, details_lv,ReserverDetailsActivity.this);
		tv_convention_telt.setText(productName);
	}



	private void initView() {	
		convention_record_back= (View)findViewById(R.id.convention_record_back);
		convention_record_text=(TextView)findViewById(R.id.convention_record_text);
		noconvention_recordtext=(TextView)findViewById(R.id.noconvention_recordtext);
		noconvention_recordview=(View)findViewById(R.id.noconvention_recordview);
		convention_recordview=(View)findViewById(R.id.convention_recordview);
		convention_viewPager=(ViewPager)findViewById(R.id.convention_viewPager);
		tv_convention_telt = (TextView) findViewById(R.id.convention_telt);
		convention_record_back.setOnClickListener(this);
		convention_record_text.setOnClickListener(this);
		noconvention_recordtext.setOnClickListener(this);
		convention_record_text.setTextColor(Color.rgb(64, 64, 66));
		noconvention_recordtext.setTextColor(Color.rgb(200, 199, 204));
		convention_recordview.setVisibility(View.VISIBLE);
		noconvention_recordview.setVisibility(View.GONE);
		initFragment();
//		details_lv = (ListView) findViewById(R.id.details_lv);
//		tv_details_productName = (TextView) findViewById(R.id.details_productName);
//		title_bar_back_btn = (LinearLayout) findViewById(R.id.title_bar_back_btn);
//		title_bar_back_btn.setOnClickListener(this);
	}
	
	private void initFragment() {
		Bundle bundle = new Bundle();
		RegisteredFragment registeredFragment = new RegisteredFragment();
		bundle.putSerializable("userBean",userBean);
		bundle.putString("productId", productId);
		registeredFragment.setArguments(bundle);
		NoRegisteredFragment noregisteredFragment = new NoRegisteredFragment();
		bundle.putSerializable("userBean",userBean);
		bundle.putString("productId", productId);
		noregisteredFragment.setArguments(bundle);
		fragments.add(registeredFragment);
		fragments.add(noregisteredFragment);
		ProductFragmentAdapter mAdapetr = new ProductFragmentAdapter(
		getSupportFragmentManager(), fragments);
		convention_viewPager.setOffscreenPageLimit(0);
		convention_viewPager.setAdapter(mAdapetr);
		convention_viewPager.setOnPageChangeListener(pageListener);
	}
	
	public OnPageChangeListener pageListener = new OnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int position) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}
		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			switch (arg0) {
			case investment_1:
				convention_record_text.setTextColor(Color.rgb(64, 64, 66));
				noconvention_recordtext.setTextColor(Color.rgb(200, 199, 204));
				convention_recordview.setVisibility(View.VISIBLE);
				noconvention_recordview.setVisibility(View.GONE);
				convention_viewPager.setCurrentItem(0);		
				break;
			case investment_2:
				convention_record_text.setTextColor(Color.rgb(200, 199, 204));
				noconvention_recordtext.setTextColor(Color.rgb(64, 64, 66));
				convention_recordview.setVisibility(View.GONE);
				noconvention_recordview.setVisibility(View.VISIBLE);
		        convention_viewPager.setCurrentItem(1);
				break;
			}
		}
	};
	@Override
	public void onClick(View view) {
		switch (view.getId()){
		case R.id.convention_record_back:	
			finish();
			break;		
		case R.id.convention_record_text:
			convention_record_text.setTextColor(Color.rgb(64, 64, 66));
			noconvention_recordtext.setTextColor(Color.rgb(200, 199, 204));
			convention_recordview.setVisibility(View.VISIBLE);
			noconvention_recordview.setVisibility(View.GONE);
	        convention_viewPager.setCurrentItem(0);
			break;
		case R.id.noconvention_recordtext:
			convention_record_text.setTextColor(Color.rgb(200, 199, 204));
			noconvention_recordtext.setTextColor(Color.rgb(64, 64, 66));
			convention_recordview.setVisibility(View.GONE);
			noconvention_recordview.setVisibility(View.VISIBLE);
	        convention_viewPager.setCurrentItem(1);
			break;
		}
		
		
	}
	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
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
					Toast.makeText(ReserverDetailsActivity.this,"发送失败",
							Toast.LENGTH_SHORT).show();
					return;
				}
				File file = new File(path);
				if (file.exists()) {
					PhotoUtil.doCropPhoto(ReserverDetailsActivity.this, file);
				}
			}
			if (requestCode == PhotoUtil.PHOTO_PICKED_WITH_DATA) {//从相册中获取
				path = PhotoUtil.getPicPath(data.getData(), this);
				if (TextUtils.isEmpty(path)) {
					Toast.makeText(ReserverDetailsActivity.this,"发送失败",
							Toast.LENGTH_SHORT).show();
					return;
				}
//				Bitmap bitmap = BitmapFactory.decodeFile(path);
//				path=PhotoUtil.saveThePicture(bitmap);
				File file = new File(path);
				if (file.exists()) {
					PhotoUtil.doCropPhoto(ReserverDetailsActivity.this, file);
				}
			}
			if (requestCode == PhotoUtil.CROP_PHOTO_WITH_DATA) {//裁剪
				if(data!=null){
					rds.getImageToView(path);
				}
			}
		}else{
			   idFlag =false;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	/*选择显示对话框*/
	public void showDialog() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(ReserverDetailsActivity.this)
		.setTitle("设置头像")
		.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					PhotoUtil.doPickPhotoFromGallery(ReserverDetailsActivity.this,false);
					break;
				case 1:
					PhotoUtil.doTakePhoto(ReserverDetailsActivity.this);
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


}
