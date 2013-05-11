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
	public void showCustomFullScreenDialog(View view) {
		CustomDialog dialog=new CustomDialog(this, R.layout.test_customdialog_content);
		dialog.setFullScreen();
		dialog.show();
	}
	/**
	 * �����һ���
	 * @param view
	 */
	public void showCustomHalfScreenDialog(View view) {
		CustomDialog dialog=new CustomDialog(this, R.layout.test_customdialog_content);
		dialog.setHalfScreen();
		dialog.show();
	}
}
