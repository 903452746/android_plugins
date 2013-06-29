package cn.com.lowe.android.app;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.com.lowe.android.app.util.CrashLogHandler;
import cn.com.lowe.android.app.util.CrashLogHandler.CrashLogListener;


import android.app.Activity;
import android.app.Application;
import android.util.Log;

public class InjectApplication extends Application {
	private static final String TAG = "InjectApplication";
	private List<Activity> aList = new ArrayList<Activity>();
	private Activity currentActivity;
	private static InjectApplication mApplication;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "InjectApplication inited");
		mApplication = this;
		CrashLogHandler crashLogHandler=CrashLogHandler.getInstance();
		crashLogHandler.init(this);
		crashLogHandler.setLogListener(new CrashLogListener() {
			private SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss SSS");
			@Override
			public String getLogFileName(Throwable ex, Date date) {
				return fmt.format(date);
			}
			
			@Override
			public Map<String, String> getExtendInfo() {
				return null;
			}
			
			@Override
			public void beforeHandlerException(CrashLogHandler logHandler, Thread thread, Throwable ex) {
				Log.d(TAG, "异常处理前的回调");
				
			}
			
			@Override
			public void afterHandlerException(CrashLogHandler logHandler, File logFile) {
				Log.d(TAG, "异常处理完后的回调");
				
			}
		});
		Thread.setDefaultUncaughtExceptionHandler(crashLogHandler);
	}

	public static InjectApplication getApplication() {
		return mApplication;
	}

	public static void addActivity(Activity o) {
		mApplication.aList.add(o);
	}

	public static void removeActivity(Activity o) {
		mApplication.aList.remove(o);
	}

	public static void setCurrentActivity(Activity o) {
		mApplication.currentActivity = o;
	}

	public static Activity getCurrentActivity() {
		return mApplication.currentActivity;
	}

	public static void exitApp() {
		for (Activity o : mApplication.aList) {
			o.finish();
		}
	}

}
