package cn.com.lowe.android.app;

import cn.com.lowe.android.app.util.InjectTools;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class InjectFragment extends Fragment {
	private static final String TAG = "InjectFragment";
	private Class<?> childClass;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		childClass = this.getClass();
		Log.d(TAG, childClass + "-->onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, childClass + "-->onCreate");
	}

	public void onViewCreated(View rootView) {
		Log.d(TAG, childClass + "-->onViewCreated");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		InjectTools.doActivityInjectWork(this, childClass);
		onViewCreated(this.getView());
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, childClass + "-->onActivityCreated");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, childClass + "-->onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, childClass + "-->onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, childClass + "-->onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, childClass + "-->onStop");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d(TAG, childClass + "-->onDestroyView");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, childClass + "-->onDestroy");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(TAG, childClass + "-->onDetach");
	}

}
