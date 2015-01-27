package com.limon.make;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.limon.bean.BlogInfo;
import com.limon.common.InfoHelper;
import com.limon.common.JsonDataPostApi;
import com.limon.common.ResourceUtil;
import com.limon.common.StringUtils;
import com.mobclick.android.MobclickAgent;

/**
 * 日志评论
 * 
 * @author Administrator
 * 
 */
public class BlogCommentActivity extends BaseActivity {
	private ProgressDialog progressDialog = null;
	private TextView textViewName = null, textViewMessage = null,
			textViewDateline = null;
	private String blogid = "", uid = "", name = "";
	private String userId = "";
	private Button mButton, rButton;
	private EditText contentEditText = null;
	private TextView wordCounterTextView = null;
	private String ref = "-1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commentdoing);
		setTitle(ResourceUtil.getString(R.string.info_commentblog));
		mContext = getApplicationContext();
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(BlogCommentActivity.this);
		userId = settings.getString("UserID", "");
		// uname = settings.getString("UserName", "");
		if ("".equals(userId)) {
			Toast.makeText(mContext,
					ResourceUtil.getString(R.string.info_nologin),
					Toast.LENGTH_LONG).show();
			startActivity(new Intent(this, LogInActivity.class));
			finish();
			unregisterReceiver(exitre);
		}
		textViewName = (TextView) findViewById(R.id.name);
		textViewMessage = (TextView) findViewById(R.id.message);
		textViewDateline = (TextView) findViewById(R.id.dateline);
		blogid = getIntent().getExtras().getString("blogid");
		uid = getIntent().getExtras().getString("uid");
		name = getIntent().getExtras().getString("name");
		String title = getIntent().getExtras().getString("title");
		String dateline = getIntent().getExtras().getString("dateline");
		// Log.d("blogid", blogid);
		// Log.d("dateline", dateline);
		// Log.d("message", message);
		// Log.d("name", name);
		textViewName.setText(name);
		textViewMessage.setText(title);
		textViewDateline.setText(dateline);

		contentEditText = (EditText) findViewById(R.id.share_content);

		wordCounterTextView = (TextView) findViewById(R.id.share_word_counter);

		rButton = (Button) findViewById(R.id.ReturnButton);
		mButton = (Button) findViewById(R.id.SignUpButton);

		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!InfoHelper.checkNetWork(mContext)) {
					Toast.makeText(mContext,
							ResourceUtil.getString(R.string.error_net),
							Toast.LENGTH_LONG).show();
				} else {
					submitSomething();
				}
			}
		});
		rButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				unregisterReceiver(exitre);
			}
		});
		// 侦听EditText字数改变
		TextWatcher watcher = new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				textCountSet();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				textCountSet();
			}

			@Override
			public void afterTextChanged(Editable s) {
				textCountSet();
			}
		};
		contentEditText.addTextChangedListener(watcher);
	}

	private void submitSomething() {
		progressDialog = ProgressDialog.show(this, "", ResourceUtil
				.getString(R.string.execute), true, false);
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				contentEditText.setText("");
				progressDialog.dismiss();
				if (!"-1".equals(ref)) {
					Toast
							.makeText(
									mContext,
									ResourceUtil.getString(R.string.submit_ok)
											+ String
													.format(
															"\n"
																	+ ResourceUtil
																			.getString(R.string.info_credit),
															"2", "1"),
									Toast.LENGTH_SHORT).show();
				}

			}
		};
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				if (isChecked()) {
					String msg = contentEditText.getText().toString();
					// if (msg.getBytes().length != msg.length()) {
					// try {
					// msg = URLEncoder.encode(msg, "UTF-8");
					// } catch (UnsupportedEncodingException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					// }
					uploadStatus(msg);
				}
				Message message = new Message();
				handler.sendMessage(message);
				Looper.loop();
			}
		}.start();
	}

	private void uploadStatus(String msg) {
		uploadData(msg, "");
	}

	private void uploadData(String msg, String newName) {

		BlogInfo blog = new BlogInfo();
		blog.setContent(msg);
		blog.setUid(uid);
		blog.setUsername(name);
		blog.setBlogid(blogid);
		blog.setMid(userId);
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		String sJson = gson.toJson(blog);
		sJson = StringUtils.encode(sJson);
		Map<String, String> params = new HashMap<String, String>();
		params.put("u", sJson);

		JsonDataPostApi api = new JsonDataPostApi();
		try {
			ref = api.makeRequest("b.ashx?op=rd", params);
		} catch (Exception e) {
			Toast.makeText(mContext,
					ResourceUtil.getString(R.string.error_net),
					Toast.LENGTH_LONG).show();
			MobclickAgent.reportError(mContext, "BlogComment_uploadData:"
					+ e.toString());
		}
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(BlogCommentActivity.this);
		Editor editor = sharedPreferences.edit();
		editor.putInt("Credit", sharedPreferences.getInt("Credit", 0) + 2);
		editor.putInt("Experience",
				sharedPreferences.getInt("Experience", 0) + 1);
		editor.commit();
	}

	/**
	 * 设置稿件字数
	 */
	private void textCountSet() {
		String textContent = contentEditText.getText().toString();
		int currentLength = textContent.length();
		if (currentLength <= 140) {
			wordCounterTextView.setTextColor(Color.BLACK);
			wordCounterTextView.setText(String.valueOf(textContent.length())
					+ " / 140");
		} else {
			wordCounterTextView.setTextColor(Color.RED);
			wordCounterTextView.setText(String.valueOf(140 - currentLength));
		}
	}

	/**
	 * 数据合法性判断
	 * 
	 * @return
	 */
	private boolean isChecked() {
		boolean ret = true;
		if (StringUtils.isBlank(contentEditText.getText().toString())) {
			Toast.makeText(mContext,
					ResourceUtil.getString(R.string.doing_content),
					Toast.LENGTH_SHORT).show();
			ret = false;
		} else if (contentEditText.getText().toString().length() > 140) {
			int currentLength = contentEditText.getText().toString().length();

			Toast.makeText(
					mContext,
					ResourceUtil.getString(R.string.error_overflow)
							+ (currentLength - 140)
							+ ResourceUtil.getString(R.string.flowword),
					Toast.LENGTH_SHORT).show();
			ret = false;
		}
		return ret;
	}
}
