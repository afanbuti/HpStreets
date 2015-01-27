package com.limon.make;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.RouteOverlay;
import com.baidu.mapapi.TransitOverlay;
import com.limon.common.ResourceUtil;
import com.limon.location.BestLocationListener;

/**
 * ·������
 * 
 * @author Administrator
 * 
 */
public class RoutePlanActivity extends BaseActivity {

	private Button mBtnDrive = null; // �ݳ�����
	private Button mBtnTransit = null; // ��������
	private Button mBtnWalk = null; // ��������
	private String locality;
	private MapView mMapView = null; // ��ͼView
	private MKSearch mSearch = null; // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	private BestLocationListener mLocationListener = null;
	private int longitude, latitude;
	protected Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.routeplan);
		mContext = getApplicationContext();
		locality = ResourceUtil.getString(R.string.venues_default);
		BMapApi app = (BMapApi) this.getApplication();
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

		// ��ʼ������ģ�飬ע���¼�����
		mSearch = new MKSearch();

		// mSearch.init(app.mBMapMan, new MKSearchListener() {
		//			
		//
		// });
		mSearch.init(app.mBMapMan, new MySearchListener());
		// ע�ᶨλ�¼�
		mLocationListener = new BestLocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					GeoPoint pt = new GeoPoint(
							(int) (location.getLatitude() * 1e6),
							(int) (location.getLongitude() * 1e6));
					mMapView.getController().animateTo(pt);
					latitude = (int) (1000000 * location.getLatitude());
					longitude = (int) (1000000 * location.getLongitude());
					try {//
						mSearch
								.reverseGeocode(new GeoPoint(latitude,
										longitude));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		// ע�����
		app.mBMapMan.getLocationManager().requestLocationUpdates(
				mLocationListener);
		// �趨������ť����Ӧ
		mBtnDrive = (Button) findViewById(R.id.drive);
		mBtnTransit = (Button) findViewById(R.id.transit);
		mBtnWalk = (Button) findViewById(R.id.walk);

		OnClickListener clickListener = new OnClickListener() {
			public void onClick(View v) {
				SearchButtonProcess(v);
			}
		};

		mBtnDrive.setOnClickListener(clickListener);
		mBtnTransit.setOnClickListener(clickListener);
		mBtnWalk.setOnClickListener(clickListener);
		findViewById(R.id.ReturnButton).setOnClickListener(
				new OnClickListener() {

					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(mContext, MyInfoActivity.class);
						// mycontext.startActivity(intent);
						GroupFour.group.getLocalActivityManager()
								.removeAllActivities();
						View view = GroupFour.group
								.getLocalActivityManager()
								.startActivity(
										"Four",
										intent
												.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
								.getDecorView();
						// Again, replace the view
						GroupFour.group.replaceView(view);
					}
				});
	}

	void SearchButtonProcess(View v) {
		// ����������ť��Ӧ
		EditText editSt = (EditText) findViewById(R.id.start);
		EditText editEn = (EditText) findViewById(R.id.end);

		// Log.d("locality", "locality"+locality);
		// ������յ��name���и�ֵ��Ҳ����ֱ�Ӷ����긳ֵ����ֵ�����򽫸��������������
		MKPlanNode stNode = new MKPlanNode();
		stNode.name = editSt.getText().toString();
		MKPlanNode enNode = new MKPlanNode();
		enNode.name = editEn.getText().toString();

		// ʵ��ʹ�����������յ���н�����ȷ���趨
		if (mBtnDrive.equals(v)) {
			mSearch.drivingSearch(locality, stNode, locality, enNode);
		} else if (mBtnTransit.equals(v)) {
			mSearch.transitSearch(locality, stNode, enNode);
		} else if (mBtnWalk.equals(v)) {
			mSearch.walkingSearch(locality, stNode, locality, enNode);
		}
	}

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

	class MySearchListener implements MKSearchListener {
		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {
			// ����ſɲο�MKEvent�еĶ���
			if (error != 0 || res == null) {
				Toast.makeText(RoutePlanActivity.this,
						ResourceUtil.getString(R.string.error_nofind),
						Toast.LENGTH_SHORT).show();
				return;
			}
			RouteOverlay routeOverlay = new RouteOverlay(
					RoutePlanActivity.this, mMapView);
			// �˴���չʾһ��������Ϊʾ��
			routeOverlay.setData(res.getPlan(0).getRoute(0));
			mMapView.getOverlays().clear();
			mMapView.getOverlays().add(routeOverlay);
			mMapView.invalidate();

			mMapView.getController().animateTo(res.getStart().pt);
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult res, int error) {
			if (error != 0 || res == null) {
				Toast.makeText(RoutePlanActivity.this,
						ResourceUtil.getString(R.string.error_nofind),
						Toast.LENGTH_SHORT).show();
				return;
			}
			TransitOverlay routeOverlay = new TransitOverlay(
					RoutePlanActivity.this, mMapView);
			// �˴���չʾһ��������Ϊʾ��
			routeOverlay.setData(res.getPlan(0));
			mMapView.getOverlays().clear();
			mMapView.getOverlays().add(routeOverlay);
			mMapView.invalidate();

			mMapView.getController().animateTo(res.getStart().pt);
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error) {
			if (error != 0 || res == null) {
				Toast.makeText(RoutePlanActivity.this,
						ResourceUtil.getString(R.string.error_nofind),
						Toast.LENGTH_SHORT).show();
				return;
			}
			RouteOverlay routeOverlay = new RouteOverlay(
					RoutePlanActivity.this, mMapView);
			// �˴���չʾһ��������Ϊʾ��
			routeOverlay.setData(res.getPlan(0).getRoute(0));
			mMapView.getOverlays().clear();
			mMapView.getOverlays().add(routeOverlay);
			mMapView.invalidate();

			mMapView.getController().animateTo(res.getStart().pt);
		}

		@Override
		public void onGetAddrResult(MKAddrInfo result, int iError) {
			// Log.d("iError", "iError" + iError);
			if (result == null) {
				return;
			}
			// ��õ�ǰ�ĳ���
			locality = result.addressComponents.city;
			// Log.i("Merchants", "����: " + locality);
			// �����������
			// String district = result.addressComponents.district;
			// Log.i("Merchants", "����: " + district);
			// String temp = name.endsWith(specialcity) ? district : name;
			// �������б仯��ʱ��
			// if(!temp.equals(city)) {
			// city = temp;
			// StringBuffer sb = new StringBuffer();
			// ��γ������Ӧ��λ��
			// sb.append(result.strAddr).append("/n");

			// �жϸõ�ַ�����Ƿ���POI��Point of Interest,����Ȥ�㣩
			// if (null != result.poiList) {
			// // �������е���Ȥ����Ϣ
			// for (MKPoiInfo poiInfo : result.poiList) {
			// sb.append("----------------------------------------")
			// .append("/n");
			// sb.append("���ƣ�").append(poiInfo.name).append("/n");
			// sb.append("��ַ��").append(poiInfo.address).append("/n");
			// sb.append("���ȣ�").append(
			// poiInfo.pt.getLongitudeE6() / 1000000.0f)
			// .append("/n");
			// sb.append("γ�ȣ�").append(
			// poiInfo.pt.getLatitudeE6() / 1000000.0f)
			// .append("/n");
			// sb.append("�绰��").append(poiInfo.phoneNum).append("/n");
			// sb.append("�ʱࣺ").append(poiInfo.postCode).append("/n");
			// // poi���ͣ�0����ͨ�㣬1������վ��2��������·��3������վ��4��������·
			// sb.append("���ͣ�").append(poiInfo.ePoiType).append("/n");
			// }
			// }
			// Log.d("d", sb.toString());
		}

		@Override
		public void onGetPoiResult(MKPoiResult res, int arg1, int arg2) {

		}
	}
}
