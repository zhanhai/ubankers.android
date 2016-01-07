package cn.com.ubankers.www.utils;

import cn.com.ubankers.www.product.model.ProductDetail;
import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ubankers.app.product.model.Product;

public class MyTextWatcher implements TextWatcher{
	private int maxLen=0;
	private EditText  tv_money_aomount=null;
	private TextView tv_money_range;
	private TextView tv_increase_money;
	private Product product;
	private Context context;
	private TextView increment_amount;

	public MyTextWatcher(int maxLen,EditText  tv_money_aomount,TextView tv_money_range,TextView tv_increase_money,Product product,Context context,TextView increment_amount){
		this.maxLen=maxLen;
		this.tv_money_aomount=tv_money_aomount;
		this.tv_money_range = tv_money_range;
		this.tv_increase_money = tv_increase_money;
		this.product = product;
		this.context = context;
		this.increment_amount = increment_amount;
	}
	@Override
	public void afterTextChanged(Editable arg0) {
		try{
			    String money_amount = tv_money_aomount.getText().toString().trim(); 
				if(Tools.rangeInDefined(Integer.parseInt(money_amount),Integer.parseInt(product.getMinMoney()+"0000"),Integer.parseInt(product.getMaxMoney()+"0000"))==false
						  || !(Integer.parseInt(money_amount)%Integer.parseInt(increment_amount.getText().toString().trim())==0)){
					tv_money_range.setVisibility(View.VISIBLE);
					tv_money_range.setText("请输入预定金额"+product.getMinMoney()+"0000"+"-"+product.getMaxMoney()+"0000"+"元！");
					tv_increase_money.setVisibility(View.VISIBLE);
					tv_increase_money.setText("请以"+product.getIncrementalMoney()+"万元 为递增金额！");
				}else if(!(Integer.parseInt(money_amount)%Integer.parseInt(increment_amount.getText().toString().trim())==0)){ 
					tv_money_range.setVisibility(View.GONE);
					tv_increase_money.setVisibility(View.VISIBLE);
					tv_increase_money.setText("请以"+product.getIncrementalMoney()+"万元 为递增金额！");
				}else if(Tools.rangeInDefined(Integer.parseInt(money_amount),Integer.parseInt(product.getMinMoney()+"0000"),Integer.parseInt(product.getMaxMoney()+"0000"))==true
						  || (Integer.parseInt(money_amount)%Integer.parseInt(increment_amount.getText().toString().trim())==0)){
					tv_money_range.setVisibility(View.GONE);
					tv_increase_money.setVisibility(View.GONE);
				}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		Editable editable = tv_money_aomount.getText();
		int len = editable.length();
		
		if(len > maxLen)
		{
			int selEndIndex = Selection.getSelectionEnd(editable);
			String str = editable.toString();
			//截取新字符串
			String newStr = str.substring(0,maxLen);
			tv_money_aomount.setText(newStr);
			editable = tv_money_aomount.getText();
			
			//新字符串的长度
			int newLen = editable.length();
			//旧光标位置超过字符串长度
			if(selEndIndex > newLen)
			{
				selEndIndex = editable.length();
			}
			//设置新光标所在的位置
			Selection.setSelection(editable, selEndIndex);
			Toast.makeText(context,"这已经是最大长度",Toast.LENGTH_SHORT).show();
		}
		
	}
	
}
