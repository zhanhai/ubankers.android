package cn.com.ubankers.www.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

public class PhotoUtil {
	public final static int PHOTO_PICKED_WITH_DATA = 1;
	public final static int CROP_PHOTO_WITH_DATA = 2;
	public final static int CAMERA_WITH_DATA = 3;
	private static final File PHOTO_DIR = new File(getCameraPath());
	private static File takePhotoFile;
	public static String getCameraPath()
	{
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/Ronganonline/Camera/");
		if (!file.exists()) {
			file.mkdirs();
		}
		
		return file.getAbsolutePath();
	}
	public static File getTakePhotoFile() {
		return takePhotoFile;
	}
	@SuppressLint("NewApi")
	public static String getPicPath(Uri photoUri, Activity activity) {
		String uriString = photoUri.toString();
		if (uriString.startsWith("file://")) {
			File f = new File(photoUri.getPath());
			return f.getAbsolutePath();
		}
		try {
			if (uriString.startsWith("content://media/")) {
				String[] proj = { MediaStore.Images.Media.DATA };
				Cursor cursor = activity.getContentResolver().query(photoUri,
						proj, null, null, null);
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				String path = cursor.getString(column_index);
				return path;
			} else if (uriString.startsWith("content://com.android.providers.")) {
				Bitmap bitmap = null;
				try {
					bitmap = BitmapFactory.decodeStream(activity
							.getContentResolver().openInputStream(
									photoUri));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return "";
				}
				if (bitmap != null) {
					return saveThePicture(bitmap);
				}
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return "";
	}
	/**
	 * 从相册获取图片
	 * 
	 * @author liaozhunhua
	 * @date 2015-6-18
	 * @param isCrop
	 *            是否裁剪图片
	 * @return
	 */
	public static void doPickPhotoFromGallery(Activity activity,boolean isCrop) {
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			if (isCrop) {
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 80);
				intent.putExtra("outputY", 80);
			}
			activity.startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 拍照获取图片
	 * 
	 * @author liaozhunhua
	 * @date 2015-6-18
	 */
	public static void doTakePhoto(Activity activity) {
		try {
			PHOTO_DIR.mkdirs();// 创建照片的存储目录
			takePhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(takePhotoFile)); 
			activity.startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {

		}
	}
	/**
	 * 用当前时间给取得的图片命名
	 * 
	 */
	private static String getPhotoFileName() {
		return System.currentTimeMillis() + ".png";
	}

	/**
	 * 裁剪图片
	 * 
	 * @author liaozhunhua
	 * @date 2015-6-18
	 * @param activity
	 * @param f
	 */
	public static void doCropPhoto(Activity activity, File f) {
		try {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(Uri.fromFile(f), "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 100);
			intent.putExtra("outputY", 100);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			intent.putExtra("return-data", false);
			activity.startActivityForResult(intent, CROP_PHOTO_WITH_DATA);
		} catch (Exception e) {
		}
	}

	
	public static void doCropPhotosfyz(Activity activity, File f) {
		try {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(Uri.fromFile(f), "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 2);
			intent.putExtra("outputX", 10);
			intent.putExtra("outputY", 20);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			intent.putExtra("return-data", false);
			activity.startActivityForResult(intent, CROP_PHOTO_WITH_DATA);
		} catch (Exception e) {
		}
	}
		//质量压缩的方法
		public static Bitmap compressImage(Bitmap image) { 
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();   
	        image.compress(Bitmap.CompressFormat.JPEG, 60, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中   
	        int options = 80;   

	        while ( (baos.size()/1024) > 80 ) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩          
	            baos.reset();//重置baos即清空baos   
	            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中   
	            options -= 10;//每次都减少10   
	        }   
	        // 把压缩后的数据baos存放到ByteArrayInputStream中 
	        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());  
	        // 把ByteArrayInputStream数据生成图片   
	        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
	        return bitmap;   
	    } 
		//图片按比例大小压缩方法（根据路径获取图片并压缩） 
		public static Bitmap getimage(String srcPath) { 
			
			File filePic = new File(srcPath);
			long lnFileSize = 0;
			if (filePic.exists()) 
				lnFileSize =  filePic.length()/1024;
			else
				return null;
			
			float dh = 800f;//这里设置高度为800f   
	        float dw = 480f;//这里设置宽度为480f   
	        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();  
			bmpFactoryOptions.inJustDecodeBounds = true;  
			Bitmap bmpBoundTemp = null;
			
			try  
			{
			    FileInputStream fs=null;
			    fs = new FileInputStream(filePic);
			    // 解决BitmapFactory.decodeFile OutOfMemoryError的问题
				bmpBoundTemp = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bmpFactoryOptions);
				
				// bmpBoundTemp = BitmapFactory.decodeFile(srcPath, bmpFactoryOptions); 
				int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/dh);  
				int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/dw);  
		
				if (heightRatio > 1 && widthRatio > 1)  
				{  
				   bmpFactoryOptions.inSampleSize = (heightRatio>widthRatio) ? heightRatio:widthRatio;  
				}  
				bmpFactoryOptions.inJustDecodeBounds = false;  
				bmpBoundTemp = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bmpFactoryOptions);
				// bmpBoundTemp = BitmapFactory.decodeFile(srcPath, bmpFactoryOptions);
				if( lnFileSize > 65 )
					return compressImage(bmpBoundTemp);  //压缩好比例大小后再进行质量压缩  
				else
					return bmpBoundTemp;
			}  
			catch(Exception e){
				e.printStackTrace();
				return bmpBoundTemp;
			}

		 } 
		//把图片保存到本地sd卡的目录下
		public static String saveThePicture(Bitmap bitmap){
				File file = new File(Environment.getExternalStorageDirectory()
						+ "/Ronganonline/Camera/","small" + System.currentTimeMillis()+".png");
				if(!file.exists()){
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					FileOutputStream fos = new FileOutputStream(file);
					if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)) {
						fos.flush();
						fos.close();
					}
				} catch (Exception e){
					e.printStackTrace();
				}
				return file.getAbsolutePath();
		}
		/**  
		    * Drawable转化为Bitmap  
		    */  
		public static Bitmap drawableToBitmap(Drawable drawable) {
			Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
												drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
												: Bitmap.Config.RGB_565);

	        Canvas canvas = new Canvas(bitmap);
	        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
	        drawable.draw(canvas);
	        return bitmap;
	    }
		
		//字符串转base64编码
		public static String  StringtoString(String str){
			String string=null;
			string = Base64.encodeToString(str.getBytes(),Base64.DEFAULT);
			return string;
		}
		
		// 图片转成BASE64编码
		public static String bitmaptoString(Bitmap bitmap){
			 String string=null;
			 ByteArrayOutputStream bStream=new ByteArrayOutputStream();
			 bitmap.compress(CompressFormat.PNG,100,bStream);
			 byte[]bytes=bStream.toByteArray();
			 string=Base64.encodeToString(bytes,Base64.DEFAULT);
			 return string;
	   }
		
		 //Base64编码转成图片
		public  static Bitmap stringtoBitmap(String string){
		   
		    Bitmap bitmap=null;
		    try {
		    	byte[]bitmapArray;
		    	bitmapArray=Base64.decode(string, Base64.DEFAULT);
		    	bitmap=BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		    return bitmap;
	   }
		//获取res下的图片
		public static Bitmap getRes(String name,Context context) {
			ApplicationInfo appInfo = context.getApplicationInfo();
			int resID = context.getResources().getIdentifier(name, "drawable",appInfo.packageName);
			return BitmapFactory.decodeResource(context.getResources(), resID);
		}
		
}
