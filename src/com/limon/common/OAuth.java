package com.limon.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.util.Log;

import com.limon.bean.Parameter;

/**
 *@author coolszy
 *@date 2011-5-29
 *@blog http://blog.csdn.net/coolszy
 */

public class OAuth {
	public static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
	public static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
	public static final String OAUTH_TIMESTAMP = "oauth_timestamp";
	public static final String OAUTH_NONCE = "oauth_nonce";
	public static final String OAUTH_VERSION = "oauth_version";
	public static final String OAUTH_CALLBACK = "oauth_callback";
	public static final String OAUTH_VERIFIER = "oauth_verifier";
	public static final String OAUTH_TOKEN = "oauth_token";
	public static final String OAUTH_SIGNATURE = "oauth_signature";

	public static final String FORMAT = "format";
	public static final String CONTENT = "content";
	public static final String CLIENTIP = "clientip";
	public static final String JING = "jing";
	public static final String WEI = "wei";

	public static final String CONSUMER_KEY = "801002513";
	public static final String CONSUMER_SECRET = "3516588cfe63790cc7dc732bb21262c6";
	public static final String SIGNATURE_METHOD = "HMAC-SHA1";
	public static final String VERSION = "1.0";

	public static final String ENCODING = "UTF-8";

	/**
	 * 鑾峰彇璇锋眰鍙傛暟
	 * 
	 * @param urlStr
	 *            璇锋眰URL
	 * @param httpMethod
	 *            璇锋眰鏂瑰紡
	 * @param callBack
	 *            璁よ瘉鎴愬姛鍚庢祻瑙堝櫒浼氳閲嶅畾鍚戝埌杩欎釜url涓紝娌℃湁鍥炶皟url锛屾鏃惰缃负瀛楃涓测?null鈥濆嵆鍙?
	 * @return
	 * @throws Exception
	 */
	public static String getPostParams(String urlStr, String httpMethod,
			String callBack) throws Exception {
		return getPostParams(urlStr, httpMethod, callBack, null, null, null);
	}

	/**
	 * 鑾峰彇璇锋眰鍙傛暟
	 * 
	 * @param urlStr
	 *            璇锋眰URL
	 * @param httpMethod
	 *            璇锋眰鏂瑰紡
	 * @param oauthToken
	 *            oauthToken
	 * @param tokenSecret
	 *            oauthTokenSecret
	 * @param content
	 *            寰崥鍐呭
	 * @param format
	 *            杩斿洖鏍煎紡xml鎴杍son
	 * @param clientIP
	 *            瀹㈡埛绔疘P
	 * @param jing
	 *            绮惧害
	 * @param wei
	 *            绾害
	 * @return
	 * @throws Exception
	 */
	public static String getPostParams(String urlStr, String httpMethod,
			String oauthToken, String tokenSecret, String content,
			String format, String clientIP, String jing, String wei)
			throws Exception {
		return getPostParams(urlStr, httpMethod, null, oauthToken, tokenSecret,
				null, content, format, clientIP, jing, wei);
	}

	/**
	 * 鑾峰彇璇锋眰鍙傛暟
	 * 
	 * @param urlStr
	 *            璇锋眰URL
	 * @param httpMethod
	 *            璇锋眰鏂瑰紡
	 * @param callBack
	 *            璁よ瘉鎴愬姛鍚庢祻瑙堝櫒浼氳閲嶅畾鍚戝埌杩欎釜url涓紝娌℃湁鍥炶皟url锛屾鏃惰缃负瀛楃涓测?null鈥濆嵆鍙?
	 * @param oauthToken
	 *            oauthToken
	 * @param tokenSecret
	 *            oauthTokenSecret
	 * @param verifier
	 *            楠岃瘉鐮?
	 * @return
	 * @throws Exception
	 */
	public static String getPostParams(String urlStr, String httpMethod,
			String callBack, String oauthToken, String tokenSecret,
			String verifier) throws Exception {
		return getPostParams(urlStr, httpMethod, callBack, oauthToken,
				tokenSecret, verifier, null, null, null, null, null);
	}

