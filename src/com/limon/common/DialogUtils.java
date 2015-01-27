package com.limon.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

import com.limon.make.R;

/**
 * 类说明： 对话框弹出帮助类
 * 
 * @version 1.0
 */
public class DialogUtils {
	/**
	 * 弹出询问窗口
	 * 
	 * @param
	 * @param
	 */
	public static void dialogBuilder(Activity instance, String title,
			String message, final DialogCallBack callBack) {
		AlertDialog.Builder builder = new Builder(instance);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton(ResourceUtil.getString(R.string.infosubmit),
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						callBack.callBack();
					}
				});

		builder.setNegativeButton(ResourceUtil.getString(R.string.infocancel),
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.create().show();
	}

	public interface DialogCallBack {
		public void callBack();
	}
}