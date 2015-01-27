package com.limon.common;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.limon.make.BMapApi;

public class ResourceUtil {
	public static int getColor(int paramInt) {
		return getResources().getColor(paramInt);
	}

	public static DisplayMetrics getDisplayMetrics() {
		return getResources().getDisplayMetrics();
	}

	public static final Drawable getDrawable(int paramInt) {
		return getResources().getDrawable(paramInt);
	}

	public static final int[] getIntArray(int paramInt) {
		return getResources().getIntArray(paramInt);
	}

	public static final int getPixelDimension(int paramInt) {
		float f = paramInt;
		DisplayMetrics localDisplayMetrics = getDisplayMetrics();
		return (int) TypedValue.applyDimension(1, f, localDisplayMetrics);
	}

	public static final Resources getResources() {
		return BMapApi.instance.getApplicationContext().getResources();
	}

	public static final String getString(int paramInt) {
		return getResources().getString(paramInt);
	}

	public static final String[] getStringArray(int paramInt) {
		return getResources().getStringArray(paramInt);
	}

}
