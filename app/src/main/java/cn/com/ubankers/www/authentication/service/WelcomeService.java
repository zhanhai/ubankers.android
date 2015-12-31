package cn.com.ubankers.www.authentication.service;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.authentication.controller.activity.MainActivity;
import cn.com.ubankers.www.authentication.controller.activity.WelcomeActivity;

public class WelcomeService {
	private Context context;
	private ArrayList<ImageView> list ;
	public static int images[] = {R.drawable.guide1,R.drawable.guide2,R.drawable.guide3,R.drawable.guide4,R.drawable.guide5};
	public WelcomeService(Context context) {
		super();
		this.context = context;
	}
	public void loginToApplition(){
		Intent mainIntent = new Intent(context,MainActivity.class);
		mainIntent.putExtra("type", true);
		context.startActivity(mainIntent);
		((WelcomeActivity)context).finish();
		((WelcomeActivity)context).overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}
    public ArrayList<ImageView> guidePage(){
    	list = new ArrayList<ImageView>();
		for(int i=0;i<5;i++){
			ImageView imageView = new ImageView(context);
			imageView.setBackgroundResource(images[i]);
			list.add(imageView);
		}
    	return list;   
   }
   
}
