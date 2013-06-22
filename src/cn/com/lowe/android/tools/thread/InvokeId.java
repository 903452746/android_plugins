package cn.com.lowe.android.tools.thread;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* @Description: InterruptThread ���÷���ע����
*
* @Author zhengjin 
* @Date 2013-6-22 ����8:13:57
* @Version 1.0
*/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InvokeId {
	public int value() default Integer.MAX_VALUE;
}
