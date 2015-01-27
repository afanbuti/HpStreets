package com.limon.make;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.limon.common.JsonDataGetApi;
import com.limon.common.ResourceUtil;
import com.limon.common.StringUtils;
import com.limon.widget.SpecialAdapter;
import com.mobclick.android.MobclickAgent;

/**
 * 列表版好友动态
 * 
 * @author Administrator
 * 
 */
public class MyTextFriendsActivity extends BaseActivity {

	private ProgressDialog progressDialog = null;
	private SpecialAdapter adapater;
	private ListView msgList;;
	private String userId = "";
	final private int MSG_TIMER = 0;
	private JsonDataGetApi api = null;
	private JSONObject json = null;
	private List<Map<String, Object>> lInfos = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listfriends);
		mContext = getApplicationContext();
		msgList = (ListView) findViewById(R.id.msglist);
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MyTextFriendsActivity.this);
		userId = settings.getString("UserID", "");
		if ("".equals(userId)) {
			Toast.makeText(mContext,
					ResourceUtil.getString(R.string.info_nologin),
					Toast.LENGTH_LONG).show();
			Intent intent = new Intent();
			intent.setClass(mContext, NoLoginFriendsActivity.class);
			GroupTwo.group.getLocalActivityManager().removeAllActivities();
			View view = GroupTwo.group.getLocalActivityManager().startActivity(
					"Two", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					.getDecorView();
			GroupTwo.group.replaceView(view);
		}
		// if ("".equals(userId)) {
		// Toast.makeText(mContext,
		// ResourceUtil.getString(R.string.info_nologin),
		// Toast.LENGTH_LONG).show();
		// startActivity(new Intent(MyTextFriendsActivity.this,
		// LogInActivity.class));
		// finish();
		// }
		getMyFriendsThread();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	private void getLocationResult() {
		api = new JsonDataGetApi();
		lInfos = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;

		// 从本地数据库取10定位记录
		JSONArray jobhp = null;
		try {
			jobhp = api.getArray("b.ashx?op=a&id=" + userId + "&count=15");
			// Log.d("getObjectByUrl:jobhp", jobhp.toString());
		} catch (Exception e) {
			MobclickAgent.reportError(mContext,
					"MyTextFriends_getLocationResult:" + e.toString());
			// Log.d("MyTextFriendsActivity:", e.toString());
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
				map.put("Pic", json.getString("Pic"));
				map.put("fpic", json.getString("Fpic"));
				map.put("redoc", json.getString("Redoc"));// 评论数
				map.put("dateline", StringUtils.datelineToDateStr(json
						.getString("Datel")));
				map.put("sex", json.getString("Sex"));
				map.put("headimg", json.getString("Headimg"));
				lInfos.add(map);
			}
		} catch (Exception e) {
			MobclickAgent.reportError(mContext, "MyTextFriends_lInfos.add:"
					+ e.toString());
		}

	}

	/**
	 * 请求好友列表的方法
	 */
	private void getMyFriendsThread() {
		FriendListTask task = new FriendListTask(this);
		task.execute("");
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

	class FriendListTask extends AsyncTask<String, Integer, String> {
		// 可变长的输入参数，与AsyncTask.exucute()对应
		// ProgressDialog pdialog;

		public FriendListTask(Context context) {

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

			if (lInfos != null) {
				adapater = new SpecialAdapter(mContext, lInfos,
						R.layout.itemdoing, new String[] { "name", "message",
								"dateline", "redoc" }, new int[] { R.id.name,
								R.id.message, R.id.dateline, R.id.redoc });

				msgList.setAdapter(adapater);

			} else {
				Toast.makeText(
						mContext,
						mContext.getResources().getString(
								R.string.info_nofriends), Toast.LENGTH_LONG)
						.show();
				startActivity(new Intent(MyTextFriendsActivity.this,
						MyContactFriendsActivity.class));
			}
			Message message = Message.obtain();
			message.what = MSG_TIMER;
			handler.sendEmptyMessage(message.what);
			// 60秒更新一次列表
			// handler.sendMessageDelayed(message, 60000L);
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
