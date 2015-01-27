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
 * 路线搜索
 * 
 * @author Administrator
 * 
 */
public class RoutePlanActivity extends BaseActivity {

	private Button mBtnDrive = null; // 驾车搜索
	private Button mBtnTransit = null; // 公交搜索
	private Button mBtnWalk = null; // 步行搜索
	private String locality;
	private MapView mMapView = null; // 地图View
	private MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
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
		// 如果使用地图SDK，请初始化地图Activity
		super.initMapActivity(app.mBMapMan);

		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.setBuiltInZoomControls(true);
		// 设置在缩放动画过程中也显示overlay,默认为不绘制
		mMapView.setDrawOverlayWhenZooming(true);

		// 初始化搜索模块，注册事件监听
		mSearch = new MKSearch();

		// mSearch.init(app.mBMapMan, new MKSearchListener() {
		//			
		//
		// });
		mSearch.init(app.mBMapMan, new MySearchListener());
		// 注册定位事件
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
		// 注册监听
		app.mBMapMan.getLocationManager().requestLocationUpdates(
				mLocationListener);
		// 设定搜索按钮的响应
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
		// 处理搜索按钮响应
		EditText editSt = (EditText) findViewById(R.id.start);
		EditText editEn = (EditText) findViewById(R.id.end);

		// Log.d("locality", "locality"+locality);
		// 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索
		MKPlanNode stNode = new MKPlanNode();
		stNode.name = editSt.getText().toString();
		MKPlanNode enNode = new MKPlanNode();
		enNode.name = editEn.getText().toString();

		// 实际使用中请对起点终点城市进行正确的设定
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
			// 错误号可参考MKEvent中的定义
			if (error != 0 || res == null) {
				Toast.makeText(RoutePlanActivity.this,
						ResourceUtil.getString(R.string.error_nofind),
						Toast.LENGTH_SHORT).show();
				return;
			}
			RouteOverlay routeOverlay = new RouteOverlay(
					RoutePlanActivity.this, mMapView);
			// 此处仅展示一个方案作为示例
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
			// 此处仅展示一个方案作为示例
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
			// 此处仅展示一个方案作为示例
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
			// 获得当前的城市
			locality = result.addressComponents.city;
			// Log.i("Merchants", "城市: " + locality);
			// 获得区县名称
			// String district = result.addressComponents.district;
			// Log.i("Merchants", "区县: " + district);
			// String temp = name.endsWith(specialcity) ? district : name;
			// 当城市有变化的时候
			// if(!temp.equals(city)) {
			// city = temp;
			// StringBuffer sb = new StringBuffer();
			// 经纬度所对应的位置
			// sb.append(result.strAddr).append("/n");

			// 判断该地址附近是否有POI（Point of Interest,即兴趣点）
			// if (null != result.poiList) {
			// // 遍历所有的兴趣点信息
			// for (MKPoiInfo poiInfo : result.poiList) {
			// sb.append("----------------------------------------")
			// .append("/n");
			// sb.append("名称：").append(poiInfo.name).append("/n");
			// sb.append("地址：").append(poiInfo.address).append("/n");
			// sb.append("经度：").append(
			// poiInfo.pt.getLongitudeE6() / 1000000.0f)
			// .append("/n");
			// sb.append("纬度：").append(
			// poiInfo.pt.getLatitudeE6() / 1000000.0f)
			// .append("/n");
			// sb.append("电话：").append(poiInfo.phoneNum).append("/n");
			// sb.append("邮编：").append(poiInfo.postCode).append("/n");
			// // poi类型，0：普通点，1：公交站，2：公交线路，3：地铁站，4：地铁线路
			// sb.append("类型：").append(poiInfo.ePoiType).append("/n");
			// }
			// }
			// Log.d("d", sb.toString());
		}

		@Override
		public void onGetPoiResult(MKPoiResult res, int arg1, int arg2) {

		}
	}
}
