package com.limon.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.limon.bean.Parameter;

public class TencGetToken {
	private String urlStr = "";
	private String httpMethod = "";
	private String paramsStr = "";
	private String response = null;
	private static final int TIME_OUT = 1000 * 6;
	private static final String METHOD_POST = "POST";
	private static final String METHOD_GET = "GET";
	private static final int HTTP_OK = 200;
	private final String CHARTSET = "UTF-8";
	private final int BUFFER = 1024 * 8;

	public String httpGet(String urlStr, String paramsStr) throws Exception {

		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(urlStr);
		if (!StringUtils.isEmpty(paramsStr)) {
			urlBuilder.append("?");
			urlBuilder.append(paramsStr);
		}
		// Log.i("TencGetToken=", urlBuilder.toString());
		URL url = null;
		HttpURLConnection conn = null;
		InputStream inStream = null;
		String response = null;
		try {
			url = new URL(urlBuilder.toString());

			conn = (HttpURLConnection) url.openConnection();

			conn.setDoInput(true);
			conn.setConnectTimeout(TIME_OUT);
			conn.setRequestMethod(METHOD_GET);
			conn.setRequestProperty("accept", "*/*");

			conn.connect();

			int responseCode = conn.getResponseCode();
			if (responseCode == HTTP_OK) {

				inStream = conn.getInputStream();

				response = getResponse(inStream);
			} else {

				response = "" + responseCode;
			}
		} catch (Exception e) {
			throw e;
		} finally {

			conn.disconnect();
		}
		return response;
	}

	public String httpPost(String urlStr, String paramsStr) throws Exception {

		byte[] data = paramsStr.getBytes();
		URL url = null;
		HttpURLConnection conn = null;
		InputStream inStream = null;
		String response = null;
		try {
			url = new URL(urlStr);

			conn = (HttpURLConnection) url.openConnection();

			conn.setConnectTimeout(TIME_OUT);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod(METHOD_POST);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", CHARTSET);
			conn.setRequestProperty("Content-Length", String
					.valueOf(data.length));
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			conn.connect();
			DataOutputStream outputStream = new DataOutputStream(conn
					.getOutputStream());

			outputStream.write(data);

			outputStream.flush();
			outputStream.close();

			int responseCode = conn.getResponseCode();
			if (responseCode == HTTP_OK) {

				inStream = conn.getInputStream();

				response = getResponse(inStream);
			} else {

				response = "" + responseCode;
			}
		} catch (Exception e) {
			throw e;
		} finally {

			conn.disconnect();
		}
		return response;
	}

	public String postWithFile(String httpUrl, String queryString,
			List<Parameter> files) throws Exception {

		final String BOUNDARY = "---------------------------7da2137580612";

		final String RETURN = "\r\n";

		final String PREFIX = "--";

		HttpURLConnection conn = null;
		InputStream inStream = null;
		String response = null;
		try {
			URL url = new URL(httpUrl);

			conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod(METHOD_POST);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", CHARTSET);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + BOUNDARY);

			DataOutputStream out = new DataOutputStream(conn.getOutputStream());

			if (queryString != null && !queryString.equals("")) {

				String[] params = queryString.split("&");
				for (String str : params) {
					if (str != null && !str.equals("")) {
						if (str.indexOf("=") > -1) {
							String[] param = str.split("=");

							String value = (param.length == 2 ? StringUtils
									.decode(param[1]) : "");

							out.writeBytes(PREFIX + BOUNDARY + RETURN);

							out
									.writeBytes("Content-Disposition: form-data; name=\""
											+ param[0] + "\"" + RETURN);

							out.writeBytes(RETURN);

							out.write(value.getBytes(CHARTSET));

							out.writeBytes(RETURN);
						}
					}
				}
			}

			for (Parameter file : files) {

				String fileName = file.getValue().substring(
						file.getValue().lastIndexOf("/") + 1);

				out.writeBytes(PREFIX + BOUNDARY + RETURN);

				out.writeBytes("Content-Disposition: form-data; name=\""
						+ file.getName() + "\"; filename=\"" + fileName + "\""
						+ RETURN);

				out.writeBytes(RETURN);

				FileInputStream fis = new FileInputStream(file.getValue());

				byte[] buffer = new byte[BUFFER];
				int count = 0;
				while ((count = fis.read(buffer)) != -1) {
					out.write(buffer, 0, count);
				}
				fis.close();
				out.writeBytes(RETURN);
			}

			out.writeBytes(PREFIX + BOUNDARY + PREFIX + RETURN);
			out.flush();
			out.close();

			int responseCode = conn.getResponseCode();
			if (responseCode == HTTP_OK) {
				inStream = conn.getInputStream();

				response = getResponse(inStream);
			} else {
				response = "" + responseCode;
			}

		} catch (Exception e) {
			throw e;
		} finally {
			conn.disconnect();
		}
		return response;
	}

