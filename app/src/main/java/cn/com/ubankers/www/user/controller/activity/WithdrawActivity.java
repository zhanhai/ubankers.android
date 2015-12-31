package cn.com.ubankers.www.user.controller.activity;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.service.WithdrawSevice;
import cn.com.ubankers.www.utils.Util;
import cn.com.ubankers.www.widget.ClearEditTextView;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class WithdrawActivity extends Activity implements OnClickListener{

	private View withdraw_back;
	public TextView withdraw_balance;
	private String totalAmount;
	private WithdrawSevice WithdrawSevice;
	public ClearEditTextView Withdrawal_amount;
	private TextView confirm;
	public TextView cost;
	private String amounta;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		registerBoradcastReceiver();
		setContentView(R.layout.withdraw_activity);
		intiew();
		WithdrawSevice=new WithdrawSevice(this);
	}
	
	protected void  intiew(){
		withdraw_back=(View)findViewById(R.id.withdraw_back);
		withdraw_balance=(TextView)findViewById(R.id.withdraw_balance);
		Withdrawal_amount=(ClearEditTextView)findViewById(R.id.Withdrawal_amount);
		confirm=(TextView)findViewById(R.id.confirm);
		cost=(TextView)findViewById(R.id.cost);
		confirm.setOnClickListener(this);
		withdraw_back.setOnClickListener(this);
	}
	
	
	

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.withdraw_back:
			WithdrawActivity.this.finish();
			break;
			
		case R.id.confirm:
		  String amount =Withdrawal_amount.getText().toString().trim(); 
		  if(amount!=null){  
			if(Util.isD(amount)){
				if(Util.Intercept2(amounta)>=Util.Intercept2(amount)&&Util.Intercept2(amount)!=0){
					WithdrawSevice.WithdrawalfeeData(Util.Intercept2(amount)+"");
				  }else if (Util.Intercept2(amount)==0){
					  Toast.makeText(this, "金额不能为0", 0).show();	
				  }else{
					  Toast.makeText(this, "账户余额不足", 0).show(); 
				  }
				
			}else{
				Withdrawal_amount.setText("");
				Toast.makeText(this, "请输入正确的金额,小数保留两位", 0).show();	
			}
			
		  }
			break;
		}
		
	}
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
		@Override  
        public void onReceive(Context context, Intent intent) {  
            String action = intent.getAction();  
            if(action.equals("ACTION_NAME")){  
            	amounta=intent.getStringExtra("amount");
            }  
            abortBroadcast();
        }  
          
    }; 
    
    public void registerBoradcastReceiver(){ 
    	  //注册广播   
        IntentFilter myIntentFilter = new IntentFilter();  
        myIntentFilter.addAction("ACTION_NAME");       
        registerReceiver(mBroadcastReceiver, myIntentFilter);  
    }

}
