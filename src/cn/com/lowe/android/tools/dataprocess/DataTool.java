package cn.com.lowe.android.tools.dataprocess;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import cn.com.lowe.android.tools.dataprocess.annotation.FieldProperty;
import cn.com.lowe.android.tools.dataprocess.annotation.TipProperty;
import cn.com.lowe.android.tools.dataprocess.lang.ValidationResult;
import cn.com.lowe.android.tools.dataprocess.util.AnnotationUtil;
import cn.com.lowe.android.tools.dataprocess.util.ErrorTipUtil;
import cn.com.lowe.android.tools.dataprocess.util.ValidationUtil;
import cn.com.lowe.android.tools.dataprocess.util.ValueUtil;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

/**
 * 数据映射工具
 * 
 * @author zhengjin
 * 
 */
public class DataTool {
	private static final String TAG = "DataTool";

	private static boolean setError = true;
	// 校验取值对象缓存
	private static Map<Class<?>, IValue> valueInterfaceMap = new HashMap<Class<?>, IValue>();
	private static Map<Class<?>, IValidation> validationInterfaceMap = new HashMap<Class<?>, IValidation>();
	static {
		valueInterfaceMap.put(ValueUtil.class, new ValueUtil());
		validationInterfaceMap.put(ValidationUtil.class, new ValidationUtil());
	}

	public static ValidationResult validation(Class<?> entityClazz, View validView) {
		Resources res = validView.getResources();
		Field[] fields = entityClazz.getDeclaredFields();
		FieldProperty fieldAnno;
		TipProperty tipAnno;
		String fieldName;
		String tipName;
		Class<?> fieldClass;
		View fieldView;
		StringBuffer errorBuf = new StringBuffer();
		Map<String, String> entity = new HashMap<String, String>();
		for (Field field : fields) {
			fieldName = field.getName();
			fieldClass = field.getType();
			if (field.isAnnotationPresent(FieldProperty.class)) {
				fieldAnno = field.getAnnotation(FieldProperty.class);
				if (fieldAnno.mapToViewId() == 0) {
					Log.e(TAG, fieldName + ":未设置FieldProperty.mapToViewId属性，无法找到对应View");
					continue;
				}
				fieldView = validView.findViewById(fieldAnno.mapToViewId());
				if (fieldView == null) {
					Log.e(TAG, fieldName + ":根据FieldProperty.mapToViewId属性，无法找到对应View");
					continue;
				}
				tipAnno = field.getAnnotation(TipProperty.class);
				tipName = AnnotationUtil.parseTipProperty(tipAnno, res, fieldName);

				IValidation iValidation = validationInterfaceMap.get(fieldAnno.validationClass());

				// 避免重复创建默认校验对象
				if (iValidation == null) {
					try {
						iValidation = (IValidation) fieldAnno.validationClass().newInstance();
						validationInterfaceMap.put(fieldAnno.validationClass(), iValidation);
					} catch (InstantiationException e) {
						Log.e(TAG, fieldName + ":根据FieldProperty.validationClass属性，无参构造函数不存在");
						continue;
					} catch (IllegalAccessException e) {
						Log.e(TAG, fieldName + ":根据FieldProperty.validationClass属性，类不存在");
						continue;
					}

				}
				IValue iValue = valueInterfaceMap.get(fieldAnno.valueConstructClass());
				// 避免重复创建默认取值对象
				if (iValue == null) {
					try {
						iValue = (IValue) fieldAnno.valueConstructClass().newInstance();
						valueInterfaceMap.put(fieldAnno.valueConstructClass(), iValue);
					} catch (InstantiationException e) {
						Log.e(TAG, fieldName + ":根据FieldProperty.valueConstructClass属性，无参构造函数不存在");
						continue;
					} catch (IllegalAccessException e) {
						Log.e(TAG, fieldName + ":根据FieldProperty.valueConstructClass属性，类不存在");
						continue;
					}
				}

				String[] ss = iValidation.check(fieldName, fieldClass, fieldView, fieldAnno, tipName, iValue);
				if (ss != null && ss.length == 2) {
					errorBuf.append(ss[0]);
					entity.put(fieldName, ss[1]);
					if (setError && !TextUtils.isEmpty(ss[0])) {
						ErrorTipUtil.setError(fieldView, ss[0]);
					}
				}

			} else {
				Log.d(TAG, fieldName + ":未设置FieldProperty注解");
			}
		}
		ValidationResult vr = new ValidationResult();
		if (errorBuf.length() > 0) {
			vr.success = false;
			vr.note = errorBuf.toString();
		} else {
			vr.success = true;
			vr.entity = entity;
		}
		return vr;

	}

