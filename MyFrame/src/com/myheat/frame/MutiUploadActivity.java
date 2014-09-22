package com.myheat.frame;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.Header;
import com.myheat.frame.http.AsyncHttpClient;
import com.myheat.frame.http.AsyncHttpResponseHandler;
import com.myheat.frame.http.RequestParams;
import com.myheat.frame.tool.DebugLog;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author myheat
 * @version 2014年9月17日下午6:00:42
 */
public class MutiUploadActivity extends Activity {
	String LOG_TAG = "MutiUploadActivity";

	String url = "http://www.4162.com/app/v1/uploadMutiFile.php";
	AsyncHttpClient client = new AsyncHttpClient();
	RequestParams params = new RequestParams();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.muti_upload);

		try {
			params.put("file1", createTempFile("demo1", getAssets().open("ic.png")));
			params.put("file2", createTempFile("demo2",  getAssets().open("ic.png")));
			params.put("file3", createTempFile("demo3",  getAssets().open("ic.png")));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		params.put("userId", 8888);
		params.put("num", 3);
		params.put("time", "123456");

		
		params.setHttpEntityIsRepeatable(true);
//		params.setUseJsonStreamer(false);
		
		((Button) findViewById(R.id.mutiUploadBtn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						client.post(MutiUploadActivity.this, url, params,
								responseHandler);
					}
				});

	}

	AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			DebugLog.d("onSuccess", "" + new String(responseBody));
		}

		
		
		@Override
		public void onProgress(int bytesWritten, int totalSize) {
			// TODO Auto-generated method stub
			super.onProgress(bytesWritten, totalSize);
			
			// Log.v(LOG_TAG, String.format("Progress %d from %d (%2.0f%%)", bytesWritten, totalSize, (totalSize > 0) ? (bytesWritten * 1.0 / totalSize) * 100 : -1));
		}



		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			DebugLog.d("onFailure", "" + error.toString() + "======"
					+ responseBody);
		}

	};

	public File createTempFile(String namePart, InputStream ins) {
		try {
			
			File f = File.createTempFile(namePart, ".png");// ,getCacheDir()
			FileOutputStream fos = new FileOutputStream(f);
			int bytesRead = 0;
			byte[] buffer = new byte[2048];
			while ((bytesRead = ins.read(buffer)) != -1) {
				fos.write(buffer, 0, bytesRead);
			}
			fos.flush();
			fos.close();
			return f;
		} catch (Throwable t) {
			Log.e(LOG_TAG, "createTempFile failed", t);
		}
		return null;
	}

}
