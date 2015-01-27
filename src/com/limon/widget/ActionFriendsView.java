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
import com.limon.make.MyContactFriendsActivity;
import com.limon.make.MyJoinFriendsActivity;
import com.limon.make.MyViewFriendsActivity;
import com.limon.make.R;

public class ActionFriendsView extends LinearLayout {
	// private int max=100;
	// private int incrAmount=1;
	// private int decrAmount=-1;
	// private ProgressBar bar=null;
	private Context mycontext;
	// private View.OnClickListener onIncr = null;
	private View.OnClickListener onDecr = null;

	public ActionFriendsView(Context ctxt, AttributeSet attrs) {
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
				R.layout.viewactionfriends, this);

		// bar=(ProgressBar)findViewById(R.id.bar);
		// bar.setMax(max);
		// ���������б�
		ImageView btn = (ImageView) findViewById(R.id.incr);
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, MyJoinFriendsActivity.class);
				GroupTwo.group.getLocalActivityManager().removeAllActivities();
				View view = GroupTwo.group
						.getLocalActivityManager()
						.startActivity("Two",
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();
				GroupTwo.group.replaceView(view);
				if (onDecr != null) {
					onDecr.onClick(ActionFriendsView.this);
				}
			}
		});
		TextView btnt = (TextView) findViewById(R.id.incrt);
		btnt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, MyJoinFriendsActivity.class);
				GroupTwo.group.getLocalActivityManager().removeAllActivities();
				View view = GroupTwo.group
						.getLocalActivityManager()
						.startActivity("Two",
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();
				GroupTwo.group.replaceView(view);
				if (onDecr != null) {
					onDecr.onClick(ActionFriendsView.this);
				}
			}
		});
		// ͨѶ¼�б�
		ImageView cbtn = (ImageView) findViewById(R.id.con);
		cbtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, MyContactFriendsActivity.class);
				GroupTwo.group.getLocalActivityManager().removeAllActivities();
				View view = GroupTwo.group
						.getLocalActivityManager()
						.startActivity("Two",
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();
				GroupTwo.group.replaceView(view);
				if (onDecr != null) {
					onDecr.onClick(ActionFriendsView.this);
				}
			}
		});
		TextView cbtnt = (TextView) findViewById(R.id.cont);
		cbtnt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, MyContactFriendsActivity.class);
				GroupTwo.group.getLocalActivityManager().removeAllActivities();
				View view = GroupTwo.group
						.getLocalActivityManager()
						.startActivity("Two",
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();
				GroupTwo.group.replaceView(view);
				if (onDecr != null) {
					onDecr.onClick(ActionFriendsView.this);
				}
			}
		});
		// ��ͼģʽ
		ImageView btnd = (ImageView) findViewById(R.id.decr);
		btnd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, MyViewFriendsActivity.class);
				GroupTwo.group.getLocalActivityManager().removeAllActivities();
				View view = GroupTwo.group
						.getLocalActivityManager()
						.startActivity("Two",
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();
				// Again, replace the view
				GroupTwo.group.replaceView(view);
				if (onDecr != null) {
					onDecr.onClick(ActionFriendsView.this);
				}
			}
		});
		TextView btndt = (TextView) findViewById(R.id.decrt);
		btndt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, MyViewFriendsActivity.class);
				GroupTwo.group.getLocalActivityManager().removeAllActivities();
				View view = GroupTwo.group
						.getLocalActivityManager()
						.startActivity("Two",
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();
				// Again, replace the view
				GroupTwo.group.replaceView(view);
				if (onDecr != null) {
					onDecr.onClick(ActionFriendsView.this);
				}
			}
		});
	}
}
