package cn.com.ubankers.www.user.controller.activity;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CfmpStateAcyivity extends Activity {
	
	private ListView caifushi_listview;
	private LinearLayout caifushi_back;
	private TextView caifushi_mujijindu;
	private TextView caifushi_shengpijingdu;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.caifushi_record);
		super.onCreate(savedInstanceState);
		initview();
		MyApplication.getInstance().addActivity(this);
		
	}

	
	public void initview (){
		caifushi_listview=(ListView) findViewById(R.id.caifushi_listview);
		caifushi_back=(LinearLayout) findViewById(R.id.caifushi_back);
		caifushi_mujijindu=(TextView) findViewById(R.id.caifushi_mujijindu);
		caifushi_shengpijingdu=(TextView) findViewById(R.id.caifushi_shengpijingdu);
		caifushi_shengpijingdu.setText("审批进度");
		caifushi_mujijindu.setText("募集进度");;
		caifushi_back.setOnClickListener(new back());
		
	}
	
	class  back implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			CfmpStateAcyivity.this.finish();
		}
		
	}
}
