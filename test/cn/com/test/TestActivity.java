package cn.com.test;

import cn.com.lowe.android.R;
import cn.com.lowe.android.widget.dialog.CustomDialog;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_main);
	}

	/**
	 * ÆÕÍ¨¶Ô»°¿ò
	 * @param view
	 */
	public void showCustomDialog(View view) {
		CustomDialog dialog=new CustomDialog(this, R.layout.test_customdialog_content);
		dialog.show();
	}
	/**
	 * È«ÆÁ¶Ô»°¿ò
	 * @param view
	 */
	public void showCustomFullDialog(View view) {
		CustomDialog dialog=new CustomDialog(this, R.layout.test_customdialog_content);
		dialog.setFullScreen();
		dialog.show();
	}
	/**
	 * °ëÆÁ¶Ò»»¿ò
	 * @param view
	 */
	public void showCustomHalfDialog(View view) {
		CustomDialog dialog=new CustomDialog(this, R.layout.test_customdialog_content);
		dialog.setHalfScreen();
		dialog.show();
	}
	/**
	 * °ëÆÁ¶Ò»»¿ò
	 * @param view
	 */
	public void showCustom2Dialog(View view) {
		CustomDialog dialog=new CustomDialog(this, R.layout.test_customdialog_content);
		dialog.setSizePercent(CustomDialog.WRAP_CONTENT, CustomDialog.WRAP_CONTENT);
		dialog.setTitleIcon(android.R.drawable.ic_dialog_email);
		dialog.setTitleText("ÎÒµÄ¶Ô»°¿ò");
		dialog.setTitleBgColor("#FFFF00");
		dialog.setTitleTextColor("#000000");
		dialog.setContentBgColor(Color.WHITE);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}
	/**
	 * °ëÆÁ¶Ò»»¿ò
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
}