	private String getResponse(InputStream inStream) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int len = -1;
		byte[] buffer = new byte[BUFFER];
		while ((len = inStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, len);
		}
		byte[] data = outputStream.toByteArray();
		return new String(data);
	}

	public Map<String, String> getRequestToken() {
		urlStr = "https://open.t.qq.com/cgi-bin/request_token";
		httpMethod = "GET";
		Map<String, String> map = new HashMap<String, String>();
		try {

			paramsStr = OAuth.getPostParams(urlStr, httpMethod, "null");

			response = httpGet(urlStr, paramsStr);
			Log.i("TencGetToken=", "RequestToken" + response);

			map = StringUtils.splitResponse(response);
		} catch (Exception e) {
			Log.i("TencGetToken=", e.getMessage());
			e.printStackTrace();
		}
		return map;
	}

	public Map<String, String> getAccessToken(String oauthToken,
			String tokenSecret, String verifier) {
		urlStr = "https://open.t.qq.com/cgi-bin/access_token";
		httpMethod = "GET";
		Map<String, String> map = new HashMap<String, String>();
		try {

			paramsStr = OAuth.getPostParams(urlStr, httpMethod, null,
					oauthToken, tokenSecret, verifier);

			response = httpGet(urlStr, paramsStr);
			Log.i("TencGetToken=", "AccessToken" + response);

			map = StringUtils.splitResponse(response);
		} catch (Exception e) {
			Log.i("TencGetToken=", e.getMessage());
			e.printStackTrace();
		}
		return map;
	}

	public String add(String oauthToken, String tokenSecret, String format,
			String content, String clientIP) {
		return add(oauthToken, tokenSecret, format, content, clientIP, "", "");
	}

	public String add(String oauthToken, String tokenSecret, String format,
			String content, String clientIP, String jing, String wei) {
		urlStr = "http://open.t.qq.com/api/t/add";
		httpMethod = "POST";

		try {

			paramsStr = OAuth.getPostParams(urlStr, httpMethod, oauthToken,
					tokenSecret, content, format, clientIP, jing, wei);

			response = httpPost(urlStr, paramsStr);
		} catch (Exception e) {
			Log.i("TencGetToken=", e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	public String addWithPic(String oauthToken, String tokenSecret,
			String format, String content, String clientIP, String[] picsPath) {
		return addWithPic(oauthToken, tokenSecret, format, content, clientIP,
				"", "", picsPath);
	}

	public String addWithPic(String oauthToken, String tokenSecret,
			String format, String content, String clientIP, String jing,
			String wei, String[] picsPath) {
		String response = null;

		if (null != picsPath && picsPath.length > 0) {
			List<Parameter> pics = new ArrayList<Parameter>();

			for (String picPath : picsPath) {
				if (new File(picPath).exists()) {

					pics.add(new Parameter("pic", picPath));
				}
			}

			if (pics.size() > 0) {
				urlStr = "http://open.t.qq.com/api/t/add_pic";
				httpMethod = "POST";
				try {

					paramsStr = OAuth.getPostParams(urlStr, httpMethod,
							oauthToken, tokenSecret, content, format, clientIP,
							jing, wei);
					response = postWithFile(urlStr, paramsStr, pics);
				} catch (Exception e) {
					Log.i("TencGetToken=", e.getMessage());
					e.printStackTrace();
				}
			} else {
				response = add(oauthToken, tokenSecret, format, content,
						clientIP, jing, wei);
			}
		} else {
			response = add(oauthToken, tokenSecret, format, content, clientIP,
					jing, wei);
		}
		return response;
	}

}
