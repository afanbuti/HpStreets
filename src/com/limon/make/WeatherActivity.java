package com.limon.make;

import java.io.FileOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.limon.common.InfoHelper;
import com.limon.common.NetUtil;
import com.limon.common.ResourceUtil;
import com.mobclick.android.MobclickAgent;

public class WeatherActivity extends BaseActivity{
	//private String SERVICE_URL="http://www.webxml.com.cn/WebServices/WeatherWebService.asmx";
	//手机号码归属地http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx
	//private String SERVICE_NS="http://WebXml.com.cn/";
	//private static String cityName="北京";//城市名    
    //private static Geocoder geocoder;//此对象能通过经纬度来获取相应的城市等信息    
    //private static BestLocationListener mLocationListener = null;
    //private EditText mSourceText;
	private String weatherString ="";
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather);
		mContext = getApplicationContext();
		if (!InfoHelper.checkNetWork(mContext)) {
			Toast.makeText(mContext,
					ResourceUtil.getString(R.string.error_net),
					Toast.LENGTH_LONG).show();
		}

        ImageView btnd = (ImageView) findViewById(R.id.decr);
		btnd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, MyInfoActivity.class);
				// mycontext.startActivity(intent);
				GroupFour.group.getLocalActivityManager().removeAllActivities();
				View view = GroupFour.group
						.getLocalActivityManager()
						.startActivity("Four",
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();
				// Again, replace the view
				GroupFour.group.replaceView(view);
			}
		});
		ImageView share = (ImageView) findViewById(R.id.title_share);
		share.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String fname = Environment.getExternalStorageDirectory()+"/"+ InfoHelper.getFileName()+".png";
				View view = v.getRootView();
				view.setDrawingCacheEnabled(true);
				view.buildDrawingCache();
				Bitmap bitmap = view.getDrawingCache();
				if (bitmap != null) {
					try {
						FileOutputStream out = new FileOutputStream(fname);
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
						System.out.println("file" + fname + "output done.");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				Intent intent = new Intent();
				intent.setClass(mContext, MyContentTimesActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("weatherimg", fname);
				intent.putExtra("content", weatherString);
				mContext.startActivity(intent);
			}
		});
		
		viewWeather();
	}
    private void viewWeather(){
    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
    	String cityName = settings.getString("mcityName", "北京");
    	TextView city_name = (TextView) findViewById(R.id.title_city_name);
		city_name.setText(cityName);
    	String w = NetUtil.getWeather(mContext,cityName);
    	MobclickAgent.reportError(mContext,	"Weather000:" + w);
	    String[] v = w.split("; string=");
    	if(v.length>1){
    	    String n="";
    	    for(int i=0;i<v.length;i++){
            	n += "String["+i+"]="+v[i]+"\r\n";
            }
    	    //Log.d("temp", n);
			if (!(v[4] == null || v[4].equals(""))) {
				String[] t = v[4].split("；");
				String temp = "";
				if (t.length > 0) {
					int i = t[0].lastIndexOf("：");
					if (i > 0) {
						temp = t[0].substring(i + 1);
					}
					temp = temp.replace("℃", "");
				}
				TextView city_temp = (TextView) findViewById(R.id.city_temp);
				city_temp.setText(temp);
			}
            Resources resources = mContext.getResources();
            TextView city_temp_today = (TextView)findViewById(R.id.cityview_temp_today);
        	TextView city_curr_today = (TextView)findViewById(R.id.cityview_curr_today);        	
        	city_curr_today.setText(v[3]+"更新");
        	city_temp_today.setText(v[4]+" "+v[5]);
        	weatherString="#开心看天气#"+v[1]+v[4]+" "+v[5];
        	TextView city_temp = (TextView)findViewById(R.id.cityview_temp);
        	TextView city_curr = (TextView)findViewById(R.id.cityview_curr);
        	ImageView city_icon1 = (ImageView)findViewById(R.id.cityview_icon1);
        	ImageView city_icon2 = (ImageView)findViewById(R.id.cityview_icon2);
            int indentify1 = resources.getIdentifier("ww"+filterStr(v[10]), "drawable", mContext.getPackageName());
            if(indentify1>0){
            	city_icon1.setImageResource(indentify1);
            }
            int indentify2 = resources.getIdentifier("ww"+filterStr(v[11]), "drawable", mContext.getPackageName());
            if(indentify2>0){
            	city_icon2.setImageResource(indentify2);
            }
            if(v[10].equals(v[11])){
            	city_icon1.setVisibility(View.VISIBLE);
            	city_icon2.setVisibility(View.GONE);
            }
            city_curr.setText(v[7]);
            city_temp.setText(v[8]+v[9]);
            TextView city_temp2 = (TextView)findViewById(R.id.cityview2_temp);
        	TextView city_curr2 = (TextView)findViewById(R.id.cityview2_curr);
        	ImageView city_icon21 = (ImageView)findViewById(R.id.cityview2_icon1);
        	ImageView city_icon22 = (ImageView)findViewById(R.id.cityview2_icon2);
            int indy21 = resources.getIdentifier("ww"+filterStr(v[15]), "drawable", mContext.getPackageName());
            if(indy21>0){
            	city_icon21.setImageResource(indy21);
            }
            int indy22 = resources.getIdentifier("ww"+filterStr(v[16]), "drawable", mContext.getPackageName());
            if(indy22>0){
            	city_icon22.setImageResource(indy22);
            }        
            if(v[15].equals(v[16])){
            	city_icon21.setVisibility(View.VISIBLE);
            	city_icon22.setVisibility(View.GONE);
            }
            city_curr2.setText(v[12]);
            city_temp2.setText(v[13]+v[14]);
            
            TextView city_temp3 = (TextView)findViewById(R.id.cityview3_temp);
        	TextView city_curr3 = (TextView)findViewById(R.id.cityview3_curr);
            ImageView city_icon31 = (ImageView)findViewById(R.id.cityview3_icon1);
        	ImageView city_icon32 = (ImageView)findViewById(R.id.cityview3_icon2);
            int indy31 = resources.getIdentifier("ww"+filterStr(v[20]), "drawable", mContext.getPackageName());
            if(indy31>0){
            	city_icon31.setImageResource(indy31);
            }
            int indy32 = resources.getIdentifier("ww"+filterStr(v[21]), "drawable", mContext.getPackageName());
            if(indy32>0){
            	city_icon32.setImageResource(indy32);
            }        
            if(v[20].equals(v[21])){
            	city_icon31.setVisibility(View.VISIBLE);
            	city_icon32.setVisibility(View.GONE);
            }
            city_curr3.setText(v[17]);
            city_temp3.setText(v[18]+v[19]);
            
            TextView city_temp4 = (TextView)findViewById(R.id.cityview4_temp);
        	TextView city_curr4 = (TextView)findViewById(R.id.cityview4_curr);
            ImageView city_icon41 = (ImageView)findViewById(R.id.cityview4_icon1);
        	ImageView city_icon42 = (ImageView)findViewById(R.id.cityview4_icon2);
            int indy41 = resources.getIdentifier("ww"+filterStr(v[25]), "drawable", mContext.getPackageName());
            if(indy41>0){
            	city_icon41.setImageResource(indy41);
            }
            int indy42 = resources.getIdentifier("ww"+filterStr(v[26]), "drawable", mContext.getPackageName());
            if(indy42>0){
            	city_icon42.setImageResource(indy42);
            }        
            if(v[25].equals(v[26])){
            	city_icon41.setVisibility(View.VISIBLE);
            	city_icon42.setVisibility(View.GONE);
            }
            city_curr4.setText(v[22]);
            city_temp4.setText(v[23]+v[24]);
            
            TextView city_temp5 = (TextView)findViewById(R.id.cityview5_temp);
        	TextView city_curr5 = (TextView)findViewById(R.id.cityview5_curr);
            ImageView city_icon51 = (ImageView)findViewById(R.id.cityview5_icon1);
        	ImageView city_icon52 = (ImageView)findViewById(R.id.cityview5_icon2);
            int indy51 = resources.getIdentifier("ww"+filterStr(v[30]), "drawable", mContext.getPackageName());
            if(indy51>0){
            	city_icon51.setImageResource(indy51);
            }
            int indy52 = resources.getIdentifier("ww"+filterStr(v[31]), "drawable", mContext.getPackageName());
            if(indy52>0){
            	city_icon52.setImageResource(indy52);
            }        
            if(filterStr(v[30]).equals(filterStr(v[31]))){
            	city_icon51.setVisibility(View.VISIBLE);
            	city_icon52.setVisibility(View.GONE);
            }
            city_curr5.setText(v[27]);
            city_temp5.setText(v[28]+v[29]);
               
            }
    }

	private String filterStr(String mcityName) {
		if (mcityName.length() != 0) {
			mcityName = mcityName.replace(".gif", "");
			mcityName = mcityName.replace(".gif; }", "");
			// return mcityName.substring(0, (mcityName.length()-1));
		}
		return mcityName;

	}

}
