package com.limon.common;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 类说明： 字符串操作类
 * 
 * @author @Cundong
 * @weibo http://weibo.com/liucundong
 * @blog http://www.liucundong.com
 * @date Apr 29, 2011 2:50:48 PM
 * @version 1.0
 */
public class StringUtils {
	/**
	 * 判断给定字符串是否空白串。<br>
	 * 空白串是指由空格、制表符、回车符、换行符组成的字符串<br>
	 * 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isBlank(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	public static boolean isEmpty(String str) {
		if (null == str || str.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public static Map<String, String> splitResponse(String response)
			throws Exception {

		Map<String, String> map = new HashMap<String, String>();

		if (!StringUtils.isEmpty(response)) {

			String[] array = response.split("&");
			if (array.length > 2) {
				String tokenStr = array[0]; // oauth_token=xxxxx
				String secretStr = array[1];// oauth_token_secret=xxxxxxx
				String[] token = tokenStr.split("=");
				if (token.length == 2) {
					map.put("oauth_token", token[1]);
				}
				String[] secret = secretStr.split("=");
				if (secret.length == 2) {
					map.put("oauth_token_secret", secret[1]);
				}
			} else {
				throw new Exception("");
			}
		} else {
			throw new Exception("");
		}
		return map;
	}

	public static String encode(String s) {
		if (s == null) {
			return "";
		}
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static String decode(String s) {
		if (s == null) {
			return "";
		}
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	// public static String encrypt(String seed, String cleartext)
	// throws Exception {
	// byte[] rawKey = getRawKey(seed.getBytes());
	// byte[] result = encrypt(rawKey, cleartext.getBytes());
	// return toHex(result);
	// }

	public static String decrypt(String seed, String encrypted)
			throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes());
		byte[] enc = toByte(encrypted);
		byte[] result = decrypt(rawKey, enc);
		return new String(result);
	}

	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(seed);
		kgen.init(128, sr); // 192 and 256 bits may not be available
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}

	@SuppressWarnings("unused")
	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		byte[] encrypted = null;
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			encrypted = cipher.doFinal(clear);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encrypted;
	}

	private static byte[] decrypt(byte[] raw, byte[] encrypted) {
		byte[] decrypted = null;
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			decrypted = cipher.doFinal(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decrypted;
	}

	public static String toHex(String txt) {
		return toHex(txt.getBytes());
	}

	public static String fromHex(String hex) {
		return new String(toByte(hex));
	}

	public static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
					16).byteValue();
		return result;
	}

	public static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	private final static String HEX = "0123456789ABCDEF";

	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}

	// 四舍五入
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	// 取整和四舍五入
	public static int iround(double v) { // new BigDecimal("2").setScale(0,
		// BigDecimal.ROUND_HALF_UP);
		// BigDecimal b=new BigDecimal(v);
		// v=b.setScale(0,BigDecimal.ROUND_HALF_UP).intValue();
		return (int) Math.floor(v);
	}

	public static String radomUUID() {
		UUID uuid = UUID.randomUUID();
		String strUUID = uuid.toString();
		strUUID = strUUID.replaceAll("-", "");
		return strUUID;
	}

	public static String shortRadomGUID() {
		String strUUID = radomUUID().substring(14);
		return strUUID;
	}

	public static String datelineToDateStr(String dateline) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM月dd日 HH:mm");
		String str = "";
		try {
			Date date = sdf.parse(dateline);
			str = sdf1.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 使用正则表达式过滤HTML标记
	 * 
	 * @param source
	 *            待过滤内容
	 * @return
	 */
	public static String filterHtml(String source) {
		if (null == source) {
			return "";
		}
		return source.replaceAll("</?[^>]+>", "").replace("??", "").replace("&nbsp;", "").trim();
	}
}