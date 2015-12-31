package cn.com.ubankers.www.user.controller.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import com.ubankers.app.product.detail.ProductDetailActivity;
import cn.com.ubankers.www.user.model.RecommendPtsBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.service.CfmpRecommendPtsService;

public class CfmpRecommendPtsActivity extends Activity {
	    private UserBean userBean;
	    private ListView lv;
	    private Context context;
		private List<RecommendPtsBean> list = new ArrayList<RecommendPtsBean>();
		private LinearLayout title_bar_back_btn;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.cfmp_list);
			lv =(ListView) findViewById(R.id.lv);
			if(MyApplication.app.getUser()!=null){
				userBean=MyApplication.app.getUser();
			}
			MyApplication.app.addActivity(this);
			title_bar_back_btn=(LinearLayout) findViewById(R.id.title_bar_back_btn);
			title_bar_back_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
			context = this;
			CfmpRecommendPtsService crs = new CfmpRecommendPtsService(context, userBean.getUserId(), lv, list);
			crs.initData();
			lv.setOnItemClickListener(new MyOnItemClickListener());
		}
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		}
		@Override
		public void onBackPressed() {
			finish();
			super.onBackPressed();
		}
		public class MyOnItemClickListener implements OnItemClickListener{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				RecommendPtsBean  cfmpProductBean = list.get(arg2);
			    Intent intent = new Intent(CfmpRecommendPtsActivity.this,ProductDetailActivity.class);
			    intent.putExtra(ProductDetailActivity.KEY_PRODUCT_ID, cfmpProductBean.getProductId());
			    startActivity(intent);
			}
			
		}
}
