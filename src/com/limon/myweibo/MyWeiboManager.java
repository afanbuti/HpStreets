package com.limon.myweibo;

import java.io.IOException;
import java.net.MalformedURLException;

import android.content.Context;

import com.weibo.net.AccessToken;
import com.weibo.net.AsyncWeiboRunner;
import com.weibo.net.AsyncWeiboRunner.RequestListener;
import com.weibo.net.Oauth2AccessTokenHeader;
import com.weibo.net.Utility;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;

public class MyWeiboManager {

	private Weibo mWeibo;

	private static MyWeiboManager mWeiboManager;

	private String mAppkey;
	private String mRedictUrl;

	public static MyWeiboManager getInstance(String appkey, String secret,
			String redictUrl) {
		if (mWeiboManager == null) {
			return new MyWeiboManager(appkey, secret, redictUrl);
		}

		return mWeiboManager;
	}

	public static MyWeiboManager getInstance() {
		return mWeiboManager;
	}

	private MyWeiboManager(String appkey, String secret, String redictUrl) {
		mWeibo = Weibo.getInstance();
		mWeibo.setupConsumerConfig(appkey, secret);
		mWeibo.setRedirectUrl(redictUrl);
		Utility.setAuthorization(new Oauth2AccessTokenHeader());

		mAppkey = appkey;
		mRedictUrl = redictUrl;

		Utility.setAuthorization(new Oauth2AccessTokenHeader());
	}

	public String getRedictUrl() {
		return mRedictUrl;
	}

	public String getAppKey() {
		return mAppkey;
	}

	public boolean isSessionValid() {
		return mWeibo.isSessionValid();
	}

	// 获得AUTHO认证URL地址
	public String getAuthoUrl() {
		WeiboParameters parameters = new WeiboParameters();
		parameters.add("client_id", mWeibo.getAppKey());
		parameters.add("response_type", "token");
		parameters.add("redirect_uri", mWeibo.getRedirectUrl());
		parameters.add("display", "mobile");

		if (mWeibo.isSessionValid()) {
			parameters.add(Weibo.TOKEN, mWeibo.getAccessToken().getToken());
		}

		String url = Weibo.URL_OAUTH2_ACCESS_AUTHORIZE + "?"
				+ Utility.encodeUrl(parameters);

		return url;
	}

	public void setAccessToaken(AccessToken accessToken) {
		mWeibo.setAccessToken(accessToken);
	}

	// 发送文本
	public String update(Context context, String content,
			RequestListener listener) throws MalformedURLException,
			IOException, WeiboException {
		
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", getAppKey());
		bundle.add("status", content);
		//Log.i(getAppKey(), "update............."+content);
		String rlt = "";
		String url = Weibo.SERVER + "statuses/update.json";
		AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(mWeibo);
		weiboRunner.request(context, url, bundle, Utility.HTTPMETHOD_POST,
				listener);
		return rlt;
	}
//带图片的微博
	public String upload(Context context, String content, String picFileName,
			RequestListener listener) throws MalformedURLException,
			IOException, WeiboException {
		//Log.i("", "upload.............");
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", getAppKey());
		bundle.add("status", content);

		// WeiboParameters file = new WeiboParameters();
		bundle.add("pic", picFileName);
		//Log.i(getAppKey(), "upload............."+content+"="+picFileName);
		String rlt = "";
		String url = Weibo.SERVER + "statuses/upload.json";
		AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(mWeibo);
		weiboRunner.request(context, url, bundle, Utility.HTTPMETHOD_POST,
				listener);
		return rlt;
	}
	//加关注
	public String createFriend(Context context, String content, 
			RequestListener listener) throws MalformedURLException,
			IOException, WeiboException {
		//Log.i("", "upload.............");
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", getAppKey());
		bundle.add("uid", "2211339253");
		bundle.add("screen_name", "%E5%BC%80%E5%BF%83%E8%A1%97%E5%8C%BA%E7%BD%91");

		// WeiboParameters file = new WeiboParameters();
		//bundle.add("pic", picFileName);
		//Log.i(getAppKey(), "upload............."+content+"="+getAppKey());
		String rlt = "";
		String url = Weibo.SERVER + "friendships/create.json";
		AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(mWeibo);
		weiboRunner.request(context, url, bundle, Utility.HTTPMETHOD_POST,
				listener);
		return rlt;
	}
}
