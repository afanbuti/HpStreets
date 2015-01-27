package com.limon.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.limon.make.GroupTwo;
import com.limon.make.MyTextFriendsActivity;
import com.limon.make.R;

public class ActionFriendsMapView extends LinearLayout {
	// private int max=100;
	// private int incrAmount=1;
	// private int decrAmount=-1;
	// private ProgressBar bar=null;
	private Context mycontext;
	// private View.OnClickListener onIncr = null;
	private View.OnClickListener onDecr = null;

	public ActionFriendsMapView(Context ctxt, AttributeSet attrs) {
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
				R.layout.viewactionreturn, this);

		// ImageView btn=(ImageView)findViewById(R.id.incr);
		// btn.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// //bar.incrementProgressBy(incrAmount);
		// Intent intent = new Intent();
		// intent.setClass(mycontext, BlogSubmitActivity.class);
		// mycontext.startActivity(intent);
		// if (onIncr!=null) {
		// onIncr.onClick(ActionFriendsMapView.this);
		// }
		// }
		// });

		ImageView btnd = (ImageView) findViewById(R.id.decr);
		btnd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, MyTextFriendsActivity.class);
				// mycontext.startActivity(intent);
				GroupTwo.group.getLocalActivityManager().removeAllActivities();
				View view = GroupTwo.group
						.getLocalActivityManager()
						.startActivity("Two",
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();
				// Again, replace the view
				GroupTwo.group.replaceView(view);
				if (onDecr != null) {
					onDecr.onClick(ActionFriendsMapView.this);
				}
			}
		});
		TextView btndt = (TextView) findViewById(R.id.decrt);
		btndt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, MyTextFriendsActivity.class);
				// mycontext.startActivity(intent);
				GroupTwo.group.getLocalActivityManager().removeAllActivities();
				View view = GroupTwo.group
						.getLocalActivityManager()
						.startActivity("Two",
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();
				// Again, replace the view
				GroupTwo.group.replaceView(view);
				if (onDecr != null) {
					onDecr.onClick(ActionFriendsMapView.this);
				}
			}
		});
	}
}
