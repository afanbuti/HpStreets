package com.limon.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.limon.make.GroupThree;
import com.limon.make.MyViewLocatActivity;
import com.limon.make.PlaceSubmitActivity;
import com.limon.make.R;

public class ActionPlaceView extends LinearLayout {
	// private int max=100;
	// private int incrAmount=1;
	// private int decrAmount=-1;
	// private ProgressBar bar=null;
	private Context mycontext;
	private View.OnClickListener onIncr = null;
	private View.OnClickListener onDecr = null;

	public ActionPlaceView(Context ctxt, AttributeSet attrs) {
		super(ctxt, attrs);
		this.setOrientation(HORIZONTAL);
		mycontext = ctxt;
		// TypedArray a=ctxt.obtainStyledAttributes(attrs,R.styleable.Meter,0,
		// 0);

		// max=a.getInt(R.styleable.Meter_max, 100);
		// incrAmount=a.getInt(R.styleable.Meter_incr, 1);
		// decrAmount=-1*a.getInt(R.styleable.Meter_decr, 1);

		// a.recycle();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		((Activity) getContext()).getLayoutInflater().inflate(
				R.layout.viewactionplace, this);

		// bar=(ProgressBar)findViewById(R.id.bar);
		// bar.setMax(max);

		ImageView btn = (ImageView) findViewById(R.id.incr);
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// bar.incrementProgressBy(incrAmount);
				Intent intent = new Intent();
				intent.setClass(mycontext, PlaceSubmitActivity.class);
				mycontext.startActivity(intent);
				if (onIncr != null) {
					onIncr.onClick(ActionPlaceView.this);
				}
			}
		});
		TextView btnt = (TextView) findViewById(R.id.incrt);
		btnt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// bar.incrementProgressBy(incrAmount);
				Intent intent = new Intent();
				intent.setClass(mycontext, PlaceSubmitActivity.class);
				mycontext.startActivity(intent);
				if (onIncr != null) {
					onIncr.onClick(ActionPlaceView.this);
				}
			}
		});
		ImageView btnd = (ImageView) findViewById(R.id.decr);
		btnd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, MyViewLocatActivity.class);
				// mycontext.startActivity(intent);
				View view = GroupThree.group
						.getLocalActivityManager()
						.startActivity("Three",
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();
				// Again, replace the view
				GroupThree.group.replaceView(view);
				if (onDecr != null) {
					onDecr.onClick(ActionPlaceView.this);
				}
			}
		});
		TextView btndt = (TextView) findViewById(R.id.decrt);
		btndt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, MyViewLocatActivity.class);
				// mycontext.startActivity(intent);
				View view = GroupThree.group
						.getLocalActivityManager()
						.startActivity("Three",
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();
				// Again, replace the view
				GroupThree.group.replaceView(view);
				if (onDecr != null) {
					onDecr.onClick(ActionPlaceView.this);
				}
			}
		});
	}
}
