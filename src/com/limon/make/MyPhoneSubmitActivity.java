package com.limon.make;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.limon.common.InfoHelper;
import com.limon.common.JsonDataGetApi;
import com.limon.common.ResourceUtil;
import com.mobclick.android.MobclickAgent;

/**
 * 类说明： 个人手机修改界面
 * 
 * @version 1.0
 */
public class MyPhoneSubmitActivity extends BaseActivity {
	private Context mContext;
	private Button regbtn = null;
	// private RadioButton r1 = null;
	// private RadioButton r2 = null;
	private ProgressDialog progressDialog = null;
	private String userId = "";
	private Button rButton;
	// private int sex = 0;
	private EditText etphone = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submitphone);
		// cateSpinner = (Spinner) findViewById(R.id.CategorySpinner);
		regbtn = (Button) findViewById(R.id.AddVenueButton);

		mContext = getApplicationContext();

		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(
		// this,android.R.layout.simple_spinner_item);
		// adapter.setDropDownViewResource(
		// android.R.layout.select_dialog_singlechoice);
		// adapter.add(ResourceUtil.getString(R.string.info_sex0));
		// adapter.add(ResourceUtil.getString(R.string.info_sex1));
		// this.cateSpinner.setAdapter(adapter);
		// this.cateSpinner.setSelection(0);
		etphone = (EditText) findViewById(R.id.share_phone);
		// r1 = (RadioButton) findViewById(R.id.radion1);
		// r2 = (RadioButton) findViewById(R.id.radion2);

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
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MyPhoneSubmitActivity.this);
		userId = settings.getString("UserID", "");
		etphone.setText(settings.getString("Phone", ""));

	}

	private void regAction() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				progressDialog.dismiss();
				// if(ResourceUtil.getString(R.string.info_sex0).equals(cateSpinner.getSelectedItem().toString()))
				// sex=0;
				// else
				// sex=1;
				SharedPreferences sharedPreferences = PreferenceManager
						.getDefaultSharedPreferences(MyPhoneSubmitActivity.this);
				Editor editor = sharedPreferences.edit();// 获取编辑器
				editor.putString("Phone", etphone.getText().toString());
				editor.commit();
				// setResult(RESULT_OK,(new
				// Intent()).setAction(cateSpinner.getSelectedItem().toString()));
				finish();
				unregisterReceiver(exitre);
			}
		};
		progressDialog = ProgressDialog.show(this, "", ResourceUtil
				.getString(R.string.execute), true, false);
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();

				signUp();

				Message message = new Message();
				handler.sendMessage(message);
				Looper.loop();
			}
		}.start();
	}

	private void signUp() {
		JsonDataGetApi api = new JsonDataGetApi();
		try {
			// String sex="0";
			// if(ResourceUtil.getString(R.string.info_sex0).equals(cateSpinner.getSelectedItem().toString()))
			// sex="0";
			// else
			// sex="1";
			api.getString("u.ashx?op=d&id=" + userId + "&p="
					+ etphone.getText().toString());
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					ResourceUtil.getString(R.string.error_net),
					Toast.LENGTH_LONG).show();
			MobclickAgent.reportError(mContext, "MyProfileSubmit_signUp:"
					+ e.toString());
		}
	}
}