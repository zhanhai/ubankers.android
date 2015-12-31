package cn.com.ubankers.www.user.controller.activity;
import com.loopj.android.http.AsyncHttpClient;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.service.SafetyCenterService;
import cn.com.ubankers.www.user.service.SettingService;
import cn.com.ubankers.www.utils.NetReceiver;
import cn.com.ubankers.www.utils.UpdateUtils;
import cn.com.ubankers.www.utils.NetReceiver.NetState;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity implements OnClickListener {
	public AsyncHttpClient client;
	private UserBean userBean;
	private Intent intent;
	private Bundle bundle;
	private LinearLayout layout_about_us;
	private LinearLayout layout_holien;
	private LinearLayout layout_update_version;
	private UpdateUtils updateUtils;
	private View bakc, layout_change_password, logout;
	public Context context;
	private SettingService settingService;
	private NetState connected;
	public boolean bExit = false;
	private SafetyCenterService safetycenterService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_setting);
		context = this;
		client = MyApplication.app.getClient(context);
		ShareSDK.initSDK(this);
		if (MyApplication.app.getUser() != null) {
			userBean = MyApplication.app.getUser();
		}
		settingService = new SettingService(context, bExit);
		safetycenterService=new SafetyCenterService(context);
		connected = NetReceiver.isConnected(this);
		intent = this.getIntent();
		bundle = intent.getExtras();
		if (bundle != null) {
			userBean = (UserBean) bundle.getSerializable("userBean");
		}
		if (connected == connected.NET_NO) {
			Toast.makeText(this, "当前网络不可用", Toast.LENGTH_SHORT).show();
		} else {

		}
		initView();
		MyApplication.getInstance().addActivity(this);
	}

	// 初始化控件
	private void initView() {
		// custome_phone = (TextView) findViewById(R.id.custome_phone);
		layout_about_us = (LinearLayout) findViewById(R.id.layout_about_us);
		layout_holien = (LinearLayout) findViewById(R.id.layout_holien);
		layout_update_version = (LinearLayout) findViewById(R.id.layout_update_version);
		layout_change_password = findViewById(R.id.layout_change_password);
		logout = findViewById(R.id.exit_login);
		bakc = findViewById(R.id.back);
		layout_about_us.setOnClickListener(this);
		layout_update_version.setOnClickListener(this);
		bakc.setOnClickListener(this);
		layout_holien.setOnClickListener(this);
		layout_change_password.setOnClickListener(this);
		logout.setOnClickListener(this);
		findViewById(R.id.layout_help_center).setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.layout_help_center:
			Toast.makeText(SettingActivity.this, "该功能正在开发中", Toast.LENGTH_SHORT).show();
			break;
		case R.id.layout_about_us:
			Intent intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:400-856-6600"));
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			}
			break;
		case R.id.layout_update_version:
			updateUtils = new UpdateUtils(SettingActivity.this, client);
			updateUtils.getUpdateData(1);
			break;

		case R.id.back:
			SettingActivity.this.finish();
			break;

		case R.id.layout_holien:
			settingService.intentToAbourt();
			break;

		case R.id.layout_change_password:
			Intent intent_change_Password = safetycenterService.Change_Password();
			startActivity(intent_change_Password);
			break;
		case R.id.exit_login:
			settingService.logout();
			SettingActivity.this.finish();
			break;

		}
	}
}
