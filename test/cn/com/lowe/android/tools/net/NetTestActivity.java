package cn.com.lowe.android.tools.net;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;
import cn.com.lowe.android.R;
import cn.com.lowe.android.app.InjectActivity;
import cn.com.lowe.android.tools.net.response.HtmlSyncResponseHandler;
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
    public void post(View view){
    	
    }
    public void get(View view){
    	new HttThread(this).execute("get");
//    	SyncHttpClient client=new SyncHttpClient();
//    	final HtmlSyncResponseHandler responsehandler=new HtmlSyncResponseHandler();
//    	client.get("http://www.baidu.com/", responsehandler);
//    	Toast.makeText(this, responsehandler.content, Toast.LENGTH_LONG).show();
    }
    public void postFile(View view){
    	
    }
    
    class HttThread extends InterruptThread{

		public HttThread(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
    	
		public void get(){
			SyncHttpClient client=new SyncHttpClient();
	    	final HtmlSyncResponseHandler responsehandler=new HtmlSyncResponseHandler();
	    	client.get("http://www.baidu.com/", responsehandler);
	    	uiHandler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(context, responsehandler.content, Toast.LENGTH_LONG).show();
				}
			});
	    	
		}
    }
}
