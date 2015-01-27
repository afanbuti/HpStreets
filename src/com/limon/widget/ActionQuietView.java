package com.limon.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.limon.make.GroupFour;
import com.limon.make.MyInfoActivity;
import com.limon.make.R;

public class ActionQuietView extends LinearLayout {
	// private int max=100;
	// private int incrAmount=1;
	// private int decrAmount=-1;
	// private ProgressBar bar=null;
	private Context mycontext;
	// private View.OnClickListener onIncr = null;
	private View.OnClickListener onDecr = null;

	public ActionQuietView(Context ctxt, AttributeSet attrs) {
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

		// bar=(ProgressBar)findViewById(R.id.bar);
		// bar.setMax(max);

		// ImageButton btn=(ImageButton)findViewById(R.id.incr);
		//		
		// btn.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// //bar.incrementProgressBy(incrAmount);
		// Intent intent = new Intent();
		// intent.setClass(mycontext, DoingSubmitActivity.class);
		// mycontext.startActivity(intent);
		// if (onIncr!=null) {
		// onIncr.onClick(ActionQuietView.this);
		// }
		// }
		// });

		ImageView btnd = (ImageView) findViewById(R.id.decr);
		btnd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, MyInfoActivity.class);
				// mycontext.startActivity(intent);
				GroupFour.group.getLocalActivityManager().removeAllActivities();
				View view = GroupFour.group
						.getLocalActivityManager()
						.startActivity("Four",
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();
				// Again, replace the view
				GroupFour.group.replaceView(view);
				if (onDecr != null) {
					onDecr.onClick(ActionQuietView.this);
				}
			}
		});
		TextView btndt = (TextView) findViewById(R.id.decrt);
		btndt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, MyInfoActivity.class);
				// mycontext.startActivity(intent);
				GroupFour.group.getLocalActivityManager().removeAllActivities();
				View view = GroupFour.group
						.getLocalActivityManager()
						.startActivity("Four",
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();
				// Again, replace the view
				GroupFour.group.replaceView(view);
				if (onDecr != null) {
					onDecr.onClick(ActionQuietView.this);
				}
			}
		});
	}
}
