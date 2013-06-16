package cn.com.lowe.android.tools.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * @Description 消息句柄代理封装,为了处理调用后台线程的ui停止后,未执行中断监测,会继续向其他现存ui发送界面操作指令而可能导致的不可捕获异常<p>
 *  
 * 
 * @Author zhengjin
 * @Date 2013-6-9 下午11:13:38
 * @Version 1.0
 */
public class HandlerProxy implements IHandler {
	private Handler handler;
	private InterruptThread interruptThread;

	public HandlerProxy(Looper looper, InterruptThread interruptThread) {
		super();
		handler = new Handler(looper);
		this.interruptThread = interruptThread;
	}

	@Override
	public void dispatchMessage(Message msg) {
		handler.dispatchMessage(msg);

	}

	@Override
	public Message obtainMessage() {
		return handler.obtainMessage();
	}

	@Override
	public Message obtainMessage(int what) {
		return handler.obtainMessage(what);
	}

	@Override
	public Message obtainMessage(int what, Object obj) {
		return handler.obtainMessage(what, obj);
	}

	@Override
	public Message obtainMessage(int what, int arg1, int arg2) {
		return handler.obtainMessage(what, arg1, arg2);
	}

	@Override
	public Message obtainMessage(int what, int arg1, int arg2, Object obj) {
		return handler.obtainMessage(what, arg1, arg2, obj);
	}

	/*
	 * ===============这些方法可能会出现为了处理调用后台线程的ui停止后,会继续向其他现存ui发送界面操作指令而可能导致的不可捕获异常错误==
	 * =========
	 */
	@Override
	public boolean post(Runnable r) {
		System.out.println(interruptThread.stoped);
		if (interruptThread.stoped){
			System.out.println("中断了吧？？？");
			return false;	
		}
			
		System.out.println("继续执行吗？？？");
		return handler.post(r);
	}

	@Override
	public boolean postAtTime(Runnable r, long uptimeMillis) {
		if (interruptThread.stoped)
			return false;
		return handler.postAtTime(r, uptimeMillis);
	}

	@Override
	public boolean postAtTime(Runnable r, Object token, long uptimeMillis) {
		if (interruptThread.stoped)
			return false;
		return handler.postAtTime(r, token, uptimeMillis);
	}

	@Override
	public boolean postDelayed(Runnable r, long delayMillis) {
		if (interruptThread.stoped)
			return false;
		return handler.postDelayed(r, delayMillis);
	}

	@Override
	public boolean postAtFrontOfQueue(Runnable r) {
		if (interruptThread.stoped)
			return false;
		return handler.postAtFrontOfQueue(r);
	}

	/*
	 * ===============这些方法可能会出现为了处理调用后台线程的ui停止后,会继续向其他现存ui发送界面操作指令而可能导致的不可捕获异常错误==
	 * =========
	 */
	/**
	 * (非 Javadoc)
	 * <p>
	 * Title: removeCallbacks
	 * </p>
	 * <p>
	 * Description:代理未做实现
	 * </p>
	 * 
	 * @param r
	 * @see cn.com.lowe.android.tools.thread.IHandler#removeCallbacks(java.lang.Runnable)
	 */
	@Override
	public void removeCallbacks(Runnable r) {
		// TODO Auto-generated method stub

	}

	/**
	 * (非 Javadoc)
	 * <p>
	 * Title: removeCallbacks
	 * </p>
	 * <p>
	 * Description:代理未做实现
	 * </p>
	 * 
	 * @param r
	 * @param token
	 * @see cn.com.lowe.android.tools.thread.IHandler#removeCallbacks(java.lang.Runnable,
	 *      java.lang.Object)
	 */
	@Override
	public void removeCallbacks(Runnable r, Object token) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean sendMessage(Message msg) {
		return handler.sendMessage(msg);
	}

	@Override
	public boolean sendEmptyMessage(int what) {
		return handler.sendEmptyMessage(what);
	}

	@Override
	public boolean sendEmptyMessageDelayed(int what, long delayMillis) {
		return handler.sendEmptyMessageDelayed(what, delayMillis);
	}

	@Override
	public boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
		return handler.sendEmptyMessageAtTime(what, uptimeMillis);
	}

	@Override
	public boolean sendMessageDelayed(Message msg, long delayMillis) {
		return handler.sendMessageDelayed(msg, delayMillis);
	}

	@Override
	public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
		return handler.sendMessageAtTime(msg, uptimeMillis);
	}

	@Override
	public boolean sendMessageAtFrontOfQueue(Message msg) {
		return handler.sendMessageAtFrontOfQueue(msg);
	}

	/**
	 * (非 Javadoc)
	 * <p>
	 * Title: removeMessages
	 * </p>
	 * <p>
	 * Description:代理未做实现
	 * </p>
	 * 
	 * @param what
	 * @see cn.com.lowe.android.tools.thread.IHandler#removeMessages(int)
	 */
	@Override
	public void removeMessages(int what) {
		// TODO Auto-generated method stub

	}

	/**
	 * (非 Javadoc)
	 * <p>
	 * Title: removeMessages
	 * </p>
	 * <p>
	 * Description: 代理未做实现
	 * </p>
	 * 
	 * @param what
	 * @param object
	 * @see cn.com.lowe.android.tools.thread.IHandler#removeMessages(int,
	 *      java.lang.Object)
	 */
	@Override
	public void removeMessages(int what, Object object) {
		// TODO Auto-generated method stub

	}

	/**
	 * (非 Javadoc)
	 * <p>
	 * Title: removeCallbacksAndMessages
	 * </p>
	 * <p>
	 * Description: 代理未做实现
	 * </p>
	 * 
	 * @param token
	 * @see cn.com.lowe.android.tools.thread.IHandler#removeCallbacksAndMessages(java.lang.Object)
	 */
	@Override
	public void removeCallbacksAndMessages(Object token) {
		// TODO Auto-generated method stub

	}

	/**
	 * (非 Javadoc)
	 * <p>
	 * Title: hasMessages
	 * </p>
	 * <p>
	 * Description:代理未做实现
	 * </p>
	 * 
	 * @param what
	 * @return
	 * @see cn.com.lowe.android.tools.thread.IHandler#hasMessages(int)
	 */
	@Override
	public boolean hasMessages(int what) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * (非 Javadoc)
	 * <p>
	 * Title: hasMessages
	 * </p>
	 * <p>
	 * Description: 代理未做实现
	 * </p>
	 * 
	 * @param what
	 * @param object
	 * @return
	 * @see cn.com.lowe.android.tools.thread.IHandler#hasMessages(int,
	 *      java.lang.Object)
	 */
	@Override
	public boolean hasMessages(int what, Object object) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * (非 Javadoc)
	 * <p>
	 * Title: getLooper
	 * </p>
	 * <p>
	 * Description:代理未做实现
	 * </p>
	 * 
	 * @return
	 * @see cn.com.lowe.android.tools.thread.IHandler#getLooper()
	 */
	@Override
	public Looper getLooper() {
		return null;
	}

}
