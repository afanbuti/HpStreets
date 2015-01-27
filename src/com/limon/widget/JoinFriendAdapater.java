package com.limon.widget;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.limon.bean.FriendInfo;
import com.limon.common.AsyncImageLoader;
import com.limon.common.AsyncImageLoader.ImageCallBack;
import com.limon.make.BMapApi;
import com.limon.make.R;
import com.limon.make.YourProfileActivity;

/**
 * �����б�������
 * 
 * @author Administrator
 * 
 */
public class JoinFriendAdapater extends BaseAdapter {
	private AsyncImageLoader asyncImageLoader;
	private List<String> listTag = null;
	private List<FriendInfo> userInfoList;
	private Context mycontext;
	private TextView userName;
	private ImageView userImage;
	private Drawable cachedImage;
	private FriendInfo ui;

	public JoinFriendAdapater(Context context, List<String> tags,
			List<FriendInfo> userList) {
		super();
		this.listTag = tags;
		mycontext = context;
		userInfoList = userList;
		asyncImageLoader = new AsyncImageLoader();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// Log.d("item== ", ((FriendInfo)getItem(position)).getName());
		// ���ݱ�ǩ���ͼ��ز�ͬ�Ĳ���ģ��
		if (listTag.contains(((FriendInfo) getItem(position)).getName())) {
			// ����Ǳ�ǩ��
			convertView = LayoutInflater.from(mycontext).inflate(
					R.layout.group_list_item_tag, null);
			userName = (TextView) convertView
					.findViewById(R.id.group_list_item_text);
			userName.setText(((FriendInfo) getItem(position)).getName());
		} else {
			// ���������������
			convertView = LayoutInflater.from(mycontext).inflate(
					R.layout.itemfriends, null);

			// convertView =
			// LayoutInflater.from(getApplicationContext()).inflate(
			// R.layout.renren_friendinfo, null);
			userName = (TextView) convertView.findViewById(R.id.username);
			userImage = (ImageView) convertView.findViewById(R.id.userimage);

			ui = new FriendInfo();

			ui = userInfoList.get(position);
			if (ui != null) {
				convertView.setTag(ui.getId());
				userName.setText(ui.getName());

				if ("".equals(ui.getHeadImg())) {
					if (ui.getSex() == 0)
						userImage.setImageResource(R.drawable.blank_girl);
					else
						userImage.setImageResource(R.drawable.blank_boy);
				} else {

					cachedImage = asyncImageLoader.loadDrawable(BMapApi
							.getInstance().getImageUrl()
							+ ui.getHeadImg() + ".thumb.jpg", userImage,
							new ImageCallBack() {
								@Override
								public void imageLoad(Drawable imageDrawable,
										ImageView imageView, String imageUrl) {
									imageView.setImageDrawable(imageDrawable);
								}
							});
					if (cachedImage == null) {
						if (ui.getSex() == 0)
							userImage.setImageResource(R.drawable.blank_girl);
						else
							userImage.setImageResource(R.drawable.blank_boy);
					} else {
						userImage.setImageDrawable(cachedImage);
					}
				}
				// �û�ͷ�����������ϲ鿴����Ӻ���
				userImage.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(mycontext, YourProfileActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("userId", ui.getId());
						intent.putExtra("name", ui.getName());
						intent.putExtra("headimg", ui.getHeadImg());
						intent.putExtra("sex", ui.getSex());
						intent.putExtra("type", "joinus");
						mycontext.startActivity(intent);
					}
				});
			}
		}
		return convertView;
	}
}
