package com.limon.make;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.limon.common.RenrenGetToken;

public class RenrenOfActivity extends BaseActivity {
	private Context mContext;
	// private com.limon.bean.AccessInfo accessInfo = null;
	RenrenGetToken httpPost;
	private String sessionKey;
	private String userId;

	public static String oauth_token_secret = "";
	public static String oauth_token = "";
	public static RenrenOfActivity webInstance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		httpPost = new RenrenGetToken();
		httpPost.getAccessToken();

		sessionKey = RenrenGetToken.SESSION_KEY;
		userId = RenrenGetToken.userid;

		SharedPreferences setting = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = setting.edit();
		editor.putString("sessionKey", sessionKey);
		editor.putString("userId", userId);
		editor.commit();

		if (!(userId == null || "".equals(userId))) {
			/*
			 * Intent intent = new Intent(this, ShareActivity.class); Bundle
			 * bundle = new Bundle(); bundle.putString("sessionKey",
			 * sessionKey); intent.putExtras(bundle); startActivity(intent);
			 */

			/*
			 * AccessInfo accessInfo = new AccessInfo();
			 * accessInfo.setUserID(userId);
			 * accessInfo.setAccessToken(httpPost.access_token);
			 * accessInfo.setAccessSecret("NoAccessSecret");
			 * 
			 * AccessInfoHelper accessDBHelper = new AccessInfoHelper(mContext);
			 * accessDBHelper.open(); accessDBHelper.create(accessInfo);
			 * accessDBHelper.close();
			 */
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(RenrenOfActivity.this);
			Editor edit = sharedPreferences.edit();// 获取编辑器
			edit.putString("UserID", userId);
			edit.putString("Token", RenrenGetToken.access_token);
			edit.putString("Secret", "Renren");
			edit.commit();

			Intent intent2 = new Intent();
			intent2.setClass(this, MainActivity.class);
			startActivity(intent2);
		} else {
			Toast.makeText(mContext, "失败了。。。", Toast.LENGTH_LONG);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// MobclickAgent.onPause(this);
	}

}