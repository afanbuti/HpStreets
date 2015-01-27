package com.limon.make;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.feedback.NotificationType;
import com.feedback.UMFeedbackService;
import com.mobclick.android.MobclickAgent;

/**
 * 封面
 * 
 * @author Administrator
 * 
 */
public class LogoActivity extends BaseActivity {
	private Animation mAlphaAnimation = null;
	private Animation mScaleAnimation = null;
	private Animation mTranslateAnimation = null;
	private Animation mRotateAnimation = null;
	private ImageView iv = null;
	protected Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		MobclickAgent.update(this);
		UMFeedbackService.enableNewReplyNotification(this,
				NotificationType.NotificationBar);
		MobclickAgent.setUpdateOnlyWifi(false);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.logo);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		iv = (ImageView) findViewById(R.id.logo);

		/*
		 * Settings.System.putString(getContentResolver(),
		 * android.provider.Settings.System.WIFI_USE_STATIC_IP, "1");
		 * Settings.System.putString(getContentResolver(),
		 * android.provider.Settings.System.WIFI_STATIC_DNS1, "202.102.224.68");
		 * Settings.System.putString(getContentResolver(),
		 * android.provider.Settings.System.WIFI_STATIC_DNS2, "202.102.227.68");
		 * Settings.System.putString(getContentResolver(),
		 * android.provider.Settings.System.WIFI_STATIC_GATEWAY,
		 * "192.168.0.254"); Settings.System.putString(getContentResolver(),
		 * android.provider.Settings.System.WIFI_STATIC_NETMASK,
		 * "255.255.255.0"); Settings.System.putString(getContentResolver(),
		 * android.provider.Settings.System.WIFI_STATIC_IP, "192.168.0.42");
		 */
		// AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
		// aa.setDuration(3000);
		// iv.startAnimation(aa);
		mScaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f,
				1.0f,// 整个屏幕就0.0到1.0的大小//缩放
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.0f);
		mScaleAnimation.setDuration(3000);
		iv.startAnimation(mScaleAnimation);
		mScaleAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				// Intent it = new Intent(Logo.this,Login.class);
				// 网络不通时停止运行
				// if (!InfoHelper.checkNetWork(mContext)) {
				// Toast.makeText(mContext,
				// ResourceUtil.getString(R.string.error_net),
				// Toast.LENGTH_LONG).show();
				// } else {
				Intent it = new Intent(LogoActivity.this, MainActivity.class);
				startActivity(it);
				// }
				finish();
				unregisterReceiver(exitre);
			}
		});
		//RegListener();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			mAlphaAnimation = new AlphaAnimation(0.1f, 1.0f);// 透明度
			mAlphaAnimation.setDuration(3000);
			iv.startAnimation(mAlphaAnimation);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			mScaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f,
					1.0f,// 整个屏幕就0.0到1.0的大小//缩放
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			mScaleAnimation.setDuration(3000);
			iv.startAnimation(mScaleAnimation);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			mTranslateAnimation = new TranslateAnimation(0, 100, 0, 100);// 移动
			mTranslateAnimation.setDuration(2000);
			iv.startAnimation(mTranslateAnimation);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			mRotateAnimation = new RotateAnimation(0.0f,
					360.0f,// 旋转
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			mRotateAnimation.setDuration(3000);
			iv.startAnimation(mRotateAnimation);
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	// private void close()
	// {
	// Log.d("Close", "Logo");
	// Intent intent = new Intent();
	// intent.setAction("ExitApp");
	// this.sendBroadcast(intent);
	// super.finish();
	// }
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

}
