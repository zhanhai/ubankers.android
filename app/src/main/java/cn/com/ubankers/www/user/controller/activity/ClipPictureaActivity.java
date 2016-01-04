package cn.com.ubankers.www.user.controller.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import cn.com.ubankers.www.R;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.product.service.ACache;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.utils.ClipView;
import cn.com.ubankers.www.utils.PhotoUtil;
import cn.com.ubankers.www.widget.ProcessDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
	/**
	 * @author Administrator
	 * 整体思想是：截取屏幕的截图，然后截取矩形框里面的图片
	 *
	 */
	public class ClipPictureaActivity extends Activity implements OnTouchListener,OnClickListener{
		ImageView srcPic;
		Button sure;
		ClipView clipview;
		 Bitmap bitmap;
		// These matrices will be used to move and zoom image
		Matrix matrix = new Matrix();
		Matrix savedMatrix = new Matrix();
		// We can be in one of these 3 states
		static final int NONE = 0;
		static final int DRAG = 1;
		static final int ZOOM = 2;
		private static final String TAG = "11";
		int mode = NONE;
		// Remember some things for zooming
		PointF start = new PointF();
		PointF mid = new PointF();
		float oldDist = 1f;
		private String activity;
		private ProcessDialog myDialog;
		private UserBean userBean;
		private Context context;
		private AsyncHttpClient client;
		private String shangfanzhangm;
		private ACache mCache;
		private Bitmap bitmapa;

		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			this.context=context;
			if (myDialog == null) {
				myDialog = ProcessDialog.createDialog(this, "正在加载中...");
			}
			client = MyApplication.app.getClient(context);
			if(MyApplication.app.getUser()!=null){
			   userBean = MyApplication.app.getUser();
			}
			
			mCache = ACache.get(this);
			Intent intent=getIntent();
			 String path= intent.getStringExtra("path");
			 activity=intent.getStringExtra("activity");
			 shangfanzhangm=intent.getStringExtra("shangfanzhangm");
			 if(path!=null||path.equals("")){
				  bitmap = PhotoUtil.getimage(path);
			 }
			setContentView(R.layout.clip_picture_activity);
			srcPic = (ImageView) this.findViewById(R.id.src_pic);
			if(bitmap!=null){
				srcPic.setImageBitmap(bitmap);
			}	
			srcPic.setOnTouchListener(this);
			sure = (Button) this.findViewById(R.id.sure);
			sure.setOnClickListener(this);
			
		}

		/*这里实现了多点触摸放大缩小，和单点移动图片的功能，参考了论坛的代码*/
		public boolean onTouch(View v, MotionEvent event)
		{
			ImageView view = (ImageView) v;
			// Handle touch events here...
			switch (event.getAction() & MotionEvent.ACTION_MASK)
				{
				case MotionEvent.ACTION_DOWN:
					savedMatrix.set(matrix);
					// 設置初始點位置
					start.set(event.getX(), event.getY());
					Log.d(TAG, "mode=DRAG");
					mode = DRAG;
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					oldDist = spacing(event);
					Log.d(TAG, "oldDist=" + oldDist);
					if (oldDist > 10f)
					{
						savedMatrix.set(matrix);
						midPoint(mid, event);
						mode = ZOOM;
						Log.d(TAG, "mode=ZOOM");
					}
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
					mode = NONE;
					Log.d(TAG, "mode=NONE");
					break;
				case MotionEvent.ACTION_MOVE:
					if (mode == DRAG)
					{
						// ...
						matrix.set(savedMatrix);
						matrix.postTranslate(event.getX() - start.x, event.getY()
								- start.y);
					} else if (mode == ZOOM)
					{
						float newDist = spacing(event);
						Log.d(TAG, "newDist=" + newDist);
						if (newDist > 10f)
						{
							matrix.set(savedMatrix);
							float scale = newDist / oldDist;
							matrix.postScale(scale, scale, mid.x, mid.y);
						}
					}
					break;
				}

			view.setImageMatrix(matrix);
			return true; // indicate event was handled
		}

		/** Determine the space between the first two fingers */
		private float spacing(MotionEvent event)
		{
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return FloatMath.sqrt(x * x + y * y);
		}

		/** Calculate the mid point of the first two fingers */
		private void midPoint(PointF point, MotionEvent event)
		{
			float x = event.getX(0) + event.getX(1);
			float y = event.getY(0) + event.getY(1);
			point.set(x / 2, y / 2);
		}

		/*获取矩形区域内的截图*/
		public void onClick(View v){
//			Bitmap fianBitmap = getBitmap();
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			fianBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//			byte[] bitmapByte = baos.toByteArray();
//			bitmapa = BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length);
////			if( shangfanzhangm.equals("1")){
////				IdCardActivity.shenfenzhenga.setImageBitmap(bitmapa);
////			}else if( shangfanzhangm.equals("2")){
////				IdCardActivity.shenfenzhengb.setImageBitmap(bitmapa);
////			}else if(shangfanzhangm.equals("3")){
////				BusinessCardActivity.mingpian_tu.setImageBitmap(bitmapa);
////			}
//			String imageUrl = PhotoUtil.bitmaptoString(bitmapa);
//			uploadingAvatar(imageUrl);
		}

		// 获取状态栏高度
		private Bitmap getBitmap(){
			getBarHeight();
			Bitmap screenShoot = takeScreenShot();
		
			clipview = (ClipView)this.findViewById(R.id.clipview);
			int width = clipview.getWidth();
			int height = clipview.getHeight();
			Bitmap finalBitmap = Bitmap.createBitmap(screenShoot,
					(width - height /3) / 2, height / 3 + titleBarHeight + statusBarHeight, height / 3, height / 3);
			return finalBitmap;
		}

		int statusBarHeight = 0;
		int titleBarHeight = 0;

		private void getBarHeight()
		{
			// 获取状态栏高度
			Rect frame = new Rect();
			this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
			statusBarHeight = frame.top;
			
			int contenttop = this.getWindow()
					.findViewById(Window.ID_ANDROID_CONTENT).getTop();
			// statusBarHeight是上面所求的状态栏的高度
			titleBarHeight = contenttop - statusBarHeight;
			
			Log.v(TAG, "statusBarHeight = " + statusBarHeight
					+ ", titleBarHeight = " + titleBarHeight);
		}

		// 获取Activity的截屏
		private Bitmap takeScreenShot()
		{
			View view = this.getWindow().getDecorView();
			view.setDrawingCacheEnabled(true);
			view.buildDrawingCache();
			return view.getDrawingCache();
		}
		
		public void uploadingAvatar(String imageUrl) {
			myDialog.show();
			RequestParams params = new RequestParams();
			params.put("picData", imageUrl);
			client.post(ClipPictureaActivity.this, HttpConfig.URL_UPLOADPHOTO, params, new JsonHttpResponseHandler() {
						private boolean flag;
						private String errorCode;

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							try {
								flag = response.getBoolean("success");
								if (flag) {
									JSONObject jsonObject = response
											.getJSONObject("result");
									errorCode = jsonObject.getString("errorCode");
									JSONObject info = jsonObject
											.getJSONObject("info");
									String userFaceId = info.getString("ids");
									if (shangfanzhangm.equals("1")) {
										mCache.put("frontfid",userFaceId);
										mCache.put("test_key3", "test value",120* ACache.TIME_DAY);
										ClipPictureaActivity.this.finish();
										myDialog.show();
									} else if (shangfanzhangm.equals("2")) {
										mCache.put("backfid", userFaceId);
										mCache.put("test_key3", "test value",120* ACache.TIME_DAY);
										ClipPictureaActivity.this.finish();
										myDialog.show();
									}else if(shangfanzhangm.equals("3")){
										mCache.put("BusinessCard", userFaceId);
										mCache.put("test_key3", "test value",120* ACache.TIME_DAY);
										ClipPictureaActivity.this.finish();
									}
									myDialog.dismiss();

								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								String responseString, Throwable throwable) {
							System.out.println("失败");
							myDialog.show();
							super.onFailure(statusCode, headers, responseString,
									throwable);
						}
						@Override
						public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							Toast.makeText(context, "请求超时，请重试。", Toast.LENGTH_SHORT).show();
							myDialog.dismiss();
						}
					});
		}

	}
