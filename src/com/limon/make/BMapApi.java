package com.limon.make;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.limon.common.Caller;
import com.limon.common.ImageCache;
import com.limon.common.RequestCache;
import com.limon.common.ResourceUtil;

public class BMapApi extends Application {

	
	// 百度MapAPI的管理类
	BMapManager mBMapMan = null;
	protected Context mContext;
	/**
	 * Image cache, one for all activities and orientations
	 */
	private static ImageCache mImageCache;

	public static final int FLUSH_MAIN_ACTIVITY = 1;
	private Handler main;

	//用于收集所有可能打开的Activity，用于退出时关闭
	private List<Activity> activityList = new LinkedList<Activity>();
	
	public static BMapApi instance;
	public static BMapApi getInstance() {
		if(null == instance){
			instance = new BMapApi();
		}
		return instance;
	}
	//添加所有用到的activity
	public void addActivity(Activity activity){
		activityList.add(activity);
	}
	//退出时遍历所有activity并关闭
	public void exit(){
		for(Activity activity:activityList){
			activity.finish();
		}
		System.exit(0);
	}
	
	public void setMainHandler(Handler handler) {
		main = handler;
	}

	public void flushMain() {
		main.sendEmptyMessage(FLUSH_MAIN_ACTIVITY);
	}

	/**
	 * Web request cache, one for all activities and orientations
	 */
	private RequestCache mRequestCache;
	// 授权Key
	// TODO: 请输入您的Key,
	// 申请地址：http://dev.baidu.com/wiki/static/imap/key/
	String mStrKey = "BD234DA412395CE51F31BE1F524FA5A7E9728B19";
	boolean m_bKeyRight = true; // 授权Key正确，验证通过
	public static final String BASE_URL = "http://www.hpstreets.com/service/";
	// public static final String BASE_URL =
	// "4FCDA450A91EC3C5D4451DD63766D240F56B6B07E2C70296E2FF6E380CAF87D6E4193625F9823376CA344D1B277E026A";
	public static final String IMAGE_URL = "http://www.hpstreets.com/res/";
	public static final String IURL = "http://www.hpstreets.com";

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	static class MyGeneralListener implements MKGeneralListener {
		@Override
		public void onGetNetworkState(int iError) {
			Toast.makeText(BMapApi.instance.getApplicationContext(),
					ResourceUtil.getString(R.string.error_net),
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == com.baidu.mapapi.MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				Toast.makeText(BMapApi.instance.getApplicationContext(),
						// "请在BMapApiDemoApp.java文件输入正确的授权Key！",
						ResourceUtil.getString(R.string.error_net),
						Toast.LENGTH_LONG).show();
				BMapApi.instance.m_bKeyRight = false;
			}
		}

	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		initEngineManager(this);
		mImageCache = new ImageCache();
		mRequestCache = new RequestCache();
		Caller.setRequestCache(mRequestCache);		
	}
	public void initEngineManager(Context context) {
        if (mBMapMan == null) {
            mBMapMan = new BMapManager(context);
        }

        if (!mBMapMan.init(mStrKey,new MyGeneralListener())) {
            //Toast.makeText(DemoApplication.getInstance().getApplicationContext(), "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
	}
	

	/**
	 * Access to global image cache across Activity instances
	 * 
	 * @return
	 */
	public ImageCache getImageCache() {
		// Log.d("mImageCache",mImageCache.size()+"="+mImageCache.containsKey(IMAGE_URL+"1d072f3e31b54503939fddaa57787681.jpg"));
		// for(int i=0;i<mImageCache.size();i++){
		// Log.d("mImageCache",mImageCache.size()+"="+mImageCache.entrySet()+"="+mImageCache.containsKey("http://116.255.160.161/res/102c179525e342f4828e6d93fba29778.jpg.thumb.jpg"));
		// }

		return mImageCache;
	}

	public String getBaseUrl() {
		String url = "";
		try {
			// url = StringUtils.decrypt("hpstreets",BASE_URL);
			url = BASE_URL;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}

	public String getImageUrl() {
		String url = "";
		try {
			// url = StringUtils.decrypt("hpstreets",IMAGE_URL);
			url = IMAGE_URL;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}

	public String getIUrl() {
		String url = "";
		try {
			// url = StringUtils.decrypt("hpstreets",IMAGE_URL);
			url = IURL;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}

	@Override
	// 建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
	public void onTerminate() {
		// TODO Auto-generated method stub
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onTerminate();
	}

}
