package cn.com.lowe.android.app;

import java.util.ArrayList;
import java.util.List;


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
