package cn.com.ubankers.www.widget;

import cn.com.ubankers.www.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

public class SnsDialog extends Dialog {  
	private Activity activity = null;
	private static SnsDialog commonProgressDialog = null;
	
	public SnsDialog(Activity activity){
		super(activity);
		this.activity = activity;
	}
	
	public SnsDialog(Activity activity, int theme) {
        super(activity, theme);
    }
	
	public static SnsDialog createDialog(Activity activity){
		commonProgressDialog = new SnsDialog(activity,R.style.CommonProgressDialog);
		commonProgressDialog.setContentView(R.layout.sns_dialog);
		commonProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		
		return commonProgressDialog;
	}
 
    public void onWindowFocusChanged(boolean hasFocus){
    	
    	if (commonProgressDialog == null){
    		return;
    	}
    	
        ImageView imageView = (ImageView) commonProgressDialog.findViewById(R.id.loadingImageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }
 
    /**
     * 
     * [Summary]
     *       setTitile 标题
     * @param strTitle
     * @return
     *
     */
    public SnsDialog setTitile(String strTitle){
    	return commonProgressDialog;
    }
    
    /**
     * 
     * [Summary]
     *       setMessage 提示内容
     * @param strMessage
     * @return
     *
     */
    public SnsDialog setMessage(String strMessage){
    	TextView tvMsg = (TextView)commonProgressDialog.findViewById(R.id.id_tv_loadingmsg);
    	
    	if (tvMsg != null){
    		tvMsg.setText(strMessage);
    	}
    	
    	return commonProgressDialog;
    }
    }  

