package cn.com.lowe.android.tools.thread;

import java.lang.reflect.Method;

import cn.com.lowe.android.tools.thread.exception.ThreadMehtodException;
import cn.com.lowe.android.tools.thread.exception.ThreadRunException;

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

	protected Class<?> childClass;
	private Method executeMethod;
	private Object[] params;
	private String tip = DEFULT_THREAD_TIP;
	/**
	 * @Fields process :进度栏
	 */
	private ProgressDialog process;
	/**
	 * @Fields innerHandler : 内部handler，不使用handler代理
	 */
	private Handler innerHandler;
	/**
	 * @Fields processDialogShowFinished : 进度栏UI显示完成
	 */
	private volatile boolean processDialogShowFinished = false;
	/**
	 * @Fields processDialogShowStarted : 进度栏UI显示开始
	 */
	private volatile boolean processDialogShowStarted = false;
	/**
	 * @Fields enableProcessDialog : 是否启用进度栏
	 */
	private volatile boolean enableProcessDialog = true;

	/**
	 * @Fields cancleProcessDialog : 是否可以取消进度啦
	 */
	private volatile boolean cancleProcessDialog = true;
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
		innerHandler = new Handler(Looper.getMainLooper());
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
			throw new ThreadRunException("后台线程函数执行失败", e);
		} finally {
			dismissProcessDialog();
		}

	}

	/**
	 * @Title: execute
	 * @Description: 线程调度执行函数
	 * @param executeMethodName
	 * @param params
	 * @return void
	 */
	public final void execute(String executeMethodName, Object... params) {
		Method[] methods = childClass.getMethods();
		for (Method method : methods) {
			if (!method.getName().equals(executeMethodName)) {
				continue;
			}
			Class<?>[] paramTypes = method.getParameterTypes();
			if (paramTypes.length != params.length) {
				continue;
			}
			for (int i = 0, len = paramTypes.length; i < len; i++) {
				try {
					paramTypes[i].cast(params[i]);
				} catch (ClassCastException e) {
					continue;
				}
			}
			executeMethod = method;
		}
		if (executeMethod == null) {
			throw new ThreadMehtodException("后台线程执行函数[" + executeMethodName + "]不存在");
		}
		this.params = params;
		this.start();
	}

	/**
	 * @Title: execute
	 * @Description: 线程调度执行函数
	 * @param invokeId
	 * @param params
	 * @return void
	 */
	public final void execute(int invokeId, Object... params) {
		Method[] methods = childClass.getMethods();
		InvokeId anno;
		for (Method method : methods) {
			if (method.isAnnotationPresent(InvokeId.class)) {
				anno = method.getAnnotation(InvokeId.class);
				if (anno.value() == invokeId) {
					executeMethod = method;
					break;
				}
			}
		}
		if (executeMethod == null) {
			throw new ThreadMehtodException("后台线程执行函数[invokeid:" + invokeId + "]不存在");
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
	 * @param msg
	 * @param cancelable
	 * @return void
	 */
	public void updateDialog(final String msg, boolean cancelable) {
		if (!enableProcessDialog) {
			return;
		}

		setDialogCancelable(cancelable);
		updateDialogMsg(msg);
	}

	/**
	 * @Title: setDialogCancelable
	 * @Description: 设置对话框不可取消
	 * @param cancelable
	 * @return void
	 */
	public void setDialogCancelable(boolean cancelable) {
		if (!enableProcessDialog) {
			return;
		}
		if (processDialogShowStarted) {

			while (!processDialogShowFinished) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			process.setCancelable(cancelable);
		}

	}

	/**
	 * @Title: enableProcessDialog
	 * @Description: 启动线程后，是否显示进度框
	 * @param enable
	 * @return void
	 */
	public void enableProcessDialog(boolean enable) {
		enableProcessDialog = enable;
	}

	/**
	 * @Title: cancleProcessDialog
	 * @Description: 启动线程后，进度对话框是否可取消
	 * @param cancle
	 * @return void
	 */
	public void cancleProcessDialog(boolean cancle) {
		cancleProcessDialog = cancle;
	}

	/**
	 * @Title: updateDialogMsg
	 * @Description: 更新对话框内容
	 * @param msg
	 * @return void
	 */
	public void updateDialogMsg(final String msg) {
		if (!enableProcessDialog) {
			return;
		}
		if (processDialogShowStarted) {

			while (!processDialogShowFinished) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Log.d(TAG, "更新进度框");
			innerHandler.post(new Runnable() {

				@Override
				public void run() {
					process.setMessage(msg);
				}
			});
		}
	}

	/**
	 * @Title: dismissProcessDialog
	 * @Description: 取消对话框
	 * @return void
	 */
	public void dismissProcessDialog() {
		if (!enableProcessDialog) {
			return;
		}
		if (processDialogShowStarted) {

			while (!processDialogShowFinished) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Log.d(TAG, "去除进度框");
			if (process != null) {
				process.dismiss();
			}
		}

	}

	/**
	 * @Title: showProcessDialog
	 * @Description: 展示对话框
	 * @return void
	 */
	public void showProcessDialog() {
		if (!enableProcessDialog) {
			return;
		}
		processDialogShowStarted = true;
		innerHandler.post(new Runnable() {

			@Override
			public void run() {
				process = ProgressDialog.show(context, null, tip);
				process.setCancelable(cancleProcessDialog);
				process.setOnDismissListener(dismissListener);
				processDialogShowFinished = true;
				Log.d(TAG, "显示进度框");

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