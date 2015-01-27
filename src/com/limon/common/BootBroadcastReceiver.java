package com.limon.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.limon.make.LogoActivity;

public class BootBroadcastReceiver extends BroadcastReceiver {

	static final String ACTION = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(ACTION)) {
			Intent sayHelloIntent = new Intent(context, LogoActivity.class);
			sayHelloIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			context.startActivity(sayHelloIntent);
		}
	}

}