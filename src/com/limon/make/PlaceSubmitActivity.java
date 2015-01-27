package com.limon.make;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.limon.bean.LocationInfo;
import com.limon.common.InfoHelper;
import com.limon.common.JsonDataPostApi;
import com.limon.common.ResourceUtil;
import com.limon.common.StringUtils;
import com.limon.location.BestLocationListener;
import com.mobclick.android.MobclickAgent;

/**
 * 地点注册
 * 
 * @version 1.0
 */
public class PlaceSubmitActivity extends BaseActivity {
	private Context mContext;
	private EditText addrEdit = null;
	private EditText nameEdit = null;
	private EditText phoneEdit = null;
	private Button regbtn = null;
	private Spinner cateSpinner = null;
	private ProgressDialog progressDialog = null;
	private String lat, lon;
	private String res = "-1";
	private Button rButton;
	// private String uname = "";
	private String userId = "";
	private BestLocationListener mLocationListener = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submitplace);
		mContext = getApplicationContext();

		// Debug.startMethodTracing("test");

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(PlaceSubmitActivity.this);
		userId = settings.getString("UserID", "");
		// uname = settings.getString("UserName", "");
		if ("".equals(userId)) {
			Toast.makeText(mContext,
					ResourceUtil.getString(R.string.info_nologin),
					Toast.LENGTH_LONG).show();
			startActivity(new Intent(this, LogInActivity.class));
			finish();
			unregisterReceiver(exitre);
		}
		BMapApi app = (BMapApi) this.getApplication();
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new BMapApi.MyGeneralListener());
		}
		// app.mBMapMan.getLocationManager().requestLocationUpdates(
		// mLocationListener);
		app.mBMapMan.start();
		addrEdit = (EditText) findViewById(R.id.AddressEditText);
		cateSpinner = (Spinner) findViewById(R.id.CategorySpinner);
		nameEdit = (EditText) findViewById(R.id.NameEditText);
		phoneEdit = (EditText) findViewById(R.id.PhoneEditText);
		regbtn = (Button) findViewById(R.id.AddVenueButton);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.add(ResourceUtil.getString(R.string.venues_cate0));
		adapter.add(ResourceUtil.getString(R.string.venues_cate1));
		adapter.add(ResourceUtil.getString(R.string.venues_cate2));
		adapter.add(ResourceUtil.getString(R.string.venues_cate3));
		adapter.add(ResourceUtil.getString(R.string.venues_cate4));
		adapter.add(ResourceUtil.getString(R.string.venues_cate5));
		adapter.add(ResourceUtil.getString(R.string.venues_cate6));
		adapter.add(ResourceUtil.getString(R.string.venues_cate7));
		adapter.add(ResourceUtil.getString(R.string.venues_cate8));
		adapter.add(ResourceUtil.getString(R.string.venues_cate9));
		this.cateSpinner.setAdapter(adapter);
		this.cateSpinner.setSelection(0);
		regbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!InfoHelper.checkNetWork(mContext)) {
					Toast.makeText(mContext,
							ResourceUtil.getString(R.string.error_net),
							Toast.LENGTH_LONG).show();
				} else {
					regAction();
				}
			}
		});
		rButton = (Button) findViewById(R.id.ReturnButton);
		rButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				unregisterReceiver(exitre);
			}
		});
		mLocationListener = new BestLocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					lat = "" + location.getLongitude();
					lon = "" + location.getLatitude();
				}

			}

		};
		// Debug.stopMethodTracing();
	}

	@Override
	protected void onPause() {
		BMapApi app = (BMapApi) this.getApplication();
		app.mBMapMan.stop();
		super.onPause();
	}

	@Override
	protected void onResume() {
		BMapApi app = (BMapApi) this.getApplication();
		app.mBMapMan.start();
		super.onResume();
	}


	// @Override
	// protected void onPause() {
	// BMapApi app = (BMapApi) this.getApplication();
	// app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);
	// app.mBMapMan.stop();
	// super.onPause();
	// }
	//
	// @Override
	// protected void onResume() {
	// BMapApi app = (BMapApi) this.getApplication();
	// // 注锟结定位锟铰硷拷锟斤拷锟斤拷位锟襟将碉拷图锟狡讹拷锟斤拷锟斤拷位锟斤拷
	// app.mBMapMan.getLocationManager().requestLocationUpdates(
	// mLocationListener);
	// app.mBMapMan.start();
	// super.onResume();
	// }
	private void regAction() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				progressDialog.dismiss();
				if (!("-1".equals(res))) {
					addrEdit.setText("");
					nameEdit.setText("");
					phoneEdit.setText("");
					cateSpinner.setSelection(0);
					Toast
							.makeText(
									mContext,
									ResourceUtil.getString(R.string.venues_ok)
											+ String
													.format(
															"\n"
																	+ ResourceUtil
																			.getString(R.string.info_credit),
															"15", "15"),
									Toast.LENGTH_LONG).show();
				}
			}
		};
		progressDialog = ProgressDialog.show(this, "", ResourceUtil
				.getString(R.string.execute), true, false);
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				if (isValid()) {
					signUp();
				}
				Message message = new Message();
				handler.sendMessage(message);
				Looper.loop();
			}
		}.start();
	}

	private boolean isValid() {
		boolean bool = TextUtils.isEmpty(this.nameEdit.getText());
		if (!bool) {
		} else {
			Toast.makeText(mContext,
					ResourceUtil.getString(R.string.error_venuesname),
					Toast.LENGTH_LONG).show();
		}
		return !bool;
	}

	private void signUp() {
		// Log.d("LAT", "" + lat);
		// Log.d("LON", "" + lon);
		LocationInfo account = new LocationInfo();
		account.setAddr(this.addrEdit.getText().toString());
		account.setName(this.nameEdit.getText().toString());
		account.setTel(this.phoneEdit.getText().toString());
		account.setLat(lat);
		account.setLon(lon);
		account.setGuid(StringUtils.shortRadomGUID());
		account.setCate(this.cateSpinner.getSelectedItem().toString());
		account.setUid(userId);
		// Log.d("this.cateSpinner.getSelectedItem().toString()",this.cateSpinner.getSelectedItem().toString());
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		String sJson = gson.toJson(account);
		sJson = StringUtils.encode(sJson);
		// Log.d("sJson:", sJson);
		Map<String, String> params = new HashMap<String, String>();
		params.put("u", sJson);

		JsonDataPostApi api = new JsonDataPostApi();
		try {
			res = api.makeRequest("l.ashx?op=r", params);
		} catch (Exception e) {
			MobclickAgent.reportError(mContext, "PlaceSubmit_signUp:"
					+ e.toString());
		}
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(PlaceSubmitActivity.this);
		Editor editor = sharedPreferences.edit();
		editor.putInt("Credit", sharedPreferences.getInt("Credit", 0) + 15);
		editor.putInt("Experience",
				sharedPreferences.getInt("Experience", 0) + 15);
		editor.commit();
	}
}