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
import com.limon.make.DoingCommentActivity;
import com.limon.make.DoingCommentListActivity;
import com.limon.make.R;
import com.limon.make.YourProfileActivity;

/**
 * �������б�������
 * 
 * @author Administrator
 * 
 */
public class SpecialAdapter extends SimpleAdapter {
	private List<? extends Map<String, ?>> mydata = null;
	private Context mycontext;
	private AsyncImageLoader asyncLoader = null;
	private Drawable cachedImage;
	private Drawable bitmap;
	private String imageUrl, fpic, npic, pic = "", headimg;

	// private Drawable w;
	public SpecialAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		mydata = data;
		mycontext = context;
		asyncLoader = new AsyncImageLoader();
		// w=mycontext.getWallpaper();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return super.getCount();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return super.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return super.getItemId(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// final View view = super.getView(position, convertView, parent);
		// int colorPos = position % colors.length;
		// view.setBackgroundColor(colors[colorPos]);
		final int p = position;
		ViewCache viewCache = null;
		// if (viewCache == null) {
		// viewCache = new ViewCache(mycontext);
		// if (convertView == null) {
		// convertView =
		// LayoutInflater.from(mycontext).inflate(R.layout.itemdoing, null);
		// }
		// viewCache.imageView = (ImageView) convertView
		// .findViewById(R.id.msgimg);
		// viewCache.reDoc = (TextView) convertView.findViewById(R.id.redoc);
		// //viewCache.reImg = (TextView) convertView.findViewById(R.id.decr);
		// viewCache.nameTV = (TextView) convertView.findViewById(R.id.name);
		// viewCache.msgTV = (TextView) convertView.findViewById(R.id.message);
		// viewCache.dateTV = (TextView) convertView
		// .findViewById(R.id.dateline);
		// viewCache.age = (ImageView) convertView.findViewById(R.id.incr);
		// viewCache.userImage = (ImageView) convertView
		// .findViewById(R.id.image);
		// convertView.setTag(viewCache);
		//
		// } else {
		// viewCache = (ViewCache) convertView.getTag();
		// }
		if (convertView == null) {
			// Log.d("convertView",""+p);
			viewCache = new ViewCache(mycontext);
			convertView = LayoutInflater.from(mycontext).inflate(
					R.layout.itemdoing, null);
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

			// final ImageView usrView = (ImageView)
			// convertView.findViewById(R.id.image);
			cachedImage = asyncLoader.loadDrawable(BMapApi.getInstance()
					.getImageUrl()
					+ headimg + ".thumb.jpg", viewCache.userImage,
					new ImageCallBack() {
						@Override
						public void imageLoad(Drawable imageDrawable,
								ImageView iView, String imageUrl) {
							// iView.setImageDrawable(imageDrawable);
							iView.setImageDrawable(imageDrawable);
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
		// Log.d("fpic=",fpic);Log.d("npic=",npic);
		if (!("".equals(fpic) || "NULL".equals(fpic.toUpperCase()))) {
			pic = "/uhome/attachment/" + fpic;
		} else if (!("".equals(npic) || "/res/".equals(npic) || "NULL"
				.equals(npic.toUpperCase()))) {
			pic = npic;
		} else {
			pic = "";
		}//
		if ("".equals(pic)) {
			// Log.d(this.toString()+"pic+",pic);
			viewCache.imageView.setVisibility(View.GONE);
		} else {
			// Log.d(this.toString()+"pic+",pic);
			imageUrl = BMapApi.getInstance().getIUrl() + pic + ".thumb.jpg";
			// final ImageView imgView = (ImageView)
			// convertView.findViewById(R.id.msgimg);
			bitmap = asyncLoader.loadDrawable(imageUrl, viewCache.imageView,
					new ImageCallBack() {
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
				// Log.d("��ͼ", "=" + viewCache.getTag());
				viewCache.imageView.setImageDrawable(bitmap);
			}

		}

		// �鿴ͼƬ
		// viewCache.imageView.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// fpic = (String) mydata.get(p).get("fpic");
		// npic = (String) mydata.get(p).get("Pic");
		// if (!("".equals(fpic) || "NULL".equals(fpic.toUpperCase()))) {
		// pic = "/uhome/attachment/" + fpic;
		// } else if (!("".equals(npic) || "/res/".equals(npic) || "NULL"
		// .equals(npic.toUpperCase()))) {
		// pic = npic;
		// }
		// Intent intent = new Intent();
		// intent.setClass(mycontext, DoingImageActivity.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// intent.putExtra("pic", pic);
		// mycontext.startActivity(intent);
		// }
		// });
		// ��������������б�
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
		// ����������
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
		// �û�ͷ�����������ϲ鿴����Ӻ���
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
		// return createViewFromResource(position, convertView, parent,
		// mResource);
	}
	// private void addItem()
	// {
	// HashMap<String, Object> map = new HashMap<String, Object>();
	// map.put("image", R.drawable.icon);
	// map.put("title", "����");
	// map.put("text", "Ҫ��ʾ������");
	// listItem.add(map);
	// listItemAdapter.notifyDataSetChanged();
	// }
	//
	// private void deleteItem()
	// {
	// int size = listItem.size();
	// if( size > 0 )
	// {
	// listItem.remove(listItem.size() - 1);
	// notifyDataSetChanged();
	// }
	// }
	// public void addItem(int resId) {
	// //textIdList.add(resId);
	// notifyDataSetChanged();
	// }
	//      
	// public void remove(int index) {
	// if (index < 0)
	// return;
	// //textIdList.remove(index);
	// notifyDataSetChanged();
	// }
	//      
	// public void removeAll() {
	// //textIdList.clear();
	// notifyDataSetChanged();
	// }
	// private View createViewFromResource(int position, View convertView,
	// ViewGroup parent, int resource) {
	// View v;
	// if (convertView == null) {
	// //v = mInflater.inflate(resource, parent, false);
	// v = LayoutInflater.from(mycontext).inflate(mResource, null);
	// // final int[] to = mTo;
	// // final int count = to.length;
	// // final View[] holder = new View[count];
	// //
	// // for (int i = 0; i < count; i++) {
	// // holder[i] = v.findViewById(to[i]);
	// // }
	// ViewCache viewCache = new ViewCache(mycontext);
	// viewCache.imageView = (ImageView) v.findViewById(R.id.msgimg);
	// viewCache.reDoc = (TextView) v.findViewById(R.id.redoc);
	// //viewCache.reImg = (TextView) convertView.findViewById(R.id.decr);
	// viewCache.nameTV = (TextView) v.findViewById(R.id.name);
	// viewCache.msgTV = (TextView) v.findViewById(R.id.message);
	// viewCache.dateTV = (TextView) v.findViewById(R.id.dateline);
	// viewCache.age = (ImageView) v.findViewById(R.id.incr);
	// viewCache.userImage = (ImageView) v.findViewById(R.id.image);
	//	
	// v.setTag(viewCache);
	// } else {
	// v = convertView;
	// }
	//        
	// 
	// return v;
	// }
	// private void bindView(int p, ViewCache viewCache) {
	// viewCache.nameTV.setText((String) mydata.get(p).get("name"));
	// viewCache.msgTV.setText((String) mydata.get(p).get("message"));
	// viewCache.dateTV.setText((String) mydata.get(p).get("dateline"));
	// if (Integer.parseInt((String) mydata.get(p).get("redoc")) > 0) {
	// viewCache.reDoc.setVisibility(View.VISIBLE);
	// } else {
	// viewCache.reDoc.setText((String) mydata.get(p).get("redoc"));
	// viewCache.reDoc.setVisibility(View.GONE);
	// }
	// }
	// private void bindView(int position, View view) {
	// final Map dataSet = mydata.get(position);
	// if (dataSet == null) {
	// return;
	// }
	// final ViewBinder binder = mViewBinder;
	// final View[] holder = (View[]) view.getTag();
	// final String[] from = mFrom;
	// final int[] to = mTo;
	// final int count = to.length;
	// 
	// for (int i = 0; i < count; i++) {
	// final View v = holder[i];
	// if (v != null) {
	// final Object data = dataSet.get(from[i]);
	// String text = data == null ? "" : data.toString();
	// if (text == null) {
	// text = "";
	// }
	// boolean bound = false;
	// if (binder != null) {
	// bound = binder.setViewValue(v, data, text);
	// }
	// if (!bound) {
	// if (v instanceof Checkable) {
	// if (data instanceof Boolean) {
	// ((Checkable) v).setChecked((Boolean) data);
	// } else {
	// throw new IllegalStateException(v.getClass().getName() +
	// " should be bound to a Boolean, not a " + data.getClass());
	// }
	// } else if (v instanceof TextView) {
	// // Note: keep the instanceof TextView check at the bottom of these
	// // ifs since a lot of views are TextViews (e.g. CheckBoxes).
	// //setViewText((TextView) v, text);
	// ((TextView) v).setText(text);
	// } else if (v instanceof ImageView) {
	// if (data instanceof Integer) {
	// setViewImage((ImageView) v, (Integer) data);
	// }
	// else if(data instanceof byte[]) { //��ע1
	// Bitmap bmp;
	// byte[] image = (byte[])data;
	// if(b.length!=0){
	// bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
	// ((ImageView) v).setImageBitmap(bmp);
	// }
	// }
	// else if(v instanceof RatingBar){
	// float score = Float.parseFloat(data.toString()); //��ע2
	// ((RatingBar)v).setRating(score);
	// }
	// else {
	// throw new IllegalStateException(v.getClass().getName() + " is not a " +
	// " view that can be bounds by this SimpleAdapter");
	// }
	// }
	// }
	// }
	// }
	//  
	// public void setViewImage(ImageView v, int value) {
	// v.setImageResource(value);
	// }
}
