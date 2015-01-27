package com.limon.widget;

import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * �Զ�������Preference��category��googleĬ�ϵ�Category�޷��ṩ�޸���ʽ�Ľӿ�
 * 
 * @author limon
 * 
 */
public class MyPreferenceCategory extends PreferenceCategory {
	public MyPreferenceCategory(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
		view.setBackgroundColor(Color.DKGRAY);
		if (view instanceof TextView) {
			TextView tv = (TextView) view;
			tv.setTextSize(18);
		}
	}
}
