package cn.com.lowe.android.tools.dataprocess.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段中文提示注解
 * @author zhengjin
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TipProperty {

	/**
	 * 中文提示字符串
	 * @return
	 */
	public String fieldCN() default "";

	/**
	 * 中文提示字符串对应的R.string.xxx
	 * @return
	 */
	public int fieldCNId() default 0;
	
	/**
	 * 通过TipProperty("xxxx")设置中文提示，若设置此值，其他值将作废
	 * @return
	 */
	public String value() default "";
}
