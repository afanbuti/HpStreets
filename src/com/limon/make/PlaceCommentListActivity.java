package com.limon.make;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.limon.common.JsonDataGetApi;
import com.limon.common.ResourceUtil;
import com.limon.common.StringUtils;
import com.limon.widget.SpecialCommentAdapter;
import com.mobclick.android.MobclickAgent;

/**
 * �ص�ǩ��+�����б�
 * 
 * @author Administrator
 * 
 */
public class PlaceCommentListActivity extends BaseActivity {
	private ListView mListView = null;
	private ProgressDialog progressDialog = null;
	private SpecialCommentAdapter adapter = null;
	private List<Map<String, Object>> lInfos = null;
	private JsonDataGetApi api = null;
	private JSONObject json = null;
	private TextView textViewName = null;
	// , textViewMessage = null, textViewDateline = null;
	private String guid = "", name = "", addr = "", tel = "", cate = "";
	private String uname = "", userId = "";
	private Button btnRef;
	private ImageView shut;
	private ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commentplacelist);
		mContext = getApplicationContext();
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(PlaceCommentListActivity.this);
		userId = settings.getString("UserID", "");
		uname = settings.getString("UserName", "");
		mListView = (ListView) findViewById(R.id.listView);
		textViewName = (TextView) findViewById(R.id.name);
		guid = getIntent().getExtras().getString("guid");
		if (getIntent().getExtras().getString("name") != null)
			name = getIntent().getExtras().getString("name");
		// if(getIntent().getExtras().getString("addr")!=null)
		// addr = getIntent().getExtras().getString("addr");
		// if(getIntent().getExtras().getString("tel")!=null)
		// tel = getIntent().getExtras().getString("tel");
		// if(getIntent().getExtras().getString("cate")!=null)
		// cate = getIntent().getExtras().getString("cate");
		// Log.d("guid", guid);
		// Log.d("addr", addr);
		// Log.d("tel", tel);
		// Log.d("cate", cate);
		// Log.d("name", name);
		textViewName.setText(name);
		// textViewMessage.setText(addr);
		// textViewDateline.setText(tel);

		btnRef = (Button) findViewById(R.id.SignUpButton);
		btnRef.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ("".equals(userId)) {
					Toast.makeText(mContext,
							ResourceUtil.getString(R.string.info_nologin),
							Toast.LENGTH_LONG).show();
					startActivity(new Intent(PlaceCommentListActivity.this,
							LogInActivity.class));
					finish();
					unregisterReceiver(exitre);
				} else {
					if (!"".equals(guid)) {
						checkIns();
					} else {
						Toast.makeText(
								mContext,
								ResourceUtil
										.getString(R.string.checkin_nolocal),
								Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		shut = (ImageView) findViewById(R.id.incr);
		shut.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, PlaceCommentActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("guid", guid);
				intent.putExtra("name", name);
				intent.putExtra("addr", addr);
				intent.putExtra("tel", tel);
				intent.putExtra("cate", cate);
				mContext.startActivity(intent);
			}
		});
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// finish();
				// unregisterReceiver(exitre);
				Intent intent = new Intent();
				intent.setClass(mContext, MyTextLocatActivity.class);
				// mycontext.startActivity(intent);
				GroupThree.group.getLocalActivityManager()
						.removeAllActivities();
				View view = GroupThree.group
						.getLocalActivityManager()
						.startActivity("Three",
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();
				// Again, replace the view
				GroupThree.group.replaceView(view);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		getSomething();
	}

	private void checkIns() {
		progressDialog = ProgressDialog.show(this, "", ResourceUtil
				.getString(R.string.process), true, false);
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				progressDialog.dismiss();
				Toast
						.makeText(
								mContext,
								ResourceUtil.getString(R.string.checkin_ok)
										+ String
												.format(
														"\n"
																+ ResourceUtil
																		.getString(R.string.info_credit),
														"10", "10"),
								Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.setClass(PlaceCommentListActivity.this,
						BlogSubmitActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("bname", name);
				startActivity(intent);
			}
		};
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				api = new JsonDataGetApi();
				try {
					api.getString(String.format(
							"l.ashx?op=c&id=%s&name=%s&guid=%s&pname=%s",
							userId, StringUtils.encode(uname), guid,
							StringUtils.encode(name)));
				} catch (JSONException e) {
					Log.d("checkIns1:", e.toString());
				} catch (Exception e) {
					Log.d("checkIns2:", e.toString());
				}
				SharedPreferences sharedPreferences = PreferenceManager
						.getDefaultSharedPreferences(PlaceCommentListActivity.this);
				Editor editor = sharedPreferences.edit();
				editor.putInt("Credit",
						sharedPreferences.getInt("Credit", 0) + 10);
				editor.putInt("Experience", sharedPreferences.getInt(
						"Experience", 0) + 10);
				editor.commit();
				Message message = new Message();
				handler.sendMessage(message);
				Looper.loop();
			}
		}.start();

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

		// �ӱ�����ݿ�ȡ10��λ��¼
		JSONArray jobhp = null;
		try {
			jobhp = api.getArray("b.ashx?op=pc&count=20&guid=" + guid);
			// Log.d("getObjectByUrl:jobhp", jobhp.toString());
		} catch (Exception e) {
			MobclickAgent.reportError(mContext,
					"PlaceCommentList_getLocationResult:" + e.toString());
			// Log.d("getLocationResult:jobhp", e.toString());
		}
		// GsonBuilder gsonb = new GsonBuilder();
		// DateDeserializer ds = new DateDeserializer();
		// jobhp.registerTypeAdapter(Date.class, ds);
		// ����ȡ���ı��ؼ�¼
		try {
			for (int i = 0; i < jobhp.length(); i++) {
				json = (JSONObject) jobhp.opt(i);
				map = new HashMap<String, Object>();
				map.put("name", json.getString("Username"));
				map.put("message", json.getString("Content"));
				// map.put("Pic", json.getString("Pic"));
				map.put("redoc", "0");// ������
				map.put("dateline", StringUtils.datelineToDateStr(json
						.getString("Datel")));
				map.put("sex", json.getString("Sex"));
				map.put("headimg", json.getString("Headimg"));
				lInfos.add(map);
			}
		} catch (Exception e) {
			MobclickAgent.reportError(mContext, "PlaceCommentList_lInfos.add:"
					+ e.toString());
		}

	}
}
