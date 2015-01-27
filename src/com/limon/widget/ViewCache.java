package com.limon.widget;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewCache extends LinearLayout {
	// private View baseView;
	public TextView reDoc, readAll, nameTV, msgTV, dateTV;
	public ImageView imageView, age, userImage;
	public CheckBox cb;

	public ViewCache(Context context) {
		super(context);
		// ((LayoutInflater) context
		// .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
		// R.layout.itemdoing, this);
		// imageView = (ImageView) findViewById(R.id.msgimg);
		// reDoc = (TextView) findViewById(R.id.redoc);
		// reImg = (TextView) findViewById(R.id.decr);
		// age = (ImageView) findViewById(R.id.incr);
		// userImage = (ImageView) findViewById(R.id.image);

	}
	// public ViewCache(View baseView) {
	// this.baseView = baseView;
	// }
	// public TextView getTextView() {
	// if (textView == null) {
	// textView = (TextView) baseView.findViewById(R.id.text);
	// }
	// return titleView;
	// }

	// public ImageView getImageView() {
	// if (imageView == null) {
	// imageView = (ImageView) baseView.findViewById(R.id.image);
	// }
	// return imageView;
	// }
}
