package cn.com.ubankers.www.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;



/**
 * @类描述:自定义List
 * 
 * @date:2015-5-30 下午4:47:43
 * 
 * @Version:1.0.0
 */
public class CommentListView extends ListView{
	/* private ScrollView parentScrollView;

	 public ScrollView getParentScrollView() {
	        return parentScrollView;
	 }

	 public void setParentScrollView(ScrollView parentScrollView) {
	        this.parentScrollView = parentScrollView;
	 }

	 private int maxHeight;

	 public int getMaxHeight() {
	       return maxHeight;
	 }

	 public void setMaxHeight(int maxHeight) {
	        this.maxHeight = maxHeight;
	 }
*/
	public CommentListView(Context context) { 
        super(context); 
        // TODO Auto-generated constructor stub 
    } 
    public CommentListView(Context context, AttributeSet attrs) { 
        super(context, attrs); 
        // TODO Auto-generated constructor stub 
    } 
    public CommentListView(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle); 
        // TODO Auto-generated constructor stub 
    } 
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
/*	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		 switch (ev.getAction()) {
         case MotionEvent.ACTION_DOWN:

             setParentScrollAble(false);
         case MotionEvent.ACTION_MOVE:

             break;
         case MotionEvent.ACTION_UP:

         case MotionEvent.ACTION_CANCEL:
             setParentScrollAble(true);
             break;
         default:
             break;
     }
     return super.onInterceptTouchEvent(ev);
	}*/
	  /**
     * @param flag
     */
   /* private void setParentScrollAble(boolean flag) {
    	parentScrollView.requestDisallowInterceptTouchEvent(!flag);
   }*/
}