package cn.com.test;

import java.lang.reflect.Field;

import cn.com.lowe.android.R;
import cn.com.lowe.android.tools.thead.ThreadTestActivity;
import cn.com.lowe.android.view.ViewTestActivity;
import cn.com.lowe.android.widget.camera.CameraActivity;
import cn.com.lowe.android.widget.dialog.CustomDialog;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TestActivity extends Activity implements Callback {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_main);
	}

	/**
	 * 普通对话框
	 * 
	 * @param view
	 */
	public void showCustomDialog(View view) {
		CustomDialog dialog = new CustomDialog(this, R.layout.test_customdialog_content);
		dialog.show();
	}

	/**
	 * 全屏对话框
	 * 
	 * @param view
	 */
	public void showCustomFullDialog(View view) {
		CustomDialog dialog = new CustomDialog(this, R.layout.test_customdialog_content);
		dialog.setFullScreen();
		dialog.show();
	}

	/**
	 * 半屏兑换框
	 * 
	 * @param view
	 */
	public void showCustomHalfDialog(View view) {
		CustomDialog dialog = new CustomDialog(this, R.layout.test_customdialog_content);
		dialog.setHalfScreen();
		dialog.show();
	}

	/**
	 * 半屏兑换框
	 * 
	 * @param view
	 */
	public void showCustom2Dialog(View view) {
		CustomDialog dialog = new CustomDialog(this, R.layout.test_customdialog_content);
		dialog.setSizePercent(CustomDialog.WRAP_CONTENT, CustomDialog.WRAP_CONTENT);
		dialog.setTitleIcon(android.R.drawable.ic_dialog_email);
		dialog.setTitleText("我的对话框");
		dialog.setTitleBgColor("#FFFF00");
		dialog.setTitleTextColor("#000000");
		dialog.setContentBgColor(Color.WHITE);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	/**
	 * 半屏兑换框
	 * 
	 * @param view
	 */
	public void showCustomListenerDialog(View view) {
		CustomDialog dialog = new CustomDialog(this, new CustomDialog.DialogCreateListener() {

			@Override
			public void onCreate(LinearLayout contentRootView) {
				View view = LayoutInflater.from(TestActivity.this).inflate(R.layout.test_customdialog_content, null);

				contentRootView.addView(view);
			}
		});
		dialog.setHalfScreen();
		dialog.show();
	}

	/**
	 * 半屏兑换框
	 * 
	 * @param view
	 */
	public void testBean(View view) {
		Intent intent = new Intent(this, BeanActivity.class);
		this.startActivity(intent);
	}

	private DemoThread thread;

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case 1:
			Toast.makeText(this, "" + msg.obj, Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
		return false;
	}

	/**
	 * 半屏兑换框
	 * 
	 * @param view
	 */
	public void testInjectActivity(View view) {
		Intent intent = new Intent(this, TestInjectActivity.class);
		this.startActivity(intent);
	}

	public void testThread(View view) {
		Intent intent = new Intent(this, ThreadTestActivity.class);
		this.startActivity(intent);
	}
	public void getSysFile(View view) {
		Log.d("", "扩展文件路径:"+Environment.getExternalStorageDirectory().getAbsolutePath());
		Log.d("", "应用文件路径:"+this.getFilesDir().getAbsolutePath());
		Build bd=new Build();
	    Field[] fields=Build.class.getFields();
	    for(Field field:fields){
	    	field.setAccessible(true);
	    	try {
				Log.d("", field.getName()+":"+field.get(bd));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
	    }
	    
	   String clazz="cn.com.lowe.android.tools.dataprocess.util.ValueUtil";
	   try {
		Class  clazz1=Class.forName(clazz);
		   Class  clazz2=Class.forName(clazz);
		   Log.d("", "类是否相同:"+(clazz1==clazz2));
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	public void testView(View view){
		Intent intent = new Intent(this, ViewTestActivity.class);
		this.startActivity(intent);
	}
	
	public void takePhoto(View view){
		Intent intent = new Intent(this, CameraActivity.class);
		this.startActivity(intent);
	}
}
