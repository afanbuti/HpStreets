package com.limon.make;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

public class GroupFirst extends ActivityGroup {
	// Keep this in a static variable to make it accessible for all the nesten
	// activities, lets them manipulate the view
	public static GroupFirst group;
	// Need to keep track of the history if you want the back-button to work
	// properly, don't use this if your activities requires a lot of memory.
	private ArrayList<View> history;
	protected ExitListenerReceiver exitre;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listdoing);
		this.history = new ArrayList<View>();
		group = this;
		View view = getLocalActivityManager().startActivity(
				"First",
				new Intent(this, BlogListActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView();
		// Replace the view of this ActivityGroup
		replaceView(view);
		RegListener();
	}

	public void replaceView(View v) {
		// Adds the old one to history
		history.add(v);
		// Changes this Groups View to the new View.
		setContentView(v);
	}

	public void back() {
		if (history.size() > 0) {
			history.remove(history.size() - 1);
			if (history.size() > 0) {
				setContentView((View) history.get(history.size() - 1));
			}
		}
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

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {

		super.onStop();
	}

	@Override
	public void onBackPressed() {
		if (history.size() > 0) {
			GroupFirst.group.back();
		}
		return;
	}

}
