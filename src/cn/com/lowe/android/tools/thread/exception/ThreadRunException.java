package cn.com.lowe.android.tools.thread.exception;

/**
* @Description: 线程运行时异常
*
* @Author zhengjin 
* @Date 2013-6-22 下午9:32:52
* @Version 1.0
*/
public class ThreadRunException extends RuntimeException{

	/**  
	* @Fields serialVersionUID : TODO
	*/  
	
	private static final long serialVersionUID = 1L;

	public ThreadRunException(String msg, Throwable e) {
		super(msg, e);
	}

	public ThreadRunException(String msg) {
		super(msg);
	}

}
