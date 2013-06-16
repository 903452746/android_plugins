package cn.com.lowe.android.tools.dataprocess.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import cn.com.lowe.android.tools.dataprocess.IValidation;
import cn.com.lowe.android.tools.dataprocess.IValue;
import cn.com.lowe.android.tools.dataprocess.annotation.FieldProperty;
import cn.com.lowe.android.tools.dataprocess.lang.DataType;
import android.text.TextUtils;
import android.view.View;

/**
 * Ĭ��У����
 * @author zhengjin
 *
 */
public class ValidationUtil implements IValidation {
	private SimpleDateFormat fmt = new SimpleDateFormat();

	@Override
	public String[] check(String fieldName, Class<?> fieldClass, View fieldView, FieldProperty fieldAnno, String tipName,IValue iValue) {
		StringBuilder sb = new StringBuilder();
		String value = iValue.getValueStr(fieldView);
		if (fieldAnno.required()) {
			sb.append(checkNullable(value, tipName));
			if (sb.length() > 0) {
				value = null;
				return new String[] { sb.toString(), value };
			}
		}
		// ��ֵ�û���������У��
		if (TextUtils.isEmpty(value)) {
			return new String[] { sb.toString(), value };
		}
		if (fieldAnno.maxLength() != Integer.MAX_VALUE || fieldAnno.minLength() != Integer.MIN_VALUE) {
			sb.append(checkLength(value, tipName, fieldAnno.maxLength(), fieldAnno.minLength(), fieldAnno.required()));
			if (sb.length() > 0) {
				value = null;
				return new String[] { sb.toString(), value };
			}
		}
		sb.append(checkDataType(value, tipName, fieldAnno.dataType(), fieldAnno.dateFmt()));
		if (sb.length() > 0) {
			value = null;
			return new String[] { sb.toString(), value };
		}
		sb.append(checkDataExpression(value, tipName, fieldAnno.dataExpression(), fieldAnno.dataExpressionTip()));
		if (sb.length() > 0) {
			value = null;
			return new String[] { sb.toString(), value };
		}
		//У���ֶ������������Ƿ����ת��
		sb.append(checkFieldType(value, tipName,fieldClass));
		if (sb.length() > 0) {
			value = null;
			return new String[] { sb.toString(), value };
		}
		return null;

	}

	/**
	* @Title: checkFieldType
	* @Description: У���ֶ������������Ƿ����ת��
	* @param @param value
	* @param @param tipName
	* @param @param fieldClass
	* @param @return
	* @return String
	* @throws
	*/
	private String checkFieldType(String value, String tipName, Class<?> fieldClass) {
		if(null==value||"".equals(value)){
			return "";
		}else if (fieldClass == String.class) {
			return "";
		} else if (fieldClass == Integer.class) {
			try {
				Integer.parseInt(value);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return tipName+"����������\n";
			}
		} else if (fieldClass == Float.class) {
			try {
				Float.parseFloat(value);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return tipName+"������С��\n";
			}
		} else if (fieldClass == Double.class) {
			try {
				Double.parseDouble(value);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return tipName+"������С��\n";
			}
		} else if (fieldClass == Short.class) {
			try {
				Short.parseShort(value);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return tipName+"����������\n";
			}
		} else if (fieldClass == Long.class) {
			try {
				Long.parseLong(value);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return tipName+"����������\n";
			}
		} else if (fieldClass == Boolean.class) {
			return "";
		}
		return "";
	}

	/**
	 * У����ʽ
	 * @param value
	 * @param tipName
	 * @param dataExpression
	 * @param dataExpressionTip
	 * @return
	 */
	private String checkDataExpression(String value, String tipName, String dataExpression, String dataExpressionTip) {
		if (TextUtils.isEmpty(dataExpression)) {
			return "";
		}
		if (!value.matches(dataExpression)) {
			return tipName  + (TextUtils.isEmpty(value) ? dataExpression : dataExpressionTip) + "�ĸ�ʽ\n";
		}
		return "";
	}

	/**
	 * У����������
	 * @param value
	 * @param tipName
	 * @param dataType
	 * @param dateFmt
	 * @return
	 */
	private String checkDataType(String value, String tipName, DataType dataType, String dateFmt) {
		switch (dataType) {
		case TEXT:
			break;
		case DATE:
		case TIME:
		case DATETIME:
			fmt.applyPattern(dateFmt);
			try {
				fmt.parse(value);
			} catch (ParseException e) {
				return tipName + "��ʽ������" + dateFmt + "\n";
			}
			break;
		case FLOATNUMBER:
			try {
				Float.parseFloat(value);
			} catch (NumberFormatException e) {
				return tipName + "������С������\n";
			}
			break;
		case NUMBER:
			try {
				Integer.parseInt(value);
			} catch (NumberFormatException e) {
				return tipName + "��������������\n";
			}
			break;

		default:
			break;
		}
		return "";
	}

	/**
	 * У��ǿ�
	 * @param value
	 * @param tipName
	 * @return
	 */
	private String checkNullable(String value, String tipName) {
		if (TextUtils.isEmpty(value)) {
			return tipName + "Ϊ������\n";
		}
		return "";

	}

	/**
	 * У�鳤��
	 * @param value
	 * @param tipName
	 * @param maxLength
	 * @param minLength
	 * @param required
	 * @return
	 */
	private String checkLength(String value, String tipName, int maxLength, int minLength, boolean required) {
		String note = "";
		// ���ηǱ���ĳ���У��
		if (maxLength != Integer.MAX_VALUE && value.length() > maxLength) {
			note += tipName + "ӦС��" + maxLength + "���ַ�\n";
		}
		if (minLength != Integer.MIN_VALUE && value.length() < minLength) {
			note += tipName + "Ӧ����" + minLength + "���ַ�\n";
		}
		return note;
	}

}
