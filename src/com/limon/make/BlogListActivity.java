package com.limon.make;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.limon.common.JsonDataGetApi;
import com.limon.common.StringUtils;
import com.limon.widget.BlogsAdapter;
import com.mobclick.android.MobclickAgent;

/**
 * 日志列表
 * 
 * @author Administrator
 * 
 */
public class BlogListActivity extends BaseActivity {

	// private BestLocationListener mLocationListener = null;
	private ListView mListView = null;
	// private Handler mHandler = null;
	// private Runnable updateui = null;
	private Context mContext;
	private ProgressDialog progressDialog = null;
	private BlogsAdapter adapter = null;
	private List<Map<String, Object>> lInfos = null;
	private JsonDataGetApi api = null;
	private JSONObject json = null;
	final private int MSG_TIMER = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listblogs);
		mContext = getApplicationContext();
		mListView = (ListView) findViewById(R.id.listView);
		getSomething();
	}

	@Override
	protected void onResume() {
		super.onResume();

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
		BlogListTask task = new BlogListTask(this);
		task.execute("");
		// progressDialog = ProgressDialog.show(getParent(), "",
		// mContext.getResources()
		// .getString(R.string.process), true, false);
		// final Handler handler = new Handler() {
		// public void handleMessage(Message message) {
		// // JSONArray jsonAr = null;
		// adapter = new BlogsAdapter(mContext, lInfos,
		// R.layout.itemdoing, new String[] { "name", "addr",
		// "dist" }, new int[] { R.id.name, R.id.addr,
		// R.id.dist });
		//
		// mListView.setAdapter(adapter);
		// progressDialog.dismiss();
		//
		// }
		// };
		// new Thread() {
		// @Override
		// public void run() {
		// Looper.prepare();
		// getLocationResult();
		// Message message = new Message();
		// handler.sendMessage(message);
		// Looper.loop();
		// }
		// }.start();
	}

	private void getLocationResult() {
		api = new JsonDataGetApi();
		lInfos = new ArrayList<Map<String, Object>>();
		// JSONObject json = null;
		Map<String, Object> map = null;
		// JSONObject jobj = null;

		// 从本地数据库取10定位记录
		JSONArray jobhp = null;
		try {
			jobhp = api.getArray("b.ashx?op=b&count=10");
			// Log.d("getObjectByUrl:jobhp", jobhp.toString());
		} catch (Exception e) {
			MobclickAgent.reportError(mContext, "BlogList_getSomething:"
					+ e.toString());
		}

		// 存入取到的本地记录
		try {
			String msg = "", message = "";
			for (int i = 0; i < jobhp.length(); i++) {
				json = (JSONObject) jobhp.opt(i);
				map = new HashMap<String, Object>();
				message = StringUtils.filterHtml(json.getString("Content"));
				if (!"".equals(message)) {
					int len = message.length();
					if (len <= 140) {
						msg = message.substring(0, len);
					} else {
						msg = message.substring(0, 140);
					}
				} else {
					continue;
				}
				map.put("title", json.getString("Title"));
				map.put("uid", json.getString("Uid"));
				map.put("name", json.getString("Username"));
				map.put("message", msg);
				map.put("ormessage", message);
				map.put("blogid", json.getString("Blogid"));
				map.put("Pic", json.getString("Pic"));
				map.put("redoc", json.getString("Redoc"));// 评论数
				map.put("dateline", StringUtils.datelineToDateStr(json
						.getString("Datel")));
				map.put("sex", json.getString("Sex"));
				map.put("headimg", json.getString("Headimg"));
				lInfos.add(map);
			}
		} catch (Exception e) {
			MobclickAgent.reportError(mContext, "BlogList_getSomething:"
					+ e.toString());
		}

	}

	class BlogListTask extends AsyncTask<String, Integer, String> {
		// 可变长的输入参数，与AsyncTask.exucute()对应
		// ProgressDialog pdialog;

		public BlogListTask(Context context) {

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
			adapter = new BlogsAdapter(mContext, lInfos, R.layout.itemblog,
					new String[] { "name", "title", "message", "dateline",
							"redoc" }, new int[] { R.id.name, R.id.title,
							R.id.message, R.id.dateline, R.id.redoc });

			mListView.setAdapter(adapter);

			Message message = Message.obtain();
			message.what = MSG_TIMER;
			handler.sendEmptyMessage(message.what);
			// 60秒更新一次列表
			// handler.sendMessageDelayed(message, 60000L);
			// Log.d("==","!!");
		}

		@Override
		protected void onPreExecute() {
			// 任务启动，可以在这里显示一个对话框，这里简单处理
			// message.setText(R.string.task_started);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// 更新进度
			// System.out.println(""+values[0]);
			// message.setText(""+values[0]);
			// pdialog.setProgress(values[0]);
		}

	}
}
