package com.limon.make;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.feedback.UMFeedbackService;
import com.limon.common.InfoHelper;
import com.limon.common.JsonDataGetApi;
import com.limon.common.ResourceUtil;

/**
 * ���ý���
 * 
 * @author Administrator
 * 
 */
public class SettingActivity extends PreferenceActivity implements
		OnPreferenceClickListener, OnPreferenceChangeListener {

	private Preference updateMyprofile, updateAbout,
			updateFeedback,updateMsctts;
	private CheckBoxPreference pushNotification, isPublic;

	private String myprofile, pushnotification, syncsettings, about,
			ispublic, feedback, msctts;
	// ProgressDialog progressDialog = null;
	private String userId = "";
	private Context mContext = null;
	private PopupWindow m_popupWindow = null;
	SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��xml�ļ������Preference��
		addPreferencesFromResource(R.xml.preferences);
		getListView().setCacheColorHint(Color.TRANSPARENT);
		// getListView().setBackgroundColor(Color.LTGRAY);
		// getListView().setBackgroundResource(R.drawable.public_mood_edit_bg);
		myprofile = getResources().getString(R.string.my_profile);
		syncsettings = getResources().getString(R.string.sync_settings);
		about = getResources().getString(R.string.about);
		msctts = getResources().getString(R.string.msctts);
		feedback = getResources().getString(R.string.info_feedback);
		pushnotification = getResources().getString(
				R.string.preferences_push_notification);
		ispublic = getResources().getString(R.string.preferences_public);

		updateMyprofile = findPreference(myprofile);
		updateMsctts = findPreference(msctts);
		pushNotification = (CheckBoxPreference) findPreference(pushnotification);
		isPublic = (CheckBoxPreference) findPreference(ispublic);
		updateFeedback = findPreference(feedback);
		updateAbout = findPreference(about);


		updateMyprofile.setOnPreferenceClickListener(this);
		updateMsctts.setOnPreferenceClickListener(this);
		pushNotification.setOnPreferenceChangeListener(this);
		isPublic.setOnPreferenceChangeListener(this);
		updateAbout.setOnPreferenceClickListener(this);
		updateFeedback.setOnPreferenceClickListener(this);
		mContext = getApplicationContext();
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		userId = sp.getString("UserID", "");

	}

	OnSharedPreferenceChangeListener myPrefListner = new OnSharedPreferenceChangeListener() {
		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			// Log.v("Key_SystemSetting", key);
			Boolean updateSwitch = sharedPreferences.getBoolean(
					pushnotification, true);
			Boolean iSpublic = sharedPreferences.getBoolean(ispublic, true);
			guiAction(iSpublic, updateSwitch);
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		sp.registerOnSharedPreferenceChangeListener(myPrefListner);
	}

	private void guiAction(final boolean iSpublic, final boolean iSsyncset) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
			}
		};
		new Thread() {
			@Override
			public void run() {

				JsonDataGetApi api = new JsonDataGetApi();
				// JSONObject json = null;
				try {
					api.getString("u.ashx?op=up&id=" + userId + "&pub="
							+ iSpublic + "&syn=" + iSsyncset);
					// Log
					// .d("u.ashx?op=u&id=" + userId, "u.ashx?op=up&id="
					// + userId + "&pub=" + iSpublic + "&syn="
					// + iSsyncset);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Message message = new Message();
				// message.what=0;
				// message.obj=jsonArray;
				handler.sendMessage(message);

			}
		}.start();
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		// Log.v("Key_SystemSetting", preference.getKey());
		// �ж����ĸ�Preference�������
		if (preference.getKey().equals(myprofile)) {
			startActivity(new Intent(this, MyProfileActivity.class));
		} else if (preference.getKey().equals(syncsettings)) {
			// Uri uri = Uri.parse("http://www.baidu.com");
			// Intent it = new Intent(Intent.ACTION_VIEW, uri);
			// startActivity(it);
		} else if (preference.getKey().equals(feedback)) {
			UMFeedbackService.openUmengFeedbackSDK(this);
		} else if (preference.getKey().equals(msctts)) {
			startActivity(new Intent(this, TtsPreferenceActivity.class));
		} else if (preference.getKey().equals(about)) {
			startActivity(new Intent(this, AboutActivity.class));
			// Uri uri = Uri
			// .parse("http://maps.google.com/maps?f=d&saddr=startLat%20startLng&daddr=endLat%20endLng&hl=en");
			// Intent it = new Intent(Intent.ACTION_VIEW, uri);
			// startActivity(it);

			/*
			 * ��绰 1. //�г����ų��� 2. Uri uri = Uri.parse("tel:0800000123"); 3.
			 * Intent it = new Intent(Intent.ACTION_DIAL, uri); 4.
			 * startActivity(it);
			 * 
			 * 1. //ֱ�Ӵ�绰��ȥ 2. Uri uri = Uri.parse("tel:0800000123"); 3. Intent
			 * it = new Intent(Intent.ACTION_CALL, uri); 4. startActivity(it);
			 * 5. //���@����Ҫ�� AndroidManifest.xml �У����� 6.
			 * //<uses-permission id="android.permission.CALL_PHONE" />
			 * 
			 * ����SMS/MMS 1. //���ö��ų��� 2. Intent it = new
			 * Intent(Intent.ACTION_VIEW, uri); 3. it.putExtra("sms_body",
			 * "The SMS text"); 4. it.setType("vnd.android-dir/mms-sms"); 5.
			 * startActivity(it);
			 * 
			 * 1. //������Ϣ 2. Uri uri = Uri.parse("smsto://0800000123"); 3.
			 * Intent it = new Intent(Intent.ACTION_SENDTO, uri); 4.
			 * it.putExtra("sms_body", "The SMS text"); 5. startActivity(it);
			 * 
			 * 1. //���� MMS 2. Uri uri =
			 * Uri.parse("content://media/external/images/media/23"); 3. Intent
			 * it = new Intent(Intent.ACTION_SEND); 4. it.putExtra("sms_body",
			 * "some text"); 5. it.putExtra(Intent.EXTRA_STREAM, uri); 6.
			 * it.setType("image/png"); 7. startActivity(it);
			 * 
			 * ���� Email 1. Uri uri = Uri.parse("mailto:xxx@abc.com"); 2.
			 * Intent it = new Intent(Intent.ACTION_SENDTO, uri); 3.
			 * startActivity(it);
			 * 
			 * 1. Intent it = new Intent(Intent.ACTION_SEND); 2.
			 * it.putExtra(Intent.EXTRA_EMAIL, "me@abc.com"); 3.
			 * it.putExtra(Intent.EXTRA_TEXT, "The email body text"); 4.
			 * it.setType("text/plain"); 5.
			 * startActivity(Intent.createChooser(it, "Choose Email Client"));
			 * 
			 * 1. Intent it=new Intent(Intent.ACTION_SEND); 2. String[]
			 * tos={"me@abc.com"}; 3. String[] ccs={"you@abc.com"}; 4.
			 * it.putExtra(Intent.EXTRA_EMAIL, tos); 5.
			 * it.putExtra(Intent.EXTRA_CC, ccs); 6.
			 * it.putExtra(Intent.EXTRA_TEXT, "The email body text"); 7.
			 * it.putExtra(Intent.EXTRA_SUBJECT, "The email subject text"); 8.
			 * it.setType("message/rfc822"); 9.
			 * startActivity(Intent.createChooser(it, "Choose Email Client"));
			 * 
			 * 1. //���͸��� 2. Intent it = new Intent(Intent.ACTION_SEND); 3.
			 * it.putExtra(Intent.EXTRA_SUBJECT, "The email subject text"); 4.
			 * it.putExtra(Intent.EXTRA_STREAM, "file:///sdcard/mysong.mp3"); 5.
			 * sendIntent.setType("audio/mp3"); 6.
			 * startActivity(Intent.createChooser(it, "Choose Email Client"));
			 * 
			 * ���Ŷ�ý�� Uri uri = Uri.parse("file:///sdcard/song.mp3"); Intent
			 * it = new Intent(Intent.ACTION_VIEW, uri);
			 * it.setType("audio/mp3"); startActivity(it);
			 * 
			 * 
			 * 
			 * Uri uri =
			 * Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
			 * "1"); Intent it = new Intent(Intent.ACTION_VIEW, uri);
			 * 
			 * startActivity(it);
			 * 
			 * 
			 * 
			 * Market ��� 1. //Ѱ��ĳ��Ӧ�� 2. Uri uri =
			 * Uri.parse("market://search?q=pname:pkg_name"); 3. Intent it = new
			 * Intent(Intent.ACTION_VIEW, uri); 4. startActivity(it); 5. //where
			 * pkg_name is the full package path for an application
			 * 
			 * 1. //��ʾĳ��Ӧ�õ������Ϣ 2. Uri uri =
			 * Uri.parse("market://details?id=app_id"); 3. Intent it = new
			 * Intent(Intent.ACTION_VIEW, uri); 4. startActivity(it); 5. //where
			 * app_id is the application ID, find the ID 6. //by clicking on
			 * your application on Market home 7. //page, and notice the ID from
			 * the address bar
			 * 
			 * 
			 * Uninstall Ӧ�ó��� 1. Uri uri = Uri.fromParts("package",
			 * strPackageName, null); 2. Intent it = new
			 * Intent(Intent.ACTION_DELETE, uri); 3. startActivity(it);
			 */
		} else {
			return false;
		}
		return true;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		// Log.v("SystemSetting", "preference is changed");
		// Log.v("Key_SystemSetting", preference.getKey());

		// �ж����ĸ�Preference�ı���
		// if(preference.getKey().equals(ispublic))
		// {
		// guiAction(String.valueOf(newValue));
		// }
		// else if(preference.getKey().equals(updateFrequencyKey))
		// {
		// Log.v("SystemSetting", "list preference is changed");
		// }
		// else
		// {
		// //����false��ʾ�����?�ı�
		// return false;
		// }
		// ����true��ʾ����ı�
		return true;
	}

	@SuppressWarnings("unused")
	private void openMenuClick() {
		LayoutInflater layoutInflater = getLayoutInflater();
		View view = layoutInflater.inflate(R.layout.popupmenu, null);

		// PopupWindow�����Ĵ�����ʾ��view,�ڶ��͵������ֱ��ʾ�˵������ڵĴ�С
		m_popupWindow = new PopupWindow(view, 250, 300);
		// PopupWindow�����Ĵ�����ʾ��λ�� ����һ������ָ���Ǹ�view��
		// ���ڶ��͵������ֱ��ʾ�����view��x��y�ľ���
		// m_popupWindow.showAsDropDown(nullimage, 0, 0);
		// �ײ���ʾ
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

	// private void openMenu() {
	// // if (!flag) {
	// m_popupWindow.setAnimationStyle(R.style.InfoPopAnimation);
	// m_popupWindow.showAtLocation(findViewById(R.id.SLoginBtn),
	// Gravity.NO_GRAVITY, 0, 0);
	// m_popupWindow.setFocusable(true);
	// m_popupWindow.update();
	// // flag = true;
	// // } else {
	// // m_popupWindow.dismiss();
	// // m_popupWindow.setFocusable(false);
	// // flag = false;
	// // }
	// }
}
