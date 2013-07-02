package cn.com.lowe.android.tools.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.http.Header;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;
import cn.com.lowe.android.R;
import cn.com.lowe.android.app.InjectActivity;
import cn.com.lowe.android.tools.net.response.BinaryHttpResponseHandler;
import cn.com.lowe.android.tools.net.response.JsonSyncResponseHandler;
import cn.com.lowe.android.tools.thread.InterruptThread;

public class NetTestActivity extends InjectActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_net);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}

	public void post(View view) {
		new HttThread(this).execute("post");
	}

	public void get(View view) {
		new HttThread(this).execute("get");
	}

	public void postFile(View view) {
		new HttThread(this).execute("postFile");
	}

	public void downFile(View view) {
		new HttThread(this).execute("downloadFile");
		
	}

	class HttThread extends InterruptThread {

		public HttThread(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public void get() {
			SyncHttpClient client = new SyncHttpClient();
			RequestParams params = new RequestParams();
			params.setENCODING("GBK");
			params.put("param1", "参数1");
			params.put("param2", "参数2");
			final JsonSyncResponseHandler responsehandler = new JsonSyncResponseHandler();
			client.get("http://192.168.30.211:80/WebServer/get.jsp", params, responsehandler);
			uiHandler.post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, responsehandler.jsonObject == null ? "" : responsehandler.jsonObject.toString(), Toast.LENGTH_LONG).show();
				}
			});

		}

		public void post() {
			SyncHttpClient client = new SyncHttpClient();
			RequestParams params = new RequestParams();
			params.setENCODING("GBK");
			params.put("param1", "参数1");
			params.put("param2", "参数2");
			final JsonSyncResponseHandler responsehandler = new JsonSyncResponseHandler();
			// client.post("http://192.168.30.126:80/WebServer/request.jsp",
			// params, responsehandler);
			client.post("http://192.168.30.211:80/WebServer/request.jsp", params, responsehandler);
			uiHandler.post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, responsehandler.jsonObject == null ? "" : responsehandler.jsonObject.toString(), Toast.LENGTH_LONG).show();
				}
			});

		}

		public void postFile() throws FileNotFoundException {

			String folderName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DCIM" + File.separator + "Camera";
			File folder = new File(folderName);
			File file = new File(folder, "中162331.jpg");
			if (file.exists()) {
				SyncHttpClient client = new SyncHttpClient();
				RequestParams params = new RequestParams();
				params.put("param1", "参数1");
				params.put("param2", "参数2");
				params.put("file", file);
				final JsonSyncResponseHandler responsehandler = new JsonSyncResponseHandler();
				client.post("http://192.168.30.211:80/WebServer/postFile.jsp", params, responsehandler);
				uiHandler.post(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(context, responsehandler.jsonObject == null ? "" : responsehandler.jsonObject.toString(), Toast.LENGTH_LONG).show();
					}
				});
			}

		}
		public void downloadFile() throws FileNotFoundException {

			String folderName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DCIM" + File.separator + "Camera";
			final File folder = new File(folderName);
			
			
				SyncHttpClient client = new SyncHttpClient();
				RequestParams params = new RequestParams();
				params.put("param1", "参数1");
				params.put("param2", "参数2");

				client.post("http://192.168.30.211:80/WebServer/downloadFile.jsp", params, new BinaryHttpResponseHandler(){

					@Override
					protected void onSuccess(int statusCode, Header[] headers, byte[] content) {
						File file = new File(folder, new Date().getTime()+".jpg");
						if (file.exists()) {
							try {
								file.createNewFile();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						FileOutputStream fos = null;
						try {
							fos = new FileOutputStream(file);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							fos.write(content);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				});
			

		}
	}
}
