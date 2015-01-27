package com.limon.make;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.limon.common.RenrenGetToken;

public class RenrenActivity extends BaseActivity {
	// private Context mContext;
	// private com.limon.bean.AccessInfo accessInfo = null;
	// RenrenGetToken httpPost;
	public static final String TAG = "RenrenActivity";
	WebView webview;
	String url1 = "";
	String code;
	String access_token;
	String expires_in;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		// MobclickAgent.onError(this);
		setContentView(R.layout.webview);

		webview = (WebView) findViewById(R.id.webview);
		// httpPost = new RenrenGetToken();
		// 这行很重要一点要有，不然网页的认证按钮会无效
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		// wv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}

			// 这个证书限于2.1版本以上的Android 系统
			// @Override
			// public void onReceivedSslError(WebView view,
			// SslErrorHandler handler, android.net.http.SslError error) { //
			// 重写此方法可以让webview处理https请求
			// handler.proceed();
			// }

			@Override
			public void onPageFinished(WebView view, String url) {
				url1 = webview.getUrl();
				if (url1 != null) {
					/*
					 * Log.i(TAG, "webview.getUrl() = " + url1);
					 * if(url1.contains("access_token=")){ access_token =
					 * url1.substring(url1.indexOf("access_token=") + 13,
					 * url1.length()-19);
					 * 
					 * try { access_token = URLDecoder.decode(access_token,
					 * "utf-8"); } catch (UnsupportedEncodingException e) {
					 * e.printStackTrace(); }
					 * 
					 * Log.i(TAG, "access_token = " + access_token);
					 * 
					 * }
					 * 
					 * if(url1.contains("expires_in=")){ Log.i(TAG,
					 * "expires_in = " + "不为空"); expires_in =
					 * url1.substring(url1.indexOf("expires_in=") + 11,
					 * url1.length()); Log.i(TAG, "expires_in = " + expires_in);
					 * }
					 */

					// Intent intent2 = new Intent();
					// intent2.setClass(RenrenActivity.this,
					// RenrenOfActivity.class);
					// startActivity(intent2);
					// httpPost.access_token = access_token;
					// httpPost.expires_in = expires_in;
					if (url1.contains("code=")) {
						code = url1.substring(url1.indexOf("code=") + 5, url1
								.length());
						Log.i(TAG, "code = " + code);
						RenrenGetToken.code = code;

						// 如果获得的code不为空，那么跳到httppost中
						if (RenrenGetToken.code != "") {
							Intent intent = new Intent(RenrenActivity.this,
									RenrenOfActivity.class);
							startActivity(intent);
						}
					}
				}
				super.onPageFinished(view, url);
			}

		});
		webview
				.loadUrl("https://graph.renren.com/oauth/authorize?"
						+ "client_id="
						+ RenrenGetToken.RENREN_API_KEY
						+ "&response_type=code"
						+ "&display=touch&redirect_uri=http://graph.renren.com/oauth/login_success.html");
	}

}