package cn.com.test;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import cn.com.lowe.android.R;
import cn.com.lowe.android.app.InjectActivity;
import cn.com.lowe.android.app.annotation.InjectView;

public class TestInjectActivity extends InjectActivity {
	
	@InjectView(R.id.text_1)
	private EditText text1;
	
	@InjectView(1232)
	private EditText text2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bean_orm);
		Log.d("", "" + (text1 != null));
		Log.d("", "" + (text2 != null));
		text2.getTag();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
