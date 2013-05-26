package cn.com.lowe.android.tools.dataprocess.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * �ֶ�������ʾע��
 * @author zhengjin
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TipProperty {

	/**
	 * ������ʾ�ַ���
	 * @return
	 */
	public String fieldCN() default "";

	/**
	 * ������ʾ�ַ�����Ӧ��R.string.xxx
	 * @return
	 */
	public int fieldCNId() default 0;
	
	/**
	 * ͨ��TipProperty("xxxx")����������ʾ�������ô�ֵ������ֵ������
	 * @return
	 */
	public String value() default "";
}
