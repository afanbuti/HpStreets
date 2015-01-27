package com.limon.common;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.mobclick.android.MobclickAgent;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;

public class NetUtil {
	//private static String SERVICE_URL="http://www.webxml.com.cn/WebServices/WeatherWebService.asmx";
	private static String SERVICE_URL="http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx";
	//手机号码归属地http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx
	private static String SERVICE_NS="http://WebXml.com.cn/";
	public static BitmapDrawable getImageFromUrl(URL url) {

		BitmapDrawable icon = null;

		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			icon = new BitmapDrawable(conn.getInputStream());

			conn.disconnect();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return icon;

	}

	public static String getTimeDiff(Date date) {
		Calendar cal = Calendar.getInstance();
		long diff = 0;
		Date dnow = cal.getTime();
		String str = "";
		diff = dnow.getTime() - date.getTime();

		//System.out.println("diff---->" + date);

		if (diff > 24 * 60 * 60 * 1000) {
			// System.out.println("1天前");
			str = "1天前";
		} else if (diff > 5 * 60 * 60 * 1000) {
			// System.out.println("2小时前");
			str = "2小时前";
		} else if (diff > 1 * 60 * 60 * 1000) {
			// System.out.println("1小时前");
			str = "小时前";
		} else if (diff > 30 * 60 * 1000) {
			// System.out.println("30分钟前");
			str = "30分钟前";
		} else if (diff > 15 * 60 * 1000) {
			// System.out.println("15分钟前");
			str = "15分钟前";
		} else if (diff > 5 * 60 * 1000) {
			// System.out.println("5分钟前");
			str = "5分钟前";
		} else if (diff > 1 * 60 * 1000) {
			// System.out.println("1分钟前");
			str = "1分钟前";
		} else {
			str = "刚刚";
		}

		return str;
	}
	public static String getWeather(Context mContext,String cityName){
    	String returnStr="";
		//String methodName="getWeatherbyCityName";    			
    	String methodName="getWeather";
		//创建SoapSerializationEnvelope对象
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		//创建SoapObject对象，创建该对象时需要传入所要调用的Web Service的命名空间和WebService方法名
		SoapObject request = new SoapObject(SERVICE_NS, methodName);
		//有参数传给Web Service服务器端
		//request.addProperty("theCityName", cityName);
		request.addProperty("theCityCode", cityName);
		request.addProperty("theUserID", "");
		//调用SoapSerializationEnvelope的setOutputSoapObject()方法，或者直接对bodyOut属性赋值，将前两步创建的SoapObject对象设为SoapSerializationEnvelope的传出SOAP消息体
		envelope.bodyOut = request;
		//设置是否调用的是dotNet开发的WebService
		envelope.dotNet = true;
		// SOAP Action
		String soapAction = SERVICE_NS+methodName;//"http://WebXml.com.cn/getMobileCodeInfo";

		//创建HttpTransportSE对象，该对象用于调用WebService操作
		HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);
		//调用对象的call()方法，并以SoapSerializationEnvelope作为参数调用远程的web service
		try {
			//Log.d("cityName",cityName);
			//mSourceText.setText(cityName);
			ht.call(null, envelope);
			//ht.call(soapAction, envelope);
		} catch (IOException e) {
			MobclickAgent.reportError(mContext,	"WeatherIOException:" + e.toString());
			//e.printStackTrace();
		} catch (XmlPullParserException e) {
			MobclickAgent.reportError(mContext,	"WeatherXmlPullParserException:" + e.toString());
			//e.printStackTrace();
		}
		//访问SoapSerializationEnvelope对象的bodyIn属性，该属性返回一个SoapObject对象，该对象就代表Web service的返回消息，解析该对象，即可获得调用web service的返回值
		SoapObject result = (SoapObject) envelope.bodyIn;
		
//		Object rst = null;
//		try {
//			rst = envelope.getResponse();
//		} catch (SoapFault e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String [] str =(String[])rst;
		//String n="";
		if(result!=null){
			returnStr = result.getProperty(0).toString();
        		}
		return returnStr;
    }
	public static List<String> getProviceList(){
		List<String> provinces=new ArrayList<String>();
		String methodName="getRegionProvince";
		//实例化SoapObject对象
        SoapObject request=new SoapObject(SERVICE_NS, methodName);
        //获得序列化的Envelope
        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut=request;
        (new MarshalBase64()).register(envelope);
        //Android传输对象
        //AndroidHttpTransport transport=new AndroidHttpTransport(serviceURL);
        HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);
        ht.debug=true;
        String soapAction = SERVICE_NS+methodName;
        //调用
        try {
            ht.call(soapAction, envelope);
//            if(envelope.getResponse()!=null){
//            	return parse(envelope.bodyIn.toString());
//            }   
            SoapObject result=(SoapObject)envelope.getResponse();
            int count=result.getPropertyCount();
            for(int index=0;index<count;index++){
                provinces.add(result.getProperty(index).toString());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }      
        return provinces;
    } 
	public static List<String> getCityList(String theRegion){
		List<String> citys=new ArrayList<String>();
		//实例化SoapObject对象
		String methodName="getSupportCityString";
		SoapObject request=new SoapObject(SERVICE_NS, methodName);
		request.addProperty("theRegionCode", theRegion);
        //获得序列化的Envelope
        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut=request;
        (new MarshalBase64()).register(envelope);
        //Android传输对象
        //AndroidHttpTransport transport=new AndroidHttpTransport(SERVICE_URL);
        HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);
        ht.debug=true;
        String soapAction = SERVICE_NS+methodName;
        //调用
        try {
            ht.call(soapAction, envelope);
//            if(envelope.getResponse()!=null){
//                return parse(envelope.bodyIn.toString());
//            }
            
            SoapObject result=(SoapObject)envelope.getResponse();
            int count=result.getPropertyCount();
            for(int index=0;index<count;index++){
                citys.add(result.getProperty(index).toString());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }   
        return citys;
    }    
	private static List<String> parse(String str){
        String temp;
        List<String> list=new ArrayList<String>();
        if(str!=null && str.length()>0){
            int start=str.indexOf("string");
            int end=str.lastIndexOf(";");
            temp=str.substring(start, end-3);
            String []test=temp.split(";");
            
             for(int i=0;i<test.length;i++){
                 if(i==0){
                     temp=test[i].substring(7);
                 }else{
                     temp=test[i].substring(8);
                 }
                 int index=temp.indexOf(",");
                 list.add(temp.substring(0, index));
             }
        }
        return list;
    }
}
