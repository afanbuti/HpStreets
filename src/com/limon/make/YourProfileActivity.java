package com.limon.make;

import org.json.JSONException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.limon.common.AsyncImageLoader;
import com.limon.common.JsonDataGetApi;
import com.limon.common.ResourceUtil;
import com.limon.common.StringUtils;
import com.limon.common.AsyncImageLoader.ImageCallBack;
import com.mobclick.android.MobclickAgent;

/**
 * ���ϲ鿴 ��Ϊ�ҵĺ��� �������Ļ�
 * 
 * @author Administrator
 * 
 */
public class YourProfileActivity extends BaseActivity {
	private ImageView imageView = null;
	private TextView nameView = null;
	private TextView levelView = null;
	// private TextView pointView = null;
	// private TextView goldView = null;
	// private TextView progressView = null;
	// private GridView gridview = null;
	// private ProgressBar progressBar = null;
	private String uname = "", type = "";
	private String userId = "", myid = "", isfriend = "0";
	// private boolean isPublic = false;
	// private int credit = 0;
	private String headurl = "", sex = "0";
	private Drawable cachedImage = null;
	// private String lat, lon;
	// final private int MSG_TIMER = 0;
	private Button shut, back, joinok, quiet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		setContentView(R.layout.profileyouractivity);
		setTitle(ResourceUtil.getString(R.string.info_setfriend));
		nameView = (TextView) findViewById(R.id.user_details_name);
		levelView = (TextView) findViewById(R.id.user_details_level);
		// pointView = (TextView) findViewById(R.id.user_details_points);
		// goldView = (TextView) findViewById(R.id.user_details_gold);
		// progressView = (TextView)
		// findViewById(R.id.user_details_level_progress_text);
		imageView = (ImageView) findViewById(R.id.user_details_icon);
		// progressBar = (ProgressBar)
		// findViewById(R.id.user_details_level_progress);
		shut = (Button) findViewById(R.id.SignUpButton);
		back = (Button) findViewById(R.id.ReturnButton);
		quiet = (Button) findViewById(R.id.QuietButton);
		joinok = (Button) findViewById(R.id.JoinButton);
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(YourProfileActivity.this);
		myid = settings.getString("UserID", "");
		if ("".equals(myid)) {
			quiet.setVisibility(View.GONE);
			shut.setVisibility(View.GONE);
			joinok.setVisibility(View.GONE);
		}
		userId = getIntent().getExtras().getString("userId");
		uname = getIntent().getExtras().getString("name");
		headurl = getIntent().getExtras().getString("headimg");
		sex = getIntent().getExtras().getString("sex");
		type = getIntent().getExtras().getString("type");
		if (myid.equals(userId)) {
			shut.setEnabled(false);
		}
		if ("friend".equals(type)) {
			shut.setVisibility(View.GONE);
			joinok.setVisibility(View.GONE);
		}
		if ("doing".equals(type) || "blog".equals(type)) {
			joinok.setVisibility(View.GONE);
		}
		if ("joinus".equals(type)) {
			shut.setVisibility(View.GONE);
		}
		getPersonInfo();

