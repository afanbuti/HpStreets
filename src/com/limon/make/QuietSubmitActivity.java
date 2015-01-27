package com.limon.make;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.limon.bean.BlogInfo;
import com.limon.common.FileUtils;
import com.limon.common.InfoHelper;
import com.limon.common.JsonDataPostApi;
import com.limon.common.ResourceUtil;
import com.limon.common.StringUtils;
import com.mobclick.android.MobclickAgent;

/**
 * 悄悄话提交
 * 
 * @author Administrator
 * 
 */
public class QuietSubmitActivity extends BaseActivity {
	private ProgressDialog progressDialog = null;
	private Button mButton, rButton;
	private EditText contentEditText = null;
	// private ImageButton imgChooseBtn = null;
	// private ImageView imgView = null;
	private TextView wordCounterTextView = null;
	// private static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
	// private static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
	private String thisLarge = null;// , theSmall = null;
	private String uname = "";
	private String userId = "", myid = "";
	private String ref = "-1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submitquiet);
		setTitle(ResourceUtil.getString(R.string.info_newquiet));
		instance = this;
		mContext = getApplicationContext();
		contentEditText = (EditText) findViewById(R.id.share_content);
		// imgChooseBtn = (ImageButton) findViewById(R.id.share_imagechoose);
		// imgView = (ImageView) findViewById(R.id.share_image);
		wordCounterTextView = (TextView) findViewById(R.id.share_word_counter);

