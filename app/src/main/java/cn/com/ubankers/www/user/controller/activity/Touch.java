package cn.com.ubankers.www.user.controller.activity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import cn.com.ubankers.www.R;
import cn.com.ubankers.www.widget.ProcessDialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class Touch extends Activity implements OnTouchListener {

	// public static final String TAG = "Touch";

	static final float MIN_ZOOM_SCALE = 0.1f;
	static final float MAX_ZOOM_SCALE = 4.0f;

	static final int NONE = 0; // 初始状态
	static final int DRAG = 1;
	static final int ZOOM = 2;
	static final float MIN_FINGER_DISTANCE = 10.0f;
	private static final int IO_BUFFER_SIZE = 0;
	int ACTION_DOWNS=0;
	int gestureMode;

	PointF prevPoint;
	PointF nowPoint;
	PointF midPoint;
	
	float prevFingerDistance;
	Handler handler;
	Matrix matrix;
	ImageView mImageView;
	Bitmap mBitmap;
	DisplayMetrics mDisplayMetrics;
	private Intent Intent;
	private String url;
	private Bitmap bitmap;
	private ProcessDialog myDialog;
	private View touch_back;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(myDialog==null){
			   myDialog= ProcessDialog.createDialog(this, "正在加载中...");
			}
        Intent=getIntent();
        url=Intent.getStringExtra("url");  
//		url="https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/logo_white_fe6da1ec.png";
		gestureMode = NONE;
		prevPoint = new PointF();
		nowPoint = new PointF();
		midPoint = new PointF();
		matrix = new Matrix();

		setContentView(R.layout.scale);
//		touch_back=findViewById(R.id.touch_back);
		mImageView = (ImageView) findViewById(R.id.imag);
		mImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(X==preX&&Y==preY){
					Touch.this.finish();
				}
			}
		});
		if(url.equals("empty")){
			mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.lijiang);
			mImageView.setImageBitmap(mBitmap);
			Touchxy(mBitmap);
		}else{
			myDialog.show();
			ImageLoader.getInstance().loadImage(url,new ImageLoadingListener() {
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					mImageView.setImageBitmap(loadedImage);
					myDialog.dismiss();
					Touchxy(loadedImage);
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		
		
	}
	
	float preX;
	float preY;
	float X;
	float Y;

	
	public void Touchxy(Bitmap mBitmap){	
		mImageView.setOnTouchListener(this);
 		mDisplayMetrics = new DisplayMetrics();
 		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics); // 获取屏幕信息（分辨率等）
 		initImageMatrix(mBitmap);
 		mImageView.setImageMatrix(matrix);
	}

	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN: // 涓荤偣鎸変笅
			gestureMode = DRAG;
			prevPoint.set(event.getX(), event.getY());
			preX=event.getX();
			preY=event.getY();
			break;
		case MotionEvent.ACTION_POINTER_DOWN: // 鍓偣鎸変笅
			prevFingerDistance = getFingerDistance(event);
			if (getFingerDistance(event) > MIN_FINGER_DISTANCE) {
				gestureMode = ZOOM;
				setMidpoint(midPoint, event);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (gestureMode == DRAG) {
				nowPoint.set(event.getX(), event.getY());
				matrix.postTranslate(nowPoint.x - prevPoint.x, nowPoint.y
						- prevPoint.y);
				prevPoint.set(nowPoint);
			} else if (gestureMode == ZOOM) {
				float currentFingerDistance = getFingerDistance(event);
				if (currentFingerDistance > MIN_FINGER_DISTANCE) {
					float zoomScale = currentFingerDistance
							/ prevFingerDistance;
					matrix.postScale(zoomScale, zoomScale, midPoint.x,
							midPoint.y);
					prevFingerDistance = currentFingerDistance;
				}
				checkImageViewSize();
			}
			mImageView.setImageMatrix(matrix);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			gestureMode = NONE;
			X=event.getX();
			Y=event.getY();
			break;
		}
		return false;
	}

	/**
	 * 限制图片缩放比例：不能太小，也不能太大
	 */
	private void checkImageViewSize() {
		float p[] = new float[9];
		matrix.getValues(p);
		if (gestureMode == ZOOM) {
			if (p[0] < MIN_ZOOM_SCALE) {
				float tScale = MIN_ZOOM_SCALE / p[0];
				matrix.postScale(tScale, tScale, midPoint.x, midPoint.y);
			} else if (p[0] > MAX_ZOOM_SCALE) {
				float tScale = MAX_ZOOM_SCALE / p[0];
				matrix.postScale(tScale, tScale, midPoint.x, midPoint.y);
			}
		}
	}

	private void initImageMatrix(Bitmap mBitmap) {
		float initImageScale = Math.min(1.0f, Math.min(
				(float) mDisplayMetrics.widthPixels
						/ (float) mBitmap.getWidth(),
				(float) mDisplayMetrics.heightPixels
						/ (float) mBitmap.getHeight()));
		if (initImageScale < 1.0f) { // 图片比屏幕大，需要缩小
			matrix.postScale(initImageScale, initImageScale);
		}

		RectF rect = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
		matrix.mapRect(rect); // 按 initImageScale 缩小矩形，或者不变

		float dx = (mDisplayMetrics.widthPixels - rect.width()) / 2.0f;
		float dy = (mDisplayMetrics.heightPixels - rect.height()) / 2.0f;
		matrix.postTranslate(dx, dy);
	}

	private float getFingerDistance(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	private void setMidpoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2.0f, y / 2.0f);
	}



//	@Override
//	public void onClick(View arg0) {
//		// TODO Auto-generated method stub
//	switch (arg0.getId()) {
//	case R.id.touch_back:
//		Touch.this.finish();
//		break;
//	case R.id.imag:
//		Touch.this.finish();
//		break;
//		
//		
//		}
//	}
}
