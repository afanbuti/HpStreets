package com.limon.make;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.limon.bean.UserInfo;
import com.limon.common.JsonDataPostApi;
import com.limon.common.SinaGetToken;
import com.limon.common.StringUtils;
import com.mobclick.android.MobclickAgent;

public class SinaOfActivity extends BaseActivity {

	public static String oauth_token_secret = "";
	public static String oauth_token = "";
	public static String user_id = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		mContext = getApplicationContext();
		Uri uri = this.getIntent().getData();
		String oauth_verifier = uri.getQueryParameter("oauth_verifier");
		oauth_token = uri.getQueryParameter("oauth_token");
		String oauthTokenSecret = SinaActivity.oauth_token_secret;
		uri = Uri.parse("http://example?"
				+ SinaGetToken.getAcsessToken(oauth_token, oauthTokenSecret,
						oauth_verifier));
		oauth_token = uri.getQueryParameter("oauth_token");
		oauth_token_secret = uri.getQueryParameter("oauth_token_secret");
		user_id = uri.getQueryParameter("user_id");
		// Log.d("SinaOfActivity", oauth_token);
		// SharedPreferences sharedPreferences = PreferenceManager
		// .getDefaultSharedPreferences(SinaOfActivity.this);
		// Editor editor = sharedPreferences.edit();// 获取编辑器
		// editor.putString("UserID", user_id);
		// editor.putString("Token", oauth_token);
		// editor.putString("Secret", "Sina");
		// editor.commit();
		// Intent intent2 = new Intent();
		// intent2.setClass(SinaOfActivity.this, MainActivity.class);
		// startActivity(intent2);
		subLoginData();
	}

	private void subLoginData() {
		// JsonDataGetApi api = new JsonDataGetApi();
		UserInfo account = new UserInfo();
		account.setWeibouid(user_id);
		account.setToken(oauth_token);
		account.setTsecret(oauth_token_secret);
		account.setWeibotype("sina");
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
			fJson = api.makeRequest("u.ashx?op=w", params);
		} catch (Exception e1) {
			Log.d("fJson:", e1.toString());
		}
		Log.d("login=fJson", fJson);
		try {
			// 调用GetAccountData方法
			// jobj = api.getObject("u.ashx?op=u&n=gsh11&p=123");
			// 从返回的Account Array中取出第一个数据
			// jobj = jArr.getJSONObject(0);
			// jobj = api.getObject("u.ashx?op=l&n=" + this.emailEdit.getText()
			// + "&p=" + this.passwordEdit.getText());

			if (!(fJson == null || "".equals(fJson) || "{}".equals(fJson))) {
				account = gson.fromJson(fJson, UserInfo.class);
				if (null != account) {
					SharedPreferences sharedPreferences = PreferenceManager
							.getDefaultSharedPreferences(SinaOfActivity.this);
					Editor editor = sharedPreferences.edit();// 获取编辑器
					editor.putString("UserID", account.getId());
					editor.putString("UserName", account.getUname());
					editor.putString("Email", account.getEmail());
					editor.putInt("Credit", account.getCredit() + 15);
					editor.putInt("Experience", account.getExperience() + 15);
					editor.putInt("Sex", account.getSex());
					editor.putString("HeadImg", account.getHeadimg());
					editor.putString("Weibouid", user_id);
					editor.putString("Token", oauth_token);
					editor.putString("Secret", "Sina");
					int level = 10;
					if (!(account.getEmail() == null || "".equals(account
							.getEmail()))) {
						editor.putInt("Level", level + 15);
					} else if (!(account.getHeadimg() == null || ""
							.equals(account.getHeadimg()))) {
						editor.putInt("Level", level + 25);
					} else if (account.getSex() != 0) {
						editor.putInt("Level", level + 5);
					}
					editor.commit();
					Intent intent2 = new Intent();
					intent2.setClass(SinaOfActivity.this, MainActivity.class);
					startActivity(intent2);
				}
			} else {
				Intent intent = new Intent();
				intent.putExtra("user_id", user_id);
				intent.putExtra("oauth_token", oauth_token);
				intent.putExtra("oauth_token_secret", oauth_token_secret);
				intent.putExtra("weibotype", "sina");
				intent.setClass(SinaOfActivity.this,
						RegisterWeiboActivity.class);
				startActivity(intent);
			}
		} catch (Exception e) {
			MobclickAgent.reportError(mContext, "Login_getJsonData:"
					+ e.toString());
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