package com.limon.make;

import java.io.File;
import java.util.Comparator;



import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;


/**
 * 实现一些基础方法
 * 
 * @author Administrator
 * 
 */
public class BaseActivity extends Activity {

	protected Activity instance;
	protected Context mContext;
	protected ExitListenerReceiver exitre;
	protected final String SDCARD_MNT = "/mnt/sdcard";
	protected final String SDCARD = "/sdcard";

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BMapApi app = (BMapApi) this.getApplication();
		app.addActivity(this);
		RegListener();
	}

	/**
	 * 注册退出事件监听
	 * 
	 */
	public void RegListener() {
		exitre = new ExitListenerReceiver();
		IntentFilter intentfilter = new IntentFilter();
		intentfilter.addAction(this.getPackageName() + "."
				+ "ExitListenerReceiver");
		this.registerReceiver(exitre, intentfilter);
	}

	class ExitListenerReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			((Activity) arg0).finish();
			unregisterReceiver(exitre);
		}
	}

	// 写一个广播的内部类，当收到动作时，结束activity
	// private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// Log.d("Close", "Base");
	// //close();
	// unregisterReceiver(this);
	// }
	//
	// };
	// private void close() {
	//    	
	// Intent intent = new Intent();
	// intent.setAction("ExitApp"); // 说明动作
	// sendBroadcast(intent);// 该函数用于发送广播
	// finish();
	// }
	@Override
	protected void onResume() {
		super.onResume();

		// 在当前的activity中注册广播
		// IntentFilter filter = new IntentFilter();
		// filter.addAction("ExitApp");
		// registerReceiver(this.broadcastReceiver, filter); // 注册
	}

	/**
	 * 通过uri获取文件的绝对路径
	 * 
	 * @param uri
	 * @return
	 */
	protected String getAbsoluteImagePath(Uri uri) {
		String imagePath = "";
		String[] proj = { MediaColumns.DATA };
		Cursor cursor = managedQuery(uri, proj, // Which columns to return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			if (cursor.getCount() > 0 && cursor.moveToFirst()) {
				imagePath = cursor.getString(column_index);
			}
		}

		return imagePath;
	}

	/**
	 * 获取图片缩略图 只有Android2.1以上版本支持
	 * 
	 * @param imgName
	 * @param kind
	 *            MediaStore.Images.Thumbnails.MICRO_KIND
	 * @return
	 */
	protected Bitmap loadImgThumbnail(String imgName, int kind) {
		Bitmap bitmap = null;

		String[] proj = { BaseColumns._ID, MediaColumns.DISPLAY_NAME };

		Cursor cursor = managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj,
				MediaColumns.DISPLAY_NAME + "='" + imgName + "'", null, null);

		if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
			ContentResolver crThumb = getContentResolver();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			bitmap = MediaStore.Images.Thumbnails.getThumbnail(crThumb, cursor
					.getInt(0), kind, options);
		}
		return bitmap;
	}

	/**
	 * 获取SD卡中最新图片路径
	 * 
	 * @return
	 */
	protected String getLatestImage() {
		String latestImage = null;
		String[] items = { BaseColumns._ID, MediaColumns.DATA };
		Cursor cursor = managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, items, null,
				null, BaseColumns._ID + " desc");

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (cursor.moveToFirst(); !cursor.isAfterLast();) {
				latestImage = cursor.getString(1);
				break;
			}
		}

		return latestImage;
	}

	// private void saveBmpToSd(Bitmap bm, String url) {
	// if (bm == null) {
	// Log.w("saveBmpToSd", " trying to savenull bitmap");
	// return;
	// }
	// //判断sdcard上的空间
	// if (FREE_SD_SPACE_NEEDED_TO_CACHE >freeSpaceOnSd()) {
	// Log.w("saveBmpToSd", "Low free space onsd, do not cache");
	// return;
	// }
	// String filename =convertUrlToFileName(url);
	// String dir = getDirectory(filename);
	// File file = new File(dir +"/" + filename);
	// try {
	// file.createNewFile();
	// OutputStream outStream = new FileOutputStream(file);
	// bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
	// outStream.flush();
	// outStream.close();
	// Log.i("saveBmpToSd", "Image saved tosd");
	// } catch (FileNotFoundException e) {
	// Log.w("saveBmpToSd","FileNotFoundException");
	// } catch (IOException e) {
	// Log.w("saveBmpToSd","IOException");
	// }
	// }
	// /**
	// * 计算sdcard上的剩余空间
	// * @return
	// */
	// private int freeSpaceOnSd() {
	// StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
	// .getPath());
	// double sdFreeMB = ((double)stat.getAvailableBlocks() * (double)
	// stat.getBlockSize()) / MB;
	// return (int) sdFreeMB;
	// }
	// /**
	// * 修改文件的最后修改时间
	// * @param dir
	// * @param fileName
	// */
	// private void updateFileTime(String dir,String fileName) {
	// File file = new File(dir,fileName);
	// long newModifiedTime =System.currentTimeMillis();
	// file.setLastModified(newModifiedTime);
	// }
	// /**
	// *计算存储目录下的文件大小，当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定
	// * 那么删除40%最近没有被使用的文件
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
	// Arrays.sort(files, new FileLastModifSort());
	//	 
	// Log.i("removeCache", "Clear some expiredcache files ");
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
	// * 删除过期文件
	// * @param dirPath
	// * @param filename
	// */
	// private void removeExpiredCache(String dirPath, String filename) {
	//	 
	// File file = new File(dirPath,filename);
	//	 
	// if (System.currentTimeMillis() -file.lastModified() > mTimeDiff) {
	//	 
	// Log.i("removeExpiredCache", "Clear some expiredcache files ");
	//	 
	// file.delete();
	//	 
	// }
	//	 
	// }
	/**
	 * TODO 根据文件的最后修改时间进行排序 *
	 */
	class FileLastModifSort implements Comparator<File> {
		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() > arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}

}