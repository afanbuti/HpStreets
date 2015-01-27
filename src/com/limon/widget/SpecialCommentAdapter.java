package com.limon.widget;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.limon.common.AsyncImageLoader;
import com.limon.common.AsyncImageLoader.ImageCallBack;
import com.limon.make.BMapApi;
import com.limon.make.R;
import com.limon.make.YourProfileActivity;

/**
 * 新鲜事回复列表适配器
 * 
 * @author Administrator
 * 
 */
public class SpecialCommentAdapter extends SimpleAdapter {
	// private int[] colors = new int[] { 0xffeeeeee, 0xffcdcacd };
	private List<? extends Map<String, ?>> mydata = null;
	private Context mycontext;
	// private TextView redoc;
	// private ImageView age, userImage;
	private AsyncImageLoader asyncImageLoader;
	private Drawable cachedImage;
	private String headimg;

	public SpecialCommentAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
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
					R.layout.itemdoing, null);
			// viewCache.imageView = (ImageView) convertView
			// .findViewById(R.id.msgimg);
			viewCache.reDoc = (TextView) convertView.findViewById(R.id.redoc);
			// viewCache.reImg = (TextView) convertView.findViewById(R.id.decr);
			viewCache.nameTV = (TextView) convertView.findViewById(R.id.name);
			viewCache.msgTV = (TextView) convertView.findViewById(R.id.message);
			viewCache.dateTV = (TextView) convertView
					.findViewById(R.id.dateline);
			viewCache.age = (ImageView) convertView.findViewById(R.id.incr);
			viewCache.userImage = (ImageView) convertView
					.findViewById(R.id.image);
			convertView.setTag(viewCache);

		} else {
			viewCache = (ViewCache) convertView.getTag();
		}
		// redoc = (TextView) view.findViewById(R.id.redoc);
		// age = (ImageView) view.findViewById(R.id.incr);
		// userImage = (ImageView) view.findViewById(R.id.image);
		viewCache.age.setVisibility(View.GONE);
		viewCache.reDoc.setVisibility(View.GONE);
		viewCache.nameTV.setText((String) mydata.get(p).get("name"));
		viewCache.msgTV.setText((String) mydata.get(p).get("message"));
		viewCache.dateTV.setText((String) mydata.get(p).get("dateline"));
		headimg = (String) mydata.get(p).get("headimg");
		// final int p = position;
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
		// 用户头像点击弹出资料查看并添加好友
		viewCache.userImage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, YourProfileActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("userId", (String) mydata.get(p).get("uid"));
				intent.putExtra("name", (String) mydata.get(p).get("name"));
				intent.putExtra("headimg", (String) mydata.get(p)
						.get("headimg"));
				intent.putExtra("sex", (String) mydata.get(p).get("sex"));
				intent.putExtra("type", "doing");
				mycontext.startActivity(intent);
			}
		});
		return convertView;
	}
}
