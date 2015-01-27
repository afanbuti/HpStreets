package com.limon.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.limon.bean.MenuData.MenuDataOneScreen;
import com.limon.make.R;

public class OneScreenListAdapter extends BaseAdapter {
	private MenuDataOneScreen mScreen;
	private LayoutInflater mInflater;

	public OneScreenListAdapter(Context context) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/** ���ｫ���ݸ���Adapter */
	public void setScreenData(MenuDataOneScreen screenData) {
		mScreen = screenData;
	}

	public int getCount() {
		return mScreen.mDataItems.size();
	}

	public Object getItem(int position) {
		return mScreen.mDataItems.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/** �ú����н����ݺ�View���й��� */
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.labelicon, null);
		}

		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		TextView textView = (TextView) view.findViewById(R.id.textview);
		imageView.setImageDrawable(mScreen.mDataItems.get(position).drawable);
		textView.setText(mScreen.mDataItems.get(position).dataName);

		return view;
	}

}
