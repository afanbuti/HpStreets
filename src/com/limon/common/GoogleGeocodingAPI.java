package com.limon.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.limon.bean.GoogleResult;
import com.limon.bean.GoogleResult.Address_components;
import com.limon.bean.GoogleResult.Results;
import com.mobclick.android.MobclickAgent;

public class GoogleGeocodingAPI {

	//根据经纬度获取地址信息
	public static String getLocationInfo(Context mContext,String lat,String lng) {
		String str="";
		try {
			HttpPost httppost = new HttpPost(
					"http://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lng+"&sensor=true&language=zh-CN&region=cn");
			HttpClient client = new DefaultHttpClient();
			HttpResponse response;
			//stringBuilder = new StringBuilder();

			response = client.execute(httppost);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
//			int b;
//			while ((b = stream.read()) != -1) {
//				stringBuilder.append((char) b);
//			}
			str=ConvertStreamToString(is);
		} catch (ClientProtocolException e) {
			MobclickAgent.reportError(mContext,	"ClientProtocolException000:" + e.toString());
		} catch (IOException e) {
			MobclickAgent.reportError(mContext,	"IOException000:" + e.toString());
		}
		//Log.d("stringBuilder.toString()=",str);
//		JSONObject jsonObject = new JSONObject();
//		try {
//			jsonObject = new JSONObject(str);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return str;
	}
	//根据地址获取地址信息
	public static String getLocationInfo(String address) {
		String str="";
		try {

			address = address.replaceAll(" ", "%20");

			HttpPost httppost = new HttpPost(
					"http://maps.google.com/maps/api/geocode/json?address="
							+ address + "&sensor=true&language=zh-CN&region=cn");
			HttpClient client = new DefaultHttpClient();
			HttpResponse response;
			//stringBuilder = new StringBuilder();

			response = client.execute(httppost);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
//			int b;
//			while ((b = stream.read()) != -1) {
//				stringBuilder.append((char) b);
//			}
			str=ConvertStreamToString(is);
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

//		JSONObject jsonObject = new JSONObject();
//		try {
//			jsonObject = new JSONObject(str);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return str;
	}
	// 读取字符流
	private static String ConvertStreamToString(InputStream is) {
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
	public static String getAddress(Context mContext,String jsonObject) {

		String address="北京";
		Gson gson = new Gson();
		GoogleResult gr = new GoogleResult();
		//String s=JsonStrTrim(jsonObject).toString();
		//jsonObject="{\"results\" : [{\"address_components\" : [{\"long_name\" : \"50号\", \"short_name\" : \"50号\",        \"types\" : [ \"street_number\" ]            },            {\"long_name\" : \"西太康路\",\"short_name\" : \"西太康路\",\"types\" : [ \"route\" ]            },            {\"long_name\" : \"金水区\",\"short_name\" : \"金水区\",\"types\" : [ \"sublocality\", \"political\" ]            },            {\"long_name\" : \"郑州\",\"short_name\" : \"郑州\",\"types\" : [ \"locality\", \"political\" ]            },            {\"long_name\" : \"河南省\",\"short_name\" : \"河南省\",\"types\" : [ \"administrative_area_level_1\", \"political\" ]            },            {\"long_name\" : \"中国\",\"short_name\" : \"CN\",\"types\" : [ \"country\", \"political\" ]            },            {\"long_name\" : \"450053\",\"short_name\" : \"450053\",\"types\" : [ \"postal_code\" ]            }         ],         \"formatted_address\" : \"中国河南省郑州市金水区西太康路50号 邮政编码: 450053\",         \"types\" : [ \"street_address\" ] }],   \"status\" : \"OK\"}";
		//Log.d("s=",jsonObject);
		try {
			gr = gson.fromJson(jsonObject,GoogleResult.class);
		} catch (JsonSyntaxException e) {
			MobclickAgent.reportError(mContext,	"JsonSyntaxException000:" + e.toString());
		}
		if (gr!=null && gr.getStatus()!=null && "OK".equals(gr.getStatus())) {
			List<Results> rss = gr.getResults();
			if (rss.size() > 0) {
				Results rs = rss.get(0);
				address = rs.getFormatted_address();
				List<Address_components> ad = rs.getAddress_components();
				if (ad.size() > 0) {
					Address_components d;
					Iterator<Address_components> iad = ad.iterator();
					while (iad.hasNext()) {
						d = (Address_components) iad.next();
						String[] s = d.getTypes();
						if (s.length > 1 && "locality".equals(s[0])) {
							address = d.getLong_name();
							break;
						}
					}
				}
			}
		}
//		try {
//
//			address = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
//					.getJSONArray("address_components").getJSONObject(3).getString("long_name");
//
//
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}

		return address;
	}
}
