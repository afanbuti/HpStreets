package com.limon.make;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import android.text.Editable;
import android.text.method.TextKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.speech.SynthesizerPlayerListener;
import com.iflytek.ui.SynthesizerDialog;
import com.limon.common.JsonDataGetApi;
import com.limon.common.StringUtils;
import com.mobclick.android.MobclickAgent;

/**
 * 合成页面,调用SDK的SynthesizerDialog实现语音合成.
 * 
 * @author iFlytek
 * @since 20120823
 */
public class TtsActivity extends BaseActivity implements OnClickListener,
		SynthesizerPlayerListener {
	private ProgressDialog progressDialog = null;
	private JsonDataGetApi api = null;
	private JSONObject json = null;
	final private int MSG_TIMER = 0;
	private List<Map<String, Object>> lInfos = null;
	// 合成的文本
	private EditText mSourceText;
	String content = "";
	// 缓存对象.
	private SharedPreferences mSharedPreferences;

	// 合成对象.
	private SynthesizerPlayer mSynthesizerPlayer;

	// 缓冲进度
	private int mPercentForBuffering = 0;

	// 播放进度
	private int mPercentForPlaying = 0;

	// 合成Dialog
	private SynthesizerDialog ttsDialog;
	//private Context mContext;

	/**
	 * 合成界面入口函数
	 * 
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.msctts);
		mContext = getApplicationContext();
		// ((TextView) findViewById(android.R.id.title))
		// .setGravity(Gravity.CENTER);

		Button ttsButton = (Button) findViewById(android.R.id.button1);
		ttsButton.setOnClickListener(this);
		ttsButton.setText(R.string.btnreturn);
		Button settingButton = (Button) findViewById(android.R.id.button2);
		settingButton.setOnClickListener(this);
		settingButton.setText(R.string.btnrand);

		mSourceText = (EditText) findViewById(R.id.txt_result);

		mSourceText.setKeyListener(TextKeyListener.getInstance());

		// 设置EditText的输入方式.
		mSourceText.setInputType(EditorInfo.TYPE_CLASS_TEXT
				| EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE);

		mSharedPreferences = getSharedPreferences(getPackageName(),
				MODE_PRIVATE);

		// mToast = Toast.makeText(this,
		// String.format(getString(R.string.tts_toast_format), 0, 0),
		// Toast.LENGTH_LONG);
		
		// 初始化合成Dialog.
		//ttsDialog = new SynthesizerDialog(TtsActivity.this, "appid="
		//		+ getString(R.string.mscappid));
		Bundle bundle = this.getIntent().getExtras();
		
		if (bundle != null) {
			content = bundle.getString("content");
		}
		if (!(content.equals("") || null == content)) {
			mSourceText.setText(content);
			synthetizeInSilence();
		} else {
			getSomething();
		}
		
	}

	/**
	 * SynthesizerPlayerListener的"停止播放"回调接口.
	 * 
	 * @param
	 */
	@Override
	protected void onStop() {
		// mToast.cancel();
		if (null != mSynthesizerPlayer) {
			mSynthesizerPlayer.cancel();
		}

		super.onStop();
	}

	/**
	 * 按钮点击事件.
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case android.R.id.button1:
			if (!(content.equals("") || null == content)) {
			finish();
			}else{
			Intent intent = new Intent();
			intent.setClass(mContext, MyInfoActivity.class);
			GroupFour.group.getLocalActivityManager().removeAllActivities();
			View view = GroupFour.group
					.getLocalActivityManager()
					.startActivity("Four",
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					.getDecorView();
			GroupFour.group.replaceView(view);
			}
			break;
		// 跳转到合成设置界面.
		case android.R.id.button2:
			getSomething();
			break;
		default:
			break;
		}
	}

	/**
	 * 使用SynthesizerPlayer合成语音，不弹出合成Dialog.
	 * 
	 * @param
	 */
	private void synthetizeInSilence() {
		if (null == mSynthesizerPlayer) {
			// 创建合成对象.
			mSynthesizerPlayer = SynthesizerPlayer.createSynthesizerPlayer(
					this, "appid=" + getString(R.string.mscappid));
		}

		// 设置合成发音人.
		String role = mSharedPreferences.getString(
				getString(R.string.preference_key_tts_role),
				getString(R.string.preference_default_tts_role));
		mSynthesizerPlayer.setVoiceName(role);

		// 设置发音人语速
		int speed = mSharedPreferences.getInt(
				getString(R.string.preference_key_tts_speed), 50);
		mSynthesizerPlayer.setSpeed(speed);

		// 设置音量.
		int volume = mSharedPreferences.getInt(
				getString(R.string.preference_key_tts_volume), 50);
		mSynthesizerPlayer.setVolume(volume);

		// 设置背景音.
		String music = mSharedPreferences.getString(
				getString(R.string.preference_key_tts_music),
				getString(R.string.preference_default_tts_music));
		mSynthesizerPlayer.setBackgroundSound(music);

		// 获取合成文本.
		Editable editable = mSourceText.getText();
		String source = null;
		if (null != editable) {
			source = editable.toString();
		}

		// 进行语音合成.
		mSynthesizerPlayer.playText(source, null, this);
		// mToast.setText(String.format(getString(R.string.tts_toast_format), 0,
		// 0));
		// mToast.show();
	}

	/**
	 * 弹出合成Dialog，进行语音合成
	 * 
	 * @param
	 */
	public void showSynDialog() {

		Editable editable = mSourceText.getText();
		String source = null;
		if (null != editable) {
			source = editable.toString();
		}
		// 设置合成文本.
		ttsDialog.setText(source, null);

		// 设置发音人.
		String role = mSharedPreferences.getString(
				getString(R.string.preference_key_tts_role),
				getString(R.string.preference_default_tts_role));
		ttsDialog.setVoiceName(role);

		// 设置语速.
		int speed = mSharedPreferences.getInt(
				getString(R.string.preference_key_tts_speed), 50);
		ttsDialog.setSpeed(speed);

		// 设置音量.
		int volume = mSharedPreferences.getInt(
				getString(R.string.preference_key_tts_volume), 50);
		ttsDialog.setVolume(volume);

		// 设置背景音.
		String music = mSharedPreferences.getString(
				getString(R.string.preference_key_tts_music),
				getString(R.string.preference_default_tts_music));
		ttsDialog.setBackgroundSound(music);

		// 弹出合成Dialog
		ttsDialog.show();
	}

	/**
	 * SynthesizerPlayerListener的"播放进度"回调接口.
	 * 
	 * @param percent
	 *            ,beginPos,endPos
	 */
	@Override
	public void onBufferPercent(int percent, int beginPos, int endPos) {
		mPercentForBuffering = percent;
		/*
		 * mToast.setText(String.format(getString(R.string.tts_toast_format),
		 * mPercentForBuffering, mPercentForPlaying)); mToast.show();
		 */
	}

	/**
	 * SynthesizerPlayerListener的"开始播放"回调接口.
	 * 
	 * @param
	 */
	@Override
	public void onPlayBegin() {
	}

	/**
	 * SynthesizerPlayerListener的"暂停播放"回调接口.
	 * 
	 * @param
	 */
	@Override
	public void onPlayPaused() {
	}

	/**
	 * SynthesizerPlayerListener的"播放进度"回调接口.
	 * 
	 * @param percent
	 *            ,beginPos,endPos
	 */
	@Override
	public void onPlayPercent(int percent, int beginPos, int endPos) {
		mPercentForPlaying = percent;
		/*
		 * mToast.setText(String.format(getString(R.string.tts_toast_format),
		 * mPercentForBuffering, mPercentForPlaying)); mToast.show();
		 */
	}

	/**
	 * SynthesizerPlayerListener的"恢复播放"回调接口，对应onPlayPaused
	 * 
	 * @param
	 */
	@Override
	public void onPlayResumed() {
	}

	/**
	 * SynthesizerPlayerListener的"结束会话"回调接口.
	 * 
	 * @param error
	 */
	@Override
	public void onEnd(SpeechError error) {
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
			jobhp = api.getArray("b.ashx?op=c&count=1");
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
				map.put("message", msg);
				map.put("blogid", json.getString("Blogid"));
				lInfos.add(map);
			}
		} catch (Exception e) {
			MobclickAgent.reportError(mContext, "DoingList_getLocationResult:"
					+ e.toString());
		}
	}

	class DoingListTask extends AsyncTask<String, Integer, String> {
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
			if (lInfos != null) {
				Iterator<Map<String, Object>> it = lInfos.iterator();
				String v = "";
				if (it != null) {
					// while (it.hasNext()) {
					Map<String, Object> map = it.next();
					Set<String> set = map.keySet();
					Iterator<String> its = set.iterator();
					if (its != null) {
						// String key = its.next();
						// String value = (String)map.get(key);
						v = (String) map.get("message");
						mSourceText.setText(v);

//						boolean show = mSharedPreferences.getBoolean(
//								getString(R.string.preference_key_tts_show),
//								false);
//						if (show) {
//							// 显示合成Dialog
//							showSynDialog();
//						} else { // 不显示Dialog，后台合成语音.
							synthetizeInSilence();
//						}
					}
				}
			}
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
