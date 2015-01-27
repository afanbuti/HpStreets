package com.limon.make;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.limon.bean.ContactInfo;
import com.limon.common.JsonDataGetApi;
import com.limon.common.JsonDataPostApi;
import com.limon.common.ResourceUtil;
import com.limon.common.StringUtils;
import com.limon.widget.ContactFriendAdapater;
import com.mobclick.android.MobclickAgent;

/**
 * 通讯录好友列表
 * 
 * @author Administrator
 * 
 */
public class MyContactFriendsActivity extends BaseActivity {
	private String uname, uid, contactid, friendname, friendphone;
	protected Activity instance;
	private ProgressDialog progressDialog = null;
	private ListView msgList;
	private ContactFriendAdapater adapater;
	private String userId = "";
	public List<ContactInfo> userInfoList;
	private List<ContactInfo> getInfoList;
	private List<String> listTag = null;
	final private int MSG_TIMER = 0;
	private Button btn, bctn, btnIn, btnRef;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listcontactfriend);
		instance = this;
		mContext = getApplicationContext();
		btnIn = (Button) findViewById(R.id.SignUpButton);
		btn = (Button) findViewById(R.id.SendButton);
		bctn = (Button) findViewById(R.id.DelButton);
		btnRef = (Button) findViewById(R.id.RefButton);
		msgList = (ListView) findViewById(R.id.msglist);
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MyContactFriendsActivity.this);
		userId = settings.getString("UserID", "");
		if ("".equals(userId)) {
			Toast.makeText(mContext,
					ResourceUtil.getString(R.string.info_nologin),
					Toast.LENGTH_LONG).show();
			startActivity(new Intent(MyContactFriendsActivity.this,
					LogInActivity.class));
			// finish();
			// unregisterReceiver(exitre);
		}
		getMyFriendsThread();
		// 导入通讯录
		btnIn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setMyContactThread();
				// DialogUtils.dialogBuilder(instance,
				// ResourceUtil.getString(R.string.info_tishi),
				// ResourceUtil.getString(R.string.info_getcontact), new
				// DialogCallBack() {
				// @Override
				// public void callBack() {
				// setMyContactThread();
				// }
				// });

			}
		});
		// 返回
		btnRef.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, MyTextFriendsActivity.class);
				// mycontext.startActivity(intent);
				GroupTwo.group.getLocalActivityManager().removeAllActivities();
				View view = GroupTwo.group
						.getLocalActivityManager()
						.startActivity("Two",
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();
				// Again, replace the view
				GroupTwo.group.replaceView(view);
			}
		});
		// 发短信
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				HashMap<String, String> state = adapater.state;
				if (!(state == null || state.size() == 0)) {
					Intent intent = new Intent();
					intent.setClass(mContext, MyContactSMSActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("mobile", state);
					mContext.startActivity(intent);

				} else {
					Toast.makeText(
							mContext,
							mContext.getResources().getString(
									R.string.info_nosms), Toast.LENGTH_LONG)
							.show();
				}
				adapater.state.clear();
			}
		});
		// 删除通讯录
		bctn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				HashMap<String, String> state = adapater.state;
				if (!(state == null || state.size() == 0)) {
					// delfromContact(state);
					ContactInfo u = new ContactInfo();
					getInfoList = new ArrayList<ContactInfo>();
					Iterator<?> iter = state.entrySet().iterator();
					Map.Entry entry;
					while (iter.hasNext()) {
						entry = (Map.Entry) iter.next();
						u = new ContactInfo();
						u.setFriendname((String) entry.getKey());
						u.setContactid("");
						u.setFriendphone((String) entry.getValue());
						getInfoList.add(u);
					}
					GsonBuilder gsonb = new GsonBuilder();
					Gson gson = gsonb.create();
					String sJson = gson.toJson(getInfoList);
					// Log.d("sJson1:", sJson);
					sJson = StringUtils.encode(sJson);
					delMyContactThread(sJson);
				} else {
					Toast.makeText(
							mContext,
							mContext.getResources().getString(
									R.string.info_nosms), Toast.LENGTH_LONG)
							.show();
				}
				adapater.state.clear();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	private List<String> myListTage() {
		listTag = new ArrayList<String>();
		// listTag.add(mContext.getResources().getString(R.string.info_myfriends));
		listTag.add(mContext.getResources().getString(R.string.info_contact));
		// listTag.add(mContext.getResources().getString(R.string.info_sina));
		// listTag.add(mContext.getResources().getString(R.string.info_renren));
		// listTag.add(mContext.getResources().getString(R.string.info_tenc));
		return listTag;
	}

	/**
	 * 载入我的好友请求列表
	 * 
	 */
	public void loadList() {
		JsonDataGetApi api = new JsonDataGetApi();
		JSONArray jobhp = null;
		try {
			jobhp = api.getArray(String.format("u.ashx?op=cl&id=%s", userId));
			// Log.d("MyTextFriends:", jobhp.toString());
		} catch (Exception e) {
			Log.d("getLocationResult:jobhp", e.toString());
		}
		JSONObject json = null;
		try {
			if (jobhp != null) {
				if (userInfoList == null) {
					userInfoList = new ArrayList<ContactInfo>();
				}
				ContactInfo u = new ContactInfo();
				// u.setName(mContext.getResources().getString(
				// R.string.info_myfriends));
				// userInfoList.add(u);
				// String headurl =
				// "http://afanbutiphp.dns87.53nic.com/res/medal4.png";
				for (int i = 0; i < jobhp.length(); i++) {
					json = (JSONObject) jobhp.opt(i);
					uid = json.getString("Uid");
					uname = json.getString("Username");
					contactid = json.getString("Contactid");
					friendname = json.getString("Friendname");
					friendphone = json.getString("Friendphone");
					u = new ContactInfo();
					u.setContactid(contactid);
					u.setFriendname(friendname);
					u.setFriendphone(friendphone);
					u.setUid(uid);
					u.setUsername(uname);
					userInfoList.add(u);
				}
			}
		} catch (JSONException e) {
			MobclickAgent.reportError(mContext, "MyContactFriends_loadList:"
					+ e.toString());
		}
	}

	/**
	 * 请求好友列表的方法
	 */
	private void getMyFriendsThread() {
		FriendListTask task = new FriendListTask(this);
		task.execute("");
	}

	/**
	 * 导入通讯录
	 */
	private void setMyContactThread() {
		ContactInfoTask task = new ContactInfoTask(this);
		task.execute("");
	}

	private void delMyContactThread(String p) {
		DelContactTask task = new DelContactTask(this);
		task.execute(p);
	}

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_TIMER:
				// getSomething();
				break;
			}
		}
	};

	class FriendListTask extends AsyncTask<String, Integer, String> {
		// 可变长的输入参数，与AsyncTask.exucute()对应
		// ProgressDialog pdialog;

		public FriendListTask(Context context) {

			progressDialog = ProgressDialog.show(getParent(), "", mContext
					.getResources().getString(R.string.process), true, false);
		}

		@Override
		protected String doInBackground(String... params) {
			loadList();
			return null;

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			// adapter = new BlogsAdapter(mContext, lInfos, R.layout.itemblog,
			// new String[] { "name","title", "message", "dateline", "redoc" },
			// new int[] { R.id.name,R.id.title, R.id.message, R.id.dateline,
			// R.id.redoc });

			// mListView.setAdapter(adapter);

			if (userInfoList != null) {
				adapater = new ContactFriendAdapater(mContext, myListTage(),
						userInfoList);
				msgList.setAdapter(adapater);

			} else {
				Toast.makeText(
						mContext,
						mContext.getResources().getString(
								R.string.info_nojoinus), Toast.LENGTH_LONG)
						.show();
			}
			Message message = Message.obtain();
			message.what = MSG_TIMER;
			handler.sendEmptyMessage(message.what);
			// 60秒更新一次列表
			// handler.sendMessageDelayed(message, 60000L);
		}

		@Override
		protected void onPreExecute() {
			// 任务启动，可以在这里显示一个对话框，这里简单处理
			// message.setText(R.string.task_started);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// 更新进度
			// System.out.println(""+values[0]);
			// message.setText(""+values[0]);
			// pdialog.setProgress(values[0]);
		}

	}

	class ContactInfoTask extends AsyncTask<String, Integer, String> {
		// 可变长的输入参数，与AsyncTask.exucute()对应
		// ProgressDialog pdialog;

		public ContactInfoTask(Context context) {

			progressDialog = ProgressDialog.show(getParent(), "", mContext
					.getResources().getString(R.string.process), true, false);
		}

		@Override
		protected String doInBackground(String... params) {
			getContactInfo();
			return null;

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			if (userInfoList != null) {
				adapater = new ContactFriendAdapater(mContext, myListTage(),
						userInfoList);
				msgList.setAdapter(adapater);

			}
			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.info_contactok),
					Toast.LENGTH_LONG).show();

			Message message = Message.obtain();
			message.what = MSG_TIMER;
			handler.sendEmptyMessage(message.what);
			// 60秒更新一次列表
			// handler.sendMessageDelayed(message, 60000L);
		}

		@Override
		protected void onPreExecute() {
			// 任务启动，可以在这里显示一个对话框，这里简单处理
			// message.setText(R.string.task_started);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// 更新进度
			// System.out.println(""+values[0]);
			// message.setText(""+values[0]);
			// pdialog.setProgress(values[0]);
		}

	}

	class DelContactTask extends AsyncTask<String, Integer, String> {
		// 可变长的输入参数，与AsyncTask.exucute()对应
		// ProgressDialog pdialog;

		public DelContactTask(Context context) {

			progressDialog = ProgressDialog.show(getParent(), "", mContext
					.getResources().getString(R.string.process), true, false);
		}

		@Override
		protected String doInBackground(String... params) {
			delfromContact(params[0]);
			return null;

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			if (userInfoList != null) {
				adapater = new ContactFriendAdapater(mContext, myListTage(),
						userInfoList);
				msgList.setAdapter(adapater);

			}
			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.info_contactok),
					Toast.LENGTH_LONG).show();

			Message message = Message.obtain();
			message.what = MSG_TIMER;
			handler.sendEmptyMessage(message.what);
			// 60秒更新一次列表
			// handler.sendMessageDelayed(message, 60000L);
		}

		@Override
		protected void onPreExecute() {
			// 任务启动，可以在这里显示一个对话框，这里简单处理
			// message.setText(R.string.task_started);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// 更新进度
			// System.out.println(""+values[0]);
			// message.setText(""+values[0]);
			// pdialog.setProgress(values[0]);
		}

	}

	private void delfromContact(String sJson) {
		// ContactInfo u = new ContactInfo();
		// getInfoList = new ArrayList<ContactInfo>();
		// Iterator iter = state.entrySet().iterator();
		// Map.Entry entry;
		// while (iter.hasNext()) {
		// entry = (Map.Entry) iter.next();
		// u = new ContactInfo();
		// u.setFriendname((String)entry.getKey());
		// u.setContactid("");
		// u.setFriendphone((String) entry.getValue());
		// getInfoList.add(u);
		// }
		// GsonBuilder gsonb = new GsonBuilder();
		// Gson gson = gsonb.create();
		// String sJson = gson.toJson(getInfoList);
		// //Log.d("sJson1:", sJson);
		// sJson = StringUtils.encode(sJson);
		// //Log.d("sJson2:", sJson);
		Map<String, String> params = new HashMap<String, String>();
		params.put("u", sJson);

		JsonDataPostApi api = new JsonDataPostApi();
		try {
			api.makeRequest("u.ashx?op=cd&id=" + userId, params);
		} catch (Exception e) {
			MobclickAgent.reportError(mContext,
					"MyContactFriends_delfromContact:" + e.toString());
		}
		loadList();
	}

	private void getContactInfo() {
		// 获得通讯录信息 ，URI是ContactsContract.Contacts.CONTENT_URI
		Cursor cursor = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		@SuppressWarnings("unused")
		String contactId, name, hasPhone, phoneNumber = null, emailAddress;
		// String poBox,street,city,state,postalCode,country,type;
		@SuppressWarnings("unused")
		Cursor phones, emails, address;
		ContactInfo u = new ContactInfo();
		getInfoList = new ArrayList<ContactInfo>();
		while (cursor.moveToNext()) {
			u = new ContactInfo();

			// 获得通讯录中每个联系人的ID
			contactId = cursor
					.getString(cursor.getColumnIndex(BaseColumns._ID));
			// 获得通讯录中联系人的名字
			name = cursor
					.getString(cursor
							.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
			// Log.v("TAG", "…name…" + name);
			// 查看给联系人是否有电话，返回结果是String类型，1表示有，0表是没有
			hasPhone = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			if (hasPhone.equalsIgnoreCase("1"))
				hasPhone = "true";
			else
				hasPhone = "false";
			// 如果有电话，根据联系人的ID查找到联系人的电话，电话可以是多个
			if (Boolean.parseBoolean(hasPhone)) {
				phones = getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				while (phones.moveToNext()) {
					phoneNumber = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					// Log.v("TAG", "…phoneNumber…  " + phoneNumber);
				}
				phones.close();
			}
			// 查找email地址，这里email也可以有多个
			// emails = getContentResolver().query(
			// ContactsContract.CommonDataKinds.Email.CONTENT_URI,
			// null,
			// ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = "
			// + contactId, null, null);
			// while (emails.moveToNext()) {
			// emailAddress = emails
			// .getString(emails
			// .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
			// Log.v("TAG", "…emailAddress…  " + emailAddress);
			// }
			// emails.close();
			// 获得联系人的地址
			// address = getContentResolver()
			// .query(
			// ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
			// null,
			// ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID
			// + " = " + contactId, null, null);
			// while (address.moveToNext()) {
			// // These are all private class variables, don’t forget to create
			// // them.
			// poBox = address
			// .getString(address
			// .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
			// street = address
			// .getString(address
			// .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
			// city = address
			// .getString(address
			// .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
			// state = address
			// .getString(address
			// .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
			// postalCode = address
			// .getString(address
			// .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
			// country = address
			// .getString(address
			// .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
			// type = address
			// .getString(address
			// .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
			// Log.v("TAG", "…city…  " + city);
			// }
			u.setFriendname(name);
			u.setContactid(contactId);
			u.setFriendphone(phoneNumber);
			getInfoList.add(u);

		}
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		String sJson = gson.toJson(getInfoList);
		// Log.d("sJson1:", sJson);
		sJson = StringUtils.encode(sJson);
		// Log.d("sJson2:", sJson);
		Map<String, String> params = new HashMap<String, String>();
		params.put("u", sJson);

		JsonDataPostApi api = new JsonDataPostApi();
		try {
			api.makeRequest("u.ashx?op=c&id=" + userId, params);
			// if (!"-1".equals(res)) {
			// } else {
			// Toast.makeText(getApplicationContext(),
			// ResourceUtil.getString(R.string.error_net),
			// Toast.LENGTH_LONG).show();
			// Log.d("getJsonDataJarr:", "11");
			// }
		} catch (Exception e) {
			MobclickAgent.reportError(mContext,
					"MyContactFriends_getContactInfo:" + e.toString());
		}
		cursor.close();
		loadList();
	}

	// 从SIM卡中取号
	// private void GetSimContact(String add){
	// //读取SIM卡手机号,有两种可能:content://icc/adn与content://sim/adn
	// try {
	// Intent intent = new Intent();
	// intent.setData(Uri.parse(add));
	// Uri uri = intent.getData();
	// mCursor = getContentResolver().query(uri, null, null, null, null);
	// if (mCursor != null) {
	// while (mCursor.moveToNext()) {
	// ContactInfo sci = new ContactInfo();
	// // 取得联系人名字
	// int nameFieldColumnIndex = mCursor.getColumnIndex("name");
	// sci.contactName = mCursor.getString(nameFieldColumnIndex);
	// // 取得电话号码
	// int numberFieldColumnIndex = mCursor
	// .getColumnIndex("number");
	// sci.userNumber = mCursor.getString(numberFieldColumnIndex);
	//	
	// sci.userNumber = GetNumber(sci.userNumber);
	// sci.isChecked = false;
	//		     
	// if (IsUserNumber(sci.userNumber)) {
	// if (!IsContain(contactList, sci.userNumber)) {
	// if(IsAlreadyCheck(wNumStr, sci.userNumber)){
	// sci.isChecked = true;
	// numberStr += "," + sci.userNumber;
	// }
	// contactList.add(sci);
	// //Log.i("eoe", "*********"+sci.userNumber);
	// }
	// }
	// }
	// mCursor.close();
	// }
	// } catch (Exception e) {
	// Log.i("eoe", e.toString());
	// }
	// }
	// SOCKET客户端
	// private void setonclick()
	// {
	// chatok.setOnClickListener(new View.OnClickListener() {
	//   
	// @Override
	// public void onClick(View v) {
	// try {
	// connecttoserver(chattxt.getText().toString());
	// } catch (UnknownHostException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// });
	// }
	//
	// public void connecttoserver(String socketData) throws
	// UnknownHostException, IOException
	// {
	// Socket socket=RequestSocket("192.168.0.107",5000);
	// SendMsg(socket,socketData);
	// String txt = ReceiveMsg(socket);
	// this.chattxt2.setText(txt);
	// }
	//     
	// private Socket RequestSocket(String host,int port) throws
	// UnknownHostException, IOException
	// {
	// Socket socket = new Socket(host, port);
	// return socket;
	// }
	//     
	// private void SendMsg(Socket socket,String msg) throws IOException
	// {
	// BufferedWriter writer = new BufferedWriter(new
	// OutputStreamWriter(socket.getOutputStream()));
	// writer.write(msg.replace("\n", " ")+"\n");
	// writer.flush();
	// }
	//     
	// private String ReceiveMsg(Socket socket) throws IOException
	// {
	// BufferedReader reader = new BufferedReader(new
	// InputStreamReader(socket.getInputStream()));
	//     
	// String txt=reader.readLine();
	// return txt;
	// }
}
