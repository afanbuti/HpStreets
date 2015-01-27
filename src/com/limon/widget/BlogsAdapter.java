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
import com.limon.make.BlogCommentActivity;
import com.limon.make.BlogCommentListActivity;
import com.limon.make.BlogDetailActivity;
import com.limon.make.R;
import com.limon.make.YourProfileActivity;

/**
 * ��־�б�������
 * 
 * @author Administrator
 * 
 */
public class BlogsAdapter extends SimpleAdapter {
	// private int[] colors = new int[] { 0xffeeeeee, 0xffcdcacd };
	private List<? extends Map<String, ?>> mydata = null;
	private Context mycontext;
	private TextView reDoc;
	// private ImageView userImage;
	private AsyncImageLoader asyncImageLoader;
	private Drawable cachedImage;
	private String headimg;

	public BlogsAdapter(Context context, List<? extends Map<String, ?>> data,
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
					R.layout.itemblog, null);
			// viewCache.imageView = (ImageView) convertView
			// .findViewById(R.id.msgimg);
			//viewCache.reDoc = (TextView) convertView.findViewById(R.id.redoc);
			viewCache.readAll = (TextView) convertView.findViewById(R.id.decr);
			viewCache.nameTV = (TextView) convertView.findViewById(R.id.title);
			viewCache.msgTV = (TextView) convertView.findViewById(R.id.message);
			viewCache.dateTV = (TextView) convertView
					.findViewById(R.id.dateline);
			//viewCache.age = (ImageView) convertView.findViewById(R.id.incr);
			viewCache.userImage = (ImageView) convertView
					.findViewById(R.id.image);
			convertView.setTag(viewCache);

		} else {
			viewCache = (ViewCache) convertView.getTag();
		}
		viewCache.nameTV.setText((String) mydata.get(p).get("title"));
		viewCache.msgTV.setText((String) mydata.get(p).get("message"));
		viewCache.dateTV.setText((String) mydata.get(p).get("dateline"));
		// reDoc = (TextView) view.findViewById(R.id.redoc);
		// readAll = (TextView) view.findViewById(R.id.decr);
		// age = (ImageView) view.findViewById(R.id.incr);
		// userImage = (ImageView) view.findViewById(R.id.image);
		if (((String) mydata.get(p).get("ormessage")).length() > 140) {
			viewCache.readAll.setVisibility(View.VISIBLE);
		} else {
			viewCache.readAll.setVisibility(View.GONE);
		}
//		if (Integer.parseInt((String) mydata.get(p).get("redoc")) > 0) {
//			viewCache.reDoc.setText((String) mydata.get(p).get("redoc"));
//			viewCache.reDoc.setVisibility(View.VISIBLE);
//		} else {
//			viewCache.reDoc.setVisibility(View.GONE);
//		}
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
					.getIUrl()
					+ headimg, viewCache.userImage,
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
		// �Ķ�ȫ��
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
				//intent.putExtra("redoc", (String) reDoc.getText());
				mycontext.startActivity(intent);
			}
		});
		// ��������б�
//		viewCache.reDoc.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(mycontext, BlogCommentListActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.putExtra("blogid", (String) mydata.get(p).get("blogid"));
//				intent.putExtra("name", (String) mydata.get(p).get("name"));
//				intent.putExtra("title", (String) mydata.get(p).get("title"));
//				intent.putExtra("dateline", (String) mydata.get(p).get(
//						"dateline"));
//				mycontext.startActivity(intent);
//			}
//		});
		// ��־����
//		viewCache.age.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(mycontext, BlogCommentActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.putExtra("blogid", (String) mydata.get(p).get("blogid"));
//				intent.putExtra("uid", (String) mydata.get(p).get("uid"));
//				intent.putExtra("name", (String) mydata.get(p).get("name"));
//				intent.putExtra("title", (String) mydata.get(p).get("title"));
//				intent.putExtra("dateline", (String) mydata.get(p).get(
//						"dateline"));
//				mycontext.startActivity(intent);
//			}
//		});
		// �û�ͷ�����������ϲ鿴����Ӻ���
		viewCache.userImage.setOnClickListener(new View.OnClickListener() {
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
				//intent.putExtra("redoc", (String) reDoc.getText());
				mycontext.startActivity(intent);
			}
		});
		return convertView;
	}
}
