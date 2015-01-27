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
	// �ɱ䳤�������������AsyncTask.exucute()��Ӧ
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
			// params[0]�������ӵ�url
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
						// ���֪����Ӧ�ĳ��ȣ�����publishProgress�������½���
						publishProgress((int) ((count / (float) length) * 100));
					}

					// ���߳�����100ms
					Thread.sleep(100);
				}
				s = new String(baos.toByteArray());
			}
			// ���ؽ��
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
		// ����HTMLҳ�������
		// message.setText(result);
		pdialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		// ����������������������ʾһ���Ի�������򵥴���
		// message.setText(R.string.task_started);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// ���½���
		// System.out.println(""+values[0]);
		// message.setText(""+values[0]);
		pdialog.setProgress(values[0]);
	}

}
