package com.limon.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.widget.ImageView;

import com.limon.make.BMapApi;

public class AsyncImageLoader {
	/**
	 * 内存图片软引用缓冲
	 */
	// private HashMap<String, SoftReference<Bitmap>> imageCache = null;
	private String folderPath = "";
	private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 2;
	private static final int MB = 1024;

	// private static final int CACHE_SIZE = 5;
	public AsyncImageLoader() {
		// imageCache = new HashMap<String, SoftReference<Bitmap>>();
		folderPath = FileUtils.sdPath("HPStreets");
	}

	// @SuppressWarnings("unchecked")
	public Drawable loadDrawable(final String imageURL,
			final ImageView imageView, final ImageCallBack imageCallBack) {
		// 在内存缓存中，则返回Bitmap对象
		// if(imageCache.containsKey(imageURL))
		// Iterator iter =
		// BMapApi.getInstance().getImageCache().entrySet().iterator();
		// Map.Entry entry;
		// //Log.d("Value",(String) iter...getValue());
		// while (iter.hasNext()) {
		// entry = (Map.Entry) iter.next();
		// Log.d("Key",(String)entry.getKey());
		// //Log.d("Value",(String) entry.getValue());
		// }
		if (BMapApi.getInstance().getImageCache().isCached(imageURL)) {
			SoftReference<Drawable> reference = BMapApi.getInstance()
					.getImageCache().get(imageURL);
			// Bitmap bitmap = reference.get();
			// if(bitmap != null)
			// {
			// Log.d("c","="+bitmap.getHeight());
			// return bitmap;
			// }
			Drawable drawable = reference.get();
			if (drawable != null) {
				// Log.d("缓存","="+drawable.getMinimumHeight());

				return drawable;
			}
		} else if (!"".equals(folderPath)) {
			/**
			 * 加上一个对本地缓存的查找
			 */
			String bitmapName = imageURL
					.substring(imageURL.lastIndexOf("/") + 1);
			// Log.d("bitmapName",bitmapName);
			File cacheDir = new File(folderPath);
			// if(!cacheDir.exists())
			// {
			// cacheDir.mkdirs();
			// }
			File[] cacheFiles = cacheDir.listFiles();

			if (cacheFiles != null) {

				int i = 0;
				for (; i < cacheFiles.length; i++) {
					if (bitmapName.equals(cacheFiles[i].getName())) {
						break;
					}
				}

				if (i < cacheFiles.length) {
					// Log.d("读卡",folderPath + bitmapName);
					Drawable d = Drawable.createFromPath(folderPath
							+ bitmapName);
					// BMapApi.getInstance().getImageCache().put(imageURL, new
					// SoftReference<Drawable>(d));
					return d;
					// return BitmapFactory.decodeFile(folderPath + bitmapName);
				}
			}
		}

		final Handler handler = new Handler() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see android.os.Handler#handleMessage(android.os.Message)
			 */
			@Override
			public void handleMessage(Message msg) {
				Drawable bp = (Drawable) msg.obj;
				// Bitmap bp = (Bitmap)msg.obj;
				// Log.d("回调","="+bp.getMinimumHeight());
				imageCallBack.imageLoad(bp, imageView, imageURL);
			}
		};

		// 如果不在内存缓存中，也不在本地（被jvm回收掉），则开启线程下载图片
		new Thread() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Thread#run()
			 */
			@Override
			public void run() {
				// TODO Auto-generated method stub
				InputStream bitmapIs = getStreamFromURL(imageURL);
				Bitmap bitmap = BitmapFactory.decodeStream(bitmapIs);
				// Bitmap bitmap = BitmapFactory.decodeStream(new
				// FlushedInputStream(bitmapIs));
				// imageCache.put(imageURL, new
				// SoftReference<Drawable>(Drawable.createFromStream(bitmapIs)));
				// if(bitmap!=null){
				Drawable d = new BitmapDrawable(bitmap);
				// }
				// try{
				// d = Drawable.createFromStream(bitmapIs, "src");
				// //d= new BitmapDrawable(bitmap);
				// Log.d("bp1","="+d.getMinimumHeight());
				// }catch(OutOfMemoryError outOfMemoryError){
				// d= new BitmapDrawable(bitmap);
				// Log.d("bp2","="+d.getMinimumHeight());
				// }
				// catch(Exception e){
				// d= new BitmapDrawable(bitmap);
				// Log.d("bp3","="+d.getMinimumHeight());
				// }
				BMapApi.getInstance().getImageCache().put(imageURL,
						new SoftReference<Drawable>(d));
				Message msg = handler.obtainMessage(0, d);
				handler.sendMessage(msg);
				if (FREE_SD_SPACE_NEEDED_TO_CACHE < freeSpaceOnSd()) {

					File dir = new File(folderPath);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					// Log.d("cacheFiles.length","=");
					File bitmapFile = new File(folderPath
							+ imageURL.substring(imageURL.lastIndexOf("/") + 1));
					if (!bitmapFile.exists()) {
						try {
							bitmapFile.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					FileOutputStream fos;
					try {
						fos = new FileOutputStream(bitmapFile);
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
						fos.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();

		return null;
	}

	public static Drawable loadImageFromUrl(String url) {
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			// Log.i("imageUrl", "imageUrl"+url);
			i = (InputStream) m.getContent();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Drawable d = Drawable.createFromStream(i, "src");
		return d;
	}

	public static InputStream getStreamFromURL(String url) {
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			// Log.i("下载", url);
			i = (InputStream) m.getContent();
		} catch (MalformedURLException e1) {
			Log.i("MalformedURLException", e1.toString());
		} catch (IOException e) {
			Log.i("IOException", e.toString());
		}

		// Drawable d = Drawable.createFromStream(i, "src");
		return i;
	}

	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

	/**
	 * 回调接口
	 * 
	 * @author onerain
	 * 
	 */
	public interface ImageCallBack {
		// public void imageLoad(Drawable bitmap,String imageUrl);
		public void imageLoad(Drawable bitmap, ImageView imageView,
				String imageUrl);
	}

	private int freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize())
				/ MB;
		return (int) sdFreeMB;
	}
}
