package cn.com.lowe.android.app.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CrashLogHandler implements UncaughtExceptionHandler {
	public static final String TAG = "CrashHandler";

	/** CrashHandler实例 */
	private static CrashLogHandler INSTANCE;

	/** 程序的Context对象 */
	@SuppressWarnings("unused")
	private Context mContext;

	/** 系统默认的UncaughtException处理类 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	/** 使用Properties来保存设备的信息和错误堆栈信息 */
	private static Map<String, String> mDeviceCrashInfo = new HashMap<String, String>();

	/** 错误报告文件的扩展名 */
	private static final String CRASH_REPORTER_EXTENSION = ".log";

	private File logFolder;

	private CrashLogListener logListener;

	private boolean enableSystemHandler = true;
	
	private String folderName=null;

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashLogHandler getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CrashLogHandler();
		return INSTANCE;
	}

	/**
	 * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
	 * 
	 * @param ctx
	 */
	public void init(Context ctx) {
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		if(folderName==null){
			folderName = "log_"+ctx.getPackageName().replace(".", "_");
		}
		if (existSDcard()) {
			logFolder = new File(Environment.getExternalStorageDirectory(), folderName);
		} else {
			logFolder = new File(ctx.getFilesDir(), folderName);
		}
		if (!logFolder.exists()) {
			logFolder.mkdirs();
		}
		collectCrashDeviceInfo(ctx);
	}

	public void setLogListener(CrashLogListener logListener) {
		this.logListener = logListener;
	}

	public void setEnableSystemHandler(boolean enableSystemHandler) {
		this.enableSystemHandler = enableSystemHandler;
	}
    
	
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		if (handleException(thread, ex)) {
			mDefaultHandler.uncaughtException(thread, ex);
		}

	}

	/**
	 * 判断存储卡是否存在
	 * 
	 * @return
	 */
	private static boolean existSDcard() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 * 
	 * @param thread
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false
	 */
	private boolean handleException(Thread thread, Throwable ex) {
		if (ex == null) {
			return enableSystemHandler;
		}
		if (logListener != null) {
			logListener.beforeHandlerException(this, thread, ex);
		}
		File file = saveCrashInfoToFile(ex);
		if (logListener != null && file != null) {
			logListener.afterHandlerException(this, file);
		}
		return enableSystemHandler;
	}

	/**
	 * 收集程序崩溃的设备信息
	 * 
	 * @param ctx
	 */
	private void collectCrashDeviceInfo(Context ctx) {
		// 获取设备硬件信息
		Build bd = new Build();
		Field[] fields = Build.class.getFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				mDeviceCrashInfo.put(field.getName(), field.get(bd).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			// 获取设备其他信息
			TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
			// IMEI
			mDeviceCrashInfo.put("IMEI", tm.getDeviceId());
			// IMSI
			mDeviceCrashInfo.put("IMSI", tm.getSubscriberId());
			// NETWORKTYPE
			mDeviceCrashInfo.put("NETWORKTYPE", "" + tm.getNetworkType());
		} catch (Exception e) {
			Log.d(TAG, "无权限读取设备TelephonyManager信息",e);
		}
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return
	 */
	private File saveCrashInfoToFile(Throwable ex) {
		PrintWriter printWriter = null;
		Writer info = null;
		try {
			info = new StringWriter();
			printWriter = new PrintWriter(info);
			ex.printStackTrace(printWriter);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}

		}
		Map<String, String> extendInfo = null;
		if (logListener != null) {
			extendInfo = logListener.getExtendInfo();
		}
		String errorLog = creatErrorLog(mDeviceCrashInfo, extendInfo, info);

		Date currentDate = new Date(System.currentTimeMillis());
		String fileName = null;
		if (logListener == null) {
			fileName = "" + currentDate.getTime() + CRASH_REPORTER_EXTENSION;
		} else {
			fileName = logListener.getLogFileName(ex, currentDate)+ CRASH_REPORTER_EXTENSION;
		}
		File file = null;
		try {
			file = createErrorFile(errorLog, fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	private File createErrorFile(String errorLog, String fileName) throws IOException{
		File file = new File(logFolder, fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(errorLog.getBytes());
			fos.flush();
		}finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	private static String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n";
	private static String TAG_ERROR_S = "<Error>\n";
	private static String TAG_ERROR_E = "</Error>";

	private static String TAG_DEVICE_S = "\t<DeviceInfo>";
	private static String TAG_DEVICE_E = "\t</DeviceInfo>\n";

	private static String TAG_EXTEND_S = "\t<ExtendInfo>";
	private static String TAG_EXTEND_E = "\t</ExtendInfo>\n";

	private static String TAG_ERRORCONTENT_S = "\t<ErrorContent>\n";
	private static String TAG_ERRORCONTENT_E = "\t</ErrorContent>\n";

	private static String DATA_S = "\t<![CDATA[\n";
	private static String DATA_E = "\t]]>\n";

	private String creatErrorLog(Map<String, String> mDeviceCrashInfo, Map<String, String> extendInfo, Writer info) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(XML_HEAD);
		strBuf.append(TAG_ERROR_S);
		// 添加设备信息
		strBuf.append(TAG_DEVICE_S);
		try {
			strBuf.append(new JSONObject(mDeviceCrashInfo).toString());
		} catch (Exception e) {
			strBuf.append("");
		}
		strBuf.append(TAG_DEVICE_E);

		// 添加额外信息
		strBuf.append(TAG_EXTEND_S);
		try {
			strBuf.append(new JSONObject(extendInfo).toString());
		} catch (Exception e) {
			strBuf.append("");
		}
		strBuf.append(TAG_EXTEND_E);

		// 添加错误信息
		strBuf.append(TAG_ERRORCONTENT_S);
		strBuf.append(DATA_S);
		strBuf.append(info.toString());
		strBuf.append("\n");
		strBuf.append(DATA_E);
		strBuf.append(TAG_ERRORCONTENT_E);

		strBuf.append(TAG_ERROR_E);
		return strBuf.toString();
	}

	public interface CrashLogListener {

		public void beforeHandlerException(CrashLogHandler logHandler, Thread thread, Throwable ex);

		public void afterHandlerException(CrashLogHandler logHandler, File logFile);

		public String getLogFileName(Throwable ex, Date date);

		public Map<String, String> getExtendInfo();
	}
}
