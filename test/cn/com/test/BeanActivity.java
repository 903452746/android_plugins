package cn.com.test;

import cn.com.lowe.android.R;
import cn.com.lowe.android.tools.dataprocess.DataTool;
import cn.com.lowe.android.tools.dataprocess.lang.ValidationResult;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class BeanActivity extends Activity {
    View viewv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bean_orm);
		viewv=findViewById(R.id.bean);
	}

	public void validation(View view){
		ValidationResult vr= DataTool.validation(Bean.class, viewv);
		Toast.makeText(this, vr.note, Toast.LENGTH_LONG).show();
	}
	public void construct(View view){
		try {
			ValidationResult vr= DataTool.validation(Bean.class, viewv);
			Bean o=(Bean) DataTool.getEntity(Bean.class, viewv,vr);
			Log.d("dddd", o.toString());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
