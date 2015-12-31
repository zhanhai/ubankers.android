package cn.com.ubankers.www.product.controller.activity;

import java.text.DecimalFormat;

import org.apache.http.Header;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.product.model.ProductDetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FamilyFortuneActivity extends Activity {
	private TextView dsf_DetailsClick, dshand_productNamehad, dsf_FactorsClick, dshand_productTerm, dshand_productRate;
	//private ProgressBar dshand_raisedProcessShow;
	private View family_GoBack;
	private View fudong;
	private ProductDetail product;
	private View FactorsClick_family_yaosu;
	private View DetailsClick_family_xinxi;
	private ImageView imageHot;
	private ImageView imageState;
	private RelativeLayout family_ty;
	private AsyncHttpClient client;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.floathead_family__details);
		Intent intent = getIntent();
		context = this;
		client = MyApplication.app.getClient(context);
		if(intent!=null){
			product = (ProductDetail) intent.getSerializableExtra("product");
		}
		
		initView();
		MyApplication.getInstance().addActivity(this);
	}

	public void initView() {
		family_ty = (RelativeLayout) findViewById(R.id.family_ty);
		dsf_DetailsClick = (TextView) findViewById(R.id.family_jingbenyaosu);
		dsf_FactorsClick = (TextView) findViewById(R.id.family_chanpinxiangqing);
	    imageState=(ImageView)findViewById(R.id.family_imageState);
	    imageHot=(ImageView)findViewById(R.id.family_imageHot);
	    family_ty.setVisibility(View.VISIBLE);
		dshand_productNamehad = (TextView) findViewById(R.id.family_productName);
		dshand_productTerm = (TextView) findViewById(R.id.family_productTerm);
		dshand_productRate = (TextView) findViewById(R.id.family_productRate);
		family_GoBack = (View) findViewById(R.id.family_GoBack);
		FactorsClick_family_yaosu=(View)findViewById(R.id.FactorsClick_family_yaosu);
		DetailsClick_family_xinxi=(View)findViewById(R.id.DetailsClick_family_xinxi);
		family_GoBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FamilyFortuneActivity.this.finish();
			}
		});
		family_GoBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FamilyFortuneActivity.this.finish();
			}
		});

		//fudong = findViewById(R.id.fudong);
		 IngredientOnClick();
	}

	public void IngredientOnClick() {
		dsf_DetailsClick.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dsf_FactorsClick.setTextColor(android.graphics.Color.BLACK);
				dsf_DetailsClick.setTextColor(android.graphics.Color.RED);
				DetailsClick_family_xinxi.setVisibility(View.GONE);
				FactorsClick_family_yaosu.setVisibility(View.VISIBLE);
//				chanpingLayout.setVisibility(View.GONE);
				family_ty.setVisibility(View.VISIBLE);
				
			}
		});

		dsf_FactorsClick.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dsf_FactorsClick.setTextColor(android.graphics.Color.RED);
				dsf_DetailsClick.setTextColor(android.graphics.Color.BLACK);
				DetailsClick_family_xinxi.setVisibility(View.VISIBLE);
				FactorsClick_family_yaosu.setVisibility(View.GONE);	
				family_ty.setVisibility(View.GONE);
			}
		});

	}

	public void initData(final ProductDetail product) {
		if(product!=null&&!product.getProductId().equals(null)){
			
			client.get(HttpConfig.URL_PRODUCT_PARTICULARS + product.getProductId(),
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							// TODO Auto-generated method stub
							super.onFailure(statusCode, headers, throwable,
									errorResponse);
							
						}

					});
		}
	}
	
	public String Intercept( String str){
		double n= (Double.valueOf(str).doubleValue()/10000);
		DecimalFormat df = new DecimalFormat("#,###.####");
		String m = df.format(n);
		return m;
	}
}
