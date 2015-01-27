package com.limon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.limon.make.R;

public class InfoView extends LinearLayout {
	private String Text = "";

	public InfoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(LinearLayout.HORIZONTAL);
		// int resouceId = -1;
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Meter,
				0, 0);
		Text = a.getString(R.styleable.Meter_Text);
		a.recycle();
		TextView tv = new TextView(context);
		// EditText et = new EditText(context);

		// resouceId = attrs.getAttributeResourceValue(null, "Text", 0);
		// if (resouceId > 0) {
		// Text = context.getResources().getText(resouceId).toString();
		// } else {
		// Text = "";
		// }
		// Log.d("Info", Text);
		tv.setText(Text);
		tv.setTextColor(0xffffd101);
		addView(tv);
		// addView(et, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT));
		this.setGravity(LinearLayout.VERTICAL);
	}

}
