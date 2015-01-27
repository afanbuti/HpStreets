package com.limon.make;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.limon.bean.FriendInfo;
import com.limon.common.AsyncImageLoader;
import com.limon.common.JsonDataGetApi;
import com.limon.common.MediaUtils;
import com.limon.location.BestLocationListener;
import com.mobclick.android.MobclickAgent;

/**
 * ��ͼ�����
 * 
 * @author Administrator
 * 
 */
public class MyViewFriendsActivity extends BaseActivity {
	//private MapView mMapView = null;
	private BestLocationListener mLocationListener = null;// onResumeʱע���listener��onPauseʱ��ҪRemove
	//private MyLocationOverlay mLocationOverlay = null; // ��λͼ��
	private ProgressDialog progressDialog = null;
	private String ispublic, headurl;
	private String uname, uid;
	private String Lon, Lat;
	private String userId = "";
	private List<FriendInfo> userInfoList;
	// private AsyncImageLoader asyncImageLoader;
	// private Drawable cachedImage;
	// private ImageView userImage;
	final private int MSG_TIMER = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapviewfriends);
		mContext = getApplicationContext();
		/*BMapApi app = (BMapApi) this.getApplication();
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new BMapApi.MyGeneralListener());
		}
		app.mBMapMan.start();
		// ���ʹ�õ�ͼSDK�����ʼ����ͼActivity
		super.initMapActivity(app.mBMapMan);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.setBuiltInZoomControls(true);
		// ���������Ŷ��������Ҳ��ʾoverlay,Ĭ��Ϊ������
		mMapView.setDrawOverlayWhenZooming(true);

		// ��Ӷ�λͼ��
		mLocationOverlay = new MyLocationOverlay(this, mMapView);
		mLocationOverlay.enableMyLocation(); // ���ö�λ
		mLocationOverlay.enableCompass(); // ����ָ����
		mMapView.getOverlays().add(mLocationOverlay);*/
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MyViewFriendsActivity.this);
		userId = settings.getString("UserID", "");
		// userImage = (ImageView)findViewById(R.id.image);
		// getFriendList();
		getMyFriendsThread();
		// if (userInfoList != null && userInfoList.size()>0) {
		// addOverlays(userInfoList);
		// //Drawable marker = getResources().getDrawable(R.drawable.iconmarka);
		// // Log.d("getMyFriendsThread=", marker.toString());
		// //mMapView.getOverlays().add(
		// // new OverItemT(marker, MyViewFriendsActivity.this)); //
		// ���ItemizedOverlayʵ��mMapView
		// }

		// ע�ᶨλ�¼�
		mLocationListener = new BestLocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					/*GeoPoint pt = new GeoPoint(
							(int) (location.getLatitude() * 1e6),
							(int) (location.getLongitude() * 1e6));
					mMapView.getController().animateTo(pt);*/
					// getMyFriendsThread(location.getLongitude(), location
					// .getLatitude());
				}
			}
		};

	}

	private void getMyFriendsThread() {
		FriendListTask task = new FriendListTask(this);
		task.execute("");
	}

	private void addOverlays(List<FriendInfo> list) {
		//GeoPoint point;
		//OverItemT itemizedoverlay;
		Drawable drawable;

		Bitmap bitmap1 = null;
		//OverlayItem overlayitem;
		// List<Overlay> mapOverlays;
		// MyLocationOverlay myLocationOverlay;
		String img = BMapApi.getInstance().getImageUrl();

		// Bitmap bitmap = ((BitmapDrawable) ResourceUtil.getResources()
		// .getDrawable(R.drawable.pin_border)).getBitmap();
		// drawable = ResourceUtil.getResources()
		// .getDrawable(R.drawable.pin_border);
		Resources res = getResources();
		Bitmap bitmap = BitmapFactory
				.decodeResource(res, R.drawable.pin_border);
		// mapOverlays = mMapView.getOverlays();
		// Log.i("aaa","width"+bitmap.getWidth());
		for (int i = 0; i < list.size(); i++) {
			// Log.i("bbb",img+list.get(i).getHeadImg());
			if ("".equals(list.get(i).getHeadImg())) {
				if (list.get(i).getSex() == 0)
					// userImage.setImageResource(R.drawable.blank_girl);
					bitmap1 = BitmapFactory.decodeResource(res,
							R.drawable.blank_girl);
				else
					// userImage.setImageResource(R.drawable.blank_boy);
					bitmap1 = BitmapFactory.decodeResource(res,
							R.drawable.blank_boy);
			} else {
				bitmap1 = ((BitmapDrawable) AsyncImageLoader
						.loadImageFromUrl(img + list.get(i).getHeadImg()))
						.getBitmap();
				/*
				 * asyncImageLoader = new AsyncImageLoader(); cachedImage =
				 * asyncImageLoader
				 * .loadDrawable(img+list.get(i).getHeadImg(),userImage,
				 * //cachedImage =asyncImageLoader.loadDrawable(img+
				 * "1d072f3e31b54503939fddaa57787681.jpg",userImage, new
				 * ImageCallback() {
				 * 
				 * @Override public void imageLoaded(Drawable imageDrawable,
				 * ImageView userImage, String imageUrl) {
				 * //idrawable=imageDrawable;
				 * userImage.setImageDrawable(imageDrawable); //bitmap1 =
				 * ((BitmapDrawable) imageDrawable).getBitmap();
				 * 
				 * } }); // if(userImage.getDrawable()!=null){ // bitmap1 =
				 * ((BitmapDrawable) userImage.getDrawable()).getBitmap(); //
				 * Log.i("bitmap0","height"+bitmap1.getHeight()); // }else{ //
				 * if (list.get(i).getSex() == 0) //
				 * bitmap1=BitmapFactory.decodeResource(res,
				 * R.drawable.blank_girl); //
				 * //userImage.setImageResource(R.drawable.blank_girl); // else
				 * // bitmap1=BitmapFactory.decodeResource(res,
				 * R.drawable.blank_boy); // } if (cachedImage == null) {
				 * if(userImage.getDrawable()!=null){ bitmap1 =
				 * ((BitmapDrawable) userImage.getDrawable()).getBitmap();
				 * Log.i("bitmap0","height"+bitmap1.getHeight()); }else{ if
				 * (list.get(i).getSex() == 0)
				 * bitmap1=BitmapFactory.decodeResource(res,
				 * R.drawable.blank_girl);
				 * 
				 * else bitmap1=BitmapFactory.decodeResource(res,
				 * R.drawable.blank_boy); } } else { bitmap1 = ((BitmapDrawable)
				 * cachedImage).getBitmap();
				 * Log.i("bitmapx","width"+bitmap1.getWidth()); }
				 */
				// }
				// res=getResources();
				// bitmap1=BitmapFactory.decodeResource(res,
				// R.drawable.umeng_share_logo_sina);
				// bitmap1 = ((BitmapDrawable)
				// userImage.getDrawable()).getBitmap();
				/*
				 * //bitmap1 = ((BitmapDrawable) getResources()
				 * .getDrawable(R.drawable.umeng_share_logo_sina)).getBitmap();
				 */
				//
			}
			// Log.i("bitmap1","height"+bitmap1.getHeight());
			drawable = new BitmapDrawable(MediaUtils.createBitmap(bitmap,
					bitmap1, 2));
			// Log.i("ddd","width"+drawable.getBounds().width());
			/*itemizedoverlay = new OverItemT(drawable, this);
			point = // new GeoPoint((int)1000000
			// *Double.parseDouble(list.get(i).getLat()),(int)1000000
			// *Double.parseDouble(list.get(i).getLat()));
			new GeoPoint(
					(int) (Double.parseDouble(list.get(i).getLat()) * 1E6),
					(int) (Double.parseDouble(list.get(i).getLon()) * 1E6));
			overlayitem = new OverlayItem(point, list.get(i).getName(), list
					.get(i).getName());

			itemizedoverlay.addOverlay(overlayitem);
			// Log.i("ddd",list.get(i).getLat()+"="+list.get(i).getLon());
			// myLocationOverlay = new MyLocationOverlay(this, mMapView);
			// myLocationOverlay.enableCompass();
			// myLocationOverlay.enableMyLocation();
			mMapView.getOverlays().add(mLocationOverlay);
			mMapView.getOverlays().add(itemizedoverlay);*/

		}
	}

	@Override
	protected void onPause() {
		/*BMapApi app = (BMapApi) this.getApplication();
		app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);
		mLocationOverlay.disableMyLocation();
		mLocationOverlay.disableCompass(); // �ر�ָ����
		app.mBMapMan.stop();*/
		super.onPause();
	}

	@Override
	protected void onResume() {
		/*BMapApi app = (BMapApi) this.getApplication();
		// ע�ᶨλ�¼�����λ�󽫵�ͼ�ƶ�����λ��
		app.mBMapMan.getLocationManager().requestLocationUpdates(
				mLocationListener);
		mLocationOverlay.enableMyLocation();
		mLocationOverlay.enableCompass(); // ��ָ����
		app.mBMapMan.start();*/
		super.onResume();
	}



	// public void getMyFriendsThread(final double lon, final double lat) {
	// progressDialog = ProgressDialog.show(this, "", mContext.getResources()
	// .getString(R.string.process), true, false);
	// final Handler handler = new Handler() {
	// public void handleMessage(Message message) {
	// if (userInfoList != null) {
	// Drawable marker = getResources().getDrawable(
	// R.drawable.iconmarka);
	// Log.d("getMyFriendsThread=", marker.toString());
	// mMapView.getOverlays().add(
	// new OverItemT(marker, MyViewFriendsActivity.this)); //
	// ���ItemizedOverlayʵ��mMapView
	// }
	// progressDialog.dismiss();
	// }
	// };
	// new Thread() {
	// @Override
	// public void run() {
	// // Looper.prepare();
	// getFriendList();
	// Message message = handler.obtainMessage(0);
	// handler.sendMessage(message);
	// // Looper.loop();
	// }
	// }.start();
	//
	// }

	private void getFriendList() {
		JsonDataGetApi api = new JsonDataGetApi();

		JSONArray jobhp = null;
		try {
			jobhp = api.getArray(String.format("u.ashx?op=p&id=%s", userId));
			// Log.d(String.format("u.ashx?op=p&id=%s", userId),
			// jobhp.toString());
		} catch (Exception e) {
			MobclickAgent.reportError(mContext, "MyViewFriends_getFriendList:"
					+ e.toString());
			// Log.d("getLocationResult:jobhp", e.toString());
		}
		JSONObject json = null;
		try {
			FriendInfo u = null;

			for (int i = 0; i < jobhp.length(); i++) {
				json = (JSONObject) jobhp.opt(i);
				uid = json.getString("Uid");
				uname = json.getString("Username");
				Lon = (json.getString("Lon") == null
						|| "null".equals(json.getString("Lon")) || ""
						.equals(json.getString("Lon"))) ? "0" : json
						.getString("Lon");
				Lat = (json.getString("Lat") == null
						|| "null".equals(json.getString("Lat")) || ""
						.equals(json.getString("Lat"))) ? "0" : json
						.getString("Lat");
				ispublic = json.getString("Ispublic");
				headurl = json.getString("Headimg");
				// Log.d("Lat:", "" + Lat);
				// Log.d("Lon:", "" + Lon);
				// String headurl =
				// "http://afanbutiphp.dns87.53nic.com/res/medal4.png";
				if (userInfoList == null) {
					userInfoList = new ArrayList<FriendInfo>();
				}
				if ("true".equals(ispublic)) {
					u = new FriendInfo();
					u.setId(uid);
					u.setName(uname);
					// u.setTinyurl("");
					u.setHeadImg(headurl);
					u.setLon(Lon);
					u.setLat(Lat);
					userInfoList.add(u);
				}

			}
		} catch (JSONException e) {
			MobclickAgent.reportError(mContext,
					"MyViewFriends_userInfoList.add:" + e.toString());
		}
	}
