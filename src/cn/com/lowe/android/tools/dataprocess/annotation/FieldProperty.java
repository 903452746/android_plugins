package cn.com.lowe.android.tools.dataprocess.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.com.lowe.android.tools.dataprocess.IValidation;
import cn.com.lowe.android.tools.dataprocess.IValue;
import cn.com.lowe.android.tools.dataprocess.lang.DataType;
import cn.com.lowe.android.tools.dataprocess.util.ValidationUtil;
import cn.com.lowe.android.tools.dataprocess.util.ValueUtil;

/**
 * �ֶ���������
 * 
 * @author zhengjin
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldProperty {
	/**
	 * �ֶζ�ӦR.id.xxx,���ڴ�View���ҵ���Ӧ��ͼ
	 * 
	 * @default 0
	 * @return
	 */
	public int mapToViewId() default 0;

	/**
	 * �ֶ���󳤶�
	 * 
	 * @default Integer.MAX_VALUE
	 * @return
	 * 
	 */
	public int maxLength() default Integer.MAX_VALUE;

	/**
	 * �ֶ���С����
	 * 
	 * @default Integer.MIN_VALUE
	 * @return
	 */
	public int minLength() default Integer.MIN_VALUE;

	/**
	 * �ֶ��Ƿ����
	 * 
	 * @default false
	 * @return
	 */
	public boolean required() default false;

	/**
	 * �ֶ�����
	 * 
	 * @default DataType.TEXT
	 * @return
	 */
	public DataType dataType() default DataType.TEXT;

	/**
	 * �������ֶεĸ�ʽ
	 * 
	 * @default "yyyy-MM-dd"
	 * @return
	 */
	public String dateFmt() default "yyyy-MM-dd";

	/**
	 * �ֶ�ֵУ��������ʽ
	 * 
	 * @default ""
	 * @return
	 */
	public String dataExpression() default "";

	/**
	 * �ֶ�ֵУ�������ʾ
	 * 
	 * @default ""
	 * @return
	 */
	public String dataExpressionTip() default "";

	/**
	 * �ֶ�У�鹤�� Ĭ��ΪValidationUtil.class,����ʵ��IValidation�ӿ����Զ���У����
	 * 
	 * @default ValidationUtil.class
	 * @return
	 */
	public Class<? extends IValidation> validationClass() default ValidationUtil.class;

	/**
	 * �ֶ�ȡֵ���� Ĭ��ΪValueUtil.class,����ʵ��IValue�ӿ����Զ���ȡֵ������
	 * 
	 * @default ValueUtil.class
	 * @return
	 */
	public Class<? extends IValue> valueConstructClass() default ValueUtil.class;
	
	
	/**
	* @Title: arrySplitFlag
	* @Description: �����ַ���ֱ�ʾ
	* @param @return
	* @return String
	* @throws
	*/
	public String arrySplitFlag() default ",";

}
