package com.limon.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class RenrenGetToken {
	public static final String TAG = "HttpPost";
	URL url = null;
	HttpURLConnection httpurlconnection = null;

	public String refresh_token;
	String scope;
	String session_key;
	public static String RENREN_API_KEY = "2f823773a2f647deae19f0710c79ec7f";
	public static String RENREN_SECRET = "723a610c4cde485ea046150a55740bdc";
	public static String code = "";
	public static String userid = "";
	public static String access_token = "";
	public static String expires_in = "";
	public static String SESSION_KEY = "";

	public void getAccessToken() {

		try {
			url = new URL("https://graph.renren.com/oauth/token?");

			httpurlconnection = (HttpURLConnection) url.openConnection();
			httpurlconnection.setDoOutput(true);
			httpurlconnection.setRequestMethod("POST");
			String param1 = "grant_type=authorization_code";
			String param2 = "&code=" + code;
			// String param3 = "&client_id=a4f9b6beaf874b59897c2ac29f3d81c4";
			// String param4 =
			// "&client_secret=2cd503937e7f49e7b7eac67c00544949";
			String param3 = "&client_id=" + RENREN_API_KEY;
			String param4 = "&client_secret=" + RENREN_SECRET;
			String param5 = "&redirect_uri=http://graph.renren.com/oauth/login_success.html"; // 此参数不能改变，必须和请求code时的回调url一致
			httpurlconnection.getOutputStream().write(
					(param1 + param2 + param3 + param4 + param5).getBytes());

			httpurlconnection.getOutputStream().flush();
			httpurlconnection.getOutputStream().close();
			int statusCode = httpurlconnection.getResponseCode();

			// 状态码返回200代表成功
			if (200 == statusCode) {
				try {
					InputStream is = httpurlconnection.getInputStream();
					Reader reader = new BufferedReader(
							new InputStreamReader(is), 4000);
					// int len =con.getContentLength(); 执行这一句时就出错, 把这一句删掉
					// 程序就顺利运行
					// 如果这个文件是流形式传递，不能得到其大小。
					// HTTP header里有content length。 stream 流除外
					// Log.i(TAG,
					// "httpurlconnection.getResponseMessage()="+httpurlconnection.getResponseMessage());
					StringBuilder buffer = new StringBuilder();
					try {
						char[] tmp = new char[1024];
						int l;
						while ((l = reader.read(tmp)) != -1) {
							buffer.append(tmp, 0, l);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						reader.close();
					}
					String string = buffer.toString();
					JSONObject jb = new JSONObject(string);
					access_token = jb.getString("access_token");
					expires_in = jb.getString("expires_in");
					refresh_token = jb.getString("refresh_token");
					// scope = jb.getString("scope"); // org.json.JSONException:
					// No value for scope

				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			// InputStream in = null;
			// in = httpurlconnection.getInputStream();
			// byte b[] = new byte[512];
			// int len = 0;
			// int temp = 0;
			// int count = 0; // 所有读取的内容都使用temp接收
			// while ((temp = in.read()) != -1) { //当没有读取完时，继续读取
			// b[len] = (byte) temp;
			// len++;
			// count++;
			// }
			//
			// String content = new String(b).trim();

			// Log.i(TAG, "返回的状态码 = " + Integer.toString(statusCode));
			// Log.i(TAG, "acesstoken = " + new String(b));
			// Log.i(TAG, "解析返回后的JSON为字符串后的值： = " + content);

			// String[] cn = content.split(",");
			// String access = cn[0].split(":")[1].replaceAll("\"", "").trim();
			// //08-10 20:29:48.189: WARN/System.err(277):
			// java.lang.StringIndexOutOfBoundsException
			// String experise = cn[1].split(":")[1];
			// experise = experise.substring(0, experise.lastIndexOf("\n"));

			// access_token = access_token;
			// expires_in = expires_in;

			Log.i(TAG, "access_token = " + access_token);
			// access_token =
			// 147396|6.b1a5beab94fc6a1efdd8dcde388e6b5c.2592000.1315638000-244248724
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpurlconnection != null)
				httpurlconnection.disconnect();
		}

		getSessionKey();
	}

	public void getSessionKey() {
		try {
			url = new URL("https://graph.renren.com/renren_api/session_key?");

			httpurlconnection = (HttpURLConnection) url.openConnection();
			httpurlconnection.setDoOutput(true);
			httpurlconnection.setRequestMethod("POST");
			String param1 = "oauth_token=" + access_token; // 建议对oauth_token进行url编码后再调用

			httpurlconnection.getOutputStream().write(param1.getBytes());
			httpurlconnection.getOutputStream().flush();
			httpurlconnection.getOutputStream().close();
			int statusCode = httpurlconnection.getResponseCode();

			// 状态码返回200代表成功
			if (200 == statusCode) {
				try {
					InputStream is = httpurlconnection.getInputStream();
					Reader reader = new BufferedReader(
							new InputStreamReader(is), 4000);
					// int len =con.getContentLength(); 执行这一句时就出错, 把这一句删掉
					// 程序就顺利运行
					// 如果这个文件是流形式传递，不能得到其大小。
					// HTTP header里有content length。 stream 流除外
					// Log.i(TAG,
					// "httpurlconnection.getResponseMessage()="+httpurlconnection.getResponseMessage());
					StringBuilder buffer = new StringBuilder();
					try {
						char[] tmp = new char[1024];
						int l;
						while ((l = reader.read(tmp)) != -1) {
							buffer.append(tmp, 0, l);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						reader.close();
					}
					String string = buffer.toString();
					JSONObject value = new JSONObject(string);
					// 先获取数组中的三个大的选项
					String renrenToken = value.getString("renren_token");
					@SuppressWarnings("unused")
					String oauth_token = value.getString("oauth_token");
					String userToken = value.getString("user");
					// 解析第一个renren_token,并获取其中的三个值
					JSONObject renren_token = new JSONObject(renrenToken);
					session_key = renren_token.getString("session_key");
					@SuppressWarnings("unused")
					String expires_in = renren_token.getString("expires_in");
					@SuppressWarnings("unused")
					String session_secret = renren_token
							.getString("session_secret");
					// 第二个不用解析，不是数组
					// 第三个，并获取userId
					JSONObject user = new JSONObject(userToken);
					String userId = user.getString("id");
					Log.i(TAG, "" + userToken + "   " + user + "   " + userId);
					userid = userId;
					SESSION_KEY = session_key;

					// 这里不能用数组进行解析，原因是第二个参数不是数组，如下所示：
					// {
					// "renren_token":{
					// "session_secret":"6710946aed8cd28b97333f2109bb68e9",
					// "expires_in":2594255,
					// "session_key":"6.b1a5beab94fc6a1efdd8dcde388e6b5c.2592000.1315638000-244248724"
					// },
					// "oauth_token":"147396|6.b1a5beab94fc6a1efdd8dcde388e6b5c.2592000.1315638000-244248724",
					// "user":{
					// "id":244248724
					// }
					// }
					// JSONArray data = new JSONArray(string);
					// for(int i = 0; i < data.length(); i++){
					// JSONObject renren_token = data.getJSONObject(i);
					// Log.i(TAG, "renren_token = "+renren_token.toString());
					// if(renren_token != null){
					// String session_secret =
					// renren_token.getString("session_secret");
					// String expires_in = renren_token.getString("expires_in");
					// String session_key =
					// renren_token.getString("session_key");
					// Log.i(TAG, "验证：" +
					// session_secret+"   "+expires_in+"   "+session_key);
					// }
					// }

				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			//			
			// InputStream in = null;
			// in = httpurlconnection.getInputStream();
			// byte b[] = new byte[1024];
			// int len = 0;
			// int temp = 0;
			// int count = 0;
			// while ((temp = in.read()) != -1) {
			// b[len] = (byte) temp;
			// len++;
			// count++;
			// }
			//
			// String content = new String(b).trim();
			// //Log.i(TAG, "statusCode = " + Integer.toString(statusCode));
			// Log.i(TAG, "acesstoken = " + new String(b));
			//			 
			// String id = content.substring(content.lastIndexOf(":") + 1,
			// content.length() - 2);
			// id = id.trim();
			// Util.userid = userId;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpurlconnection != null) {
				httpurlconnection.disconnect();
			}
		}

	}
}
