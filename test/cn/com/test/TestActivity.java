package cn.com.test;

import cn.com.lowe.android.R;
import cn.com.lowe.android.widget.dialog.CustomDialog;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_main);
	}

	/**
	 * 普通对话框
	 * @param view
	 */
	public void showCustomDialog(View view) {
		CustomDialog dialog=new CustomDialog(this, R.layout.test_customdialog_content);
		dialog.show();
	}
	/**
	 * 全屏对话框
	 * @param view
	 */
	public void showCustomFullScreenDialog(View view) {
		CustomDialog dialog=new CustomDialog(this, R.layout.test_customdialog_content);
		dialog.setFullScreen();
		dialog.show();
	}
	/**
	 * 半屏兑换框
	 * @param view
	 */
	public void showCustomHalfScreenDialog(View view) {
		CustomDialog dialog=new CustomDialog(this, R.layout.test_customdialog_content);
		dialog.setHalfScreen();
		dialog.show();
	}
}
