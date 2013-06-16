package cn.com.lowe.android.tools.thread;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import cn.com.lowe.android.tools.thread.exception.ThreadMehtodException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * @Description: TODO
 * 
 * @Author zhengjin
 * @Date 2013-6-8 下午6:36:52
 * @Version 1.0
 */
public abstract class InterruptThread extends Thread {
	public static final String TAG = "InterruptThread";
	private static final String DEFULT_THREAD_TIP = "正在处理中,请稍后...";
	private static Map<String, Method> threadMethod = new HashMap<String, Method>();
	
	protected Class<?> childClass;
	private Method executeMethod;
	private Object[] params;
	private String tip = DEFULT_THREAD_TIP;
	private ProgressDialog process;
	private Handler innerHandler;
	/**
	 * 通过该对象操作UI界面变化
	 * <p>
	 * post
	 * 
	 * @Fields uiHandler : UI消息句柄
	 */
	protected HandlerProxy uiHandler;
	/**
	 * @Fields stoped : 后台线程停止标识 默认为false;
	 */
	protected volatile boolean stoped = false;

	/**
	 * 
	 * @Fields context : 界面上下文
	 */
	protected Context context;

	/**
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param context
	 */
	public InterruptThread(Context context) {
		super();
		this.context = context;
		this.childClass = this.getClass();
		uiHandler = new HandlerProxy(Looper.getMainLooper(), this);
		innerHandler=new Handler(Looper.getMainLooper());
	}

	@Override
	public void run() {
		try {
			if (stoped) {
				Log.d(TAG, "线程被终止");
				return;
			}
			showProcessDialog();
			executeMethod.invoke(this, params);

		} catch (Exception e) {
			Log.e(TAG, "后台线程函数执行失败", e);
		} finally {
			dismissProcessDialog();
		}
	}

	/**
	 * @Title: execute
	 * @Description: 线程调度执行函数
	 * @param @param executeMethodName
	 * @param @param params
	 * @param @throws ThreadMehtodException
	 * @return void
	 * @throws
	 */
	public final void execute(String executeMethodName, Object... params) throws ThreadMehtodException {
		executeMethod = threadMethod.get(executeMethodName);
		if (executeMethod == null) {
			Method[] methods = childClass.getMethods();
			for (Method o : methods) {
				if (o.getName().equals(executeMethodName)) {
					executeMethod = o;
					threadMethod.put(executeMethodName, o);
					break;
				}
			}
			if (executeMethod == null) {
				throw new ThreadMehtodException("后台线程执行函数[" + executeMethodName + "]不存在");
			}
			if (!executeMethod.getReturnType().getName().equalsIgnoreCase("void")) {
				throw new ThreadMehtodException("后台线程执行函数[" + executeMethodName + "]返回参数必须为空");
			}
		}
		this.params = params;
		this.start();
	}

	/**
	 * (非 Javadoc)
	 * <p>
	 * Title: interrupt
	 * </p>
	 * <p>
	 * Description: 终止线程指令
	 * </p>
	 * 
	 * @see java.lang.Thread#interrupt()
	 */
	@Override
	public void interrupt() {
		this.stoped = true;
		super.interrupt();
		Log.d(TAG, "为线程打上终止标识");
	}

	/**
	 * @Title: updateDialog
	 * @Description: 更新对话框指令
	 * @param @param msg
	 * @param @param cancelable
	 * @return void
	 * @throws
	 */
	public void updateDialog(final String msg, boolean cancelable) {
		setDialogCancelable(cancelable);
		updateDialogMsg(msg);
	}

	/**
	 * @Title: setDialogCancelable
	 * @Description: 设置对话框不可取消
	 * @param @param cancelable
	 * @return void
	 * @throws
	 */
	public void setDialogCancelable(boolean cancelable) {
		process.setCancelable(cancelable);
	}

	/**
	 * @Title: updateDialogMsg
	 * @Description: 更新对话框内容
	 * @param @param msg
	 * @return void
	 * @throws
	 */
	public void updateDialogMsg(final String msg) {
		innerHandler.post(new Runnable() {

			@Override
			public void run() {
				process.setMessage(msg);
			}
		});

	}

	/**
	 * @Title: dismissProcessDialog
	 * @Description: 取消对话框
	 * @param
	 * @return void
	 * @throws
	 */
	public void dismissProcessDialog() {
		if (process != null && process.isShowing()) {
			innerHandler.post(new Runnable() {

				@Override
				public void run() {
					process.dismiss();
				}
			});
		}
	}

	/**
	 * @Title: showProcessDialog
	 * @Description: 展示对话框
	 * @param
	 * @return void
	 * @throws
	 */
	public void showProcessDialog() {
		innerHandler.post(new Runnable() {

			@Override
			public void run() {
				process = ProgressDialog.show(context, null, tip);
				process.setCancelable(true);
				process.setOnDismissListener(dismissListener);
			}
		});
	}

	/**
	 * @Fields dismissListener : 对话框中断监听
	 */
	private OnDismissListener dismissListener = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface dialog) {
			InterruptThread.this.interrupt();
		}
	};
}
