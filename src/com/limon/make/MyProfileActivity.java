package com.limon.make;

import java.io.File;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.limon.common.AsyncImageLoader;
import com.limon.common.FileUtils;
import com.limon.common.InfoHelper;
import com.limon.common.JsonDataGetApi;
import com.limon.common.MediaUtils;
import com.limon.common.ResourceUtil;
import com.limon.common.StringUtils;
import com.limon.common.AsyncImageLoader.ImageCallBack;
import com.mobclick.android.MobclickAgent;

/**
 * 个人资料修改
 * 
 * @author Administrator
 * 
 */
public class MyProfileActivity extends BaseActivity {
	// private ImageButton imgChooseBtn = null;
	private ProgressDialog progressDialog = null;
	private static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
	private static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
	private static final int REQUEST_CODE_UPDATE_SEX = 2;
	private static final int REQUEST_CODE_UPDATE_PHONE = 3;
	private String thisLarge = null, theSmall = null;
	private ImageView imageView = null;
	private TextView nameView = null, userSex = null, userPhone = null;
	private TextView levelView = null;
	private TextView pointView = null;
	private TextView goldView = null;
	private TextView progressView = null;
	private ProgressBar progressBar = null;
	private String uname = "", phone = "";
	private String userId = "";
	private int credit = 0, sex = 0, experience = 0, level = 0;
	private String headurl = "", newName = "";
	private Drawable cachedImage = null;
	private JsonDataGetApi api = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		instance = this;
		setContentView(R.layout.profilemyactivity);
		setTitle(ResourceUtil.getString(R.string.info_setmyinfo));
		// imgChooseBtn = (ImageButton) findViewById(R.id.share_imagechoose);
		userSex = (TextView) findViewById(R.id.userSexValue);
		userPhone = (TextView) findViewById(R.id.userPhoneValue);
		nameView = (TextView) findViewById(R.id.user_details_name);
		levelView = (TextView) findViewById(R.id.user_details_level);
		pointView = (TextView) findViewById(R.id.user_details_points);
		goldView = (TextView) findViewById(R.id.user_details_gold);
		progressView = (TextView) findViewById(R.id.user_details_level_progress_text);
		imageView = (ImageView) findViewById(R.id.user_details_icon);
		progressBar = (ProgressBar) findViewById(R.id.user_details_level_progress);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CharSequence[] items = {
						ResourceUtil.getString(R.string.phone_album),
						ResourceUtil.getString(R.string.phone_graph),
						ResourceUtil.getString(R.string.phone_clean) };
				imageChooseItem(items);
			}
		});
		ImageView btnd = (ImageView) findViewById(R.id.decr);
		btnd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
				unregisterReceiver(exitre);
			}
		});
		userSex.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, MyProfSubmitActivity.class);
				startActivityForResult(intent, REQUEST_CODE_UPDATE_SEX);
			}
		});
		userPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,
						MyPhoneSubmitActivity.class);
				startActivityForResult(intent, REQUEST_CODE_UPDATE_PHONE);
			}
		});
		thisLarge = getImgPathByCaptureSendFilter();
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(
		// this,android.R.layout.simple_spinner_item);
		// adapter.setDropDownViewResource(
		// android.R.layout.simple_spinner_dropdown_item);
		// adapter.add("男");
		// adapter.add("女");
		// this.cateSpinner.setAdapter(adapter);
		// this.cateSpinner.setSelection(0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPersonInfo();
		// guiAction();
	}

	// 获取个人信息绘制界面
	private void getPersonInfo() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MyProfileActivity.this);
		userId = settings.getString("UserID", "");
		uname = settings.getString("UserName", "");
		phone = settings.getString("Phone", "");
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
		userSex.setText(InfoHelper.getSex(sex));
		userPhone.setText(phone);
		if ("".equals(headurl)) {
			if (sex == 0)
				imageView.setImageResource(R.drawable.blank_girl);
			else
				imageView.setImageResource(R.drawable.blank_boy);
		} else {
			AsyncImageLoader asyncImageLoader = new AsyncImageLoader(); // 异步获取图片
			cachedImage = asyncImageLoader.loadDrawable(BMapApi.getInstance()
					.getImageUrl()
					+ headurl, imageView, new ImageCallBack() {
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

	// private void submitSex() {
	// progressDialog = ProgressDialog.show(this, "", ResourceUtil
	// .getString(R.string.execute), true, false);
	// final Handler handler = new Handler() {
	// public void handleMessage(Message message) {
	// progressDialog.dismiss();
	//
	// }
	// };
	// new Thread() {
	// @Override
	// public void run() {
	// Looper.prepare();
	// uploadSex("");
	// Message message = new Message();
	// handler.sendMessage(message);
	// Looper.loop();
	// }
	// }.start();
	// }
	// private void uploadSex(String newName) {
	// api = new JsonDataGetApi();
	// try {
	// api.getString("u.ashx?op=us&id="+userId+"&s="+newName);
	// } catch (Exception e) {
	// Toast.makeText(getApplicationContext(),
	// ResourceUtil.getString(R.string.error_net),
	// Toast.LENGTH_LONG).show();
	// Log.d("getJsonDataJarr:", e.toString());
	// }
	// }
	private void submitSomething() {
		progressDialog = ProgressDialog.show(this, "", ResourceUtil
				.getString(R.string.execute), true, false);
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				progressDialog.dismiss();
				SharedPreferences sharedPreferences = PreferenceManager
						.getDefaultSharedPreferences(MyProfileActivity.this);
				Editor editor = sharedPreferences.edit();// 获取编辑器
				editor.putString("HeadImg", newName);
				editor.commit();
			}
		};
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				// Log.d("thisLarge=",thisLarge);
				if (!(thisLarge == null || "".equals(thisLarge))) {
					newName = StringUtils.radomUUID() + "."
							+ FileUtils.getFileFormat(thisLarge);
					uploadData(newName);
					FileUtils.uploadFile(newName, thisLarge);
				}

				Message message = new Message();
				handler.sendMessage(message);
				Looper.loop();
			}
		}.start();
	}

	private void uploadData(String newName) {
		api = new JsonDataGetApi();
		try {
			api.getString("u.ashx?op=ui&id=" + userId + "&hi=" + newName);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					ResourceUtil.getString(R.string.error_net),
					Toast.LENGTH_LONG).show();
			MobclickAgent.reportError(mContext, "MyProfile_uploadData:"
					+ e.toString());
		}
	}

	// 根据获取的个人信息绘制头像界面
	// private void guiAction() {
	// final Handler handler = new Handler() {
	// public void handleMessage(Message message) {
	// AsyncImageLoader asyncImageLoader = new AsyncImageLoader(); // 异步获取图片
	// cachedImage = asyncImageLoader.loadDrawable(headurl, imageView,
	// new ImageCallback() {
	// @Override
	// // 这里是重写了回调接口
	// public void imageLoaded(Drawable imageDrawable,
	// ImageView imageView, String imageUrl) {
	// imageView.setImageDrawable(imageDrawable);
	// }
	// });
	// if (cachedImage == null) {
	// // 如果没有图片，就以一个载入的图片代替显示
	// if(sex==0)
	// imageView.setImageResource(R.drawable.blank_girl);
	// else
	// imageView.setImageResource(R.drawable.blank_boy);
	// } else {
	// // 如果有图片，就载入图片
	// imageView.setImageDrawable(cachedImage);
	// }
	// }
	// };
	// new Thread() {
	// @Override
	// public void run() {
	// // 获取头像地址
	// //headurl = "http://afanbutiphp.dns87.53nic.com/res/medal4.png";
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
	/**
	 * 操作选择
	 * 
	 * @param items
	 */
	public void imageChooseItem(CharSequence[] items) {
		AlertDialog imageDialog = new AlertDialog.Builder(instance).setTitle(
				ResourceUtil.getString(R.string.phone_add)).setItems(items,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// 手机选图
						if (item == 0) {
							Intent intent = new Intent(
									Intent.ACTION_GET_CONTENT);
							intent.setType("image/*");
							startActivityForResult(intent,
									REQUEST_CODE_GETIMAGE_BYSDCARD);
						}
						// 拍照
						else if (item == 1) {
							Intent intent = new Intent(
									"android.media.action.IMAGE_CAPTURE");

							String camerName = InfoHelper.getFileName();
							String fileName = "Share" + camerName + ".tmp";

							File camerFile = new File(
									InfoHelper.getCamerPath(), fileName);

							theSmall = InfoHelper.getCamerPath() + fileName;
							thisLarge = getLatestImage();

							Uri originalUri = Uri.fromFile(camerFile);
							intent.putExtra(MediaStore.EXTRA_OUTPUT,
									originalUri);
							startActivityForResult(intent,
									REQUEST_CODE_GETIMAGE_BYCAMERA);
						} else if (item == 2) {
							thisLarge = null;
							imageView.setBackgroundDrawable(null);
						}
					}
				}).create();

		imageDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_GETIMAGE_BYSDCARD) {
			if (resultCode != RESULT_OK) {
				return;
			}

			if (data == null)
				return;

			Uri thisUri = data.getData();
			String thePath = InfoHelper
					.getAbsolutePathFromNoStandardUri(thisUri);

			// 如果是标准Uri
			if (StringUtils.isBlank(thePath)) {
				thisLarge = getAbsoluteImagePath(thisUri);
			} else {
				thisLarge = thePath;
			}

			String attFormat = FileUtils.getFileFormat(thisLarge);
			if (!"photo".equals(MediaUtils.getContentType(attFormat))) {
				Toast.makeText(mContext,
						ResourceUtil.getString(R.string.phone_nofind),
						Toast.LENGTH_SHORT).show();
				return;
			}
			// String imgName = FileUtils.getFileName(thisLarge);
			// Log.d(imgName,thisLarge);
			// Bitmap bitmap = loadImgThumbnail(imgName,
			// MediaStore.Images.Thumbnails.MICRO_KIND);
			Log.d("thisLarge", thisLarge);
			Bitmap bitmap = InfoHelper.getScaleBitmap(mContext, thisLarge);
			if (bitmap != null) {
				// imageView.setBackgroundDrawable(null);
				imageView.setImageDrawable(new BitmapDrawable(bitmap));
			}

			// 存储头像
			submitSomething();
		}
		// 拍摄图片
		else if (requestCode == REQUEST_CODE_GETIMAGE_BYCAMERA) {
			if (resultCode != RESULT_OK) {
				return;
			}

			super.onActivityResult(requestCode, resultCode, data);

			Bitmap bitmap = InfoHelper.getScaleBitmap(mContext, theSmall);

			if (bitmap != null) {
				// imageView.setBackgroundDrawable(null);
				imageView.setImageDrawable(new BitmapDrawable(bitmap));
			}
			// 存储头像
			submitSomething();
		}
		// 修改性别
		else if (requestCode == REQUEST_CODE_UPDATE_SEX) {
			if (resultCode != RESULT_OK) {
				return;
			}
			super.onActivityResult(requestCode, resultCode, data);
			// //修改性别
			// Log.d("getAction", data.getAction());
			userSex.setText(data.getAction());
		} else if (requestCode == REQUEST_CODE_UPDATE_PHONE) {
			if (resultCode != RESULT_OK) {
				return;
			}
			super.onActivityResult(requestCode, resultCode, data);
			// //修改性别
			// Log.d("getAction", data.getAction());
			userPhone.setText(data.getAction());
		}
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(thisLarge)),
						"image/*");
				startActivity(intent);
			}
		});

	}

	/**
	 * 捕捉android.intent.action.SEND，并得到捕捉到的图片路径
	 * 
	 * @return
	 */
	private String getImgPathByCaptureSendFilter() {
		String thisLarge = "";
		Uri mUri = null;
		final Intent intent = getIntent();
		final String action = intent.getAction();
		if (!StringUtils.isBlank(action)
				&& "android.intent.action.SEND".equals(action)) {
			boolean hasExtra = intent.hasExtra("android.intent.extra.STREAM");
			if (hasExtra) {
				mUri = (Uri) intent
						.getParcelableExtra("android.intent.extra.STREAM");
			}

			if (mUri != null) {
				String mUriString = mUri.toString();
				mUriString = Uri.decode(mUriString);

				String pre1 = "file://" + SDCARD + File.separator;
				String pre2 = "file://" + SDCARD_MNT + File.separator;

				if (mUriString.startsWith(pre1)) {
					thisLarge = Environment.getExternalStorageDirectory()
							.getPath()
							+ File.separator
							+ mUriString.substring(pre1.length());
				} else if (mUriString.startsWith(pre2)) {
					thisLarge = Environment.getExternalStorageDirectory()
							.getPath()
							+ File.separator
							+ mUriString.substring(pre2.length());
				} else {
					thisLarge = getAbsoluteImagePath(mUri);
				}
			}
		}
		return thisLarge;
	}
}
