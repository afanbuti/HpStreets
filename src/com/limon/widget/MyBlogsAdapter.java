package com.limon.widget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.limon.common.AsyncImageLoader;
import com.limon.common.AsyncImageLoader.ImageCallBack;
import com.limon.make.BMapApi;
import com.limon.make.BlogCommentActivity;
import com.limon.make.BlogCommentListActivity;
import com.limon.make.BlogDetailActivity;
import com.limon.make.R;

/**
 * 日志列表适配器
 * 
 * @author Administrator
 * 
 */
public class MyBlogsAdapter extends SimpleAdapter {
	// private int[] colors = new int[] { 0xffeeeeee, 0xffcdcacd };
	private List<? extends Map<String, ?>> mydata = null;
	private Context mycontext;
	// private CheckBox cb;
	private TextView reDoc;// , readAll;
	// private ImageView age, userImage;
	private AsyncImageLoader asyncImageLoader;
	private Drawable cachedImage;
	private String headimg;
	public HashMap<String, String> state = new HashMap<String, String>();

	public MyBlogsAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		mydata = data;
		mycontext = context;
		asyncImageLoader = new AsyncImageLoader();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// View view = super.getView(position, convertView, parent);
		// int colorPos = position % colors.length;
		// view.setBackgroundColor(colors[colorPos]);
		final int p = position;
		ViewCache viewCache = null;
		if (convertView == null) {
			viewCache = new ViewCache(mycontext);
			convertView = LayoutInflater.from(mycontext).inflate(
					R.layout.myitemblog, null);
			// viewCache.imageView = (ImageView) convertView
			// .findViewById(R.id.msgimg);
			viewCache.reDoc = (TextView) convertView.findViewById(R.id.redoc);
			viewCache.readAll = (TextView) convertView.findViewById(R.id.decr);
			viewCache.nameTV = (TextView) convertView.findViewById(R.id.name);
			viewCache.msgTV = (TextView) convertView.findViewById(R.id.message);
			viewCache.dateTV = (TextView) convertView
					.findViewById(R.id.dateline);
			viewCache.age = (ImageView) convertView.findViewById(R.id.incr);
			viewCache.userImage = (ImageView) convertView
					.findViewById(R.id.image);
			viewCache.cb = (CheckBox) convertView.findViewById(R.id.cb);
			convertView.setTag(viewCache);

		} else {
			viewCache = (ViewCache) convertView.getTag();
		}
		viewCache.nameTV.setText((String) mydata.get(p).get("name"));
		viewCache.msgTV.setText((String) mydata.get(p).get("message"));
		viewCache.dateTV.setText((String) mydata.get(p).get("dateline"));
		// reDoc = (TextView) view.findViewById(R.id.redoc);
		// readAll = (TextView) view.findViewById(R.id.decr);
		// age = (ImageView) view.findViewById(R.id.incr);
		// userImage = (ImageView) view.findViewById(R.id.image);
		// cb = (CheckBox) view.findViewById(R.id.cb);
		if (((String) mydata.get(p).get("ormessage")).length() > 140) {
			viewCache.readAll.setVisibility(View.VISIBLE);
		} else {
			viewCache.readAll.setVisibility(View.GONE);
		}
		if (Integer.parseInt((String) mydata.get(p).get("redoc")) > 0) {
			viewCache.reDoc.setText((String) mydata.get(p).get("redoc"));
			viewCache.reDoc.setVisibility(View.VISIBLE);
		} else {
			viewCache.reDoc.setVisibility(View.GONE);
		}
		headimg = (String) mydata.get(p).get("headimg");
		if ("".equals(headimg)) {
			if ("0".equals(mydata.get(p).get("sex")))
				viewCache.userImage.setImageResource(R.drawable.blank_girl);
			else
				viewCache.userImage.setImageResource(R.drawable.blank_boy);
		} else {
			// final ImageView usrView = (ImageView) convertView
			// .findViewById(R.id.image);
			cachedImage = asyncImageLoader.loadDrawable(BMapApi.getInstance()
					.getImageUrl()
					+ headimg + ".thumb.jpg", viewCache.userImage,
					new ImageCallBack() {
						@Override
						public void imageLoad(Drawable imageDrawable,
								ImageView imageView, String imageUrl) {
							imageView.setImageDrawable(imageDrawable);
						}
					});
			if (cachedImage == null) {
				if ("0".equals(mydata.get(p).get("sex")))
					viewCache.userImage.setImageResource(R.drawable.blank_girl);
				else
					viewCache.userImage.setImageResource(R.drawable.blank_boy);
			} else {
				viewCache.userImage.setImageDrawable(cachedImage);
			}
		}
		// 阅读全文
		viewCache.readAll.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, BlogDetailActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("blogid", (String) mydata.get(p).get("blogid"));
				intent.putExtra("name", (String) mydata.get(p).get("name"));
				intent.putExtra("title", (String) mydata.get(p).get("title"));
				intent.putExtra("dateline", (String) mydata.get(p).get(
						"dateline"));
				intent.putExtra("message", (String) mydata.get(p).get(
						"ormessage"));
				intent.putExtra("redoc", (String) reDoc.getText());
				mycontext.startActivity(intent);
			}
		});
		// 浏览评论列表
		viewCache.reDoc.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, BlogCommentListActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("blogid", (String) mydata.get(p).get("blogid"));
				intent.putExtra("name", (String) mydata.get(p).get("name"));
				intent.putExtra("title", (String) mydata.get(p).get("title"));
				intent.putExtra("dateline", (String) mydata.get(p).get(
						"dateline"));
				mycontext.startActivity(intent);
			}
		});
		// 日志评论
		viewCache.age.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, BlogCommentActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("blogid", (String) mydata.get(p).get("blogid"));
				intent.putExtra("uid", (String) mydata.get(p).get("uid"));
				intent.putExtra("name", (String) mydata.get(p).get("name"));
				intent.putExtra("title", (String) mydata.get(p).get("title"));
				intent.putExtra("dateline", (String) mydata.get(p).get(
						"dateline"));
				mycontext.startActivity(intent);
			}
		});
		// 用户头像点击弹出资料查看并添加好友
		// userImage.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// Intent intent = new Intent();
		// intent.setClass(mycontext, YourProfileActivity.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// intent.putExtra("userId", (String) mydata.get(p).get("uid"));
		// intent.putExtra("name", (String) mydata.get(p).get("name"));
		// intent.putExtra("headimg", (String) mydata.get(p)
		// .get("headimg"));
		// intent.putExtra("sex", (String) mydata.get(p).get("sex"));
		// intent.putExtra("type", "blog");
		// mycontext.startActivity(intent);
		// }
		// });
		viewCache.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// Log.d("position","position="+position);
				if (isChecked) {
					// Log.d((String) mydata.get(p).get("blogid"),(String)
					// mydata.get(p).get("message"));
					state.put((String) mydata.get(p).get("blogid"),
							(String) mydata.get(p).get("message"));
				} else {
					state.remove(p);
				}
			}
		});
		viewCache.cb.setChecked((state.get(position) == null ? false : true));
		return convertView;
	}
}
