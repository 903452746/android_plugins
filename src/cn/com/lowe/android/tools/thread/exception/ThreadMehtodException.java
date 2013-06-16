package cn.com.lowe.android.tools.thread.exception;

public class ThreadMehtodException extends Exception{

	/**  
	* @Fields serialVersionUID : TODO
	*/  
	
	private static final long serialVersionUID = 1L;

	public ThreadMehtodException(String msg, Throwable e) {
		super(msg, e);
	}

	public ThreadMehtodException(String msg) {
		super(msg);
	}

}
