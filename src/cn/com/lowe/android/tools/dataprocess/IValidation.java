package cn.com.lowe.android.tools.dataprocess;

import cn.com.lowe.android.tools.dataprocess.annotation.FieldProperty;
import android.view.View;

/**
 * У��ӿ�
 * @author zhengjin
 *
 */
public interface IValidation {
	
	/**
	 * @param fieldName �ֶ���
	 * @param fieldClass �ֶ�����
	 * @param fieldView �ֶζ�ӦView 
	 * @param fieldAnno �ֶ�����ע��
	 * @param tipName �ֶδ�����ʾ����
	 * @param iValue �ֶ�ȡֵ�ӿ�
	 * @return
	 */
	public String[] check(String fieldName, Class<?> fieldClass, View fieldView, FieldProperty fieldAnno, String tipName, IValue iValue);

}
