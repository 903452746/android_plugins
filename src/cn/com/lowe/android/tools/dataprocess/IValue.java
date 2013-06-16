package cn.com.lowe.android.tools.dataprocess;

import android.view.View;

/**
 * �ֶ�ȡֵ�ӿ�
 * 
 * @author zhengjin
 * 
 */
public interface IValue {

	/**
	 * �ֶ�ȡֵ
	 * 
	 * @param view
	 *            �ֶζ�ӦView
	 * @param clazz
	 *            �ֶ�����
	 * @param splitFlag
	 * @return
	 */
	public Object getValue(View view, Class<?> clazz) throws Exception;

	/**
	 * �ֶ�ȡ�ֶδ�
	 * 
	 * @param view
	 * @return
	 */
	public String getValueStr(View view);

	/**
	 * �ֶθ�ֵ
	 * 
	 * @param value
	 *            �ֶ�ֵ
	 * @param view
	 *            �ֶζ�ӦView
	 * @return
	 */
	public boolean setValue(Object value, View view, String splitFlag);

	/**
	 * �ַ�ֵת�ֶ�ֵ
	 * 
	 * @param value
	 *            �ֶ�ֵ
	 * @param clazz
	 *            �ֶ�����
	 * @param splitFlag
	 * @return
	 */
	public Object getValueByStr(String value, Class<?> clazz) throws Exception;

}
