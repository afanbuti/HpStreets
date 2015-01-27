package com.limon.make;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;

import com.limon.bean.MenuData.DataItem;
import com.limon.common.JsonDataGetApi;
import com.limon.widget.SlideMenuSwitcher;
import com.mobclick.android.MobclickAgent;

/**
 * 勋章列表
 * 
 * @author Administrator
 * 
 */
public class BadgesListActivity extends BaseActivity {
	private SlideMenuSwitcher switcher;
	final private int MSG_TIMER = 0;
	private JsonDataGetApi api = null;
	private JSONObject json = null;
	private List<Map<String, Object>> lInfos = null;
	private String myid = "";
	private ProgressDialog progressDialog = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listbadges);
		mContext = getApplicationContext();
		switcher = (SlideMenuSwitcher) findViewById(R.id.slide_view);
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(BadgesListActivity.this);
		myid = settings.getString("UserID", "");

		findViewById(R.id.button_next).setOnClickListener(
				new OnClickListener() {

					public void onClick(View v) {
						switcher.showNextScreen(); // 点击右边按钮，显示下一屏，当然可以采用手势
					}
				});

		findViewById(R.id.button_prev).setOnClickListener(
				new OnClickListener() {

					public void onClick(View v) {
						switcher.showPreviousScreen(); // 点击左边按钮，显示上一屏，当然可以采用手势

					}
				});
		findViewById(R.id.decr).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, MyInfoActivity.class);
				// mycontext.startActivity(intent);
				GroupFour.group.getLocalActivityManager().removeAllActivities();
				View view = GroupFour.group
						.getLocalActivityManager()
						.startActivity("Four",
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();
				// Again, replace the view
				GroupFour.group.replaceView(view);
			}
		});
		getSomething();

	}

	/** 模拟24个应用程序 */
	private ArrayList<DataItem> makeItems(List<Map<String, Object>> ls) {
		ArrayList<DataItem> items = new ArrayList<DataItem>();
		String[] s = getResources().getStringArray(R.array.badges);
		Drawable drawable = null;
		DataItem item = null;
		String path = "", ba = "";
		for (int k = 0; k < ls.size(); k++) {
			ba += (String) ls.get(k).get("Badges") + ";";
		}
		for (int i = 0; i < 12; i++) {
			// String label = "" + i;
			// Drawable drawable =
			// getResources().getDrawable(R.drawable.img_badge_def);
			item = new DataItem();
			item.dataName = s[i];
			if (ba.contains("" + i)) {
				path = "com/limon/resource/" + i + ".png";
				InputStream is = getClassLoader().getResourceAsStream(path);
				drawable = Drawable.createFromStream(is, "src");
			} else {
				drawable = getResources().getDrawable(R.drawable.img_badge_def);
			}
			// int resID = getResources().getIdentifier("imageName", "drawable",
			// "com.limon.make");
			// drawable = getResources().getDrawable(n);
			item.drawable = drawable;
			items.add(item);
		}

		return items;
	}

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_TIMER:
				// getSomething();
				break;
			}
		}
	};

	private void getSomething() {
		DoingListTask task = new DoingListTask(this);
		task.execute("");
	}

	private void getLocationResult() {
		api = new JsonDataGetApi();
		lInfos = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;

		// 从本地数据库取10定位记录
		JSONArray jobhp = null;
		try {
			jobhp = api.getArray("u.ashx?op=b&id=" + myid);
			// Log.d("getObjectByUrl:jobhp", jobhp.toString());
		} catch (Exception e) {
			MobclickAgent.reportError(mContext, "BadgesListActivity:"
					+ e.toString());
		}
		// GsonBuilder gsonb = new GsonBuilder();
		// DateDeserializer ds = new DateDeserializer();
		// jobhp.registerTypeAdapter(Date.class, ds);
		// 存入取到的本地记录
		try {
			// String msg = "";
			for (int i = 0; i < jobhp.length(); i++) {
				json = (JSONObject) jobhp.opt(i);
				map = new HashMap<String, Object>();

				// map.put("uid", json.getString("Uid"));
				// map.put("Icons", json.getString("Icons"));
				map.put("Badges", json.getString("Badges"));

				lInfos.add(map);
			}
		} catch (Exception e) {
			MobclickAgent.reportError(mContext, "BadgesListActivity:"
					+ e.toString());
		}

	}

	class DoingListTask extends AsyncTask<String, Integer, String> {
		// 可变长的输入参数，与AsyncTask.exucute()对应
		// ProgressDialog pdialog;

		public DoingListTask(Context context) {
			progressDialog = ProgressDialog.show(getParent(), "", mContext
					.getResources().getString(R.string.process), true, false);
		}

		@Override
		protected String doInBackground(String... params) {

			getLocationResult();
			return null;

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			switcher.setData(makeItems(lInfos));
			Message message = Message.obtain();
			message.what = MSG_TIMER;
			handler.sendEmptyMessage(message.what);
		}
	}

}
