package com.limon.make;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.baidu.mapapi.BMapManager;
import com.baidu.mobads.AdSettings;
import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.limon.bean.LocationInfo;
import com.limon.bean.UserInfo;
import com.limon.common.AsyncImageLoader;
import com.limon.common.AsyncImageLoader.ImageCallBack;
import com.limon.common.GoogleGeocodingAPI;
import com.limon.common.JsonDataGetApi;
import com.limon.common.JsonDataPostApi;
import com.limon.common.ResourceUtil;
import com.limon.common.StringUtils;
import com.limon.location.BestLocationListener;
import com.limon.myweibo.AuthoSharePreference;
import com.limon.myweibo.MyWeiboManager;
import com.limon.myweibo.WeiboConstParam;
import com.mobclick.android.MobclickAgent;
import com.weibo.net.AccessToken;
import com.weibo.net.AsyncWeiboRunner.RequestListener;
import com.weibo.net.WeiboException;

/**
 * 我的地盘
 * 
 * @author Administrator
 * 
 */

public class MyInfoActivity extends BaseActivity implements RequestListener {
	private BestLocationListener mLocationListener = null;
	private JsonDataGetApi api = null;
	// private static final int REQ_SYSTEM_SETTINGS = 0;
	private ImageView imageView = null;
	private TextView nameView = null;
	private TextView levelView = null;
	private TextView pointView = null;
	private TextView goldView = null;
	private TextView progressView = null;
	private GridView gridview = null;
	private ProgressBar progressBar = null;
	private String uname = "", email = "";
	private String userId = "";
	private boolean isPublic = false;
	private int credit = 0, sex = 0, experience = 0, level = 0;
	private String headurl = "";
	private Drawable cachedImage = null;
	private String lat, lon;
	final private int MSG_TIMER = 0;
	//private static Geocoder geocoder;
	private int[] images = { R.drawable.db_news, R.drawable.db_friends,
			R.drawable.db_places, R.drawable.db_estates, R.drawable.db_badges,
			R.drawable.db_pm, R.drawable.db_journal, //R.drawable.db_nearby_info,
			R.drawable.db_preference, R.drawable.db_ting, //R.drawable.weath,
			R.drawable.db_vips, R.drawable.logow, R.drawable.db_other };
	private String[] texts = { ResourceUtil.getString(R.string.main_info),
			ResourceUtil.getString(R.string.main_friend),
			ResourceUtil.getString(R.string.main_home),
			ResourceUtil.getString(R.string.main_estates),
			ResourceUtil.getString(R.string.main_badges),
			ResourceUtil.getString(R.string.main_quiet),
			ResourceUtil.getString(R.string.main_journal),
			//ResourceUtil.getString(R.string.main_nearby),
			ResourceUtil.getString(R.string.main_content),
			ResourceUtil.getString(R.string.main_msctts),
			//ResourceUtil.getString(R.string.main_weath),
			ResourceUtil.getString(R.string.main_umeng),
			ResourceUtil.getString(R.string.main_weibo),
			ResourceUtil.getString(R.string.main_other) };

