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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.limon.common.JsonDataGetApi;
import com.limon.common.StringUtils;
import com.limon.widget.MyContentAdapter;
import com.mobclick.android.MobclickAgent;

/**
 * 内容库列表
 * 
 * @author Administrator
 * 
 */
public class MyContentListActivity extends BaseActivity {
	private ListView mListView = null;
	private Context mContext;
	private ProgressDialog progressDialog = null;
	private MyContentAdapter adapter = null;
	private List<Map<String, Object>> lInfos = null;
	private JsonDataGetApi api = null;
	private JSONObject json = null;
	final private int MSG_TIMER = 0;
	private Button btnRef, btnRand;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylistcontent);
		mContext = getApplicationContext();
		mListView = (ListView) findViewById(R.id.listView);
		// bctn = (Button) findViewById(R.id.BytimeButton);
		btnRef = (Button) findViewById(R.id.RefButton);
		btnRand = (Button) findViewById(R.id.RandButton);
		getSomething();
		// 返回
		btnRef.setOnClickListener(new OnClickListener() {
			@Override
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
		// 随机访问
		btnRand.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getSomething();
			}
		});
		// 定时发布
		// bctn.setOnClickListener(new View.OnClickListener() {
		// @SuppressWarnings("unchecked")
		// public void onClick(View v) {
		// HashMap<String, String> state = adapter.state;
		// if(!(state==null || state.size()==0)){
		//
		// // BlogInfo u = new BlogInfo();
		// // getInfoList = new ArrayList<BlogInfo>();
		// // Iterator iter = state.entrySet().iterator();
		// // Map.Entry entry;
		// // while (iter.hasNext()) {
		// // entry = (Map.Entry) iter.next();
		// // u = new BlogInfo();
		// // u.setBlogid((String)entry.getKey());
		// // u.setContent("");
		// // getInfoList.add(u);
		// // }
		// // GsonBuilder gsonb = new GsonBuilder();
		// // Gson gson = gsonb.create();
		// // String sJson = gson.toJson(getInfoList);
		// // //Log.d("sJson1:", sJson);
		// // sJson = StringUtils.encode(sJson);
		// // delMyDoingThread(sJson);
		// }else{
		// Toast.makeText(mContext,
		// mContext.getResources().getString(R.string.info_nosms),
		// Toast.LENGTH_LONG).show();
		// }
		// adapter.state.clear();
		// }
		// });
	}

	@Override
	protected void onResume() {
		super.onResume();// getSomething();
		// mListView = (ListView) findViewById(R.id.listView);
		// Log.d("do", ""+lInfos.isEmpty());
		// LoadView(this, mListView,lInfos);
	}

	// public static void LoadView(Context c,ListView listView,List<Map<String,
	// Object>> lInfos) {
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("uid", "Uid");
	// map.put("name", "Username");
	// map.put("message", "msg");
	// map.put("blogid", "Blogid");
	// map.put("Pic", "");
	// map.put("redoc", "0");// 评论数
	// map.put("dateline","Datel");
	// map.put("sex", "1");
	// map.put("headimg", "");
	// lInfos.add(map);
	// //adapter.notifyDataSetChanged();
	// SpecialAdapter adapter = new SpecialAdapter(c, lInfos,
	// R.layout.itemdoing,
	// new String[] { "name", "message", "dateline", "redoc" },
	// new int[] { R.id.name, R.id.message, R.id.dateline,
	// R.id.redoc });
	// listView.setAdapter(adapter);
	// }
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
			jobhp = api.getArray("b.ashx?op=c&count=10");
			// Log.d("getObjectByUrl:jobhp", jobhp.toString());
		} catch (Exception e) {
			MobclickAgent.reportError(mContext, "DoingList_getLocationResult:"
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
				// map.put("uid", json.getString("Uid"));
				// map.put("name", json.getString("Username"));
				map.put("message", msg);
				map.put("blogid", json.getString("Blogid"));
				// map.put("Pic", json.getString("Pic"));
				// map.put("redoc", json.getString("Redoc"));// 评论数
				// map.put("dateline", StringUtils.datelineToDateStr(json
				// .getString("Datel")));
				// map.put("sex", json.getString("Sex"));
				// map.put("headimg", json.getString("Headimg"));
				lInfos.add(map);
			}
		} catch (Exception e) {
			MobclickAgent.reportError(mContext, "DoingList_getLocationResult:"
					+ e.toString());
		}

	}

	@Override
	protected void onStart() {// 重写onStart方法
		// DataReceiver dataReceiver = new DataReceiver();
		// IntentFilter filter = new IntentFilter();// 创建IntentFilter对象
		// filter.addAction("com.limon.task.LimonService");
		// registerReceiver(dataReceiver, filter);// 注册Broadcast Receiver
		super.onStart();
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
			adapter = new MyContentAdapter(mContext, lInfos,
					R.layout.myitemcontent, new String[] { "message" },
					new int[] { R.id.message });

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
