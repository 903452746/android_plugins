package cn.com.lowe.android.app;

import cn.com.lowe.android.app.util.InjectTools;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class InjectV4Activity extends FragmentActivity {
	private static final String TAG = "InjectV4FragmentActivity";
	private Class<?> childClass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		childClass = this.getClass();
		Log.d(TAG, childClass.getName() + "-->onCreate");
		InjectApplication.addActivity(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, childClass.getName() + "-->onStart");
		InjectApplication.setCurrentActivity(this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, childClass.getName() + "-->onRestart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, childClass.getName() + "-->onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, childClass.getName() + "-->onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, childClass.getName() + "-->onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, childClass.getName() + "-->onDestroy");
		InjectApplication.removeActivity(this);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		Log.d(TAG, childClass.getName()+" do inject action");
		InjectTools.doActivityInjectWork(InjectV4Activity.this, childClass);
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		Log.d(TAG, childClass.getName()+" do inject action");
		InjectTools.doActivityInjectWork(InjectV4Activity.this, childClass);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		Log.d(TAG, childClass.getName()+" do inject action");
		InjectTools.doActivityInjectWork(InjectV4Activity.this, childClass);
	}
}