		rButton = (Button) findViewById(R.id.ReturnButton);
		mButton = (Button) findViewById(R.id.SignUpButton);

		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!InfoHelper.checkNetWork(mContext)) {
					Toast.makeText(mContext,
							ResourceUtil.getString(R.string.error_net),
							Toast.LENGTH_LONG).show();
				} else {
					submitSomething();
				}
			}
		});
		rButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				unregisterReceiver(exitre);
			}
		});
		// imgChooseBtn.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// CharSequence[] items = {
		// ResourceUtil.getString(R.string.phone_album),
		// ResourceUtil.getString(R.string.phone_graph),
		// ResourceUtil.getString(R.string.phone_clean)};
		// imageChooseItem(items);
		// }
		// });
		// 侦听EditText字数改变
		TextWatcher watcher = new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				textCountSet();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				textCountSet();
			}

			@Override
			public void afterTextChanged(Editable s) {
				textCountSet();
			}
		};

		contentEditText.addTextChangedListener(watcher);
		// thisLarge = getImgPathByCaptureSendFilter();

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(QuietSubmitActivity.this);
		myid = settings.getString("UserID", "");
		uname = settings.getString("UserName", "");
		userId = getIntent().getExtras().getString("userId");
	}

	private void submitSomething() {
		progressDialog = ProgressDialog.show(this, "", ResourceUtil
				.getString(R.string.execute), true, false);
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				contentEditText.setText("");
				thisLarge = null;
				// imgView.setBackgroundDrawable(null);
				progressDialog.dismiss();
				if (!"-1".equals(ref)) {
					Toast.makeText(mContext,
							ResourceUtil.getString(R.string.submit_ok),
							Toast.LENGTH_SHORT).show();
				}

			}
		};
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				if (isChecked()) {
					String msg = contentEditText.getText().toString();
					// if (msg.getBytes().length != msg.length()) {
					// try {
					// msg = URLEncoder.encode(msg, "UTF-8");
					// } catch (UnsupportedEncodingException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					// }
					if (!(thisLarge == null || "".equals(thisLarge))) {
						String newName = StringUtils.radomUUID() + "."
								+ FileUtils.getFileFormat(thisLarge);
						// Log.d("uuid=", newName);
						uploadStatus(msg, thisLarge, newName);
					} else {
						uploadStatus(msg);
					}
				}
				Message message = new Message();
				handler.sendMessage(message);
				Looper.loop();
			}
		}.start();
	}

	private void uploadStatus(String msg) {
		uploadData(msg, "");
	}

	private void uploadStatus(String msg, String filePath, String newName) {
		uploadData(msg, newName);
		FileUtils.uploadFile(newName, filePath);
	}

	private void uploadData(String msg, String newName) {

		BlogInfo blog = new BlogInfo();
		blog.setContent(msg);
		blog.setUid(myid);
		blog.setUsername(uname);
		blog.setBlogid(userId);

		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		String sJson = gson.toJson(blog);
		sJson = StringUtils.encode(sJson);
		Map<String, String> params = new HashMap<String, String>();
		params.put("u", sJson);

		JsonDataPostApi api = new JsonDataPostApi();
		try {
			ref = api.makeRequest("b.ashx?op=q", params);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					ResourceUtil.getString(R.string.error_net),
					Toast.LENGTH_LONG).show();
			MobclickAgent.reportError(mContext, "QuietSubmit_uploadData:"
					+ e.toString());
		}
	}

	/**
	 * 设置稿件字数
	 */
	private void textCountSet() {
		String textContent = contentEditText.getText().toString();
		int currentLength = textContent.length();
		if (currentLength <= 140) {
			wordCounterTextView.setTextColor(Color.BLACK);
			wordCounterTextView.setText(String.valueOf(textContent.length())
					+ " / 140");
		} else {
			wordCounterTextView.setTextColor(Color.RED);
			wordCounterTextView.setText(String.valueOf(140 - currentLength));
		}
	}

	/**
	 * 操作选择
	 * 
	 * @param items
	 */
	// public void imageChooseItem(CharSequence[] items) {
	// AlertDialog imageDialog = new AlertDialog.Builder(instance).setTitle(
	// ResourceUtil.getString(R.string.phone_add)).setItems(items, new
	// DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int item) {
	// // 手机选图
	// if (item == 0) {
	// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	// intent.setType("image/*");
	// startActivityForResult(intent,
	// REQUEST_CODE_GETIMAGE_BYSDCARD);
	// }
	// // 拍照
	// else if (item == 1) {
	// Intent intent = new Intent(
	// "android.media.action.IMAGE_CAPTURE");
	//
	// String camerName = InfoHelper.getFileName();
	// String fileName = "Share" + camerName + ".tmp";
	//
	// File camerFile = new File(InfoHelper.getCamerPath(),
	// fileName);
	//
	// theSmall = InfoHelper.getCamerPath() + fileName;
	// thisLarge = getLatestImage();
	//
	// Uri originalUri = Uri.fromFile(camerFile);
	// intent.putExtra(MediaStore.EXTRA_OUTPUT, originalUri);
	// startActivityForResult(intent,
	// REQUEST_CODE_GETIMAGE_BYCAMERA);
	// } else if (item == 2) {
	// thisLarge = null;
	// imgView.setBackgroundDrawable(null);
	// }
	// }
	// }).create();
	//
	// imageDialog.show();
	// }

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// if (requestCode == REQUEST_CODE_GETIMAGE_BYSDCARD) {
	// if (resultCode != RESULT_OK) {
	// return;
	// }
	//
	// if (data == null)
	// return;
	//
	// Uri thisUri = data.getData();
	// String thePath = InfoHelper
	// .getAbsolutePathFromNoStandardUri(thisUri);
	//
	// // 如果是标准Uri
	// if (StringUtils.isBlank(thePath)) {
	// thisLarge = getAbsoluteImagePath(thisUri);
	// } else {
	// thisLarge = thePath;
	// }
	//
	// String attFormat = FileUtils.getFileFormat(thisLarge);
	// if (!"photo".equals(MediaUtils.getContentType(attFormat))) {
	// Toast.makeText(mContext, ResourceUtil.getString(R.string.phone_nofind),
	// Toast.LENGTH_SHORT).show();
	// return;
	// }
	// String imgName = FileUtils.getFileName(thisLarge);
	//
	// Bitmap bitmap = loadImgThumbnail(imgName,
	// MediaStore.Images.Thumbnails.MICRO_KIND);
	// if (bitmap != null) {
	// imgView.setBackgroundDrawable(new BitmapDrawable(bitmap));
	// }
	// }
	// // 拍摄图片
	// else if (requestCode == REQUEST_CODE_GETIMAGE_BYCAMERA) {
	// if (resultCode != RESULT_OK) {
	// return;
	// }
	//
	// super.onActivityResult(requestCode, resultCode, data);
	//
	// Bitmap bitmap = InfoHelper.getScaleBitmap(mContext, theSmall);
	//
	// if (bitmap != null) {
	// imgView.setBackgroundDrawable(new BitmapDrawable(bitmap));
	// }
	// }
	//
	// imgView.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// Intent intent = new Intent();
	// intent.setAction(android.content.Intent.ACTION_VIEW);
	// intent.setDataAndType(Uri.fromFile(new File(thisLarge)),
	// "image/*");
	// startActivity(intent);
	// }
	// });
	// }

	/**
	 * 数据合法性判断
	 * 
	 * @return
	 */
	private boolean isChecked() {
		boolean ret = true;
		if (StringUtils.isBlank(contentEditText.getText().toString())) {
			Toast.makeText(mContext,
					ResourceUtil.getString(R.string.doing_content),
					Toast.LENGTH_SHORT).show();
			ret = false;
		} else if (contentEditText.getText().toString().length() > 140) {
			int currentLength = contentEditText.getText().toString().length();

			Toast.makeText(
					mContext,
					ResourceUtil.getString(R.string.error_overflow)
							+ (currentLength - 140)
							+ ResourceUtil.getString(R.string.flowword),
					Toast.LENGTH_SHORT).show();
			ret = false;
		}
		return ret;
	}

	/**
	 * 捕捉android.intent.action.SEND，并得到捕捉到的图片路径
	 * 
	 * @return
	 */
	// private String getImgPathByCaptureSendFilter() {
	// String thisLarge = "";
	// Uri mUri = null;
	// final Intent intent = getIntent();
	// final String action = intent.getAction();
	// if (!StringUtils.isBlank(action)
	// && "android.intent.action.SEND".equals(action)) {
	// boolean hasExtra = intent.hasExtra("android.intent.extra.STREAM");
	// if (hasExtra) {
	// mUri = (Uri) intent
	// .getParcelableExtra("android.intent.extra.STREAM");
	// }
	//
	// if (mUri != null) {
	// String mUriString = mUri.toString();
	// mUriString = Uri.decode(mUriString);
	//
	// String pre1 = "file://" + SDCARD + File.separator;
	// String pre2 = "file://" + SDCARD_MNT + File.separator;
	//
	// if (mUriString.startsWith(pre1)) {
	// thisLarge = Environment.getExternalStorageDirectory()
	// .getPath()
	// + File.separator
	// + mUriString.substring(pre1.length());
	// } else if (mUriString.startsWith(pre2)) {
	// thisLarge = Environment.getExternalStorageDirectory()
	// .getPath()
	// + File.separator
	// + mUriString.substring(pre2.length());
	// } else {
	// thisLarge = getAbsoluteImagePath(mUri);
	// }
	// }
	// }
	// return thisLarge;
	// }

}
