package com.limon.make;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.limon.bean.BlogInfo;
import com.limon.common.FileUtils;
import com.limon.common.InfoHelper;
import com.limon.common.JsonDataPostApi;
import com.limon.common.MediaUtils;
import com.limon.common.ResourceUtil;
import com.limon.common.StringUtils;
import com.limon.myweibo.AuthoSharePreference;
import com.limon.myweibo.MyWeiboManager;
import com.limon.myweibo.WeiboConstParam;
import com.mobclick.android.MobclickAgent;
import com.weibo.net.AccessToken;
import com.weibo.net.WeiboException;
import com.weibo.net.AsyncWeiboRunner.RequestListener;

/**
 * ��־�ύ
 * 
 * @author Administrator
 * 
 */
public class BlogSubmitActivity extends BaseActivity implements
RequestListener{
	ProgressDialog progressDialog = null;
	private Button mButton, rButton;
	private EditText contentEditText = null, subjectEditText = null;
	private ImageButton imgChooseBtn = null;
	private ImageView imgView = null;
	private static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
	private static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
	private String thisLarge = null;// , theSmall = null;
	private String uname = "";
	private String userId = "";
	private String ref = "-1";
	private String bname = "";
	private MyWeiboManager mWeiboManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submitblog);
		setTitle(ResourceUtil.getString(R.string.info_newblog));
		instance = this;
		mContext = getApplicationContext();
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(BlogSubmitActivity.this);
		userId = settings.getString("UserID", "");
		uname = settings.getString("UserName", "");
		if ("".equals(userId)) {
			Toast.makeText(mContext,
					ResourceUtil.getString(R.string.info_nologin),
					Toast.LENGTH_LONG).show();
			startActivity(new Intent(this, LogInActivity.class));
			finish();
			unregisterReceiver(exitre);
		}
		subjectEditText = (EditText) findViewById(R.id.share_subject);
		contentEditText = (EditText) findViewById(R.id.share_content);
		try {
			bname = getIntent().getExtras().getString("bname");
		} catch (Exception e) {
		}
		if (!"".equals(bname)) {
			SimpleDateFormat dateFm = new SimpleDateFormat(" MM-dd HH:mm");
			String dateTime = dateFm.format(new java.util.Date());
			subjectEditText.setText(ResourceUtil.getString(R.string.info_zai)
					+ bname + ResourceUtil.getString(R.string.checkin_ok));
			contentEditText.setText(ResourceUtil.getString(R.string.info_zai)
					+ bname + ResourceUtil.getString(R.string.checkin_ok)
					+ dateTime);
		}

		imgChooseBtn = (ImageButton) findViewById(R.id.share_imagechoose);
		imgView = (ImageView) findViewById(R.id.share_image);

		/* ����mButton��onClick�¼����� */
		mButton = (Button) findViewById(R.id.SignUpButton);
		rButton = (Button) findViewById(R.id.ReturnButton);
		rButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				unregisterReceiver(exitre);
			}
		});
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!InfoHelper.checkNetWork(mContext)) {
					Toast.makeText(mContext,
							ResourceUtil.getString(R.string.error_net),
							Toast.LENGTH_LONG).show();
				} else {
					getSomething();
				}
			}
		});
		imgChooseBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CharSequence[] items = {
						ResourceUtil.getString(R.string.phone_album),
						ResourceUtil.getString(R.string.phone_graph),
						ResourceUtil.getString(R.string.phone_clean) };
				imageChooseItem(items);
			}
		});
		// ����EditText����ı�
		// TextWatcher watcher = new TextWatcher() {
		// public void onTextChanged(CharSequence s, int start, int before,
		// int count) {
		// textCountSet();
		// }
		//
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		// textCountSet();
		// }
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		// textCountSet();
		// }
		// };

		// contentEditText.addTextChangedListener(watcher);
		thisLarge = getImgPathByCaptureSendFilter();
		mWeiboManager = MyWeiboManager.getInstance();
		if (mWeiboManager == null) {
			mWeiboManager = MyWeiboManager.getInstance(
					WeiboConstParam.CONSUMER_KEY,
					WeiboConstParam.CONSUMER_SECRET,
					WeiboConstParam.REDIRECT_URL);
		}
		String token = AuthoSharePreference.getToken(this);
		if(!("".equals(token) || token==null)){
		AccessToken accessToken = new AccessToken(token, WeiboConstParam.CONSUMER_SECRET);     
        mWeiboManager.setAccessToaken(accessToken);
		}
	}

	private void getSomething() {
		progressDialog = ProgressDialog.show(this, "", ResourceUtil
				.getString(R.string.execute), true, false);
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				subjectEditText.setText("");
				contentEditText.setText("");
				thisLarge = null;
				imgView.setBackgroundDrawable(null);
				progressDialog.dismiss();
				if (!"-1".equals(ref)) {
					Toast
							.makeText(
									mContext,
									ResourceUtil.getString(R.string.submit_ok)
											+ String
													.format(
															"\n"
																	+ ResourceUtil
																			.getString(R.string.info_credit),
															"5", "5"),
									Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				if (isChecked()) {
					String title = subjectEditText.getText().toString();
					String msg = contentEditText.getText().toString();

					// try {
					// if (title.getBytes().length != title.length()) {
					// title = URLEncoder.encode(title, "UTF-8");
					// }
					// if (msg.getBytes().length != msg.length()) {
					// msg = URLEncoder.encode(msg, "UTF-8");
					// }
					// } catch (UnsupportedEncodingException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }

					if (!(thisLarge == null || "".equals(thisLarge))) {
						String newName = StringUtils.radomUUID() + "."
								+ FileUtils.getFileFormat(thisLarge);
						// Log.d("uuid=", newName);
						uploadStatus(title, msg, thisLarge, newName);
					} else {
						uploadStatus(title, msg);
					}
				}
				Message message = new Message();
				handler.sendMessage(message);
				Looper.loop();
			}
		}.start();
	}

	private void uploadStatus(String title, String msg) {
		uploadData(title, msg, "");
		try {
			mWeiboManager.update(this, msg, this);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WeiboException e) {
			e.printStackTrace();
			Log.e("", "e.errcode = " + e.getStatusCode());
		}
	}

	private void uploadStatus(String title, String msg, String filePath,
			String newName) {
		boolean bu = FileUtils.uploadFile(newName, filePath);
		if (bu) {
			uploadData(title, msg, newName);
			try {
				mWeiboManager.upload(this, msg, filePath, this);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (WeiboException e) {
				e.printStackTrace();
				Log.e("", "e.errcode = " + e.getStatusCode());
			}
		}
	}

	private void uploadData(String title, String msg, String newName) {

		BlogInfo blog = new BlogInfo();
		blog.setTitle(title);
		blog.setContent(msg);
		blog.setUid(userId);
		blog.setUsername(uname);
		blog.setPic(newName);

		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		String sJson = gson.toJson(blog);
		sJson = StringUtils.encode(sJson);
		Map<String, String> params = new HashMap<String, String>();
		params.put("u", sJson);

		JsonDataPostApi api = new JsonDataPostApi();
		try {
			ref = api.makeRequest("b.ashx?op=z", params);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					ResourceUtil.getString(R.string.error_net),
					Toast.LENGTH_LONG).show();
			MobclickAgent.reportError(mContext, "BlogSubmit_uploadData:"
					+ e.toString());
		}
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(BlogSubmitActivity.this);
		Editor editor = sharedPreferences.edit();
		editor.putInt("Credit", sharedPreferences.getInt("Credit", 0) + 5);
		editor.putInt("Experience",
				sharedPreferences.getInt("Experience", 0) + 5);
		editor.commit();
	}

	/**
	 * ���ø������
	 */
	// private void textCountSet() {
	// String textContent = contentEditText.getText().toString();
	// int currentLength = textContent.length();
	// if (currentLength <= 140) {
	// wordCounterTextView.setTextColor(Color.BLACK);
	// wordCounterTextView.setText(String.valueOf(textContent.length()));
	// } else {
	// wordCounterTextView.setTextColor(Color.RED);
	// wordCounterTextView.setText(String.valueOf(140 - currentLength));
	// }
	// }

	/**
	 * ����ѡ��
	 * 
	 * @param items
	 */
	public void imageChooseItem(CharSequence[] items) {
		AlertDialog imageDialog = new AlertDialog.Builder(instance).setTitle(
				ResourceUtil.getString(R.string.phone_add)).setItems(items,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// �ֻ�ѡͼ
						if (item == 0) {
							Intent intent = new Intent(
									Intent.ACTION_GET_CONTENT);
							intent.setType("image/*");
							startActivityForResult(intent,
									REQUEST_CODE_GETIMAGE_BYSDCARD);
						}
						// ����
						else if (item == 1) {
							// Intent intent = new Intent(
							// "android.media.action.IMAGE_CAPTURE");

							// String camerName = InfoHelper.getFileName();
							// String fileName = "Share" + camerName + ".tmp";

							// File camerFile = new
							// File(InfoHelper.getCamerPath(), fileName);

							// theSmall = InfoHelper.getCamerPath() + fileName;
							// thisLarge = getLatestImage();

							// Uri originalUri = Uri.fromFile(camerFile);
							// intent.putExtra(MediaStore.EXTRA_OUTPUT,originalUri);
							Intent intent = new Intent(
									android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(intent,
									REQUEST_CODE_GETIMAGE_BYCAMERA);
							startActivityForResult(intent,
									REQUEST_CODE_GETIMAGE_BYCAMERA);
						} else if (item == 2) {
							thisLarge = null;
							imgView.setBackgroundDrawable(null);
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

			// ����Ǳ�׼Uri
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

			// Bitmap bitmap = loadImgThumbnail(imgName,
			// MediaStore.Images.Thumbnails.MICRO_KIND);
			Bitmap bitmap = InfoHelper.getScaleBitmap(mContext, thisLarge);
			if (bitmap != null) {
				imgView.setImageDrawable(new BitmapDrawable(bitmap));
				// imgView.setBackgroundDrawable(new BitmapDrawable(bitmap));
			}
		}
		// ����ͼƬ
		else if (requestCode == REQUEST_CODE_GETIMAGE_BYCAMERA) {
			if (resultCode != RESULT_OK) {
				return;
			}

			super.onActivityResult(requestCode, resultCode, data);
			Bundle extras = data.getExtras();
			Bitmap b = (Bitmap) extras.get("data");
			if (b != null) {
				imgView.setBackgroundDrawable(new BitmapDrawable(b));
			}
			ContentValues values = new ContentValues();
			values.put(MediaColumns.TITLE, "title");
			values.put(ImageColumns.BUCKET_ID, "test");
			values.put(ImageColumns.DESCRIPTION, "test Image taken");
			values.put(MediaColumns.MIME_TYPE, "image/jpeg");
			Uri uri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,
					values);
			OutputStream outstream;
			try {
				outstream = getContentResolver().openOutputStream(uri);
				b.compress(Bitmap.CompressFormat.JPEG, 70, outstream);
				outstream.close();
			} catch (FileNotFoundException e) {
				//
			} catch (IOException e) {
				//
			}
			thisLarge = getLatestImage();
			// Bitmap bitmap = InfoHelper.getScaleBitmap(mContext, theSmall);
			//
			// if (bitmap != null) {
			// imgView.setBackgroundDrawable(new BitmapDrawable(bitmap));
			// }
		}

		imgView.setOnClickListener(new OnClickListener() {
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
	 * ��ݺϷ����ж�
	 * 
	 * @return
	 */
	private boolean isChecked() {
		boolean ret = true;
		if (StringUtils.isBlank(subjectEditText.getText().toString())) {
			Toast.makeText(mContext,
					ResourceUtil.getString(R.string.doing_content),
					Toast.LENGTH_SHORT).show();
			ret = false;
		} else if (StringUtils.isBlank(contentEditText.getText().toString())) {
			Toast.makeText(mContext,
					ResourceUtil.getString(R.string.doing_content),
					Toast.LENGTH_SHORT).show();
			ret = false;
		}
		return ret;
	}

	/**
	 * ��׽android.intent.action.SEND�����õ���׽����ͼƬ·��
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