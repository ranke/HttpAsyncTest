package com.example.httpasynctest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.httpaynctest.R;
import com.util.http.MyImageDownLoader;
import com.util.http.MyImageDownLoader.DownLoaderListener;
import com.util.http.MyUploader;

public class HttpTestActivity extends Activity {
	
	private static final String TAG = "HttpTestActivity";
	private ImageView mImageView;
	private Button btnDownload;
	private Button btnUpload;
	
	private Context mContext;
	
	//
	private static String URL_PHOTO = "http://img001.us.expono.com/100001/100001-1bc30-2d736f_m.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_test);
        mContext = this;
        
        btnDownload = (Button)findViewById(R.id.button_download_test);
        
        btnUpload = (Button)findViewById(R.id.button_upload_test);
        
        mImageView = (ImageView) findViewById(R.id.imageview_download);
        
        setOnclickListener();
        
        
    }
    
    @Override
    protected void onResume() {
    	super.onResume();

    	
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.http_test, menu);
        return true;
    }
    
    private void setOnclickListener() {
    	btnDownload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
		    	String fileName = "" + System.currentTimeMillis() + ".jpg";
		    	String savePath = mContext.getCacheDir() + File.separator + fileName;
		    	downloadTest(URL_PHOTO, savePath);
				
			}
		});
    	
    	btnUpload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				uploadThreadTest();
				
			}
		});
    }
    
    private void downloadTest(String url, final String savePath) {
    	MyImageDownLoader mMyImageDownLoader = new MyImageDownLoader();
    	// 开始异步下载
		mMyImageDownLoader.downloadImage(
				url,
				savePath,
				new DownLoaderListener() {

					@Override
					public void onResult(int res, String s) {
						Log.i(TAG, "onResult====res===" + res);
						Log.i(TAG, "onResult====s===" + s);

						if (res == 0) {
							// 下载成功
							//setimage
							Toast.makeText(mContext, "download success!", Toast.LENGTH_LONG).show();
							setImageView(s);
							//LoginActivity.this.finish();
						} else {
							// 下载photo失败
							//LoginActivity.this.finish();
							Log.i(TAG, "onNotify====login fail!!======mLogin");
							Toast.makeText(mContext, "download fail!", Toast.LENGTH_LONG).show();
							
						}
					}
				});
    }
    
    private void setImageView(String imagePath) {
    	Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
		if (bitmap == null) {
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.empty_photo);
			
		}else {
			mImageView.setImageBitmap(bitmap); 
		}
    }
    
    ////////////case2:
    
	public void uploadThreadTest() {
		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					upload();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();

	}
    
    private void upload() {
    	String url = "https://httpbin.org/post";
    	List<String> fileList = getCacheFiles();
    	
    	if (fileList == null) {
    		myHandler.sendEmptyMessage(-1);
    	}else {
    		MyUploader myUpload = new MyUploader();
    		//同步请求，直接返回结果，根据结果来判断是否成功。
    		String reulstCode = myUpload.MyUploadMultiFileSync(url, fileList, null);
    		Log.i(TAG, "upload reulstCode: " + reulstCode);
    		myHandler.sendEmptyMessage(0);

    	}
    	
    }
    
    private List<String> getCacheFiles() {
    	List<String> fileList = new ArrayList<String>();
    	File catchPath = mContext.getCacheDir();
    	
    	if (catchPath!=null && catchPath.isDirectory()) {
    		
    		File[] files = catchPath.listFiles();
    		if (files == null || files.length<1) {
    			return null;
    		}
    		for (int i = 0; i < files.length; i++) {
    			if (files[i].isFile() && files[i].getAbsolutePath().endsWith(".jpg")) {
    				fileList.add(files[i].getAbsolutePath());
    			}
    			
    		}
    		return fileList;

    	}
    	return null;
    	
    	
    }
    ////////////handler/////
    private Handler myHandler = new Handler() {
    	@Override
		public void handleMessage(Message msg) {
			Log.i(TAG,"handleMessage msg===" + msg);
			if (msg.what == -1) {
				Toast.makeText(mContext, "not find file!", Toast.LENGTH_LONG).show();
				return;
			}else {
				Toast.makeText(mContext, "upload success!", Toast.LENGTH_LONG).show();
			}
			
    	}
    	
    };

    
}