	/**
	 * 鑾峰彇璇锋眰鍙傛暟
	 * 
	 * @param urlStr
	 *            璇锋眰URL
	 * @param httpMethod
	 *            璇锋眰鏂瑰紡
	 * @param callBack
	 *            璁よ瘉鎴愬姛鍚庢祻瑙堝櫒浼氳閲嶅畾鍚戝埌杩欎釜url涓紝娌℃湁鍥炶皟url锛屾鏃惰缃负瀛楃涓测?null鈥濆嵆鍙?
	 * @param oauthToken
	 *            oauthToken
	 * @param tokenSecret
	 *            oauthTokenSecret
	 * @param verifier
	 *            楠岃瘉鐮?
	 * @param content
	 *            寰崥鍐呭
	 * @param format
	 *            杩斿洖鏍煎紡xml鎴杍son
	 * @param clientIP
	 *            瀹㈡埛绔疘P
	 * @param jing
	 *            绮惧害
	 * @param wei
	 *            绾害
	 * @return
	 * @throws Exception
	 */
	public static String getPostParams(String urlStr, String httpMethod,
			String callBack, String oauthToken, String tokenSecret,
			String verifier, String content, String format, String clientIP,
			String jing, String wei) throws Exception {
		// 淇濆瓨鍙傛暟闆嗗悎
		List<Parameter> params = new ArrayList<Parameter>();
		// 鑾峰彇鏃堕棿鎴?
		String timestamp = generateTimeStamp();
		// 鑾峰彇鍗曟鍊?
		String nonce = generateNonce();
		// 娣诲姞鍙傛暟
		params.add(new Parameter(OAUTH_CONSUMER_KEY, encode(CONSUMER_KEY)));
		params.add(new Parameter(OAUTH_SIGNATURE_METHOD,
				encode(SIGNATURE_METHOD)));
		params.add(new Parameter(OAUTH_TIMESTAMP, encode(timestamp)));
		params.add(new Parameter(OAUTH_NONCE, encode(nonce)));
		params.add(new Parameter(OAUTH_VERSION, encode(VERSION)));

		if (!StringUtils.isEmpty(callBack)) {
			params.add(new Parameter(OAUTH_CALLBACK, encode(callBack)));
		}
		// 楠岃瘉鐮?
		if (!StringUtils.isEmpty(verifier)) {
			params.add(new Parameter(OAUTH_VERIFIER, encode(verifier)));
		}
		// oauthToken
		if (!StringUtils.isEmpty(oauthToken)) {
			params.add(new Parameter(OAUTH_TOKEN, encode(oauthToken)));
		}
		// 寰崥鍐呭
		if (!StringUtils.isEmpty(content)) {
			params.add(new Parameter(CONTENT, encode(content)));
		}
		// 杩斿洖鏍煎紡
		if (!StringUtils.isEmpty(format)) {
			params.add(new Parameter(FORMAT, encode(format)));
		}
		// 瀹㈡埛绔疘P
		if (!StringUtils.isEmpty(clientIP)) {
			params.add(new Parameter(CLIENTIP, encode(clientIP)));
		}
		// 缁忓害
		if (!StringUtils.isEmpty(jing)) {
			params.add(new Parameter(JING, encode(oauthToken)));
		}
		// 绾害
		if (!StringUtils.isEmpty(jing)) {
			params.add(new Parameter(WEI, encode(wei)));
		}

		// 鑾峰彇绛惧悕鍊?
		String signature = generateSignature(httpMethod, urlStr, params,
				CONSUMER_SECRET, tokenSecret);
		params.add(new Parameter(OAUTH_SIGNATURE, encode(signature)));

		// 鏋勯?璇锋眰鍙傛暟瀛楃涓?
		StringBuilder urlBuilder = new StringBuilder();
		for (Parameter param : params) {
			urlBuilder.append(param.getName());
			urlBuilder.append("=");
			urlBuilder.append(param.getValue());
			urlBuilder.append("&");
		}

		urlBuilder.deleteCharAt(urlBuilder.length() - 1);
		Log.i("OAuth=", "paramsStr=" + urlBuilder.toString());
		return urlBuilder.toString();
	}

