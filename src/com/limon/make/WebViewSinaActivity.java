package com.limon.make;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.limon.bean.UserInfo;
import com.limon.common.JsonDataPostApi;
import com.limon.common.StringUtils;
import com.limon.myweibo.AuthoSharePreference;
import com.limon.myweibo.IWeiboClientListener;
import com.limon.myweibo.MyWeiboManager;
import com.limon.myweibo.WeiboConstParam;
import com.mobclick.android.MobclickAgent;
import com.weibo.net.AccessToken;
import com.weibo.net.Utility;
import com.weibo.net.WeiboException;

public class WebViewSinaActivity extends BaseActivity implements IWeiboClientListener{

//	public final static int 
	
	private WebView mWebView;
	
	private View progressBar;
	
	private WeiboWebViewClient mWeiboWebViewClient;
	
	private MyWeiboManager mWeiboManager;
	protected Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);
		mContext = getApplicationContext();
		initView();
		initData();
		
		
		//Log.d("onCreate", "MainThread().getId() = " + Thread.currentThread().getId());
	}

	public void initView()
	{
		mWebView = (WebView) findViewById(R.id.webview);
	    mWebView.setVerticalScrollBarEnabled(false);
	    mWebView.setHorizontalScrollBarEnabled(false);
	    mWebView.requestFocus();
	    
	    WebSettings webSettings = mWebView.getSettings();
	    webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		
		progressBar = findViewById(R.id.show_request_progress_bar);
		
	}

	
	public void initData()
	{
		mWeiboWebViewClient = new WeiboWebViewClient();
		mWebView.setWebViewClient(mWeiboWebViewClient);
		
		CookieSyncManager.createInstance(this);
		 
		
		
		mWeiboManager = MyWeiboManager.getInstance(WeiboConstParam.CONSUMER_KEY,
													WeiboConstParam.CONSUMER_SECRET, 
													WeiboConstParam.REDIRECT_URL);
		
		String authoUrl = mWeiboManager.getAuthoUrl();

		mWebView.loadUrl(authoUrl);

	}

	
	private void showProgress()
	{
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.VISIBLE);
			}
		});
		
	}
	
	private void hideProgress()
	{
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.INVISIBLE);
			}
		});


	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Auth cancel", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onComplete(Bundle values) {
		// TODO Auto-generated method stub
			
			CookieSyncManager.getInstance().sync();
		
	        String access_token = values.getString("access_token");
	        String expires_in = values.getString("expires_in");
	        String remind_in = values.getString("remind_in");
	        String uid = values.getString("uid");
	        
	        Log.d("onComplete", "access_token = " + access_token + 
	        									"\nexpires_in = " + expires_in);

			AuthoSharePreference.putToken(this, access_token);
			AuthoSharePreference.putExpires(this, expires_in);
			AuthoSharePreference.putRemind(this, remind_in);
			AuthoSharePreference.putUid(this, uid);
			      
	        AccessToken accessToken = new AccessToken(access_token, WeiboConstParam.CONSUMER_SECRET);
	        mWeiboManager.setAccessToaken(accessToken);
	        setResult(RESULT_OK);
	        
	        subLoginData(uid,access_token,"");
	        
	        finish();
	}

	@Override
	public void onWeiboException(WeiboException e) {
		// TODO Auto-generated method stub
		Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
	}
	 
	private void subLoginData(String user_id,String oauth_token,String oauth_token_secret) {
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
		//Log.d("login=fJson", fJson);
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
							.getDefaultSharedPreferences(this);
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
					intent2.setClass(this, MainActivity.class);
					startActivity(intent2);
				}
			} else {
				Intent intent = new Intent();
				intent.putExtra("user_id", user_id);
				intent.putExtra("oauth_token", oauth_token);
				intent.putExtra("oauth_token_secret", oauth_token_secret);
				intent.putExtra("weibotype", "sina");
				intent.setClass(this,
						RegisterWeiboActivity.class);
				startActivity(intent);
			}
		} catch (Exception e) {
			MobclickAgent.reportError(mContext, "Login_getJsonData:"
					+ e.toString());
		}

	}
	
	
	 private class WeiboWebViewClient extends WebViewClient {

	        @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        	//Log.d("WeiboWebViewClient", "shouldOverrideUrlLoading url = " + url);
	            showProgress();
	            view.loadUrl(url);
	            return super.shouldOverrideUrlLoading(view, url);
	        }

	        @Override
	        public void onReceivedError(WebView view, int errorCode, String description,
	                String failingUrl) {
	            
	        	//Log.d("WeiboWebViewClient", "onReceivedError failingUrl = " + failingUrl);
	            super.onReceivedError(view, errorCode, description, failingUrl);
	        }

	        @Override
	        public void onPageStarted(WebView view, String url, Bitmap favicon) {
	        	
	        	//Log.d("WeiboWebViewClient", "onPageStarted url = " + url + "\nthreadid = " +Thread.currentThread().getId());
	        	
	        	showProgress();
	        	if (url.startsWith(mWeiboManager.getRedictUrl())) {
	               handleRedirectUrl(view, url, WebViewSinaActivity.this);
	               view.stopLoading();
	               return;
	            }
	        	
	            super.onPageStarted(view, url, favicon);

	        }

	        @Override
	        public void onPageFinished(WebView view, String url) {
	        	//Log.d("WeiboWebViewClient", "onPageFinished url = " + url);
	        	hideProgress();
	            super.onPageFinished(view, url);
	        }
	        
	        private  boolean handleRedirectUrl(WebView view, String url, IWeiboClientListener listener) 
	   	 	{
	   			Bundle values = Utility.parseUrl(url);
	   	        String error = values.getString("error");
	   	        String error_code = values.getString("error_code");
	   	      

	   	     //Log.d("handleRedirectUrl", "error = " + error + "\n error_code = " + error_code);
	   	        if (error == null && error_code == null)
	   	        {
	   	        	listener.onComplete(values);
	   	        }else if (error.equals("access_denied"))
	   	        {
	   	        	listener.onCancel();
	   	        }else{	 
	   	        	WeiboException weiboException = new WeiboException(error, Integer.parseInt(error_code));
	   	        	listener.onWeiboException(weiboException);
	   	        }
	   	        
	   	        return false;
	   	 	}
	 }
	 /**
		 * 监听BACK键
		 * 
		 * @param keyCode
		 * @param event
		 * @return
		 */
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
					&& event.getRepeatCount() == 0) {
				SinaActivity.webInstance.finish();
				finish();
				unregisterReceiver(exitre);
				return true;
			}

			return super.onKeyDown(keyCode, event);
		}

	
}
