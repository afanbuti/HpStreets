package com.limon.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.limon.make.BMapApi;

public class JsonDataPostApi {
	// private static final String BASE_URL =
	// "http://afanbutiphp.dns87.53nic.com/service/";
	// private static final String BASE_URL = WebDataGetApi.BASE_URL;
	public String makeRequest(String extpath, Map<?, ?> params) throws Exception {
		// map is similar to a dictionary or hash
		// instantiates httpclient to make request
		// DefaultHttpClient httpclient = new DefaultHttpClient();
		// url with the post data
		HttpPost httpost = new HttpPost(BMapApi.getInstance().getBaseUrl()
				+ extpath);

		// all the passed parameters from the post request
		// iterator used to loop through all the parameters passed in the post
		// request
		Iterator<?> iter = params.entrySet().iterator();
		// Stores JSON
		// JSONObject holder = new JSONObject();
		// using the eariler example your first entry would get email and the
		// inner while would get the value which would be 'foo@bar.com'
		// { fan: { email : 'foo@bar.com' } }
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		// While there is another entry
		while (iter.hasNext()) {
			// gets an entry in the params
			Map.Entry pairs = (Map.Entry) iter.next();
			// creates a key for Map
			String key = (String) pairs.getKey();
			String data = (String) pairs.getValue();
			// Create a new map
			// Map m = (Map)pairs.getValue();
			// object for storing Json
			// JSONObject data = new JSONObject();
			// gets the value
			/*
			 * Iterator iter2 = m.entrySet().iterator(); while(iter2.hasNext())
			 * { Map.Entry pairs2 = (Map.Entry)iter2.next();
			 * data.put((String)pairs2.getKey(), (String)pairs2.getValue()); }
			 */
			// puts email and 'foo@bar.com' together in map
			// holder.put(key, data);
			nameValuePairs.add(new BasicNameValuePair(key, data));
		}
		// passes the results to a string builder/entity
		// StringEntity se = new StringEntity(holder.toString());
		// sets the post request as the resulting string
		httpost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		// httpost.setEntity(se);
		// sets a request header so the page receving the request will know what
		// to do with it
		// httpost.setHeader("Accept", "application/json");
		// httpost.setHeader("Content-type", "application/json");
		// Handles what is returned from the page
		// ResponseHandler responseHandler = new BasicResponseHandler();
		// response = httpclient.execute(httpost, responseHandler);
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		HttpClient httpclient = new DefaultHttpClient(httpParameters);

		// 发送请求
		// HttpResponse httpResponse = new DefaultHttpClient().execute(httpost);
		// httpost.setHeader("ContentEncoding","UTF8");

		HttpResponse httpResponse = httpclient.execute(httpost);
		// 得到应答的字符串，这也是一个 JSON 格式保存的数据
		// String retSrc = EntityUtils.toString(httpResponse.getEntity());
		// return retSrc;
		// 生成 JSON 对象
		// JSONObject result = new JSONObject( retSrc);
		// String token = result.get("token");
		// return result.toString();
		// return "";

		HttpEntity entity = httpResponse.getEntity();
		InputStream is = entity.getContent();
		return ConvertStreamToString(is);
	}

	// 读取字符流
	private String ConvertStreamToString(InputStream is) {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String data = "";
		try {
			while ((data = br.readLine()) != null) {
				sb.append(data);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final String result = sb.toString();
		return result;
	}

}
