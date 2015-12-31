package cn.com.ubankers.www.product.view;

import java.util.List;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.product.model.ProductDetail;
import cn.com.ubankers.www.utils.Tools;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProductListAdapter extends BaseAdapter {
	private List<ProductDetail> newsListmian;
	private Activity activity;
	private int count;

	public ProductListAdapter(Activity activity, List<ProductDetail> newsListmian) {
		this.activity = activity;
		this.newsListmian = newsListmian;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (newsListmian != null) {
			count = newsListmian.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		if (newsListmian != null && newsListmian.size() != 0) {
			return newsListmian.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ProductDetail ProductConstants = newsListmian.get(position);
		ViewHolder mHolder = null;
		// View view = convertView;
		
		if (ProductConstants.getModuleId().equals("8888")) {
			if (convertView == null) {
				mHolder = new ViewHolder();
				convertView = View.inflate(activity, R.layout.jiazucaif, null);
				convertView.setTag(mHolder);
			} else {
				mHolder =(ViewHolder) convertView.getTag();
			}
			return convertView;
		} else {
			if (convertView == null) {
				mHolder = new ViewHolder();
				convertView = View.inflate(activity, R.layout.product_list, null);
				mHolder.productName = (TextView) convertView.findViewById(R.id.text_productName);
				mHolder.imageState = (ImageView) convertView.findViewById(R.id.imageState);
				mHolder.imageHot = (ImageView) convertView.findViewById(R.id.imageHot);
				mHolder. purchase = (TextView) convertView.findViewById(R.id.purchase);//起头金额
				mHolder.Collect_progress = (TextView) convertView.findViewById(R.id.Collect_progress);//募集进度
				mHolder.progres_layout=(View)convertView.findViewById(R.id.progres_layout);
				mHolder.minSureBuyPrice = (TextView) convertView.findViewById(R.id.text_minSureBuyPrice);//起购金额
				mHolder.raisedProcessShowb = (TextView) convertView.findViewById(R.id.textV_raisedProcessShowb);//百分比
				mHolder.ProductRat = (TextView) convertView.findViewById(R.id.textView_productRate);
				mHolder.progress = (ProgressBar) convertView.findViewById(R.id.progressBar_raisedProcessShow);
				mHolder.Progressimg=(ProgressBar)convertView.findViewById(R.id.Progressimage);
				
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			int Processint = ProductConstants.getRaisedProcessShow();
			mHolder.progress.setProgress(Processint);
			if (ProductConstants.getProductName() != null) {
				mHolder.productName.setText(ProductConstants.getProductName());
			} else {
				mHolder.productName.setText("");
			}
			
			if (ProductConstants.getRaisedProcessShow() != 0) {
				mHolder.raisedProcessShowb.setText(ProductConstants
						.getRaisedProcessShow() + "%");
			} else {
				mHolder.raisedProcessShowb.setText("0" + "%");
			}
			if (ProductConstants.getCountProductRate() != null) {
				if (ProductConstants.getCountProductRate().equals("")
						|| ProductConstants.getCountProductRate().equals("-1")
						) {

					mHolder.ProductRat.setText("浮动");
				} else {
					mHolder.ProductRat.setText(ProductConstants
							.getCountProductRate() + "%");
				}
			} else {

				mHolder.ProductRat.setText("");
	    	}
			
			if (ProductConstants.getMinSureBuyPrice() != null) {
				if(ProductConstants.getMinSureBuyPrice().equals("null")||ProductConstants.getMinSureBuyPrice().equals("")){
					ProductConstants.setMinSureBuyPrice("0");
				}
				mHolder.minSureBuyPrice.setText(Tools.Intercept(ProductConstants
						.getMinSureBuyPrice()) + "元");
			} else {
				mHolder.minSureBuyPrice.setText(" " + "元");
			}
			if (ProductConstants.getState() == 4) {
				mHolder.progres_layout.setVisibility(View.GONE);
				mHolder.imageState.setVisibility(View.VISIBLE);
				mHolder.imageState.setImageResource(R.drawable.advance_sale);//预售
				mHolder.productName.setTextColor(Color.BLACK); //产品名称  
				mHolder.purchase.setTextColor(Color.GRAY);////起头金额
				mHolder.Collect_progress.setTextColor(Color.GRAY);//募集进度
				mHolder.minSureBuyPrice.setTextColor(Color.BLACK);//
				mHolder.raisedProcessShowb.setTextColor(Color.rgb(0,174,239));//募集百分比
				mHolder.progress.setVisibility(View.VISIBLE);
				mHolder.Progressimg.setVisibility(View.GONE);
				mHolder.ProductRat.setTextColor(Color.RED);
			} else if (ProductConstants.getState() == 5) {
				mHolder.progres_layout.setVisibility(View.VISIBLE);
				mHolder.imageState.setVisibility(View.VISIBLE);
				mHolder.imageState.setImageResource(R.drawable.onsell);//在售		
				mHolder.productName.setTextColor(Color.BLACK); //产品名称  
				mHolder.purchase.setTextColor(Color.GRAY);////起头金额
				mHolder.Collect_progress.setTextColor(Color.GRAY);//募集进度
				mHolder.minSureBuyPrice.setTextColor(Color.BLACK);//
				mHolder.raisedProcessShowb.setTextColor(Color.rgb(0,102,204));//募集百分比
				mHolder.progress.setVisibility(View.VISIBLE);
				mHolder.Progressimg.setVisibility(View.GONE);
				mHolder.ProductRat.setTextColor(Color.RED);
			} else if (ProductConstants.getState() == 10) {
				mHolder.progres_layout.setVisibility(View.VISIBLE);
				mHolder.imageState.setVisibility(View.VISIBLE);
				mHolder.imageState.setImageResource(R.drawable.sold_out);//售罄
				mHolder.productName.setTextColor(Color.GRAY);   
				mHolder.purchase.setTextColor(Color.GRAY);
				mHolder.Collect_progress.setTextColor(Color.GRAY);
				mHolder.minSureBuyPrice.setTextColor(Color.GRAY);
				mHolder.raisedProcessShowb.setTextColor(Color.GRAY);
				mHolder.progress.setVisibility(View.GONE);
				mHolder.Progressimg.setVisibility(View.VISIBLE);
				mHolder.ProductRat.setTextColor(Color.GRAY);
			} else {
				mHolder.imageState.setVisibility(View.GONE);
			}
			if (ProductConstants.getIsHot()== 1) {
				mHolder.imageHot.setVisibility(View.VISIBLE);
			} else {
				mHolder.imageHot.setVisibility(View.GONE);
			}
		}
    	return convertView;
	}
	class ViewHolder {
		TextView productName;
		TextView productTerm;
		TextView minSureBuyPrice;
		TextView raisedProcessShowb;
		TextView ProductRat;
		ImageView imageState;
		ImageView imageHot;
		ProgressBar progress;
		TextView purchase;
		TextView Collect_progress;
		View progres_layout;
		ProgressBar Progressimg;
	}
	
	private void  ProductConstants(){
		
	}

}
