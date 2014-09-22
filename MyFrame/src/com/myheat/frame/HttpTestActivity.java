package com.myheat.frame;

import java.io.IOException;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import com.myheat.frame.http.AsyncHttpClient;
import com.myheat.frame.http.AsyncHttpResponseHandler;
import com.myheat.frame.http.RequestParams;
import com.myheat.frame.http.ResponseHandlerInterface;
import com.myheat.frame.tool.DebugLog;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * @author myheat
 * @version 2014年9月17日下午5:30:55
 */
public class HttpTestActivity extends Activity {

    final static String LOG_TAG = "HttpTestActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		String url = "http://www.4162.com/app/v1/login.php";
		
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("uname", "myheat");
		params.put("pwd", "123456");
		
		asyncHttpClient.get(url, params, asyncHttpResponseHandler);
		 
	}

	
	AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			
			DebugLog.d("onSuccess", "" + new String(responseBody));
			
		}
		
		@Override
		public void onProgress(int bytesWritten, int totalSize) {
			// TODO Auto-generated method stub
			super.onProgress(bytesWritten, totalSize);
			
			 Log.v(LOG_TAG, String.format("Progress %d from %d (%2.0f%%)", bytesWritten, totalSize, (totalSize > 0) ? (bytesWritten * 1.0 / totalSize) * 100 : -1));
		}



		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			
			DebugLog.d("onFailure", "" + new String(responseBody));
		}
	};
	
}
