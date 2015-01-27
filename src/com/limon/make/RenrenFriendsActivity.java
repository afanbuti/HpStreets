package com.limon.make;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.limon.bean.FriendInfo;
import com.limon.common.AsyncImageLoader;
import com.limon.common.RenrenGetToken;
import com.limon.common.AsyncImageLoader.ImageCallBack;

public class RenrenFriendsActivity extends BaseActivity {
	private static final String TAG = "TestMyFriends";

	String sig;
	String strResult;
	HttpClient client;

	TextView userName;
	ImageView userImage;

	List<FriendInfo> userInfoList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listfriends);

		loadList();

	}

	/**
	 * 锟斤拷锟斤拷锟揭的猴拷锟斤拷锟叫?锟角革拷莺锟斤拷鸦锟皆撅拷确锟斤拷氐锟斤拷锟斤拷
	 * 
	 */
	public void loadList() {
		sig = getParams(); // 锟斤拷锟紸PI签锟斤拷

		getMyFriendsThread();
	}

	/**
	 * 锟斤拷取签锟斤拷锟絡ava锟斤拷锟斤拷
	 */
	public String getParams() {
		long time = System.currentTimeMillis();
		String strTime = URLEncoder.encode(String.valueOf(time)); // 系统锟侥碉拷前时锟戒，锟斤拷为call_id锟斤拷值
		float callId = Float.parseFloat(strTime);

		List<String> params = new ArrayList<String>();
		String method = "friends.getFriends";
		String v1 = "1.0";
		@SuppressWarnings("unused")
		float call_id = callId;
		String access_token = RenrenGetToken.access_token;
		String format = "JSON";
		int count = 20;

		params.add("method=" + method);
		params.add("v=" + v1);
		// params.add("call_id="+call_id);
		params.add("access_token=" + access_token);
		params.add("format=" + format);
		params.add("count=" + count);

		Log.i(TAG, "" + params);

		return getSignature(params, RenrenGetToken.RENREN_SECRET);
	}

	/**
	 * 锟斤拷取签锟斤拷值
	 * 
	 * @param paramList
	 * @param secret
	 * @return
	 */
	public String getSignature(List<String> paramList, String secret) {
		Collections.sort(paramList);
		StringBuffer buffer = new StringBuffer();
		for (String param : paramList) {
			buffer.append(param); // 锟斤拷锟斤拷锟斤拷锟街碉拷裕锟斤拷锟斤拷值锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷泻锟狡达拷锟斤拷锟揭伙拷锟?
		}
		buffer.append(secret); // 锟斤拷末尾追锟斤拷锟斤拷应锟矫碉拷Secret Key
		try { // 锟斤拷锟斤拷锟角斤拷拼锟矫碉拷锟街凤拷转锟斤拷MD5值锟斤拷然锟襟返伙拷
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			StringBuffer result = new StringBuffer();
			try {
				for (byte b : md.digest(buffer.toString().getBytes("UTF-8"))) {
					result.append(Integer.toHexString((b & 0xf0) >>> 4));
					result.append(Integer.toHexString(b & 0x0f));
				}
			} catch (UnsupportedEncodingException e) {
				for (byte b : md.digest(buffer.toString().getBytes())) {
					result.append(Integer.toHexString((b & 0xf0) >>> 4));
					result.append(Integer.toHexString(b & 0x0f));
				}
			}

			return result.toString();
		} catch (java.security.NoSuchAlgorithmException ex) {

		}

		return null;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷暮锟斤拷锟斤拷斜锟侥凤拷锟斤拷
	 */
	public void getMyFriendsThread() {
		new Thread() {
			@Override
			public void run() {
				long time = System.currentTimeMillis();
				String strTime = URLEncoder.encode(String.valueOf(time)); // 系统锟侥碉拷前时锟戒，锟斤拷为call_id锟斤拷值
				@SuppressWarnings("unused")
				float callId = Float.parseFloat(strTime);
				String url = "http://api.renren.com/restserver.do"; // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟狡教ˋPI锟斤拷锟斤拷锟斤拷锟侥碉拷址
				String signature = sig; // 锟斤拷锟斤拷锟斤拷签锟斤拷
				String method = "friends.getFriends"; // 锟接匡拷锟斤拷锟?
				String v2 = "1.0"; // API锟侥版本锟脚ｏ拷锟斤拷锟斤拷锟矫筹拷1.0
				// float call_id = callId;
				String access_token = RenrenGetToken.access_token;
				String format = "JSON";
				int count = 20;

				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("sig", signature));
				params.add(new BasicNameValuePair("method", method));
				params.add(new BasicNameValuePair("v", v2));
				// params.add(new BasicNameValuePair("call_id",
				// String.valueOf(call_id)));
				params
						.add(new BasicNameValuePair("access_token",
								access_token));
				params.add(new BasicNameValuePair("format", format));
				params.add(new BasicNameValuePair("count", Integer
						.toString(count)));

				client = new DefaultHttpClient();

				try {
					HttpPost httpPost = new HttpPost(url);
					/* 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟?*/
					httpPost.setEntity(new UrlEncodedFormEntity(params,
							HTTP.UTF_8));
					/* 锟斤拷锟斤拷锟斤拷锟襟并等达拷锟斤拷应 */
					HttpResponse httpResponse = client.execute(httpPost);
					/* 锟斤拷状态锟斤拷为200 ok */
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						/* 锟斤拷锟斤拷锟斤拷锟斤拷锟?*/
						strResult = EntityUtils.toString(httpResponse
								.getEntity());
					} else {
						strResult = "Error Response: "
								+ httpResponse.getStatusLine().toString();
					}
				} catch (ClientProtocolException e) {
					strResult = e.getMessage().toString();
					e.printStackTrace();
				} catch (IOException e) {
					strResult = e.getMessage().toString();
					e.printStackTrace();
				} catch (Exception e) {
					strResult = e.getMessage().toString();
					e.printStackTrace();
				}

				Log.i(TAG, strResult);
				JSONArray data;
				try {
					data = new JSONArray(strResult);

					for (int i = 0; i < data.length(); i++) {
						JSONObject user = data.getJSONObject(i);
						String id = user.getString("id");
						String name = user.getString("name");
						@SuppressWarnings("unused")
						String tinyurl = user.getString("tinyurl");
						String headurl = user.getString("headurl");

						if (userInfoList == null) {
							userInfoList = new ArrayList<FriendInfo>();
						}

						FriendInfo u = new FriendInfo();
						u.setId(id);
						u.setName(name);
						// u.setTinyurl(tinyurl);
						u.setHeadImg(headurl);
						userInfoList.add(u);

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				Message message = handler.obtainMessage(0);
				handler.sendMessage(message);
			}
		}.start();

	}

	/**
	 * 锟斤拷锟斤拷UI锟竭筹拷ListView
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			if (userInfoList != null) {
				FriendsAdapater adapater = new FriendsAdapater();
				ListView msgList = (ListView) findViewById(R.id.msglist);

				// 锟斤拷每一锟斤拷微锟斤拷锟斤拷息锟斤拷锟斤拷锟绞憋拷锟斤拷锟接?
				msgList.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View view,
							int arg2, long arg3) {
						Object obj = view.getTag();
						if (obj != null) {
							@SuppressWarnings("unused")
							String id = obj.toString();
							String un = userName.getText().toString();
							CharSequence[] items = { un + "锟斤拷锟斤拷页",
									"锟斤拷锟斤拷通讯锟斤拷头锟斤拷", "锟斤拷锟斤拷锟斤拷通讯锟斤拷" };

							AlertDialog.Builder builder = new AlertDialog.Builder(
									RenrenFriendsActivity.this);
							builder.setTitle("锟斤拷锟斤拷");
							builder.setIcon(R.drawable.icon);
							builder.setItems(items,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int item) {
											// 去锟斤拷锟斤拷锟斤拷页
											if (item == 0) {
												// Intent intent = new
												// Intent(TestMyFriends.this,
												// TestFriendView.class);
												// startActivity(intent);
											}
											// 锟斤拷锟斤拷通讯锟斤拷头锟斤拷
											else if (item == 1) {

											}
											// 锟斤拷锟斤拷锟斤拷通讯锟斤拷
											else if (item == 2) {

											}
										}
									});
							AlertDialog alert = builder.create();
							alert.show();
						}
					}

				});

				msgList.setAdapter(adapater);
			}
		}
	};

	/**
	 * 锟矫伙拷锟叫憋拷Adapater
	 */
	public class FriendsAdapater extends BaseAdapter {
		private AsyncImageLoader asyncImageLoader;

		@Override
		public int getCount() {
			return userInfoList.size();
		}

		@Override
		public Object getItem(int position) {
			return userInfoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			asyncImageLoader = new AsyncImageLoader(); // 锟届步锟斤拷取图片

			convertView = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.itemfriends, null);
			userName = (TextView) convertView.findViewById(R.id.username);
			userImage = (ImageView) convertView.findViewById(R.id.userimage);

			FriendInfo ui = new FriendInfo();

			ui = userInfoList.get(position);
			if (ui != null) {
				convertView.setTag(ui.getId());
				userName.setText(ui.getName());
				userImage.setImageResource(R.drawable.icon);

				Drawable cachedImage = asyncImageLoader.loadDrawable(ui
						.getHeadImg(), userImage, new ImageCallBack() {
					@Override
					// 锟斤拷锟斤拷锟斤拷锟斤拷写锟剿回碉拷锟接匡拷
					public void imageLoad(Drawable imageDrawable,
							ImageView imageView, String imageUrl) {
						imageView.setImageDrawable(imageDrawable);
					}
				});

				if (cachedImage == null) {
					// 锟斤拷锟矫伙拷锟酵计拷锟斤拷锟斤拷锟揭伙拷锟斤拷锟斤拷锟斤拷图片锟斤拷锟斤拷锟斤拷示
					userImage.setImageResource(R.drawable.icon);
				} else {
					// 锟斤拷锟斤拷锟酵计拷锟斤拷锟斤拷锟斤拷锟酵计?
					userImage.setImageDrawable(cachedImage);
				}
			}

			return convertView;
		}
	}
}