		joinok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				joinUsOk();
			}
		});

		back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
				unregisterReceiver(exitre);
			}
		});

		quiet.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, QuietSubmitActivity.class);
				intent.putExtra("userId", userId);
				startActivity(intent);
			}
		});

	}

	// ��ȡ������Ϣ���ƽ���
	private void getPersonInfo() {
		nameView.setText(uname);
		levelView.setText("");
		// pointView.setText("" + credit);
		// progressView.setText("...");
		// goldView.setText("...");
		// progressBar.setProgress(50);
		if ("".equals(headurl)) {
			if ("0".equals(sex))
				imageView.setImageResource(R.drawable.blank_girl);
			else
				imageView.setImageResource(R.drawable.blank_boy);
		} else {
			AsyncImageLoader asyncImageLoader = new AsyncImageLoader(); // �첽��ȡͼƬ
			cachedImage = asyncImageLoader.loadDrawable(BMapApi.getInstance()
					.getImageUrl()
					+ headurl, imageView, new ImageCallBack() {
				@Override
				public void imageLoad(Drawable imageDrawable,
						ImageView imageView, String imageUrl) {
					imageView.setImageDrawable(imageDrawable);
				}
			});
			if (cachedImage == null) {
				if ("0".equals(sex))
					imageView.setImageResource(R.drawable.blank_girl);
				else
					imageView.setImageResource(R.drawable.blank_boy);
			} else {
				imageView.setImageDrawable(cachedImage);
			}
		}
		if ("1".equals(isfriend)) {
			shut.setText(ResourceUtil.getString(R.string.submit_cancelus));
		}
		shut.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!"1".equals(isfriend)) {
					joinUs();
				} else {
					cancelJoinUs();
				}
			}
		});
		// final Handler handler = new Handler() {
		// public void handleMessage(Message message) {
		// nameView.setText(uname);
		// levelView.setText("");
		// pointView.setText("" + credit);
		// progressView.setText("...");
		// goldView.setText("...");
		// progressBar.setProgress(50);
		// //Log.d("Isfriend=",isfriend);
		// if("1".equals(isfriend)){
		// shut.setText(ResourceUtil.getString(R.string.submit_cancelus));
		// }
		// shut.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// if(!"1".equals(isfriend)){
		// joinUs();
		// }else{
		// cancelJoinUs();
		// }
		// }
		// });
		// }
		// };
		// new Thread() {
		// @Override
		// public void run() {
		//
		// JsonDataGetApi api = new JsonDataGetApi();
		// JSONObject json = null;
		// try {
		// json = api.getObject("u.ashx?op=uf&mid=" + myid+"&yid=" + userId);
		//					 
		// //GsonBuilder gsonb = new GsonBuilder();
		// //Gson gson = gsonb.create();
		// if (!(json == null || "-1".equals(json.toString()))) {
		// //UserInfo u = gson.fromJson(json.toString(),
		// // UserInfo.class);
		// //uname = u.getUname();
		// uname=json.getString("Uname");
		// credit=json.getInt("Credit");
		// isfriend=json.getString("Isfriend");
		// //credit = u.getCredit();
		// //isfriend=u.getIsfriend();
		// }
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// Message message = new Message();
		// // message.what=0;
		// // message.obj=jsonArray;
		// handler.sendMessage(message);
		//
		// }
		// }.start();
	}

	// ȡ����ѹ�ϵ
	private void cancelJoinUs() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				shut.setEnabled(false);
				Toast.makeText(getApplicationContext(),
						ResourceUtil.getString(R.string.info_cancelfriend),
						Toast.LENGTH_LONG).show();
			}
		};
		new Thread() {
			@Override
			public void run() {

				JsonDataGetApi api = new JsonDataGetApi();

				try {
					api
							.getString("u.ashx?op=cf&mid=" + myid + "&yid="
									+ userId);
					// Log.d("ȡ����ѹ�ϵ", "u.ashx?op=cf&mid=" + myid + "&yid="
					// + userId);
				} catch (JSONException e) {
					MobclickAgent.reportError(mContext,
							"YourPrifile_cancelJoinUs:" + e.toString());
				} catch (Exception e) {
					MobclickAgent.reportError(mContext,
							"YourPrifile_cancelJoinUs:" + e.toString());
				}

				Message message = new Message();
				// message.what=0;
				// message.obj=jsonArray;
				handler.sendMessage(message);

			}
		}.start();
	}

	// �����Ϊ����
	private void joinUs() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				shut.setEnabled(false);
				Toast.makeText(getApplicationContext(),
						ResourceUtil.getString(R.string.info_waitfriend),
						Toast.LENGTH_LONG).show();
			}
		};
		new Thread() {
			@Override
			public void run() {

				JsonDataGetApi api = new JsonDataGetApi();

				try {
					api.getString("u.ashx?op=jf&mid=" + myid + "&yid=" + userId
							+ "&name=" + StringUtils.encode(uname));
					// + "&name=" + uname);
					// Log.d("�����Ϊ����", "u.ashx?op=jf&mid=" + myid + "&yid="
					// + userId + "&name=" + StringUtils.encode(uname));
				} catch (JSONException e) {
					MobclickAgent.reportError(mContext, "YourPrifile_joinUs:"
							+ e.toString());
				} catch (Exception e) {
					MobclickAgent.reportError(mContext, "YourPrifile_joinUs:"
							+ e.toString());
				}

				Message message = new Message();
				// message.what=0;
				// message.obj=jsonArray;
				handler.sendMessage(message);

			}
		}.start();
	}

	// ��׼��������
	private void joinUsOk() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				shut.setText(ResourceUtil.getString(R.string.submit_cancelus));
				Toast.makeText(getApplicationContext(),
						ResourceUtil.getString(R.string.info_joinfriend),
						Toast.LENGTH_LONG).show();
			}
		};
		new Thread() {
			@Override
			public void run() {

				JsonDataGetApi api = new JsonDataGetApi();

				try {
					api.getString("u.ashx?op=jfok&mid=" + myid + "&yid="
							+ userId + "&name=" + StringUtils.encode(uname));
					// Log.d("��׼��������", "u.ashx?op=jfok&mid=" + myid +
					// "&yid="
					// + userId + "&name=" + StringUtils.encode(uname));
				} catch (JSONException e) {
					MobclickAgent.reportError(mContext, "YourPrifile_joinUsOk:"
							+ e.toString());
				} catch (Exception e) {
					MobclickAgent.reportError(mContext, "YourPrifile_joinUsOk:"
							+ e.toString());
				}

				Message message = new Message();
				// message.what=0;
				// message.obj=jsonArray;
				handler.sendMessage(message);

			}
		}.start();
	}

}