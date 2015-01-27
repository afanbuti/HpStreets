package com.limon.make;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.exchange.Public.ExchangeConstants;
import com.exchange.View.ExchangeViewManager;

/**
 * 关于
 * 
 * @author Administrator
 * 
 */
public class AboutActivity extends BaseActivity {

	private TextView pointView = null;
	protected Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		setContentView(R.layout.profileabout);

		pointView = (TextView) findViewById(R.id.user_details_points);
		// DisplayMetrics dm = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(dm);
		// String strPM = " 屏幕分辨率:" + dm.widthPixels+"* "+dm.heightPixels;
		PackageInfo info;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
			pointView.setText(mContext.getResources().getString(
					R.string.info_ver)
					+ info.versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ImageView btnd = (ImageView) findViewById(R.id.decr);
		btnd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
				unregisterReceiver(exitre);
			}
		});
		ViewGroup rootLayout = (ViewGroup) findViewById(R.id.rootId);
		ExchangeViewManager exchangeViewManager = new ExchangeViewManager();
		// exchangeViewManager.addView(this,
		// rootLayout,ExchangeConstants.type_small_handler_list_bottom);
		exchangeViewManager.addView(this, rootLayout,
				ExchangeConstants.type_standalone_handler);

	}

}
