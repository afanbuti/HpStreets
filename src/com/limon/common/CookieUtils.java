package com.limon.common;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class CookieUtils {
	CookieStore mCookieStore;

	public void getcookie() {
		try {

			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet("http://www.mylicenser.com/write.php");
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			Log.i("TAG", "geolo: " + EntityUtils.toString(entity));
			mCookieStore = httpclient.getCookieStore();
			List<Cookie> cookies = mCookieStore.getCookies();
			if (entity != null) {
				entity.consumeContent();
			}

			if (cookies.isEmpty()) {
				Log.i("TAG", "NONE");
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					Log.i("TAG", "- domain " + cookies.get(i).getDomain());
					Log.i("TAG", "- path " + cookies.get(i).getPath());
					Log.i("TAG", "- value " + cookies.get(i).getValue());
					Log.i("TAG", "- name " + cookies.get(i).getName());
					Log.i("TAG", "- port " + cookies.get(i).getPorts());
					Log.i("TAG", "- comment " + cookies.get(i).getComment());
					Log.i("TAG", "- commenturl"
							+ cookies.get(i).getCommentURL());

					Log.i("TAG", "- all " + cookies.get(i).toString());
				}
			}

			httpclient.getConnectionManager().shutdown();

		} catch (Exception e) {
			Log.e("TAG", e.toString());
		} finally {
			// Todo
		}
	}

	public void setcookie() {
		try {
			Log.i("TAG", "Start......");
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet("http://www.mylicenser.com/read.php");
			httpclient.setCookieStore(mCookieStore);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			Log.i("TAG", "geolo: " + EntityUtils.toString(entity));

			List<Cookie> cookies = httpclient.getCookieStore().getCookies();
			if (entity != null) {
				entity.consumeContent();
			}

			if (cookies.isEmpty()) {
				Log.i("TAG", "NONE");
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					Log.i("TAG", "- domain " + cookies.get(i).getDomain());
					Log.i("TAG", "- path " + cookies.get(i).getPath());
					Log.i("TAG", "- value " + cookies.get(i).getValue());
					Log.i("TAG", "- name " + cookies.get(i).getName());
					Log.i("TAG", "- port " + cookies.get(i).getPorts());
					Log.i("TAG", "- comment " + cookies.get(i).getComment());
					Log.i("TAG", "- commenturl"
							+ cookies.get(i).getCommentURL());

					Log.i("TAG", "- all " + cookies.get(i).toString());
				}
			}
			httpclient.getConnectionManager().shutdown();

		} catch (Exception e) {
			Log.e("TAG", e.toString());
		} finally {
			// Todo
		}
	}
}
