package cn.com.ubankers.www.user.controller.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.loopj.android.http.AsyncHttpClient;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.user.model.MembersBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.model.WealthBean;
import cn.com.ubankers.www.widget.CircleImg;
import cn.com.ubankers.www.widget.MyDialog;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MemberSpaceActivity extends Activity implements OnClickListener{
	private Context context;
	private AsyncHttpClient client;
	private UserBean userBean;
	private MyDialog myDialog;
	public  CircleImg memberImageView;
	public TextView memberNameView,memberMobileView,memberDateView,tv4;
	public LinearLayout back;
	public Button deleteMember;
	private ListView workRecordlistView;
	private WealthBean wealthBean;
	private MembersBean memberBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.workspace_member_activity);
		context = this;
		client = MyApplication.app.getClient(context);
		if(MyApplication.app.getUser()!=null){
			userBean = MyApplication.app.getUser();
		}
		if(myDialog==null){
		   myDialog=MyDialog.createDialog(MemberSpaceActivity.this,"正在加载中...");
		}	
		Intent intent =this.getIntent();
		if(intent!=null){
			memberBean = (MembersBean)intent.getSerializableExtra("memberBean");
		}
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		tv4 = (TextView)findViewById(R.id.tv4); //个人业绩
		memberImageView = (CircleImg)findViewById(R.id.memberImageView); //头像
		memberNameView = (TextView)findViewById(R.id.memberNameView); //姓名
		memberMobileView = (TextView)findViewById(R.id.memberMobileView); //手机号码
		memberDateView = (TextView)findViewById(R.id.memberDateView); //加入时间
		deleteMember = (Button)findViewById(R.id.deleteMember);
		deleteMember.setVisibility(View.GONE);
		workRecordlistView = (ListView)findViewById(R.id.workRecordlistView);//工作记录listView
		back = (LinearLayout)findViewById(R.id.back);
		back.setOnClickListener(this);
		memberNameView.setText(memberBean.getNickName());
		memberMobileView.setText(memberBean.getMobile());
		memberDateView.setText(getStringDate(memberBean.getJoinDate())+"");	
	}
	
	public static String getStringDate(Long time ){
		 String strs = "";
		 try {
			 if( time != null ){
				 Date date = new Date(time);
				 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				 strs = sdf.format(date);
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 return strs;
	 }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.back:
			MemberSpaceActivity.this.finish();
			overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			break;		
	}
	}

}
