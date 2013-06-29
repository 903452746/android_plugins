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
 * 默认校验器
 * 
 * @author zhengjin
 * 
 */
public class ValidationUtil implements IValidation {
	private SimpleDateFormat fmt = new SimpleDateFormat();

	@Override
	public String[] check(String fieldName, Class<?> fieldClass, View fieldView, FieldProperty fieldAnno, String tipName, IValue iValue) {
		StringBuilder sb = new StringBuilder();
		String value = iValue.getValueStr(fieldView);
		if (fieldAnno.required()) {
			sb.append(checkNullable(value, tipName));
			if (sb.length() > 0) {
				value = null;
				return new String[] { sb.toString(), value };
			}
		}
		// 空值得话不做以下校验
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
		// 校验字段类型与数据是否可以转换
		sb.append(checkFieldType(value, tipName, fieldClass));
		if (sb.length() > 0) {
			value = null;
			return new String[] { sb.toString(), value };
		}
		return new String[] { "", value };

	}

	/**
	 * @Title: checkFieldType
	 * @Description: 校验字段类型与数据是否可以转换
	 * @param @param value
	 * @param @param tipName
	 * @param @param fieldClass
	 * @param @return
	 * @return String
	 * @throws
	 */
	private String checkFieldType(String value, String tipName, Class<?> fieldClass) {
		if (null == value || "".equals(value)) {
			return "";
		} else if (fieldClass == String.class) {
			return "";
		} else if (fieldClass == Integer.class) {
			try {
				Integer.parseInt(value);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return tipName + "必须是整数\n";
			}
		} else if (fieldClass == Float.class) {
			try {
				Float.parseFloat(value);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return tipName + "必须是小数\n";
			}
		} else if (fieldClass == Double.class) {
			try {
				Double.parseDouble(value);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return tipName + "必须是小数\n";
			}
		} else if (fieldClass == Short.class) {
			try {
				Short.parseShort(value);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return tipName + "必须是整数\n";
			}
		} else if (fieldClass == Long.class) {
			try {
				Long.parseLong(value);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return tipName + "必须是整数\n";
			}
		} else if (fieldClass == Boolean.class) {
			return "";
		}
		return "";
	}

	/**
	 * 校验表达式
	 * 
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
			return tipName + (TextUtils.isEmpty(value) ? dataExpression : dataExpressionTip) + "的格式\n";
		}
		return "";
	}

	/**
	 * 校验数据类型
	 * 
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
				return tipName + "格式必须是" + dateFmt + "\n";
			}
			break;
		case FLOATNUMBER:
			try {
				Float.parseFloat(value);
			} catch (NumberFormatException e) {
				return tipName + "必须是小数类型\n";
			}
			break;
		case NUMBER:
			try {
				Integer.parseInt(value);
			} catch (NumberFormatException e) {
				return tipName + "必须是整数类型\n";
			}
			break;

		default:
			break;
		}
		return "";
	}

	/**
	 * 校验非空
	 * 
	 * @param value
	 * @param tipName
	 * @return
	 */
	public static String checkNullable(String value, String tipName) {
		if (TextUtils.isEmpty(value)) {
			return tipName + "为必填项\n";
		}
		return "";

	}

	/**
	 * 校验长度
	 * 
	 * @param value
	 * @param tipName
	 * @param maxLength
	 * @param minLength
	 * @param required
	 * @return
	 */
	private String checkLength(String value, String tipName, int maxLength, int minLength, boolean required) {
		String note = "";
		// 屏蔽非必填的长度校验
		if (maxLength != Integer.MAX_VALUE && value.length() > maxLength) {
			note += tipName + "应小于" + maxLength + "个字符\n";
		}
		if (minLength != Integer.MIN_VALUE && value.length() < minLength) {
			note += tipName + "应大于" + minLength + "个字符\n";
		}
		return note;
	}

}
