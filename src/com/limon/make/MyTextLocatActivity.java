package com.limon.make;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.baidu.mapapi.BMapManager;
import com.limon.common.JsonDataGetApi;
import com.limon.common.StringUtils;
import com.limon.location.BestLocationListener;
import com.limon.widget.PlacesAdapter;
import com.mobclick.android.MobclickAgent;

/**
 * �б��λ��
 * 
 * @author Administrator
 * 
 */
public class MyTextLocatActivity extends BaseActivity {

	private BestLocationListener mLocationListener = null;
	private ListView mListView = null;
	// private Handler mHandler = null;
	// private Runnable updateui = null;
	// private ProgressDialog progressDialog = null;
	private PlacesAdapter adapter = null;
	private List<Map<String, Object>> lInfos = null;
	private JsonDataGetApi api = null;
	// private JSONObject json = null;
	// private JSONArray jsonArray = null;
	final private int MSG_TIMER = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listplaces);
		mContext = getApplicationContext();
		BMapApi app = (BMapApi) this.getApplication();
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new BMapApi.MyGeneralListener());
		}
		app.mBMapMan.start();
		mListView = (ListView) findViewById(R.id.listView);
		lInfos = new ArrayList<Map<String, Object>>();
		/*
		 * adapter = new SimpleAdapter(mContext, lInfos, R.layout.place_item,
		 * new String[] { "name", "addr", "dist" }, new int[] { R.id.name,
		 * R.id.addr, R.id.dist });
		 */
		// mListView.setAdapter(adapter);
		// ע�ᶨλ�¼�
		mLocationListener = new BestLocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					getSomething("" + location.getLongitude(), ""
							+ location.getLatitude());
				}
			}
			//
			// @Override
			// public void onStatusChanged(String provider, int status,
			// Bundle extras) {
			// switch (status) {
			// case LocationProvider.AVAILABLE:
			// Log.d("x", "GPS available again\n");
			// break;
			// case LocationProvider.OUT_OF_SERVICE:
			// Log.d("x", "GPS out of service\n");
			// break;
			// case LocationProvider.TEMPORARILY_UNAVAILABLE:
			// Log.d("x", "GPS temporarily unavailable\n");
			// break;
			// }
			// }
			//
			// @Override
			// public void onProviderEnabled(String provider) {
			// Log.d("", "GPS Provider Enabled\n");
			// }
			//
			// @Override
			// public void onProviderDisabled(String provider) {
			// Log.d("", "GPS Provider Disabled\n");
			// }
		};

	}

	// private void submitSomething(final JSONArray jsonArray) {
	// api = new JsonDataGetApi();
	//
	// final Handler handler = new Handler() {
	// public void handleMessage(Message message) {
	// // imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
	// }
	// };
	// new Thread() {
	// @Override
	// public void run() {
	// Looper.prepare();
	// try {
	// for (int i = 0; i < jsonArray.length(); i++) {
	// json = (JSONObject) jsonArray.opt(i);
	// Log.d("xxx:", json.toString());
	// if ("none".equals(api.getString("l.ashx?op=i&id="
	// + json.getString("guid")))) {
	// submitLocation(json);
	// }
	//
	// }
	// } catch (JSONException e) {
	// Log.d("getLocationResult1:", e.toString());
	// } catch (Exception e) {
	// Log.d("getLocationResult2:", e.toString());
	// }
	// Message message = new Message();
	// // message.what=1;
	// handler.sendMessage(message);
	// Looper.loop();
	// }
	// }.start();
	// }

	// private void submitLocation(JSONObject json) {
	//
	// LocationInfo lInfo = new LocationInfo();
	// try {
	// lInfo.setGuid(json.getString("guid"));
	// lInfo.setName(json.getString("name"));
	// lInfo.setAddr(json.getString("addr"));
	// lInfo.setTel(json.getString("tel"));
	// lInfo.setLon(json.getDouble("lon"));
	// lInfo.setLat(json.getDouble("lat"));
	// } catch (JSONException e) {
	// Log.d("submitLocation1:", e.toString());
	// }
	// try {
	// JSONObject cate=null;
	// JSONArray cates = json.getJSONArray("categories");
	// //Log.d("cates:",""+cates.length());
	// if(cates!=null && cates.length()>0){
	// cate = (JSONObject) cates.opt(0);
	// lInfo.setCate(cate.getString("name"));
	// }
	// } catch (JSONException e1) {
	// Log.d("JSONArray cates:", e1.toString());
	// }
	// GsonBuilder gsonb = new GsonBuilder();
	// Gson gson = gsonb.create();
	// String sJson = gson.toJson(lInfo);
	//
	// Map<String, String> params = new HashMap<String, String>();
	// sJson = StringUtils.encode(sJson);
	// params.put("u", sJson);
	// // Log.d("sJson:", sJson);
	// JsonDataPostApi api = new JsonDataPostApi();
	// try {
	// api.makeRequest("l.ashx?op=r", params);
	// // Log.d("submitLocation=:", "l.ashx?op=r"+json.getString("guid"));
	// } catch (Exception e) {
	// Log.d("submitLocation2:", e.toString());
	// }
	// }

	@Override
	protected void onPause() {
		BMapApi app = (BMapApi) this.getApplication();
		app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);
		app.mBMapMan.stop();
		super.onPause();
	}

	@Override
	protected void onResume() {
		BMapApi app = (BMapApi) this.getApplication();
		app.mBMapMan.getLocationManager().requestLocationUpdates(
				mLocationListener);
		app.mBMapMan.start();
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	private void getSomething(final String lon, final String lat) {
		LocationListTask task = new LocationListTask(this);
		task.execute(lon, lat);
		// progressDialog = ProgressDialog.show(getParent(), "",
		// ResourceUtil.getString(R.string.process), true, false);
		// final Handler handler = new Handler() {
		// public void handleMessage(Message message) {
		// // JSONArray jsonAr = null;
		// adapter = new SimpleAdapter(mContext, lInfos,
		// R.layout.itemplaces, new String[] { "name", "addr",
		// "dist" }, new int[] { R.id.name, R.id.addr,
		// R.id.dist });
		//
		// mListView.setAdapter(adapter);
		// progressDialog.dismiss();
		//
		// }
		// };
		// new Thread() {
		// @Override
		// public void run() {
		// Looper.prepare();
		// getLocationResult(lon, lat);
		// Message message = new Message();
		// // message.what=0;
		// // message.obj=jsonArray;
		// handler.sendMessage(message);
		// Looper.loop();
		// }
		// }.start();
	}

	private void getLocationResult(String lon, String lat) {
		api = new JsonDataGetApi();
		lInfos = new ArrayList<Map<String, Object>>();
		JSONObject json = null;
		Map<String, Object> map = null;
		// JSONObject jobj = null;

		// �ӱ�����ݿ�ȡ10��λ��¼
		JSONArray jobhp = null;
		try {
			jobhp = api.getArray(String.format(
					"l.ashx?op=e&lat=%s&lon=%s&count=10", lat, lon));
			// Log.d("getObjectByUrl:jobhp", jobhp.toString());
		} catch (Exception e) {
			MobclickAgent.reportError(mContext,
					"MyTextLocat_getLocationResult:jobhp:" + e.toString());
			// Log.d("getLocationResult:jobhp", e.toString());
		}
		// ����ȡ���ı��ؼ�¼
		try {
			for (int i = 0; i < jobhp.length(); i++) {
				json = (JSONObject) jobhp.opt(i);
				map = new HashMap<String, Object>();
				map.put("guid", json.getString("Guid"));
				map.put("name", json.getString("Name").replace("??", ""));
				map.put("addr", json.getString("Addr").replace("??", ""));
				map.put("dist", (StringUtils
						.iround(json.getDouble("Dist") * 1000))
						+ " m");
				map.put("tel", json.getString("Tel"));
				map.put("cate", json.getString("Cate"));
				map.put("redoc", json.getString("Redoc"));
				lInfos.add(map);
			}
		} catch (Exception e) {
			MobclickAgent.reportError(mContext,
					"MyTextLocat_getLocationResult:lInfos.add:" + e.toString());
			// Log.d("getLocationResult:", e.toString());
		}

		// ����վȡ10��λ��¼
		// try {
		// jobj = api
		// .getObjectByUrl(String
		// .format(
		// "http://api.jiepang.com/v1/locations/search?lat=%s&lon=%s&count=10&source=100182",
		// lat, lon));
		// // jobj =
		// //
		// api.getObjectByUrl("http://api.jiepang.com/v1/locations/search?lat=39.916&lon=116.393&count=1&source=100182");
		// // Log.d("getObjectByUrl:jobj", jobj.toString());
		// } catch (Exception e) {
		// Log.d("getLocationResult:jobj", e.toString());
		// }
		// ����ȡ������վ��¼
		// try {
		// // JSONObject jsonObject = jobj.getJSONObject("items");
		// jsonArray = jobj.getJSONArray("items");
		//
		// for (int i = 0; i < jsonArray.length(); i++) {
		// json = (JSONObject) jsonArray.opt(i);
		// //Log.d("json=", json.toString());
		// map = new HashMap<String, Object>();
		// map.put("guid", json.getString("guid"));
		// map.put("name", json.getString("name"));
		// map.put("addr", json.getString("addr"));
		// map.put("dist", json.getString("dist"));
		// map.put("tel", json.getString("tel"));
		// map.put("redoc", json.getString("Redoc"));// ������
		// try {
		// JSONObject cate = null;
		// JSONArray cates = json.getJSONArray("categories");
		// // Log.d("cates:",""+cates.length());
		// if (cates != null && cates.length() > 0) {
		// cate = (JSONObject) cates.opt(0);
		// Log.d("cates:",""+cate.getString("name"));
		// map.put("cate",cate.getString("name"));
		// // }else{
		// // map.put("cate","");
		// }
		// } catch (JSONException e1) {
		// Log.d("JSONArray cates:", e1.toString());
		// }
		// lInfos.add(map);
		// }
		// } catch (Exception e) {
		// Log.d("getLocationResult:", e.toString());
		// }

		// submitSomething(jsonArray);

	}

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_TIMER:
				// getSomething();
				break;
			}
		}
	};

	class LocationListTask extends AsyncTask<String, Integer, String> {

		public LocationListTask(Context context) {
			// progressDialog = ProgressDialog.show(getParent(), "",
			// mContext.getResources()
			// .getString(R.string.process), true, false);
		}

		@Override
		protected String doInBackground(String... params) {
			// Looper.prepare();
			getLocationResult(params[0], params[1]);
			// Looper.loop();
			return null;

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {

			adapter = new PlacesAdapter(mContext, lInfos, R.layout.itemplaces,
					new String[] { "name", "addr", "dist", "redoc" },
					new int[] { R.id.name, R.id.addr, R.id.dist, R.id.redoc });

			mListView.setAdapter(adapter);
			mListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View v, int index,
						long arg3) {
					String guid = ((String) lInfos.get(index).get("guid") == null) ? ""
							: (String) lInfos.get(index).get("guid");
					String name = ((String) lInfos.get(index).get("name") == null) ? ""
							: (String) lInfos.get(index).get("name");
					String cate = ((String) lInfos.get(index).get("cate") == null) ? ""
							: (String) lInfos.get(index).get("cate");
					Intent intent = new Intent();
					intent.setClass(mContext, PlaceCommentListActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("guid", guid);
					intent.putExtra("name", name);
					intent.putExtra("cate", cate);
					mContext.startActivity(intent);

				}
			});

			Message message = Message.obtain();
			message.what = MSG_TIMER;
			handler.sendEmptyMessage(message.what);
			// 60�����һ���б�
			// handler.sendMessageDelayed(message, 60000L);
			// progressDialog.dismiss();
			// Log.d("==","??");
		}

		@Override
		protected void onPreExecute() {
			// ����������������������ʾһ���Ի�������򵥴���
			// message.setText(R.string.task_started);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// ���½��
			// System.out.println(""+values[0]);
			// message.setText(""+values[0]);
			// pdialog.setProgress(values[0]);
		}

	}

}
