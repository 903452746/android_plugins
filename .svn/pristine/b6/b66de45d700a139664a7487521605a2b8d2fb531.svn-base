package cn.com.lowe.android.tools.dataprocess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cn.com.lowe.android.R;
import cn.com.lowe.android.app.InjectActivity;
import cn.com.lowe.android.app.annotation.InjectView;
import cn.com.lowe.android.tools.dataprocess.DataTool;
import cn.com.lowe.android.tools.dataprocess.lang.ValidationResult;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class BeanActivity extends InjectActivity  {
    View viewv;
    @InjectView(R.id.text_1)
    EditText test1;
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
	public void getEntity(View view){
		try {
			ValidationResult vr= DataTool.validation(Bean.class, viewv);
			Bean o=(Bean) DataTool.getEntity(Bean.class, viewv,vr);
			Log.d("dddd",  new Gson().toJson(o));
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void setEntity(View view){
		Bean o=new Bean();
		o.text1="字符数据格式";
		o.text2=999999999;
		o.text3=9999;
		o.text4=(long)100000000;
		o.text5=25.8f;
		o.text6=12.22;
		o.text7=true;
		o.text8="必读";
		o.text9="非必读";
		o.text10="最大长度为4";
		o.text11="最大长度为2";
		o.text12="文本格式";
		o.text13="2012-02-12";
		o.text14="2012-02-12 13:34:00";
		o.text15="23232.22";
		o.text16="232";
		o.text17="13:01:02";
		o.text18="8888888888";
		DataTool.setView(o, Bean.class, viewv);
		
	}
}
