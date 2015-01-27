package com.limon.common;

import java.io.UnsupportedEncodingException;

public class BASE64Decoder {
	private static final byte[] dTable = { -2, -2, -2, -2, -2, -2, -2, -2, -2,
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
			62, -2, -2, -2, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -2, -2,
			-2, -1, -2, -2, -2, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
			14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -2, -2, -2, -2, -2,
			-2, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41,
			42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -2, -2, -2, -2, -2 };

	private static final byte value(char c) {
		if (c > 127)
			return -2;
		return dTable[c];
	}

	/**
	 * Do the decoding
	 * 
	 * @param s
	 *            base64-encoded data
	 * @return decoded data
	 */
	public static final byte[] decode(String s) {
		int i, j = 0, l = 0;
		byte[] d, e = { 0, 0, 0, 0 };
		byte c;
		for (i = 0; i < s.length(); i++) {
			if ((c = value(s.charAt(i))) > -2) {
				j++;
				switch (j) {
				case 1:
				case 2:
					if (c < 0)
						j = 0;
					break;
				case 3:
					if (c < 0) {
						j = 0;
						l++;
					}
					;
					break;
				case 4:
					if (c < 0)
						l += 2;
					else
						l += 3;
					j = 0;
				}
			}
		}
		d = new byte[l];
		j = 0;
		l = 0;
		for (i = 0; i < s.length(); i++) {
			if ((c = value(s.charAt(i))) > -2) {
				e[j] = c;
				j++;
				switch (j) {
				case 1:
				case 2:
					if (c < 0)
						j = 0;
					break;
				case 3:
					if (c < 0) {
						j = 0;
						d[l++] = (byte) ((e[0] << 2) | ((e[1] >> 4) & 3));
					}
					break;
				case 4:
					d[l++] = (byte) ((e[0] << 2) | ((e[1] >> 4) & 3));
					d[l++] = (byte) (((e[1] & 15) << 4) | (e[2] >> 2));
					if (c > -1)
						d[l++] = (byte) (((e[2] & 3) << 6) | c);
					j = 0;
				}
			}
		}
		return d;
	}

	public static String decodeToString(String s) {
		try {
			return new String(decode(s), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
