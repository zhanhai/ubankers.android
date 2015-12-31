package cn.com.ubankers.www.user.view;

import java.util.List;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.user.model.AccountBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BankCardAdapter extends BaseAdapter {

	private String[] arrstr;
	private int count;
	private Context context;
	private TextView bankcaradper;
	

	public BankCardAdapter(String[] arrstr, Context context) {
		// TODO Auto-generated constructor stub
		this.arrstr=arrstr;
		this.context=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(arrstr!=null){
			count = arrstr.length;	
		}
		return count;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arrstr[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub	
		String str=arrstr[arg0];
		arg1 = LayoutInflater.from(context).inflate(R.layout.bank_card_adapter, null);
		bankcaradper=(TextView)arg1.findViewById(R.id.bankcaradper);
		bankcaradper.setText(str);
		return arg1;
	}

}
