package com.limon.make;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.widget.ListView;

import com.limon.common.JsonDataGetApi;
import com.limon.common.StringUtils;
import com.limon.widget.QuietAdapter;
import com.mobclick.android.MobclickAgent;

/**
 * 接收悄悄话列表
 * 
 * @author Administrator
 * 
 */
public class MyQuietActivity extends BaseActivity {
	private ListView mListView = null;
	private Context mContext;
	private ProgressDialog progressDialog = null;
	private QuietAdapter adapter = null;
	private List<Map<String, Object>> lInfos = null;
	private JsonDataGetApi api = null;
	private JSONObject json = null;
	final private int MSG_TIMER = 0;
	private String myid = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listquiet);
		mContext = getApplicationContext();
		mListView = (ListView) findViewById(R.id.listView);
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MyQuietActivity.this);
		myid = settings.getString("UserID", "");
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
			jobhp = api.getArray("b.ashx?op=ql&id=" + myid + "&count=15");
			// Log.d("getObjectByUrl:jobhp", jobhp.toString());
		} catch (Exception e) {
			MobclickAgent.reportError(mContext, "MyQuiet_getLocationResult:"
					+ e.toString());
		}
		// GsonBuilder gsonb = new GsonBuilder();
		// DateDeserializer ds = new DateDeserializer();
		// jobhp.registerTypeAdapter(Date.class, ds);
		// 存入取到的本地记录
		try {
			String msg = "";
			for (int i = 0; i < jobhp.length(); i++) {
				json = (JSONObject) jobhp.opt(i);
				map = new HashMap<String, Object>();
				msg = StringUtils.filterHtml(json.getString("Content"));
				if (!"".equals(msg)) {
				} else {
					continue;
				}
				map.put("uid", json.getString("Uid"));
				map.put("name", json.getString("Username"));
				map.put("message", msg);
				map.put("blogid", json.getString("Blogid"));
				map.put("Pic", "");
				map.put("redoc", json.getString("Redoc"));
				map.put("dateline", StringUtils.datelineToDateStr(json
						.getString("Datel")));
				map.put("sex", json.getString("Sex"));
				map.put("headimg", json.getString("Headimg"));
				lInfos.add(map);
			}
		} catch (Exception e) {
			MobclickAgent.reportError(mContext, "MyQuiet_getLocationResult:"
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
			adapter = new QuietAdapter(mContext, lInfos, R.layout.itemdoing,
					new String[] { "name", "message", "dateline", "redoc" },
					new int[] { R.id.name, R.id.message, R.id.dateline,
							R.id.redoc });

			mListView.setAdapter(adapter);

			Message message = Message.obtain();
			message.what = MSG_TIMER;
			handler.sendEmptyMessage(message.what);
		}
	}
}
