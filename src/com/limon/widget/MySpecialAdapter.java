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
import com.limon.make.DoingCommentActivity;
import com.limon.make.DoingCommentListActivity;
import com.limon.make.R;

/**
 * 新鲜事列表适配器
 * 
 * @author Administrator
 * 
 */
public class MySpecialAdapter extends SimpleAdapter {
	// private int[] colors = new int[] { 0xffeeeeee, 0xffcdcacd };
	private List<? extends Map<String, ?>> mydata = null;
	private Context mycontext;
	// private TextView reDoc;
	// private ImageView userImage;
	// private CheckBox cb;
	private String fpic, npic, pic = "", headimg, imageUrl;
	private AsyncImageLoader asyncImageLoader;
	private Drawable cachedImage;
	// private AsyncImageLoader asyncLoader = null;
	private Drawable bitmap;
	public HashMap<String, String> state = new HashMap<String, String>();

	public MySpecialAdapter(Context context,
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
					R.layout.myitemdoing, null);
			viewCache.imageView = (ImageView) convertView
					.findViewById(R.id.msgimg);
			viewCache.reDoc = (TextView) convertView.findViewById(R.id.redoc);
			// viewCache.reImg = (TextView) convertView.findViewById(R.id.decr);
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
							notifyDataSetChanged();
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
		fpic = (String) mydata.get(p).get("fpic");
		npic = (String) mydata.get(p).get("Pic");
		if (!("".equals(fpic) || "NULL".equals(fpic.toUpperCase()))) {
			pic = "/uhome/attachment/" + fpic;
		} else if (!("".equals(npic) || "/res/".equals(npic) || "NULL"
				.equals(npic.toUpperCase()))) {
			pic = npic;
		} else {
			pic = "";
		}
		if ("".equals(pic)) {
			viewCache.imageView.setVisibility(View.GONE);
		} else {
			imageUrl = BMapApi.getInstance().getIUrl() + pic + ".thumb.jpg";

			// final ImageView imgView = (ImageView)
			// convertView.findViewById(R.id.msgimg);
			bitmap = asyncImageLoader.loadDrawable(imageUrl,
					viewCache.imageView, new ImageCallBack() {
						@Override
						public void imageLoad(Drawable imageDrawable,
								ImageView iView, String imageURL) {

							// Log.d("s1","="+viewCache.getTag());
							// viewCache.imageView = (ImageView)
							// listView.findViewById(R.id.msgimg);

							// viewCache.userImage.setImageDrawable(viewCache.userImage.getDrawable());
							// viewCache.imageView.setImageDrawable(imageDrawable);
							// imgView.setImageDrawable(imageDrawable);
							iView.setImageDrawable(imageDrawable);
							notifyDataSetChanged();
							// ImageView imageViewByTag = (ImageView)
							// view.findViewWithTag(mydata.get(p).get("Pic"));
							// if (imageViewByTag != null) {
							// imageViewByTag.setImageDrawable(d);
							// }

						}

					});

			if (bitmap == null) {
				viewCache.imageView.setVisibility(View.GONE);
			} else {
				// Log.d("有图", "=" + viewCache.getTag());
				viewCache.imageView.setImageDrawable(bitmap);
			}

		}
		// 浏览新鲜事评论列表
		viewCache.reDoc.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, DoingCommentListActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("blogid", (String) mydata.get(p).get("blogid"));
				intent.putExtra("name", (String) mydata.get(p).get("name"));
				intent.putExtra("message", (String) mydata.get(p)
						.get("message"));
				intent.putExtra("dateline", (String) mydata.get(p).get(
						"dateline"));
				mycontext.startActivity(intent);
			}
		});
		// 新鲜事评论
		viewCache.age.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, DoingCommentActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("blogid", (String) mydata.get(p).get("blogid"));
				intent.putExtra("name", (String) mydata.get(p).get("name"));
				intent.putExtra("message", (String) mydata.get(p)
						.get("message"));
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
		// intent.putExtra("type", "doing");
		// mycontext.startActivity(intent);
		// }
		// });
		viewCache.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// Log.d("position","position="+position);
				if (isChecked) {
					// ui = userInfoList.get(p);
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
