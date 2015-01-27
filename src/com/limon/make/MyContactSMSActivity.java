package com.limon.make;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobclick.android.MobclickAgent;

public class MyContactSMSActivity extends BaseActivity {

	private EditText contText;
	private Button sendBtn, canBtn;
	private ProgressDialog progressDialog = null;
	final private int MSG_TIMER = 0;
	private HashMap<String, String> state = null;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submitsms);
		mContext = getApplicationContext();

		// 根据id找到控件并实例化它
		// mobileText = (EditText)findViewById(R.id.MobileEditText);
		contText = (EditText) findViewById(R.id.share_content);
		sendBtn = (Button) findViewById(R.id.SignUpButton);
		canBtn = (Button) findViewById(R.id.ReturnButton);
		// 发送按钮的处理事件
		sendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setMyContactThread();
				// setMyContactThread();

			}
		});
		canBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				unregisterReceiver(exitre);
			}
		});
		state = (HashMap<String, String>) getIntent().getExtras()
				.getSerializable("mobile");

	}

	private void setMyContactThread() {
		ContactInfoTask task = new ContactInfoTask(this);
		task.execute("");
	}

	private void getContactInfo() {
		String content = contText.getText().toString();
		String mobile = "";
		if (state != null && state.size() > 0) {
			Iterator<?> iter = state.entrySet().iterator();
			Map.Entry entry;
			// Object key,val;
			SmsManager smsManager = SmsManager.getDefault();
			while (iter.hasNext()) {
				entry = (Map.Entry) iter.next();
				// key = entry.getKey();
				mobile = (String) entry.getValue();
				// Log.d("mobile","mobile"+mobile);
				try {
					if (content.length() > 70) {
						// 使用短信管理器进行短信内容的分段，返回分成的段
						ArrayList<String> contents = smsManager
								.divideMessage(content);
						for (String msg : contents) {
							// 使用短信管理器发送短信内容
							// 参数一为短信接收者
							// 参数三为短信内容
							// 其他可以设为null
							// Log.d("mobile1", "="+mobile);
							smsManager.sendTextMessage(mobile, null, msg, null,
									null);
						}
						// 否则一次过发送
					} else {
						// Log.d("mobile2", "="+mobile);
						smsManager.sendTextMessage(mobile, null, content, null,
								null);
					}
				} catch (Exception e) {
					MobclickAgent.reportError(mContext,
							"MyContactSMS_getContactInfo:" + e.toString());
				}
			}
		}

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

	class ContactInfoTask extends AsyncTask<String, Integer, String> {
		// 可变长的输入参数，与AsyncTask.exucute()对应
		// ProgressDialog pdialog;

		public ContactInfoTask(Context context) {

			progressDialog = ProgressDialog.show(getParent(), "", mContext
					.getResources().getString(R.string.process), true, false);
		}

		@Override
		protected String doInBackground(String... params) {
			getContactInfo();
			return null;

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();

			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.info_smsok),
					Toast.LENGTH_LONG).show();

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