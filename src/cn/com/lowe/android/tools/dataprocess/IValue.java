package cn.com.lowe.android.tools.dataprocess;

import android.view.View;

/**
 * 字段取值接口
 * 
 * @author zhengjin
 * 
 */
public interface IValue {

	/**
	 * 字段取值
	 * @param view  字段对应View
	 * @param clazz 字段类型
	 * @return
	 */
	public Object getValue(View view, Class<?> clazz);

	/**
	 * 字段取字段串
	 * @param view  
	 * @return 
	 */
	public String getValueStr(View view);

	/**
	 * 字段赋值
	 * @param value 字段值
	 * @param view 字段对应View
	 * @return
	 */
	public boolean setValue(Object value, View view);

	/**
	 * 字符值转字段值
	 * @param value 字段值
	 * @param clazz 字段类型
	 * @return
	 */
	public Object getValueByStr(String value, Class<?> clazz);

}
