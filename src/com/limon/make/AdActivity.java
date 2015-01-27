package com.limon.make;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ListView;

import com.exchange.View.ExchangeViewManager;

public class AdActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		setContentView(R.layout.adactivity);
		ViewGroup rootLayout = (ViewGroup) findViewById(R.id.rootId);
		// ExchangeViewManager exchangeViewManager = new ExchangeViewManager();
		// exchangeViewManager.addView(this, rootLayout,
		// ExchangeConstants.type_small_handler_list_bottom);
		ListView listView1 = (ListView) this.findViewById(R.id.list1);
		new ExchangeViewManager().addView(this, rootLayout, listView1);
		// ImageView btnd = (ImageView) findViewById(R.id.decr);
		// btnd.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// Intent intent = new Intent();
		// intent.setClass(mContext, MyInfoActivity.class);
		// // mycontext.startActivity(intent);
		// GroupFour.group.getLocalActivityManager().removeAllActivities();
		// View view = GroupFour.group
		// .getLocalActivityManager()
		// .startActivity("Four",
		// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
		// .getDecorView();
		// // Again, replace the view
		// GroupFour.group.replaceView(view);
		// }
		// });
	}
}
