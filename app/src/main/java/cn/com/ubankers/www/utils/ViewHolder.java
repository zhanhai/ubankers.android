package cn.com.ubankers.www.utils;



import android.util.SparseArray;
import android.view.View;

public class ViewHolder {
	/**
	 * 工具类的原理：
	 * 1、ViewHolder既然是依赖View的Tag存放，但是以一个 SparseArray 集合存放。
	   2、判断View里的Tag是否存在viewHolder，不存在，赶紧叫她生一个。
	   3、然后在viewholder（也就是SparseArray）寻找View的索引，如果没有，赶紧findViewById一个put进去顺便return出来，如果已经存在，皆大欢喜，直接用呗。

	 * @param view
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View view, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
	    if (viewHolder == null) {
	      viewHolder = new SparseArray<View>();
	      view.setTag(viewHolder);
	    }
	    View childView = viewHolder.get(id);
	    if (childView == null) {
	      childView = view.findViewById(id);
	      viewHolder.put(id, childView);
	    }
	    return (T) childView;
	  }

}
