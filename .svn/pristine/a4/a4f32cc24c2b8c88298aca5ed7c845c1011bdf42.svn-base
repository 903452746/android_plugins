package cn.com.lowe.android.tools.thead;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import cn.com.lowe.android.tools.thread.InterruptThread;
import cn.com.lowe.android.tools.thread.InvokeId;

public class ThreadDemo extends InterruptThread {
	public static final int changeProgressThread = 1;
	public static final int threadrun = 2;
	public static final int threadspeciallrun = 3;
	private static Integer tag = new Integer("1");

	public ThreadDemo(Context context) {
		super(context);
	}

	@InvokeId(threadspeciallrun)
	public void threadspeciallrun(String param1) {
		uiHandler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, "线程开始，将执行4s", Toast.LENGTH_SHORT).show();
			}
		});
		Log.d(TAG, "计算开始时间：" + new Date().getTime());
		for (int i = 0; i < 100000000; i++) {
			for (int j = 0; j < 7; j++) {
				int k=0;
			}
		}
		Log.d(TAG, "计算结束时间：" + new Date().getTime());
		System.out.print(param1);
		uiHandler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, "线程结束", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@InvokeId(threadrun)
	public void threadrun(String param1) {
		uiHandler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, "线程开始，将执行4s", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(context, "线程结束", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@InvokeId(changeProgressThread)
	public void changeProgressThread() {
		uiHandler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, "线程开始，将执行7s", Toast.LENGTH_SHORT).show();
			}
		});
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		updateDialogMsg("我修改了进度栏的内容了");
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		uiHandler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, "线程结束", Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void testUnCancleThread() {
		updateDialogMsg("我将进度栏设置成不可取消，不信你点返回试试");
		uiHandler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, "线程开始，将执行7s", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(context, "线程结束", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
