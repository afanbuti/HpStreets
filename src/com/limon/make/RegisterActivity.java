package com.limon.make;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.limon.bean.UserInfo;
import com.limon.common.InfoHelper;
import com.limon.common.JsonDataPostApi;
import com.limon.common.ResourceUtil;
import com.limon.common.StringUtils;
import com.mobclick.android.MobclickAgent;

/**
 * 类说明： 注册界面
 * 
 * @version 1.0
 */
public class RegisterActivity extends BaseActivity {
	private Context mContext;
	private EditText emailEdit = null;
	private EditText nameEdit = null;
	private EditText passwordEdit = null;
	private Button regbtn = null;
	private ProgressDialog progressDialog = null;
	private boolean bool = false;
	private Button rButton;
	private TextView textViewTitle = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		// MobclickAgent.onError(this);
		setContentView(R.layout.register);
		emailEdit = (EditText) findViewById(R.id.SignUpEmail);
		textViewTitle = (TextView) findViewById(R.id.title);
		// (Spinner) findViewById(R.id.SignUpGenderSpinner);
		// phoneEdit=(EditText)findViewById(R.id.SignUpMobile);
		nameEdit = (EditText) findViewById(R.id.SignUpName);
		passwordEdit = (EditText) findViewById(R.id.SignUpPassword);
		regbtn = (Button) findViewById(R.id.SignUpButton);

		mContext = getApplicationContext();
		// accessInfo = InfoHelper.getAccessInfo(mContext);

		regbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!InfoHelper.checkNetWork(mContext)) {
					Toast.makeText(mContext,
							ResourceUtil.getString(R.string.error_net),
							Toast.LENGTH_LONG).show();
				} else {
					regAction();
				}
			}
		});
		rButton = (Button) findViewById(R.id.ReturnButton);
		rButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				unregisterReceiver(exitre);
			}
		});
		textViewTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(RegisterActivity.this,
						RegisterDetailActivity.class));
			}
		});
	}

	private void regAction() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				progressDialog.dismiss();
				// Log.d("bool:", String.valueOf(bool));
				if (!bool) {
					startActivity(new Intent(RegisterActivity.this,
							MainActivity.class));
				}
			}
		};
		progressDialog = ProgressDialog.show(this, "", ResourceUtil
				.getString(R.string.execute), true, false);
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				if (!isValid()) {
					signUp();
				}
				Message message = new Message();
				handler.sendMessage(message);
				Looper.loop();
			}
		}.start();
	}

	private boolean isValid() {
		bool = TextUtils.isEmpty(this.nameEdit.getText());
		if (!bool) {
			bool = TextUtils.isEmpty(this.passwordEdit.getText());
			if (!bool) {
				// bool = TextUtils.isEmpty(this.emailEdit.getText());
				// if (!bool) {
				// JsonDataGetApi api = new JsonDataGetApi();
				// JSONObject jobj = null;
				// try {
				// jobj = api.getObject("u.ashx?op=e&n="
				// + this.emailEdit.getText().toString());
				// // Log.d("Reg:",jobj.toString());
				// // GsonBuilder gsonb = new GsonBuilder();
				// // Gson gson = gsonb.create();
				// if (!(jobj == null || "-1".equals(jobj.toString()))) {
				// // UserInfo account = gson.fromJson(jobj.toString(),
				// // UserInfo.class);
				//
				// bool = this.emailEdit.getText().toString().equals(
				// jobj.getString("Email"));
				//
				// if (bool) {
				// Toast
				// .makeText(
				// mContext,
				// ResourceUtil
				// .getString(R.string.error_regemailhas),
				// Toast.LENGTH_LONG).show();
				// }
				// } else {
				// bool = false;
				// }
				// } catch (Exception e) {
				// bool = false;
				// Log.d("Reg:", e.toString());
				// }
				// } else {
				// Toast.makeText(mContext,
				// ResourceUtil.getString(R.string.error_regemail),
				// Toast.LENGTH_LONG).show();
				// // this.emailEdit.setFocusable(true);
				// // this.emailEdit.requestFocus();
				// // this.emailEdit.setFocusableInTouchMode(true);
				// }
			} else {
				Toast.makeText(mContext,
						ResourceUtil.getString(R.string.error_regpass),
						Toast.LENGTH_LONG).show();
				// this.passwordEdit.setFocusable(true);
				// this.passwordEdit.requestFocus();
				// this.passwordEdit.setFocusableInTouchMode(true);
			}

		} else {
			Toast.makeText(mContext,
					ResourceUtil.getString(R.string.error_regname),
					Toast.LENGTH_LONG).show();
			// this.nameEdit.setFocusable(true);
			// this.nameEdit.requestFocus();
			// this.nameEdit.setFocusableInTouchMode(true);
		}
		return bool;
	}

	private void signUp() {
		UserInfo account = new UserInfo();
		account.setEmail(this.emailEdit.getText().toString());
		// account.setNickname(this.nameEdit.getText().toString());
		account.setPsw(this.passwordEdit.getText().toString());
		account.setUname(this.nameEdit.getText().toString());
		// account.setUnderwrite("");
		// account.setHeadImg("");

		// this.genderSpinner.getSelectedItem().toString();
		// this.phoneEdit.getText();

		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		String sJson = gson.toJson(account);
		// Log.d("sJson:", sJson);
		sJson = StringUtils.encode(sJson);
		// Log.d("sJson:", sJson);
		Map<String, String> params = new HashMap<String, String>();
		params.put("u", sJson);

		JsonDataPostApi api = new JsonDataPostApi();
		try {
			String res = api.makeRequest("u.ashx?op=r", params);
			if (!"-1".equals(res)) {
				SharedPreferences sharedPreferences = PreferenceManager
						.getDefaultSharedPreferences(RegisterActivity.this);
				Editor editor = sharedPreferences.edit();// 获取编辑器
				editor.putString("UserID", res);
				editor
						.putString("UserName", this.nameEdit.getText()
								.toString());
				editor.putString("Email", this.emailEdit.getText().toString());
				editor.putInt("Credit", 15);
				editor.putInt("Experience", 0);
				editor.putInt("Sex", 0);
				editor.putString("HeadImg", "");
				editor.putString("Token", "NoAccessToken");
				editor.putString("Secret", "NoAccessSecret");
				editor.putString("Phone", "");
				if (!(this.emailEdit.getText().toString() == null || ""
						.equals(this.emailEdit.getText().toString()))) {
					editor.putInt("Level", 25);
				} else {
					editor.putInt("Level", 10);
				}
				editor.commit();
				bool = false;
				Toast.makeText(getApplicationContext(),
						ResourceUtil.getString(R.string.sucess_reg),
						Toast.LENGTH_LONG).show();
			} else {
				bool = true;
				Toast.makeText(getApplicationContext(),
						ResourceUtil.getString(R.string.error_regfaile),
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					ResourceUtil.getString(R.string.error_net),
					Toast.LENGTH_LONG).show();
			bool = true;
			MobclickAgent.reportError(mContext, "Register_signUp:"
					+ e.toString());
		}
	}
}