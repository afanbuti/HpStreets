package com.limon.make;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 日志全文
 * 
 * @author Administrator
 * 
 */
public class BlogDetailActivity extends BaseActivity {
	// private ProgressDialog progressDialog = null;
	private String blogid = "", name = "", title = "", dateline = "",
			message = "", redoc = "";
	// private String uname = "";
	// private String userId = "";
	// private String ref="-1";
	private TextView textViewTitle = null, textViewMessage = null,
			textViewDateline = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailblog);
		// instance = this;
		mContext = getApplicationContext();
		textViewTitle = (TextView) findViewById(R.id.title);
		textViewMessage = (TextView) findViewById(R.id.message);
		textViewDateline = (TextView) findViewById(R.id.dateline);
		blogid = getIntent().getExtras().getString("blogid");
		name = getIntent().getExtras().getString("name");
		title = getIntent().getExtras().getString("title");
		message = getIntent().getExtras().getString("message");
		dateline = getIntent().getExtras().getString("dateline");
		//redoc = getIntent().getExtras().getString("redoc");
		textViewMessage.setText(message);
		textViewTitle.setText(title);
		textViewDateline.setText(dateline);
		//TextView reDoc = (TextView) findViewById(R.id.redoc);
//		reDoc.setText(redoc);
//		if (Integer.parseInt((String) reDoc.getText()) > 0) {
//			reDoc.setVisibility(View.VISIBLE);
//		} else {
//			reDoc.setVisibility(View.GONE);
//		}

		// 浏览评论列表
//		reDoc.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(mContext, BlogCommentListActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.putExtra("blogid", blogid);
//				intent.putExtra("name", name);
//				intent.putExtra("title", title);
//				intent.putExtra("dateline", dateline);
//				mContext.startActivity(intent);
//			}
//		});
//		ImageView age = (ImageView) findViewById(R.id.incr);
//		// 日志评论
//		age.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(mContext, BlogCommentActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.putExtra("blogid", blogid);
//				intent.putExtra("name", name);
//				intent.putExtra("title", title);
//				intent.putExtra("dateline", dateline);
//				mContext.startActivity(intent);
//			}
//		});
		ImageView btnd = (ImageView) findViewById(R.id.decr);
		btnd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
				unregisterReceiver(exitre);
			}
		});

	}

}