package com.limon.widget;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.limon.make.R;

public class ImageList extends BaseAdapter {
	Activity activity;
	int[] image = { R.drawable.db_badges, R.drawable.db_estates,
			R.drawable.db_friends, R.drawable.db_journal,
			R.drawable.db_nearby_info, R.drawable.db_news,
			R.drawable.db_places, R.drawable.db_pm, R.drawable.db_preference };
	String[] text = { "db_badges", "db_estates", "db_friends", "db_journal",
			"db_nearby_info", "db_news", "db_places", "db_pm", "db_preference" };

	// construct
	public ImageList(Activity a) {
		activity = a;
	}

	public ImageList(Activity a, int res, String[] from, int[] to) {
		activity = a;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return image.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return image[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView iv = new ImageView(activity);
		iv.setImageResource(image[position]);
		return iv;
	}
}