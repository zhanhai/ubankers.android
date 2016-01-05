package cn.com.ubankers.www.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ubankers.app.base.AppComponent;
import com.ubankers.app.base.AppModule;
import com.ubankers.app.base.DaggerAppComponent;
import com.ubankers.app.base.session.Session;
import com.ubankers.app.base.session.SessionManager;

import java.util.ArrayList;

import cn.com.ubankers.www.product.model.ProductDetail;
import cn.com.ubankers.www.user.model.RoleBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.utils.Tools;

public class MyApplication extends Application implements SessionManager {


	public static ImageLoader IMAGE_LOADER;
	private static DisplayImageOptions IMAGE_OPTIONS;
	public static MyApplication app;
	private static cn.com.ubankers.www.user.model.UserBean user;
	private static AsyncHttpClient client;
	public static ArrayList<Activity> listActivity = new ArrayList<Activity>();
	private static MyApplication instance;
	private static RoleBean type;
	private static ProductDetail product;
	private static String UserFaceId;//用户头像的id
	private static String orderInvestorId;//财富师给投资者预约，投资者的id

    private AppComponent component;

	public static String getOrderInvestorId() {
		return orderInvestorId;
	}
	public static void setOrderInvestorId(String orderInvestorId) {
		MyApplication.orderInvestorId = orderInvestorId;
	}
	public static String getUserFaceId() {
		return UserFaceId;
	}
	public static void setUserFaceId(String userFaceId) {
		UserFaceId = userFaceId;
	}
	// 单例模式中获取唯一的ExitApplication实例
    public static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}
	public ProductDetail getProduct() {
		return product;
	}
	public void setProduct(ProductDetail product) {
		this.product = product;
	}
	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		listActivity.add(activity);
	}
	// 遍历所有Activity并finish
	public void exit() {
		for (Activity activity : listActivity) {
			activity.finish();
		}
		System.exit(0);
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		initImageLoader(getApplicationContext());
		app = this;

        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this, this))
                .build();

		setDefaultUncaughtExceptionHandler();
	}


	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		// 整体摧毁的时候调用这个方法
	}
	/**
	 * 初始化ImageLoader 
	 * @param zhangpengfei
	 **/
	public void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				// .threadPoolSize(Thread.NORM_PRIORITY)
				.threadPriority(Thread.NORM_PRIORITY)
				// 线程池内加载的数量
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
//				 .discCache(new UnlimitedDiscCache(new
//				 File(Environment.getExternalStorageDirectory()+"ubankers/imageCache")))//
				 //	 自定义缓存路径
				 .defaultDisplayImageOptions(IMAGE_OPTIONS)
				.memoryCacheSize(2 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024).writeDebugLogs().build();
		IMAGE_LOADER = ImageLoader.getInstance();
		IMAGE_LOADER.init(config);// 全局初始化此配置
	}
	/**
	 * 下载图片
	 * @author Zhang
	 * @param uri
	 * @param imageView
	 */
	public static void loadImage(
			String uri,
			ImageView imageView,
			com.nostra13.universalimageloader.core.listener.ImageLoadingListener imageLoadingListener) {
		if (null == IMAGE_OPTIONS) {
			IMAGE_OPTIONS = new DisplayImageOptions.Builder()
					.cacheInMemory(true).cacheOnDisc(true).build();
		}
		IMAGE_LOADER.displayImage(uri, imageView, IMAGE_OPTIONS,
                imageLoadingListener);
	}
	public RoleBean getType() {
		return type;
	}
	public void setType(RoleBean type) {
		this.type = type;
	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}


	public AsyncHttpClient getClient(Context context) {
		if(client==null){
			client = new AsyncHttpClient();
			client.setSSLSocketFactory(Tools.getSocketFactory(context));
			client.setUserAgent("android/"+Tools.getVersion(context));
		}
		client.setTimeout(30000);
		PersistentCookieStore myCookieStore = new PersistentCookieStore(context);    
		client.setCookieStore(myCookieStore);   
		return client;
	}

	public static AsyncHttpClient getClient(){
		return app.client;
	}

	public void setClient(AsyncHttpClient client) {
		this.client = client;
	}

    public AppComponent getComponent(){
        return component;
    }

	public void exitApply() {
		user = null;
		app = null;
		client = null;

        component = null;
	}

	private static void setDefaultUncaughtExceptionHandler() {
		try {
			Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

				@Override
				public void uncaughtException(Thread t, Throwable e) {
					System.out.println("Uncaught Exception detected in thread " + t);
					e.printStackTrace();
				}
			});
		} catch (SecurityException e) {
			System.out.println("Could not set the Default Uncaught Exception Handler");
		}
	}

	@Override
	public void onLogin(UserBean user) {

	}

	@Override
	public void onLogout() {
		setUser(null);
		setClient(null);
	}

	@Override
	public Session getSession() {
		return new Session(user);
	}
}
