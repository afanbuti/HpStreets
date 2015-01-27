package com.limon.make;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;

import com.limon.myweibo.AuthoSharePreference;
import com.limon.myweibo.MyWeiboManager;
import com.limon.myweibo.WeiboConstParam;
import com.weibo.net.AccessToken;

public class SinaActivity extends BaseActivity {
	@SuppressWarnings("unused")
	private Context mContext;
	private String accessToken;
	private String accessSecret;
	public static String oauth_token_secret = "";
	public static String oauth_token = "", userId = "";
	public static SinaActivity webInstance = null;
	private MyWeiboManager mWeiboManager;
	public static final  int REQUEST_AUTH_ACTIVITY_CODE = 0x0001;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		// setContentView( R.layout.splash );
		webInstance = this;
		mContext = getApplicationContext();
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(SinaActivity.this);
		userId = settings.getString("UserID", "");
		accessToken = settings.getString("Token", "");
		accessSecret = settings.getString("Secret", "");

	}

	@Override
	protected void onResume() {
		super.onResume();
		// 之前登陆过
//		if (!"".equals(userId) && !"".equals(accessToken)
//				&& "Sina".equals(accessSecret)) {
//			Intent intent = new Intent();
//			intent.setClass(SinaActivity.this, MainActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			finish();
//			unregisterReceiver(exitre);
//		} else {
//			startOAuthView();
//		}
		
		//
    	mWeiboManager = MyWeiboManager.getInstance(WeiboConstParam.CONSUMER_KEY,
				WeiboConstParam.CONSUMER_SECRET, 
				WeiboConstParam.REDIRECT_URL);	
		if (!"".equals(userId) && !"".equals(accessToken)
		&& "Sina".equals(accessSecret)) {
			String token = AuthoSharePreference.getToken(this);
    		 AccessToken accessToken = new AccessToken(token, WeiboConstParam.CONSUMER_SECRET);     
    	        mWeiboManager.setAccessToaken(accessToken);
    			Intent intent = new Intent();
    			intent.setClass(SinaActivity.this, MainActivity.class);
    			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			startActivity(intent);
    			finish();
    			unregisterReceiver(exitre);
    	 
    	}else{
    		Intent intent = new Intent();
    		intent.setClass(SinaActivity.this, WebViewSinaActivity.class);
    		startActivityForResult(intent, REQUEST_AUTH_ACTIVITY_CODE);
    	}
    	
	}
/*
	private void startOAuthView() {
		try {

			String tokenUrl = "http://open.weibo.com/oauth/authorize?oauth_token=";
			String tempStr = SinaGetToken.getRequestToken();
			if (!tempStr.equals("error")) { // 如果获取失败
				Uri uri = Uri.parse("http://example?" + tempStr);
				oauth_token = uri.getQueryParameter("oauth_token").trim();
				oauth_token_secret = uri
						.getQueryParameter("oauth_token_secret").trim();
				// String
				// msg="获取token成功!\ntoken="+oauth_token+"\noauth_token_secret="+oauth_token_secret;
				tokenUrl = tokenUrl + oauth_token;
				tokenUrl += "&oauth_callback=" + SinaGetToken.oauth_callback;
				// Log.d("SinaActivity", tokenUrl);

				Intent it = new Intent();
				it.putExtra("url", tokenUrl);
				it.setClass(SinaActivity.this, WebViewActivity.class);
				it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(it);
				// SplashActivity.this.finish();
			}

		} catch (Exception e) {

		}
	}*/
}