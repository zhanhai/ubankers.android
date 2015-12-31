package cn.com.ubankers.www.sns.view;

import java.util.Date;
import java.util.List;





import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class SnsArticleAdapter extends PagerAdapter{
	
	private Context context;
	private List<ImageView> list;
	private  Date date = null;
	private int count;
	public SnsArticleAdapter(Context context, List<ImageView> list){
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
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}
	 @Override
	    public Object instantiateItem(View container, int position) {
	    	// TODO Auto-generated method stub
		       try{
	    		if(list.get(position).getParent()==null){
	    			((ViewPager)container).addView(list.get(position));
	    		}else{
	    		   ((ViewGroup)list.get(position).getParent()).removeView(list.get(position));
	    		   ((ViewPager)container).addView(list.get(position)); 
	    		   }
	    		    }catch(Exception e){
	    		       e.printStackTrace();
	    		    } 
	    return list.get(position);
	    }
	    @Override
	    public void destroyItem(View container, int position, Object object) {
	    	((ViewPager)container).removeView((View)object);;
	    }
}

