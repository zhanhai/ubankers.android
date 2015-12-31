package cn.com.ubankers.www.sns.view;

import java.util.List;












import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.sns.model.ArticleBean;
import cn.com.ubankers.www.sns.model.ColArticleBean;
import cn.com.ubankers.www.utils.Tools;
import cn.com.ubankers.www.utils.ViewHolder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class FavorCommentAdapter extends BaseAdapter {
	private List<ColArticleBean> list;
	private Context context;
	private int count;
	public FavorCommentAdapter(Context context,List<ColArticleBean> list) {
		super();
		this.list = list;
		this.context =context;
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
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		        ColArticleBean colArticleBean = (ColArticleBean)list.get(arg0);
		        View view = LayoutInflater.from(context).inflate(R.layout.article, null);
		        try {
		        	TextView title =(TextView) view.findViewById(R.id.tvName);			
					ImageView imageCover=(ImageView) view.findViewById(R.id.ivAlbumPic);		
					TextView create_date=(TextView) view.findViewById(R.id.tvDuration);
					TextView read = (TextView) view.findViewById(R.id.eye);
					TextView author_name=(TextView) view.findViewById(R.id.tvAuthor);
					imageCover.setScaleType(ScaleType.CENTER_CROP);
					ImageView VIP=ViewHolder.get(view, R.id.VIP); //VIP标志				
					VIP.setVisibility(view.VISIBLE);
					if(colArticleBean.getIdentifying()==1){
						VIP.setBackgroundResource(R.drawable.vipyellow);
					}else if(colArticleBean.getIdentifying()==2){
						VIP.setBackgroundResource(R.drawable.vipblue);
					}
					else if(colArticleBean.getIdentifying()==0){
						VIP.setVisibility(view.GONE);
					}
					
					title.setText(colArticleBean.getFavor_content_title());
					author_name.setText(colArticleBean.getFavor_author());
					String url =colArticleBean.getUserface();
					MyApplication.loadImage(HttpConfig.HTTP_IMAGE_QUERY_URL+colArticleBean.getFavor_cover(), imageCover, null);	
					String time =colArticleBean.getFavor_date(); 
					String str = Tools.getDateM(time);
					create_date.setText(str);
					read.setText(colArticleBean.getFavor_commentCount()+"");
				} catch (Exception e) {
					// TODO: handle exception
				}
			
			return view;
           }
}
