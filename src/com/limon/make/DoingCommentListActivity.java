package com.limon.make;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.limon.common.JsonDataGetApi;
import com.limon.common.StringUtils;
import com.limon.widget.SpecialCommentAdapter;
import com.mobclick.android.MobclickAgent;

/**
 * 新鲜事评论列表
 * 
 * @author Administrator
 * 
 */
public class DoingCommentListActivity extends BaseActivity {
	private ListView mListView = null;
	private ProgressDialog progressDialog = null;
	private SpecialCommentAdapter adapter = null;
	private List<Map<String, Object>> lInfos = null;
	private JsonDataGetApi api = null;
	private JSONObject json = null;
	private String blogid = "", name = "", message = "", dateline = "";
	private TextView textViewName = null, textViewMessage = null,
			textViewDateline = null;
	// private String uname = "", userId = "";
	private ImageView shut;
	private ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commentdoinglist);
		mContext = getApplicationContext();
		mListView = (ListView) findViewById(R.id.listView);
		textViewName = (TextView) findViewById(R.id.name);
		textViewMessage = (TextView) findViewById(R.id.message);
		textViewDateline = (TextView) findViewById(R.id.dateline);
		blogid = getIntent().getExtras().getString("blogid");
		name = getIntent().getExtras().getString("name");
		message = getIntent().getExtras().getString("message");
		dateline = getIntent().getExtras().getString("dateline");
		// Log.d("blogid", blogid);
		// Log.d("dateline", dateline);
		// Log.d("message", message);
		// Log.d("name", name);
		textViewName.setText(name);
		textViewMessage.setText(message);
		textViewDateline.setText(dateline);
		// SharedPreferences settings = PreferenceManager
		// .getDefaultSharedPreferences(DoingCommentListActivity.this);
		// userId = settings.getString("UserID", "");
		// uname = settings.getString("UserName", "");

		shut = (ImageView) findViewById(R.id.incr);
		shut.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, DoingCommentActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("blogid", blogid);
				intent.putExtra("name", name);
				intent.putExtra("message", message);
				intent.putExtra("dateline", dateline);
				mContext.startActivity(intent);
			}
		});
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
				unregisterReceiver(exitre);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		getSomething();
	}

	private void getSomething() {
		progressDialog = ProgressDialog.show(this, "", mContext.getResources()
				.getString(R.string.process), true, false);
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				// JSONArray jsonAr = null;
				adapter = new SpecialCommentAdapter(mContext, lInfos,
						R.layout.itemdoing, new String[] { "name", "message",
								"dateline", "redoc" }, new int[] { R.id.name,
								R.id.message, R.id.dateline, R.id.redoc });

				mListView.setAdapter(adapter);
				// mListView.setOnItemClickListener(new OnItemClickListener() {
				// @Override
				// public void onItemClick(AdapterView<?> arg0, View v, int
				// index,
				// long arg3) {
				// }
				// });
				progressDialog.dismiss();

			}
		};
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				getLocationResult();
				Message message = new Message();
				handler.sendMessage(message);
				Looper.loop();
			}
		}.start();
	}

	private void getLocationResult() {
		api = new JsonDataGetApi();
		lInfos = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;

		// 从本地数据库取10定位记录
		JSONArray jobhp = null;
		try {
			jobhp = api.getArray("b.ashx?op=d&count=15&blogid=" + blogid);
			// Log.d("getObjectByUrl:jobhp", jobhp.toString());
		} catch (Exception e) {
			MobclickAgent.reportError(mContext,
					"DoingCommentList_getLocationResult:" + e.toString());
		}
		// GsonBuilder gsonb = new GsonBuilder();
		// DateDeserializer ds = new DateDeserializer();
		// jobhp.registerTypeAdapter(Date.class, ds);
		// 存入取到的本地记录
		try {
			for (int i = 0; i < jobhp.length(); i++) {
				json = (JSONObject) jobhp.opt(i);
				map = new HashMap<String, Object>();
				map.put("name", json.getString("Username"));
				map.put("message", json.getString("Content"));
				// map.put("Pic", json.getString("Pic"));
				map.put("redoc", "0");// 评论数
				map.put("dateline", StringUtils.datelineToDateStr(json
						.getString("Datel")));
				map.put("sex", json.getString("Sex"));
				map.put("headimg", json.getString("Headimg"));
				lInfos.add(map);
			}
		} catch (Exception e) {
			MobclickAgent.reportError(mContext,
					"DoingCommentList_getLocationResult:" + e.toString());
		}

	}
}