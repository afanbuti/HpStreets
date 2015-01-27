package com.limon.make;

import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;

import com.limon.common.StringUtils;
import com.limon.common.TencGetToken;

public class TencActivity extends BaseActivity {
	// private Context mContext;
	private String requestToken;
	private String requestTokenSecret;
	private String verifier;
	private String accessToken;
	private String accessTokenSecret;
	private final static int REQUEST_CODE = 1;
	SharedPreferences pres;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		pres = PreferenceManager.getDefaultSharedPreferences(TencActivity.this);
		accessToken = pres.getString("Token", "");
		accessTokenSecret = pres.getString("Secret", "");
		if (!StringUtils.isEmpty(accessToken)
				&& !StringUtils.isEmpty(accessTokenSecret)) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		} else {
			startWebView();
		}
	}

	protected void startWebView() {
		String urlStr = "https://open.t.qq.com/cgi-bin/authorize";
		TencGetToken TencGetToken = new TencGetToken();

		Map<String, String> map = TencGetToken.getRequestToken();

		requestToken = map.get("oauth_token");
		requestTokenSecret = map.get("oauth_token_secret");
		Log.i("TencActivity", "Request Token=" + requestToken);
		Log.i("TencActivity", "Request Token Secret=" + requestTokenSecret);

		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(urlStr);
		urlBuilder.append("?");
		urlBuilder.append("oauth_token=" + requestToken);
		Intent intent = new Intent(TencActivity.this, TencOfActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("urlStr", urlBuilder.toString());
		intent.putExtras(bundle);
		startActivityForResult(intent, REQUEST_CODE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == TencOfActivity.RESULT_CODE) {
				Bundle bundle = data.getExtras();
				verifier = bundle.getString("verifier");
				if (!StringUtils.isEmpty(verifier)) {
					getAccessToken();
				}
			}
		}
	}

	protected void getAccessToken() {
		TencGetToken TencGetToken = new TencGetToken();
		Map<String, String> map = TencGetToken.getAccessToken(requestToken,
				requestTokenSecret, verifier);
		accessToken = map.get("oauth_token");
		accessTokenSecret = map.get("oauth_token_secret");
		Log.i("TencActivity", "Access Token=" + accessToken);
		Log.i("TencActivity", "Access Token Secret=" + accessTokenSecret);
		if (!StringUtils.isEmpty(accessToken)) {
			Editor editor = pres.edit();
			editor.putString("Token", accessToken);
			editor.putString("Secret", "Tenc");
			editor.commit();

			Intent intent = new Intent(this, MainActivity.class);
			// intent.putExtra("Token", accessToken);
			// intent.putExtra("Secret", accessTokenSecret);
			startActivity(intent);
		}
	}
}