	/**
	 * 浜х敓绛惧悕鍊?
	 * 
	 * @param method
	 *            璇锋眰鏂规硶
	 * @param url
	 *            璇锋眰璺緞
	 * @param params
	 *            璇锋眰鍙傛暟闆嗗悎
	 * @param consumerSecret
	 *            AppSecret
	 * @param tokenSecret
	 *            tokenSecret
	 * @return
	 * @throws Exception
	 */
	private static String generateSignature(String method, String url,
			List<Parameter> params, String consumerSecret, String tokenSecret)
			throws Exception {
		// 鑾峰彇婧愪覆
		String signatureBase = generateSignatureBase(method, url, params);
		// 鏋勯?瀵嗛挜
		String oauthKey = "";
		if (null == tokenSecret || tokenSecret.equals("")) {
			oauthKey = encode(consumerSecret) + "&";
		} else {
			oauthKey = encode(consumerSecret) + "&" + encode(tokenSecret);
		}
		BASE64Encoder en = new BASE64Encoder();
		byte[] encryptBytes = en.HmacSHA1Encrypt(signatureBase, oauthKey);
		return en.encode(encryptBytes);
	}

	/**
	 * 鏋勯?婧愪覆锛欻TTP璇锋眰鏂瑰紡 & urlencode(url) & urlencode(a=x&b=y&...)
	 * 
	 * @param method
	 *            璇锋眰鏂规硶
	 * @param url
	 *            璇锋眰璺緞
	 * @param params
	 *            璇锋眰鍙傛暟
	 * @return
	 */
	private static String generateSignatureBase(String method, String url,
			List<Parameter> params) {
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(method.toUpperCase() + "&");
		url = encode(url.toLowerCase());
		urlBuilder.append(url + "&");
		// 鎵?湁鍙傛暟鎸塳ey杩涜瀛楀吀鍗囧簭鎺掑垪
		Collections.sort(params);
		for (Parameter param : params) {
			String name = encode(param.getName());
			String value = encode(param.getValue());
			urlBuilder.append(name);
			urlBuilder.append("%3D"); // 瀛楃 =
			urlBuilder.append(value);
			urlBuilder.append("%26"); // 瀛楃 &
		}
		// 鍒犻櫎鏈熬鐨?26
		urlBuilder.delete(urlBuilder.length() - 3, urlBuilder.length());
		return urlBuilder.toString();
	}

	/**
	 * 瀵瑰瓧绗︿覆杩涜缂栫爜
	 * 
	 * @param s
	 * @return
	 * @see <a
	 *      href="http://tools.ietf.org/html/draft-hammer-oauth-10#section-3.6">涓轰粈涔堢紪鐮佸悗闇?鍐嶆澶勭悊</a>
	 * @see <a
	 *      href="http://oauth.googlecode.com/svn/code/java/core/commons/src/main/java/net/oauth/OAuth.java">鍙傝?浠ｇ爜锛岃繘鍘诲悗鎼滅储RLEncoder.encode</a>
	 */
	public static String encode(String s) {
		if (s == null) {
			return "";
		}
		String encoded = "";
		try {
			encoded = URLEncoder.encode(s, ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < encoded.length(); i++) {
			char c = encoded.charAt(i);
			if (c == '+') {
				sBuilder.append("%20");
			} else if (c == '*') {
				sBuilder.append("%2A");
			}
			// URLEncoder.encode()浼氭妸鈥渵鈥濅娇鐢ㄢ?%7E鈥濊〃绀猴紝鍥犳鍦ㄨ繖閲屾垜浠渶瑕佸彉鎴愨?~鈥?
			else if ((c == '%') && ((i + 1) < encoded.length())
					&& ((i + 2) < encoded.length())
					& (encoded.charAt(i + 1) == '7')
					&& (encoded.charAt(i + 2) == 'E')) {
				sBuilder.append("~");
				i += 2;
			} else {
				sBuilder.append(c);
			}
		}
		return sBuilder.toString();
	}

	/**
	 * 浜х敓鏃堕棿鎴?
	 * 
	 * @return
	 */
	private static String generateTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}

	/**
	 * 浜х敓鍗曟鍊?
	 * 
	 * @param is32
	 *            浜х敓瀛楃涓查暱搴︽槸鍚︿负32浣?
	 * @return
	 */
	private static String generateNonce() {
		Random random = new Random();
		// 浜х敓123400鑷?999999闅忔満鏁?
		String result = String.valueOf(random.nextInt(9876599) + 123400);
		// 杩涜MD5鍔犲瘑
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(result.getBytes());
			byte b[] = md.digest();
			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
}
