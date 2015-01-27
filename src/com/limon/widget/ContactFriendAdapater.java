package com.limon.widget;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.limon.bean.ContactInfo;
import com.limon.make.R;

/**
 * 通讯录好友适配器
 * 
 * @author Administrator
 * 
 */
public class ContactFriendAdapater extends BaseAdapter {
	// private AsyncImageLoader asyncImageLoader;
	private List<String> listTag = null;
	private List<ContactInfo> userInfoList;
	private Context mycontext;
	// private TextView userName, userPhone;
	// private CheckBox cb;
	// SharedPreferences sharedPreferences;
	private ContactInfo ui;
	// 记录checkbox的状态
	public HashMap<String, String> state = new HashMap<String, String>();

	public ContactFriendAdapater(Context context, List<String> tags,
			List<ContactInfo> userList) {
		super();
		this.listTag = tags;
		mycontext = context;
		userInfoList = userList;
	}

	@Override
	public int getCount() {
		return userInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return userInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean isEnabled(int position) {
		if (listTag.contains(getItem(position))) {
			return false;
		}
		return super.isEnabled(position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// Log.d("item== ", ((FriendInfo)getItem(position)).getName());
		// 根据标签类型加载不同的布局模板
		// if (listTag.contains(((FriendInfo) getItem(position)).getName())) {
		// // 如果是标签项
		// convertView = LayoutInflater.from(mycontext).inflate(
		// R.layout.group_list_item_tag, null);
		// userName = (TextView) convertView
		// .findViewById(R.id.group_list_item_text);
		// userName.setText(((FriendInfo) getItem(position)).getName());
		// } else {
		// 否则就是数据项了
		final int p = position;
		ViewCache viewCache = null;

		if (convertView == null) {
			viewCache = new ViewCache(mycontext);
			convertView = LayoutInflater.from(mycontext).inflate(
					R.layout.itemcontact, null);
			viewCache.nameTV = (TextView) convertView
					.findViewById(R.id.username);
			viewCache.msgTV = (TextView) convertView.findViewById(R.id.message);
			viewCache.cb = (CheckBox) convertView.findViewById(R.id.cb);
			convertView.setTag(viewCache);

		} else {
			viewCache = (ViewCache) convertView.getTag();
		}
		// convertView =
		// LayoutInflater.from(getApplicationContext()).inflate(
		// R.layout.renren_friendinfo, null);
		// userName = (TextView) convertView.findViewById(R.id.username);
		// userImage = (ImageView) convertView.findViewById(R.id.userimage);
		// userPhone = (TextView) convertView.findViewById(R.id.message);
		// viewCache.cb = (CheckBox) convertView.findViewById(R.id.cb);

		ui = userInfoList.get(p);
		if (ui != null) {
			// convertView.setTag(ui.getContactid());
			viewCache.nameTV.setText(ui.getFriendname());
			viewCache.msgTV.setText(ui.getFriendphone());
		}
		viewCache.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// Log.d("position","position="+position);
				if (isChecked) {
					ui = userInfoList.get(p);
					// Log.d(ui.getFriendname(),ui.getFriendphone());
					state.put(ui.getFriendname(), ui.getFriendphone());
				} else {
					state.remove(p);
				}
			}
		});
		viewCache.cb.setChecked((state.get(p) == null ? false : true));
		return convertView;
	}
}
