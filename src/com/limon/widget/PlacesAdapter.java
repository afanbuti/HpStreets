package com.limon.widget;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.limon.make.PlaceCommentActivity;
import com.limon.make.PlaceCommentListActivity;
import com.limon.make.R;

/**
 * 地点列表适配器
 * 
 * @author Administrator
 * 
 */
public class PlacesAdapter extends SimpleAdapter {
	// private int[] colors = new int[] { 0xffeeeeee, 0xffcdcacd };
	private List<? extends Map<String, ?>> mydata = null;
	private Context mycontext;

	public PlacesAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		mydata = data;
		mycontext = context;
	}

	@SuppressWarnings("unused")
	private String guid, name, cate, addr, tel;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = super.getView(position, convertView, parent);
		// int colorPos = position % colors.length;
		// view.setBackgroundColor(colors[colorPos]);

		TextView myImage = (TextView) view.findViewById(R.id.redoc);
		ImageView age = (ImageView) view.findViewById(R.id.incr);
		if (Integer.parseInt((String) myImage.getText()) > 0) {
			myImage.setVisibility(View.VISIBLE);
		} else {
			myImage.setVisibility(View.GONE);
		}
		final int p = position;
		// Log.d("guid",(mydata.get(p).get("guid")==null)?"x":(String)mydata.get(p).get("guid"));
		guid = (mydata.get(p).get("guid") == null || "null".equals(mydata
				.get(p).get("guid"))) ? "" : (String) mydata.get(p).get("guid");
		// Log.d("name",(mydata.get(p).get("name")==null)?"x":(String)mydata.get(p).get("name"));
		name = (mydata.get(p).get("name") == null || "null".equals(mydata
				.get(p).get("name"))) ? "" : (String) mydata.get(p).get("name");
		// Log.d("addr",(mydata.get(p).get("addr")==null)?"x":(String)mydata.get(p).get("addr"));
		// addr=(mydata.get(p).get("addr")==null ||
		// (String)mydata.get(p).get("addr")==null)?"":(String)mydata.get(p).get("addr");
		// Log.d("tel",(mydata.get(p).get("tel")==null)?"x":(String)mydata.get(p).get("tel"));
		// tel=(mydata.get(p).get("tel")==null ||
		// (String)mydata.get(p).get("tel")==null)?"":(String)mydata.get(p).get("tel");
		cate = (mydata.get(p).get("cate") == null || "null".equals(mydata
				.get(p).get("cate"))) ? "" : (String) mydata.get(p).get("cate");
		// Log.d("guid", guid);
		// Log.d("name", name);
		// Log.d("cate", cate);
		myImage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, PlaceCommentListActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("guid", guid);
				intent.putExtra("name", name);
				intent.putExtra("cate", cate);
				mycontext.startActivity(intent);
			}
		});
		age.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mycontext, PlaceCommentActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("guid", guid);
				intent.putExtra("name", name);
				intent.putExtra("cate", cate);
				mycontext.startActivity(intent);
			}
		});
		return view;
	}
}
