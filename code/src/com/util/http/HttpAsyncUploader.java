package com.util.http;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * 异步上传单个文件
 */
public class HttpAsyncUploader {
	private static final String TAG = "HttpAsyncUploader";

	Context mContext;

	public HttpAsyncUploader() {
	}

	public void uploadFile(String uri, String path) {

		File myFile = new File(path);
		RequestParams params = new RequestParams();

		try {
			params.put("image", myFile, "application/octet-stream");

			AsyncHttpClient client = new AsyncHttpClient();

			HttpClientUtil.post(path, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					Log.i(TAG, " statusCode=========" + statusCode);
					Log.i(TAG, " statusCode=========" + headers);
					Log.i(TAG, " statusCode====binaryData len====="+ responseBody.length);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					Log.i(TAG, " statusCode=========" + statusCode);
					Log.i(TAG, " statusCode=========" + headers);
					Log.i(TAG, " statusCode====binaryData len====="+ responseBody.length);
					Log.i(TAG," statusCode====error====="+ error.getLocalizedMessage());

				}

			});

		} catch (FileNotFoundException e) {

		}
	}
}