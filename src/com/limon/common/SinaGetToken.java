package com.limon.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SinaGetToken {
	public static String oauth_callback = "weibo://SinaOfActivity";
	// public static String appKey = "3255371166";
	// public static String appSecret = "42d8582501a0c54c407b888c567d9f1b";
	public static String appKey = "2398870068";
	public static String appSecret = "410f0313221c9d73ee779c24e1f2192a";

	/*
	 * 
	 * 锟斤拷锟节伙拷取RequestToken锟斤拷也锟斤拷锟斤拷锟斤拷权锟侥碉拷一锟斤拷锟斤拷锟斤拷锟教拷锟斤拷貌锟斤拷锟斤拷锟斤拷榭?
	 * http://open.weibo.com/wiki/Oauth
	 * 锟剿凤拷锟斤拷锟斤拷实锟绞匡拷锟斤拷锟斤拷强锟揭斤拷锟介不要写锟缴撅拷态锟斤拷锟斤拷锟斤拷锟轿伙拷悴伙拷锟斤拷锟斤拷谩锟斤拷锟斤拷锟?
	 */
	public static String getRequestToken() {
		HttpURLConnection con = null;
		Random RAND = new Random();
		// a b c d e f g h i j k l m n o p q r s t u v w x y z
		// 锟斤拷锟斤拷锟斤拷傻锟絙aseString 锟叫的诧拷锟斤拷锟角撅拷锟斤拷锟斤拷母锟斤拷锟斤拷模锟斤拷锟斤拷锟斤拷锟斤拷锟揭伙拷锟斤拷锟矫粗碉拷锟斤拷卸锟斤拷锟斤拷锟斤拷锟?
		String method = "POST";
		String url = "http://api.t.sina.com.cn/oauth/request_token";

		String oauth_consumer_key = appKey;
		long oauth_nonce;
		String oauth_signature_method = "HMAC-SHA1";
		long oauth_timestamp;
		String oauth_version = "1.0";

		oauth_timestamp = System.currentTimeMillis() / 1000;
		oauth_nonce = oauth_timestamp + RAND.nextInt();
		String tempcallbackurl = oauth_callback;
		try {
			tempcallbackurl = URLEncoder.encode(tempcallbackurl, "UTF-8");
		} catch (UnsupportedEncodingException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		// 锟斤拷锟絙aseString锟斤拷一锟斤拷要注锟斤拷baseString锟杰革拷元锟截碉拷顺锟斤拷顺锟斤拷锟角帮拷锟秸诧拷锟斤拷锟斤拷锟斤拷母锟斤拷锟斤拷锟斤拷模锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟酵拷锟阶帮拷锟斤拷锟斤拷锟角否经癸拷锟斤拷encode锟斤拷锟斤拷痛锟斤拷锟斤拷锟藉步锟借。
		// 锟斤拷锟叫凤拷锟斤拷url锟斤拷锟酵的讹拷要锟斤拷encode锟劫放碉拷baseString锟斤拷锟芥，然锟斤拷锟劫讹拷锟斤拷锟叫的诧拷锟斤拷锟斤拷傻锟絊tring锟斤拷锟斤拷一锟斤拷encode锟斤拷
		// 锟斤拷锟斤拷锟角帮拷锟斤拷锟较凤拷锟绞凤拷式锟斤拷encode锟斤拷牡锟街?

		String baseString = "oauth_callback=" + tempcallbackurl
				+ "&oauth_consumer_key=" + oauth_consumer_key + "&oauth_nonce="
				+ oauth_nonce + "&oauth_signature_method="
				+ oauth_signature_method + "&oauth_timestamp="
				+ oauth_timestamp + "&oauth_version=" + oauth_version;
		String tempStr = null;
		try {
			baseString = URLEncoder.encode(baseString, "UTF-8");
			tempStr = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		baseString = method + '&' + tempStr + "&" + baseString;
		byte[] byteHMAC = null;
		String oauth_signature = null;
		/*
		 * 锟斤拷锟給auth_signature
		 */
		Mac mac = null;
		try {
			mac = Mac.getInstance("HmacSHA1");

			// 注锟斤拷锟斤拷锟饺拷墓锟斤拷锟斤拷要锟矫碉拷锟斤拷key锟斤拷锟斤拷一锟斤拷锟斤拷壹堑锟斤拷锟絜ncode锟斤拷锟斤拷锟斤拷锟斤拷锟?
			String key = URLEncoder.encode(appSecret, "UTF-8") + "&";
			SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
			mac.init(spec);
			byteHMAC = mac.doFinal(baseString.getBytes());
			oauth_signature = new BASE64Encoder().encode(byteHMAC);
			// 锟斤拷锟斤拷锟斤拷encode一锟轿★拷锟斤拷锟斤拷
			oauth_signature = URLEncoder.encode(oauth_signature, "UTF-8");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			oauth_signature = "error";
			e1.printStackTrace();
		}
		// httpHeader锟斤拷锟斤拷锟斤拷锟剿筹拷锟斤拷锟斤拷猓拷锟斤拷锟斤拷锟揭伙拷锟揭拷锟絙asestring锟叫碉拷锟斤拷全一锟斤拷锟杰讹拷也锟斤拷锟斤拷锟劫ｏ拷锟斤拷锟杰改变。
		String httpHeaderStr = "OAuth oauth_consumer_key=\""
				+ oauth_consumer_key + "\",oauth_signature_method=\""
				+ oauth_signature_method + "\",oauth_timestamp=\""
				+ oauth_timestamp + "\",oauth_nonce=\"" + oauth_nonce
				+ "\",oauth_version=\"" + oauth_version
				+ "\",oauth_signature=\"" + oauth_signature
				+ "\",oauth_callback=\"" + oauth_callback + "\"";

		// 锟斤拷锟斤拷锟斤拷
		try {
			URL postUrl = new URL(url);
			con = (HttpURLConnection) postUrl.openConnection();
			// 锟斤拷锟矫革拷锟街筹拷时
			con.setConnectTimeout(20000);
			con.setReadTimeout(120000);
			con.setDoInput(true);
			// 注锟斤拷Authorization锟斤拷锟杰达拷
			con.addRequestProperty("Authorization", httpHeaderStr);
			// 锟剿达拷锟侥凤拷锟绞凤拷式要锟斤拷baseString锟斤拷锟斤拷谋锟斤拷锟揭伙拷虏锟斤拷校锟?
			con.setRequestMethod("POST");
			// 锟斤拷取锟斤拷锟截达拷锟斤拷
			@SuppressWarnings("unused")
			int code = con.getResponseCode();
			InputStream is = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));
			StringBuffer buf = new StringBuffer();
			String line;
			while (null != (line = br.readLine())) {
				buf.append(line).append("\n");
			}
			System.out.println(buf);
			return buf.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";

	}

	/*
	 * 
	 * 锟斤拷锟节伙拷取AcsessToken锟斤拷也锟斤拷锟斤拷锟斤拷权锟斤拷锟斤拷锟揭伙拷锟斤拷锟斤拷锟角帮拷锟揭伙拷锟斤拷锟斤拷锟斤拷锟酵★拷欤拷揖筒锟斤拷俣锟斤拷锟斤拷锟剿★拷锟斤拷锟斤拷
	 */
	public static String getAcsessToken(String oauthtoken,
			String oauthTokenSecret, String oauthverifier) {

		HttpURLConnection con = null;
		Random RAND = new Random();
		// a b c d e f g h i j k l m n o p q r s t u v w x y z
		// 锟斤拷锟斤拷锟斤拷傻锟絙aseString 锟叫的诧拷锟斤拷锟角撅拷锟斤拷锟斤拷母锟斤拷锟斤拷模锟斤拷锟斤拷锟斤拷锟斤拷锟揭伙拷锟斤拷锟矫粗碉拷锟斤拷卸锟斤拷锟斤拷锟斤拷锟?
		String consumerSecret = appSecret;
		String method = "POST";
		String url = "http://api.t.sina.com.cn/oauth/access_token";
		String oauth_consumer_key = appKey;
		long oauth_nonce;
		String oauth_signature_method = "HMAC-SHA1";
		long oauth_timestamp;
		String oauth_token = oauthtoken;
		String oauth_verifier = oauthverifier;
		String oauth_version = "1.0";

		oauth_timestamp = System.currentTimeMillis() / 1000;
		oauth_nonce = oauth_timestamp + RAND.nextInt();

		String baseString = "oauth_consumer_key=" + oauth_consumer_key
				+ "&oauth_nonce=" + oauth_nonce + "&oauth_signature_method="
				+ oauth_signature_method + "&oauth_timestamp="
				+ oauth_timestamp + "&oauth_token=" + oauth_token
				+ "&oauth_verifier=" + oauth_verifier + "&oauth_version="
				+ oauth_version;
		String tempStr = null;
		try {
			baseString = URLEncoder.encode(baseString, "UTF-8");
			tempStr = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		baseString = method + '&' + tempStr + "&" + baseString;
		byte[] byteHMAC = null;
		String oauth_signature = null;
		/*
		 * 锟斤拷锟給auth_signature
		 */
		Mac mac = null;
		try {
			mac = Mac.getInstance("HmacSHA1");
			String key = URLEncoder.encode(consumerSecret, "UTF-8") + "&"
					+ URLEncoder.encode(oauthTokenSecret, "UTF-8");
			SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
			mac.init(spec);
			byteHMAC = mac.doFinal(baseString.getBytes());
			oauth_signature = new BASE64Encoder().encode(byteHMAC);
			oauth_signature = URLEncoder.encode(oauth_signature, "UTF-8");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			oauth_signature = "error";
			e1.printStackTrace();
		}
		String httpHeaderStr = "OAuth oauth_consumer_key=\""
				+ oauth_consumer_key + "\",oauth_signature_method=\""
				+ oauth_signature_method + "\",oauth_timestamp=\""
				+ oauth_timestamp + "\",oauth_nonce=\"" + oauth_nonce
				+ "\",oauth_version=\"" + oauth_version
				+ "\",oauth_signature=\"" + oauth_signature
				+ "\",oauth_token=\"" + oauth_token + "\",oauth_verifier=\""
				+ oauth_verifier + "\"";

		// 锟斤拷锟斤拷锟斤拷
		try {
			URL postUrl = new URL(url);
			con = (HttpURLConnection) postUrl.openConnection();
			con.setConnectTimeout(20000);
			con.setReadTimeout(120000);
			con.setDoInput(true);
			con.addRequestProperty("Authorization", httpHeaderStr);
			con.setRequestMethod("POST");
			@SuppressWarnings("unused")
			int code = con.getResponseCode();
			InputStream is = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));
			StringBuffer buf = new StringBuffer();
			String line;
			while (null != (line = br.readLine())) {
				buf.append(line).append("\n");
			}
			System.out.println(buf);
			return buf.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";

	}

}
