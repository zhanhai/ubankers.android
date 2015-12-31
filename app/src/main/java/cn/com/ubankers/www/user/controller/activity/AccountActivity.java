package cn.com.ubankers.www.user.controller.activity;

import java.util.ArrayList;
import java.util.List;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.model.DictBean;
import cn.com.ubankers.www.user.service.AccountServices;
import cn.com.ubankers.www.utils.XListView;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebIconDatabase.IconListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class AccountActivity extends Activity implements OnClickListener,cn.com.ubankers.www.utils.XListView.IXListViewListener{
	private View wdzh_yonhuzhuce;
	private Spinner wdzh_spinner;
	public  XListView account_listView1;
	private Handler mHandler;
    public int i=1;
	private AccountServices AccountServices;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myaccount_activity);
		initview();	
		AccountServices	=new AccountServices(this,mHandler);
		AccountServices.initData(i);
	}

	
	public void initview(){	
		wdzh_yonhuzhuce=(View)findViewById(R.id.wdzh_yonhuzhuce);
		wdzh_yonhuzhuce.setOnClickListener(this);
		account_listView1 =(XListView)findViewById(R.id.account_listView1);
		account_listView1.setPullLoadEnable(true);
		account_listView1.setXListViewListener(this);
		mHandler = new Handler();
		
	}
	

	private void onLoad() {
		account_listView1.stopRefresh();
		account_listView1.stopLoadMore();  
		account_listView1.setRefreshTime("");  
	}
	
	
	public static void main(String name) {
		String jiage2="\\d+(\\.\\d+)?(人民币|RMB|￥|美元|\\$)";
		}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.wdzh_yonhuzhuce:
			AccountActivity.this.finish();
			break;
		}
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				AccountServices.initDatasx(1);
				onLoad();
			}
		}, 100);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				i=i+1;
				AccountServices.initData(i);
				onLoad();
			}
		}, 100);
	}
}
