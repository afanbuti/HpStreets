package com.limon.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ViewSwitcher.ViewFactory;

import com.limon.make.R;

public class SlideViewFactory implements ViewFactory {
	LayoutInflater mInflater;

	public SlideViewFactory(Context context) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * ���������ǵõ�����Ҫ��ɵ�View������ʵ����ֱ�ӴӲ��ֵõ��� ���Ƕ������һ��GridView
	 * ��һ��GridView������ʾһ����Ӧ�ó���
	 */
	public View makeView() {
		return mInflater.inflate(R.layout.slidelistview, null);
	}
}
