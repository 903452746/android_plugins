package cn.com.lowe.android.tools.dataprocess;

import cn.com.lowe.android.tools.dataprocess.annotation.FieldProperty;
import android.view.View;

/**
 * 校验接口
 * @author zhengjin
 *
 */
public interface IValidation {
	
	/**
	 * @param fieldName 字段名
	 * @param fieldClass 字段类型
	 * @param fieldView 字段对应View 
	 * @param fieldAnno 字段属性注解
	 * @param tipName 字段错误提示内容
	 * @param iValue 字段取值接口
	 * @return
	 */
	public String[] check(String fieldName, Class<?> fieldClass, View fieldView, FieldProperty fieldAnno, String tipName, IValue iValue);

}
