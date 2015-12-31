package cn.com.ubankers.www.authentication.view;

import java.util.List;

import cn.com.ubankers.www.authentication.controller.activity.WelcomeActivity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GuidePicAdapter  extends PagerAdapter{
    private Context context;
    private List<ImageView> list;
    private int count;
	
	public GuidePicAdapter(Context context,List<ImageView> list) {
		this.context = context;
		this.list = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(list!=null){
		count =list.size();	
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
    		 ImageView view = list.get(position);
      		if(view.getParent()==null){
      			((ViewPager)container).addView(view);
      		}else{
      		   ((ViewGroup)view.getParent()).removeView(view);
      		   ((ViewPager)container).addView(view); }
      		    }catch(Exception e){
      		       e.printStackTrace();
      		}
      		return list.get(position);
    }
   
    @Override
    public void destroyItem(View container, int position, Object object) {
    	// TODO Auto-generated method stub
    	((ViewPager)container).removeView((View)object);;
    }
}   