	private List<Map<String, Object>> fillMap() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0, j = texts.length; i < j; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("images", images[i]);
			map.put("texts", texts[i]);
			list.add(map);
		}
		return list;
	}

	private MyWeiboManager mWeiboManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		setContentView(R.layout.myinfo);
		gridview = (GridView) findViewById(R.id.grid);
		nameView = (TextView) findViewById(R.id.user_details_name);
		levelView = (TextView) findViewById(R.id.user_details_level);
		pointView = (TextView) findViewById(R.id.user_details_points);
		goldView = (TextView) findViewById(R.id.user_details_gold);
		progressView = (TextView) findViewById(R.id.user_details_level_progress_text);
		imageView = (ImageView) findViewById(R.id.user_details_icon);
		progressBar = (ProgressBar) findViewById(R.id.user_details_level_progress);
		BMapApi app = (BMapApi) this.getApplication();
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new BMapApi.MyGeneralListener());
		}
		app.mBMapMan.start();
		SimpleAdapter adapter = new SimpleAdapter(this, fillMap(),
				R.layout.itemdashboard, new String[] { "texts", "images" },
				new int[] { R.id.text, R.id.icon });
		gridview.setAdapter(adapter);
		mWeiboManager = MyWeiboManager.getInstance();
		if (mWeiboManager == null) {
			mWeiboManager = MyWeiboManager.getInstance(
					WeiboConstParam.CONSUMER_KEY,
					WeiboConstParam.CONSUMER_SECRET,
					WeiboConstParam.REDIRECT_URL);
		}
		//geocoder = new Geocoder(this, Locale.CHINA);
		String token = AuthoSharePreference.getToken(this);
		if (!("".equals(token) || token == null)) {
			AccessToken accessToken = new AccessToken(token,
					WeiboConstParam.CONSUMER_SECRET);
			mWeiboManager.setAccessToaken(accessToken);
		}
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int index, long id) {
				if (index < 0 || index >= texts.length)
					return;
				Intent intent = null;
				View v = null;
				switch (index) {
				case 0:
					intent = new Intent(mContext, MainActivity.class);
					intent.putExtra("STARTING_TAB", 0);
					startActivity(intent);
					break;
				case 1:
					intent = new Intent(mContext, MainActivity.class);
					intent.putExtra("STARTING_TAB", 1);
					startActivity(intent);
					break;
				case 2:
					intent = new Intent(mContext, MainActivity.class);
					intent.putExtra("STARTING_TAB", 2);
					startActivity(intent);
					break;
				case 3:
					intent = new Intent();
					intent.setClass(mContext, RoutePlanActivity.class);
					v = GroupFour.group
							.getLocalActivityManager()
							.startActivity(
									"Four",
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
							.getDecorView();
					GroupFour.group.replaceView(v);
					break;
				case 4:
					intent = new Intent();
					intent.setClass(mContext, BadgesListActivity.class);
					v = GroupFour.group
							.getLocalActivityManager()
							.startActivity(
									"Four",
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
							.getDecorView();
					GroupFour.group.replaceView(v);
					break;
				case 5:
					intent = new Intent();
					intent.setClass(mContext, MyQuietActivity.class);
					v = GroupFour.group
							.getLocalActivityManager()
							.startActivity(
									"Four",
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
							.getDecorView();
					GroupFour.group.replaceView(v);
					break;
				case 6:
					intent = new Intent();
					intent.setClass(mContext, MyDoingListActivity.class);
					v = GroupFour.group
							.getLocalActivityManager()
							.startActivity(
									"Four",
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
							.getDecorView();
					GroupFour.group.replaceView(v);
					break;
//				case 7:
//					intent = new Intent();
//					intent.setClass(mContext, MyBlogListActivity.class);
//					v = GroupFour.group
//							.getLocalActivityManager()
//							.startActivity(
//									"Four",
//									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//							.getDecorView();
//					GroupFour.group.replaceView(v);
//					break;
				case 7:
					intent = new Intent();
					intent.setClass(mContext, MyContentListActivity.class);
					v = GroupFour.group
							.getLocalActivityManager()
							.startActivity(
									"Four",
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
							.getDecorView();
					GroupFour.group.replaceView(v);
					break;
				case 8:
					intent = new Intent();
					intent.setClass(mContext, TtsActivity.class);
					v = GroupFour.group
							.getLocalActivityManager()
							.startActivity(
									"Four",
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
							.getDecorView();
					GroupFour.group.replaceView(v);
					break;
//				case 9:
//					intent = new Intent();
//					intent.setClass(mContext, WeatherActivity.class);
//					v = GroupFour.group
//							.getLocalActivityManager()
//							.startActivity(
//									"Four",
//									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//							.getDecorView();
//					GroupFour.group.replaceView(v);
//					break;
				case 9:
					intent = new Intent();
					intent.setClass(mContext, AdActivity.class);
					v = GroupFour.group
							.getLocalActivityManager()
							.startActivity(
									"Four",
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
							.getDecorView();
					GroupFour.group.replaceView(v);
					break;
				case 10:
					createFri();
					startActivity(new Intent("android.intent.action.VIEW", Uri
							.parse("http://weibo.com/hpstreets")));
					/*
					 * intent = new Intent(); intent.setClass(mContext,
					 * WebViewShareActivity.class); intent.putExtra("url",
					 * "http://weibo.com/hpstreets");
					 * 
					 * v = GroupFour.group .getLocalActivityManager()
					 * .startActivity( "Four", intent
					 * .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
					 * .getDecorView(); GroupFour.group.replaceView(v);
					 */
					break;
				case 11:
					startActivity(new Intent("android.intent.action.VIEW", Uri
							.parse("http://www.hpstreets.com/other.aspx?v=h")));
					/*
					 * intent = new Intent(); intent.setClass(mContext,
					 * WebViewShareActivity.class); intent.putExtra("url",
					 * "http://www.hpstreets.com/other.aspx?v=h"); v =
					 * GroupFour.group .getLocalActivityManager()
					 * .startActivity( "Four", intent
					 * .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					 * .getDecorView(); GroupFour.group.replaceView(v);
					 */
					break;
				}
			}
		});
		// 注锟结定位锟铰硷拷
		mLocationListener = new BestLocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					lon = ""+location.getLongitude();
					lat = ""+location.getLatitude();
					submitMyLocation(lon, lat);
					
				}
			}
		};
		// ViewGroup rootLayout = (ViewGroup) findViewById(R.id.rootId);
		// ExchangeViewManager exchangeViewManager = new ExchangeViewManager();
		// exchangeViewManager.addView(this, rootLayout,
		// ExchangeConstants.type_small_handler_list_bottom);
		
		
	}

	private void updateWithNewLocation(final String lng, final String lat) {
		String mcityName = "北京";
//		double lat = 0;
//		double lng = 0;
//		//List<Address> addList = null;
//		if (location != null) {
//			lat = location.getLatitude();
//			lng = location.getLongitude();
//		} else {
//			// System.out.println("无法获取地理信息");
//			Toast.makeText(getApplicationContext(), "无法获取地理信息",
//					Toast.LENGTH_SHORT).show();
//		}
		//获取地点，暂时不用
		String jo=GoogleGeocodingAPI.getLocationInfo(mContext,lat, lng);
		MobclickAgent.reportError(mContext,	"updateWithNewLocation000:" + jo);
		mcityName=GoogleGeocodingAPI.getAddress(mContext,jo);
		MobclickAgent.reportError(mContext,	"updateWithNewLocation111:" + mcityName);
		//获取地点，暂时不用
		
		//mcityName=GoogleGeocodingAPI.reverseGeocode(lat, lng);
		//Log.d("mcityName=",mcityName);
		//JSONObject jo=GoogleGeocodingAPI.getLocationInfo("郑州");
		//GeoPoint gp = GoogleGeocodingAPI.getLatLong(jo);
		//Log.d("lat",""+gp.getLatitudeE6());
		//Log.d("lng",""+gp.getLongitudeE6());
		
//		try {
//			Toast.makeText(getApplicationContext(),
//					mcityName + "=" + lat + "=" + lng, Toast.LENGTH_SHORT)
//					.show();
//			addList = geocoder.getFromLocation(lat, lng, 1); // 解析经纬度
//			StringBuilder sb = new StringBuilder();
//			if (addList.size() > 0) {
//				Address address = addList.get(0);
//
//				for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
//					sb.append(address.getAddressLine(i)).append("\n");
//
//				sb.append(address.getLocality()).append("\n");
//				sb.append(address.getPostalCode()).append("\n");
//				sb.append(address.getCountryName());
//				Log.d("SB_cityName", sb.toString());
//			} else {
//				Log.d("NoSB_cityName", mcityName);
//			}
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if (addList != null && addList.size() > 0) {
//			for (int i = 0; i < addList.size(); i++) {
//				Address add = addList.get(i);
//				mcityName += add.getLocality();
//				Log.d("mcityName", mcityName);
//				Toast.makeText(getApplicationContext(), mcityName,
//						Toast.LENGTH_SHORT).show();
//			}
//		} else {
//			Log.d("NomcityName", mcityName);
//		}
//		if (mcityName.length() != 0) {
//			mcityName = mcityName.substring(0, (mcityName.length() - 1));
//		}
		
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putString("mcityName", mcityName);
		editor.commit();
		
		//Toast.makeText(getApplicationContext(), mcityName, Toast.LENGTH_SHORT).show();
	}

	
	private void createFri() {
		try {
			mWeiboManager.createFriend(mContext, "", this);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WeiboException e) {
			e.printStackTrace();
			Log.e("", "e.errcode = " + e.getStatusCode());
		}
	}

	// 锟斤拷取锟斤拷锟斤拷锟斤拷息锟斤拷锟狡斤拷锟斤拷
	private void getPersonInfo() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MyInfoActivity.this);
		userId = settings.getString("UserID", "");
		uname = settings.getString("UserName", "");
		email = settings.getString("Email", "");
		isPublic = settings.getBoolean(
				getResources().getString(R.string.preferences_public), true);
		credit = settings.getInt("Credit", 0);
		experience = settings.getInt("Experience", 0);
		sex = settings.getInt("Sex", 0);
		level = settings.getInt("Level", 0);
		headurl = settings.getString("HeadImg", "");
		nameView.setText(uname);
		levelView.setText("");
		pointView.setText(getResources().getString(R.string.info_cre) + credit);
		progressView.setText(level + "/100");
		goldView.setText(getResources().getString(R.string.info_exp)
				+ experience);
		progressBar.setProgress(level);

		if ("".equals(headurl)) {
			if (sex == 0)
				imageView.setImageResource(R.drawable.blank_girl);
			else
				imageView.setImageResource(R.drawable.blank_boy);
		} else {
			AsyncImageLoader asyncImageLoader = new AsyncImageLoader(); // 锟届步锟斤拷取图片
			cachedImage = asyncImageLoader.loadDrawable(BMapApi.getInstance()
					.getImageUrl() + headurl + ".thumb.jpg", imageView,
					new ImageCallBack() {
						@Override
						public void imageLoad(Drawable imageDrawable,
								ImageView imageView, String imageUrl) {
							imageView.setImageDrawable(imageDrawable);
						}
					});
			if (cachedImage == null) {
				if (sex == 0)
					imageView.setImageResource(R.drawable.blank_girl);
				else
					imageView.setImageResource(R.drawable.blank_boy);
			} else {
				imageView.setImageDrawable(cachedImage);
			}
		}
	}

	// 锟结交锟斤拷锟剿讹拷位锟斤拷息
	private void submitMyLocation(final String lon, final String lat) {
		SubmitLocationListTask task = new SubmitLocationListTask(this);
		task.execute(lon, lat);
		// final Handler handler = new Handler() {
		// public void handleMessage(Message message) {
		// }
		// };
		// new Thread() {
		// @Override
		// public void run() {
		// // Looper.prepare();
		// // 锟斤拷锟斤拷锟斤拷锟斤拷位锟矫ｏ拷锟斤拷锟节猴拷锟窖凤拷锟斤拷锟皆硷拷
		// if (isPublic) {
		// submitLocationResult(lon, lat);
		// }
		// Message message = new Message();
		// handler.sendMessage(message);
		// // Looper.loop();
		// }
		// }.start();
	}

	private void submitLocation(String lon, String lat) {
		api = new JsonDataGetApi();
		List<Map<String, Object>> lInfos = new ArrayList<Map<String, Object>>();
		JSONObject json = null;
		Map<String, Object> map = null;
		JSONObject jobj = null;
		JSONArray jsonArray = null;
		String sguid = "";
		// 锟斤拷锟斤拷站取30锟斤拷位锟斤拷录
		try {
			jobj = api
					.getObjectByUrl(String
							.format("http://api.jiepang.com/v1/locations/search?lat=%s&lon=%s&count=30&source=100182",
									lat, lon));
		} catch (Exception e) {
			Log.d("getLocationResult:jobj", e.toString());
		}
		// 锟斤拷锟斤拷取锟斤拷锟斤拷锟斤拷站锟斤拷录
		try {
			jsonArray = jobj.getJSONArray("items");
			for (int i = 0; i < jsonArray.length(); i++) {
				json = (JSONObject) jsonArray.opt(i);
				map = new HashMap<String, Object>();
				map.put("name", json.getString("name"));
				map.put("addr", json.getString("addr"));
				map.put("dist", json.getString("dist"));
				lInfos.add(map);
			}
		} catch (Exception e) {
			MobclickAgent.reportError(mContext,
					"MyInfo_getLocationResult:" + e.toString());
			// Log.d("getLocationResult:", e.toString());
		}

		for (int i = 0; i < jsonArray.length(); i++) {
			json = (JSONObject) jsonArray.opt(i);
			// Log.d("xxx:", json.toString());
			try {
				sguid = api.getString("l.ashx?op=i&id="
						+ json.getString("guid"));
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if ("none".equals(sguid)) {

				LocationInfo lInfo = new LocationInfo();
				try {
					lInfo.setGuid(json.getString("guid"));
					lInfo.setName(json.getString("name"));
					lInfo.setAddr(json.getString("addr"));
					lInfo.setTel(json.getString("tel"));
					lInfo.setLon(json.getString("lon"));
					lInfo.setLat(json.getString("lat"));
				} catch (JSONException e) {
					MobclickAgent.reportError(mContext,
							"MyInfo_getLocationResult:" + e.toString());
					// Log.d("submitLocation1:", e.toString());
				}
				try {
					JSONObject cate = null;
					JSONArray cates = json.getJSONArray("categories");
					// Log.d("cates:",""+cates.length());
					if (cates != null && cates.length() > 0) {
						cate = (JSONObject) cates.opt(0);
						lInfo.setCate(cate.getString("name"));
					}
				} catch (JSONException e1) {
					// Log.d("JSONArray cates:", e1.toString());
				}
				GsonBuilder gsonb = new GsonBuilder();
				Gson gson = gsonb.create();
				String sJson = gson.toJson(lInfo);

				Map<String, String> params = new HashMap<String, String>();
				sJson = StringUtils.encode(sJson);
				params.put("u", sJson);
				// Log.d("sJson:", sJson);
				JsonDataPostApi api = new JsonDataPostApi();
				try {
					api.makeRequest("l.ashx?op=r", params);
					// Log.d("submitLocation=:",
					// "l.ashx?op=r"+json.getString("guid"));
				} catch (Exception e) {
					MobclickAgent.reportError(mContext,
							"MyInfo_submitLocation:" + e.toString());
					// Log.d("submitLocation2:", e.toString());
				}
			}
		}
		//updateWithNewLocation(lon, lat);
		if (isPublic) {
			submitLocationResult(lon, lat);
		}
	}

	private void submitLocationResult(String lon, String lat) {
		api = new JsonDataGetApi();
		JsonDataPostApi postapi = new JsonDataPostApi();
		// JSONObject json =null;
		// try {
		// json =
		// api.getObjectByUrl(String.format("http://api.go2map.com/engine/api/translate/json?points=%s,%s&type=3",lon,lat));
		//
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// try {
		// // JSONObject jsonObject = jobj.getJSONObject("items");
		// JSONArray jsonArray = json.getJSONArray("points");
		//
		// if(jsonArray!=null && jsonArray.length()>0) {
		// json = (JSONObject) jsonArray.opt(0);
		// map.put("name", json.getString("name"));
		// map.put("addr", json.getString("addr"));
		//
		// }
		// } catch (Exception e) {
		// Log.d("getLocationResult:", e.toString());
		// }
		UserInfo u = new UserInfo();
		u.setId(userId);
		u.setEmail(email);
		u.setUname(uname);
		u.setSex(sex);
		u.setLat(lat);
		u.setLon(lon);
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		String usJson = gson.toJson(u);
		usJson = StringUtils.encode(usJson);
		// Log.d("user:", usJson);
		Map<String, String> params = new HashMap<String, String>();
		params.put("u", usJson);
		try {
			// 锟结交锟斤拷位锟斤拷锟?
			postapi.makeRequest("u.ashx?op=ul", params);
			// api.getString("u.ashx?op=ul&id=" + userId + "&lon=" + lon +
			// "&lat="
			// + lat + "&name=" + uname + "&sex=" + sex + "&email="
			// + email);
			// 锟斤拷取锟斤拷位锟届迹锟斤拷锟?锟剿版本锟斤拷时锟斤拷锟斤拷
			// api.getString("l.ashx?op=t&id=" + userId + "&lon=" + lon +
			// "&lat="
			// + lat + "&name=" + StringUtils.encode(uname));
			// Log.d("submitLocationResult", (String.format(
			// "u.ashx?op=ul&id=%s&lon=%s&lat=%s", userId, lon, lat)));
		} catch (Exception e) {
			Log.d("锟结交锟斤拷位锟斤拷息1", e.toString());
		}

		// TerminalInfo account = new TerminalInfo();
		// TelephonyManager phoneMgr = (TelephonyManager) this
		// .getSystemService(Context.TELEPHONY_SERVICE);
		// account.setBuildmodel(Build.MODEL);
		// account.setLine1number(phoneMgr.getLine1Number());
		// account.setUid(userId);
		// account.setVersionrelease(Build.VERSION.RELEASE);
		// account.setVersionsdk(Build.VERSION.SDK);
		// // GsonBuilder gsonb = new GsonBuilder();
		// // Gson gson = gsonb.create();
		// String sJson = gson.toJson(account);
		// sJson = StringUtils.encode(sJson);
		// // Log.d("phoneMgr:", sJson);
		// params = new HashMap<String, String>();
		// params.put("u", sJson);
		// try {
		// postapi.makeRequest("u.ashx?op=t", params);
		// } catch (Exception e) {
		// Log.d("锟结交锟斤拷位锟斤拷息2", e.toString());
		// }

	}

	// 锟斤拷莼锟饺★拷母锟斤拷锟斤拷锟较拷锟斤拷锟酵凤拷锟斤拷锟斤拷
	// private void guiAction() {
	// final Handler handler = new Handler() {
	// public void handleMessage(Message message) {
	// AsyncImageLoader asyncImageLoader = new AsyncImageLoader(); // 锟届步锟斤拷取图片
	// cachedImage = asyncImageLoader.loadDrawable(headurl, imageView,
	// new ImageCallback() {
	// @Override
	// // 锟斤拷锟斤拷锟斤拷锟斤拷写锟剿回碉拷锟接匡拷
	// public void imageLoaded(Drawable imageDrawable,
	// ImageView imageView, String imageUrl) {
	// imageView.setImageDrawable(imageDrawable);
	// }
	// });
	// if (cachedImage == null) {
	// // 锟斤拷锟矫伙拷锟酵计拷锟斤拷锟斤拷锟揭伙拷锟斤拷锟斤拷锟斤拷图片锟斤拷锟斤拷锟斤拷示
	// imageView.setImageResource(R.drawable.icon);
	// } else {
	// // 锟斤拷锟斤拷锟酵计拷锟斤拷锟斤拷锟斤拷锟酵计?
	// imageView.setImageDrawable(cachedImage);
	// }
	// }
	// };
	// new Thread() {
	// @Override
	// public void run() {
	// // 锟斤拷取头锟斤拷锟街?
	// headurl = "http://afanbutiphp.dns87.53nic.com/res/medal4.png";
	// // JsonDataGetApi api = new JsonDataGetApi();
	// // JSONObject json = null;
	// // try {
	// // json = api.getObject("u.ashx?op=u&id=" + userId);
	// // // Log.d("u.ashx?op=u&id=" + userId, json.toString());
	// // } catch (JSONException e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // } catch (Exception e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // }
	//
	// Message message = new Message();
	// // message.what=0;
	// // message.obj=jsonArray;
	// handler.sendMessage(message);
	//
	// }
	// }.start();
	// }

	// // 锟斤拷锟斤拷锟斤拷锟矫斤拷锟?
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// if (requestCode == REQ_SYSTEM_SETTINGS) {
	// // 锟斤拷取锟斤拷锟矫斤拷锟斤拷PreferenceActivity锟叫革拷锟斤拷Preference锟斤拷值
	// String pushnotification = getResources().getString(
	// R.string.preferences_push_notification);
	// String ispublic = getResources().getString(
	// R.string.preferences_public);
	// // 取锟斤拷锟斤拷锟斤拷锟斤拷锟接︼拷贸锟斤拷锟斤拷SharedPreferences
	// SharedPreferences settings = PreferenceManager
	// .getDefaultSharedPreferences(this);
	// // Boolean updateSwitch = settings.getBoolean(pushnotification,
	// // true);
	// Boolean iSpublic = settings.getBoolean(ispublic, true);
	// // String updateFrequency = settings.getString(updateFrequencyKey,
	// // "10");
	// // Log.v("CheckBoxPreference_Main", updateSwitch.toString());
	// if (iSpublic) {
	// guiAction("true");
	// } else {
	// guiAction("false");
	// }
	// guiAction();
	// // Log.v("ListPreference_Main", updateFrequency);
	// } else {
	// // 锟斤拷锟斤拷Intent锟斤拷锟截的斤拷锟?
	// }
	// }

	// // 锟斤拷锟斤拷锟角否公匡拷锟斤拷锟斤拷位锟斤拷
	// private void guiAction(final String ispublic) {
	// final Handler handler = new Handler() {
	// public void handleMessage(Message message) {
	// }
	// };
	// new Thread() {
	// @Override
	// public void run() {
	//
	// JsonDataGetApi api = new JsonDataGetApi();
	// // JSONObject json = null;
	// try {
	// api.getString("u.ashx?op=up&id=" + userId + "&pub="
	// + ispublic);
	// Log.d("u.ashx?op=u&id=" + userId, ispublic);
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// Message message = new Message();
	// // message.what=0;
	// // message.obj=jsonArray;
	// handler.sendMessage(message);
	//
	// }
	// }.start();
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
		getPersonInfo();
		super.onResume();
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

	class SubmitLocationListTask extends AsyncTask<String, Integer, String> {

		public SubmitLocationListTask(Context context) {
			// progressDialog = ProgressDialog.show(getParent(), "",
			// mContext.getResources()
			// .getString(R.string.process), true, false);
		}

		@Override
		protected String doInBackground(String... params) {
			// submitLocationResult(params[0], params[1]);
			submitLocation(params[0], params[1]);
			return null;

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			Message message = Message.obtain();
			message.what = MSG_TIMER;
			handler.sendEmptyMessage(message.what);
			// 60锟斤拷锟斤拷锟揭伙拷锟斤拷斜锟?
			// handler.sendMessageDelayed(message, 60000L);
			// progressDialog.dismiss();
			// Log.d("==","??");
		}

		@Override
		protected void onPreExecute() {
			// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷示一锟斤拷锟皆伙拷锟斤拷锟斤拷锟斤拷虻ゴ锟斤拷锟?
			// message.setText(R.string.task_started);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// 锟斤拷锟铰斤拷锟?
			// System.out.println(""+values[0]);
			// message.setText(""+values[0]);
			// pdialog.setProgress(values[0]);
		}

	}

	@Override
	public void onComplete(String response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onIOException(IOException e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(WeiboException e) {
		// TODO Auto-generated method stub

	}
}
