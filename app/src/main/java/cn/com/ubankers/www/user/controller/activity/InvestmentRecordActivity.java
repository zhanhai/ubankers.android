package cn.com.ubankers.www.user.controller.activity;




import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class InvestmentRecordActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personage_information);
		initView();
		MyApplication.getInstance().addActivity(this);
	}
	//初始化控件
	private void initView() {
		ImageView back =(ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}
