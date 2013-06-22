package cn.com.lowe.android.tools.thead;

import android.os.Bundle;
import android.view.View;
import cn.com.lowe.android.R;
import cn.com.lowe.android.app.InjectActivity;

public class ThreadTestActivity extends InjectActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_thread);
	}
	
	public void testThread(View view){
		ThreadDemo thread =new ThreadDemo(this);
		thread.execute(ThreadDemo.threadrun,"");
	}
	public void testNoProgressThread(View view){
		ThreadDemo thread =new ThreadDemo(this);
		thread.enableProcessDialog(false);
		thread.execute(ThreadDemo.threadrun,"");
	}
	public void testChangeProgressThread(View view){
		ThreadDemo thread =new ThreadDemo(this);
		thread.execute("changeProgressThread");
	}
	public void testUnCancleThread(View view){
		ThreadDemo thread =new ThreadDemo(this);
		thread.cancleProcessDialog(false);
		thread.execute("testUnCancleThread");
	}
	public void testAnnoSuccThread(View view){
		ThreadDemo thread =new ThreadDemo(this);
		thread.execute(ThreadDemo.changeProgressThread);
	}
	public void testAnnoFailThread(View view){
		ThreadDemo thread =new ThreadDemo(this);
		thread.execute(3);
	}
	public void testMethodNameSuccThread(View view){
		ThreadDemo thread =new ThreadDemo(this);
		thread.execute("threadrun","222");
	}
	public void testMethodNameFailThread(View view){
		ThreadDemo thread =new ThreadDemo(this);
		thread.execute("threadrun");
	}
}
