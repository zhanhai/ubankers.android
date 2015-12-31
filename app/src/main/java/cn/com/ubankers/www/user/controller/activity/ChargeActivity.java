package cn.com.ubankers.www.user.controller.activity;
import cn.com.ubankers.www.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ChargeActivity extends Activity implements OnClickListener{
	private TextView confirm;
	private View charge_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.charge_activity);
		intiew();
	}
	
	protected void	intiew(){
//		confirm=(TextView)findViewById(R.id.confirm);
		charge_back=(View)findViewById(R.id.charge_back);
		charge_back.setOnClickListener(this);
//		charge_back.setOnClickListener(this);
	}
	

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.charge_back:
			ChargeActivity.this.finish();
			break;
		}
		
	}

}
