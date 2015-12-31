package cn.com.ubankers.www.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * @author yangyu
 *	功能描述：弹窗内部子类项（绘制标题和图标）
 */
public class ActionItem {
	//定义图片对象
	public Drawable mDrawable;
	//定义文本对象
	public CharSequence mTitle;
	
	public ActionItem(Drawable drawable, CharSequence title){
		this.mDrawable = drawable;
		this.mTitle = title;
	}
	
	public ActionItem(Context context, int titleId, Drawable drawableId){
		this.mTitle = context.getResources().getText(titleId);
		this.mDrawable = drawableId;
	}
	
	public ActionItem(Context context, CharSequence title) {
		this.mTitle = title;
	}
}
