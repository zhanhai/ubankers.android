package cn.com.ubankers.www.product.listener;






import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import com.ubankers.app.product.detail.ProductDetailActivity;
import cn.com.ubankers.www.product.controller.activity.RegisterIntervorActivity;
import cn.com.ubankers.www.product.model.ProductDetail;
import cn.com.ubankers.www.product.service.ProductDetailService;
import cn.com.ubankers.www.utils.LoginDialog;
import cn.com.ubankers.www.utils.MyTextWatcher;
import cn.com.ubankers.www.utils.Tools;
import cn.com.ubankers.www.widget.MyDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

//财富师给投资者预约的监听事件
public class MyCfmpOrderListener implements OnClickListener{
	
	private ProductDetail product;
	private Context context;
	private TextView increment_amount;
	public static EditText etNumber,unregistered_name,registered_name;
	private String id;
	private String reserveName;
	private AsyncHttpClient client;
	private MyDialog myDialog;
	public MyCfmpOrderListener(Context context,ProductDetail product,String id,String reserveName){
		this.context = context;
		this.product = product;
		this.id = id;
		this.reserveName = reserveName;
		client = MyApplication.app.getClient(context);
		if(myDialog==null){
			myDialog = myDialog.createDialog(context,"正在加载中...");
		}
		
	}
	@Override
	public void onClick(View arg0) {
		myDialog.show();
		client.post(HttpConfig.URL_QUALIFIED_WEALTH_DIVISION, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					boolean flag = response.getBoolean("success");
					if(flag){
						JSONObject jsonObject = response.getJSONObject("result");
						JSONObject jsonObject2 = jsonObject.getJSONObject("info");
						int fied =jsonObject2.getInt("qualified");
						if(fied==1){
							final AlertDialog dialog=new AlertDialog.Builder(context).create();
							dialog.setTitle("预约产品");
							final View view = View.inflate(context, R.layout.cfmp_order_dialog, null);
							TextView investment_amount =(TextView) view.findViewById(R.id.cfmp_investment_amount);
							investment_amount.setText((Tools.InterceptTo(product.getMinMoneyYuan())));
							increment_amount =(TextView) view.findViewById(R.id.cfmp_increment_amount);
							increment_amount.setText(product.getIncrementalMoney()+"0000");
							etNumber = (EditText) view.findViewById(R.id.cfmp_number);
							//未注册的编辑框
							unregistered_name= (EditText) view.findViewById(R.id.unregistered_name);
							registered_name= (EditText) view.findViewById(R.id.registered_name);
							final EditText etMoney = (EditText) view.findViewById(R.id.cfmp_money);
							final TextView tv_money_range=(TextView) view.findViewById(R.id.cfmp_money_range);
							final TextView tv_increase_money=(TextView) view.findViewById(R.id.cfmp_increase_money);
							RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
							final RadioButton radio_unregistered_intervor=(RadioButton) view.findViewById(R.id.radio_unregistered_intervor);
							final RadioButton radio_registered_intervor=(RadioButton) view.findViewById(R.id.radio_registered_intervor);
							final TextView cfmp_okay = (TextView) view.findViewById(R.id.cfmp_okay);
							MyTextWatcher myTextWatcher = new MyTextWatcher(20, etMoney, tv_money_range, tv_increase_money, product, context, increment_amount);
							etMoney.addTextChangedListener(myTextWatcher);
							radio_unregistered_intervor.setChecked(true);
							if(radio_unregistered_intervor.isChecked()){//未注册投资者
								unregistered_name.setVisibility(View.VISIBLE);
								registered_name.setVisibility(View.GONE);
							/*	unregistered_name.setOnClickListener(new MyOnClickListenerNickName(2));*/
								etNumber.setText("");
								unregistered_name.setText("");
								etMoney.setText("");
								cfmp_okay.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View arg0) {
										getCfmpByIntervors(etNumber, etMoney, unregistered_name, dialog, 2);
									}
							    });
							}else if(radio_registered_intervor.isChecked()){//已注册投资者
								etNumber.setText("");
								registered_name.setVisibility(View.VISIBLE);
								unregistered_name.setVisibility(View.GONE);
								registered_name.setText("");
								etMoney.setText("");
								registered_name.setOnClickListener(new MyOnClickListenerNickName(1));
								cfmp_okay.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View arg0) {
										getCfmpByIntervors(etNumber, etMoney, registered_name, dialog, 1);
									}
							    });
								dialog.dismiss();
							}
							  radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
								
								@Override
								public void onCheckedChanged(RadioGroup group, int checkedId) {
									if(radio_unregistered_intervor.getId()==checkedId){//未注册投资者
										unregistered_name.setVisibility(View.VISIBLE);
										registered_name.setVisibility(View.GONE);
										etNumber.setText("");
										unregistered_name.setText("");
										etMoney.setText("");
										cfmp_okay.setOnClickListener(new OnClickListener() {
											
											@Override
											public void onClick(View arg0) {
												getCfmpByIntervors(etNumber, etMoney, unregistered_name, dialog, 2);
											}
									    });
									}else if(radio_registered_intervor.getId()==checkedId){//已注册投资者
										registered_name.setVisibility(View.VISIBLE);
										unregistered_name.setVisibility(View.GONE);
										etNumber.setText("");
										registered_name.setText("");
										etMoney.setText("");
										registered_name.setOnClickListener(new MyOnClickListenerNickName(1));
										cfmp_okay.setOnClickListener(new OnClickListener() {	
											@Override
											public void onClick(View arg0) {
												getCfmpByIntervors(etNumber, etMoney, registered_name, dialog, 1);
											}
									    });
									}
								}
							});
							dialog.setView(view); 
							dialog.show();
						}else{
							cn.com.ubankers.www.utils.MyDialog dialog = new cn.com.ubankers.www.utils.MyDialog(context);
							dialog.businessCardDialog();
						}
					}
					myDialog.dismiss();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable,
						errorResponse);
				if(statusCode==401){
					try {
						boolean flag = errorResponse.getBoolean("success");
						if(flag==false){
							JSONObject jsonObject = errorResponse.getJSONObject("result");
							String errorCode = jsonObject.getString("errorCode");
							if(errorCode.equals("noLogin")){
								Toast.makeText(context, "用户未登录", Toast.LENGTH_SHORT).show();
								MyApplication.app.setUser(null);
								MyApplication.app.setClient(null);
								LoginDialog loginDialog = new LoginDialog(context,0,0);
								loginDialog.onLogin();
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
					myDialog.dismiss();
				}
			}
			
		});
	}
	//财富师给投资者预约的接口
	private void getCfmpByIntervors(EditText etNumber,EditText etMoney,EditText etName,final AlertDialog dialog,int isBInvestor){
		String number = etNumber.getText().toString().trim();
		String money = etMoney.getText().toString().trim();
		String name = etName.getText().toString().trim();
		if(name.equals("")){
			 Toast.makeText(context,"昵称不能为空",Toast.LENGTH_SHORT).show();
		}else if(number.equals("")){
			 Toast.makeText(context,"手机号不能为空",Toast.LENGTH_SHORT).show();
		}else if(Tools.isMobileNO(number)==false){
			 Toast.makeText(context, "手机号不正确", Toast.LENGTH_SHORT).show();
		}else if(money.equals("")){
			 Toast.makeText(context,"预约金额不能为空",Toast.LENGTH_SHORT).show();
		}else if(Tools.rangeLongDefined(Long.parseLong(money),Long.parseLong(product.getMinMoney()+"0000"),Long.parseLong(product.getMaxMoney()+"0000"))==false|| !(Long.parseLong(money)%Long.parseLong(increment_amount.getText().toString().trim())==0)){
//		}else if(Tools.rangeInDefined(Integer.parseInt(money),Integer.parseInt(product.getMinMoney()+"0000"),Integer.parseInt(product.getMaxMoney()+"0000"))==false|| !(Integer.parseInt(money)%Integer.parseInt(increment_amount.getText().toString().trim())==0)){
			
			 Toast.makeText(context, "请输入在限制范围内的金额",Toast.LENGTH_SHORT).show();
		}else{
			ProductDetailService pds = new ProductDetailService(product.getProductId(),context);
			pds.cfmpOrder(isBInvestor, money, number, name, dialog);	
	    }
	}
	//点击昵称的监听
	private class MyOnClickListenerNickName implements OnClickListener{
		private int type;
		public MyOnClickListenerNickName(int type){
			this.type = type;
		}
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(context, RegisterIntervorActivity.class);
			intent.putExtra(ProductDetailActivity.KEY_CLIENT_TYPE, type);
			((ProductDetailActivity) context).startActivityForResult(intent, ProductDetailActivity.request);
		}
		
	}
	
}
