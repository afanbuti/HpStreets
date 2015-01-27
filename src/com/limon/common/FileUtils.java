package com.limon.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.os.Environment;
import android.util.Log;

import com.limon.make.BMapApi;

/**
 * 锟斤拷说锟斤拷锟斤拷 锟侥硷拷锟斤拷写
 * 
 * @author @Cundong
 * @weibo http://weibo.com/liucundong
 * @blog http://www.liucundong.com
 * @date Apr 29, 2011 2:50:48 PM
 * @version 1.0
 */
public class FileUtils {
	/**
	 * 写锟侥憋拷锟侥硷拷 锟斤拷Android系统锟叫ｏ拷锟侥硷拷锟斤拷锟斤拷锟斤拷 /data/data/PACKAGE_NAME/files 目录锟斤拷
	 * 
	 * @param context
	 * @param msg
	 */
	public static void write(Context context, String fileName, String content) {
		if (content == null)
			content = "";

		try {
			FileOutputStream fos = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			fos.write(content.getBytes());

			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 锟斤拷取锟侥憋拷锟侥硷拷
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String read(Context context, String fileName) {
		try {
			FileInputStream in = context.openFileInput(fileName);
			return readInStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private static String readInStream(FileInputStream inStream) {
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[512];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, length);
			}

			outStream.close();
			inStream.close();
			return outStream.toString();
		} catch (IOException e) {
			Log.i("FileTest", e.getMessage());
		}
		return null;
	}

	/**
	 * 锟斤拷取SD锟斤拷路锟斤拷
	 * 
	 * @param buffer
	 * @param folder
	 * @param fileName
	 * @return
	 */
	public static String sdPath(String folder) {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		String folderPath = "";
		if (sdCardExist) {
			folderPath = Environment.getExternalStorageDirectory()
					+ File.separator + folder + File.separator;
		}
		return folderPath;
	}

	public static File createFile(String folderPath, String fileName) {
		File destDir = new File(folderPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		return new File(folderPath, fileName + fileName);
	}

	/**
	 * 锟斤拷锟街伙拷写图片
	 * 
	 * @param buffer
	 * @param folder
	 * @param fileName
	 * @return
	 */
	public static boolean writeFile(byte[] buffer, String folder,
			String fileName) {
		boolean writeSucc = false;

		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);

		String folderPath = "";
		if (sdCardExist) {
			folderPath = Environment.getExternalStorageDirectory()
					+ File.separator + folder + File.separator;
		} else {
			writeSucc = false;
		}

		File fileDir = new File(folderPath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}

		File file = new File(folderPath + fileName);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(buffer);
			writeSucc = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return writeSucc;
	}

	/**
	 * 锟斤拷锟斤拷募锟斤拷锟斤拷路锟斤拷锟斤拷取锟侥硷拷锟斤拷
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (StringUtils.isBlank(filePath))
			return "";
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}

	/**
	 * 锟斤拷锟斤拷募锟斤拷木锟斤拷路锟斤拷锟斤拷取锟侥硷拷锟斤拷锟斤拷锟斤拷展锟斤拷
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameNoFormat(String filePath) {
		if (StringUtils.isBlank(filePath)) {
			return "";
		}
		int point = filePath.lastIndexOf('.');
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1,
				point);
	}

	/**
	 * 锟斤拷取锟侥硷拷锟斤拷展锟斤拷
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileFormat(String fileName) {
		if (StringUtils.isBlank(fileName))
			return "";

		int point = fileName.lastIndexOf('.');
		return fileName.substring(point + 1);
	}

	/**
	 * 锟斤拷取锟侥硷拷锟斤拷小
	 * 
	 * @param filePath
	 * @return
	 */
	public static long getFileSize(String filePath) {
		long size = 0;

		File file = new File(filePath);
		if (file != null && file.exists()) {
			size = file.length();
		}
		return size;
	}

	/**
	 * 锟斤拷取锟侥硷拷锟斤拷小
	 * 
	 * @param size
	 *            锟街斤拷
	 * @return
	 */
	public static String getFileSize(long size) {
		if (size <= 0)
			return "0";
		java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
		float temp = (float) size / 1024;
		if (temp >= 1024) {
			return df.format(temp / 1024) + "M";
		} else {
			return df.format(temp) + "K";
		}
	}

	public static byte[] toBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1) {
			out.write(ch);
		}
		byte buffer[] = out.toByteArray();
		out.close();
		return buffer;
	}

	/**
	 * 图片水印
	 */
	public static Bitmap waterBitmap(Bitmap src, Bitmap watermark, float x,
			float y) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 锟斤拷锟斤拷一锟斤拷锟铰的猴拷SRC锟斤拷锟饺匡拷锟揭伙拷锟斤拷位图

		Canvas cv = new Canvas(newb);
		// draw src into
		cv.drawBitmap(src, 0, 0, null);// 锟斤拷 0锟斤拷0锟斤拷昕硷拷锟斤拷锟絪rc
		// draw watermark into
		if (watermark != null) {
			cv.drawBitmap(watermark, x, y - 30, null);// 锟斤拷src锟斤拷锟斤拷锟铰角伙拷锟斤拷水印
		}
		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 锟斤拷锟斤拷
		// store
		cv.restore();// 锟芥储
		return newb;
	}

	/**
	 * 锟斤拷锟斤拷水印
	 */
	public static Bitmap waterWord(Bitmap src, float x, float y) {
		/* 时锟斤拷水印锟斤拷锟斤拷 */
		// Time t = new Time();
		// t.setToNow(); // 取锟斤拷系统时锟戒。
		// int year = t.year;
		// int month = t.month;
		// int date = t.monthDay;
		// int hour = t.hour;
		// int minute = t.minute;
		// int second = t.second;
		int w = src.getWidth(), h = src.getHeight();
		// String mstrTitle = year + "-" + month + "-" + date
		// + "," + hour + ":" + minute + ":" + second
		// + "---" + Myaddress;
		String mstrTitle = "@锟斤拷锟侥斤拷锟斤拷";
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvasTemp = new Canvas(newb);
		Paint p = new Paint();
		String familyName = "锟斤拷锟斤拷";
		Typeface font = Typeface.create(familyName, Typeface.BOLD);
		p.setColor(Color.RED);
		p.setTypeface(font);
		p.setTextSize(22);
		canvasTemp.drawBitmap(src, 0, 0, p);
		canvasTemp.drawText(mstrTitle, 0, 20, p);
		// newb.compress(CompressFormat.JPEG, 100, bos);
		// save all clip
		canvasTemp.save(Canvas.ALL_SAVE_FLAG);// 锟斤拷锟斤拷
		// store
		canvasTemp.restore();// 锟芥储
		return newb;
	}

	// /**
	// * 锟斤拷锟斤拷sdcard锟较碉拷剩锟斤拷占锟?
	// * @return
	// */
	// private int freeSpaceOnSd() {
	// StatFs stat = newStatFs(Environment.getExternalStorageDirectory()
	// .getPath());
	// double sdFreeMB = ((double)stat.getAvailableBlocks() * (double)
	// stat.getBlockSize()) / MB;
	// return (int) sdFreeMB;
	// }
	// /**
	// * 锟睫革拷锟侥硷拷锟斤拷锟斤拷锟斤拷薷锟绞憋拷锟?
	// * @param dir
	// * @param fileName
	// */
	// private void updateFileTime(String dir,String fileName) {
	// File file = new File(dir,fileName);
	// long newModifiedTime =System.currentTimeMillis();
	// file.setLastModified(newModifiedTime);
	// }
	// /**
	// *锟斤拷锟斤拷娲⒛柯硷拷碌锟斤拷募锟斤拷锟叫★拷锟斤拷锟斤拷募锟斤拷艽锟叫★拷锟斤拷诠娑拷锟紺ACHE_SIZE锟斤拷锟斤拷sdcard剩锟斤拷占锟叫★拷锟紽REE_SD_SPACE_NEEDED_TO_CACHE锟侥规定
	// * 锟斤拷么删锟斤拷40%锟斤拷锟矫伙拷斜锟绞癸拷玫锟斤拷募锟?
	// * @param dirPath
	// * @param filename
	// */
	// private void removeCache(String dirPath) {
	// File dir = new File(dirPath);
	// File[] files = dir.listFiles();
	// if (files == null) {
	// return;
	// }
	// int dirSize = 0;
	// for (int i = 0; i < files.length;i++) {
	// if(files[i].getName().contains(WHOLESALE_CONV)) {
	// dirSize += files[i].length();
	// }
	// }
	// if (dirSize > CACHE_SIZE * MB ||FREE_SD_SPACE_NEEDED_TO_CACHE >
	// freeSpaceOnSd()) {
	// int removeFactor = (int) ((0.4 *files.length) + 1);
	// 
	// Arrays.sort(files, newFileLastModifSort());
	// 
	// Log.i(TAG, "Clear some expiredcache files ");
	// 
	// for (int i = 0; i <removeFactor; i++) {
	// 
	// if(files[i].getName().contains(WHOLESALE_CONV)) {
	// 
	// files[i].delete();
	// 
	// }
	// 
	// }
	// 
	// }
	// 
	// }
	// /**
	// * 删锟斤拷锟斤拷锟斤拷募锟?
	// * @param dirPath
	// * @param filename
	// */
	// private void removeExpiredCache(String dirPath, String filename) {
	// 
	// File file = new File(dirPath,filename);
	// 
	// if (System.currentTimeMillis() -file.lastModified() > mTimeDiff) {
	// 
	// Log.i(TAG, "Clear some expiredcache files ");
	// 
	// file.delete();
	// 
	// }
	// 
	// }
	/**
	 * 锟接伙拷锟斤拷锟叫伙拷取图片
	 */
	public Bitmap getBitmapFromCache(String url) {
		// 锟饺达拷mHardBitmapCache锟斤拷锟斤拷锟叫伙拷取
		// synchronized (BMapApi.getImageCache()) {
		// final Bitmap bitmap =BMapApi.getImageCache().get(url);
		// if (bitmap != null) {
		// //锟斤拷锟斤拷业锟斤拷幕锟斤拷锟斤拷锟皆拷锟斤拷频锟絣inkedhashmap锟斤拷锟斤拷前锟芥，锟接讹拷证锟斤拷LRU锟姐法锟斤拷锟斤拷锟斤拷锟缴撅拷锟?
		// BMapApi.getImageCache().remove(url);
		// BMapApi.getImageCache().put(url,bitmap);
		// return bitmap;
		// }
		// }
		// 锟斤拷锟絤HardBitmapCache锟斤拷锟揭诧拷锟斤拷锟斤拷锟斤拷mSoftBitmapCache锟斤拷锟斤拷
		// SoftReference<Bitmap>bitmapReference = mSoftBitmapCache.get(url);
		// if (bitmapReference != null) {
		// final Bitmap bitmap =bitmapReference.get();
		// if (bitmap != null) {
		// return bitmap;
		// } else {
		// mSoftBitmapCache.remove(url);
		// }
		// }
		return null;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷图片 锟皆硷拷锟斤拷锟斤拷锟斤拷图片
	 * 
	 * @param httpurl
	 */
	// public static void cacheUrlImage(String httpurl){
	// try {
	// //First create a new URL object
	// URL url = new URL(httpurl);
	// //Next create a file, the example below will save to the SDCARD using
	// JPEG format
	// File file = new File("/sdcard/example.jpg");
	// //Next create a Bitmap object and download the image to bitmap
	// Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
	// //Finally compress the bitmap, saving to the file previously created
	// bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));
	// } catch (MalformedURLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// 锟斤拷锟斤拷锟竭程帮拷图片锟斤拷锟斤拷锟斤拷锟较碉拷锟斤拷锟斤拷锟皆猴拷锟斤拷锟饺伙拷锟斤拷锟斤拷锟斤拷锟斤拷锟侥硷拷锟斤拷
	// //android.content.ContextWrapper.getCacheDir()
	// android系统锟结供锟斤拷一锟斤拷锟斤拷藕偷玫锟斤拷锟斤拷锟斤拷募锟斤拷械姆锟斤拷锟?
	// 然锟斤拷锟斤拷募锟饺凤拷锟斤拷锟饺★拷锟阶褐帮拷锟斤拷锟斤拷萁锟斤拷锟組D5锟斤拷锟杰诧拷锟斤拷锟阶浩达拷锟揭伙拷锟斤拷募锟斤拷锟斤拷锟斤拷锟斤拷锟侥硷拷锟斤拷痛锟斤拷锟斤拷路锟斤拷锟斤拷锟斤拷一锟斤拷锟侥硷拷
	// 写一锟斤拷锟斤拷锟芥方锟斤拷
	// 锟叫讹拷锟角凤拷锟斤拷诨锟斤拷锟斤拷募锟?
	// 锟斤拷锟斤拷锟斤拷蚍祷丶锟斤拷芎锟斤拷募锟斤拷锟経ri锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟侥硷拷锟斤拷锟斤拷锟斤拷锟侥硷拷锟斤拷Uri.锟斤拷锟斤拷锟斤拷锟侥匡拷锟斤拷锟斤拷没锟斤拷锟斤拷锟侥硷拷锟斤拷锟斤拷Uri锟酵匡拷锟斤拷唯一锟斤拷识一锟斤拷锟侥硷拷锟斤拷
	// public static Uri cacheFile(String path,File saveDir)throws Exception{
	// File file = new File(saveDir, "");
	// //MD5.getMD5(path)+ path.substring(path.lastIndexOf('.')));
	// if(file.exists()){
	// return Uri.fromFile(file);
	// }else{
	// FileOutputStream outStream = new FileOutputStream(file);
	// HttpURLConnection conn = (HttpURLConnection)new
	// URL(path).openConnection();
	// conn.setConnectTimeout(5 * 1000);
	// conn.setRequestMethod("GET");
	// if(conn.getResponseCode()==200){
	// InputStream inStream = conn.getInputStream();
	// byte[] buffer = new byte[1024];
	// int len = 0;
	// while( (len = inStream.read(buffer)) !=-1 ){
	// outStream.write(buffer, 0, len);
	// }
	// outStream.close();
	// inStream.close();
	// return Uri.fromFile(file);
	// }else{
	// throw new Exception("锟侥硷拷锟斤拷锟斤拷失锟杰ｏ拷");
	// }
	// }
	// }

	/**
	 * 锟较达拷锟侥硷拷锟斤拷Server锟侥凤拷锟斤拷
	 */
	public static boolean uploadFile(String newName, String thisLarge) {
		boolean ref = false;
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {
			URL url = new URL(BMapApi.getInstance().getBaseUrl()
					+ "b.ashx?op=u");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 锟斤拷锟斤拷Input锟斤拷Output锟斤拷锟斤拷使锟斤拷Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 锟斤拷锟矫达拷锟酵碉拷method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			/* 锟斤拷锟斤拷DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"file1\";filename=\"" + newName + "\"" + end);
			ds.writeBytes(end);

			/* 取锟斤拷锟侥硷拷锟斤拷FileInputStream */
			FileInputStream fStream = new FileInputStream(thisLarge);
			/* 锟斤拷锟斤拷每锟斤拷写锟斤拷1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];

			int length = -1;
			/* 锟斤拷锟侥硷拷锟斤拷取锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟?*/
			while ((length = fStream.read(buffer)) != -1) {
				/* 锟斤拷锟斤拷锟斤拷写锟斤拷DataOutputStream锟斤拷 */
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

			/* close streams */
			fStream.close();
			ds.flush();

			/* 取锟斤拷Response锟斤拷锟斤拷 */
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			ref = true;
			/* 锟斤拷Response锟斤拷示锟斤拷Dialog */
			// showDialog("锟较达拷锟缴癸拷" + b.toString().trim());
			/* 锟截憋拷DataOutputStream */
			ds.close();
		} catch (Exception e) {
			// showDialog("锟较达拷失锟斤拷" + e);
			ref = false;
		}
		return ref;
	}

	// 锟斤拷锟斤拷锟斤拷谋锟斤拷锟斤拷募锟斤拷幕锟斤拷锟矫赐拷锟絤ap锟洁传锟捷斤拷锟斤拷锟斤拷锟斤拷锟斤拷募锟斤拷幕锟酵拷锟紽ormFile锟斤拷锟捷斤拷锟斤拷
	// public static String post(String actionUrl, Map<String, String> params,
	// FormFile[] files) throws IOException {
	//
	// String BOUNDARY = "743520vjdk4e";
	// String MULTIPART_FROM_DATA = "multipart/form-data";
	//
	// URL uri = new URL(actionUrl);
	// HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
	// conn.setReadTimeout(5 * 1000); // 锟斤拷锟斤拷锟斤拷畛な憋拷锟?
	// conn.setDoInput(true);// 锟斤拷锟斤拷锟斤拷锟斤拷
	// conn.setDoOutput(true);// 锟斤拷锟斤拷锟斤拷锟?
	// conn.setUseCaches(false); // 锟斤拷锟斤拷锟斤拷使锟矫伙拷锟斤拷
	// // 锟斤拷锟斤拷募锟斤拷锟街碉拷潜锟斤拷锟斤拷锟揭拷锟斤拷媒锟饺ワ拷锟?
	// conn.setRequestMethod("POST");
	// conn.setRequestProperty("connection", "keep-alive");
	// conn.setRequestProperty("Charsert", "UTF-8");
	// conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
	// + ":boundary" + BOUNDARY);
	//
	// // 锟斤拷锟斤拷锟斤拷拼锟侥憋拷锟斤拷锟酵的诧拷锟斤拷
	// StringBuilder sb = new StringBuilder();
	//
	// // 锟斤拷锟斤拷胤锟绞癸拷锟斤拷锟組ap循锟斤拷 map循锟斤拷锟侥凤拷式锟斤拷要注锟斤拷一锟斤拷锟斤拷
	// for (Map.Entry<String, String> entry : params.entrySet()) {
	// sb.append("--");
	// sb.append(BOUNDARY);
	// // 锟截筹拷锟斤拷锟斤拷
	// sb.append("\r\n");
	// sb.append("Content-Disposition:form-data:name\"" + entry.getKey()
	// + "\r\n\r\n");
	// sb.append(entry.getValue());
	// sb.append("\r\n");
	// }
	// DataOutputStream outStream = new DataOutputStream(conn
	// .getOutputStream());
	// outStream.write(sb.toString().getBytes());
	//
	// // 前锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟脚匡拷锟斤拷
	// // 锟斤拷锟斤拷锟侥硷拷锟斤拷锟?
	// for (FormFile file : files) {
	//
	// StringBuilder sb1 = new StringBuilder();
	// sb1.append("---");
	// sb1.append(BOUNDARY);
	// sb1.append("\r\n");
	// // 锟斤拷锟斤拷胤锟矫伙拷锟斤拷锟?
	// sb1.append("Content-Disposition:form-data:name=\""
	// + file.getFormnames());
	// sb1.append("Content-Type" + file.getContentType() + "\r\n\r\n");
	// outStream.write(sb1.toString().getBytes());
	//
	// // 锟斤拷锟叫讹拷formfile锟斤拷锟斤拷锟角凤拷为锟斤拷 锟斤拷锟轿拷盏幕锟斤拷锟叫达拷锟?锟斤拷取formfile锟斤拷data锟斤拷锟斤拷锟?
	// if (file.getInStream() != null) {
	// // 锟结供锟斤拷锟侥的凤拷式锟侥伙拷锟斤拷锟斤拷一锟竭讹拷一锟斤拷写锟斤拷
	// byte[] buffer = new byte[1024];
	// int len = 0;
	// while ((len = file.getInStream().read(buffer)) != -1) {
	// outStream.write(buffer, 0, len);
	// }
	// file.getInStream().close();
	// } else {
	// outStream.write(file.getData(), 0, file.getData().length);
	//
	// }
	// outStream.write("\r\n".getBytes());
	//
	// }
	// byte[] end_data = ("--" + BOUNDARY + "\r\n").getBytes();
	// outStream.write(end_data);
	// outStream.flush();
	//
	// // 锟矫碉拷锟斤拷应锟斤拷锟斤拷
	// int res = conn.getResponseCode();
	// if (res != 200)
	// throw new RuntimeException("锟斤拷锟斤拷失锟斤拷 ");
	// InputStream in = conn.getInputStream();
	// int ch;
	// StringBuilder sb2 = new StringBuilder();
	// while ((ch = in.read()) != -1) {
	// sb.append((char) ch);
	// }
	// outStream.close();
	// conn.disconnect();
	// return in.toString();
	// }
}

// class FormFile {
//
// // 锟斤拷锟斤拷锟斤拷使锟矫碉拷锟侥硷拷锟斤拷锟截碉拷
// // 锟较达拷锟侥硷拷锟斤拷锟斤拷锟?
// private byte[] data;
// private InputStream inStream;
// // 锟侥硷拷锟斤拷锟?
// private String fileName;
// // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
// private String Formnames;
// // 锟斤拷锟斤拷锟斤拷锟斤拷
// private String contentType = "application/octet-stream";
//
// public FormFile(byte[] data, String fileName, String formnames,
// String contentType) {
// this.data = data;
// this.fileName = fileName;
// Formnames = formnames;
// this.contentType = contentType;
// }
//
// public FormFile(InputStream inStream, String fileName, String formnames,
// String contentType) {
// this.inStream = inStream;
// this.fileName = fileName;
// Formnames = formnames;
// this.contentType = contentType;
// }
//
// public byte[] getData() {
// return data;
// }
//
// public void setData(byte[] data) {
// this.data = data;
// }
//
// public InputStream getInStream() {
// return inStream;
// }
//
// public void setInStream(InputStream inStream) {
// this.inStream = inStream;
// }
//
// public String getFileName() {
// return fileName;
// }
//
// public void setFileName(String fileName) {
// this.fileName = fileName;
// }
//
// public String getFormnames() {
// return Formnames;
// }
//
// public void setFormnames(String formnames) {
// Formnames = formnames;
// }
//
// public String getContentType() {
// return contentType;
// }
//
// public void setContentType(String contentType) {
// this.contentType = contentType;
// }
// }