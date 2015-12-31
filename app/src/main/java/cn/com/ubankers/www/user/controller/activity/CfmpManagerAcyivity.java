package cn.com.ubankers.www.user.controller.activity;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CfmpManagerAcyivity extends Activity {
	
	private ListView caifushi_listview;
	private View caifushi_back;
	private TextView caifushi_shengpijingdu;
	private TextView caifushi_mujijindu;


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
		caifushi_back=findViewById(R.id.caifushi_back);
		caifushi_shengpijingdu=(TextView) findViewById(R.id.caifushi_shengpijingdu);
		caifushi_mujijindu=(TextView) findViewById(R.id.caifushi_mujijindu);
		caifushi_shengpijingdu.setText("存续期产品");
		caifushi_mujijindu.setText("已兑付产品");;
		caifushi_back.setOnClickListener(new back());
		
	}
	
	class  back implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			CfmpManagerAcyivity.this.finish();
		}
		
	}
}
