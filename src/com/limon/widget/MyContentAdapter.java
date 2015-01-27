package com.limon.widget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.limon.make.MyContentTimesActivity;
import com.limon.make.R;
import com.limon.make.TtsActivity;

/**
 * ���ݿ��б�������
 * 
 * @author Administrator
 * 
 */
public class MyContentAdapter extends SimpleAdapter {
	// private int[] colors = new int[] { 0xffeeeeee, 0xffcdcacd };
	private List<? extends Map<String, ?>> mydata = null;
	private Context mycontext;
	// private TextView reDoc;
	private ImageView myImage;
	private ImageView mkfImage;
	// private BlogInfo ui;
	// private List<BlogInfo> userInfoList;
	public HashMap<String, String> state = new HashMap<String, String>();

	public MyContentAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		mydata = data;
		mycontext = context;
	}

	// private AsyncImageLoader asyncImageLoader;
	// private Drawable cachedImage;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = super.getView(position, convertView, parent);
		// int colorPos = position % colors.length;
		// view.setBackgroundColor(colors[colorPos]);
		final int p = position;
		myImage = (ImageView) view.findViewById(R.id.incr);
		mkfImage = (ImageView) view.findViewById(R.id.mkf);
		myImage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, MyContentTimesActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("cid", (String) mydata.get(p).get("blogid"));
				intent.putExtra("content", (String) mydata.get(p)
						.get("message"));
				mycontext.startActivity(intent);
			}
		});
		mkfImage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, TtsActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("cid", (String) mydata.get(p).get("blogid"));
				intent.putExtra("content", (String) mydata.get(p)
						.get("message"));
				mycontext.startActivity(intent);
			}
		});
		// reDoc = (TextView) view.findViewById(R.id.redoc);
		// age = (ImageView) view.findViewById(R.id.incr);
		// userImage = (ImageView) view.findViewById(R.id.image);
		// if (Integer.parseInt((String) reDoc.getText()) > 0) {
		// reDoc.setVisibility(View.VISIBLE);
		// } else {
		// reDoc.setVisibility(View.GONE);
		// }
		// cb = (CheckBox) view.findViewById(R.id.cb);
		// if ("".equals((String) mydata.get(p).get("headimg"))) {
		// if ("0".equals((String) mydata.get(p).get("sex")))
		// userImage.setImageResource(R.drawable.blank_girl);
		// else
		// userImage.setImageResource(R.drawable.blank_boy);
		// } else {
		// asyncImageLoader = new AsyncImageLoader();
		// cachedImage = asyncImageLoader.loadDrawable(BMapApi.getInstance()
		// .getImageUrl()
		// + (String) mydata.get(p).get("headimg"), userImage,
		// new ImageCallback() {
		// @Override
		// public void imageLoaded(Drawable imageDrawable,
		// ImageView imageView, String imageUrl) {
		// imageView.setImageDrawable(imageDrawable);
		// }
		// });
		// if (cachedImage == null) {
		// if ("0".equals((String) mydata.get(p).get("sex")))
		// userImage.setImageResource(R.drawable.blank_girl);
		// else
		// userImage.setImageResource(R.drawable.blank_boy);
		// } else {
		// userImage.setImageDrawable(cachedImage);
		// }
		// }

		// ��������������б�
		// reDoc.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// Intent intent = new Intent();
		// intent.setClass(mycontext, DoingCommentListActivity.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// intent.putExtra("blogid", (String) mydata.get(p).get("blogid"));
		// intent.putExtra("name", (String) mydata.get(p).get("name"));
		// intent.putExtra("message", (String) mydata.get(p)
		// .get("message"));
		// intent.putExtra("dateline", (String) mydata.get(p).get(
		// "dateline"));
		// mycontext.startActivity(intent);
		// }
		// });
		// ����������
		// age.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// Intent intent = new Intent();
		// intent.setClass(mycontext, DoingCommentActivity.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// intent.putExtra("blogid", (String) mydata.get(p).get("blogid"));
		// intent.putExtra("name", (String) mydata.get(p).get("name"));
		// intent.putExtra("message", (String) mydata.get(p)
		// .get("message"));
		// intent.putExtra("dateline", (String) mydata.get(p).get(
		// "dateline"));
		// mycontext.startActivity(intent);
		// }
		// });
		// �û�ͷ�����������ϲ鿴����Ӻ���
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
		// cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// //Log.d("position","position="+position);
		// if (isChecked) {
		// //ui = userInfoList.get(p);
		// //Log.d((String) mydata.get(p).get("blogid"),(String)
		// mydata.get(p).get("message"));
		// state.put((String) mydata.get(p).get("blogid"), (String)
		// mydata.get(p).get("message"));
		// }
		// else {
		// state.remove(p);
		// }
		// }
		// });
		// cb.setChecked((state.get(position) == null ? false : true));
		return view;
	}
}
