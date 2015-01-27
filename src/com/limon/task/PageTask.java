package com.limon.task;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

public class PageTask extends AsyncTask<String, Integer, String> {
	// 可变长的输入参数，与AsyncTask.exucute()对应
	ProgressDialog pdialog;

	public PageTask(Context context) {
		pdialog = new ProgressDialog(context, 0);
		pdialog.setButton("cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int i) {
				dialog.cancel();
			}
		});
		pdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				// finish();
			}
		});
		pdialog.setCancelable(true);
		pdialog.setMax(100);
		pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pdialog.show();

	}

	@Override
	protected String doInBackground(String... params) {

		try {

			HttpClient client = new DefaultHttpClient();
			// params[0]代表连接的url
			HttpGet get = new HttpGet(params[0]);
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			long length = entity.getContentLength();
			InputStream is = entity.getContent();
			String s = null;
			if (is != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				byte[] buf = new byte[128];

				int ch = -1;

				int count = 0;

				while ((ch = is.read(buf)) != -1) {

					baos.write(buf, 0, ch);

					count += ch;

					if (length > 0) {
						// 如果知道响应的长度，调用publishProgress（）更新进度
						publishProgress((int) ((count / (float) length) * 100));
					}

					// 让线程休眠100ms
					Thread.sleep(100);
				}
				s = new String(baos.toByteArray());
			}
			// 返回结果
			return s;
		} catch (Exception e) {
			e.printStackTrace();

		}

		return null;

	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(String result) {
		// 返回HTML页面的内容
		// message.setText(result);
		pdialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		// 任务启动，可以在这里显示一个对话框，这里简单处理
		// message.setText(R.string.task_started);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// 更新进度
		// System.out.println(""+values[0]);
		// message.setText(""+values[0]);
		pdialog.setProgress(values[0]);
	}

}
