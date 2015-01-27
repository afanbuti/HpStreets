package com.limon.make;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.limon.location.BestLocationListener;

/**
 * ��ͼ��λ��
 * 
 * @author Administrator
 * 
 */
public class MyViewLocatActivity extends BaseActivity {

	//MapView mMapView = null;
	BestLocationListener mLocationListener = null;// onResumeʱע���listener��onPauseʱ��ҪRemove
	//MyLocationOverlay mLocationOverlay = null; // ��λͼ��
	ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapviewlocation);
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
		// ���������Ŷ���������Ҳ��ʾoverlay,Ĭ��Ϊ������
		mMapView.setDrawOverlayWhenZooming(true);

		// ��Ӷ�λͼ��
		mLocationOverlay = new MyLocationOverlay(this, mMapView);
		mMapView.getOverlays().add(mLocationOverlay);
*/
		// ע�ᶨλ�¼�
		mLocationListener = new BestLocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					/*GeoPoint pt = new GeoPoint(
							(int) (location.getLatitude() * 1e6),
							(int) (location.getLongitude() * 1e6));
					mMapView.getController().animateTo(pt);*/
					GUIAction(location.getLongitude(), location.getLatitude());
				}
			}
		};

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


	public void GUIAction(final double lon, final double lat) {
		progressDialog = ProgressDialog.show(getParent(), "", mContext
				.getResources().getString(R.string.process), true, false);
		new Thread() {
			@Override
			public void run() {
				// getLocationResult(lon, lat);
				endProcessHandler.sendEmptyMessage(0);
			}
		}.start();
	}

	private Handler endProcessHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			progressDialog.dismiss();
		}
	};
}
