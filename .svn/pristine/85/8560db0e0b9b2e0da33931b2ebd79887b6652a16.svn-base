package cn.com.test;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import cn.com.lowe.android.tools.thread.InterruptThread;

public class DemoThread extends InterruptThread {
	
	public DemoThread(Context context) {
		super(context);
	}

	public void saveInfo(String name,Boolean jo){
		System.out.println(name);
		if(stoped){
			return;
		}
		System.out.println(jo);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("睡眠结束");
		updateDialog("xxxxx",false);
		uiHandler.post(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(context, "dddddd", Toast.LENGTH_LONG).show();
				
			}
		});
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		uiHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Log.d(TAG, "执行延迟操作");
				Toast.makeText(context, "执行延迟操作", Toast.LENGTH_LONG).show();
				
			}
		},2000);
		uiHandler.obtainMessage(1, "通过消息发送").sendToTarget();
		
	}
}
