package com.limon.make;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.feedback.NotificationType;
import com.feedback.UMFeedbackService;
import com.limon.common.DialogUtils;
import com.limon.common.ResourceUtil;
import com.limon.common.DialogUtils.DialogCallBack;
import com.mobclick.android.MobclickAgent;
import com.papaya.appflood.AppFlood;

/**
 * 主界面
 * 
 * @author Administrator
 * 
 */
public class MainActivity extends TabActivity {
	private static final int TOOLBAR0 = 0;
	private static final int TOOLBAR1 = 1;
	private static final int TOOLBAR2 = 2;
	protected Context mContext;
	protected Activity instance;
	private Intent mPlaceIntent;
	private Intent mMyinfoIntent;
	private Intent mFriendsIntent;
	private Intent mLoginIntent;
	private Intent mDoingIntent;
	private int tadid = 0;
	private int iCount = 0;
	private String userId = "";
	private ProgressDialog progressDialog = null;
	public static TabHost tabHost = null;
	public static TabWidget tabWidget = null;
	public static TabSpec ts0 = null, ts1 = null, ts2 = null, ts3 = null;
	private Field mBottomLeftStrip;
	private Field mBottomRightStrip;
	private static final int REQ_SYSTEM_SETTINGS = 0;
	private boolean exit = false;
	SharedPreferences sharedPreferences;
	private String appID = "mwf8Ctelwirf17CL";
	private String appSecret = "Od2y9fRX74L5077bcea";
	private BMapApi app=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		app = (BMapApi) this.getApplication();
		app.addActivity(this);
		
		instance = this;
		mContext = getApplicationContext();

		MobclickAgent.update(this);
		UMFeedbackService.enableNewReplyNotification(this,
				NotificationType.NotificationBar);
		MobclickAgent.onError(mContext);
		MobclickAgent.setUpdateOnlyWifi(false);

		AppFlood.initialize(MainActivity.this, appID, appSecret,
				AppFlood.AD_ALL);

		tabHost = (TabHost) this.findViewById(android.R.id.tabhost);
		tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		tabHost.setup();
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(MainActivity.this);
		userId = sharedPreferences.getString("UserID", "");
		// accessToken = sharedPreferences.getString("Token", "");
		iCount = sharedPreferences.getInt("iCount", 1);
		Editor editor = sharedPreferences.edit();// 获取编辑器
		editor.putInt("iCount", ++iCount);
		editor.commit();

		this.mDoingIntent = new Intent(this, GroupFirst.class);
		this.mPlaceIntent = new Intent(this, GroupThree.class);
		this.mFriendsIntent = new Intent(this, GroupTwo.class);
		this.mLoginIntent = new Intent(this, LogInActivity.class);
		this.mMyinfoIntent = new Intent(this, GroupFour.class);

		try {
			tadid = getIntent().getExtras().getInt("STARTING_TAB");
		} catch (Exception e1) {
		}

		if (iCount == 1) {
			addShortcut();
		}

