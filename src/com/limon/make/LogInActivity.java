package com.limon.make;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
 * 锟斤拷说锟斤拷锟斤拷 锟斤拷录锟斤拷锟斤拷
 * 
 * @version 1.0
 */
public class LogInActivity extends BaseActivity {

	private Button sloginbtn = null;
	private Button loginbtn = null;
	private Button regbtn = null;
	private EditText emailEdit = null;
	private EditText passwordEdit = null;
	private boolean flag = false;
	private PopupWindow m_popupWindow = null;
	private Button rButton;
	private ProgressDialog progressDialog = null;
	private boolean bool = false;
	private ImageView nullimage = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		sloginbtn = (Button) findViewById(R.id.SLoginBtn);
		loginbtn = (Button) findViewById(R.id.LoginBtn);
		regbtn = (Button) findViewById(R.id.RegBtn);
		emailEdit = (EditText) findViewById(R.id.LogName);
		passwordEdit = (EditText) findViewById(R.id.LogPass);
		nullimage = (ImageView) findViewById(R.id.nullimage);
		// webInstance = this;
		mContext = getApplicationContext();

		sloginbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!InfoHelper.checkNetWork(mContext)) {
					Toast.makeText(mContext,
							ResourceUtil.getString(R.string.error_net),
							Toast.LENGTH_LONG).show();
				} else {
					// openMenuClick();
					// openMenu();
					Intent intent = new Intent();
					intent.setClass(mContext, SinaActivity.class);
					startActivity(intent);
				}
			}
		});
		loginbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!InfoHelper.checkNetWork(mContext)) {
					Toast.makeText(mContext,
							ResourceUtil.getString(R.string.error_net),
							Toast.LENGTH_LONG).show();
				} else {
					if (!TextUtils.isEmpty(emailEdit.getText())
							&& !TextUtils.isEmpty(passwordEdit.getText())) {
						loginAction();
					} else {
						Toast.makeText(
								getApplicationContext(),
								ResourceUtil
										.getString(R.string.error_loginnull),
								Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		regbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!InfoHelper.checkNetWork(mContext)) {
					Toast.makeText(mContext,
							ResourceUtil.getString(R.string.error_net),
							Toast.LENGTH_LONG).show();
				} else {
					startActivity(new Intent(LogInActivity.this,
							RegisterActivity.class));
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
	}

	@SuppressWarnings("unused")
	private void openMenuClick() {
		LayoutInflater layoutInflater = getLayoutInflater();
		View view = layoutInflater.inflate(R.layout.popupmenu, null);

		// PopupWindow锟斤拷锟斤拷锟侥达拷锟斤拷锟斤拷示锟斤拷view,锟节讹拷锟酵碉拷锟斤拷锟斤拷锟街憋拷锟绞撅拷说锟斤拷锟斤拷锟斤拷诘拇锟叫?
		m_popupWindow = new PopupWindow(view, 250, 300);
		// PopupWindow锟斤拷锟斤拷锟侥达拷锟斤拷锟斤拷示锟斤拷位锟斤拷 锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷指锟斤拷锟角革拷view锟斤拷
		// 锟斤拷锟节讹拷锟酵碉拷锟斤拷锟斤拷锟街憋拷锟绞撅拷锟斤拷锟斤拷view锟斤拷x锟斤拷y锟侥撅拷锟斤拷
		m_popupWindow.showAsDropDown(nullimage, 0, 0);
		// 锟阶诧拷锟斤拷示
		// m_popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		// View home = m_popupWindow.findViewById();
		Button sinabtn = null;
		Button renrenbtn = null;
		Button tencbtn = null;
		Button retbtn = null;
		sinabtn = (Button) view.findViewById(R.id.sinaBtn);
		renrenbtn = (Button) view.findViewById(R.id.renrenBtn);
		tencbtn = (Button) view.findViewById(R.id.tencBtn);
		retbtn = (Button) view.findViewById(R.id.retBtn);
		sinabtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!InfoHelper.checkNetWork(mContext)) {
					Toast.makeText(mContext,
							ResourceUtil.getString(R.string.error_net),
							Toast.LENGTH_LONG).show();
				} else {
					Intent intent = new Intent();
					intent.setClass(mContext, SinaActivity.class);
					startActivity(intent);
					m_popupWindow.dismiss();
				}
			}
		});
		renrenbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!InfoHelper.checkNetWork(mContext)) {
					Toast.makeText(mContext,
							ResourceUtil.getString(R.string.error_net),
							Toast.LENGTH_LONG).show();
				} else {
					Intent intent = new Intent();
					intent.setClass(mContext, RenrenActivity.class);
					startActivity(intent);
					m_popupWindow.dismiss();
				}
			}
		});
		tencbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!InfoHelper.checkNetWork(mContext)) {
					Toast.makeText(mContext,
							ResourceUtil.getString(R.string.error_net),
							Toast.LENGTH_LONG).show();
				} else {
					Intent intent = new Intent();
					intent.setClass(mContext, TencActivity.class);
					startActivity(intent);
					m_popupWindow.dismiss();
				}
			}
		});
		retbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_popupWindow.dismiss();

			}
		});
	}

	@SuppressWarnings("unused")
	private void openMenu() {
		if (!flag) {
			m_popupWindow.setAnimationStyle(R.style.InfoPopAnimation);
			m_popupWindow.showAtLocation(findViewById(R.id.SLoginBtn),Gravity.NO_GRAVITY, 0, 0);
			m_popupWindow.setFocusable(true);
			m_popupWindow.update();
			flag = true;
		} else {
			m_popupWindow.dismiss();
			m_popupWindow.setFocusable(false);
			flag = false;
		}
	}

	private void loginAction() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				progressDialog.dismiss();
				if (bool) {
					Intent intent = new Intent();
					intent.setClass(LogInActivity.this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(new Intent(LogInActivity.this,
							MainActivity.class));
				}
			}
		};
		progressDialog = ProgressDialog.show(this, "", mContext.getResources()
				.getString(R.string.process), true, false);
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				getJsonData();
				Message message = new Message();
				handler.sendMessage(message);
				Looper.loop();
			}
		}.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void getJsonData() {
		// JsonDataGetApi api = new JsonDataGetApi();
		UserInfo account = new UserInfo();
		account.setPsw(this.passwordEdit.getText().toString());
		account.setUname(this.emailEdit.getText().toString());

		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		String sJson = gson.toJson(account);
		// Log.d("login=sJson", sJson.toString());
		sJson = StringUtils.encode(sJson);
		Map<String, String> params = new HashMap<String, String>();
		params.put("u", sJson);

		JsonDataPostApi api = new JsonDataPostApi();
		String fJson = null;
		try {
			fJson = api.makeRequest("u.ashx?op=l", params);
		} catch (Exception e1) {
			Log.d("fJson:", e1.toString());
		}
		// Log.d("login=fJson", fJson);
		try {
			// 锟斤拷锟斤拷GetAccountData锟斤拷锟斤拷
			// jobj = api.getObject("u.ashx?op=u&n=gsh11&p=123");
			// 锟接凤拷锟截碉拷Account Array锟斤拷取锟斤拷锟斤拷一锟斤拷锟斤拷锟?
			// jobj = jArr.getJSONObject(0);
			// jobj = api.getObject("u.ashx?op=l&n=" + this.emailEdit.getText()
			// + "&p=" + this.passwordEdit.getText());

			if (!(fJson == null || "".equals(fJson))) {
				account = gson.fromJson(fJson, UserInfo.class);
			}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					ResourceUtil.getString(R.string.error_loginpass),
					Toast.LENGTH_LONG).show();
			MobclickAgent.reportError(mContext, "Login_getJsonData:"
					+ e.toString());
		}
		// jobj = api.getObject("u.ashx?op=l&n=" + this.emailEdit.getText()
		// + "&p=" + this.passwordEdit.getText());
		// GsonBuilder gsonb = new GsonBuilder();
		// Json锟叫碉拷锟斤拷锟节憋拷锓绞矫伙拷邪旆ㄖ憋拷锟阶拷锟斤拷锟斤拷锟斤拷堑锟紻ate锟斤拷锟斤拷,
		// 锟斤拷锟斤拷锟揭拷锟斤拷锟阶拷锟揭伙拷锟紻ate锟侥凤拷锟斤拷锟叫伙拷锟斤拷.
		// com.limon.common.DateDeserializer ds = new
		// com.limon.common.DateDeserializer();
		// 锟斤拷GsonBuilder锟斤拷锟斤拷锟斤拷锟斤拷指锟斤拷Date锟斤拷锟酵的凤拷锟斤拷锟叫伙拷锟斤拷锟斤拷
		// gsonb.registerTypeAdapter(Date.class, ds);
		// Log.d("login=", jobj.toString());
		// Gson gson = gsonb.create();
		if (null != account) {
			// Log.d("login-headimg",account.getHeadimg());
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(LogInActivity.this);
			Editor editor = sharedPreferences.edit();// 锟斤拷取锟洁辑锟斤拷
			// editor.putString("UserID", jobj.getString("Id"));
			// editor.putString("UserName", jobj.getString("Uname"));
			// editor.putString("Email", jobj.getString("Email"));
			// editor.putInt("Credit", jobj.getInt("Credit"));
			// editor.putInt("Experience", jobj.getInt("Experience"));
			// editor.putInt("Sex", jobj.getInt("Sex"));
			editor.putString("UserID", account.getId());
			editor.putString("UserName", account.getUname());
			editor.putString("Email", account.getEmail());
			editor.putInt("Credit", account.getCredit() + 15);
			editor.putInt("Experience", account.getExperience() + 15);
			editor.putInt("Sex", account.getSex());
			editor.putString("HeadImg", account.getHeadimg());
			editor.putString("Token", "NoToken");
			editor.putString("Secret", "NoSecret");
			editor.putString("Phone", account.getPhone());
			int level = 10;
			if (!(account.getEmail() == null || "".equals(account.getEmail()))) {
				editor.putInt("Level", level + 15);
			} else if (!(account.getHeadimg() == null || "".equals(account
					.getHeadimg()))) {
				editor.putInt("Level", level + 25);
			} else if (account.getSex() != 0) {
				editor.putInt("Level", level + 5);
			}
			editor.commit();
			bool = true;
		} else {
			bool = false;
			Toast.makeText(getApplicationContext(),
					ResourceUtil.getString(R.string.error_loginpass),
					Toast.LENGTH_LONG).show();
		}

	}
}