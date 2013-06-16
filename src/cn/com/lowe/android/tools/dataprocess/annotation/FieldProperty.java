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
 * 字段属性设置
 * 
 * @author zhengjin
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldProperty {
	/**
	 * 字段对应R.id.xxx,用于从View中找到对应视图
	 * 
	 * @default 0
	 * @return
	 */
	public int mapToViewId() default 0;

	/**
	 * 字段最大长度
	 * 
	 * @default Integer.MAX_VALUE
	 * @return
	 * 
	 */
	public int maxLength() default Integer.MAX_VALUE;

	/**
	 * 字段最小长度
	 * 
	 * @default Integer.MIN_VALUE
	 * @return
	 */
	public int minLength() default Integer.MIN_VALUE;

	/**
	 * 字段是否必填
	 * 
	 * @default false
	 * @return
	 */
	public boolean required() default false;

	/**
	 * 字段类型
	 * 
	 * @default DataType.TEXT
	 * @return
	 */
	public DataType dataType() default DataType.TEXT;

	/**
	 * 日期型字段的格式
	 * 
	 * @default "yyyy-MM-dd"
	 * @return
	 */
	public String dateFmt() default "yyyy-MM-dd";

	/**
	 * 字段值校验正则表达式
	 * 
	 * @default ""
	 * @return
	 */
	public String dataExpression() default "";

	/**
	 * 字段值校验错误提示
	 * 
	 * @default ""
	 * @return
	 */
	public String dataExpressionTip() default "";

	/**
	 * 字段校验工具 默认为ValidationUtil.class,可以实现IValidation接口来自定义校验器
	 * 
	 * @default ValidationUtil.class
	 * @return
	 */
	public Class<? extends IValidation> validationClass() default ValidationUtil.class;

	/**
	 * 字段取值工具 默认为ValueUtil.class,可以实现IValue接口来自定义取值构造器
	 * 
	 * @default ValueUtil.class
	 * @return
	 */
	public Class<? extends IValue> valueConstructClass() default ValueUtil.class;
	
	
	/**
	* @Title: arrySplitFlag
	* @Description: 数组字符拆分标示
	* @param @return
	* @return String
	* @throws
	*/
	public String arrySplitFlag() default ",";

}
