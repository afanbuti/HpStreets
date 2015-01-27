package com.limon.make;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class GroupFour extends ActivityGroup {
	// Keep this in a static variable to make it accessible for all the nesten
	// activities, lets them manipulate the view
	public static GroupFour group;
	// Need to keep track of the history if you want the back-button to work
	// properly, don't use this if your activities requires a lot of memory.
	private ArrayList<View> history;
	// private Intent mMyinfoIntent;
	protected ExitListenerReceiver exitre;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listdoing);
		this.history = new ArrayList<View>();
		group = this;
		// SharedPreferences settings = PreferenceManager
		// .getDefaultSharedPreferences(GroupFour.this);
		// String userId = settings.getString("UserID", "");
		// String accessToken = settings.getString("Token", "");
		// if ("".equals(userId)) {
		// mMyinfoIntent = new Intent(this, LogInActivity.class);
		// } else {
		// mMyinfoIntent = new Intent(this, MyInfoActivity.class);
		// }
		View view = getLocalActivityManager().startActivity(
				"Four",
				new Intent(this, MyInfoActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView();

		replaceView(view);
		RegListener();
	}

	/**
	 * 注册退出事件监听
	 * 
	 */
	public void RegListener() {
		exitre = new ExitListenerReceiver();
		IntentFilter intentfilter = new IntentFilter();
		intentfilter.addAction(this.getPackageName() + "."
				+ "ExitListenerReceiver");
		this.registerReceiver(exitre, intentfilter);
	}

	class ExitListenerReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			((Activity) arg0).finish();
			unregisterReceiver(exitre);
		}
	}

	public void replaceView(View v) {
		// Adds the old one to history
		history.add(v);
		// Changes this Groups View to the new View.
		setContentView(v);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	public void back() {
		if (history.size() > 0) {
			history.remove(history.size() - 1);
			if (history.size() > 0) {
				setContentView((View) history.get(history.size() - 1));
			}
		}
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			close();
			unregisterReceiver(this); // 这句话必须要写要不会报错，不写虽然能关闭，会报一堆错
		}

	};

	private void close() {
		Log.d("Close", "Groupfour");
		Intent intent = new Intent();
		intent.setAction("ExitApp"); // 说明动作
		sendBroadcast(intent);// 该函数用于发送广播
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction("ExitApp");
		registerReceiver(this.broadcastReceiver, filter); // 注册
	}

	@Override
	public void onBackPressed() {
		if (history.size() > 0) {
			GroupFour.group.back();
		}
		return;
	}
}
