package cn.com.ubankers.www.sns.view;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.sns.controller.fragment.ArticleFragment;
import cn.com.ubankers.www.sns.model.ArticleBean;
import cn.com.ubankers.www.utils.Tools;
import cn.com.ubankers.www.utils.ViewHolder;
import cn.com.ubankers.www.utils.XutilsHttp;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class ArticleListAdapter extends BaseAdapter {
	private Context context;
	private List<ArticleBean> list;
	private  Date date = null;
	private SimpleDateFormat format = null;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private int count;
	private ImageView comment,articleType;
	public ArticleListAdapter(Context context,List<ArticleBean> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(list!=null){
			count = list.size();	
		}
		return count;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ArticleBean articleBean =(ArticleBean) list.get(position);
	    ViewHolder viewHolder =null;
	    if(convertView==null){
	    	viewHolder = new ViewHolder();
	    	convertView = LayoutInflater.from(context).inflate(R.layout.newarticle, null);
	    	comment=(ImageView)convertView.findViewById(R.id.commentey);
	    	viewHolder.title =(TextView) convertView.findViewById(R.id.tvName);			
	    	viewHolder.create_date =(TextView) convertView.findViewById(R.id.tvDuration);			
	    	viewHolder.read =(TextView) convertView.findViewById(R.id.eye);			
	    	viewHolder.author_name =(TextView) convertView.findViewById(R.id.tvAuthor);			
	    	viewHolder.imageCover=(ImageView) convertView.findViewById(R.id.ivAlbumPic);	
	    	viewHolder.VIP=(ImageView) convertView.findViewById(R.id.VIP);
	    	viewHolder.articleType=(ImageView)convertView.findViewById(R.id.articleType);
	    	viewHolder.abstractLabel=(TextView)convertView.findViewById(R.id.label);
	    	viewHolder.content=(TextView)convertView.findViewById(R.id.content);
	    	viewHolder.icon=(ImageView)convertView.findViewById(R.id.icon);
	    	viewHolder.imageCover.setScaleType(ScaleType.CENTER_CROP);
	    	viewHolder.imageCover.setTag(articleBean.getCover());
	    	viewHolder.VIP.setVisibility(View.GONE);
	    	viewHolder.articleType.setVisibility(View.GONE);
	    	viewHolder.icon.setVisibility(View.GONE);
			convertView.setTag(viewHolder);
	      }else{
	    	viewHolder = (ViewHolder) convertView.getTag();
	      } 
	
		  if(articleBean.getIdentifying()==1){
			    viewHolder.VIP.setVisibility(View.VISIBLE);
				viewHolder.VIP.setBackgroundResource(R.drawable.vipyellow);
		   }else if(articleBean.getIdentifying()==2){
				viewHolder.VIP.setVisibility(View.VISIBLE);
				viewHolder.VIP.setBackgroundResource(R.drawable.vipblue);
		   }else if(articleBean.getIdentifying()==0){
				viewHolder.VIP.setVisibility(View.GONE);
		   }
		  	  
				 viewHolder.read.setText(articleBean.getRead_count()+""); 
				 try {
					   StringBuffer sb = new StringBuffer();
					   JSONArray jsonArray = new JSONArray(articleBean.getTags());
					   int iSize = jsonArray.length();
					   for (int i = 0; i < iSize; i++) {
							String string = jsonArray.getString(i);
					        sb.append(string+" ");
					   }
					   String lib = sb.toString();
//					   Log.e("aaa", lib.length()+"");
					   try {
						   if(lib.length()==0){
							   viewHolder.icon.setVisibility(View.GONE);
							   viewHolder.abstractLabel.setText("");
						   }else{
							   viewHolder.icon.setVisibility(View.VISIBLE);
							   viewHolder.icon.setBackgroundResource(R.drawable.iconbutton);
							   viewHolder.abstractLabel.setText(lib);
						   }
					} catch (Exception e) {
						// TODO: handle exception
					}
					   
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 if(articleBean != null && articleBean.getTitle() != null){
					 viewHolder.title.setText(articleBean.getTitle());
				 }
		   
		   if(articleBean != null && articleBean.getAbstracting() != null){
			   viewHolder.content.setText(articleBean.getAbstracting());
		   }
		   
		   if(articleBean!=null&&articleBean.getAuthor()!=null&&articleBean.getAuthor().getAuthor_name()!=null){
			   viewHolder.author_name.setText(articleBean.getAuthor().getAuthor_name()); 
		   }
		   if(articleBean!=null&&articleBean.getSourceBean()!=null&&articleBean.getSourceBean().getSource_detail()!=null){
			   Log.i("aaaaaaa", articleBean.getSourceBean().getSource_detail());
		   }
	
	  
	  
		   if(articleBean!=null&&articleBean.getSourceBean()!=null&&articleBean.getSourceBean().getSource_type()!=null){
			   Log.i("ssssssss", articleBean.getSourceBean().getSource_type());
			 
			   if(Integer.parseInt(articleBean.getSourceBean().getSource_type())==0){
				   viewHolder.articleType.setVisibility(View.VISIBLE);
				   viewHolder.articleType.setBackgroundResource(R.drawable.original);
			   }else if(Integer.parseInt(articleBean.getSourceBean().getSource_type())==1){
				   viewHolder.articleType.setVisibility(View.VISIBLE);
				   viewHolder.articleType.setBackgroundResource(R.drawable.translation);
			   }else if(Integer.parseInt(articleBean.getSourceBean().getSource_type())==2){
				   viewHolder.articleType.setVisibility(View.VISIBLE);
				   viewHolder.articleType.setBackgroundResource(R.drawable.transfer);
			   }
		   }else {
			   viewHolder.articleType.setVisibility(View.GONE);
		}
//		   imageLoader.displayImage(HttpConfig.HTTP_QUERY_URL+articleBean.getCover(), viewHolder.imageCover);
//		   new XutilsHttp(context).display(viewHolder.imageCover,HttpConfig.HTTP_QUERY_URL+articleBean.getCover());
		   imageLoader.displayImage(HttpConfig.HTTP_QUERY_URL+articleBean.getCover(), viewHolder.imageCover,ArticleFragment.options);
		   String time =articleBean.getCreate_date(); 
		   String str = Tools.getDateM(time);
		   viewHolder.create_date.setText(str);
		  
			
		return convertView;
	}
	
	class ViewHolder{
		TextView title;
		TextView create_date;
		TextView read,abstractLabel,content;
		TextView author_name;
		ImageView imageCover;
		ImageView VIP,icon,articleType;
	}
}
		
		
	