		OnTabChangeListener changeLis = new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				if (tabId == null || "".equals(tabId))
					tabId = "0";
				// Log.d("List-tabId=", tabId);
				tadid = Integer.parseInt(tabId);
			}
		};
		tabHost.setOnTabChangedListener(changeLis);
		// Log.d("onCreate","userId"+userId);
		// Log.d("x",BASE64Decoder.decodeToString("MTEzLjY1OTMyMjQzODM1"));
		// Log.d("y",BASE64Decoder.decodeToString("MzQuNzk5NTczNDMwNjI="));
		// Toast.makeText(getApplicationContext(), "访问次数：" + iCount,
		// Toast.LENGTH_LONG).show();
		// starService();

	}

	@Override
	protected void onResume() {
		// Log.d("onResume","tadid"+tadid);
		creatTabs(tadid);
		// 
		super.onResume();

	}

	@Override
	protected void onPause() {
		// Log.d("onPause","tadid"+tadid);
		creatTabs(tadid);
		// Log.d("onPause","userId"+userId);
		super.onPause();

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// Log.d("onNewIntent","tadid"+tadid);
		creatTabs(tadid);
		// Log.d("onNewIntent","userId"+userId);
		super.onNewIntent(intent);
		// int tabIndex = intent.getIntExtra("STARTING_TAB", -1);
		// if(tabIndex != -1 ){
		// getTabHost().setCurrentTab(tabIndex);
		// }
		// Log.v("test", "TabActivity onNewIntent");
	}

	private void creatTabs(int tadid) {

		// if (this.tabHost == null) {
		tabHost = (TabHost) this.findViewById(android.R.id.tabhost);
		tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		tabHost.setup();
		tabHost.bringToFront();
		// Log.d("ChildCount1","ChildCount="+tabHost.getTabWidget().getTabCount());
		if (tabHost != null) {
			tabHost.setCurrentTab(0);
			tabHost.clearAllTabs();
		}
		// Log.d("ChildCount2","ChildCount="+tabHost.getTabWidget().getTabCount());
		ts0 = tabHost.newTabSpec("0").setIndicator(
				ResourceUtil.getString(R.string.main_info),
				getResources().getDrawable(R.drawable.tab_interact))
				.setContent(this.mDoingIntent);
		tabHost.addTab(ts0);
		// tabHost.setCurrentTab(0);
		// Log.d("ChildCount01","ChildCount="+tabHost.getTabWidget().getTabCount());

		ts1 = tabHost.newTabSpec("1").setIndicator(
				ResourceUtil.getString(R.string.main_friend),
				getResources().getDrawable(R.drawable.tab_friends)).setContent(
				this.mFriendsIntent);
		tabHost.addTab(ts1);
		// tabHost.setCurrentTab(0);
		// Log.d("ChildCount02","ChildCount="+tabHost.getTabWidget().getTabCount());
		ts2 = tabHost.newTabSpec("2").setIndicator(
				ResourceUtil.getString(R.string.main_home),
				getResources().getDrawable(R.drawable.tab_nearby_venues))
				.setContent(this.mPlaceIntent);
		tabHost.addTab(ts2);
		// tabHost.setCurrentTab(1);
		// Log.d("ChildCount03","ChildCount="+tabHost.getTabWidget().getTabCount());
		// ts3 = null;
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(MainActivity.this);
		userId = sharedPreferences.getString("UserID", "");
		// Log.d("creatTabs","userId"+userId);
		if ("".equals(userId)) {
			ts3 = tabHost.newTabSpec("3").setIndicator(
					ResourceUtil.getString(R.string.login),
					getResources().getDrawable(R.drawable.tab_myinfo))
					.setContent(this.mLoginIntent);
			// tadid = 0;
		} else {
			ts3 = tabHost.newTabSpec("3").setIndicator(
					ResourceUtil.getString(R.string.main_myinfo),
					getResources().getDrawable(R.drawable.tab_nearby_tips))
					.setContent(this.mMyinfoIntent);

			// tabHost.setCurrentTab(2);
		}
		tabHost.addTab(ts3);
		// Log.d("ChildCount3","ChildCount="+tabHost.getTabWidget().getTabCount());

		// Log.d("ChildCount3","ChildCount="+tabHost.getTabWidget().getChildTabViewAt(0));
		// tabHost.addTab(ts3);
		/*
		 * try { Field idcurrent =
		 * tabHost.getClass().getDeclaredField("mCurrentTab");
		 * idcurrent.setAccessible(true); if (tadid == 0) {
		 * idcurrent.setInt(tabHost, 1); } else { idcurrent.setInt(tabHost, 0);
		 * } } catch (Exception e) { e.printStackTrace(); }
		 */
		if (tabHost != null) {
			// 进入传来的选项卡
			tabHost.setCurrentTab(tadid);

			if (Integer.valueOf(Build.VERSION.SDK) <= 7) {
				try {
					mBottomLeftStrip = tabWidget.getClass().getDeclaredField(
							"mBottomLeftStrip");
					mBottomRightStrip = tabWidget.getClass().getDeclaredField(
							"mBottomRightStrip");
					if (!mBottomLeftStrip.isAccessible()) {
						mBottomLeftStrip.setAccessible(true);
					}
					if (!mBottomRightStrip.isAccessible()) {
						mBottomRightStrip.setAccessible(true);
					}
					// mBottomLeftStrip.set(tabWidget,
					// getResources().getDrawable (R.drawable.linee));
					// mBottomRightStrip.set(tabWidget,
					// getResources().getDrawable (R.drawable.linee));

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					mBottomLeftStrip = tabWidget.getClass().getDeclaredField(
							"mLeftStrip");
					mBottomRightStrip = tabWidget.getClass().getDeclaredField(
							"mRightStrip");
					if (!mBottomLeftStrip.isAccessible()) {
						mBottomLeftStrip.setAccessible(true);
					}
					if (!mBottomRightStrip.isAccessible()) {
						mBottomRightStrip.setAccessible(true);
					}
					// mBottomLeftStrip.set(tabWidget,
					// getResources().getDrawable(R.drawable.linee));
					// mBottomRightStrip.set(tabWidget,
					// getResources().getDrawable(R.drawable.linee));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			for (int i = 0; i < tabWidget.getChildCount(); i++) {

				final TextView tv = (TextView) tabWidget.getChildAt(i)
						.findViewById(android.R.id.title);
				tv.setTextColor(Color.BLACK);
				tv.setTextSize(16.0f);
				View vvv = tabWidget.getChildAt(i);
				// if (tabHost.getCurrentTab() == i) {
				// vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.focus));
				// } else {
				// vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.unfocus));
				// }
				vvv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.tab_pg));
			}
		}
	}

	@SuppressWarnings("unused")
	private void starService() {
		Intent it = new Intent("com.limon.task.LimonService");
		startService(it);
	}

	Handler endSessionHandle = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			manager.restartPackage(getPackageName());
			android.os.Process.killProcess(android.os.Process.myPid()); // 获取PID
			System.exit(0); // 常规java、c#的标准退出法，返回值为0代表正常退出
			close();
			finish();
		}
	};

	// 用户注销线程
	// class EndSessionThread implements Runnable
	// {
	// public void run()
	// {
	// AccessInfoHelper accessDBHelper = new AccessInfoHelper(mContext);
	// accessDBHelper.open();
	// accessDBHelper.delete();
	// accessDBHelper.close();
	// // Weibo weibo = OAuthConstant.getInstance().getWeibo();
	// // try
	// // {
	// // weibo.endSession();
	// // } catch (WeiboException e) {
	// // e.printStackTrace();
	// // }
	// endSessionHandle.sendEmptyMessage(201);
	// }
	// }
	public void endSession() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(MainActivity.this);
		Editor editor = sharedPreferences.edit();// 获取编辑器
		editor.clear();
		editor.commit();
		this.close();
		// endSessionHandle.sendEmptyMessage(201);
		// System.exit(0);
	}

	private void close() {
		// Log.d("Close", "Main");
		// Intent intent = new Intent();
		// intent.setAction("ExitApp");
		// this.sendBroadcast(intent);
		// finish();
		Intent intent = new Intent(getPackageName() + ".ExitListenerReceiver");
		sendBroadcast(intent);
		finish();
	}

	public void endSessionRelogin() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(MainActivity.this);
		Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
		if (tabHost != null) {
			tabHost.setCurrentTab(0);
		}
		Intent it = new Intent();
		it.setClass(MainActivity.this, LogInActivity.class);
		it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(it);
		// endSessionHandle.sendEmptyMessage(201);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!"".equals(userId)) {
			menu.add(0, TOOLBAR0, 1,
					ResourceUtil.getString(R.string.main_setting)).setIcon(
					android.R.drawable.ic_menu_set_as);// android.R.drawable.ic_menu_delete
			menu.add(0, TOOLBAR1, 2,
					ResourceUtil.getString(R.string.info_relogin)).setIcon(
					android.R.drawable.ic_menu_revert);
			menu
					.add(0, TOOLBAR2, 3,
							ResourceUtil.getString(R.string.info_exit))
					.setIcon(android.R.drawable.ic_dialog_info);
		} else {
			menu
					.add(0, TOOLBAR2, 3,
							ResourceUtil.getString(R.string.info_exit))
					.setIcon(android.R.drawable.ic_dialog_info);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Intent intent = new Intent(mContext, SettingActivity.class);
			startActivityForResult(intent, REQ_SYSTEM_SETTINGS);
			break;
		case 1:
			DialogUtils.dialogBuilder(instance, ResourceUtil
					.getString(R.string.info_tishi), ResourceUtil
					.getString(R.string.info_qrelogin), new DialogCallBack() {
				@Override
				public void callBack() {
					// dialog.show();
					// dialog.setMessage("注销登录中...");
					// Thread thread = new Thread( new
					// EndSessionThread() );
					// thread.start();
					endSessionRelogin();
				}
			});
			break;
		case 2:
			DialogUtils.dialogBuilder(instance, ResourceUtil
					.getString(R.string.info_tishi), ResourceUtil
					.getString(R.string.info_qexit), new DialogCallBack() {
				@Override
				public void callBack() {
					//endSession();
					close();
					finish();
				}
			});
			break;
		}
		// if (item.getItemId() == 0) {
		// // android.os.Process.killProcess(android.os.Process.myPid());
		// // //获取PID
		// // System.exit(0); //常规java、c#的标准退出法，返回值为0代表正常退出
		// // ActivityManager manager = (ActivityManager)
		// // getSystemService(Context.ACTIVITY_SERVICE);
		// // manager.restartPackage(getPackageName());
		// // finish();
		// // DialogUtils.dialogBuilder(instance, "提示",
		// // "确定要退出？", new DialogCallBack() {
		// // @Override
		// // public void callBack() {
		// // endSession();
		// // }
		// // });
		// Intent intent = new Intent(mContext, SettingActivity.class);
		// startActivityForResult(intent, REQ_SYSTEM_SETTINGS);
		// } else {
		// DialogUtils.dialogBuilder(instance, ResourceUtil
		// .getString(R.string.info_tishi), ResourceUtil
		// .getString(R.string.info_qrelogin), new DialogCallBack() {
		// @Override
		// public void callBack() {
		// // dialog.show();
		// // dialog.setMessage("注销登录中...");
		// // Thread thread = new Thread( new
		// // EndSessionThread() );
		// // thread.start();
		// endSessionRelogin();
		// }
		// });
		// }
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 为程序创建桌面快捷方式
	 */
	private void addShortcut() {
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");

		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getString(R.string.app_name));
		shortcut.putExtra("duplicate", false); // 不允许重复创建

		// 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
		// 注意: ComponentName的第二个参数必须加上点号(.)，否则快捷方式无法启动相应程序
		ComponentName comp = new ComponentName(this.getPackageName(),
				".LogoActivity");
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
				Intent.ACTION_MAIN).setComponent(comp));

		// 快捷方式的图标
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
				this, R.drawable.icon);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

		sendBroadcast(shortcut);
	}

	/**
	 * 删除程序的快捷方式
	 */
	@SuppressWarnings("unused")
	private void delShortcut() {
		Intent shortcut = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");

		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getString(R.string.app_name));

		// 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
		// 注意: ComponentName的第二个参数必须是完整的类名（包名+类名），否则无法删除快捷方式
		String appClass = this.getPackageName() + "."
				+ this.getLocalClassName();
		ComponentName comp = new ComponentName(this.getPackageName(), appClass);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
				Intent.ACTION_MAIN).setComponent(comp));

		sendBroadcast(shortcut);

	}

	public void GUIAction(final double lon, final int lat) {
		progressDialog = ProgressDialog.show(MainActivity.this, "", mContext
				.getResources().getString(R.string.process), true, false);
		// processMediaDialog.show();
		// Thread processThread =
		new Thread() {
			@Override
			public void run() {

				endProcessHandler.sendEmptyMessage(0);
			}
		}.start();
		// processThread.start();
	}

	private Handler endProcessHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//android.os.Process.killProcess(android.os.Process.myPid());
			//System.exit(0);
			//ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			//manager.restartPackage(getPackageName());
			//finish();
			app.exit();
			progressDialog.dismiss();
		}
	};

	// 处理设置结果
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Log.d("requestCode",""+requestCode);
		// if (requestCode == REQ_SYSTEM_SETTINGS) {
		// // 获取设置界面PreferenceActivity中各个Preference的值
		// String pushnotification = getResources().getString(
		// R.string.preferences_push_notification);
		// String ispublic = getResources().getString(
		// R.string.preferences_public);
		// // 取得属于整个应用程序的SharedPreferences
		// SharedPreferences settings = PreferenceManager
		// .getDefaultSharedPreferences(this);
		// Boolean updateSwitch = settings.getBoolean(pushnotification, true);
		// Boolean iSpublic = settings.getBoolean(ispublic, true);
		// // String updateFrequency = settings.getString(updateFrequencyKey,
		// // "10");
		// // Log.v("CheckBoxPreference_Main", updateSwitch.toString());
		// //if (iSpublic) {
		// guiAction(iSpublic,updateSwitch);
		// //} else {
		// // guiAction("false");
		// //}
		// } else {
		// // 其他Intent返回的结果
		// }
	}

	// 设置是否公开个人位置
	// private void guiAction(final boolean iSpublic,final boolean iSsyncset) {
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
	// api.getString("u.ashx?op=up&id=" + userId + "&pub="+ iSpublic+ "&syn="+
	// iSsyncset);
	// Log.d("u.ashx?op=u&id=" + userId, "u.ashx?op=up&id=" + userId + "&pub="+
	// iSpublic+ "&syn="+ iSsyncset);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// DialogUtils.dialogBuilder(instance, ResourceUtil
			// .getString(R.string.info_tishi), ResourceUtil
			// .getString(R.string.info_qrelogin), new DialogCallBack() {
			// @Override
			// public void callBack() {
			// endSessionRelogin();
			// }
			// });
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		if (exit) {
			super.onBackPressed();
		} else {
			DialogUtils.dialogBuilder(instance, ResourceUtil
					.getString(R.string.info_tishi), ResourceUtil
					.getString(R.string.info_qrelogin), new DialogCallBack() {
				@Override
				public void callBack() {
					//endSessionRelogin();
					close();
				}
			});
		}
		exit = true;
		new Handler().postDelayed(new Runnable() {
			public void run() {
				exit = false;
			}
		}, 2000);
	}
}