/*
	class OverItemT extends ItemizedOverlay<OverlayItem> {
		private List<OverlayItem> GeoList = new ArrayList<OverlayItem>();
		private Context mContext;
		@SuppressWarnings("unused")
		private Drawable marker;

		public OverItemT(Drawable marker) {
			super(boundCenterBottom(marker));
		}

		public OverItemT(Drawable defaultMarker, Context context) {
			super(boundCenterBottom(defaultMarker));

			this.mContext = context;
			marker = defaultMarker;
			FriendInfo u = new FriendInfo();
			GeoPoint p = null;
			for (int i = 0; i < userInfoList.size(); i++) {
				u = userInfoList.get(i); // �ø�ľ�γ�ȹ���GeoPoint����λ��΢�� (�� *
											// 1E6)

				//
				// Log.d("u.getName()=",
				// u.getName()+"="+u.getLat()+"="+u.getLon());
				p = new GeoPoint((int) (Double.parseDouble(u.getLat()) * 1E6),
						(int) (Double.parseDouble(u.getLon()) * 1E6));
				GeoList.add(new OverlayItem(p, u.getName(), u.getName()));
			}
			populate(); // createItem(int)��������item��һ��������ݣ��ڵ�������ǰ�����ȵ����������

		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);
			// Projection�ӿ�������Ļ���ص����ϵͳ�͵�����澭γ�ȵ����ϵͳ֮��ı任
			Projection projection = mapView.getProjection();
			// �������е�OverlayItem
			for (int index = this.size() - 1; index >= 0; index--) {
				// �õ��������item
				OverlayItem overLayItem = getItem(index);

				// �Ѿ�γ�ȱ任�������MapView���Ͻǵ���Ļ�������
				Point point = projection.toPixels(overLayItem.getPoint(), null);

				Paint paintText = new Paint();
				paintText.setColor(Color.RED);
				paintText.setTextSize(13);
				// �����ı�
				canvas.drawText(overLayItem.getTitle(), point.x + 10,
						point.y - 15, paintText);
			}
		}

		@Override
		protected OverlayItem createItem(int i) {
			return GeoList.get(i);
		}

		public void addOverlay(OverlayItem overlay) {
			GeoList.add(overlay);
			populate();
		}

		@Override
		public int size() {
			return GeoList.size();
		}

		@Override
		// ���?����¼�
		protected boolean onTap(int i) {
			setFocus(GeoList.get(i));
			Toast.makeText(this.mContext, GeoList.get(i).getSnippet(),
					Toast.LENGTH_SHORT).show();
			return true;
		}
	}
*/
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

	class FriendListTask extends AsyncTask<String, Integer, String> {
		// �ɱ䳤�����������AsyncTask.exucute()��Ӧ
		// ProgressDialog pdialog;

		public FriendListTask(Context context) {

			progressDialog = ProgressDialog.show(getParent(), "", mContext
					.getResources().getString(R.string.process), true, false);
		}

		@Override
		protected String doInBackground(String... params) {
			getFriendList();
			return null;

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();

			if (userInfoList != null && userInfoList.size() > 0) {
				addOverlays(userInfoList);
			}
			Message message = Message.obtain();
			message.what = MSG_TIMER;
			handler.sendEmptyMessage(message.what);
			// 60�����һ���б�
			// handler.sendMessageDelayed(message, 60000L);
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
