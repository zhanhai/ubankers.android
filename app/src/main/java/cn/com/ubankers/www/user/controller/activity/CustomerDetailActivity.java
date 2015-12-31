package cn.com.ubankers.www.user.controller.activity;

import cn.com.ubankers.www.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CustomerDetailActivity extends Activity {
	
	private ListView caifushi_listview;
	private LinearLayout caifushi_back;
	private TextView caifushi_mujijindu;
	private TextView caifushi_shengpijingdu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.caifushi_record);
		initview ();
	}
	public void initview (){
		caifushi_listview=(ListView) findViewById(R.id.caifushi_listview);
		caifushi_back=(LinearLayout) findViewById(R.id.caifushi_back);
		caifushi_shengpijingdu=(TextView) findViewById(R.id.caifushi_shengpijingdu);
		caifushi_mujijindu=(TextView) findViewById(R.id.caifushi_mujijindu);
		caifushi_shengpijingdu.setText("即将到期");
		caifushi_mujijindu.setText("历史详情");
		caifushi_back.setOnClickListener(new back());		
	}
	
	class  back implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			CustomerDetailActivity.this.finish();
		}
		
	}
}
