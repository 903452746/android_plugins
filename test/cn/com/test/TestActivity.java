package cn.com.test;


import cn.com.lowe.android.R;
import cn.com.lowe.android.tools.thread.exception.ThreadMehtodException;
import cn.com.lowe.android.widget.dialog.CustomDialog;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TestActivity extends Activity implements Callback{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_main);
	}

	/**
	 * ��ͨ�Ի���
	 * @param view
	 */
	public void showCustomDialog(View view) {
		CustomDialog dialog=new CustomDialog(this, R.layout.test_customdialog_content);
		dialog.show();
	}
	/**
	 * ȫ���Ի���
	 * @param view
	 */
	public void showCustomFullDialog(View view) {
		CustomDialog dialog=new CustomDialog(this, R.layout.test_customdialog_content);
		dialog.setFullScreen();
		dialog.show();
	}
	/**
	 * �����һ���
	 * @param view
	 */
	public void showCustomHalfDialog(View view) {
		CustomDialog dialog=new CustomDialog(this, R.layout.test_customdialog_content);
		dialog.setHalfScreen();
		dialog.show();
	}
	/**
	 * �����һ���
	 * @param view
	 */
	public void showCustom2Dialog(View view) {
		CustomDialog dialog=new CustomDialog(this, R.layout.test_customdialog_content);
		dialog.setSizePercent(CustomDialog.WRAP_CONTENT, CustomDialog.WRAP_CONTENT);
		dialog.setTitleIcon(android.R.drawable.ic_dialog_email);
		dialog.setTitleText("�ҵĶԻ���");
		dialog.setTitleBgColor("#FFFF00");
		dialog.setTitleTextColor("#000000");
		dialog.setContentBgColor(Color.WHITE);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}
	/**
	 * �����һ���
	 * @param view
	 */
	public void showCustomListenerDialog(View view) {
		CustomDialog dialog=new CustomDialog(this, new CustomDialog.DialogCreateListener() {
			
			@Override
			public void onCreate(LinearLayout contentRootView) {
				View view =LayoutInflater.from(TestActivity.this).inflate(R.layout.test_customdialog_content, null);
				
				contentRootView.addView(view);
			}
		});
		dialog.setHalfScreen();
		dialog.show();
	}
	/**
	 * �����һ���
	 * @param view
	 */
	public void testBean(View view) {
		Intent intent=new Intent(this, BeanActivity.class);
		this.startActivity(intent);
	}
	private DemoThread thread;
	public void testThread(View view){
		thread=new DemoThread(this);
		try {
			thread.execute("saveInfo", new Object[]{new String("dddd"),null});
			//thread.execute("saveInfo", new Object[]{new String("1111"),null});
			//thread.interrupt();
		} catch (ThreadMehtodException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case 1:
			Toast.makeText(this, ""+msg.obj, Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
		return false;
	}
	
	
}
