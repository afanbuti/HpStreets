package com.limon.make;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.limon.bean.FriendInfo;
import com.limon.common.JsonDataGetApi;
import com.limon.widget.JoinFriendAdapater;
import com.mobclick.android.MobclickAgent;

/**
 * 好友请求列表
 * 
 * @author Administrator
 * 
 */
public class MyJoinFriendsActivity extends BaseActivity {
	private String ispublic, headurl;
	private String uname, uid;
	private int sex;
	private ProgressDialog progressDialog = null;
	private ListView msgList;
	private JoinFriendAdapater adapater;
	private String userId = "";
	private List<FriendInfo> userInfoList;
	private List<String> listTag = null;
	final private int MSG_TIMER = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listjoinfriend);
		mContext = getApplicationContext();

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MyJoinFriendsActivity.this);
		userId = settings.getString("UserID", "");

		getMyFriendsThread();

	}

	private List<String> myListTage() {
		listTag = new ArrayList<String>();
		listTag.add(mContext.getResources().getString(R.string.info_myfriends));
		// listTag.add(mContext.getResources().getString(R.string.info_contact));
		// listTag.add(mContext.getResources().getString(R.string.info_sina));
		// listTag.add(mContext.getResources().getString(R.string.info_renren));
		// listTag.add(mContext.getResources().getString(R.string.info_tenc));
		return listTag;
	}

	/**
	 * 载入我的好友请求列表
	 * 
	 */
	public void loadList() {
		JsonDataGetApi api = new JsonDataGetApi();
		JSONArray jobhp = null;
		try {
			jobhp = api.getArray(String.format("u.ashx?op=q&id=%s", userId));
			// Log.d("MyTextFriends:", jobhp.toString());
		} catch (Exception e) {
			MobclickAgent.reportError(mContext, "MyJoinFriends_loadList:"
					+ e.toString());
		}
		JSONObject json = null;
		try {
			if (jobhp != null) {
				if (userInfoList == null) {
					userInfoList = new ArrayList<FriendInfo>();
				}
				FriendInfo u = new FriendInfo();
				// u.setName(mContext.getResources().getString(
				// R.string.info_myfriends));
				// userInfoList.add(u);
				// String headurl =
				// "http://afanbutiphp.dns87.53nic.com/res/medal4.png";
				for (int i = 0; i < jobhp.length(); i++) {
					json = (JSONObject) jobhp.opt(i);
					uid = json.getString("Uid");
					uname = json.getString("Username");
					ispublic = json.getString("Ispublic");
					sex = json.getInt("Sex");
					headurl = json.getString("Headimg");
					u = new FriendInfo();
					u.setId(uid);
					u.setName(uname);
					u.setSex(sex);
					u.setHeadImg(headurl);
					u.setIspublic(ispublic);
					userInfoList.add(u);
				}
			}
		} catch (JSONException e) {
			MobclickAgent.reportError(mContext,
					"MyJoinFriends_userInfoList.add:" + e.toString());
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
			loadList();
			return null;

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			// adapter = new BlogsAdapter(mContext, lInfos, R.layout.itemblog,
			// new String[] { "name","title", "message", "dateline", "redoc" },
			// new int[] { R.id.name,R.id.title, R.id.message, R.id.dateline,
			// R.id.redoc });

			// mListView.setAdapter(adapter);

			if (userInfoList != null) {
				adapater = new JoinFriendAdapater(mContext, myListTage(),
						userInfoList);
				msgList = (ListView) findViewById(R.id.msglist);

				// 当每一条微博消息被点击时的响应
				msgList.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View view,
							int position, long id) {
						// Object obj = view.getTag();
					}

				});

				msgList.setAdapter(adapater);

			} else {
				Toast.makeText(
						mContext,
						mContext.getResources().getString(
								R.string.info_nojoinus), Toast.LENGTH_LONG)
						.show();
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
