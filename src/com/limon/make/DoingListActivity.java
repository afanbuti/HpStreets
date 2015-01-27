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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.limon.common.JsonDataGetApi;
import com.limon.common.StringUtils;
import com.limon.widget.SpecialAdapter;
import com.mobclick.android.MobclickAgent;

/**
 * 新鲜事列表
 * 
 * @author Administrator
 * 
 */
public class DoingListActivity extends BaseActivity {
	private ListView mListView = null;
	private Context mContext;
	private ProgressDialog progressDialog = null;
	private SpecialAdapter adapter = null;
	private List<Map<String, Object>> lInfos = null;
	private JsonDataGetApi api = null;
	private JSONObject json = null;
	final private int MSG_TIMER = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listdoing);
		mContext = getApplicationContext();
		mListView = (ListView) findViewById(R.id.listView);

		ImageView btn = (ImageView) findViewById(R.id.incr);
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// bar.incrementProgressBy(incrAmount);
				Intent intent = new Intent();
				intent.setClass(mContext, DoingSubmitActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
			}
		});
		TextView btnt = (TextView) findViewById(R.id.incrt);
		btnt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// bar.incrementProgressBy(incrAmount);
				Intent intent = new Intent();
				intent.setClass(mContext, DoingSubmitActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
			}
		});
		ImageView btnr = (ImageView) findViewById(R.id.reflash);
		btnr.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				BMapApi appC = (BMapApi) getApplication();
				appC.flushMain();
			}
		});
		TextView btnrt = (TextView) findViewById(R.id.reflasht);
		btnrt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				BMapApi appC = (BMapApi) getApplication();
				appC.flushMain();
			}
		});
		ImageView btnd = (ImageView) findViewById(R.id.decr);
		btnd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, BlogListActivity.class);
				// mycontext.startActivity(intent);
				View view = GroupFirst.group
						.getLocalActivityManager()
						.startActivity("First",
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();
				// Again, replace the view
				GroupFirst.group.replaceView(view);
			}
		});
		TextView btndt = (TextView) findViewById(R.id.decrt);
		btndt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, BlogListActivity.class);
				// mycontext.startActivity(intent);
				View view = GroupFirst.group
						.getLocalActivityManager()
						.startActivity("First",
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();
				// Again, replace the view
				GroupFirst.group.replaceView(view);
			}
		});
		getSomething();

		BMapApi appC = (BMapApi) getApplication();
		appC.setMainHandler(flushHandler);
	}

	@Override
	protected void onResume() {
		super.onResume();// getSomething();
		// mListView = (ListView) findViewById(R.id.listView);
		// Log.d("totalMemory", ""+Runtime.getRuntime().totalMemory());
		// LoadView(this, mListView,lInfos);.
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
	Handler flushHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BMapApi.FLUSH_MAIN_ACTIVITY:
				getSomething();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
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
			jobhp = api.getArray("b.ashx?op=e&count=15");
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
				// Log.d("jj",json.getString("Redoc"));
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
			adapter = new SpecialAdapter(mContext, lInfos, R.layout.itemdoing,
					new String[] { "name", "message", "dateline", "redoc" },
					new int[] { R.id.name, R.id.message, R.id.dateline,
							R.id.redoc });

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
