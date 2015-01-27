package com.limon.make;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.limon.common.StringUtils;

public class TencOfActivity extends BaseActivity {
	public final static int RESULT_CODE = 1;
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		webView = (WebView) findViewById(R.id.webview);
		Intent intent = this.getIntent();
		if (!intent.equals(null)) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				if (bundle.containsKey("urlStr")) {
					String urlStr = bundle.getString("urlStr");
					WebSettings webSettings = webView.getSettings();
					webSettings.setJavaScriptEnabled(true);
					webSettings.setSaveFormData(true);
					webSettings.setSavePassword(true);
					webSettings.setSupportZoom(true);
					webSettings.setBuiltInZoomControls(true);
					webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
					webView.requestFocus();
					webView.loadUrl(urlStr);
					// Log.i("TAG", "WebView Starting....");
				}
			}
		}
		// 绑定java对象到JavaScript中，这样就能在JavaScript中调用java对象，实现通信。
		// 这种方法第一个参数就是java对象，第二个参数表示java对象的别名，在JavaScript中使用
		webView.addJavascriptInterface(new JavaScriptInterface(), "Methods");
		WebViewClient client = new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// Log.i("TAG", "WebView onPageFinished...");
				view
						.loadUrl("javascript:window.Methods.getHTML('<head>'+document.getElementsByTagName('body')[0].innerHTML+'</head>');");
				super.onPageFinished(view, url);
			}
		};
		webView.setWebViewClient(client);
	}

	class JavaScriptInterface {
		private static final String TAG = "MainActivity";

		public void getHTML(String html) {
			String verifier = getVerifier(html);
			if (!StringUtils.isEmpty(verifier)) {
				Log.i(TAG, "verifier:" + verifier);
				Intent intent = new Intent();
				intent.putExtra("verifier", verifier);
				setResult(RESULT_CODE, intent);
				finish();
				unregisterReceiver(exitre);
			}
		}

		public String getVerifier(String html) {
			String ret = "";
			String regEx = "授权码：[0-9]{6}";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(html);
			boolean result = m.find();
			if (result) {
				ret = m.group(0).substring(4);
			}
			return ret;
		}
	}
}
