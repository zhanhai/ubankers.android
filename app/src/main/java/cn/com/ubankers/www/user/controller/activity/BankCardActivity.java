package cn.com.ubankers.www.user.controller.activity;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.service.BankCardService;
import cn.com.ubankers.www.user.view.BankCardAdapter;
import cn.com.ubankers.www.utils.PhotoUtil;
import cn.com.ubankers.www.widget.ClearEditTextView;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BankCardActivity extends Activity implements OnClickListener{
	private View yinhang_back;
	private EditText kahao;
	private Button yinhang_queding;
	private String yinghangkahao;
	private AsyncHttpClient client;
	private String userUrl;
	private UserBean userBean;
	private Context context;
	private Drawable drawable;
	public int MY_SCAN_REQUEST_CODE = 100;
	private String bankcard="0";
	private ListView bankcardlilstview;
	private BankCardService bankcardActivityService;
	private TextView bankcard_redact;
	private TextView failure_reason;
	private ImageView exclamation_mark;
	int BANKVARD_STAUS=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yinhangkarenzhengactivity);
		context=BankCardActivity.this;
		client = MyApplication.app.getClient(context);
		Intent intent=getIntent();
		userBean=(UserBean) intent.getSerializableExtra("userBean");
		System.out.println("银行卡555"+userBean.getBankcard_status());
		initView();
		addstr();
		bankcardActivityService=new BankCardService(context);
		MyApplication.getInstance().addActivity(this);	

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(context, SafetyCenterActivity.class);
			intent.putExtra("bankcard","0");
			setResult(RESULT_OK,intent);
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	public void initView() {
		yinhang_back = (View) findViewById(R.id.yinhang_back);
		kahao = (EditText) findViewById(R.id.yinhang_kahao);
		yinhang_queding = (Button) findViewById(R.id.yinhang_queding);
		bankcardlilstview=(ListView)findViewById(R.id.bankcardlilstview);
		bankcard_redact=(TextView)findViewById(R.id.bankcard_redact);
		failure_reason=(TextView)findViewById(R.id.failure_reason);
		exclamation_mark=(ImageView)findViewById(R.id.exclamation_mark);
		yinhang_queding.setOnClickListener(this);
		bankcard_redact.setOnClickListener(this);
		yinhang_back.setOnClickListener(this);	
		if(userBean!=null&&userBean.getBankcard_status()==null){
			BANKVARD_STAUS=1;
		}else if(userBean!=null&&userBean.getBankcard_status()!=null){
			BANKVARD_STAUS=2;
			BangDing();
			if(userBean.getBankcard_status().equals("2")){
				exclamation_mark.setVisibility(View.VISIBLE);
				bankcard_redact.setVisibility(View.VISIBLE);
				bankcardlilstview.setVisibility(View.GONE);
				failure_reason.setText("审核未通过原因:"+userBean.getBankcard_Failure_reason());
			}
		}
	}
	public void BangDing() {

		String kahan = userBean.getBankcard_no();
		int changdu = kahan.length();
		String jiequhaoma = kahan.substring(0, changdu - 10) + "*****"+ kahan.substring(changdu - 4);
		kahao.setText(jiequhaoma);
		kahao.setEnabled(false);
		kahao.setFocusableInTouchMode(false);
		yinhang_queding.setText("返回");

	}

	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.yinhang_back://返回跳转
			//数据是使用Intent返回
            Intent intent = new Intent();
            intent.putExtra("bankcard", "0");
            this.setResult(RESULT_OK, intent);
			BankCardActivity.this.finish();
			break;
		case R.id.bankcard_redact:
			BANKVARD_STAUS=3;
			kahao.setText("");
			bankcardlilstview.setVisibility(View.VISIBLE);
			failure_reason.setText("支持银行");
			yinhang_queding.setText("重新提交审核");
			exclamation_mark.setVisibility(View.GONE);
			kahao.setEnabled(true);
			kahao.setFocusableInTouchMode(true);
			break;
			
		case R.id.yinhang_queding:
			if(BANKVARD_STAUS==1||BANKVARD_STAUS==3){
				yinghangkahao = kahao.getText().toString();
				if (yinghangkahao.length()<=29&&yinghangkahao.length()>=15) {			
					bankcardActivityService.BankCardCheck(yinghangkahao);
				} else {
					Toast.makeText(BankCardActivity.this, "银行卡位数不正确",
							Toast.LENGTH_SHORT).show();
				}
			}else if (BANKVARD_STAUS==2){
				Intent intent1 = new Intent(context, SafetyCenterActivity.class);
				intent1.putExtra("bankcard","0");
				setResult(MY_SCAN_REQUEST_CODE,intent1);
				finish();
			}
			break;
		}
		// TODO Auto-generated method stub
		
	}	
	
	
	public void addstr(){
		
		String[] bArray = {"中国邮政储蓄银行","中国工商银行","中国农业银行","中国银行","中国建设银行","交通银行","中信银行",
				          "光大银行","华夏银行","中国民生银行","平安银行","招商银行","兴业银行","浦发银行","乐山商行","北京银行","南京银行",
				          "东莞银行","南昌银行","泉州银行","绵阳商行","农信山东","农信安徽","农信福建","农信江西","农信内蒙"};  
	
		BankCardAdapter bankCardadapter=new BankCardAdapter(bArray,this);
		bankcardlilstview.setAdapter(bankCardadapter);
	}
	
	
}
