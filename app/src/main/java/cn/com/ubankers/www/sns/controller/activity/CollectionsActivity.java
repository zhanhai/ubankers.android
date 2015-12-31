package cn.com.ubankers.www.sns.controller.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.authentication.controller.activity.LoginActivity;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.http.ParseUtils;
import cn.com.ubankers.www.sns.model.ArticleBean;
import cn.com.ubankers.www.sns.model.ColArticleBean;
import cn.com.ubankers.www.sns.view.FavorCommentAdapter;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.utils.NetReceiver;
import cn.com.ubankers.www.utils.NetReceiver.NetState;
import cn.com.ubankers.www.widget.MyDialog;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CollectionsActivity extends Activity {
	private AsyncHttpClient client;
	private UserBean userBean;
	private Context context;
	private List<ColArticleBean> articleList;
	private ListView listView;
	private StringEntity entity ;
	private ArticleBean favorBean;
	private EditText editText;
	private ImageView backImage;
	private LinearLayout articlebottom;
	private MyDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rongan_suocang);
		context =this;
		if (progressDialog == null) {
			progressDialog = MyDialog.createDialog(this,"正在加载中...");
		}
		listView = (ListView) findViewById(R.id.shoucang_listView);
		editText=(EditText)findViewById(R.id.comment); 
		backImage =(ImageView) findViewById(R.id.rong_head);
		articleList =new ArrayList<ColArticleBean>();
		backImage.setOnClickListener(new TitleOnclickListener());
		listView.setOnItemClickListener(new MyOnItemClickListener());
		client = MyApplication.app.getClient(context);
		NetState connected = NetReceiver.isConnected(this);
		if(connected==connected.NET_NO){
				Toast.makeText(this, "当前网络不可用",Toast.LENGTH_SHORT).show();
		}else{
			initData();
		}
		MyApplication.getInstance().addActivity(this);
	}
	public class TitleOnclickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			CollectionsActivity.this.finish();
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
		}
	}
	public class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (articleList != null) {
				ColArticleBean colArticleBean = articleList.get(arg2);
				Intent intent = new Intent(context, SnsArticleActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("articleBean", colArticleBean);
				bundle.putString("type", "collection");
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}
	}
	//对于session超时处理
		public  void  TimeOutMethod(){
			Toast.makeText(CollectionsActivity.this, "用户登录超时,请重新登录",Toast.LENGTH_SHORT).show();
			MyApplication.app.setUser(null);
			MyApplication.app.setClient(null);
			Intent intent = new Intent(CollectionsActivity.this, LoginActivity.class);
			startActivity(intent);
			CollectionsActivity.this.finish();
			overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
		}
private void initData(){
	 String params = null;
	try {
		if(MyApplication.app.getUser()!=null){
	    userBean =MyApplication.app.getUser();
		String userId = userBean.getUserId();
		params =userId+"/favor";
		progressDialog.show(); 
		}else{
			return;}	
	} catch (Exception e) {
		e.printStackTrace();
	} 
	  client.get(CollectionsActivity.this, HttpConfig.URL_COLLECTIONS+params, entity, "application/json",
			new JsonHttpResponseHandler() {
				public void onSuccess(int statusCode,
						org.apache.http.Header[] headers,
						JSONObject response) { 
					try {
						boolean flag = response.getBoolean("success");
						if (flag) {
						JSONArray array =response.getJSONArray("result");						
						for(int i=0;i<array.length();i++){
							JSONObject obj =array.getJSONObject(i);
							articleList.add(ParseUtils.parseColArticle(obj.toString()));
						  }	
						 progressDialog.dismiss();
						FavorCommentAdapter adapter = new FavorCommentAdapter(CollectionsActivity.this,articleList);
						listView.setAdapter(adapter);
						} else {
								TimeOutMethod();
								progressDialog.dismiss();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						progressDialog.dismiss();
					}

				}
				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable,
							errorResponse);
					Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
					progressDialog.dismiss();
				}
			});
    }
}