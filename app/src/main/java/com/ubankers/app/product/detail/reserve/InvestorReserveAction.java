package com.ubankers.app.product.detail.reserve;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.product.model.ProductDetail;
import cn.com.ubankers.www.product.service.ProductDetailService;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.utils.MessageDialog;
import cn.com.ubankers.www.utils.MyTextWatcher;
import cn.com.ubankers.www.utils.Tools;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.ubankers.app.product.model.Product;

//投资者预约监听事件
	public class InvestorReserveAction implements OnClickListener{
		    private Context context;
		    private UserBean userBean;
			private String reserveName;
			private Product product;
			private String investorId;
			private AsyncHttpClient client;
			private TextView increment_amount;
			private EditText et_name,et_number,et_money;
			private AlertDialog dialog;

			public InvestorReserveAction(Context context, String reserveName, Product product){
		    	this.context = context;
		    	this.reserveName = reserveName;
		    	this.product = product;
		    	if(MyApplication.app.getUser()!=null){
		    		userBean = MyApplication.app.getUser();
		    	}
		    	client = MyApplication.app.getClient(context);
		    }

			@Override
			public void onClick(View arg0) {
				dialog=new AlertDialog.Builder(context).create();
				dialog.setTitle("预约产品");
				final View view = View.inflate(context, R.layout.order_dialog, null);
				TextView investment_amount =(TextView) view.findViewById(R.id.investment_amount);			
				investment_amount.setText((Tools.InterceptTo(product.getMinMoneyYuan())));				
				increment_amount =(TextView) view.findViewById(R.id.increment_amount);
				increment_amount.setText(product.getIncrementalMoney()+"0000");
				et_name = (EditText) view.findViewById(R.id.name);
				et_number = (EditText) view.findViewById(R.id.number);
				et_money = (EditText) view.findViewById(R.id.money);
				final TextView moTextView = (TextView) view.findViewById(R.id.money_range);
				final TextView inTextView = (TextView) view.findViewById(R.id.increase_money);
				MyTextWatcher textWatcher = new MyTextWatcher(20, et_money, moTextView, inTextView, product, context, increment_amount);
				et_money.addTextChangedListener(textWatcher);
				if (userBean != null && userBean.getIdcard_status() != null) {
					int idcard_statu = Integer.parseInt(userBean.getIdcard_status());
					if (idcard_statu == 0 || idcard_statu == 1) {
						et_name.setText(userBean.getIdcard_name());
						if(et_name.getText().toString().trim().equals(userBean.getIdcard_name())){
							et_name.clearFocus();
							et_name.setFocusable(false);
						}
					}
				}else{
					et_name.setText(reserveName);
					if(et_name.getText().toString().trim().equals(reserveName)){
						et_name.clearFocus();
						et_name.setFocusable(false);
					}
				}
				if(!et_name.getText().toString().trim().equals("")){
					et_name.setText(et_name.getText().toString().trim());
					et_name.clearFocus();
					et_name.setFocusable(false);
				}
				et_number.setText(userBean.getUserMobile());
				//投资者是否绑定财富师的接口
				client.get(HttpConfig.URL_INVESTOR_BOUND, new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							boolean flag = response.getBoolean("success");
							if(flag){
								JSONObject jsonObject = response.getJSONObject("result");
								String errorCode = jsonObject.getString("errorCode");
								if(errorCode.equals("success")){
									JSONArray jsonArray = jsonObject.getJSONArray("list");
									for(int i = 0;i<jsonArray.length();i++){
										investorId = jsonArray.getString(i);
//										Log.e("investorId", "investorId="+investorId);
									}
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(statusCode, headers, response);
					}
					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, responseString, throwable);
					}
					
					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable, errorResponse);
						Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
						
					}
				});
				view.findViewById(R.id.okay).setOnClickListener(new MyOnclickListener());
				dialog.setView(view); 
				dialog.show();
				
			}
			public class MyOnclickListener implements OnClickListener{
				final MessageDialog myDialog = new MessageDialog(context);

				@Override
				public void onClick(View arg0) {
					String phone_number =et_number.getText().toString().trim();
					String money_amount =et_money.getText().toString().trim();
					String order_name =et_name.getText().toString().trim();
					if(order_name.equals("")){
						 Toast.makeText(context,"姓名不能为空",Toast.LENGTH_SHORT).show();
					}else if(phone_number.equals("")){
						 Toast.makeText(context,"手机号不能为空",Toast.LENGTH_SHORT).show();
					}else if(Tools.isMobileNO(phone_number)==false){
						 Toast.makeText(context, "手机号不正确", Toast.LENGTH_SHORT).show();
					}else if(money_amount.equals("")){
						 Toast.makeText(context,"预约金额不能为空",Toast.LENGTH_SHORT).show();
					}else if(investorId==null){
						dialog.dismiss();
						myDialog.shoeBoundDialog();
					}else if(Tools.rangeLongDefined(Long.parseLong(money_amount),Long.parseLong(product.getMinMoney()+"0000"),Long.parseLong(product.getMaxMoney()+"0000"))==false|| !(Long.parseLong(money_amount)%Long.parseLong(increment_amount.getText().toString().trim())==0)){
						 Toast.makeText(context, "请输入在限制范围内的金额",Toast.LENGTH_SHORT).show();
					}else{
						ProductDetailService pds = new ProductDetailService(product.getProductId(),context);
						pds.InvestorOrder(money_amount, phone_number, order_name, et_name, dialog);
					}
				}
				
			}
		}
