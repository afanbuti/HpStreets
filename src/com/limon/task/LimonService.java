package com.limon.task;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LimonService extends Service {
	private static final String TAG = "MyService";

	// MediaPlayer player;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {// 重写onStartCommand方法
		// IntentFilter filter = new IntentFilter();//创建IntentFilter对象
		// filter.addAction("com.justel.service");
		// //registerReceiver(cmdReceiver, filter);//注册Broadcast Receiver
		doJob();// 调用方法启动线程，自己来完成
		// return super.onStartCommand(intent, flags, startId);
		return Service.START_STICKY;
	}

	private void doJob() {
		// Object data;//服务器返回的数据data
		Intent intent = new Intent();// 创建Intent对象
		intent.setAction("com.justel.service");
		// intent.putExtra("data", data);
		sendBroadcast(intent);// 发送广播
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "My Service created", Toast.LENGTH_LONG).show();
		Log.i(TAG, "onCreate");

		// player = MediaPlayer.create(this, R.raw.braincandy);
		// player.setLooping(false);
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "My Service Stoped", Toast.LENGTH_LONG).show();
		Log.i(TAG, "onDestroy");
		// player.stop();
	}

	@Override
	public void onStart(Intent intent, int startid) {
		Toast.makeText(this, "My Service Start", Toast.LENGTH_LONG).show();
		Log.i(TAG, "onStart");
		// player.start();
	}
	/**
	 * Show a notification while this service is running.
	 */
	/*
	 * private void showNotification() { // In this sample, we'll use the same
	 * text for the ticker and the expanded notification CharSequence text =
	 * getText(R.string.local_service_started);
	 * 
	 * // Set the icon, scrolling text and timestamp Notification notification =
	 * new Notification(R.drawable.stat_sample, text,
	 * System.currentTimeMillis());
	 * 
	 * // The PendingIntent to launch our activity if the user selects this
	 * notification PendingIntent contentIntent =
	 * PendingIntent.getActivity(this, 0, new Intent(this,
	 * LocalServiceController.class), 0);
	 * 
	 * // Set the info for the views that show in the notification panel.
	 * notification.setLatestEventInfo(this,
	 * getText(R.string.local_service_label), text, contentIntent);
	 * 
	 * // Send the notification. // We use a layout id because it is a unique
	 * number. We use it later to cancel.
	 * mNM.notify(R.string.local_service_started, notification); }
	 */
}