package cn.com.lowe.android.tools.thead;

import android.content.Context;
import android.widget.Toast;
import cn.com.lowe.android.tools.thread.InterruptThread;
import cn.com.lowe.android.tools.thread.InvokeId;

public class ThreadDemo extends InterruptThread {
	public static final int changeProgressThread=1;
	public static final int threadrun=2;
	public ThreadDemo(Context context) {
		super(context);
	}
	@InvokeId(threadrun)
	public void threadrun(String param1){
		uiHandler.post(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(context, "�߳̿�ʼ����ִ��4s", Toast.LENGTH_SHORT).show();
			}
		});
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		uiHandler.post(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(context, "�߳̽���", Toast.LENGTH_SHORT).show();
			}
		});
	}
	@InvokeId(changeProgressThread)
	public void changeProgressThread(){
		uiHandler.post(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(context, "�߳̿�ʼ����ִ��7s", Toast.LENGTH_SHORT).show();
			}
		});
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		updateDialogMsg("���޸��˽�������������");
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		uiHandler.post(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(context, "�߳̽���", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void testUnCancleThread(){
		updateDialogMsg("�ҽ����������óɲ���ȡ����������㷵������");
		uiHandler.post(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(context, "�߳̿�ʼ����ִ��7s", Toast.LENGTH_SHORT).show();
			}
		});
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		uiHandler.post(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(context, "�߳̽���", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
