package cn.com.ubankers.www.widget;
import cn.com.ubankers.www.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyDialog extends Dialog {  
	private Context context = null;
	private static MyDialog commonProgressDialog = null;
	
	public MyDialog(Context context){
		super(context);
		this.context = context;
	}
	
	public MyDialog(Context context, int theme) {
        super(context, theme);
    }
	
	public static MyDialog createDialog(Context context,String msg){
			LayoutInflater inflater = LayoutInflater.from(context);  
	        View v = inflater.inflate(R.layout.common_progressdialog, null);// 得到加载view  
	        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局  
	        // main.xml中的ImageView  
	        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);  
	        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字  
	        // 加载动画  
	        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(  
	                context, R.anim.loading_animation);  
	        // 使用ImageView显示动画  
	        spaceshipImage.startAnimation(hyperspaceJumpAnimation);  
	        tipTextView.setText(msg);// 设置加载信息  
	  
	        commonProgressDialog = new MyDialog(context, R.style.loading_dialog);// 创建自定义样式dialog  
	  
	        commonProgressDialog.setCancelable(false);// 不可以用“返回键”取消  
	        commonProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
	        commonProgressDialog.setContentView(layout, new LinearLayout.LayoutParams(  
	                LinearLayout.LayoutParams.FILL_PARENT,  
	                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局  
	        return commonProgressDialog;  
	}
 
//    public void onWindowFocusChanged(boolean hasFocus){
//    	
//    	if (commonProgressDialog == null){
//    		return;
//    	}
//    	
//        ImageView imageView = (ImageView) commonProgressDialog.findViewById(R.id.loadingImageView);
//        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
//        animationDrawable.start();
//    }
 
    /**
     * 
     * [Summary]
     *       setTitile 标题
     * @param strTitle
     * @return
     *
     */
    public MyDialog setTitile(String strTitle){
    	return commonProgressDialog;
    }
    
//    /**
//     * 
//     * [Summary]
//     *       setMessage 提示内容
//     * @param strMessage
//     * @return
//     *
//     */
//    public MyDialog setMessage(String strMessage){
//    	TextView tvMsg = (TextView)commonProgressDialog.findViewById(R.id.id_tv_loadingmsg);
//    	
//    	if (tvMsg != null){
//    		tvMsg.setText(strMessage);
//    	}
//    	
//    	return commonProgressDialog;
//    }
    }  
