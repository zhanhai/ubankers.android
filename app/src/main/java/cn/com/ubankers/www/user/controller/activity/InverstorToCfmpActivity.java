package cn.com.ubankers.www.user.controller.activity;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.user.service.InverstorToCfmpService;










import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class InverstorToCfmpActivity extends Activity{
		private  Context context;
		private String investorId;
		public  RelativeLayout bound_cfmp;
		private EditText et_cfmp_moblie;
		public ListView listView1;
		private InverstorToCfmpService inverstorToCfmpService;
		private LinearLayout back;
		public Button bt_bound;
		private Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(16[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
		private String phone;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.mycfmp_layout);
			context = this;
			inverstorToCfmpService = new InverstorToCfmpService(context);
			initView();
			investorId =inverstorToCfmpService.isBindCfmp();
			MyApplication.app.addActivity(this);
			
		}
		/**
		 * InverstorToCfmpActivity data init
		 */
		private void initView(){
			listView1 = (ListView) findViewById(R.id.listView1);
			bound_cfmp=(RelativeLayout) findViewById(R.id.bound_cfmp);
			et_cfmp_moblie=(EditText) findViewById(R.id.cfmp_moblie);
			bt_bound=(Button) findViewById(R.id.bound);
			back =(LinearLayout) findViewById(R.id.back);
			back.setOnClickListener(new BackclickListener());
			bt_bound.setOnClickListener(new MyOnclickListener());
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case android.R.id.home:
				/*Intent intent = new Intent(context,UserCenterActivity.class);
				startActivity(intent);*/
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		}
		
		/** 
		 * InverstorToCfmpActivity backPressed
		 */
		@Override
		public void onBackPressed() {
			/*Intent intent = new Intent(context,UserCenterActivity.class);
			startActivity(intent);*/
			finish();
			super.onBackPressed();
		}
		
		/** 
		 * InverstorToCfmpActivity  listView search
		 */
		public class MyOnclickListener implements OnClickListener{
			@Override
			public void onClick(View arg0) {
				listView1.setVisibility(View.VISIBLE);
				phone = et_cfmp_moblie.getText().toString().trim();
				Matcher m = p.matcher(phone);
				if(et_cfmp_moblie.getText().toString().length()==0){
				Toast.makeText(InverstorToCfmpActivity.this, "请先输入正确的手机号", Toast.LENGTH_SHORT).show();
				}else if(phone.length() == 11 && m.matches()){
					inverstorToCfmpService.selectCfmp(et_cfmp_moblie.getText().toString().trim());
				}else{
//					inverstorToCfmpService.selectCfmp(et_cfmp_moblie.getText().toString().trim());
					Toast.makeText(InverstorToCfmpActivity.this, "手机号码不正确", Toast.LENGTH_SHORT).show();
				}
				
				
				
			}
			
		}
		
		/** 
		 * InverstorToCfmpActivity is finish
		 */
		private  class BackclickListener implements OnClickListener{
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*Intent intent = new Intent(context,UserCenterActivity.class);
				startActivity(intent);*/
				InverstorToCfmpActivity.this.finish();
			}
			
		}
}
