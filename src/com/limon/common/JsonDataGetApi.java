package com.limon.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.limon.make.BMapApi;

public class JsonDataGetApi extends WebDataGetApi {
	// private static final String BASE_URL =
	// "http://afanbutiphp.dns87.53nic.com/service/";
	// private static final String EXTENSION = "service/";
	// private static final String BASE_URL = WebDataGetApi.BASE_URL;
	public JSONObject getObject(String sbj) throws JSONException, Exception {
		return new JSONObject(getRequest(BMapApi.getInstance().getBaseUrl()
				+ sbj));
	}

	public JSONArray getArray(String sbj) throws JSONException, Exception {
		return new JSONArray(getRequest(BMapApi.getInstance().getBaseUrl()
				+ sbj));
	}

	public String getString(String sbj) throws JSONException, Exception {
		return getRequest(BMapApi.getInstance().getBaseUrl() + sbj);
	}

	public JSONObject getObjectByUrl(String url) throws JSONException,
			Exception {
		return new JSONObject(getRequest(url));
	}
}
