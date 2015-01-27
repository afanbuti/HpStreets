package com.limon.common;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;

/**
 * 类说明： 媒体类型工具
 * 
 * @author @Cundong
 * @weibo http://weibo.com/liucundong
 * @blog http://www.liucundong.com
 * @date Apr 29, 2011 2:50:48 PM
 * @version 1.0
 */
public class MediaUtils {
	private static Map<String, String> FORMAT_TO_CONTENTTYPE = new HashMap<String, String>();

	static {
		// 音频
		FORMAT_TO_CONTENTTYPE.put("mp3", "audio");
		FORMAT_TO_CONTENTTYPE.put("mid", "audio");
		FORMAT_TO_CONTENTTYPE.put("midi", "audio");
		FORMAT_TO_CONTENTTYPE.put("asf", "audio");
		FORMAT_TO_CONTENTTYPE.put("wm", "audio");
		FORMAT_TO_CONTENTTYPE.put("wma", "audio");
		FORMAT_TO_CONTENTTYPE.put("wmd", "audio");
		FORMAT_TO_CONTENTTYPE.put("amr", "audio");
		FORMAT_TO_CONTENTTYPE.put("wav", "audio");
		FORMAT_TO_CONTENTTYPE.put("3gpp", "audio");
		FORMAT_TO_CONTENTTYPE.put("mod", "audio");
		FORMAT_TO_CONTENTTYPE.put("mpc", "audio");

		// 视频
		FORMAT_TO_CONTENTTYPE.put("fla", "video");
		FORMAT_TO_CONTENTTYPE.put("flv", "video");
		FORMAT_TO_CONTENTTYPE.put("wav", "video");
		FORMAT_TO_CONTENTTYPE.put("wmv", "video");
		FORMAT_TO_CONTENTTYPE.put("avi", "video");
		FORMAT_TO_CONTENTTYPE.put("rm", "video");
		FORMAT_TO_CONTENTTYPE.put("rmvb", "video");
		FORMAT_TO_CONTENTTYPE.put("3gp", "video");
		FORMAT_TO_CONTENTTYPE.put("mp4", "video");
		FORMAT_TO_CONTENTTYPE.put("mov", "video");

		// flash
		FORMAT_TO_CONTENTTYPE.put("swf", "video");

		FORMAT_TO_CONTENTTYPE.put("null", "video");

		// 图片
		FORMAT_TO_CONTENTTYPE.put("jpg", "photo");
		FORMAT_TO_CONTENTTYPE.put("jpeg", "photo");
		FORMAT_TO_CONTENTTYPE.put("png", "photo");
		FORMAT_TO_CONTENTTYPE.put("bmp", "photo");
		FORMAT_TO_CONTENTTYPE.put("gif", "photo");
	}

	/**
	 * 根据根据扩展名获取类型
	 * 
	 * @param attFormat
	 * @return
	 */
	public static String getContentType(String attFormat) {
		String contentType = FORMAT_TO_CONTENTTYPE.get("null");

		if (attFormat != null) {
			contentType = FORMAT_TO_CONTENTTYPE.get(attFormat.toLowerCase());
		}
		return contentType;
	}

	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	// Create photo icon start
	/**
	 * create the bitmap from a byte array
	 * 
	 * @param src
	 *            the bitmap object you want proecss
	 * @param watermark
	 *            the water mark above the src
	 * @return return a bitmap object ,if paramter's length is 0,return null
	 */
	public static Bitmap createBitmap(Bitmap src, Bitmap watermark, int num) {
		if (src == null) {
			return null;
		}

		int mWidth = src.getWidth();
		int mHeight = src.getHeight();

		int width = watermark.getWidth();
		int height = watermark.getHeight();
		// 设置想要的大小
		int newWidth = 32;
		int newHeight = 32;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm1 = Bitmap.createBitmap(watermark, 0, 0, width, height,
				matrix, true);
		Bitmap newbm = null;
		if (num == 1 || num == 4) {
			newbm = toGrayscale(newbm1);
		} else {
			newbm = newbm1;
		}

		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_4444);
		Canvas canvas = new Canvas(newb);
		// draw src into
		Paint paint = new Paint(Color.GRAY);
		canvas.drawBitmap(src, 0, 0, paint);
		// draw watermark into
		canvas.drawBitmap(newbm, 5, 5, paint);
		// save all clip
		canvas.save(Canvas.ALL_SAVE_FLAG);
		// store
		canvas.restore();
		return newb;
	}
}