	public static Object getEntity(Class<?> entityClazz, View validView, ValidationResult validationResult) throws IllegalAccessException, InstantiationException {
		Field[] fields = entityClazz.getDeclaredFields();
		FieldProperty fieldAnno;
		String fieldName;
		Class<?> fieldClass;
		View fieldView;
		Map<String, String> entity = validationResult.entity;
		boolean entityIsNull = (entity == null);
		if (entityIsNull) {
			Log.d(TAG, entityClazz.getName() + "取值过程中未发现缓存数据");
		}
		Object o = null;
		try {
			o = entityClazz.newInstance();
		} catch (InstantiationException e) {
			Log.e(TAG, entityClazz.getName() + "无参构造函数不存在");
			throw e;
		} catch (IllegalAccessException e) {
			Log.e(TAG, entityClazz.getName() + "类不存在");
			throw e;
		}
		for (Field field : fields) {
			fieldName = field.getName();
			fieldClass = field.getType();
			if (field.isAnnotationPresent(FieldProperty.class)) {
				fieldAnno = field.getAnnotation(FieldProperty.class);
				if (fieldAnno.mapToViewId() == 0) {
					Log.e(TAG, fieldName + ":未设置FieldProperty.mapToViewId属性，无法找到对应View");
					continue;
				}
				fieldView = validView.findViewById(fieldAnno.mapToViewId());
				if (fieldView == null) {
					Log.e(TAG, fieldName + ":根据FieldProperty.mapToViewId属性，无法找到对应View");
					continue;
				}
				IValue iValue = valueInterfaceMap.get(fieldAnno.valueConstructClass());
				// 避免重复创建默认取值对象
				if (iValue == null) {
					try {
						iValue = (IValue) fieldAnno.valueConstructClass().newInstance();
						valueInterfaceMap.put(fieldAnno.valueConstructClass(), iValue);
					} catch (InstantiationException e) {
						Log.e(TAG, fieldName + ":根据FieldProperty.valueConstructClass属性，无参构造函数不存在");
						continue;
					} catch (IllegalAccessException e) {
						Log.e(TAG, fieldName + ":根据FieldProperty.valueConstructClass属性，类不存在");
						continue;
					}
				}
				Object value = null;
				try {
					if (entityIsNull) {
						value = iValue.getValue(fieldView, fieldClass);
					} else if (entity.get(fieldName) != null) {
						value = iValue.getValueByStr(entity.get(fieldName), fieldClass);
					} else {
						value = iValue.getValue(fieldView, fieldClass);
					}
				} catch (Exception e) {
					Log.e(TAG, fieldName + "内容不可转换为" + fieldClass + "对象", e);
				}
				field.setAccessible(true);
				field.set(o, value);
			} else {
				Log.d(TAG, fieldName + ":未设置FieldProperty注解");
			}

		}
		return o;

	}

	public static void setView(Object entity, Class<?> entityClazz, View validView) {
		Field[] fields = entityClazz.getDeclaredFields();
		FieldProperty fieldAnno;
		String fieldName;
		View fieldView;
		for (Field field : fields) {
			fieldName = field.getName();
			if (field.isAnnotationPresent(FieldProperty.class)) {
				fieldAnno = field.getAnnotation(FieldProperty.class);
				if (fieldAnno.mapToViewId() == 0) {
					Log.e(TAG, fieldName + ":未设置FieldProperty.mapToViewId属性，无法找到对应View");
					continue;
				}
				fieldView = validView.findViewById(fieldAnno.mapToViewId());
				if (fieldView == null) {
					Log.e(TAG, fieldName + ":根据FieldProperty.mapToViewId属性，无法找到对应View");
					continue;
				}
				IValue iValue = valueInterfaceMap.get(fieldAnno.valueConstructClass());
				// 避免重复创建默认取值对象
				if (iValue == null) {
					try {
						iValue = (IValue) fieldAnno.valueConstructClass().newInstance();
						valueInterfaceMap.put(fieldAnno.valueConstructClass(), iValue);
					} catch (InstantiationException e) {
						Log.e(TAG, fieldName + ":根据FieldProperty.valueConstructClass属性，无参构造函数不存在");
						continue;
					} catch (IllegalAccessException e) {
						Log.e(TAG, fieldName + ":根据FieldProperty.valueConstructClass属性，类不存在");
						continue;
					}
				}
				Object o = null;
				field.setAccessible(true);
				try {
					o = field.get(entity);
				} catch (IllegalArgumentException e) {
					Log.e(TAG, fieldName + "属性不存在");
					continue;
				} catch (IllegalAccessException e) {
					Log.e(TAG, fieldName + "属性不可见");
					continue;
				}
				iValue.setValue(o, fieldView, fieldAnno.arrySplitFlag());
			} else {
				Log.d(TAG, fieldName + ":未设置FieldProperty注解");
			}

		}
	}
}
