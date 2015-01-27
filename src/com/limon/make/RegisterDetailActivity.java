package com.limon.make;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * 注册协议
 * 
 * @author Administrator
 * 
 */
public class RegisterDetailActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailregist);
		// instance = this;
		mContext = getApplicationContext();

		ImageView btnd = (ImageView) findViewById(R.id.decr);
		btnd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
				unregisterReceiver(exitre);
			}
		});

	}

}